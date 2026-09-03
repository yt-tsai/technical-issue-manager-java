<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.RequestDispatcher" %>
<%@ page import="com.example.technicalissuemanager.util.HtmlEscaper" %>
<%
    Object statusAttribute = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    int statusCode = statusAttribute instanceof Integer ? (Integer) statusAttribute : 500;
    String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    String heading;
    String description;

    if (statusCode == 400) {
        heading = "リクエストが正しくありません";
        description = errorMessage == null || errorMessage.isBlank()
                ? "入力内容を確認して、もう一度お試しください。"
                : errorMessage;
    } else if (statusCode == 404) {
        heading = "ページまたは課題が見つかりません";
        description = errorMessage == null || errorMessage.isBlank()
                ? "指定されたページまたは課題は存在しません。"
                : errorMessage;
    } else {
        heading = "システムエラーが発生しました";
        description = "しばらくしてからもう一度お試しください。";
    }
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>エラー - 技術課題管理システム</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>
<main class="container">
    <section class="error">
        <p class="status">HTTP <%= statusCode %></p>
        <h2><%= HtmlEscaper.escape(heading) %></h2>
        <p><%= HtmlEscaper.escape(description) %></p>
        <a href="<%= request.getContextPath() %>/issues">課題一覧へ戻る</a>
    </section>
</main>
</body>
</html>
