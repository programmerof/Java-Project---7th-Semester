package servlets.admin;

import dao.ResultDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/results")
public class AdminResultsServlet extends HttpServlet {

    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !((User) session.getAttribute("user")).isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String detailParam = req.getParameter("resultId");
        if (detailParam != null) {
            // Show detailed result view
            int resultId = Integer.parseInt(detailParam);
            req.setAttribute("result", resultDAO.getResultDetail(resultId));
            req.getRequestDispatcher("/jsp/admin/result-detail.jsp").forward(req, resp);
        } else {
            // Show all results list
            req.setAttribute("results", resultDAO.getAllResults());
            req.getRequestDispatcher("/jsp/admin/results.jsp").forward(req, resp);
        }
    }
}
