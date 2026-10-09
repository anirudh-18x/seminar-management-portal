<%--

  add-event.jsp  Admin: Add New Event Form (View)

  

  Data set by EventManagementServlet (on validation error):

  - "errorMessage" : String

  - "formData"     : Event (to restore form values)

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

    request.setAttribute("pageTitle", "Add Event");

    String errorMsg = (String) request.getAttribute("errorMessage");

    Event fd = (Event) request.getAttribute("formData"); // for restoring values on error

%>

<%@ include file="header.jsp" %>



<main>

    <div class="page-header">

        <h1> Add New Event</h1>

        <a href="<%= request.getContextPath() %>/admin/dashboard" class="btn btn-neutral btn-sm">&larr; Back to Dashboard</a>

    </div>



    <% if (errorMsg != null) { %>

        <div class="alert alert-error"><%= errorMsg %></div>

    <% } %>



    <div class="form-card" style="max-width: 680px;">

        <form id="eventForm" method="post" action="<%= request.getContextPath() %>/admin/add-event">



            <div class="form-row">

                <div class="form-group" style="grid-column: span 2;">

                    <label for="title">Event Title *</label>

                    <input type="text" id="title" name="title" maxlength="200"

                           value="<%= fd != null ? escapeHtml(fd.getTitle()) : "" %>"

                           placeholder="Enter event title">

                </div>

            </div>



            <div class="form-row">

                <div class="form-group">

                    <label for="eventType">Event Type *</label>

                    <select id="eventType" name="eventType">

                        <option value="">-- Select Type --</option>

                        <option value="Seminar"  <%= fd != null && "Seminar".equals(fd.getEventType())  ? "selected" : "" %>>Seminar</option>

                        <option value="Workshop" <%= fd != null && "Workshop".equals(fd.getEventType()) ? "selected" : "" %>>Workshop</option>

                    </select>

                </div>

                <div class="form-group">

                    <label for="department">Department *</label>

                    <select id="department" name="department">

                        <option value="">-- Select Department --</option>

                        <% String[] depts = {"Computer Science","Information Technology",

                               "Electronics and Communication","Mechanical Engineering",

                               "Civil Engineering","Electrical Engineering","All Departments"};

                           for (String d : depts) { %>

                            <option value="<%= d %>" <%= fd != null && d.equals(fd.getDepartment()) ? "selected" : "" %>>

                                <%= d %>

                            </option>

                        <% } %>

                    </select>

                </div>

            </div>



            <div class="form-row">

                <div class="form-group">

                    <label for="eventDate">Event Date *</label>

                    <input type="date" id="eventDate" name="eventDate"

                           value="<%= fd != null && fd.getEventDate() != null ? fd.getEventDate().toString() : "" %>">

                </div>

                <div class="form-group">

                    <label for="eventTime">Event Time *</label>

                    <select id="eventTime" name="eventTime" required>
                        <option value="" disabled <%= (fd == null || fd.getEventTime() == null) ? "selected" : "" %>>Select Time</option>
                        <% for(int h=9; h<=16; h++) { 
                            String val = String.format("%02d:00", h);
                            String label = (h > 12 ? (h-12) : h) + ":00 " + (h >= 12 ? "PM" : "AM");
                            String sel = (fd != null && fd.getEventTime() != null && fd.getEventTime().toString().startsWith(val)) ? "selected" : "";
                        %>
                        <option value="<%= val %>" <%= sel %>><%= label %></option>
                        <% } %>
                    </select>

                </div>
                <div class="form-group">
                    <label for="durationHours">Duration (Hours) *</label>
                    <select id="durationHours" name="durationHours" required>
                        <% for(int i=1; i<=7; i++) { 
                            String sel = (fd != null && fd.getDurationHours() == i) ? "selected" : "";
                        %>
                        <option value="<%= i %>" <%= sel %>><%= i %> Hour<%= i > 1 ? "s" : "" %></option>
                        <% } %>
                    </select>
                </div>

            </div>



            <div class="form-group">

                <label for="venue">Venue *</label>

                <select id="venue" name="venue" required>
                    <option value="" disabled <%= (fd == null || fd.getVenue() == null || fd.getVenue().isEmpty()) ? "selected" : "" %>>Select a Venue</option>
                    <% java.util.List<String> venues = (java.util.List<String>) request.getAttribute("venues");
                       if (venues != null) {
                           for (String v : venues) {
                               String sel = (fd != null && v.equals(fd.getVenue())) ? "selected" : "";
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

                           value="<%= fd != null ? escapeHtml(fd.getSpeakerName()) : "" %>"

                           placeholder="Speaker's full name">

                </div>

                <div class="form-group">

                    <label for="totalSeats">Total Seats *</label>

                    <input type="number" id="totalSeats" name="totalSeats" min="1" max="1000"

                           value="<%= fd != null && fd.getTotalSeats() > 0 ? fd.getTotalSeats() : "50" %>"

                           placeholder="50">

                </div>

            </div>



            <div class="form-group">

                <label for="description">Description</label>

                <textarea id="description" name="description" maxlength="2000"

                          placeholder="Brief description of the event content..."><%= fd != null ? escapeHtml(fd.getDescription()) : "" %></textarea>

            </div>



            <div style="display:flex; gap:12px; margin-top:8px;">

                <button type="submit" class="btn btn-primary">Add Event</button>

                <a href="<%= request.getContextPath() %>/admin/dashboard" class="btn btn-neutral">Cancel</a>

            </div>

        </form>

    </div>

</main>



<%@ include file="footer.jsp" %>






