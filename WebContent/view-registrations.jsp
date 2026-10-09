<%-- view-registrations.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Registration, model.Event" %>
<%!
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    request.setAttribute("pageTitle", "View Registrations");
    Event event = (Event) request.getAttribute("event");
    List<Registration> registrations = (List<Registration>) request.getAttribute("registrations");
    int count = registrations != null ? registrations.size() : 0;
%>
<%@ include file="header.jsp" %>

<main>
    <div class="page-header">
        <h1>Registered Students</h1>
        <a href="<%= request.getContextPath() %>/admin/dashboard" class="btn btn-secondary btn-sm">&larr; Back to Dashboard</a>
    </div>

    <% if (event != null) { %>
    <div class="card" style="margin-bottom:20px;">
        <div class="card-header">
            <h2><%= escapeHtml(event.getTitle()) %></h2>
            <span class="badge <%= "Seminar".equals(event.getEventType()) ? "badge-seminar" : "badge-workshop" %>">
                <%= event.getEventType() %>
            </span>
        </div>
        <div class="event-meta">
            <div><strong>Date & Time:</strong> <%= event.getEventDate() %> at <%= event.getEventTime().toString().substring(0,5) %> (<%= event.getDurationHours() %> Hour<%= event.getDurationHours() > 1 ? "s" : "" %>)</div>
            <div><strong>Venue:</strong> <%= escapeHtml(event.getVenue()) %></div>
            <div><strong>Speaker:</strong> <%= escapeHtml(event.getSpeakerName()) %></div>
            <div style="margin-top: 0.75rem;">
                <span class="badge badge-seats" style="margin-right: 10px;">Total Seats: <%= event.getTotalSeats() %></span>
                <span class="badge" style="background-color: var(--primary); color: white;">Registered: <%= count %></span>
            </div>
        </div>
    </div>
    <% } %>

    <div class="card">
        <% if (registrations == null || registrations.isEmpty()) { %>
            <div class="empty-state">
                <p>No students have registered for this event yet.</p>
            </div>
        <% } else { %>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Student Name</th>
                            <th>Roll Number</th>
                            <th>Email</th>
                            <th>Department</th>
                            <th>Phone</th>
                            <th>Registration Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% int sno = 1; for (Registration reg : registrations) { %>
                        <tr>
                            <td><%= sno++ %></td>
                            <td><strong><%= escapeHtml(reg.getStudentName()) %></strong></td>
                            <td><%= escapeHtml(reg.getRollNumber()) %></td>
                            <td><%= escapeHtml(reg.getStudentEmail()) %></td>
                            <td><%= escapeHtml(reg.getStudentDepartment()) %></td>
                            <td><%= reg.getStudentPhone() != null ? escapeHtml(reg.getStudentPhone()) : "-" %></td>
                            <td><%= reg.getRegisteredAt() %></td>
                            <td>
                                <form action="<%= request.getContextPath() %>/delete-registration" method="POST" onsubmit="return confirm('Are you sure you want to remove this student from the event?');">
                                    <input type="hidden" name="studentId" value="<%= reg.getStudentId() %>">
                                    <input type="hidden" name="eventId" value="<%= event.getEventId() %>">
                                    <input type="hidden" name="returnTo" value="admin">
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</main>

<%@ include file="footer.jsp" %>

