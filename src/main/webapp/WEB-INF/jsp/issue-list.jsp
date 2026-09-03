<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%@ page import="com.example.technicalissuemanager.util.IssueViewHelper" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題一覧 - 技術課題管理システム</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<main class="container">
    <div class="page-heading">
        <h1>課題一覧</h1>
        <a class="button-link button-primary" href="<%= request.getContextPath() %>/issues/create">＋ 課題を新規登録</a>
    </div>
<%
    String successMessage = (String) request.getAttribute("successMessage");
    if (successMessage != null) {
%>
    <p class="success-message"><%= HtmlEscaper.escape(successMessage) %></p>
<%
    }
    List<Issue> issues = (List<Issue>) request.getAttribute("issues");
    for (Issue issue : issues) {
        String dueDateLabel = IssueViewHelper.dueDateLabel(issue);
%>
    <section class="issue">
        <h2><a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>">#<%= issue.getId() %> <%= HtmlEscaper.escape(issue.getTitle()) %></a></h2>
        <p class="meta">顧客：<%= HtmlEscaper.escape(issue.getCustomer()) %> ／ 製品・プロジェクト：<%= HtmlEscaper.escape(issue.getProduct()) %></p>
        <p class="meta">
            優先度：<span class="badge <%= IssueViewHelper.priorityClass(issue.getPriority()) %>"><%= HtmlEscaper.escape(issue.getPriority()) %></span>
            ／ 状況：<span class="badge <%= IssueViewHelper.statusClass(issue.getStatus()) %>"><%= HtmlEscaper.escape(issue.getStatus()) %></span>
        </p>
        <p class="meta due-date <%= IssueViewHelper.dueDateClass(issue) %>">
            担当者：<%= HtmlEscaper.escape(issue.getAssignee()) %> ／ 期限：<%= issue.getDueDate() %>
<%      if (!dueDateLabel.isEmpty()) { %>
            <span class="due-label"><%= HtmlEscaper.escape(dueDateLabel) %></span>
<%      } %>
        </p>
        <p class="description"><%= HtmlEscaper.escape(issue.getDescription()) %></p>
        <p>コメント：<%= issue.getCommentCount() %>件</p>
        <p>進捗率：<%= issue.getProgress() %>%</p>
        <div class="progress" aria-label="進捗率 <%= issue.getProgress() %>%">
            <div class="progress-bar" style="width: <%= issue.getProgress() %>%"></div>
        </div>
    </section>
<%
    }
%>
</main>
</body>
</html>
