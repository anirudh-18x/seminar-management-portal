package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBUtil — Database Connection Utility
 *
 * This class provides a static method to obtain a JDBC Connection
 * to the MySQL database. It reads credentials from db.properties
 * so that passwords are never hardcoded in source code.
 *
 * How it works:
 *  1. The static block runs once when the class is first loaded.
 *  2. It reads db.url, db.username, and db.password from db.properties.
 *  3. getConnection() returns a new Connection every time it is called.
 *  4. Always close the Connection after use (use try-with-resources).
 */
public class DBUtil {

    private static String url;
    private static String username;
    private static String password;

    // Static initializer: runs once when the class is loaded.
    static {
        try {
            // Load the MySQL JDBC driver (required for older JDBC versions;
            // harmless with JDBC 4+ which auto-discovers the driver).
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Load properties from db.properties placed in the classpath root.
            Properties props = new Properties();
            InputStream in = DBUtil.class.getClassLoader()
                                         .getResourceAsStream("db.properties");
            if (in == null) {
                throw new RuntimeException(
                    "db.properties not found in classpath. " +
                    "Place it in the WEB-INF/classes/ directory.");
            }
            props.load(in);
            in.close();

            url      = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                "Failed to initialize DBUtil: " + e.getMessage());
        }
    }

    /**
     * Returns a new JDBC Connection.
     * Always close this connection after use, preferably with try-with-resources:
     *
     *   try (Connection con = DBUtil.getConnection()) {
     *       // ... use con ...
     *   }
     *
     * @return a live Connection to the seminar_portal database
     * @throws SQLException if the connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Private constructor — this class should never be instantiated.
    private DBUtil() {}
}
