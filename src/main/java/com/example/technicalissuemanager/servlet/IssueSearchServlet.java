package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.IssueDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/issues/search")
public class IssueSearchServlet extends HttpServlet {

    private static final int MAX_KEYWORD_LENGTH = 100;
    private static final String DEFAULT_SORT = "id-asc";
    private static final Set<String> VALID_PRIORITIES = Set.of("", "High", "Medium", "Low");
    private static final Set<String> VALID_STATUSES = Set.of("", "Open", "In Progress", "Resolved");
    private static final Set<String> VALID_SORTS = Set.of(
            "id-asc", "updated-desc", "due-asc", "priority-desc");

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!hasSearchParameters(request)) {
            setFormValues(request, "", "", "", DEFAULT_SORT);
            request.getRequestDispatcher("/WEB-INF/jsp/issue-search.jsp").forward(request, response);
            return;
        }

        String keyword = trimToEmpty(request.getParameter("keyword"));
        String priority = trimToEmpty(request.getParameter("priority"));
        String status = trimToEmpty(request.getParameter("status"));
        String sort = trimToEmpty(request.getParameter("sort"));
        if (sort.isEmpty()) {
            sort = DEFAULT_SORT;
        }

        setFormValues(request, keyword, priority, status, sort);

        String validationError = validateSearchParameters(keyword, priority, status, sort);
        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            request.getRequestDispatcher("/WEB-INF/jsp/issue-search.jsp").forward(request, response);
            return;
        }

        try {
            request.setAttribute("issues", issueDao.search(keyword, priority, status, sort));
            request.setAttribute("hasSearched", true);
            request.getRequestDispatcher("/WEB-INF/jsp/issue-search.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題検索に失敗しました。", exception);
        }
    }

    private boolean hasSearchParameters(HttpServletRequest request) {
        return request.getParameter("keyword") != null
                || request.getParameter("priority") != null
                || request.getParameter("status") != null
                || request.getParameter("sort") != null;
    }

    private String validateSearchParameters(
            String keyword, String priority, String status, String sort) {
        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            return "検索キーワードは100文字以内で入力してください。";
        }
        if (!VALID_PRIORITIES.contains(priority)
                || !VALID_STATUSES.contains(status)
                || !VALID_SORTS.contains(sort)) {
            return "指定された検索条件が正しくありません。";
        }
        return null;
    }

    private void setFormValues(
            HttpServletRequest request, String keyword, String priority, String status, String sort) {
        request.setAttribute("keyword", keyword);
        request.setAttribute("priority", priority);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
