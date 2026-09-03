<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%@ page import="com.example.technicalissuemanager.util.IssueViewHelper" %>
<%
    String keyword = (String) request.getAttribute("keyword");
    String priority = (String) request.getAttribute("priority");
    String status = (String) request.getAttribute("status");
    String sort = (String) request.getAttribute("sort");
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
    <p>キーワードと条件を組み合わせて検索できます。キーワードを空欄にして、条件だけで検索することもできます。</p>

    <form class="search-form" action="<%= request.getContextPath() %>/issues/search" method="get">
        <div class="field search-keyword">
            <label for="keyword">キーワード</label>
            <input id="keyword" name="keyword" type="search" maxlength="100"
                   value="<%= HtmlEscaper.escape(keyword) %>" placeholder="タイトル、顧客、担当者など">
        </div>
        <div class="field">
            <label for="priority">優先度</label>
            <select id="priority" name="priority">
                <option value="" <%= "".equals(priority) ? "selected" : "" %>>すべて</option>
                <option value="High" <%= "High".equals(priority) ? "selected" : "" %>>High</option>
                <option value="Medium" <%= "Medium".equals(priority) ? "selected" : "" %>>Medium</option>
                <option value="Low" <%= "Low".equals(priority) ? "selected" : "" %>>Low</option>
            </select>
        </div>
        <div class="field">
            <label for="status">状況</label>
            <select id="status" name="status">
                <option value="" <%= "".equals(status) ? "selected" : "" %>>すべて</option>
                <option value="Open" <%= "Open".equals(status) ? "selected" : "" %>>Open</option>
                <option value="In Progress" <%= "In Progress".equals(status) ? "selected" : "" %>>In Progress</option>
                <option value="Resolved" <%= "Resolved".equals(status) ? "selected" : "" %>>Resolved</option>
            </select>
        </div>
        <div class="field">
            <label for="sort">並び順</label>
            <select id="sort" name="sort">
                <option value="id-asc" <%= "id-asc".equals(sort) ? "selected" : "" %>>登録順</option>
                <option value="updated-desc" <%= "updated-desc".equals(sort) ? "selected" : "" %>>更新が新しい順</option>
                <option value="due-asc" <%= "due-asc".equals(sort) ? "selected" : "" %>>期限が近い順</option>
                <option value="priority-desc" <%= "priority-desc".equals(sort) ? "selected" : "" %>>優先度が高い順</option>
            </select>
        </div>
        <div class="search-actions">
            <button class="button-primary" type="submit">この条件で検索</button>
            <a class="button-link" href="<%= request.getContextPath() %>/issues/search">条件をクリア</a>
        </div>
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
        <p>コメント：<%= issue.getCommentCount() %>件</p>
    </section>
<%         }
       }
   } %>
</main>
</body>
</html>
