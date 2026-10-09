<%--

  admin-setup.jsp  Admin Account Setup Page

  Used to create new admin accounts.

--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%

    request.setAttribute("pageTitle", "Admin Setup");

    String errorMsg   = (String) request.getAttribute("errorMessage");

    String successMsg = (String) request.getAttribute("successMessage");

%>

<%@ include file="header.jsp" %>



<main>

    <div class="form-card" style="margin-top: 40px;">

        <h2> Admin Account Setup</h2>

        <p style="color:var(--text-light); font-size:0.88rem; margin-bottom:20px;">

            Create a new administrator account. This page is for initial setup only.

        </p>



        <% if (errorMsg != null) { %>

            <div class="alert alert-error"><%= errorMsg %></div>

        <% } %>

        <% if (successMsg != null) { %>

            <div class="alert alert-success"><%= successMsg %></div>

        <% } %>



        <form method="post" action="<%= request.getContextPath() %>/admin/setup">

            <div class="form-group">

                <label for="username">Username *</label>

                <input type="text" id="username" name="username" maxlength="100" placeholder="Admin username">

            </div>

            <div class="form-group">

                <label for="password">Password * (min 6 characters)</label>

                <input type="password" id="password" name="password" maxlength="100">

            </div>

            <div class="form-group">

                <label for="confirmPassword">Confirm Password *</label>

                <input type="password" id="confirmPassword" name="confirmPassword" maxlength="100">

            </div>

            <button type="submit" class="btn btn-success" style="width:100%;">Create Admin Account</button>

        </form>



        <p style="text-align:center; margin-top:16px; font-size:0.85rem;">

            <a href="<%= request.getContextPath() %>/admin/login">Go to Admin Login</a>

        </p>

    </div>

</main>



<%@ include file="footer.jsp" %>
