package dao;

import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {
    public List<String> getAllVenues() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT venue_name FROM venues ORDER BY venue_name ASC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("venue_name"));
            }
        }
        return list;
    }

    public boolean addVenue(String name) throws SQLException {
        String sql = "INSERT INTO venues (venue_name) VALUES (?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // Already exists
        }
    }
}