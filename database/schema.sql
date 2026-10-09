-- ============================================================
-- College Seminar and Workshop Management Portal
-- Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS seminar_portal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE seminar_portal;

-- ============================================================
-- Table: events
-- Stores seminar and workshop details.
-- ============================================================
CREATE TABLE IF NOT EXISTS events (
    event_id      INT          AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    description   TEXT,
    event_type    ENUM('Seminar','Workshop') NOT NULL DEFAULT 'Seminar',
    department    VARCHAR(100) NOT NULL,
    event_date    DATE         NOT NULL,
    event_time    TIME         NOT NULL,
    venue         VARCHAR(200) NOT NULL,
    speaker_name  VARCHAR(150) NOT NULL,
    total_seats   INT          NOT NULL DEFAULT 50,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table: students
-- Stores basic student details collected at registration.
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
    student_id   INT          AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(150) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    roll_number  VARCHAR(50)  NOT NULL UNIQUE,
    department   VARCHAR(100) NOT NULL,
    phone        VARCHAR(15),
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table: registrations
-- Connects students to events.
-- The UNIQUE constraint on (student_id, event_id) prevents
-- the same student from registering for the same event twice.
-- ============================================================
CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT       AUTO_INCREMENT PRIMARY KEY,
    student_id      INT       NOT NULL,
    event_id        INT       NOT NULL,
    registered_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_event   FOREIGN KEY (event_id)   REFERENCES events(event_id)   ON DELETE CASCADE,
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id)
);

-- ============================================================
-- Table: admins
-- Stores administrator login credentials (hashed passwords).
-- ============================================================
CREATE TABLE IF NOT EXISTS admins (
    admin_id       INT          AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Stored Procedure: GetRegistrationsByEvent
-- Returns all students registered for a given event.
-- Demonstrates CallableStatement usage from the syllabus.
-- ============================================================
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS GetRegistrationsByEvent(IN p_event_id INT)
BEGIN
    SELECT
        s.student_id,
        s.full_name,
        s.email,
        s.roll_number,
        s.department,
        s.phone,
        r.registered_at
    FROM registrations r
    JOIN students s ON r.student_id = s.student_id
    WHERE r.event_id = p_event_id
    ORDER BY r.registered_at ASC;
END$$
DELIMITER ;


