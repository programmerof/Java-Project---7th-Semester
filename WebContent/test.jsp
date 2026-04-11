<%@ page import="java.sql.*" %>
<%
    String url  = "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&serverTimezone=UTC";
    String user = "root";
    String pass = "qwerty";
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pass);
        out.println("<h2 style='color:green'>✅ DB Connected!</h2>");
        PreparedStatement ps = con.prepareStatement("SELECT email, password FROM users");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            out.println("<p>Email: " + rs.getString("email") + " | Pass: " + rs.getString("password") + "</p>");
        }
        con.close();
    } catch (Exception e) {
        out.println("<h2 style='color:red'>❌ Error: " + e.getMessage() + "</h2>");
    }
%>