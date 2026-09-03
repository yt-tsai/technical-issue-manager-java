package com.example.technicalissuemanager.validation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains field-level validation errors for an issue form submission.
 */
public class IssueFormValidationException extends Exception {

    private final Map<String, String> errors;

    public IssueFormValidationException(Map<String, String> errors) {
        this.errors = Collections.unmodifiableMap(new LinkedHashMap<>(errors));
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
