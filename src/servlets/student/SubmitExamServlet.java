package servlets.student;

import dao.ExamDAO;
import dao.ResultDAO;
import model.Question;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/student/submit")
public class SubmitExamServlet extends HttpServlet {

    private final ExamDAO   examDAO   = new ExamDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login"); return;
        }
        User user   = (User) session.getAttribute("user");
        int examId  = Integer.parseInt(req.getParameter("examId"));
        int studentId = user.getId();

        List<Question> questions = examDAO.getQuestionsByExam(examId);

        // Collect answers: param name = "answer_<questionId>"
        Map<Integer, String> answers = new HashMap<>();
        for (Question q : questions) {
            String chosen = req.getParameter("answer_" + q.getId());
            answers.put(q.getId(), chosen); // null if skipped
        }

        int resultId = resultDAO.submitExam(studentId, examId, questions, answers);

        if (resultId == -2) {
            // Already attempted
            resp.sendRedirect(req.getContextPath() + "/student/results?alreadyAttempted=true");
        } else if (resultId > 0) {
            resp.sendRedirect(req.getContextPath() + "/student/results?resultId=" + resultId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/student/dashboard?error=submitFailed");
        }
    }
}
