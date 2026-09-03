<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%!
    private String formValue(HttpServletRequest request, String fieldName, String defaultValue) {
        String value = request.getParameter(fieldName);
        return value == null ? defaultValue : value;
    }
%>
<%
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    if (errors == null) {
        errors = Collections.emptyMap();
    }
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>課題を新規登録 - 技術課題管理システム</title>
    <style>
        body { font-family: sans-serif; margin: 2rem; color: #222; }
        form { max-width: 760px; }
        .field { margin: 1rem 0; }
        label { display: block; font-weight: bold; margin-bottom: 0.35rem; }
        input, select, textarea { box-sizing: border-box; width: 100%; padding: 0.55rem; font: inherit; }
        textarea { min-height: 8rem; resize: vertical; }
        .field-error { color: #b91c1c; margin: 0.35rem 0 0; }
        .input-error { border: 2px solid #b91c1c; }
        .actions { display: flex; align-items: center; gap: 1rem; margin-top: 1.5rem; }
        button { padding: 0.6rem 1rem; font: inherit; cursor: pointer; }
    </style>
</head>
<body>
    <h1>技術課題管理システム</h1>
    <h2>課題を新規登録</h2>

    <form action="<%= request.getContextPath() %>/issues/create" method="post">
        <div class="field">
            <label for="title">タイトル</label>
            <input id="title" name="title" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "title", "")) %>" class="<%= errors.containsKey("title") ? "input-error" : "" %>" required>
<% if (errors.containsKey("title")) { %>
            <p class="field-error"><%= errors.get("title") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="customer">顧客</label>
            <input id="customer" name="customer" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "customer", "")) %>" class="<%= errors.containsKey("customer") ? "input-error" : "" %>" required>
<% if (errors.containsKey("customer")) { %>
            <p class="field-error"><%= errors.get("customer") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="product">製品・プロジェクト</label>
            <input id="product" name="product" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "product", "")) %>" class="<%= errors.containsKey("product") ? "input-error" : "" %>" required>
<% if (errors.containsKey("product")) { %>
            <p class="field-error"><%= errors.get("product") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="priority">優先度</label>
            <select id="priority" name="priority" class="<%= errors.containsKey("priority") ? "input-error" : "" %>" required>
                <option value="High" <%= "High".equals(formValue(request, "priority", "Medium")) ? "selected" : "" %>>High</option>
                <option value="Medium" <%= "Medium".equals(formValue(request, "priority", "Medium")) ? "selected" : "" %>>Medium</option>
                <option value="Low" <%= "Low".equals(formValue(request, "priority", "Medium")) ? "selected" : "" %>>Low</option>
            </select>
<% if (errors.containsKey("priority")) { %>
            <p class="field-error"><%= errors.get("priority") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="status">状況</label>
            <select id="status" name="status" class="<%= errors.containsKey("status") ? "input-error" : "" %>" required>
                <option value="Open" <%= "Open".equals(formValue(request, "status", "Open")) ? "selected" : "" %>>Open</option>
                <option value="In Progress" <%= "In Progress".equals(formValue(request, "status", "Open")) ? "selected" : "" %>>In Progress</option>
                <option value="Resolved" <%= "Resolved".equals(formValue(request, "status", "Open")) ? "selected" : "" %>>Resolved</option>
            </select>
<% if (errors.containsKey("status")) { %>
            <p class="field-error"><%= errors.get("status") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="progress">進捗率（0〜100）</label>
            <input id="progress" name="progress" type="number" min="0" max="100" value="<%= HtmlEscaper.escape(formValue(request, "progress", "0")) %>" class="<%= errors.containsKey("progress") ? "input-error" : "" %>" required>
<% if (errors.containsKey("progress")) { %>
            <p class="field-error"><%= errors.get("progress") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="assignee">担当者</label>
            <input id="assignee" name="assignee" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "assignee", "")) %>" class="<%= errors.containsKey("assignee") ? "input-error" : "" %>" required>
<% if (errors.containsKey("assignee")) { %>
            <p class="field-error"><%= errors.get("assignee") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="dueDate">期限</label>
            <input id="dueDate" name="dueDate" type="date" value="<%= HtmlEscaper.escape(formValue(request, "dueDate", "")) %>" class="<%= errors.containsKey("dueDate") ? "input-error" : "" %>" required>
<% if (errors.containsKey("dueDate")) { %>
            <p class="field-error"><%= errors.get("dueDate") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="description">詳細説明</label>
            <textarea id="description" name="description" class="<%= errors.containsKey("description") ? "input-error" : "" %>" required><%= HtmlEscaper.escape(formValue(request, "description", "")) %></textarea>
<% if (errors.containsKey("description")) { %>
            <p class="field-error"><%= errors.get("description") %></p>
<% } %>
        </div>
        <div class="actions">
            <button type="submit">登録する</button>
            <a href="<%= request.getContextPath() %>/issues">キャンセル</a>
        </div>
    </form>
</body>
</html>
