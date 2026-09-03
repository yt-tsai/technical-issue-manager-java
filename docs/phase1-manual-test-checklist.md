# Phase 1 Manual Test Checklist

> This checklist records the Phase 1 baseline. For the current application behavior, use the [Phase 2 checklist](phase2-manual-test-checklist.md).

Run these checks after MySQL and Tomcat are running. Use the application URL:

```text
http://localhost:8080/technical-issue-manager
```

## 1. Issue List

- [ ] Open `/issues`.
- [ ] Confirm issue cards load from MySQL.
- [ ] Confirm each card shows its comment count.
- [ ] Open an issue detail page by clicking its title.

## 2. Create Issue

- [ ] Open `/issues/create`.
- [ ] Enter all required fields and submit.
- [ ] Confirm the new detail page opens.
- [ ] Return to `/issues` and confirm the new issue appears.

## 3. Edit Issue

- [ ] Open a detail page and select `編集`.
- [ ] Change a title, status, progress, or description.
- [ ] Submit and confirm the detail page shows the changed values.

## 4. Delete Issue

- [ ] Create or select a disposable test issue.
- [ ] Select `削除` from its detail page.
- [ ] Confirm the browser confirmation dialog.
- [ ] Confirm the issue no longer appears in the list.

## 5. Search

- [ ] Open `/issues/search`.
- [ ] Search with a matching keyword, such as `portal`.
- [ ] Confirm matching issues appear.
- [ ] Search with a non-matching keyword and confirm the 0-result message.
- [ ] Phase 1 baseline: submitting a blank keyword displayed an input message. Phase 2 intentionally allows searches without a keyword.

## 6. Statistics

- [ ] Open `/issues/statistics`.
- [ ] Confirm the total count matches the list.
- [ ] Confirm status counts and priority counts match the stored issues.

## 7. Comments and Replies

- [ ] Open an issue detail page.
- [ ] Add a comment with an author, To recipient, optional comma-separated CC recipients, and content.
- [ ] Confirm the comment appears and the issue list comment count increases.
- [ ] Select `返信` for that comment.
- [ ] Add a reply and confirm `返信元` shows the original comment number.

## Expected Error Handling

- [ ] `/issues/detail?id=999999` returns a not-found page.
- [ ] `/issues/detail?id=abc` returns a bad-request page.
- [ ] `/issues/edit?id=abc` returns a bad-request page.

## Notes

- Use only disposable test issues for delete tests.
- Deleting an issue also deletes its comments through the MySQL foreign-key setting.
- The MySQL auto-increment ID may skip numbers after test records are deleted; this is normal.
