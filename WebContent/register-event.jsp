<%--

  register-event.jsp  Event Registration Form (View)

  

  Data set by RegisterEventServlet:

  - "event"          : Event bean

  - "availableSeats" : int

  

  Query parameters read here:

  - error   : error message from failed submission

  - status  : "success" after successful registration

  

  JSP Concepts:

  - <jsp:useBean> : demonstrates the JSP action for accessing a JavaBean

  - <jsp:getProperty> : another JSP action to output a bean property

  - Reading request parameters with request.getParameter()

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

    request.setAttribute("pageTitle", "Register for Event");

    

    Event event          = (Event) request.getAttribute("event");

    Integer availSeats   = (Integer) request.getAttribute("availableSeats");

    

    String errorMsg   = request.getParameter("error");

    String statusMsg  = request.getParameter("status");

    

    // Retrieve previously submitted values so the form retains data on error

    String prevName   = request.getParameter("fullName");

    String prevEmail  = request.getParameter("email");

    String prevRoll   = request.getParameter("rollNumber");

    String prevDept   = request.getParameter("department");

    String prevPhone  = request.getParameter("phone");

    

    if (prevName  == null) prevName  = "";

    if (prevEmail == null) prevEmail = "";

    if (prevRoll  == null) prevRoll  = "";

    if (prevDept  == null) prevDept  = "";

    if (prevPhone == null) prevPhone = "";

%>

<%@ include file="header.jsp" %>



<main>

<% if (event == null) { %>

    <div class="alert alert-error">Event not found. <a href="<%= request.getContextPath() %>/events">Go back to events</a>.</div>

<% } else { %>



    <% if ("success".equals(statusMsg)) { %>

        <div class="alert alert-success">

             You have successfully registered for <strong><%= escapeHtml(event.getTitle()) %></strong>!

            &nbsp; <a href="<%= request.getContextPath() %>/my-registrations">View My Registrations</a>

            &nbsp; | &nbsp; <a href="<%= request.getContextPath() %>/events">Browse More Events</a>

        </div>

    <% } %>



    <% if (errorMsg != null && !errorMsg.isEmpty()) { %>

        <div class="alert alert-error"> <%= errorMsg %></div>

    <% } %>



    <!-- Event Summary Card -->

    <div class="card" style="margin-bottom:24px;">

        <div class="card-header">

            <h2><%= escapeHtml(event.getTitle()) %></h2>

            <span class="badge <%= "Seminar".equals(event.getEventType()) ? "badge-seminar" : "badge-workshop" %>">

                <%= event.getEventType() %>

            </span>

        </div>

        <div class="event-meta" style="margin: 15px 0;">
            <div><strong>Date & Time:</strong> <%= event.getEventDate() %> at <%= event.getEventTime().toString().substring(0,5) %> (<%= event.getDurationHours() %> Hour<%= event.getDurationHours() > 1 ? "s" : "" %>)</div>
            <div><strong>Venue:</strong> Room <%= escapeHtml(event.getVenue()) %></div>
            <div><strong>Department:</strong> <%= escapeHtml(event.getDepartment()) %></div>
            <div><strong>Speaker:</strong> <%= escapeHtml(event.getSpeakerName()) %></div>
        </div>

        <% if (event.getDescription() != null && !event.getDescription().isEmpty()) { %>

            <p style="color:var(--text-light); font-size:0.9rem;">

                <%= escapeHtml(event.getDescription()) %>

            </p>

        <% } %>

        <div style="margin-top:10px;">

            <% if (availSeats != null && availSeats <= 0) { %>

                <span class="badge badge-full">No seats available</span>

            <% } else { %>

                <span class="badge badge-seats"><%= availSeats %> seat(s) available</span>

            <% } %>

        </div>

    </div>



    <%-- 

      JSP Action: <jsp:useBean>  demonstrates accessing a JavaBean.

      Here we declare the 'event' bean from request scope.

      Then <jsp:getProperty> is used to read a property.

    --%>

    <jsp:useBean id="eventBean" class="model.Event" scope="request" />



    <% if (availSeats != null && availSeats > 0 && !"success".equals(statusMsg)) { %>



    <div class="form-card">

        <h2> Registration Form</h2>



        <form id="registrationForm" method="post"

              action="<%= request.getContextPath() %>/register-event">



            <!-- Hidden field: pass the event ID along with the form -->

            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">

            <%-- <jsp:getProperty> reads a bean property directly in JSP --%>

            <%-- Event title for confirmation: <jsp:getProperty name="event" property="title" /> --%>



            <div class="form-row">

                <div class="form-group">

                    <label for="fullName">Full Name *</label>

                    <input type="text" id="fullName" name="fullName"

                           value="<%= escapeHtml(prevName) %>"

                           placeholder="Enter your full name" maxlength="150">

                </div>

                <div class="form-group">

                    <label for="rollNumber">Roll Number *</label>

                    <input type="text" id="rollNumber" name="rollNumber"

                           value="<%= escapeHtml(prevRoll) %>"

                           placeholder="e.g. CS2023001" maxlength="50">

                </div>

            </div>



            <div class="form-group">

                <label for="email">Email Address *</label>

                <input type="email" id="email" name="email"

                       value="<%= escapeHtml(prevEmail) %>"

                       placeholder="your.email@college.edu" maxlength="150">

                <div class="form-hint">

                    If you have registered before, use the same email to avoid duplicate entries.

                </div>

            </div>



            <div class="form-row">

                <div class="form-group">

                    <label for="department">Department *</label>

                    <select id="department" name="department">

                        <option value="">-- Select Department --</option>

                        <% String[] depts = {"Computer Science","Information Technology",

                               "Electronics and Communication","Mechanical Engineering",

                               "Civil Engineering","Electrical Engineering","Other"};

                           for (String d : depts) { %>

                            <option value="<%= d %>" <%= d.equals(prevDept) ? "selected" : "" %>>

                                <%= d %>

                            </option>

                        <% } %>

                    </select>

                </div>

                <div class="form-group">

                    <label for="phone">Phone Number</label>

                    <input type="tel" id="phone" name="phone"

                           value="<%= escapeHtml(prevPhone) %>"

                           placeholder="10-digit mobile number" maxlength="15">

                </div>

            </div>



            <div style="display:flex; gap:12px; margin-top:8px;">

                <button type="submit" class="btn btn-success btn-lg">Confirm Registration</button>

                <a href="<%= request.getContextPath() %>/events" class="btn btn-secondary">Cancel</a>

            </div>

        </form>

    </div>



    <% } else if ("success".equals(statusMsg)) { %>

        <!-- Already shown success banner above, no form needed -->

    <% } else { %>

        <div class="alert alert-warning">

            This event is full. No registrations are being accepted at this time.

        </div>

        <a href="<%= request.getContextPath() %>/events" class="btn btn-primary">Browse Other Events</a>

    <% } %>



<% } %>

</main>



<%@ include file="footer.jsp" %>



