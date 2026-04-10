package servlets.student;

import dao.ExamDAO;
import dao.ResultDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

    private final ExamDAO   examDAO   = new ExamDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login"); return;
        }
        User user = (User) session.getAttribute("user");
        if (user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard"); return;
        }

        req.setAttribute("user",      user);
        req.setAttribute("exams",     examDAO.getActiveExams());
        req.setAttribute("results",   resultDAO.getResultsByStudent(user.getId()));
        req.setAttribute("resultDAO", resultDAO);
        req.getRequestDispatcher("/jsp/student/dashboard.jsp").forward(req, resp);
    }
}