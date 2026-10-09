package dao;

import model.Event;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public boolean addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (title, description, event_type, department, " +
                     "event_date, event_time, duration_hours, venue, speaker_name, total_seats) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getEventType());
            ps.setString(4, event.getDepartment());
            ps.setDate  (5, event.getEventDate());
            ps.setTime  (6, event.getEventTime());
            ps.setInt   (7, event.getDurationHours());
            ps.setString(8, event.getVenue());
            ps.setString(9, event.getSpeakerName());
            ps.setInt   (10, event.getTotalSeats());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date ASC, event_time ASC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { list.add(mapRow(rs)); }
        }
        return list;
    }

    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET title=?, description=?, event_type=?, department=?, " +
                     "event_date=?, event_time=?, duration_hours=?, venue=?, speaker_name=?, total_seats=? " +
                     "WHERE event_id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getEventType());
            ps.setString(4, event.getDepartment());
            ps.setDate  (5, event.getEventDate());
            ps.setTime  (6, event.getEventTime());
            ps.setInt   (7, event.getDurationHours());
            ps.setString(8, event.getVenue());
            ps.setString(9, event.getSpeakerName());
            ps.setInt   (10, event.getTotalSeats());
            ps.setInt   (11, event.getEventId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;
        }
    }

    public int getAvailableSeats(int eventId) throws SQLException {
        String sql = "SELECT " +
                     "  (SELECT total_seats FROM events WHERE event_id = ?) - " +
                     "  (SELECT COUNT(*) FROM registrations WHERE event_id = ?) AS available";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int avail = rs.getInt("available");
                    return Math.max(avail, 0);
                }
            }
        }
        return 0;
    }

    private Event mapRow(ResultSet rs) throws SQLException {
        Event e = new Event();
        e.setEventId     (rs.getInt("event_id"));
        e.setTitle       (rs.getString("title"));
        e.setDescription (rs.getString("description"));
        e.setEventType   (rs.getString("event_type"));
        e.setDepartment  (rs.getString("department"));
        e.setEventDate   (rs.getDate("event_date"));
        e.setEventTime   (rs.getTime("event_time"));
        e.setDurationHours(rs.getInt("duration_hours"));
        e.setVenue       (rs.getString("venue"));
        e.setSpeakerName (rs.getString("speaker_name"));
        e.setTotalSeats  (rs.getInt("total_seats"));
        e.setCreatedAt   (rs.getTimestamp("created_at"));
        return e;
    }

    public boolean isVenueOccupied(String venue, java.sql.Date date, java.sql.Time time, int durationHours, int excludeEventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE venue = ? AND event_date = ? " +
                     "AND event_time < ADDTIME(?, SEC_TO_TIME(? * 3600)) " +
                     "AND ? < ADDTIME(event_time, SEC_TO_TIME(duration_hours * 3600))";
        if (excludeEventId > 0) {
            sql += " AND event_id != ?";
        }
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, venue);
            ps.setDate(2, date);
            ps.setTime(3, time);
            ps.setInt(4, durationHours);
            ps.setTime(5, time);
            if (excludeEventId > 0) {
                ps.setInt(6, excludeEventId);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}