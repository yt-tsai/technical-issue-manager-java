# Automated Test Cases

This document describes the automated unit-test coverage for Technical Issue Manager.

## Test Strategy

| Test level | Purpose | Database or Tomcat required |
| --- | --- | --- |
| Unit tests | Verify validation, presentation rules, escaping, and session messages | No |
| Manual tests | Verify browser workflows, JSP rendering, MySQL persistence, and deployment | Yes |

The unit tests are intentionally isolated from MySQL and Tomcat so they can run quickly and consistently on a development machine or continuous integration service.

## Running the Tests

From the project root:

```bash
mvn test
```

A successful run reports 22 tests with no failures or errors. Detailed reports are generated under `target/surefire-reports/`.

## Current Automated Cases

### HTML Escaping

| ID | Scenario | Expected result |
| --- | --- | --- |
| HT-01 | Escape ampersand, quotation marks, angle brackets, and apostrophes | Every special character is converted to its HTML entity |
| HT-02 | Escape a null value | An empty string is returned |
| HT-03 | Escape Japanese and ordinary text | The original text remains unchanged |

### Issue Display Rules

| ID | Scenario | Expected result |
| --- | --- | --- |
| IV-01 | Convert known and unknown priorities to CSS classes | High, Medium, and Low receive their classes; other values use the neutral class |
| IV-02 | Convert known and unknown statuses to CSS classes | Open, In Progress, and Resolved receive their classes; other values use the neutral class |
| IV-03 | Check overdue, urgent, warning, and future due dates | The correct due-date class and Japanese label are returned at each boundary |
| IV-04 | Check a resolved issue with an overdue date | The resolved style takes precedence |
| IV-05 | Check an issue without a due date | No warning class or label is returned |

### Issue Form Validation

| ID | Scenario | Expected result |
| --- | --- | --- |
| IF-01 | Submit valid issue values with surrounding spaces | Values are trimmed and mapped to an Issue |
| IF-02 | Leave all required issue fields empty | An error is returned for every required field |
| IF-03 | Submit invalid priority, status, progress, and date values | An error is returned for each invalid field |
| IF-04 | Submit text longer than the database column limit | An error is returned for every oversized short-text field |

### Comment Form Validation

| ID | Scenario | Expected result |
| --- | --- | --- |
| CF-01 | Submit valid comment values and comma-separated CC recipients | Values are trimmed, empty CC entries are ignored, and a Comment is created |
| CF-02 | Leave To blank | The issue assignee or reply author is used as the default recipient |
| CF-03 | Leave author and content empty | Both required-field errors are returned |
| CF-04 | Submit values exactly at all length and CC limits | The comment is accepted |
| CF-05 | Submit author, To, content, or CC values above their length limits | An error is returned for every oversized field |
| CF-06 | Submit more than 20 CC recipients | A CC limit error is returned |

### Flash Messages

| ID | Scenario | Expected result |
| --- | --- | --- |
| FM-01 | Store a success message | The message is saved in the HTTP session |
| FM-02 | Consume a stored success message | The message is returned and removed from the session |
| FM-03 | Consume a message without an existing session | Null is returned without creating a session |
| FM-04 | Consume an unexpected non-text session value | Null is returned and the value is removed |

## Coverage Boundaries

These unit tests do not verify SQL execution, database transactions, JSP layout, browser navigation, or Tomcat deployment. Those behaviors remain covered by the Phase 1 and Phase 2 manual test checklists.

When database integration tests are introduced, they should use a separate disposable test database and must never run against normal local development data.
