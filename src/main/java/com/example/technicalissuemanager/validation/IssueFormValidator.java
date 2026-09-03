package com.example.technicalissuemanager.validation;

import com.example.technicalissuemanager.model.Issue;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Validates issue create and edit form input.
 */
public final class IssueFormValidator {

    private static final int SHORT_TEXT_MAX_LENGTH = 255;
    private static final Set<String> PRIORITIES = Set.of("High", "Medium", "Low");
    private static final Set<String> STATUSES = Set.of("Open", "In Progress", "Resolved");

    private IssueFormValidator() {
    }

    public static Issue validate(HttpServletRequest request) throws IssueFormValidationException {
        Map<String, String> errors = new LinkedHashMap<>();

        String title = requiredValue(request, "title", "タイトル", errors);
        String customer = requiredValue(request, "customer", "顧客", errors);
        String product = requiredValue(request, "product", "製品・プロジェクト", errors);
        String priority = requiredValue(request, "priority", "優先度", errors);
        String status = requiredValue(request, "status", "状況", errors);
        String progressValue = requiredValue(request, "progress", "進捗率", errors);
        String assignee = requiredValue(request, "assignee", "担当者", errors);
        String dueDateValue = requiredValue(request, "dueDate", "期限", errors);
        String description = requiredValue(request, "description", "詳細説明", errors);

        validateMaxLength("title", title, "タイトル", errors);
        validateMaxLength("customer", customer, "顧客", errors);
        validateMaxLength("product", product, "製品・プロジェクト", errors);
        validateMaxLength("assignee", assignee, "担当者", errors);
        validateAllowedValue("priority", priority, "優先度", PRIORITIES, errors);
        validateAllowedValue("status", status, "状況", STATUSES, errors);

        int progress = validateProgress(progressValue, errors);
        LocalDate dueDate = validateDueDate(dueDateValue, errors);

        if (!errors.isEmpty()) {
            throw new IssueFormValidationException(errors);
        }

        Issue issue = new Issue();
        issue.setTitle(title);
        issue.setCustomer(customer);
        issue.setProduct(product);
        issue.setPriority(priority);
        issue.setStatus(status);
        issue.setProgress(progress);
        issue.setAssignee(assignee);
        issue.setDueDate(dueDate);
        issue.setDescription(description);
        return issue;
    }

    private static String requiredValue(HttpServletRequest request, String parameterName,
                                        String label, Map<String, String> errors) {
        String value = request.getParameter(parameterName);
        String trimmedValue = value == null ? "" : value.trim();
        if (trimmedValue.isEmpty()) {
            errors.put(parameterName, label + "を入力してください。");
        }
        return trimmedValue;
    }

    private static void validateMaxLength(String fieldName, String value, String label,
                                          Map<String, String> errors) {
        if (!value.isEmpty() && value.length() > SHORT_TEXT_MAX_LENGTH) {
            errors.put(fieldName, label + "は" + SHORT_TEXT_MAX_LENGTH + "文字以内で入力してください。");
        }
    }

    private static void validateAllowedValue(String fieldName, String value, String label,
                                             Set<String> allowedValues,
                                             Map<String, String> errors) {
        if (!value.isEmpty() && !allowedValues.contains(value)) {
            errors.put(fieldName, label + "を正しく選択してください。");
        }
    }

    private static int validateProgress(String value, Map<String, String> errors) {
        if (value.isEmpty()) {
            return 0;
        }

        try {
            int progress = Integer.parseInt(value);
            if (progress < 0 || progress > 100) {
                errors.put("progress", "進捗率は0から100までの整数で入力してください。");
            }
            return progress;
        } catch (NumberFormatException exception) {
            errors.put("progress", "進捗率は0から100までの整数で入力してください。");
            return 0;
        }
    }

    private static LocalDate validateDueDate(String value, Map<String, String> errors) {
        if (value.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            errors.put("dueDate", "期限を正しい日付で入力してください。");
            return null;
        }
    }
}
