package servlets;

import dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * RegisterServlet - handles student self-registration.
 * Maps to /register
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fullName  = req.getParameter("fullName");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");
        String confirm   = req.getParameter("confirmPassword");

        // ── Validation ───────────────────────────────────────────────
        if (fullName == null || email == null || password == null ||
            fullName.isBlank() || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        // ── Register ─────────────────────────────────────────────────
        boolean success = userDAO.register(fullName.trim(), email.trim(), password);

        if (!success) {
            req.setAttribute("error", "Email already registered. Please login.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        // Redirect to login with success message
        resp.sendRedirect(req.getContextPath() + "/login?registered=true");
    }
}
