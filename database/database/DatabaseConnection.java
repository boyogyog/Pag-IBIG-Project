package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a reusable database connection for all DAOs.
 * Call DatabaseConnection.getConnection() anywhere you need a Connection.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/pagibig";
    private static final String USERNAME = "root";
    // private static final String PASSWORD = "adminuser";
    private static final String PASSWORD = "adminuser";
    //private static final String PASSWORD = "yeshua26.";

    /**
     * Returns a new Connection to the pagibig database.
     * Always close it after use (DAOs handle this via try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Quick connection test — run this main() to verify the DB is reachable.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to MySQL Successfully!");
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}
