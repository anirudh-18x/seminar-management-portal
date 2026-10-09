<%-- error-404.jsp  Custom 404 Page --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>

<%@ include file="header.jsp" %>

<main>

    <div class="empty-state" style="padding:80px 20px;">

        <div style="font-size:4rem;"></div>

        <h2 style="font-size:1.5rem; margin:16px 0 8px;">Page Not Found</h2>

        <p>The page you are looking for does not exist or has been moved.</p>

        <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-primary">Go Home</a>

    </div>

</main>

<%@ include file="footer.jsp" %>
