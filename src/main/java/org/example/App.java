package org.example;

import java.util.Scanner;
import java.util.Random;
import database.DatabaseManager;

public class App {

    public static final int SIZE = 4;
    public static final char MOVE_LEFT = 'A';
    public static final char MOVE_RIGHT = 'D';
    public static final char MOVE_UP = 'W';
    public static final char MOVE_DOWN = 'S';

    private int board[][];
    private Random random;
    private Scanner scanner;
    private DatabaseManager dbManager;

    public App() {
        board = new int[4][4];
        random = new Random();
        scanner = new Scanner(System.in);
        dbManager = new DatabaseManager();
    }

    public void showBoard() {
        for( int i=0; i<4; i++ ) {
            System.out.print("-------");
        }
        System.out.println();

        for( int i=0; i<4; i++ ) {
            System.out.print("|");
            for( int j=0; j<4; j++ ) {
                System.out.print("      |");
            }
            System.out.println();

            System.out.print("|");
            for( int j=0; j<4; j++ ) {
                if( board[i][j]==0 ) {
                    System.out.printf("  %-3s |", "");
                }
                else {
                    System.out.printf("  %-3s |", "" + board[i][j]);
                }
            }
            System.out.println();

            System.out.print("|");
            for( int j=0; j<4; j++ ) {
                System.out.print("      |");
            }
            System.out.println();

            for( int j=0; j<4; j++ ) {
                System.out.print("-------");
            }
            System.out.println();
        }
    }

    public void addRandomDigit(int digit) {
        int i = random.nextInt(4);
        int j = random.nextInt(4);

        while (board[i][j] != 0) {
            i = random.nextInt(4);
            j = random.nextInt(4);
        }
        board[i][j] = digit;
    }

    public boolean searchOnBoard(int x) {
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                if (board[i][j] == x) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean gameWon() {
        return searchOnBoard(2048);
    }

    public boolean userCanMakeAMove() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == board[i][j+1] ||
                        board[i][j] == board[i+1][j]
                ) {
                    return true;
                }
            }
        }
        for (int j=0; j<3; j++) {
            if (board[3][j] == board[3][j+1]) {
                return true;
            }
        }
        for (int i=0; i<3; i++) {
            if (board[i][3] == board[i+1][3]) {
                return true;
            }
        }

        return false;
    }

    public boolean isGameOver() {
        if (gameWon()) {
            return true;
        }

        if (searchOnBoard(0)) {
            return false;
        }

        return !userCanMakeAMove();
    }

    public char getUserMove() {
        System.out.println("Choose a move: ");
        System.out.println("W is up");
        System.out.println("S is down");
        System.out.println("A is left");
        System.out.println("D is right");
        System.out.println("Q to quit the game");
        System.out.print("Enter your move: ");

        String moveInput = scanner.nextLine();
        if (moveInput.equalsIgnoreCase("a") ||
                moveInput.equalsIgnoreCase("w") ||
                moveInput.equalsIgnoreCase("s") ||
                moveInput.equalsIgnoreCase("d") ||
                moveInput.equalsIgnoreCase("q")
        ) {
            return moveInput.toUpperCase().charAt(0);
        }

        System.out.println("Invalid input");
        System.out.print("");
        showBoard();
        return getUserMove();
    }

    public int[] processLeftMove(int row[]) {
        int newRow[] = new int[4];
        int j = 0;
        for (int i=0; i<4; i++) {
            if (row[i]!=0) {
                newRow[j++] = row[i];
            }
        }

        for (int i=0; i<3; i++) {
            if (newRow[i]!=0 && newRow[i]==newRow[i+1]) {
                newRow[i] = 2*newRow[i];
                for (j=i+1; j<3; j++) {
                    newRow[j] = newRow[j+1];
                }
                newRow[3] = 0;
            }
        }
        return newRow;
    }

    public int[] reverseArray(int arr[]) {
        int[] reverseArr = new int[arr.length];
        for (int i=arr.length-1; i>=0; i--) {
            reverseArr[i] = arr[arr.length-i-1];
        }
        return reverseArr;
    }

    public int[] processRightMove(int row[]) {
        int newRow[] = new int[4];
        int j = 0;
        for (int i=0; i<4; i++) {
            if (row[i]!=0) {
                newRow[j++] = row[i];
            }
        }
        newRow = reverseArray(newRow);
        newRow =processLeftMove(newRow);
        return reverseArray(newRow);
    }

    public void processMove(char move) {
        switch (move) {
            case MOVE_LEFT:
            {
                for (int i=0; i<4; i++) {
                    int newRow[] = processLeftMove(board[i]);
                    for (int j=0; j<4; j++) {
                        board[i][j] = newRow[j];
                    }
                }
            }
            break;
            case MOVE_RIGHT:
            {
                for (int i=0; i<4; i++) {
                    int newRow[] = processRightMove(board[i]);
                    for (int j=0; j<4; j++) {
                        board[i][j] = newRow[j];
                    }
                }
            }
            break;
            case MOVE_UP:
            {
                for (int j=0; j<4; j++) {
                    int row[] = new int[4];
                    for (int i=0; i<4; i++) {
                        row[i] = board[i][j];
                    }
                    int newRow[] = processLeftMove(row);
                    for (int i=0; i<4; i++) {
                        board[i][j] = newRow[i];
                    }
                }
            }
            break;
            case MOVE_DOWN:
            {
                for (int j=0; j<4; j++) {
                    int row[] = new int[4];
                    for (int i=0; i<4; i++) {
                        row[i] = board[i][j];
                    }
                    int newRow[] = processRightMove(row);
                    for (int i=0; i<4; i++) {
                        board[i][j] = newRow[i];
                    }
                }
            }
            break;
        }
    }

    public void play() {
        addRandomDigit(2);
        addRandomDigit(4);

        String playerName = "Player 1";
        int finalScore = 0;

        while (!isGameOver()) {
            showBoard();
            char move = getUserMove();

            if (move == 'Q') {
                System.out.println("Game aborted by the player.");
                break;
            }

            processMove(move);

            int r = random.nextInt(100);
            if (r%2==0) {
                addRandomDigit(2);
            }
            else {
                addRandomDigit(4);
            }
        }

        if (gameWon()) {
            System.out.println("Congratulations! You won!");
        }
        else {
            System.out.println("You lost!");
        }

        endGame(playerName, finalScore);
    }

    static void showArray(int arr[]) {
        System.out.print("[");
        for (int i: arr) {
            System.out.print(i+" ");
        }
        System.out.println("]");
    }

    public static void main (String args[]) {
        App consoleApp = new App();
        consoleApp.play();
    }

    public void endGame(String playerName, int finalScore) {
        System.out.println("Game over! Saving your score...");
        dbManager.saveScore(playerName, finalScore);
        System.out.println("Top scores:");
        dbManager.getTopScores(5);
    }
}


