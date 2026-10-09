<%-- my-registrations.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Registration, model.Student" %>
<%!
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    request.setAttribute("pageTitle", "My Registrations");
    Student student = (Student) request.getAttribute("student");
    List<Registration> registrations = (List<Registration>) request.getAttribute("registrations");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String status = request.getParameter("status");
%>
<%@ include file="header.jsp" %>
<main>
    <div class="page-header">
        <h1>My Registrations</h1>
    </div>

    <% if (errorMessage != null) { %>
        <div class="alert alert-error"><%= escapeHtml(errorMessage) %></div>
    <% } %>
    
    <% if ("unregistered".equals(status)) { %>
        <div class="alert alert-success">You have successfully unregistered from the event. Please enter your roll number again to see your updated list.</div>
    <% } %>

    <div class="form-card">
        <form action="<%= request.getContextPath() %>/my-registrations" method="POST">
            <div class="form-group">
                <label for="rollNumber">Student Roll Number</label>
                <input type="text" id="rollNumber" name="rollNumber" required placeholder="e.g. CS2024-001">
            </div>
            <button type="submit" class="btn btn-primary">View Registrations</button>
        </form>
    </div>

    <% if (student != null) { %>
    <div style="margin-bottom:1.5rem; text-align:center;">
        <h2 style="font-size:1.3rem;">Welcome, <%= escapeHtml(student.getFullName()) %></h2>
        <p>Department: <%= escapeHtml(student.getDepartment()) %></p>
    </div>
    <% } %>

    <% if (registrations != null && !registrations.isEmpty()) { %>
    <div class="card">
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Event Title</th>
                        <th>Event ID</th>
                        <th>Date & Time</th><th>Venue</th><th>Date & Time</th><th>Venue</th><th>Registered On</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% int sno = 1; for (Registration reg : registrations) { %>
                    <tr>
                        <td><%= sno++ %></td>
                        <td><strong><%= escapeHtml(reg.getEventTitle()) %></strong></td>
                        <td>#<%= reg.getEventId() %></td><td><%= reg.getEventDate() %> <%= reg.getEventTime() %></td><td><%= escapeHtml(reg.getVenue()) %></td><td><%= reg.getEventDate() %> <%= reg.getEventTime().toString().substring(0,5) %></td><td><%= escapeHtml(reg.getVenue()) %></td>
                        <td><%= reg.getRegisteredAt() %></td>
                        <td>
                            <div class="action-links">
                                <a href="<%= request.getContextPath() %>/register-event?eventId=<%= reg.getEventId() %>" class="btn btn-outline btn-sm">View</a>
                                <form action="<%= request.getContextPath() %>/delete-registration" method="POST" style="display:inline;" onsubmit="return confirm('Are you sure you want to unregister from this event?');">
                                    <input type="hidden" name="studentId" value="<%= student.getStudentId() %>">
                                    <input type="hidden" name="eventId" value="<%= reg.getEventId() %>">
                                    <input type="hidden" name="returnTo" value="student">
                                    <button type="submit" class="btn btn-danger btn-sm">Unregister</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
    <% } else if (registrations != null) { %>
        <div class="empty-state">
            <p>No registrations found.</p>
            <a href="<%= request.getContextPath() %>/events" class="btn btn-primary">Browse Events</a>
        </div>
    <% } %>
</main>
<%@ include file="footer.jsp" %>

