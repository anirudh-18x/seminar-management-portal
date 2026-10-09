package controller;

import dao.RegistrationDAO;
import dao.StudentDAO;
import model.Registration;
import model.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/my-registrations")
public class MyRegistrationsServlet extends HttpServlet {
    private StudentDAO studentDAO = new StudentDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/my-registrations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollNumber = request.getParameter("rollNumber");

        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please provide your Roll Number.");
            request.getRequestDispatcher("/my-registrations.jsp").forward(request, response);
            return;
        }
        
        try {
            Student student = studentDAO.findByRollNumber(rollNumber.trim());
            
            if (student == null) {
                request.setAttribute("errorMessage", "Student not found with this Roll Number.");
                request.getRequestDispatcher("/my-registrations.jsp").forward(request, response);
                return;
            }
            
            List<Registration> regs = registrationDAO.getRegistrationsByStudent(student.getStudentId());
            request.setAttribute("student", student);
            request.setAttribute("registrations", regs);
            request.getRequestDispatcher("/my-registrations.jsp").forward(request, response);
            
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}