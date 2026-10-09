package controller;

import dao.AdminDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * AdminSetupServlet — One-time admin account creation tool
 *
 * URL: /admin/setup
 *
 * This page is only for initial setup. After creating the admin account,
 * the sample-data.sql already inserts a default admin (admin/Admin@1234).
 * Use this servlet to create additional accounts or change passwords.
 *
 * In a real application you would remove this after setup.
 * For this college project it is kept for demonstration purposes.
 */
@WebServlet("/admin/setup")
public class AdminSetupServlet extends HttpServlet {

    private AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin-setup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));
        String confirm  = trim(request.getParameter("confirmPassword"));

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/admin-setup.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirm)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("/admin-setup.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters.");
            request.getRequestDispatcher("/admin-setup.jsp").forward(request, response);
            return;
        }

        try {
            if (adminDAO.adminExists(username)) {
                request.setAttribute("errorMessage", "Username '" + escapeHtml(username) + "' already exists.");
                request.getRequestDispatcher("/admin-setup.jsp").forward(request, response);
                return;
            }

            boolean created = adminDAO.createAdmin(username, password);
            if (created) {
                request.setAttribute("successMessage",
                    "Admin account '" + escapeHtml(username) + "' created successfully. " +
                    "<a href='" + request.getContextPath() + "/admin/login'>Login now</a>");
            } else {
                request.setAttribute("errorMessage", "Failed to create account. Please try again.");
            }
            request.getRequestDispatcher("/admin-setup.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }

    private String trim(String s)       { return s == null ? "" : s.trim(); }
    private String escapeHtml(String s) {
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
