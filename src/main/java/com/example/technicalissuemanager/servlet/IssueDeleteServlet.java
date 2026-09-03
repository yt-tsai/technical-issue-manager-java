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

@WebServlet("/issues/delete")
public class IssueDeleteServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int issueId;
        try {
            issueId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "課題IDが正しくありません。");
            return;
        }

        try {
            if (!issueDao.deleteById(issueId)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された課題は存在しません。");
                return;
            }

            FlashMessage.setSuccess(request, "課題を削除しました。");

            response.sendRedirect(request.getContextPath() + "/issues");
        } catch (SQLException exception) {
            throw new ServletException("課題の削除に失敗しました。", exception);
        }
    }
}
