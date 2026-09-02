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

@WebServlet("/issues/create")
public class IssueCreateServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/WEB-INF/jsp/issue-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Issue issue = createIssue(request);
            int issueId = issueDao.save(issue);
            response.sendRedirect(request.getContextPath() + "/issues/detail?id=" + issueId);
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            throw new ServletException("課題の登録に失敗しました。", exception);
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
