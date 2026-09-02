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
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        .back-link { display: inline-block; margin-bottom: 1.5rem; }
        .statistics { border-collapse: collapse; min-width: 360px; }
        .statistics th, .statistics td { border: 1px solid #ccc; padding: 0.75rem 1rem; text-align: left; }
        .statistics th { background: #f5f5f5; }
        .statistics td { text-align: right; }
    </style>
</head>
<body>
    <h1>技術課題管理システム</h1>
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
</body>
</html>
