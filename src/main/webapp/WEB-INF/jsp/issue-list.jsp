<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題一覧 - 技術課題管理システム</title>
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        h1 { margin-bottom: 1.5rem; }
        .issue { border: 1px solid #ccc; border-radius: 8px; padding: 1rem; margin-bottom: 1rem; max-width: 760px; }
        .issue h2 { margin-top: 0; }
        .meta { color: #555; margin: 0.35rem 0; }
        .progress { background: #eee; border-radius: 4px; height: 1rem; overflow: hidden; }
        .progress-bar { background: #3b82f6; height: 100%; }
        .create-link { display: inline-block; margin-bottom: 1rem; }
        .search-link { display: inline-block; margin: 0 0 1rem 1rem; }
        .statistics-link { display: inline-block; margin: 0 0 1rem 1rem; }
        .success-message { background: #dcfce7; border: 1px solid #86efac; color: #166534; max-width: 760px; padding: 0.75rem 1rem; }
    </style>
</head>
<body>
    <h1>技術課題管理システム</h1>
    <h2>課題一覧</h2>
<%
    String successMessage = (String) request.getAttribute("successMessage");
    if (successMessage != null) {
%>
    <p class="success-message"><%= HtmlEscaper.escape(successMessage) %></p>
<%
    }
%>
    <a class="create-link" href="<%= request.getContextPath() %>/issues/create">＋ 課題を新規登録</a>
    <a class="search-link" href="<%= request.getContextPath() %>/issues/search">課題検索</a>
    <a class="statistics-link" href="<%= request.getContextPath() %>/issues/statistics">課題集計</a>
<%
    List<Issue> issues = (List<Issue>) request.getAttribute("issues");
    for (Issue issue : issues) {
%>
    <section class="issue">
        <h2><a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>">#<%= issue.getId() %> <%= issue.getTitle() %></a></h2>
        <p class="meta">顧客：<%= issue.getCustomer() %> ／ 製品・プロジェクト：<%= issue.getProduct() %></p>
        <p class="meta">優先度：<%= issue.getPriority() %> ／ 状況：<%= issue.getStatus() %></p>
        <p class="meta">担当者：<%= issue.getAssignee() %> ／ 期限：<%= issue.getDueDate() %></p>
        <p><%= issue.getDescription() %></p>
        <p>コメント：<%= issue.getCommentCount() %>件</p>
        <p>進捗率：<%= issue.getProgress() %>%</p>
        <div class="progress" aria-label="進捗率 <%= issue.getProgress() %>%">
            <div class="progress-bar" style="width: <%= issue.getProgress() %>%"></div>
        </div>
    </section>
<%
    }
%>
</body>
</html>
