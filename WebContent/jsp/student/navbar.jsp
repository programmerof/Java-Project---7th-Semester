<%@ page contentType="text/html;charset=UTF-8" %>
<% model.User _u = (model.User) session.getAttribute("user"); %>
<nav class="navbar">
    <div class="nav-brand">&#128221; ExamPortal <span class="nav-role">Student</span></div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/student/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/student/results">My Results</a>
        <span class="nav-user">&#9679; <%= _u != null ? _u.getFullName() : "" %></span>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
    </div>
</nav>