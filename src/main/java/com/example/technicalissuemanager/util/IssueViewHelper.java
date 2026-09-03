package com.example.technicalissuemanager.util;

import com.example.technicalissuemanager.model.Issue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Provides safe CSS classes and labels for issue presentation.
 */
public final class IssueViewHelper {

    private IssueViewHelper() {
    }

    public static String priorityClass(String priority) {
        if ("High".equals(priority)) {
            return "priority-high";
        }
        if ("Medium".equals(priority)) {
            return "priority-medium";
        }
        if ("Low".equals(priority)) {
            return "priority-low";
        }
        return "badge-neutral";
    }

    public static String statusClass(String status) {
        if ("Open".equals(status)) {
            return "status-open";
        }
        if ("In Progress".equals(status)) {
            return "status-in-progress";
        }
        if ("Resolved".equals(status)) {
            return "status-resolved";
        }
        return "badge-neutral";
    }

    public static String dueDateClass(Issue issue) {
        if ("Resolved".equals(issue.getStatus())) {
            return "due-resolved";
        }

        Long daysRemaining = daysRemaining(issue);
        if (daysRemaining == null) {
            return "";
        }
        if (daysRemaining < 0) {
            return "due-overdue";
        }
        if (daysRemaining <= 3) {
            return "due-urgent";
        }
        if (daysRemaining <= 7) {
            return "due-warning";
        }
        return "";
    }

    public static String dueDateLabel(Issue issue) {
        if ("Resolved".equals(issue.getStatus())) {
            return "対応済み";
        }

        Long daysRemaining = daysRemaining(issue);
        if (daysRemaining == null) {
            return "";
        }
        if (daysRemaining < 0) {
            return "期限超過";
        }
        if (daysRemaining <= 3) {
            return "期限間近";
        }
        if (daysRemaining <= 7) {
            return "要注意";
        }
        return "";
    }

    private static Long daysRemaining(Issue issue) {
        LocalDate dueDate = issue.getDueDate();
        if (dueDate == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }
}
