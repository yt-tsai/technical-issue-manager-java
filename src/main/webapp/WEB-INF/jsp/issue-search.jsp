<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%@ page import="com.example.technicalissuemanager.util.IssueViewHelper" %>
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
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<main class="container">
    <a class="back-link" href="<%= request.getContextPath() %>/issues">← 課題一覧へ戻る</a>
    <h1>課題検索</h1>
    <p>タイトル、顧客、製品・プロジェクト、優先度、状況、担当者、期限、詳細説明を検索できます。</p>

    <form class="search-form" action="<%= request.getContextPath() %>/issues/search" method="get">
        <input name="keyword" type="search" value="<%= HtmlEscaper.escape(keyword) %>" placeholder="検索キーワード" required>
        <button class="button-primary" type="submit">検索</button>
    </form>

<% if (errorMessage != null) { %>
    <p class="error-message"><%= HtmlEscaper.escape(errorMessage) %></p>
<% } %>

<% if (Boolean.TRUE.equals(hasSearched)) { %>
    <h2>検索結果：<%= issues.size() %> 件</h2>
<%     if (issues.isEmpty()) { %>
    <p>該当する課題はありません。</p>
<%     } else {
           for (Issue issue : issues) {
               String dueDateLabel = IssueViewHelper.dueDateLabel(issue); %>
    <section class="issue">
        <h3><a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>">#<%= issue.getId() %> <%= HtmlEscaper.escape(issue.getTitle()) %></a></h3>
        <p class="meta">顧客：<%= HtmlEscaper.escape(issue.getCustomer()) %> ／ 製品・プロジェクト：<%= HtmlEscaper.escape(issue.getProduct()) %></p>
        <p class="meta">
            優先度：<span class="badge <%= IssueViewHelper.priorityClass(issue.getPriority()) %>"><%= HtmlEscaper.escape(issue.getPriority()) %></span>
            ／ 状況：<span class="badge <%= IssueViewHelper.statusClass(issue.getStatus()) %>"><%= HtmlEscaper.escape(issue.getStatus()) %></span>
            ／ 担当者：<%= HtmlEscaper.escape(issue.getAssignee()) %>
        </p>
        <p class="meta due-date <%= IssueViewHelper.dueDateClass(issue) %>">
            期限：<%= issue.getDueDate() %>
<%             if (!dueDateLabel.isEmpty()) { %>
            <span class="due-label"><%= HtmlEscaper.escape(dueDateLabel) %></span>
<%             } %>
        </p>
        <p class="description"><%= HtmlEscaper.escape(issue.getDescription()) %></p>
    </section>
<%         }
       }
   } %>
</main>
</body>
</html>
