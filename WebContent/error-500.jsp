<%-- error-500.jsp  Custom 500 Internal Server Error Page --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>

<%@ include file="header.jsp" %>

<main>

    <div class="empty-state" style="padding:80px 20px;">

        <div style="font-size:4rem;"></div>

        <h2 style="font-size:1.5rem; margin:16px 0 8px;">Internal Server Error</h2>

        <p>Something went wrong on the server. Please check Tomcat's logs for details.</p>

        <% if (exception != null) { %>

            <div class="alert alert-error" style="text-align:left; max-width:600px; margin:20px auto;">

                <strong>Error:</strong> <%= exception.getMessage() %>

            </div>

        <% } %>

        <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-primary">Go Home</a>

    </div>

</main>

<%@ include file="footer.jsp" %>
