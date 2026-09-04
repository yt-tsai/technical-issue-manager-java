package com.example.technicalissuemanager.util;

import com.example.technicalissuemanager.model.Issue;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IssueViewHelperTest {

    @Test
    void mapsPriorityToCssClass() {
        assertAll(
                () -> assertEquals("priority-high", IssueViewHelper.priorityClass("High")),
                () -> assertEquals("priority-medium", IssueViewHelper.priorityClass("Medium")),
                () -> assertEquals("priority-low", IssueViewHelper.priorityClass("Low")),
                () -> assertEquals("badge-neutral", IssueViewHelper.priorityClass("Unknown")),
                () -> assertEquals("badge-neutral", IssueViewHelper.priorityClass(null)));
    }

    @Test
    void mapsStatusToCssClass() {
        assertAll(
                () -> assertEquals("status-open", IssueViewHelper.statusClass("Open")),
                () -> assertEquals("status-in-progress", IssueViewHelper.statusClass("In Progress")),
                () -> assertEquals("status-resolved", IssueViewHelper.statusClass("Resolved")),
                () -> assertEquals("badge-neutral", IssueViewHelper.statusClass("Unknown")),
                () -> assertEquals("badge-neutral", IssueViewHelper.statusClass(null)));
    }

    @Test
    void identifiesOverdueUrgentAndWarningDueDates() {
        LocalDate today = LocalDate.now();

        assertAll(
                () -> assertDueDate("Open", today.minusDays(1), "due-overdue", "期限超過"),
                () -> assertDueDate("Open", today, "due-urgent", "期限間近"),
                () -> assertDueDate("Open", today.plusDays(3), "due-urgent", "期限間近"),
                () -> assertDueDate("Open", today.plusDays(4), "due-warning", "要注意"),
                () -> assertDueDate("Open", today.plusDays(7), "due-warning", "要注意"),
                () -> assertDueDate("Open", today.plusDays(8), "", ""));
    }

    @Test
    void resolvedIssueTakesPrecedenceOverDueDateWarning() {
        assertDueDate("Resolved", LocalDate.now().minusDays(1), "due-resolved", "対応済み");
    }

    @Test
    void returnsNoDueDateWarningWhenDueDateIsMissing() {
        assertDueDate("Open", null, "", "");
    }

    private void assertDueDate(String status, LocalDate dueDate, String cssClass, String label) {
        Issue issue = new Issue();
        issue.setStatus(status);
        issue.setDueDate(dueDate);

        assertAll(
                () -> assertEquals(cssClass, IssueViewHelper.dueDateClass(issue)),
                () -> assertEquals(label, IssueViewHelper.dueDateLabel(issue)));
    }
}
