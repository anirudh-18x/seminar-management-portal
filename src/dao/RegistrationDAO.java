package dao;

import model.Registration;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {

    public boolean register(int studentId, int eventId) throws SQLException {
        String sql = "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isAlreadyRegistered(int studentId, int eventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registrations WHERE student_id = ? AND event_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public List<Registration> getRegistrationsByStudent(int studentId) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT r.registration_id, r.student_id, r.event_id, r.registered_at, " +
                     "e.title AS event_title, e.event_date, e.event_time, e.venue " +
                     "FROM registrations r " +
                     "JOIN events e ON r.event_id = e.event_id " +
                     "WHERE r.student_id = ? " +
                     "ORDER BY r.registered_at DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Registration reg = new Registration();
                    reg.setRegistrationId(rs.getInt("registration_id"));
                    reg.setStudentId     (rs.getInt("student_id"));
                    reg.setEventId       (rs.getInt("event_id"));
                    reg.setRegisteredAt  (rs.getTimestamp("registered_at"));
                    reg.setEventTitle    (rs.getString("event_title"));
                    reg.setEventDate     (rs.getDate("event_date"));
                    reg.setEventTime     (rs.getTime("event_time"));
                    reg.setVenue         (rs.getString("venue"));
                    list.add(reg);
                }
            }
        }
        return list;
    }

    public List<Registration> getRegistrationsByEvent(int eventId) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = "{CALL GetRegistrationsByEvent(?)}";
        try (Connection con = DBUtil.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, eventId);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Registration reg = new Registration();
                    reg.setStudentId       (rs.getInt("student_id"));
                    reg.setStudentName     (rs.getString("full_name"));
                    reg.setStudentEmail    (rs.getString("email"));
                    reg.setRollNumber      (rs.getString("roll_number"));
                    reg.setStudentDepartment(rs.getString("department"));
                    reg.setStudentPhone    (rs.getString("phone"));
                    reg.setRegisteredAt    (rs.getTimestamp("registered_at"));
                    list.add(reg);
                }
            }
        }
        return list;
    }

    public boolean unregister(int studentId, int eventId) throws SQLException {
        String sql = "DELETE FROM registrations WHERE student_id = ? AND event_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        }
    }

    public int getRegistrationCount(int eventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registrations WHERE event_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}