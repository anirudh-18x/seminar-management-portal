<%--

  edit-event.jsp  Admin: Edit Existing Event Form (View)

  

  Data set by EventManagementServlet:

  - "event"        : Event (existing data to pre-fill the form)

  - "errorMessage" : String (on validation error)

--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>





<%@ page import="model.Event" %>






<%!
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%

    request.setAttribute("pageTitle", "Edit Event");

    Event event = (Event) request.getAttribute("event");

    String errorMsg = (String) request.getAttribute("errorMessage");

    

    if (event == null) {

        response.sendRedirect(request.getContextPath() + "/admin/dashboard");

        return;

    }

%>

<%@ include file="header.jsp" %>



<main>

    <div class="page-header">

        <h1> Edit Event</h1>

        <a href="<%= request.getContextPath() %>/admin/dashboard" class="btn btn-neutral btn-sm">&larr; Back to Dashboard</a>

    </div>



    <% if (errorMsg != null) { %>

        <div class="alert alert-error"><%= errorMsg %></div>

    <% } %>



    <div class="form-card" style="max-width: 680px;">

        <form id="eventForm" method="post" action="<%= request.getContextPath() %>/admin/edit-event">



            <!-- Hidden: existing event ID so the servlet knows which record to update -->

            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">



            <div class="form-group">

                <label for="title">Event Title *</label>

                <input type="text" id="title" name="title" maxlength="200"

                       value="<%= escapeHtml(event.getTitle()) %>"

                       placeholder="Enter event title">

            </div>



            <div class="form-row">

                <div class="form-group">

                    <label for="eventType">Event Type *</label>

                    <select id="eventType" name="eventType">

                        <option value="Seminar"  <%= "Seminar".equals(event.getEventType())  ? "selected" : "" %>>Seminar</option>

                        <option value="Workshop" <%= "Workshop".equals(event.getEventType()) ? "selected" : "" %>>Workshop</option>

                    </select>

                </div>

                <div class="form-group">

                    <label for="department">Department *</label>

                    <select id="department" name="department">

                        <% String[] depts = {"Computer Science","Information Technology",

                               "Electronics and Communication","Mechanical Engineering",

                               "Civil Engineering","Electrical Engineering","All Departments"};

                           for (String d : depts) { %>

                            <option value="<%= d %>" <%= d.equals(event.getDepartment()) ? "selected" : "" %>><%= d %></option>

                        <% } %>

                    </select>

                </div>

            </div>



            <div class="form-row">

                <div class="form-group">

                    <label for="eventDate">Event Date *</label>

                    <input type="date" id="eventDate" name="eventDate"

                           value="<%= event.getEventDate() != null ? event.getEventDate().toString() : "" %>">

                </div>

                <div class="form-group">

                    <label for="eventTime">Event Time *</label>

                    <select id="eventTime" name="eventTime" required>
                        <% for(int h=9; h<=16; h++) { 
                            String val = String.format("%02d:00", h);
                            String label = (h > 12 ? (h-12) : h) + ":00 " + (h >= 12 ? "PM" : "AM");
                            String sel = (event.getEventTime() != null && event.getEventTime().toString().startsWith(val)) ? "selected" : "";
                        %>
                        <option value="<%= val %>" <%= sel %>><%= label %></option>
                        <% } %>
                    </select>

                </div>
                <div class="form-group">
                    <label for="durationHours">Duration (Hours) *</label>
                    <select id="durationHours" name="durationHours" required>
                        <% for(int i=1; i<=7; i++) { 
                            String sel = (event.getDurationHours() == i) ? "selected" : "";
                        %>
                        <option value="<%= i %>" <%= sel %>><%= i %> Hour<%= i > 1 ? "s" : "" %></option>
                        <% } %>
                    </select>
                </div>

            </div>



            <div class="form-group">

                <label for="venue">Venue *</label>

                <select id="venue" name="venue" required>
                    <% java.util.List<String> venues = (java.util.List<String>) request.getAttribute("venues");
                       if (venues != null) {
                           for (String v : venues) {
                               String sel = (event.getVenue() != null && event.getVenue().equals(v)) ? "selected" : "";
                    %>
                    <option value="<%= v %>" <%= sel %>>Room <%= escapeHtml(v) %></option>
                    <%     }
                       } %>
                </select>

            </div>



            <div class="form-row">

                <div class="form-group">

                    <label for="speakerName">Speaker Name *</label>

                    <input type="text" id="speakerName" name="speakerName" maxlength="150"

                           value="<%= escapeHtml(event.getSpeakerName()) %>">

                </div>

                <div class="form-group">

                    <label for="totalSeats">Total Seats *</label>

                    <input type="number" id="totalSeats" name="totalSeats" min="1" max="1000"

                           value="<%= event.getTotalSeats() %>">

                </div>

            </div>



            <div class="form-group">

                <label for="description">Description</label>

                <textarea id="description" name="description" maxlength="2000"><%= escapeHtml(event.getDescription()) %></textarea>

            </div>



            <div style="display:flex; gap:12px; margin-top:8px;">

                <button type="submit" class="btn btn-primary">Update Event</button>

                <a href="<%= request.getContextPath() %>/admin/dashboard" class="btn btn-neutral">Cancel</a>

            </div>

        </form>

    </div>

</main>



<%@ include file="footer.jsp" %>






