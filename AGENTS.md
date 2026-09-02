# AGENTS.md

## Project overview

This repository contains `technical-issue-manager`, a student project that starts as a Python prototype and will later be expanded into a Java / Servlet / HTML / MySQL web application.

## Current phase

Phase 1: build the Java web foundation from the Python prototype.

Focus on:

- project structure
- domain models
- persistence
- servlet/JSP flow
- issue CRUD
- search/statistics
- comments and reply-to-comment support

## Important domain model

### Issue

- id
- title
- customer
- product
- priority
- status
- progress
- assignee
- due_date
- description
- comments
- created_at
- updated_at

### Comment

- comment_id
- reply_to
- author
- to
- cc
- content
- created_at

## General rules

- Preserve existing behavior unless the task explicitly asks to change it.
- Keep user-facing labels in Japanese unless otherwise instructed.
- Use UTF-8 everywhere.
- Prefer small, incremental changes.
- Do not refactor unrelated code.
- Do not rename public fields, routes, or labels unless necessary.
- If a file or API is not clearly documented, inspect the existing code before changing it.
- If something is unclear, stop and explain the uncertainty instead of guessing.

## Current reference behavior

If the Python prototype exists in this repository or a sibling folder, use it as the behavior reference.

The prototype already includes:

- issue list
- issue create
- issue search
- issue update
- issue delete
- issue statistics
- comment add
- reply-to-comment
- issue detail
- JSON persistence

Try to preserve these behaviors in the Java version.

## Suggested Java project structure

Use a standard Maven-based web application structure unless the repository already uses a different build tool.

Suggested packages:

- com.example.technicalissuemanager.model
- com.example.technicalissuemanager.dao
- com.example.technicalissuemanager.service
- com.example.technicalissuemanager.servlet
- com.example.technicalissuemanager.util

Suggested web resources:

- src/main/webapp/WEB-INF/jsp/
- src/main/resources/
- src/main/resources/sql/
- docs/sql/

## Database rules

- Keep table and column names aligned with the current prototype fields.
- Preserve backward compatibility when adding fields.
- If schema changes are needed, provide SQL scripts or migration notes.
- Keep issue/comment relations explicit and easy to query.

## Workflow

- Read the repository first.
- Make one small complete change at a time.
- After each change, report:
  - files changed
  - what changed
  - how to test
  - any risks or follow-up work
- Prefer passing tests over large refactors.
- Before finishing, ensure `git status` is clean.

## Testing

Run the relevant build/test commands for the repository and report the results.

Suggested commands:

- mvn test
- mvn package

If the repository adds integration tests or servlet tests later, run those too.

## Git workflow

- Only commit completed, tested changes.
- Use one commit per logical step.
- Do not amend existing commits.
- Leave the worktree clean after the task.
- If a commit is created, report the commit hash.

## UI / JSP rules

- Keep labels and messages consistent with the current prototype terminology.
- Keep forms simple and readable.
- Avoid unnecessary styling before core functionality is complete.
- Prefer clarity over visual polish in Phase 1.

## Notes for this project

This project is for a school presentation and portfolio use.
The first Java version should be small, stable, and easy to explain.
