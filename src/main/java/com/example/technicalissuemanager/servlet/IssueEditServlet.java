package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.IssueDao;
import com.example.technicalissuemanager.model.Issue;
import com.example.technicalissuemanager.validation.IssueFormValidationException;
import com.example.technicalissuemanager.validation.IssueFormValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/issues/edit")
public class IssueEditServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int issueId = parseIssueId(request, response);
        if (issueId == 0) {
            return;
        }

        try {
            Optional<Issue> issue = issueDao.findById(issueId);
            if (issue.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された課題は存在しません。");
                return;
            }

            request.setAttribute("issue", issue.get());
            showForm(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題編集画面の取得に失敗しました。", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int issueId = parseIssueId(request, response);
        if (issueId == 0) {
            return;
        }

        try {
            Issue issue = IssueFormValidator.validate(request);
            issue.setId(issueId);

            if (!issueDao.update(issue)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された課題は存在しません。");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/issues/detail?id=" + issueId);
        } catch (IssueFormValidationException exception) {
            Issue formIssue = new Issue();
            formIssue.setId(issueId);
            request.setAttribute("issue", formIssue);
            request.setAttribute("errors", exception.getErrors());
            showForm(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題の更新に失敗しました。", exception);
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/WEB-INF/jsp/issue-edit.jsp").forward(request, response);
    }

    private int parseIssueId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            return Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "課題IDが正しくありません。");
            return 0;
        }
    }
}
