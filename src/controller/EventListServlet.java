package controller;

import dao.EventDAO;
import dao.RegistrationDAO;
import model.Event;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * EventListServlet — Controller for the student-facing event list
 *
 * URL: /events
 *
 * doGet():
 *   - Fetches all events from the database via EventDAO.
 *   - Adds seat count information for each event.
 *   - Stores the list in request scope and forwards to events.jsp.
 *
 * This demonstrates:
 *   - Servlet doGet() handling.
 *   - Calling DAO from Servlet.
 *   - Storing data in request attributes (request.setAttribute).
 *   - Forwarding to a JSP view (RequestDispatcher.forward).
 */
@WebServlet("/events")
public class EventListServlet extends HttpServlet {

    private EventDAO eventDAO = new EventDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Event> events = eventDAO.getAllEvents();

            // Attach available seats count to each event — stored as a request attribute
            int[] availableSeats = new int[events.size()];
            int[] registeredCount = new int[events.size()];
            for (int i = 0; i < events.size(); i++) {
                int eid = events.get(i).getEventId();
                availableSeats[i]   = eventDAO.getAvailableSeats(eid);
                registeredCount[i]  = registrationDAO.getRegistrationCount(eid);
            }

            // Store data in request scope for the JSP view
            request.setAttribute("events", events);
            request.setAttribute("availableSeats", availableSeats);
            request.setAttribute("registeredCount", registeredCount);

            // Forward to the view (JSP)
            request.getRequestDispatcher("/events.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading events: " + e.getMessage(), e);
        }
    }
}
