package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.IssueDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/issues/search")
public class IssueSearchServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            request.getRequestDispatcher("/WEB-INF/jsp/issue-search.jsp").forward(request, response);
            return;
        }

        keyword = keyword.trim();
        if (keyword.isEmpty()) {
            request.setAttribute("errorMessage", "検索キーワードを入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/issue-search.jsp").forward(request, response);
            return;
        }

        try {
            request.setAttribute("keyword", keyword);
            request.setAttribute("issues", issueDao.findByKeyword(keyword));
            request.setAttribute("hasSearched", true);
            request.getRequestDispatcher("/WEB-INF/jsp/issue-search.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題検索に失敗しました。", exception);
        }
    }
}
