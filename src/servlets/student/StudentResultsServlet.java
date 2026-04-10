package servlets.student;

import dao.ResultDAO;
import model.Result;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/student/results")
public class StudentResultsServlet extends HttpServlet {

    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login"); return;
        }
        User user = (User) session.getAttribute("user");

        String resultIdParam = req.getParameter("resultId");
        if (resultIdParam != null) {
            // Show single result detail
            Result result = resultDAO.getResultDetail(Integer.parseInt(resultIdParam));
            req.setAttribute("result", result);
            req.getRequestDispatcher("/jsp/student/result-detail.jsp").forward(req, resp);
        } else {
            // Show all results for this student
            req.setAttribute("results", resultDAO.getResultsByStudent(user.getId()));
            req.getRequestDispatcher("/jsp/student/results.jsp").forward(req, resp);
        }
    }
}
