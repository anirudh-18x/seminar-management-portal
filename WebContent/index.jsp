<%-- index.jsp â€” Student Home Page (View) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Page Directive sets response content type
    request.setAttribute("pageTitle", "Home");
%>
<%@ include file="header.jsp" %>

<main>
    <!-- Hero Section -->
    <section class="hero">
        <h1>Discover. Learn. Connect.</h1>
        <p>
            Explore upcoming seminars and hands-on workshops hosted by our college departments. 
            Enhance your skills, meet industry experts, and track your registrations all in one place.
        </p>
        <div class="hero-actions">
            <a href="<%= request.getContextPath() %>/events" class="btn btn-primary">Explore Events</a>
            <a href="<%= request.getContextPath() %>/my-registrations" class="btn btn-secondary">View My Registrations</a>
        </div>
    </section>

    </main>

<%@ include file="footer.jsp" %>

