package model;

import java.sql.Timestamp;

/**
 * Registration JavaBean
 *
 * Represents a row in the 'registrations' table.
 * Links a Student to an Event and stores the registration timestamp.
 *
 * This bean also carries the joined student and event data
 * for display purposes (populated by RegistrationDAO queries).
 */
public class Registration {

    private int       registrationId;
    private int       studentId;
    private int       eventId;
    private Timestamp registeredAt;

    // Joined fields â€” for display in JSP views
    private String studentName;
    private String studentEmail;
    private String rollNumber;
    private String studentDepartment;
    private String studentPhone;
    private String eventTitle;
    private java.sql.Date eventDate;
    private java.sql.Time eventTime;
    private String venue;

    // No-argument constructor (required for JavaBean)
    public Registration() {}

    // --- Getters ---

    public int getRegistrationId()     { return registrationId; }
    public int getStudentId()          { return studentId; }
    public int getEventId()            { return eventId; }
    public Timestamp getRegisteredAt() { return registeredAt; }
    public String getStudentName()     { return studentName; }
    public String getStudentEmail()    { return studentEmail; }
    public String getRollNumber()      { return rollNumber; }
    public String getStudentDepartment(){ return studentDepartment; }
    public String getStudentPhone()    { return studentPhone; }
    public String getEventTitle()      { return eventTitle; }
    public java.sql.Date getEventDate() { return eventDate; }
    public java.sql.Time getEventTime() { return eventTime; }
    public String getVenue()           { return venue; }

    // --- Setters ---

    public void setRegistrationId(int registrationId)         { this.registrationId = registrationId; }
    public void setStudentId(int studentId)                   { this.studentId = studentId; }
    public void setEventId(int eventId)                       { this.eventId = eventId; }
    public void setRegisteredAt(Timestamp registeredAt)       { this.registeredAt = registeredAt; }
    public void setStudentName(String studentName)            { this.studentName = studentName; }
    public void setStudentEmail(String studentEmail)          { this.studentEmail = studentEmail; }
    public void setRollNumber(String rollNumber)              { this.rollNumber = rollNumber; }
    public void setStudentDepartment(String studentDepartment){ this.studentDepartment = studentDepartment; }
    public void setStudentPhone(String studentPhone)          { this.studentPhone = studentPhone; }
    public void setEventTitle(String eventTitle)              { this.eventTitle = eventTitle; }
    public void setEventDate(java.sql.Date eventDate)         { this.eventDate = eventDate; }
    public void setEventTime(java.sql.Time eventTime)         { this.eventTime = eventTime; }
    public void setVenue(String venue)                        { this.venue = venue; }
}
