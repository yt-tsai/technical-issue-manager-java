<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.model.Comment" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%@ page import="com.example.technicalissuemanager.util.IssueViewHelper" %>
<%!
    private String commentFormValue(
            HttpServletRequest request, String fieldName, String defaultValue) {
        String value = request.getParameter(fieldName);
        return value == null ? defaultValue : value;
    }
%>
<%
    Issue issue = (Issue) request.getAttribute("issue");
    List<Comment> commentThread = (List<Comment>) request.getAttribute("commentThread");
    Comment replyTarget = (Comment) request.getAttribute("replyTarget");
    java.util.Map<String, String> commentErrors =
            (java.util.Map<String, String>) request.getAttribute("commentErrors");
    if (commentErrors == null) {
        commentErrors = java.util.Collections.emptyMap();
    }

    String successMessage = (String) request.getAttribute("successMessage");
    String dueDateLabel = IssueViewHelper.dueDateLabel(issue);
    String defaultTo = replyTarget == null ? issue.getAssignee() : replyTarget.getAuthor();
    String toValue = request.getParameter("to");
    if (toValue == null || toValue.isBlank()) {
        toValue = defaultTo;
    }
    java.time.format.DateTimeFormatter commentDateFormatter =
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題詳細 - 技術課題管理システム</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<main class="container">
    <a class="back-link" href="<%= request.getContextPath() %>/issues">← 課題一覧へ戻る</a>
<% if (successMessage != null) { %>
    <p class="success-message"><%= HtmlEscaper.escape(successMessage) %></p>
<% } %>

    <section class="issue">
        <h1>#<%= issue.getId() %> <%= HtmlEscaper.escape(issue.getTitle()) %></h1>
        <p class="meta">顧客：<%= HtmlEscaper.escape(issue.getCustomer()) %></p>
        <p class="meta">製品・プロジェクト：<%= HtmlEscaper.escape(issue.getProduct()) %></p>
        <p class="meta">
            優先度：<span class="badge <%= IssueViewHelper.priorityClass(issue.getPriority()) %>"><%= HtmlEscaper.escape(issue.getPriority()) %></span>
            ／ 状況：<span class="badge <%= IssueViewHelper.statusClass(issue.getStatus()) %>"><%= HtmlEscaper.escape(issue.getStatus()) %></span>
        </p>
        <p class="meta due-date <%= IssueViewHelper.dueDateClass(issue) %>">
            担当者：<%= HtmlEscaper.escape(issue.getAssignee()) %> ／ 期限：<%= issue.getDueDate() %>
<% if (!dueDateLabel.isEmpty()) { %>
            <span class="due-label"><%= HtmlEscaper.escape(dueDateLabel) %></span>
<% } %>
        </p>
        <p class="meta">進捗率：<%= issue.getProgress() %>%</p>
        <div class="progress" aria-label="進捗率 <%= issue.getProgress() %>%">
            <div class="progress-bar" style="width: <%= issue.getProgress() %>%"></div>
        </div>
        <h2>詳細説明</h2>
        <p class="description"><%= HtmlEscaper.escape(issue.getDescription()) %></p>
        <div class="actions">
            <a class="button-link" href="<%= request.getContextPath() %>/issues/edit?id=<%= issue.getId() %>">編集</a>
            <form class="delete-form" action="<%= request.getContextPath() %>/issues/delete?id=<%= issue.getId() %>" method="post" onsubmit="return confirm('この課題を削除しますか？ この操作は元に戻せません。');">
                <button class="delete-button" type="submit">削除</button>
            </form>
        </div>
    </section>

    <section id="comments" class="comments">
        <div class="comments-heading">
            <h2>コメント（<%= commentThread.size() %>件）</h2>
            <a class="button-link" href="#comment-form">コメントを追加</a>
        </div>
