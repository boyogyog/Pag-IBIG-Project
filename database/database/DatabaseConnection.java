package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class DatabaseConnection {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/pagibig";
        String username = "root";
        String password = "adminuser";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL Successfully!");

            // Run a query
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM companydetailstable");

            while (rs.next()) {
                System.out.println(rs.getString(1)); // prints first column
            }

            connection.close();
        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}