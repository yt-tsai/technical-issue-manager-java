<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%
    String keyword = (String) request.getAttribute("keyword");
    String errorMessage = (String) request.getAttribute("errorMessage");
    Boolean hasSearched = (Boolean) request.getAttribute("hasSearched");
    List<Issue> issues = (List<Issue>) request.getAttribute("issues");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題検索 - 技術課題管理システム</title>
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        form { display: flex; gap: 0.5rem; max-width: 760px; }
        input { flex: 1; padding: 0.55rem; font: inherit; }
        button { padding: 0.55rem 1rem; font: inherit; cursor: pointer; }
        .error { color: #b91c1c; }
        .issue { border: 1px solid #ccc; border-radius: 8px; padding: 1rem; margin-top: 1rem; max-width: 760px; }
        .issue h3 { margin-top: 0; }
        .meta { color: #555; }
        .back-link { display: inline-block; margin-bottom: 1.5rem; }
    </style>
</head>
<body>
    <h1>技術課題管理システム</h1>
    <a class="back-link" href="<%= request.getContextPath() %>/issues">← 課題一覧へ戻る</a>
    <h2>課題検索</h2>
    <p>タイトル、顧客、製品・プロジェクト、優先度、状況、担当者、期限、詳細説明を検索できます。</p>

    <form action="<%= request.getContextPath() %>/issues/search" method="get">
        <input name="keyword" type="search" value="<%= keyword == null ? "" : keyword %>" placeholder="検索キーワード" required>
        <button type="submit">検索</button>
    </form>

<% if (errorMessage != null) { %>
    <p class="error"><%= errorMessage %></p>
<% } %>

<% if (Boolean.TRUE.equals(hasSearched)) { %>
    <h2>検索結果：<%= issues.size() %> 件</h2>
<%     if (issues.isEmpty()) { %>
    <p>該当する課題はありません。</p>
<%     } else {
           for (Issue issue : issues) { %>
    <section class="issue">
        <h3><a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>">#<%= issue.getId() %> <%= issue.getTitle() %></a></h3>
        <p class="meta">顧客：<%= issue.getCustomer() %> ／ 製品・プロジェクト：<%= issue.getProduct() %></p>
        <p class="meta">優先度：<%= issue.getPriority() %> ／ 状況：<%= issue.getStatus() %> ／ 担当者：<%= issue.getAssignee() %></p>
        <p><%= issue.getDescription() %></p>
    </section>
<%         }
       }
   } %>
</body>
</html>
