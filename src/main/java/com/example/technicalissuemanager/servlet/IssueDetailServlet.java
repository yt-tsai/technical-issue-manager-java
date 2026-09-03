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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@WebServlet("/issues/detail")
public class IssueDetailServlet extends HttpServlet {

    private final IssueDao issueDao = new IssueDao();
    private final CommentDao commentDao = new CommentDao();

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

            List<Comment> comments = commentDao.findByIssueId(issueId);
            Comment replyTarget;
            try {
                replyTarget = findReplyTarget(request.getParameter("replyTo"), comments);
            } catch (IllegalArgumentException exception) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
                return;
            }

            request.setAttribute("issue", issue.get());
            request.setAttribute("commentThread", buildCommentThread(comments));
            request.setAttribute("replyTarget", replyTarget);
            request.setAttribute("successMessage", FlashMessage.consumeSuccess(request));
            request.getRequestDispatcher("/WEB-INF/jsp/issue-detail.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("課題詳細の取得に失敗しました。", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!(request.getAttribute("issueId") instanceof Integer)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // Internal forward used to redisplay a comment form with validation errors.
        doGet(request, response);
    }

    private int parseIssueId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Object forwardedIssueId = request.getAttribute("issueId");
        if (forwardedIssueId instanceof Integer) {
            return (Integer) forwardedIssueId;
        }

        try {
            int issueId = Integer.parseInt(request.getParameter("id"));
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

    private List<Comment> buildCommentThread(List<Comment> comments) {
        Map<Integer, Comment> commentsById = new LinkedHashMap<>();
        Map<Integer, List<Comment>> repliesByParentId = new LinkedHashMap<>();
        List<Comment> rootComments = new ArrayList<>();

        for (Comment comment : comments) {
            comment.setReplyToComment(null);
            comment.setThreadDepth(0);
            commentsById.put(comment.getCommentId(), comment);
        }

        for (Comment comment : comments) {
            Comment parent = comment.getReplyTo() == null
                    ? null
                    : commentsById.get(comment.getReplyTo());
            if (parent == null || parent == comment) {
                rootComments.add(comment);
            } else {
                comment.setReplyToComment(parent);
                repliesByParentId
                        .computeIfAbsent(parent.getCommentId(), ignored -> new ArrayList<>())
                        .add(comment);
            }
        }

        List<Comment> orderedComments = new ArrayList<>();
        Set<Integer> appendedIds = new HashSet<>();
        for (Comment rootComment : rootComments) {
            appendComment(rootComment, 0, repliesByParentId, appendedIds, orderedComments);
        }

        for (Comment comment : comments) {
            if (!appendedIds.contains(comment.getCommentId())) {
                appendComment(comment, 0, repliesByParentId, appendedIds, orderedComments);
            }
        }
        return orderedComments;
    }

    private void appendComment(
            Comment comment, int depth, Map<Integer, List<Comment>> repliesByParentId,
            Set<Integer> appendedIds, List<Comment> orderedComments) {
        if (!appendedIds.add(comment.getCommentId())) {
            return;
        }

        comment.setThreadDepth(depth);
        orderedComments.add(comment);
        for (Comment reply : repliesByParentId.getOrDefault(comment.getCommentId(), List.of())) {
            appendComment(reply, depth + 1, repliesByParentId, appendedIds, orderedComments);
        }
    }
}
