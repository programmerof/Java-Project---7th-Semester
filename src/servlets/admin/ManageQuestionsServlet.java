package servlets.admin;

import dao.ExamDAO;
import model.Exam;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/questions")
public class ManageQuestionsServlet extends HttpServlet {

    private final ExamDAO examDAO = new ExamDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isAdmin(req)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        int examId = Integer.parseInt(req.getParameter("examId"));
        Exam exam = examDAO.getExamById(examId);
        if (exam == null) { resp.sendRedirect(req.getContextPath() + "/admin/dashboard"); return; }

        req.setAttribute("exam", exam);
        req.setAttribute("questions", examDAO.getQuestionsByExam(examId));
        req.getRequestDispatcher("/jsp/admin/questions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isAdmin(req)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        String action = req.getParameter("action");
        int examId    = Integer.parseInt(req.getParameter("examId"));

        if ("add".equals(action)) {
            String text       = req.getParameter("questionText");
            String optA       = req.getParameter("optA");
            String optB       = req.getParameter("optB");
            String optC       = req.getParameter("optC");
            String optD       = req.getParameter("optD");
            String correctOpt = req.getParameter("correctOpt");

            if (text == null || text.isBlank()) {
                req.setAttribute("error", "Question text cannot be empty.");
            } else {
                boolean ok = examDAO.addQuestion(examId, text, optA, optB, optC, optD, correctOpt);
                if (ok) req.setAttribute("success", "Question added successfully.");
                else    req.setAttribute("error", "Failed to add question.");
            }

        } else if ("delete".equals(action)) {
            int questionId = Integer.parseInt(req.getParameter("questionId"));
            examDAO.deleteQuestion(questionId);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/questions?examId=" + examId);
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("user") != null
               && ((User) s.getAttribute("user")).isAdmin();
    }
}
