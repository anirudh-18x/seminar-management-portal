package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Event {
    private int       eventId;
    private String    title;
    private String    description;
    private String    eventType;
    private String    department;
    private Date      eventDate;
    private Time      eventTime;
    private int       durationHours = 1;
    private String    venue;
    private String    speakerName;
    private int       totalSeats;
    private Timestamp createdAt;

    public Event() {}

    public int getEventId()              { return eventId; }
    public String getTitle()             { return title; }
    public String getDescription()       { return description; }
    public String getEventType()         { return eventType; }
    public String getDepartment()        { return department; }
    public Date getEventDate()           { return eventDate; }
    public Time getEventTime()           { return eventTime; }
    public int getDurationHours()        { return durationHours; }
    public String getVenue()             { return venue; }
    public String getSpeakerName()       { return speakerName; }
    public int getTotalSeats()           { return totalSeats; }
    public Timestamp getCreatedAt()      { return createdAt; }

    public void setEventId(int eventId)                   { this.eventId = eventId; }
    public void setTitle(String title)                    { this.title = title; }
    public void setDescription(String description)        { this.description = description; }
    public void setEventType(String eventType)            { this.eventType = eventType; }
    public void setDepartment(String department)          { this.department = department; }
    public void setEventDate(Date eventDate)              { this.eventDate = eventDate; }
    public void setEventTime(Time eventTime)              { this.eventTime = eventTime; }
    public void setDurationHours(int durationHours)       { this.durationHours = durationHours; }
    public void setVenue(String venue)                    { this.venue = venue; }
    public void setSpeakerName(String speakerName)        { this.speakerName = speakerName; }
    public void setTotalSeats(int totalSeats)             { this.totalSeats = totalSeats; }
    public void setCreatedAt(Timestamp createdAt)         { this.createdAt = createdAt; }
}