package utils;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Connection successful!");
            } else {
                System.out.println(" Connection failed: Connection is null or closed.");
            }
        } catch (SQLException e) {
            System.out.println(" Connection failed.");
            e.printStackTrace();
        }
    }
}
