package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.IssueDao;
import com.example.technicalissuemanager.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/issues")
public class IssueListServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            request.setAttribute("successMessage", FlashMessage.consumeSuccess(request));
            request.setAttribute("issues", issueDao.findAll());
            request.getRequestDispatcher("/WEB-INF/jsp/issue-list.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題一覧の取得に失敗しました。", exception);
        }
    }
}
