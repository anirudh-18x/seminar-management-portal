package controller;

import dao.EventDAO;
import dao.RegistrationDAO;
import dao.VenueDAO;
import model.Event;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private EventDAO eventDAO = new EventDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();
    private VenueDAO venueDAO = new VenueDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String filterDate = request.getParameter("filterDate");
        String filterVenue = request.getParameter("filterVenue");

        try {
            List<Event> allEvents = eventDAO.getAllEvents();
            List<Event> events = new ArrayList<>();
            
            // Apply filters
            for (Event e : allEvents) {
                boolean matchDate = (filterDate == null || filterDate.isEmpty() || e.getEventDate().toString().equals(filterDate));
                boolean matchVenue = (filterVenue == null || filterVenue.isEmpty() || e.getVenue().equals(filterVenue));
                
                if (matchDate && matchVenue) {
                    events.add(e);
                }
            }

            int[] regCounts = new int[events.size()];
            for (int i = 0; i < events.size(); i++) {
                regCounts[i] = registrationDAO.getRegistrationCount(events.get(i).getEventId());
            }

            request.setAttribute("events", events);
            request.setAttribute("regCounts", regCounts);
            request.setAttribute("adminUser", getAdminUser(request));
            request.setAttribute("venues", venueDAO.getAllVenues());
            request.setAttribute("filterDate", filterDate);
            request.setAttribute("filterVenue", filterVenue);
            
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }
        
        String action = request.getParameter("action");
        if ("addVenue".equals(action)) {
            String newVenue = request.getParameter("newVenue");
            if (newVenue != null && !newVenue.trim().isEmpty()) {
                try {
                    venueDAO.addVenue(newVenue.trim());
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard?venueAdded=true");
                } catch (SQLException e) {
                    throw new ServletException("Database error: " + e.getMessage(), e);
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?venueError=empty");
            }
        }
    }

    static boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("adminUser") != null;
    }

    static String getAdminUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? "" : (String) session.getAttribute("adminUser");
    }
}