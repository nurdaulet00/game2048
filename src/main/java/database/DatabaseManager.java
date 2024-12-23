package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/2048";
    private static final String USER = "root";
    private static final String PASSWORD = "Geekin2024";
    private Connection connection;

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                System.out.println("Connected to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

    public void saveScore(String playerName, int score) {
        connect();

        String query = "INSERT INTO score (idplayer, score, player_name) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String getUserIdQuery = "SELECT iduser FROM user WHERE name = ?";
            try (PreparedStatement userStmt = connection.prepareStatement(getUserIdQuery)) {
                userStmt.setString(1, playerName);
                ResultSet resultSet = userStmt.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt("iduser");
                    stmt.setInt(1, userId);
                    stmt.setInt(2, score);
                    stmt.setString(3, playerName);
                    stmt.executeUpdate();
                } else {
                    System.out.println("Player not found in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTopScores(int limit) {
        connect();

        String query = "SELECT player_name, score FROM score ORDER BY score DESC LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("player_name") + " - " + resultSet.getInt("score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

