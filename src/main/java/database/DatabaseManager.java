package database;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/2048";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Geekin2024";

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public void saveScore(String playerName, int score) {
        String query = "INSERT INTO scores (player_name, score) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();
            System.out.println("Score saved successfully.");
        } catch (SQLException e) {
            System.err.println("Error while saving score.");
            e.printStackTrace();
        }
    }

    public void getTopScores(int limit) {
        String query = "SELECT player_name, score FROM scores ORDER BY score DESC LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                System.out.println(playerName + " - " + score);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching top scores.");
            e.printStackTrace();
        }
    }
}

