package controller;

import dao.RegistrationDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete-registration")
public class DeleteRegistrationServlet extends HttpServlet {
    private RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentIdStr = request.getParameter("studentId");
        String eventIdStr = request.getParameter("eventId");
        String returnTo = request.getParameter("returnTo"); // "student" or "admin"
        
        if (studentIdStr != null && eventIdStr != null) {
            try {
                int studentId = Integer.parseInt(studentIdStr);
                int eventId = Integer.parseInt(eventIdStr);
                registrationDAO.unregister(studentId, eventId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if ("admin".equals(returnTo)) {
            response.sendRedirect(request.getContextPath() + "/admin/view-registrations?eventId=" + eventIdStr);
        } else {
            // For student, they have to re-submit their roll number to view, so just send them back to the form
            response.sendRedirect(request.getContextPath() + "/my-registrations?status=unregistered");
        }
    }
}