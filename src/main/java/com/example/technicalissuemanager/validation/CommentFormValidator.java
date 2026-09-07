package com.example.technicalissuemanager.validation;

import com.example.technicalissuemanager.model.Comment;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates comment form input and creates a comment from valid values.
 */
public final class CommentFormValidator {

    private static final int SHORT_TEXT_MAX_LENGTH = 255;
    private static final int CONTENT_MAX_LENGTH = 5000;
    private static final int MAX_CC_RECIPIENTS = 20;

    private CommentFormValidator() {
    }

    public static Comment validate(HttpServletRequest request, String defaultTo)
            throws CommentFormValidationException {
        Map<String, String> errors = new LinkedHashMap<>();

        String author = trimToEmpty(request.getParameter("author"));
        String to = trimToEmpty(request.getParameter("to"));
        String content = trimToEmpty(request.getParameter("content"));

        validateRequired("author", author, "投稿者", errors);
        validateMaxLength("author", author, "投稿者", SHORT_TEXT_MAX_LENGTH, errors);

        if (to.isEmpty()) {
            to = trimToEmpty(defaultTo);
        }
        validateMaxLength("to", to, "To", SHORT_TEXT_MAX_LENGTH, errors);

        List<String> cc = parseCc(request.getParameter("cc"), errors);

        validateRequired("content", content, "コメント内容", errors);
        validateMaxLength("content", content, "コメント内容", CONTENT_MAX_LENGTH, errors);

        if (!errors.isEmpty()) {
            throw new CommentFormValidationException(errors);
        }

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setTo(to);
        comment.setCc(cc);
        comment.setContent(content);
        return comment;
    }

    private static List<String> parseCc(String ccInput, Map<String, String> errors) {
        List<String> cc = new ArrayList<>();
        if (ccInput == null || ccInput.isBlank()) {
            return cc;
        }

        for (String name : ccInput.split(",")) {
            String trimmedName = name.trim();
            if (trimmedName.isEmpty()) {
                continue;
            }
            cc.add(trimmedName);
            if (trimmedName.length() > SHORT_TEXT_MAX_LENGTH) {
                errors.putIfAbsent("cc", "CCの各宛先は255文字以内で入力してください。");
            }
        }

        if (cc.size() > MAX_CC_RECIPIENTS) {
            errors.put("cc", "CCは20件以内で入力してください。");
        }
        return cc;
    }

    private static void validateRequired(
            String fieldName, String value, String label, Map<String, String> errors) {
        if (value.isEmpty()) {
            errors.put(fieldName, label + "を入力してください。");
        }
    }

    private static void validateMaxLength(
            String fieldName, String value, String label, int maxLength,
            Map<String, String> errors) {
        if (!value.isEmpty() && value.length() > maxLength) {
            errors.put(fieldName, label + "は" + maxLength + "文字以内で入力してください。");
        }
    }

    private static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
