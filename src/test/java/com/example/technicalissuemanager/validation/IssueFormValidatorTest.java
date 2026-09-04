package com.example.technicalissuemanager.validation;

import com.example.technicalissuemanager.model.Issue;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IssueFormValidatorTest {

    private HttpServletRequest request;
    private Map<String, String> parameters;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        parameters = validParameters();
        when(request.getParameter(anyString()))
                .thenAnswer(invocation -> parameters.get(invocation.getArgument(0, String.class)));
    }

    @Test
    void createsIssueFromValidTrimmedParameters() throws IssueFormValidationException {
        parameters.put("title", "  Login error  ");
        parameters.put("description", "  Steps to reproduce  ");

        Issue issue = IssueFormValidator.validate(request);

        assertEquals("Login error", issue.getTitle());
        assertEquals("Aster Solutions", issue.getCustomer());
        assertEquals("Customer Portal", issue.getProduct());
        assertEquals("Medium", issue.getPriority());
        assertEquals("Open", issue.getStatus());
        assertEquals(40, issue.getProgress());
        assertEquals("Peter", issue.getAssignee());
        assertEquals(LocalDate.of(2026, 9, 30), issue.getDueDate());
        assertEquals("Steps to reproduce", issue.getDescription());
    }

    @Test
    void reportsEveryMissingRequiredField() {
        parameters.replaceAll((key, value) -> "");

        IssueFormValidationException exception = assertThrows(
                IssueFormValidationException.class,
                () -> IssueFormValidator.validate(request));

        assertEquals(9, exception.getErrors().size());
        assertTrue(exception.getErrors().keySet().containsAll(
                Set.of("title", "customer", "product", "priority", "status",
                        "progress", "assignee", "dueDate", "description")));
    }

    @Test
    void rejectsInvalidChoiceNumberAndDateValues() {
        parameters.put("priority", "Urgent");
        parameters.put("status", "Waiting");
        parameters.put("progress", "101");
        parameters.put("dueDate", "not-a-date");

        IssueFormValidationException exception = assertThrows(
                IssueFormValidationException.class,
                () -> IssueFormValidator.validate(request));

        assertTrue(exception.getErrors().containsKey("priority"));
        assertTrue(exception.getErrors().containsKey("status"));
        assertTrue(exception.getErrors().containsKey("progress"));
        assertTrue(exception.getErrors().containsKey("dueDate"));
    }

    @Test
    void rejectsTextThatExceedsDatabaseColumnLength() {
        String tooLong = "x".repeat(256);
        parameters.put("title", tooLong);
        parameters.put("customer", tooLong);
        parameters.put("product", tooLong);
        parameters.put("assignee", tooLong);

        IssueFormValidationException exception = assertThrows(
                IssueFormValidationException.class,
                () -> IssueFormValidator.validate(request));

        assertTrue(exception.getErrors().containsKey("title"));
        assertTrue(exception.getErrors().containsKey("customer"));
        assertTrue(exception.getErrors().containsKey("product"));
        assertTrue(exception.getErrors().containsKey("assignee"));
    }

    private Map<String, String> validParameters() {
        Map<String, String> values = new HashMap<>();
        values.put("title", "Login error");
        values.put("customer", "Aster Solutions");
        values.put("product", "Customer Portal");
        values.put("priority", "Medium");
        values.put("status", "Open");
        values.put("progress", "40");
        values.put("assignee", "Peter");
        values.put("dueDate", "2026-09-30");
        values.put("description", "Steps to reproduce");
        return values;
    }
}
