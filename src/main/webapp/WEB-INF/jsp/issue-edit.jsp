<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.technicalissuemanager.model.Issue" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%!
    private String formValue(HttpServletRequest request, String fieldName, String defaultValue) {
        String value = request.getParameter(fieldName);
        return value == null ? defaultValue : value;
    }
%>
<%
    Issue issue = (Issue) request.getAttribute("issue");
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
    <title>課題を編集 - 技術課題管理システム</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<main class="container">
    <h2>課題を編集</h2>

    <form class="issue-form" action="<%= request.getContextPath() %>/issues/edit?id=<%= issue.getId() %>" method="post">
        <div class="field">
            <label for="title">タイトル</label>
            <input id="title" name="title" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "title", issue.getTitle())) %>" class="<%= errors.containsKey("title") ? "input-error" : "" %>" required>
<% if (errors.containsKey("title")) { %>
            <p class="field-error"><%= errors.get("title") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="customer">顧客</label>
            <input id="customer" name="customer" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "customer", issue.getCustomer())) %>" class="<%= errors.containsKey("customer") ? "input-error" : "" %>" required>
<% if (errors.containsKey("customer")) { %>
            <p class="field-error"><%= errors.get("customer") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="product">製品・プロジェクト</label>
            <input id="product" name="product" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "product", issue.getProduct())) %>" class="<%= errors.containsKey("product") ? "input-error" : "" %>" required>
<% if (errors.containsKey("product")) { %>
            <p class="field-error"><%= errors.get("product") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="priority">優先度</label>
            <select id="priority" name="priority" class="<%= errors.containsKey("priority") ? "input-error" : "" %>" required>
                <option value="High" <%= "High".equals(formValue(request, "priority", issue.getPriority())) ? "selected" : "" %>>High</option>
                <option value="Medium" <%= "Medium".equals(formValue(request, "priority", issue.getPriority())) ? "selected" : "" %>>Medium</option>
                <option value="Low" <%= "Low".equals(formValue(request, "priority", issue.getPriority())) ? "selected" : "" %>>Low</option>
            </select>
<% if (errors.containsKey("priority")) { %>
            <p class="field-error"><%= errors.get("priority") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="status">状況</label>
            <select id="status" name="status" class="<%= errors.containsKey("status") ? "input-error" : "" %>" required>
                <option value="Open" <%= "Open".equals(formValue(request, "status", issue.getStatus())) ? "selected" : "" %>>Open</option>
                <option value="In Progress" <%= "In Progress".equals(formValue(request, "status", issue.getStatus())) ? "selected" : "" %>>In Progress</option>
                <option value="Resolved" <%= "Resolved".equals(formValue(request, "status", issue.getStatus())) ? "selected" : "" %>>Resolved</option>
            </select>
<% if (errors.containsKey("status")) { %>
            <p class="field-error"><%= errors.get("status") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="progress">進捗率（0〜100）</label>
            <input id="progress" name="progress" type="number" min="0" max="100" value="<%= HtmlEscaper.escape(formValue(request, "progress", String.valueOf(issue.getProgress()))) %>" class="<%= errors.containsKey("progress") ? "input-error" : "" %>" required>
<% if (errors.containsKey("progress")) { %>
            <p class="field-error"><%= errors.get("progress") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="assignee">担当者</label>
            <input id="assignee" name="assignee" type="text" maxlength="255" value="<%= HtmlEscaper.escape(formValue(request, "assignee", issue.getAssignee())) %>" class="<%= errors.containsKey("assignee") ? "input-error" : "" %>" required>
<% if (errors.containsKey("assignee")) { %>
            <p class="field-error"><%= errors.get("assignee") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="dueDate">期限</label>
            <input id="dueDate" name="dueDate" type="date" value="<%= HtmlEscaper.escape(formValue(request, "dueDate", String.valueOf(issue.getDueDate()))) %>" class="<%= errors.containsKey("dueDate") ? "input-error" : "" %>" required>
<% if (errors.containsKey("dueDate")) { %>
            <p class="field-error"><%= errors.get("dueDate") %></p>
<% } %>
        </div>
        <div class="field">
            <label for="description">詳細説明</label>
            <textarea id="description" name="description" class="<%= errors.containsKey("description") ? "input-error" : "" %>" required><%= HtmlEscaper.escape(formValue(request, "description", issue.getDescription())) %></textarea>
<% if (errors.containsKey("description")) { %>
            <p class="field-error"><%= errors.get("description") %></p>
<% } %>
        </div>
        <div class="actions">
            <button type="submit">更新する</button>
            <a href="<%= request.getContextPath() %>/issues/detail?id=<%= issue.getId() %>">キャンセル</a>
        </div>
    </form>
</main>
</body>
</html>
