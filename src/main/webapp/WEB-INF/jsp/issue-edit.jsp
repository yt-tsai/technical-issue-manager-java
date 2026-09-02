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
    <title>課題を編集 - 技術課題管理システム</title>
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        form { max-width: 760px; }
        .field { margin: 1rem 0; }
        label { display: block; font-weight: bold; margin-bottom: 0.35rem; }
        input, select, textarea { box-sizing: border-box; width: 100%; padding: 0.55rem; font: inherit; }
        textarea { min-height: 8rem; resize: vertical; }
        .actions { display: flex; align-items: center; gap: 1rem; margin-top: 1.5rem; }
        button { padding: 0.6rem 1rem; font: inherit; cursor: pointer; }
    </style>
</head>
<body>
    <h1>技術課題管理システム</h1>
    <h2>課題を編集</h2>

    <form action="<%= request.getContextPath() %>/issues/edit?id=<%= issue.getId() %>" method="post">
        <div class="field">
            <label for="title">タイトル</label>
            <input id="title" name="title" type="text" maxlength="255" value="<%= issue.getTitle() %>" required>
        </div>
        <div class="field">
            <label for="customer">顧客</label>
            <input id="customer" name="customer" type="text" maxlength="255" value="<%= issue.getCustomer() %>" required>
        </div>
        <div class="field">
            <label for="product">製品・プロジェクト</label>
            <input id="product" name="product" type="text" maxlength="255" value="<%= issue.getProduct() %>" required>
        </div>
        <div class="field">
            <label for="priority">優先度</label>
            <select id="priority" name="priority" required>
                <option value="High" <%= "High".equals(issue.getPriority()) ? "selected" : "" %>>High</option>
                <option value="Medium" <%= "Medium".equals(issue.getPriority()) ? "selected" : "" %>>Medium</option>
                <option value="Low" <%= "Low".equals(issue.getPriority()) ? "selected" : "" %>>Low</option>
            </select>
        </div>
        <div class="field">
            <label for="status">状況</label>
            <select id="status" name="status" required>
                <option value="Open" <%= "Open".equals(issue.getStatus()) ? "selected" : "" %>>Open</option>
                <option value="In Progress" <%= "In Progress".equals(issue.getStatus()) ? "selected" : "" %>>In Progress</option>
                <option value="Resolved" <%= "Resolved".equals(issue.getStatus()) ? "selected" : "" %>>Resolved</option>
            </select>
        </div>
        <div class="field">
            <label for="progress">進捗率（0〜100）</label>
            <input id="progress" name="progress" type="number" min="0" max="100" value="<%= issue.getProgress() %>" required>
        </div>
        <div class="field">
            <label for="assignee">担当者</label>
            <input id="assignee" name="assignee" type="text" maxlength="255" value="<%= issue.getAssignee() %>" required>
        </div>
        <div class="field">
            <label for="dueDate">期限</label>
            <input id="dueDate" name="dueDate" type="date" value="<%= issue.getDueDate() %>" required>
        </div>
        <div class="field">
            <label for="description">詳細説明</label>
            <textarea id="description" name="description" required><%= issue.getDescription() %></textarea>
        </div>
        <div class="actions">
            <button type="submit">更新する</button>
            <a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>">キャンセル</a>
        </div>
    </form>
</body>
</html>
