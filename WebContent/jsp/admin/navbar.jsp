<%@ page contentType="text/html;charset=UTF-8" %>
<% model.User _u = (model.User) session.getAttribute("user"); %>
<nav class="navbar">
    <div class="nav-brand">&#128221; ExamPortal <span class="nav-role">Admin</span></div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/create-exam">New Exam</a>
        <a href="${pageContext.request.contextPath}/admin/results">Results</a>
        <span class="nav-user">&#9679; <%= _u != null ? _u.getFullName() : "" %></span>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
    </div>
</nav>