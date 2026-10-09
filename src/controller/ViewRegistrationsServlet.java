package controller;

import dao.EventDAO;
import dao.RegistrationDAO;
import model.Event;
import model.Registration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * ViewRegistrationsServlet — Admin: view all students registered for an event
 *
 * URL: /admin/view-registrations
 *
 * doGet():
 *   - Checks admin session.
 *   - Reads eventId parameter.
 *   - Calls RegistrationDAO.getRegistrationsByEvent() — which uses CallableStatement.
 *   - Forwards to view-registrations.jsp.
 */
@WebServlet("/admin/view-registrations")
public class ViewRegistrationsServlet extends HttpServlet {

    private RegistrationDAO registrationDAO = new RegistrationDAO();
    private EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String idParam = request.getParameter("eventId");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        try {
            int eventId = Integer.parseInt(idParam);
            Event event = eventDAO.getEventById(eventId);

            if (event == null) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }

            // getRegistrationsByEvent uses CallableStatement internally
            List<Registration> registrations = registrationDAO.getRegistrationsByEvent(eventId);

            request.setAttribute("event", event);
            request.setAttribute("registrations", registrations);
            request.getRequestDispatcher("/view-registrations.jsp").forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error loading registrations: " + e.getMessage(), e);
        }
    }
}
