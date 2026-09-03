package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.dao.CommentDao;
import com.example.technicalissuemanager.dao.IssueDao;
import com.example.technicalissuemanager.model.Comment;
import com.example.technicalissuemanager.model.Issue;
import com.example.technicalissuemanager.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/comments/create")
public class CommentCreateServlet extends HttpServlet {

    private static final int SHORT_TEXT_MAX_LENGTH = 255;
    private static final int CONTENT_MAX_LENGTH = 5000;
    private static final int MAX_CC_RECIPIENTS = 20;

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

            Map<String, String> errors = new LinkedHashMap<>();
            Comment comment = createComment(request, defaultTo, errors);
            comment.setReplyTo(replyTarget == null ? null : replyTarget.getCommentId());

            if (!errors.isEmpty()) {
                request.setAttribute("commentErrors", errors);
                request.setAttribute("issueId", issueId);
                request.getRequestDispatcher("/issues/detail").forward(request, response);
                return;
            }

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

    private Comment createComment(
            HttpServletRequest request, String defaultTo, Map<String, String> errors) {
        String author = trimToEmpty(request.getParameter("author"));
        String to = trimToEmpty(request.getParameter("to"));
        String content = trimToEmpty(request.getParameter("content"));

        validateRequired("author", author, "投稿者", errors);
        validateMaxLength("author", author, "投稿者", SHORT_TEXT_MAX_LENGTH, errors);

        if (to.isEmpty()) {
            to = defaultTo;
        }
        validateMaxLength("to", to, "To", SHORT_TEXT_MAX_LENGTH, errors);

        List<String> cc = parseCc(request.getParameter("cc"), errors);

        validateRequired("content", content, "コメント内容", errors);
        validateMaxLength("content", content, "コメント内容", CONTENT_MAX_LENGTH, errors);

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setTo(to);
        comment.setCc(cc);
        comment.setContent(content);
        return comment;
    }

    private List<String> parseCc(String ccInput, Map<String, String> errors) {
        List<String> cc = new ArrayList<>();
        if (ccInput == null || ccInput.isBlank()) {
            return cc;
        }

        for (String name : ccInput.split(",")) {
            String trimmedName = name.trim();
            if (trimmedName.isEmpty()) {
                continue;
            }
            cc.add(trimmedName);
            if (trimmedName.length() > SHORT_TEXT_MAX_LENGTH) {
                errors.putIfAbsent(
                        "cc", "CCの各宛先は255文字以内で入力してください。");
            }
        }

        if (cc.size() > MAX_CC_RECIPIENTS) {
            errors.put("cc", "CCは20件以内で入力してください。");
        }
        return cc;
    }

    private void validateRequired(
            String fieldName, String value, String label, Map<String, String> errors) {
        if (value.isEmpty()) {
            errors.put(fieldName, label + "を入力してください。");
        }
    }

    private void validateMaxLength(
            String fieldName, String value, String label, int maxLength,
            Map<String, String> errors) {
        if (!value.isEmpty() && value.length() > maxLength) {
            errors.put(fieldName, label + "は" + maxLength + "文字以内で入力してください。");
        }
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
