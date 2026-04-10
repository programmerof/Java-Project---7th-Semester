package servlets;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet - handles GET (show form) and POST (process login).
 * Maps to /login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    /** Show the login page */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // If already logged in, redirect to correct dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            redirect(resp, user);
            return;
        }
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    /** Process login form submission */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        // Basic validation
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Please fill in all fields.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.login(email.trim(), password.trim());

        if (user == null) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        // Login successful — create session
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId",   user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        redirect(resp, user);
    }

    /** Route user to correct dashboard based on role */
    private void redirect(HttpServletResponse resp, User user) throws IOException {
        if (user.isAdmin()) {
            resp.sendRedirect("admin/dashboard");
        } else {
            resp.sendRedirect("student/dashboard");
        }
    }
}
