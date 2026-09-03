<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.model.Comment" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%@ page import="com.example.technicalissuemanager.util.IssueViewHelper" %>
<%
    Issue issue = (Issue) request.getAttribute("issue");
    List<Comment> comments = (List<Comment>) request.getAttribute("comments");
    String replyTo = request.getParameter("replyTo");
    String successMessage = (String) request.getAttribute("successMessage");
    String dueDateLabel = IssueViewHelper.dueDateLabel(issue);
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

    <section class="comments">
        <h2>コメント</h2>
<% if (comments.isEmpty()) { %>
        <p>コメントはまだありません。</p>
<% } else {
       for (Comment comment : comments) { %>
        <article class="comment">
            <h3>#<%= comment.getCommentId() %></h3>
<%         if (comment.getReplyTo() != null) { %>
            <p class="meta">返信元：#<%= comment.getReplyTo() %></p>
<%         } %>
            <p class="meta">投稿者：<%= HtmlEscaper.escape(comment.getAuthor()) %></p>
            <p class="meta">To：<%= HtmlEscaper.escape(comment.getTo()) %></p>
            <p class="meta">CC：<%= HtmlEscaper.escape(comment.getCc().isEmpty() ? "なし" : String.join(", ", comment.getCc())) %></p>
            <p class="comment-content"><%= HtmlEscaper.escape(comment.getContent()) %></p>
            <p class="meta">日時：<%= comment.getCreatedAt() %></p>
            <a class="reply-link" href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>&replyTo=<%= comment.getCommentId() %>#comment-form">返信</a>
        </article>
<%     }
   } %>

        <form id="comment-form" class="comment-form" action="<%= request.getContextPath() %>/comments/create" method="post">
            <input type="hidden" name="issueId" value="<%= issue.getId() %>">
<% if (replyTo != null && !replyTo.isBlank()) { %>
            <input type="hidden" name="replyTo" value="<%= HtmlEscaper.escape(replyTo) %>">
            <h3>#<%= HtmlEscaper.escape(replyTo) %> への返信</h3>
            <p><a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>#comment-form">返信を取り消す</a></p>
<% } else { %>
            <h3>コメントを追加</h3>
<% } %>
            <div class="field">
                <label for="author">投稿者</label>
                <input id="author" name="author" type="text" maxlength="255" required>
            </div>
            <div class="field">
                <label for="to">To</label>
                <input id="to" name="to" type="text" maxlength="255" value="<%= HtmlEscaper.escape(issue.getAssignee()) %>">
            </div>
            <div class="field">
                <label for="cc">CC（カンマ区切りで複数可）</label>
                <input id="cc" name="cc" type="text">
            </div>
            <div class="field">
                <label for="content">コメント内容</label>
                <textarea id="content" name="content" required></textarea>
            </div>
            <button class="button-primary" type="submit">コメントを追加</button>
        </form>
    </section>
</main>
</body>
</html>
