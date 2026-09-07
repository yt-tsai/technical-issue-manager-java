package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.CommentDao;
import com.example.technicalissuemanager.dao.IssueDao;
import com.example.technicalissuemanager.model.Comment;
import com.example.technicalissuemanager.model.Issue;
import com.example.technicalissuemanager.util.FlashMessage;
import com.example.technicalissuemanager.validation.CommentFormValidationException;
import com.example.technicalissuemanager.validation.CommentFormValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
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

            List<Comment> comments = commentDao.findByIssueId(issueId);
            Comment replyTarget = findReplyTarget(request.getParameter("replyTo"), comments);
            String defaultTo = replyTarget == null
                    ? issue.get().getAssignee()
                    : replyTarget.getAuthor();

            Comment comment;
            try {
                comment = CommentFormValidator.validate(request, defaultTo);
            } catch (CommentFormValidationException exception) {
                request.setAttribute("commentErrors", exception.getErrors());
                request.setAttribute("issueId", issueId);
                request.getRequestDispatcher("/issues/detail").forward(request, response);
                return;
            }
            comment.setReplyTo(replyTarget == null ? null : replyTarget.getCommentId());

            commentDao.save(issueId, comment);
            String successMessage = comment.getReplyTo() == null
                    ? "コメントを追加しました。"
                    : "返信を追加しました。";
            FlashMessage.setSuccess(request, successMessage);
            response.sendRedirect(
                    request.getContextPath() + "/issues/detail?id=" + issueId + "#comments");
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            throw new ServletException("コメントの登録に失敗しました。", exception);
        }
    }

    private int parseIssueId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int issueId = Integer.parseInt(request.getParameter("issueId"));
            if (issueId <= 0) {
                throw new NumberFormatException();
            }
            return issueId;
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "課題IDが正しくありません。");
            return 0;
        }
    }

    private Comment findReplyTarget(String replyToValue, List<Comment> comments) {
        if (replyToValue == null || replyToValue.isBlank()) {
            return null;
        }

        try {
            int replyTo = Integer.parseInt(replyToValue);
            if (replyTo <= 0) {
                throw new NumberFormatException();
            }

            for (Comment comment : comments) {
                if (comment.getCommentId() == replyTo) {
                    return comment;
                }
            }
            throw new IllegalArgumentException("返信元のコメントが見つかりません。");
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("返信元のコメント番号が正しくありません。");
        }
    }
}
