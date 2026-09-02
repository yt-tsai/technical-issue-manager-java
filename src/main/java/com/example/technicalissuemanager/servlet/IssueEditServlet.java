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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@WebServlet("/issues/edit")
public class IssueEditServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

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
            request.getRequestDispatcher("/WEB-INF/jsp/issue-edit.jsp").forward(request, response);
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
            Issue issue = createIssue(request);
            issue.setId(issueId);

            if (!issueDao.update(issue)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された課題は存在しません。");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/issues/detail?id=" + issueId);
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            throw new ServletException("課題の更新に失敗しました。", exception);
        }
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

    private Issue createIssue(HttpServletRequest request) {
        Issue issue = new Issue();
        issue.setTitle(requiredValue(request, "title"));
        issue.setCustomer(requiredValue(request, "customer"));
        issue.setProduct(requiredValue(request, "product"));
        issue.setPriority(requiredValue(request, "priority"));
        issue.setStatus(requiredValue(request, "status"));
        issue.setAssignee(requiredValue(request, "assignee"));
        issue.setDescription(requiredValue(request, "description"));

        try {
            issue.setProgress(Integer.parseInt(requiredValue(request, "progress")));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("進捗率は0から100までの整数で入力してください。");
        }

        if (issue.getProgress() < 0 || issue.getProgress() > 100) {
            throw new IllegalArgumentException("進捗率は0から100までの整数で入力してください。");
        }

        try {
            issue.setDueDate(LocalDate.parse(requiredValue(request, "dueDate")));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("期限を正しい日付で入力してください。");
        }

        return issue;
    }

    private String requiredValue(HttpServletRequest request, String parameterName) {
        String value = request.getParameter(parameterName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("必須項目を入力してください。");
        }
        return value.trim();
    }
}
