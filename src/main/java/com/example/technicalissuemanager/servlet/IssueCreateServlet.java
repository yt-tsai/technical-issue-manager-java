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

@WebServlet("/issues/create")
public class IssueCreateServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Issue issue = IssueFormValidator.validate(request);
            int issueId = issueDao.save(issue);
            response.sendRedirect(request.getContextPath() + "/issues/detail?id=" + issueId);
        } catch (IssueFormValidationException exception) {
            request.setAttribute("errors", exception.getErrors());
            showForm(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題の登録に失敗しました。", exception);
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/WEB-INF/jsp/issue-create.jsp").forward(request, response);
    }
}
