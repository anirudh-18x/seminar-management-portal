package dao;

import util.DBUtil;
import java.sql.*;

/**
 * AdminDAO — Data Access Object for administrator authentication
 *
 * Passwords are now stored in plain text as requested.
 */
public class AdminDAO {

    /**
     * Validates an admin login attempt.
     */
    public boolean validateAdmin(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ? AND password = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether an admin username already exists.
     */
    public boolean adminExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Creates a new admin account with a plain text password.
     */
    public boolean createAdmin(String username, String password) throws SQLException {
        String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeUpdate() > 0;
        }
    }
}
