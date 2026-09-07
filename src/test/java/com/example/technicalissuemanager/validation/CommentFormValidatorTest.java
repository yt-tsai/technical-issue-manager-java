package com.example.technicalissuemanager.validation;

import com.example.technicalissuemanager.model.Comment;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentFormValidatorTest {

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
    void createsCommentFromValidTrimmedParameters() throws CommentFormValidationException {
        parameters.put("author", "  Mika  ");
        parameters.put("to", "  Peter  ");
        parameters.put("cc", " Ken, , Yuki ");
        parameters.put("content", "  Please confirm the fix.  ");

        Comment comment = CommentFormValidator.validate(request, "Default Assignee");

        assertEquals("Mika", comment.getAuthor());
        assertEquals("Peter", comment.getTo());
        assertEquals(java.util.List.of("Ken", "Yuki"), comment.getCc());
        assertEquals("Please confirm the fix.", comment.getContent());
    }

    @Test
    void usesDefaultRecipientWhenToIsBlank() throws CommentFormValidationException {
        parameters.put("to", "   ");

        Comment comment = CommentFormValidator.validate(request, " Peter ");

        assertEquals("Peter", comment.getTo());
    }

    @Test
    void reportsMissingAuthorAndContent() {
        parameters.put("author", "");
        parameters.put("content", "   ");

        CommentFormValidationException exception = assertThrows(
                CommentFormValidationException.class,
                () -> CommentFormValidator.validate(request, "Peter"));

        assertEquals(2, exception.getErrors().size());
        assertTrue(exception.getErrors().containsKey("author"));
        assertTrue(exception.getErrors().containsKey("content"));
    }

    @Test
    void acceptsValuesAtMaximumLengthsAndTwentyCcRecipients()
            throws CommentFormValidationException {
        parameters.put("author", "a".repeat(255));
        parameters.put("to", "t".repeat(255));
        parameters.put("content", "c".repeat(5000));
        parameters.put("cc", recipients(20));

        Comment comment = CommentFormValidator.validate(request, "Peter");

        assertEquals(255, comment.getAuthor().length());
        assertEquals(255, comment.getTo().length());
        assertEquals(5000, comment.getContent().length());
        assertEquals(20, comment.getCc().size());
    }

    @Test
    void rejectsValuesAboveMaximumLengths() {
        parameters.put("author", "a".repeat(256));
        parameters.put("to", "t".repeat(256));
        parameters.put("content", "c".repeat(5001));
        parameters.put("cc", "x".repeat(256));

        CommentFormValidationException exception = assertThrows(
                CommentFormValidationException.class,
                () -> CommentFormValidator.validate(request, "Peter"));

        assertTrue(exception.getErrors().containsKey("author"));
        assertTrue(exception.getErrors().containsKey("to"));
        assertTrue(exception.getErrors().containsKey("content"));
        assertTrue(exception.getErrors().containsKey("cc"));
    }

    @Test
    void rejectsMoreThanTwentyCcRecipients() {
        parameters.put("cc", recipients(21));

        CommentFormValidationException exception = assertThrows(
                CommentFormValidationException.class,
                () -> CommentFormValidator.validate(request, "Peter"));

        assertTrue(exception.getErrors().containsKey("cc"));
    }

    private Map<String, String> validParameters() {
        Map<String, String> values = new HashMap<>();
        values.put("author", "Mika");
        values.put("to", "Peter");
        values.put("cc", "");
        values.put("content", "Please confirm the fix.");
        return values;
    }

    private String recipients(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(number -> "User" + number)
                .collect(Collectors.joining(","));
    }
}
