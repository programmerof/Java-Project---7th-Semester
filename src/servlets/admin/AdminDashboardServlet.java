package servlets.admin;

import dao.ExamDAO;
import model.Exam;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final ExamDAO examDAO = new ExamDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Auth guard
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (!user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
            return;
        }

        List<Exam> exams = examDAO.getAllExams();
        req.setAttribute("exams", exams);
        req.setAttribute("totalExams", exams.size());
        long activeCount = exams.stream().filter(Exam::isActive).count();
        req.setAttribute("activeExams", activeCount);

        req.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(req, resp);
    }

    /** Handle toggle active / delete from dashboard */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        int examId = Integer.parseInt(req.getParameter("examId"));

        if ("toggle".equals(action)) {
            examDAO.toggleActive(examId);
        } else if ("delete".equals(action)) {
            examDAO.deleteExam(examId);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}
