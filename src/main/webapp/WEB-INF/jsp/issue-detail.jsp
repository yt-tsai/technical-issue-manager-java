<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.model.Comment" %>
<%
    Issue issue = (Issue) request.getAttribute("issue");
    List<Comment> comments = (List<Comment>) request.getAttribute("comments");
    String replyTo = request.getParameter("replyTo");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題詳細 - 技術課題管理システム</title>
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        .issue, .comment { border: 1px solid #ccc; border-radius: 8px; padding: 1.5rem; max-width: 760px; }
        .meta { color: #555; margin: 0.6rem 0; }
        .description, .comment-content { white-space: pre-wrap; line-height: 1.6; }
        .progress { background: #eee; border-radius: 4px; height: 1rem; overflow: hidden; }
        .progress-bar { background: #3b82f6; height: 100%; }
        .back-link { display: inline-block; margin-bottom: 1.5rem; }
        .edit-link { display: inline-block; margin-top: 1rem; }
        .actions { display: flex; align-items: center; gap: 1rem; margin-top: 1rem; }
        .delete-form { margin: 0; }
        .delete-button { color: #b91c1c; cursor: pointer; }
        .comments { margin-top: 2rem; border-top: 1px solid #ccc; padding-top: 1rem; max-width: 760px; }
        .comment { margin: 1rem 0; }
        .comment h3 { margin-top: 0; }
        .reply-link { display: inline-block; margin-top: 0.5rem; }
        .comment-form { margin-top: 1.5rem; }
        .field { margin: 1rem 0; }
        label { display: block; font-weight: bold; margin-bottom: 0.35rem; }
        input, textarea { box-sizing: border-box; width: 100%; padding: 0.55rem; font: inherit; }
        textarea { min-height: 7rem; resize: vertical; }
        button { padding: 0.6rem 1rem; font: inherit; cursor: pointer; }
    </style>
</head>
<body>
    <h1>技術課題管理システム</h1>
    <a class="back-link" href="<%= request.getContextPath() %>/issues">← 課題一覧へ戻る</a>

    <section class="issue">
        <h2>#<%= issue.getId() %> <%= issue.getTitle() %></h2>
        <p class="meta">顧客：<%= issue.getCustomer() %></p>
        <p class="meta">製品・プロジェクト：<%= issue.getProduct() %></p>
        <p class="meta">優先度：<%= issue.getPriority() %> ／ 状況：<%= issue.getStatus() %></p>
        <p class="meta">進捗率：<%= issue.getProgress() %>%</p>
        <div class="progress" aria-label="進捗率 <%= issue.getProgress() %>%">
            <div class="progress-bar" style="width: <%= issue.getProgress() %>%"></div>
        </div>
        <p class="meta">担当者：<%= issue.getAssignee() %> ／ 期限：<%= issue.getDueDate() %></p>
        <h3>詳細説明</h3>
        <p class="description"><%= issue.getDescription() %></p>
        <div class="actions">
            <a class="edit-link" href="<%= request.getContextPath() %>/issues/edit?id=<%= issue.getId() %>">編集</a>
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
            <p class="meta">投稿者：<%= comment.getAuthor() %></p>
            <p class="meta">To：<%= comment.getTo() %></p>
            <p class="meta">CC：<%= comment.getCc().isEmpty() ? "なし" : String.join(", ", comment.getCc()) %></p>
            <p class="comment-content"><%= comment.getContent() %></p>
            <p class="meta">日時：<%= comment.getCreatedAt() %></p>
            <a class="reply-link" href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>&replyTo=<%= comment.getCommentId() %>#comment-form">返信</a>
        </article>
<%     }
   } %>

        <form id="comment-form" class="comment-form" action="<%= request.getContextPath() %>/comments/create" method="post">
            <input type="hidden" name="issueId" value="<%= issue.getId() %>">
<% if (replyTo != null && !replyTo.isBlank()) { %>
            <input type="hidden" name="replyTo" value="<%= replyTo %>">
            <h3>#<%= replyTo %> への返信</h3>
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
                <input id="to" name="to" type="text" maxlength="255" value="<%= issue.getAssignee() %>">
            </div>
            <div class="field">
                <label for="cc">CC（カンマ区切りで複数可）</label>
                <input id="cc" name="cc" type="text">
            </div>
            <div class="field">
                <label for="content">コメント内容</label>
                <textarea id="content" name="content" required></textarea>
            </div>
            <button type="submit">コメントを追加</button>
        </form>
    </section>
</body>
</html>
