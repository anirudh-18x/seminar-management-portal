package controller;

import dao.EventDAO;
import dao.RegistrationDAO;
import dao.StudentDAO;
import model.Event;
import model.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

@WebServlet("/register-event")
public class RegisterEventServlet extends HttpServlet {
    private EventDAO eventDAO = new EventDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("eventId");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/events");
            return;
        }
        try {
            int eventId = Integer.parseInt(idParam);
            Event event = eventDAO.getEventById(eventId);
            if (event == null) {
                request.setAttribute("errorMessage", "Event not found.");
                request.getRequestDispatcher("/events.jsp").forward(request, response);
                return;
            }
            int availableSeats = eventDAO.getAvailableSeats(eventId);
            request.setAttribute("event", event);
            request.setAttribute("availableSeats", availableSeats);
            request.getRequestDispatcher("/register-event.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fullName   = trim(request.getParameter("fullName"));
        String email      = trim(request.getParameter("email"));
        String rollNumber = trim(request.getParameter("rollNumber"));
        String department = trim(request.getParameter("department"));
        String phone      = trim(request.getParameter("phone"));
        String eventIdStr = trim(request.getParameter("eventId"));

        if (fullName.isEmpty() || email.isEmpty() || rollNumber.isEmpty() || department.isEmpty() || eventIdStr.isEmpty()) {
            redirectWithError(request, response, eventIdStr, "All required fields must be filled.");
            return;
        }

        int eventId = Integer.parseInt(eventIdStr);

        try {
            int available = eventDAO.getAvailableSeats(eventId);
            if (available <= 0) {
                redirectWithError(request, response, eventIdStr, "Sorry, no seats available.");
                return;
            }

            Student student = studentDAO.findByRollNumber(rollNumber);
            if (student == null) {
                if (studentDAO.findByEmail(email) != null) {
                    redirectWithError(request, response, eventIdStr, "Email is already registered to another roll number.");
                    return;
                }
                student = new Student();
                student.setFullName(fullName);
                student.setEmail(email);
                student.setRollNumber(rollNumber);
                student.setDepartment(department);
                student.setPhone(phone);
                int sid = studentDAO.insertStudent(student);
                student.setStudentId(sid);
            }

            if (registrationDAO.isAlreadyRegistered(student.getStudentId(), eventId)) {
                redirectWithError(request, response, eventIdStr, "You are already registered for this event.");
                return;
            }

            registrationDAO.register(student.getStudentId(), eventId);
            response.sendRedirect(request.getContextPath() + "/register-event?eventId=" + eventId + "&status=success");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void redirectWithError(HttpServletRequest req, HttpServletResponse res, String eventId, String msg) throws IOException {
        String url = req.getContextPath() + "/register-event?eventId=" + eventId + "&error=" + URLEncoder.encode(msg, "UTF-8");
        res.sendRedirect(url);
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}