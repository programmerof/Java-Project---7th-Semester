package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Singleton utility to get a MySQL connection.
 * Change DB_URL, DB_USER, DB_PASS to match your local MySQL setup.
 */
public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/online_exam_db?useSSL=false&serverTimezone=UTC&connectTimeout=5000&socketTimeout=10000&allowPublicKeyRetrieval=true";

    private static final String DB_USER = "root";        // change if needed
    private static final String DB_PASS = "qwerty";            // change if needed

    private static Connection connection = null;

    // Private constructor — no instantiation
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j.jar to your classpath.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
