package servlets.student;

import dao.ExamDAO;
import dao.ResultDAO;
import model.Exam;
import model.Question;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/take-exam")
public class TakeExamServlet extends HttpServlet {

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

        int examId = Integer.parseInt(req.getParameter("examId"));
        Exam exam  = examDAO.getExamById(examId);

        if (exam == null || !exam.isActive()) {
            resp.sendRedirect(req.getContextPath() + "/student/dashboard"); return;
        }

        // Block if already attempted
        if (resultDAO.hasAttempted(user.getId(), examId)) {
            resp.sendRedirect(req.getContextPath() + "/student/results?alreadyAttempted=true"); return;
        }

        List<Question> questions = examDAO.getQuestionsByExam(examId);
        if (questions.isEmpty()) {
            req.setAttribute("error", "This exam has no questions yet.");
            resp.sendRedirect(req.getContextPath() + "/student/dashboard"); return;
        }

        req.setAttribute("exam",      exam);
        req.setAttribute("questions", questions);
        req.getRequestDispatcher("/jsp/student/take-exam.jsp").forward(req, resp);
    }
}
