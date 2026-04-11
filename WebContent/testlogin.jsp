<%@ page import="java.sql.*" %>
<%
    String url = "jdbc:mysql://127.0.0.1:3306/online_exam_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    String user = "root";
    String pass = "qwerty";
    String testEmail = "admin@exam.com";
    String testPass  = "admin123";
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pass);

        // Test exact query UserDAO uses
        PreparedStatement ps = con.prepareStatement(
            "SELECT id, full_name, email, role FROM users WHERE email = ? AND password = ?"
        );
        ps.setString(1, testEmail);
        ps.setString(2, testPass);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            out.println("<h2 style='color:green'>✅ Login works! User: " + rs.getString("full_name") + " Role: " + rs.getString("role") + "</h2>");
        } else {
            out.println("<h2 style='color:red'>❌ Query returned no results</h2>");
            // Check what's actually stored
            PreparedStatement ps2 = con.prepareStatement("SELECT email, password, LENGTH(password) as len FROM users");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                out.println("<p>Email: [" + rs2.getString("email") + "] Pass: [" + rs2.getString("password") + "] Length: " + rs2.getInt("len") + "</p>");
            }
        }
        con.close();
    } catch (Exception e) {
        out.println("<h2 style='color:red'>Error: " + e.getMessage() + "</h2>");
    }
%>