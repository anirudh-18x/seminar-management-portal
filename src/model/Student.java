package model;

/**
 * Student JavaBean
 *
 * Represents a student stored in the 'students' table.
 * Used to carry student information between DAO, Servlet, and JSP.
 */
public class Student {

    private int    studentId;
    private String fullName;
    private String email;
    private String rollNumber;
    private String department;
    private String phone;

    // No-argument constructor (required for JavaBean)
    public Student() {}

    // --- Getters ---

    public int getStudentId()       { return studentId; }
    public String getFullName()     { return fullName; }
    public String getEmail()        { return email; }
    public String getRollNumber()   { return rollNumber; }
    public String getDepartment()   { return department; }
    public String getPhone()        { return phone; }

    // --- Setters ---

    public void setStudentId(int studentId)       { this.studentId = studentId; }
    public void setFullName(String fullName)      { this.fullName = fullName; }
    public void setEmail(String email)            { this.email = email; }
    public void setRollNumber(String rollNumber)  { this.rollNumber = rollNumber; }
    public void setDepartment(String department)  { this.department = department; }
    public void setPhone(String phone)            { this.phone = phone; }

    @Override
    public String toString() {
        return "Student{id=" + studentId + ", name='" + fullName + "', roll='" + rollNumber + "'}";
    }
}
