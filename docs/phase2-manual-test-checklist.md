# Phase 2 Manual Test Checklist

Run these checks after MySQL and Tomcat are running. Use disposable records for create, edit, delete, and comment tests.

```text
http://localhost:8080/technical-issue-manager
```

## 1. Shared Layout and Navigation

- [ ] Open `/issues` and confirm the shared header has links for list, create, search, and statistics.
- [ ] Confirm Japanese labels are displayed without garbled characters.
- [ ] Open each main page and confirm the same navigation and styling are used.
- [ ] Narrow the browser window and confirm navigation, forms, and cards remain usable.
- [ ] Confirm priority and status badges use consistent colors.
- [ ] Confirm overdue, urgent, warning, and resolved due-date labels appear when applicable.

## 2. Issue Form Validation

- [ ] Open `/issues/create` and submit incomplete required fields.
- [ ] Confirm invalid fields are identified and previously entered values remain visible.
- [ ] Confirm progress accepts only an integer from 0 through 100.
- [ ] Confirm priority and status are restricted to the displayed options.
- [ ] Repeat the validation checks on an existing issue edit page.
- [ ] Create and edit a disposable issue with Japanese text and confirm it is stored correctly.

## 3. Messages and Error Pages

- [ ] Create, edit, or delete a disposable issue and confirm a success message appears.
- [ ] Refresh or revisit the page and confirm the success message does not repeat.
- [ ] Open `/issues/detail?id=999999` and confirm the friendly not-found page appears.
- [ ] Open `/issues/detail?id=abc` and confirm the friendly bad-request page appears.
- [ ] Confirm internal database details and stack traces are not shown in the browser.

## 4. Advanced Search and Sorting

- [ ] Open `/issues/search`.
- [ ] Search using only a keyword and confirm the original search behavior still works.
- [ ] Leave the keyword blank and filter by priority or status.
- [ ] Combine a keyword, priority, and status in one search.
- [ ] Test registration, newest update, nearest due date, and highest priority sorting.
- [ ] Confirm the selected conditions remain visible after searching.
- [ ] Confirm each result displays its comment count.
- [ ] Select `条件をクリア` and confirm the form returns to its defaults.

## 5. Comment Threads and Validation

- [ ] Open an issue that contains a reply and confirm the reply is indented below its parent comment.
- [ ] Confirm comment timestamps use the `yyyy-MM-dd HH:mm` format.
- [ ] Select `返信する` and confirm the parent comment preview appears.
- [ ] Confirm the reply To field defaults to the parent comment author.
- [ ] Add a comment and confirm the issue list comment count increases.
- [ ] Add a reply and confirm it appears below the selected parent comment.
- [ ] Submit a comment with missing required values and confirm field errors appear without losing other input.
- [ ] Enter more than 20 comma-separated CC recipients and confirm the validation message appears.
- [ ] Confirm a nonexistent or cross-issue reply target returns a bad-request page.

## 6. Regression Checks

- [ ] Confirm issue list, detail, create, edit, and delete still work.
- [ ] Confirm statistics match the current stored records.
- [ ] Confirm descriptions and comment content escape HTML instead of executing it.
- [ ] Confirm issue and comment data remains after restarting Tomcat.

## 7. Build Check

From the project root, run:

```bash
mvn clean package
```

- [ ] Confirm the command completes successfully.
- [ ] Confirm `target/technical-issue-manager.war` is generated.

## Notes

- MySQL auto-increment IDs can skip numbers after test records are removed; this is normal.
- Visual reply indentation is capped after three levels, but deeper reply relationships remain available.
- Phase 3 automated tests and deployment preparation are not included in this checklist.
