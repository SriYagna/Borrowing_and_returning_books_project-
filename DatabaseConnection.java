import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A utility class for managing database connections.
 * This class provides a method to establish a connection
 * to the MySQL database using JDBC.
 */
public class DatabaseConnection {
    // Database connection URL. Replace 'library_db' with your actual database name.
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";

    // Database username. Replace 'root' with your actual username if different.
    private static final String USER = "root";

    // Database password. Replace with your actual password.
    private static final String PASSWORD = "Sriyagna@573";

    /**
     * Establishes a connection to the MySQL database.
     * 
     * @return A Connection object to the database.
     * @throws SQLException If a database access error occurs or the URL is
     *                      incorrect.
     */
    public static Connection getConnection() throws SQLException {
        // Use DriverManager to create a connection to the database using the specified
        // URL, username, and password.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
