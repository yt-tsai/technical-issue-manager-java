package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.CommentDao;
import com.example.technicalissuemanager.dao.IssueDao;
import com.example.technicalissuemanager.model.Comment;
import com.example.technicalissuemanager.model.Issue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/comments/create")
public class CommentCreateServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();
    private final CommentDao commentDao = new CommentDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int issueId;
        try {
            issueId = Integer.parseInt(request.getParameter("issueId"));
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

            Comment comment = createComment(request, issue.get());
            comment.setReplyTo(parseReplyTo(request, issueId));
            commentDao.save(issueId, comment);
            response.sendRedirect(request.getContextPath() + "/issues/detail?id=" + issueId);
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            throw new ServletException("コメントの登録に失敗しました。", exception);
        }
    }

    private Integer parseReplyTo(HttpServletRequest request, int issueId) throws SQLException {
        String replyToValue = request.getParameter("replyTo");
        if (replyToValue == null || replyToValue.isBlank()) {
            return null;
        }

        try {
            int replyTo = Integer.parseInt(replyToValue);
            if (!commentDao.existsByIdAndIssueId(replyTo, issueId)) {
                throw new IllegalArgumentException("返信元のコメントが見つかりません。");
            }
            return replyTo;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("返信元のコメント番号が正しくありません。");
        }
    }

    private Comment createComment(HttpServletRequest request, Issue issue) {
        Comment comment = new Comment();
        comment.setAuthor(requiredValue(request, "author"));

        String to = request.getParameter("to");
        comment.setTo(to == null || to.isBlank() ? issue.getAssignee() : to.trim());
        comment.setCc(parseCc(request.getParameter("cc")));
        comment.setContent(requiredValue(request, "content"));
        return comment;
    }

    private List<String> parseCc(String ccInput) {
        List<String> cc = new ArrayList<>();
        if (ccInput == null || ccInput.isBlank()) {
            return cc;
        }

        for (String name : ccInput.split(",")) {
            if (!name.isBlank()) {
                cc.add(name.trim());
            }
        }
        return cc;
    }

    private String requiredValue(HttpServletRequest request, String parameterName) {
        String value = request.getParameter(parameterName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("必須項目を入力してください。");
        }
        return value.trim();
    }
}
