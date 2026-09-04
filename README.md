# Technical Issue Manager

A student portfolio project that evolves the Python CLI prototype **技術課題管理システム** into a Java web application for managing technical issues.

## Project Status

Phase 1 and Phase 2 are functionally complete. Phase 3 is in progress, beginning with automated unit tests and final project preparation.

## Features

- Issue list and detail views with live comment counts
- Create, edit, and delete issues with server-side validation
- Responsive shared navigation and consistent page styling
- Priority, status, progress, and due-date indicators
- Combined keyword, priority, and status search filters
- Safe sorting by registration, update time, due date, or priority
- Statistics by status and priority
- Threaded comments with To and CC recipients
- Reply previews, automatic reply recipients, and comment validation
- Friendly success and error messages
- MySQL persistence with UTF-8 support

## Technology

- Java 21
- Maven
- Jakarta Servlet API 6.0
- JSP
- MySQL 8.0+
- Apache Tomcat 11 for local development
- JUnit 6 and Mockito 5 for unit testing

## Project Structure

```text
technical-issue-manager-java/
├── pom.xml
├── README.md
├── docs/
│   ├── phase1-manual-test-checklist.md
│   └── phase2-manual-test-checklist.md
└── src/
    ├── main/
    │   ├── java/com/example/technicalissuemanager/
    │   │   ├── dao/
    │   │   ├── model/
    │   │   ├── servlet/
    │   │   ├── util/
    │   │   └── validation/
    │   ├── resources/sql/
    │   │   ├── schema.sql
    │   │   └── sample-data.sql
    │   └── webapp/
    │       ├── css/
    │       └── WEB-INF/
    │           ├── jsp/
    │           └── web.xml
    └── test/java/com/example/technicalissuemanager/
        ├── util/
        └── validation/
```

## Database Setup

Use a local MySQL administrator account to create the database and a project-specific application account. Replace the example password with your own local password.

```sql
CREATE DATABASE technical_issue_manager
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER 'technical_issue_app'@'localhost'
    IDENTIFIED BY 'choose-a-local-password';

GRANT ALL PRIVILEGES ON technical_issue_manager.*
    TO 'technical_issue_app'@'localhost';

FLUSH PRIVILEGES;
```

Apply the schema from the project root:

```bash
mysql -u technical_issue_app -p technical_issue_manager \
  < src/main/resources/sql/schema.sql
```

Optional: load the included baseline sample issues.

```bash
mysql -u technical_issue_app -p technical_issue_manager \
  < src/main/resources/sql/sample-data.sql
```

## Database Configuration

The application reads database settings in this order:

1. Environment variables
2. Java system properties
3. Local development defaults in `DatabaseConnection.java`

For a Tomcat process started from a terminal, configure environment variables in that same terminal before starting Tomcat:

```bash
export DB_URL="jdbc:mysql://localhost:3306/technical_issue_manager?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo"
export DB_USER="technical_issue_app"
export DB_PASSWORD="your-local-password"
```

For a service-managed Tomcat installation, configure the equivalent values in the service environment or as JVM properties:

```text
-Ddb.url=...
-Ddb.user=technical_issue_app
-Ddb.password=your-local-password
```

Real credentials are intentionally not part of this repository. Keep them in local environment or service configuration and never add them to version control.

## Automated Tests

Run the unit tests from the project root. MySQL and Tomcat do not need to be running.

```bash
mvn test
```

The current tests cover HTML escaping, issue form validation, and issue priority, status, and due-date display rules.

## Build and Run

Build the WAR file:

```bash
mvn clean package
```

The generated file is:

```text
target/technical-issue-manager.war
```

For the local Homebrew Tomcat setup used during development:

```bash
cp target/technical-issue-manager.war /opt/homebrew/opt/tomcat/libexec/webapps/technical-issue-manager.war
brew services restart tomcat
```

Open the application:

```text
http://localhost:8080/technical-issue-manager/issues
```

## Main Pages

| Function | URL |
| --- | --- |
| Issue list | `/issues` |
| Create issue | `/issues/create` |
| Issue detail | `/issues/detail?id=1` |
| Edit issue | `/issues/edit?id=1` |
| Search | `/issues/search` |
| Statistics | `/issues/statistics` |

## Verification

- [Phase 1 baseline manual test checklist](docs/phase1-manual-test-checklist.md)
- [Phase 2 current manual test checklist](docs/phase2-manual-test-checklist.md)

## Scope

Phase 1 and Phase 2 are complete. Phase 3 is in progress and covers automated tests, presentation preparation, final documentation and refactoring, and deployment preparation.
