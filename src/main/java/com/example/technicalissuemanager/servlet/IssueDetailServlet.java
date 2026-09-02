package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.IssueDao;
import com.example.technicalissuemanager.model.Issue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/issues/detail")
public class IssueDetailServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        int issueId;
        try {
            issueId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "課題IDが正しくありません。");
            return;
        }

        try {
            Optional<Issue> issue = issueDao.findById(issueId);
            if (issue.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された課題は存在しません。");
                return;
            }

            request.setAttribute("issue", issue.get());
            request.getRequestDispatcher("/WEB-INF/jsp/issue-detail.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題詳細の取得に失敗しました。", exception);
        }
    }
}
