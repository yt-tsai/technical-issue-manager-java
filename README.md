# Technical Issue Manager

A student portfolio project that evolves a Python CLI prototype into a Java web application for managing technical issues.

## Current Phase

Phase 1: build the first Java / Servlet / JSP / MySQL version from the Python prototype.

The current implementation is the initial Maven web application skeleton. Business features will be added incrementally.

## Technology

- Java 21
- Maven
- Servlet / JSP
- MySQL (planned)

## Current Progress

### Completed

- Inspected the Python CLI prototype and its JSON data format
- Created the Maven WAR project structure
- Added UTF-8 build settings
- Added a basic `web.xml`
- Added `.gitignore` for Maven, IDE, macOS, and local environment files

### Planned for Phase 1

- Define `Issue` and `Comment` domain classes
- Create the MySQL schema
- Add database connection utilities
- Implement issue list, detail, create, update, and delete
- Implement search and statistics
- Implement comments and reply-to-comment
- Add basic JSP pages

## Reference Prototype

The original Python prototype is kept under:

```text
Reference Sample_Python prototype/technical-issue-manager/prototype-python/
```

It provides the behavior reference for issue management, statistics, comments, replies, issue details, JSON persistence, and compatibility with older records.

## Project Structure

```text
technical-issue-manager-java/
├── pom.xml
├── README.md
├── AGENTS.md
├── docs/
└── src/
    ├── main/
    │   ├── java/
    │   ├── resources/
    │   └── webapp/WEB-INF/
    │       ├── jsp/
    │       └── web.xml
    └── test/java/
```

## Build and Test

Run the following commands from the project root:

```bash
mvn test
mvn package
```

The generated WAR file will be placed in `target/`.

## Scope

Only Phase 1 is being implemented at this stage. UI refinement, stronger validation, documentation, presentation preparation, deployment preparation, and final refactoring are reserved for later phases.
