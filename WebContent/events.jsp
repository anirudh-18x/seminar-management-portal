<%-- events.jsp — Browse Events Page (View) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Event" %>
<%
    request.setAttribute("pageTitle", "Browse Events");
    
    List<Event> events        = (List<Event>) request.getAttribute("events");
    int[] availableSeats      = (int[])       request.getAttribute("availableSeats");
    int[] registeredCount     = (int[])       request.getAttribute("registeredCount");
%>
<%!
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%@ include file="header.jsp" %>

<main>
    <div class="page-header">
        <h1>Browse Events</h1>
    </div>
    <p>Discover our upcoming seminars and workshops. Register now to secure your seat!</p>

    <% if (events == null || events.isEmpty()) { %>
        <div class="empty-state">
            <p>No events are currently scheduled. Please check back later!</p>
        </div>
    <% } else { %>
        <div class="event-grid">
            <% for (int i = 0; i < events.size(); i++) {
                   Event ev = events.get(i);
                   int seats = availableSeats != null ? availableSeats[i] : 0;
                   int registered = registeredCount != null ? registeredCount[i] : 0;
                   boolean isFull = seats <= 0;
                   String badgeClass = "Seminar".equals(ev.getEventType()) ? "badge-seminar" : "badge-workshop";
            %>
            <div class="event-card">
                <div style="margin-bottom: 0.75rem;">
                    <span class="badge <%= badgeClass %>"><%= ev.getEventType() %></span>
                </div>
                
                <h2 class="event-title">
                    <%= escapeHtml(ev.getTitle()) %>
                </h2>
                
                <div class="event-meta">
                    <div><strong>Date & Time:</strong> <%= ev.getEventDate() %> at <%= ev.getEventTime().toString().substring(0,5) %> (<%= ev.getDurationHours() %> Hour<%= ev.getDurationHours() > 1 ? "s" : "" %>)</div>
                    <div><strong>Venue:</strong> <%= escapeHtml(ev.getVenue()) %></div>
                    <div><strong>Department:</strong> <%= escapeHtml(ev.getDepartment()) %></div>
                    <div><strong>Speaker:</strong> <%= escapeHtml(ev.getSpeakerName()) %></div>
                </div>
                
                <% if (ev.getDescription() != null && !ev.getDescription().isEmpty()) { %>
                <p style="font-size: 0.9rem; margin-top: 0.5rem;">
                    <%= escapeHtml(ev.getDescription()).length() > 100
                        ? escapeHtml(ev.getDescription()).substring(0, 100) + "..."
                        : escapeHtml(ev.getDescription()) %>
                </p>
                <% } %>
                
                <div class="event-footer">
                    <% if (isFull) { %>
                        <span class="badge badge-full">Full (<%= registered %>/<%= ev.getTotalSeats() %>)</span>
                    <% } else { %>
                        <span class="badge badge-seats"><%= seats %> seats left</span>
                    <% } %>
                    
                    <% if (!isFull) { %>
                        <a href="<%= request.getContextPath() %>/register-event?eventId=<%= ev.getEventId() %>"
                           class="btn btn-primary btn-sm">Register</a>
                    <% } else { %>
                        <button class="btn btn-secondary btn-sm" disabled style="opacity:0.5; cursor:not-allowed;">Closed</button>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
    <% } %>
</main>

<%@ include file="footer.jsp" %>


