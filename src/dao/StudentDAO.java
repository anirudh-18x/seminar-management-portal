package dao;

import model.Student;
import util.DBUtil;
import java.sql.*;

public class StudentDAO {
    public Student findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM students WHERE email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapRow(rs); }
        }
        return null;
    }
    public Student findByRollNumber(String rollNumber) throws SQLException {
        String sql = "SELECT * FROM students WHERE roll_number = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNumber);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapRow(rs); }
        }
        return null;
    }
    public Student findById(int studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapRow(rs); }
        }
        return null;
    }
    public int insertStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (full_name, email, roll_number, department, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, student.getFullName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getRollNumber());
            ps.setString(4, student.getDepartment());
            ps.setString(5, student.getPhone());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) return keys.getInt(1); }
            }
        }
        return -1;
    }
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET full_name = ?, email = ?, department = ?, phone = ? WHERE student_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, student.getFullName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getDepartment());
            ps.setString(4, student.getPhone());
            ps.setInt(5, student.getStudentId());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        }
    }
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setDepartment(rs.getString("department"));
        s.setPhone(rs.getString("phone"));
        return s;
    }
}