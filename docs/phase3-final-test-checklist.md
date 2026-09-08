# Phase 3 Final Verification Checklist

Use this checklist for the final release review. The boxes are intentionally left blank so that a reviewer can record each verification run.

Use disposable records for create, edit, delete, comment, and reply tests.

## 1. Clean Build and Automated Tests

From the project root, run:

```bash
mvn clean verify
```

- [ ] Confirm all 22 automated tests pass.
- [ ] Confirm the build reports `BUILD SUCCESS`.
- [ ] Confirm `target/technical-issue-manager.war` is generated.

## 2. Application Startup

- [ ] Confirm MySQL is running and the project database is available.
- [ ] Deploy the generated WAR file to Tomcat.
- [ ] Open `http://localhost:8080/technical-issue-manager/issues`.
- [ ] Confirm the issue list loads without an application error.

## 3. Main Issue Workflow

- [ ] Create a disposable issue with Japanese text.
- [ ] Confirm the new issue appears in the list and detail pages.
- [ ] Edit its status, progress, assignee, due date, and description.
- [ ] Confirm the updated values are saved.
- [ ] Delete the disposable issue and confirm it no longer appears.

## 4. Search, Sorting, and Statistics

- [ ] Search by keyword.
- [ ] Filter by priority and status.
- [ ] Test every available sorting option.
- [ ] Confirm result comment counts are correct.
- [ ] Confirm statistics match the currently stored issues.

## 5. Comments and Replies

- [ ] Add a comment with To and CC recipients.
- [ ] Reply to an existing comment.
- [ ] Confirm the reply appears below its parent comment.
- [ ] Confirm the issue list comment count increases.
- [ ] Confirm comment values remain visible after a validation error.

## 6. Validation and Error Handling

- [ ] Submit incomplete issue and comment forms and confirm Japanese validation messages appear.
- [ ] Confirm progress rejects values outside 0 through 100.
- [ ] Open a detail URL with an invalid or nonexistent issue ID.
- [ ] Confirm friendly 400, 404, and 405 pages do not expose stack traces or database details.
- [ ] Enter HTML-like text and confirm it is displayed as text instead of executing.

## 7. Display and Persistence

- [ ] Confirm Japanese text is displayed without garbled characters.
- [ ] Confirm the shared navigation works on every main page.
- [ ] Confirm priority, status, progress, and due-date indicators are readable.
- [ ] Narrow the browser window and confirm forms and issue cards remain usable.
- [ ] Restart Tomcat and confirm stored issues and comments remain available.

## 8. GitHub Actions and Repository Review

- [ ] Push the final commit and confirm the Java CI workflow displays a green check.
- [ ] Confirm no passwords, `.env` files, IDE settings, or build output are tracked.
- [ ] Confirm README setup and test commands match the final application.

## Expected Scope Limitations

- User authentication and role-based authorization are not included.
- MySQL and Tomcat must be prepared separately for local execution.
- Browser and database workflows are verified manually rather than by automated integration tests.
