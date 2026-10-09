<%--

  login.jsp  Administrator Login Page (View)

  

  Data set by AdminLoginServlet:

  - "errorMessage" : String (on failed login)

--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%

    request.setAttribute("pageTitle", "Admin Login");

    String errorMsg = (String) request.getAttribute("errorMessage");

%>

<%@ include file="header.jsp" %>



<main>

    <div class="form-card" style="margin-top: 40px;">

        <h2> Administrator Login</h2>

        

        <% if (errorMsg != null) { %>

            <div class="alert alert-error"><%= errorMsg %></div>

        <% } %>



        <form method="post" action="<%= request.getContextPath() %>/admin/login">

            

            <div class="form-group">

                <label for="username">Username</label>

                <input type="text" id="username" name="username"

                       placeholder="Admin username" autocomplete="username" maxlength="100">

            </div>



            <div class="form-group">

                <label for="password">Password</label>

                <input type="password" id="password" name="password"

                       placeholder="Password" autocomplete="current-password" maxlength="100">

            </div>



            <button type="submit" class="btn btn-primary" style="width:100%; margin-top:8px;">

                Login

            </button>

        </form>



        

        <p style="text-align:center; font-size:0.85rem; margin-top:8px;">

            <a href="<%= request.getContextPath() %>/index.jsp">&larr; Back to Home</a>

        </p>

    </div>

</main>



<%@ include file="footer.jsp" %>

