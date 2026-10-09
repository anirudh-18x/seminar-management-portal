<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Event" %>
<%!
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    request.setAttribute("pageTitle", "Admin Dashboard");
    String adminUser = (String) request.getAttribute("adminUser");
    List<Event> events = (List<Event>) request.getAttribute("events");
    int[] regCounts = (int[]) request.getAttribute("regCounts");
    List<String> venues = (List<String>) request.getAttribute("venues");
    
    String filterDate = (String) request.getAttribute("filterDate");
    String filterVenue = (String) request.getAttribute("filterVenue");

    int totalEvents = events != null ? events.size() : 0;
    int totalRegistrations = 0;
    if (regCounts != null) {
        for (int c : regCounts) totalRegistrations += c;
    }
%>
<%@ include file="header.jsp" %>
<main>
    <div class="page-header" style="display:flex; justify-content:space-between; align-items:center;">
        <h1>Admin Dashboard</h1>
        <div>
            <span style="margin-right:15px;">Logged in as: <strong><%= escapeHtml(adminUser) %></strong></span>
            <a href="<%= request.getContextPath() %>/admin/logout" class="btn btn-outline btn-sm">Logout</a>
        </div>
    </div>

    <% if ("true".equals(request.getParameter("added"))) { %>
        <div class="alert alert-success">Event successfully added.</div>
    <% } %>
    <% if ("true".equals(request.getParameter("updated"))) { %>
        <div class="alert alert-success">Event successfully updated.</div>
    <% } %>
    <% if ("true".equals(request.getParameter("deleted"))) { %>
        <div class="alert alert-success">Event successfully deleted.</div>
    <% } %>
    <% if ("true".equals(request.getParameter("venueAdded"))) { %>
        <div class="alert alert-success">Venue successfully added.</div>
    <% } %>
    <% if ("empty".equals(request.getParameter("venueError"))) { %>
        <div class="alert alert-error">Venue name cannot be empty or already exists.</div>
    <% } %>

    <div class="stats-grid" style="display:grid; grid-template-columns: repeat(2, 1fr); gap:20px; margin-bottom:30px;">
        <div class="card" style="text-align:center;">
            <h3>Total Events (Filtered)</h3>
            <p style="font-size:2rem; font-weight:bold; color:var(--primary);"><%= totalEvents %></p>
        </div>
        <div class="card" style="text-align:center;">
            <h3>Total Registrations</h3>
            <p style="font-size:2rem; font-weight:bold; color:var(--primary);"><%= totalRegistrations %></p>
        </div>
        </div>

    <div class="card">
        <div class="card-header" style="display:flex; justify-content:space-between; align-items:center;">
            <h2>Manage Events</h2>
            <form action="<%= request.getContextPath() %>/admin/dashboard" method="GET" style="display:flex; gap:10px; align-items:center; margin:0;">
                <input type="date" name="filterDate" value="<%= filterDate != null ? filterDate : "" %>" style="padding:5px;">
                <select name="filterVenue" style="padding:5px;">
                    <option value="">All Venues</option>
                    <% if (venues != null) { 
                        for (String v : venues) {
                            String sel = (v.equals(filterVenue)) ? "selected" : "";
                    %>
                    <option value="<%= v %>" <%= sel %>>Room <%= escapeHtml(v) %></option>
                    <% } } %>
                </select>
                <button type="submit" class="btn btn-outline btn-sm">Filter</button>
                <a href="<%= request.getContextPath() %>/admin/dashboard" class="btn btn-outline btn-sm">Clear</a>
            </form>
            <div style="display:flex; gap:15px; align-items:center;">
                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" id="addVenueForm" style="margin:0;">
                    <input type="hidden" name="action" value="addVenue">
                    <input type="hidden" name="newVenue" id="newVenueInput">
                    <button type="button" class="btn btn-primary" style="padding:6px 16px; height:34px; display:flex; align-items:center;" onclick="promptAddVenue()">Add Venue</button>
                </form>
                <a href="<%= request.getContextPath() %>/admin/add-event" class="btn btn-primary" style="padding:6px 16px; height:34px; display:flex; align-items:center;">Add New Event</a>
            </div>
        </div>

        <% if (events == null || events.isEmpty()) { %>
            <div class="empty-state">
                <p>No events found for this filter.</p>
            </div>
        <% } else { %>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Time (Dur)</th>
                            <th>Venue</th>
                            <th>Reg / Seats</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (int i = 0; i < events.size(); i++) {
                            Event e = events.get(i);
                            int regCount = regCounts[i];
                        %>
                        <tr>
                            <td><%= e.getEventId() %></td>
                            <td><strong><%= escapeHtml(e.getTitle()) %></strong></td>
                            <td>
                                <span class="badge <%= "Seminar".equals(e.getEventType()) ? "badge-seminar" : "badge-workshop" %>">
                                    <%= e.getEventType() %>
                                </span>
                            </td>
                            <td><%= e.getEventDate() %></td>
                            <td><%= e.getEventTime().toString().substring(0,5) %> (<%= e.getDurationHours() %>h)</td>
                            <td><%= escapeHtml(e.getVenue()) %></td>
                            <td><%= regCount %> / <%= e.getTotalSeats() %></td>
                            <td>
                                <div class="action-links">
                                    <a href="<%= request.getContextPath() %>/admin/view-registrations?eventId=<%= e.getEventId() %>"
                                       class="btn btn-outline btn-sm">Students</a>
                                    <a href="<%= request.getContextPath() %>/admin/edit-event?eventId=<%= e.getEventId() %>"
                                       class="btn btn-secondary btn-sm">Edit</a>
                                    <a href="<%= request.getContextPath() %>/admin/delete-event?eventId=<%= e.getEventId() %>"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('Are you sure you want to delete this event?');">Delete</a>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</main>
<script>
function promptAddVenue() {
    var venueName = prompt("Enter the name or number of the new venue:");
    if (venueName != null && venueName.trim() !== "") {
        document.getElementById("newVenueInput").value = venueName.trim();
        document.getElementById("addVenueForm").submit();
    }
}
</script>
<%@ include file="footer.jsp" %>

