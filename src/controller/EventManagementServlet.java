package controller;

import dao.EventDAO;
import dao.VenueDAO;
import model.Event;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

/**
 * EventManagementServlet ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â Admin: Add, Edit, and Delete events
 *
 * URL mappings:
 *   /admin/add-event    ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â show add form (GET) / process add (POST)
 *   /admin/edit-event   ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â show edit form (GET) / process edit (POST)
 *   /admin/delete-event ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â process deletion (GET with confirmation)
 *
 * Each action checks for a valid admin session before proceeding.
 */
@WebServlet(urlPatterns = {"/admin/add-event", "/admin/edit-event", "/admin/delete-event"})
public class EventManagementServlet extends HttpServlet {

    private EventDAO eventDAO = new EventDAO();
    private VenueDAO venueDAO = new VenueDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String uri = request.getRequestURI();

        if (uri.endsWith("/add-event")) {
            // Show blank add-event form
            try { request.setAttribute("venues", venueDAO.getAllVenues()); } catch (SQLException e) { } request.getRequestDispatcher("/add-event.jsp").forward(request, response);

        } else if (uri.endsWith("/edit-event")) {
            // Load event data for editing
            String idParam = request.getParameter("eventId");
            if (idParam == null) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }
            try {
                Event event = eventDAO.getEventById(Integer.parseInt(idParam));
                if (event == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
                }
                request.setAttribute("event", event); try { request.setAttribute("venues", venueDAO.getAllVenues()); } catch (SQLException e) { }
                request.getRequestDispatcher("/edit-event.jsp").forward(request, response);
            } catch (SQLException | NumberFormatException e) {
                throw new ServletException("Error loading event: " + e.getMessage(), e);
            }

        } else if (uri.endsWith("/delete-event")) {
            // Delete the event
            String idParam = request.getParameter("eventId");
            if (idParam != null) {
                try {
                    eventDAO.deleteEvent(Integer.parseInt(idParam));
                } catch (SQLException | NumberFormatException e) {
                    throw new ServletException("Error deleting event: " + e.getMessage(), e);
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/dashboard?deleted=true");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();

        try {
            Event event = buildEventFromRequest(request);
            String error = validateEvent(event);

            if (error != null) {
                request.setAttribute("errorMessage", error);
                if (uri.endsWith("/add-event")) {
                    request.setAttribute("formData", event); try { request.setAttribute("venues", venueDAO.getAllVenues()); } catch (SQLException e) { }
                    try { request.setAttribute("venues", venueDAO.getAllVenues()); } catch (SQLException e) { } request.getRequestDispatcher("/add-event.jsp").forward(request, response);
                } else {
                    request.setAttribute("event", event); try { request.setAttribute("venues", venueDAO.getAllVenues()); } catch (SQLException e) { }
                    request.getRequestDispatcher("/edit-event.jsp").forward(request, response);
                }
                return;
            }

            if (uri.endsWith("/add-event")) {
                eventDAO.addEvent(event);
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?added=true");

            } else if (uri.endsWith("/edit-event")) {
                eventDAO.updateEvent(event);
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?updated=true");
            }

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error saving event: " + e.getMessage(), e);
        }
    }

    // Build an Event object from request parameters
    private Event buildEventFromRequest(HttpServletRequest request) throws NumberFormatException {
        Event e = new Event();

        String idStr = request.getParameter("eventId");
        if (idStr != null && !idStr.isEmpty()) {
            e.setEventId(Integer.parseInt(idStr));
        }

        e.setTitle      (trim(request.getParameter("title")));
        e.setDescription(trim(request.getParameter("description")));
        e.setEventType  (trim(request.getParameter("eventType")));
        e.setDepartment (trim(request.getParameter("department")));
        e.setVenue      (trim(request.getParameter("venue")));
        e.setSpeakerName(trim(request.getParameter("speakerName")));

        String dateStr  = trim(request.getParameter("eventDate"));
        String timeStr  = trim(request.getParameter("eventTime"));
        String seatsStr = trim(request.getParameter("totalSeats"));

        if (!dateStr.isEmpty())  e.setEventDate(Date.valueOf(dateStr));
        if (!timeStr.isEmpty())  e.setEventTime(Time.valueOf(timeStr + ":00"));
        String durationStr = trim(request.getParameter("durationHours"));
        if (!durationStr.isEmpty()) e.setDurationHours(Integer.parseInt(durationStr));
        if (!seatsStr.isEmpty()) e.setTotalSeats(Integer.parseInt(seatsStr));

        return e;
    }

    // Validate required event fields
    private String validateEvent(Event e) throws SQLException {
        if (e.getTitle()       == null || e.getTitle().isEmpty())       return "Event title is required.";
        if (e.getEventType()   == null || e.getEventType().isEmpty())   return "Event type is required.";
        if (e.getDepartment()  == null || e.getDepartment().isEmpty())  return "Department is required.";
        if (e.getVenue()       == null || e.getVenue().isEmpty())       return "Venue is required.";
        if (e.getSpeakerName() == null || e.getSpeakerName().isEmpty()) return "Speaker name is required.";
        if (e.getEventDate()   == null)                                 return "Event date is required.";
        if (e.getEventTime()   == null)                                 return "Event time is required.";
        if (e.getTotalSeats()  <= 0)                                    return "Total seats must be greater than zero.";
        if (eventDAO.isVenueOccupied(e.getVenue(), e.getEventDate(), e.getEventTime(), e.getDurationHours(), e.getEventId())) {
            return "This venue is already booked for another event on this day within 1 hour of the selected time.";
        }
        return null; // no error
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
