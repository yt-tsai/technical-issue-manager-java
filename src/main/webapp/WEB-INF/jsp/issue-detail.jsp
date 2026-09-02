<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%
    Issue issue = (Issue) request.getAttribute("issue");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題詳細 - 技術課題管理システム</title>
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        .issue { border: 1px solid #ccc; border-radius: 8px; padding: 1.5rem; max-width: 760px; }
        .meta { color: #555; margin: 0.6rem 0; }
        .description { white-space: pre-wrap; line-height: 1.6; }
        .progress { background: #eee; border-radius: 4px; height: 1rem; overflow: hidden; }
        .progress-bar { background: #3b82f6; height: 100%; }
        .back-link { display: inline-block; margin-bottom: 1.5rem; }
        .edit-link { display: inline-block; margin-top: 1rem; }
        .comments { margin-top: 2rem; border-top: 1px solid #ccc; padding-top: 1rem; }
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
        <a class="edit-link" href="<%= request.getContextPath() %>/issues/edit?id=<%= issue.getId() %>">編集</a>
    </section>

    <section class="comments">
        <h2>コメント</h2>
        <p>コメント機能は次の小段階で追加します。</p>
    </section>
</body>
</html>
