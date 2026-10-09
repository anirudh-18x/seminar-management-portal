-- ============================================================
-- Stored Procedure Script (standalone)
-- Run this if the procedure was not created by schema.sql
-- ============================================================

USE seminar_portal;

DROP PROCEDURE IF EXISTS GetRegistrationsByEvent;

DELIMITER $$
CREATE PROCEDURE GetRegistrationsByEvent(IN p_event_id INT)
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

-- Test the procedure:
-- CALL GetRegistrationsByEvent(1);
