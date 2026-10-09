package controller;

import dao.AdminDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * AdminLoginServlet — Handles administrator login and logout
 *
 * URL: /admin/login
 *
 * doGet():
 *   - If already logged in, redirect to dashboard.
 *   - Otherwise show login.jsp.
 *
 * doPost():
 *   - Reads username and password from the form.
 *   - Calls AdminDAO.validateAdmin() to check credentials.
 *   - On success: sets session attribute "adminUser" and redirects to dashboard.
 *   - On failure: forwards back to login.jsp with an error message.
 *
 * Logout: handled via GET with parameter ?action=logout
 *
 * This demonstrates:
 *   - Session creation (request.getSession()).
 *   - Storing admin identity in session.
 *   - Session invalidation on logout.
 */
@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Handle logout
        if ("logout".equals(request.getParameter("action"))) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // destroys all session data
            }
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        // Already logged in? Go to dashboard.
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminUser") != null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            boolean valid = adminDAO.validateAdmin(username, password);

            if (valid) {
                // Create a new session and store the admin's username
                HttpSession session = request.getSession(true);
                session.setAttribute("adminUser", username);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error during login: " + e.getMessage(), e);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