<% if (commentThread.isEmpty()) { %>
        <p>コメントはまだありません。</p>
<% } else {
       for (Comment comment : commentThread) {
           int displayDepth = Math.min(comment.getThreadDepth(), 3);
           Comment parentComment = comment.getReplyToComment();
           String formattedCreatedAt = comment.getCreatedAt() == null
                   ? ""
                   : comment.getCreatedAt().format(commentDateFormatter); %>
        <article id="comment-<%= comment.getCommentId() %>"
                 class="comment<%= comment.getThreadDepth() > 0
                         ? " comment-reply comment-depth-" + displayDepth
                         : "" %>">
            <div class="comment-header">
                <h3>#<%= comment.getCommentId() %> <%= HtmlEscaper.escape(comment.getAuthor()) %></h3>
<%         if (!formattedCreatedAt.isEmpty()) { %>
                <time datetime="<%= comment.getCreatedAt() %>"><%= formattedCreatedAt %></time>
<%         } %>
            </div>
<%         if (parentComment != null) { %>
            <p class="reply-context">↳
                <a href="#comment-<%= parentComment.getCommentId() %>">
                    #<%= parentComment.getCommentId() %> <%= HtmlEscaper.escape(parentComment.getAuthor()) %>
                </a>
                への返信
            </p>
<%         } %>
            <p class="meta comment-recipients">
                To：<%= HtmlEscaper.escape(comment.getTo()) %>
                ／ CC：<%= HtmlEscaper.escape(
                        comment.getCc().isEmpty() ? "なし" : String.join(", ", comment.getCc())) %>
            </p>
            <p class="comment-content"><%= HtmlEscaper.escape(comment.getContent()) %></p>
            <a class="reply-link" href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>&replyTo=<%= comment.getCommentId() %>#comment-form">返信する</a>
        </article>
<%     }
   } %>

        <form id="comment-form" class="comment-form"
              action="<%= request.getContextPath() %>/comments/create#comment-form" method="post">
            <input type="hidden" name="issueId" value="<%= issue.getId() %>">
            <h3><%= replyTarget == null ? "コメントを追加" : "返信を追加" %></h3>

<% if (replyTarget != null) { %>
            <input type="hidden" name="replyTo" value="<%= replyTarget.getCommentId() %>">
            <div class="reply-target">
                <p class="reply-target-title">
                    #<%= replyTarget.getCommentId() %> <%= HtmlEscaper.escape(replyTarget.getAuthor()) %> への返信
                </p>
                <p class="comment-content"><%= HtmlEscaper.escape(replyTarget.getContent()) %></p>
            </div>
            <p><a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>#comment-form">返信を取り消す</a></p>
<% } %>

<% if (!commentErrors.isEmpty()) { %>
            <p class="error-message">入力内容を確認してください。</p>
<% } %>
            <div class="field">
                <label for="author">投稿者</label>
                <input id="author" name="author" type="text" maxlength="255"
                       value="<%= HtmlEscaper.escape(commentFormValue(request, "author", "")) %>"
                       class="<%= commentErrors.containsKey("author") ? "input-error" : "" %>" required>
<% if (commentErrors.containsKey("author")) { %>
                <p class="field-error"><%= HtmlEscaper.escape(commentErrors.get("author")) %></p>
<% } %>
            </div>
            <div class="field">
                <label for="to">To</label>
                <input id="to" name="to" type="text" maxlength="255"
                       value="<%= HtmlEscaper.escape(toValue) %>"
                       class="<%= commentErrors.containsKey("to") ? "input-error" : "" %>">
<% if (commentErrors.containsKey("to")) { %>
                <p class="field-error"><%= HtmlEscaper.escape(commentErrors.get("to")) %></p>
<% } %>
            </div>
            <div class="field">
                <label for="cc">CC（カンマ区切りで複数可、最大20件）</label>
                <input id="cc" name="cc" type="text"
                       value="<%= HtmlEscaper.escape(commentFormValue(request, "cc", "")) %>"
                       class="<%= commentErrors.containsKey("cc") ? "input-error" : "" %>">
<% if (commentErrors.containsKey("cc")) { %>
                <p class="field-error"><%= HtmlEscaper.escape(commentErrors.get("cc")) %></p>
<% } %>
            </div>
            <div class="field">
                <label for="content">コメント内容（最大5000文字）</label>
                <textarea id="content" name="content" maxlength="5000"
                          class="<%= commentErrors.containsKey("content") ? "input-error" : "" %>"
                          required><%= HtmlEscaper.escape(
                                  commentFormValue(request, "content", "")) %></textarea>
<% if (commentErrors.containsKey("content")) { %>
                <p class="field-error"><%= HtmlEscaper.escape(commentErrors.get("content")) %></p>
<% } %>
            </div>
            <button class="button-primary" type="submit">
                <%= replyTarget == null ? "コメントを追加" : "返信を追加" %>
            </button>
        </form>
    </section>
</main>
</body>
</html>
