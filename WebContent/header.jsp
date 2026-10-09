<%-- header.jsp ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Shared Navigation Header --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) pageTitle = "College Seminar Portal";
    
    // Check if the current user is an admin by inspecting the session
    boolean isAdmin = (session != null && session.getAttribute("adminUser") != null);
    
    String currentURI = request.getRequestURI();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> | College Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=4">
</head>
<body>

<header class="navbar">
    <div class="nav-brand">
        <a href="<%= request.getContextPath() %>/">🎓 Seminar Portal</a>
    </div>
    
    <button class="mobile-menu-btn" id="mobileMenuBtn">ÃƒÂ¢Ã¢â‚¬Â°Ã‚Â¡</button>

    <nav class="nav-links" id="navLinks">
        <% if (!isAdmin) { %>
            <a href="<%= request.getContextPath() %>/" 
               class="<%= currentURI.endsWith("/") || currentURI.endsWith("index.jsp") ? "active" : "" %>">Home</a>
            <a href="<%= request.getContextPath() %>/events"
               class="<%= currentURI.contains("/events") ? "active" : "" %>">Browse Events</a>
            <a href="<%= request.getContextPath() %>/my-registrations"
               class="<%= currentURI.contains("/my-registrations") ? "active" : "" %>">My Registrations</a>
            <a href="<%= request.getContextPath() %>/admin/login">Admin Login</a>
        <% } else { %>
            <a href="<%= request.getContextPath() %>/admin/dashboard"
               class="<%= currentURI.contains("/admin/dashboard") ? "active" : "" %>">Dashboard</a>
            <a href="<%= request.getContextPath() %>/admin/add-event"
               class="<%= currentURI.contains("/admin/add-event") ? "active" : "" %>">Add Event</a>
            <a href="<%= request.getContextPath() %>/admin/login?action=logout" class="btn-outline btn-sm">Logout</a>
        <% } %>
    </nav>
</header>

