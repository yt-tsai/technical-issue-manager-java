<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.technicalissuemanager.model.IssueStatistics" %>
<%
    IssueStatistics statistics = (IssueStatistics) request.getAttribute("statistics");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題集計 - 技術課題管理システム</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<main class="container">
    <a class="back-link" href="<%= request.getContextPath() %>/issues">← 課題一覧へ戻る</a>
    <h2>課題集計</h2>

    <table class="statistics">
        <tr><th>総件数</th><td><%= statistics.getTotalCount() %></td></tr>
        <tr><th colspan="2">状況別</th></tr>
        <tr><th>Open</th><td><%= statistics.getOpenCount() %></td></tr>
        <tr><th>In Progress</th><td><%= statistics.getInProgressCount() %></td></tr>
        <tr><th>Resolved</th><td><%= statistics.getResolvedCount() %></td></tr>
        <tr><th colspan="2">優先度別</th></tr>
        <tr><th>High</th><td><%= statistics.getHighCount() %></td></tr>
        <tr><th>Medium</th><td><%= statistics.getMediumCount() %></td></tr>
        <tr><th>Low</th><td><%= statistics.getLowCount() %></td></tr>
    </table>
</main>
</body>
</html>
