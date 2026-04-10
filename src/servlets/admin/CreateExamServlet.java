package servlets.admin;

import dao.ExamDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/create-exam")
public class CreateExamServlet extends HttpServlet {

    private final ExamDAO examDAO = new ExamDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        guardAdmin(req, resp);
        req.getRequestDispatcher("/jsp/admin/create-exam.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        guardAdmin(req, resp);

        String title       = req.getParameter("title");
        String description = req.getParameter("description");
        String durStr      = req.getParameter("durationMins");

        if (title == null || title.isBlank()) {
            req.setAttribute("error", "Exam title is required.");
            req.getRequestDispatcher("/jsp/admin/create-exam.jsp").forward(req, resp);
            return;
        }

        int duration = 30;
        try { duration = Integer.parseInt(durStr); } catch (NumberFormatException ignored) {}

        User user = (User) req.getSession().getAttribute("user");
        int examId = examDAO.createExam(title.trim(), description, duration, user.getId());

        if (examId > 0) {
            // Redirect to add questions page for this new exam
            resp.sendRedirect(req.getContextPath() + "/admin/questions?examId=" + examId + "&created=true");
        } else {
            req.setAttribute("error", "Failed to create exam. Please try again.");
            req.getRequestDispatcher("/jsp/admin/create-exam.jsp").forward(req, resp);
        }
    }

    private void guardAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null ||
            !((User) session.getAttribute("user")).isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
