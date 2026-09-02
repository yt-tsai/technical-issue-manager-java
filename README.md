# Technical Issue Manager

A student portfolio project that evolves the Python CLI prototype **技術課題管理システム** into a Java web application for managing technical issues.

## Phase 1 Status

Phase 1 is functionally complete. It provides the first Java / Servlet / JSP / MySQL version of the original Python prototype.

## Features

- Issue list with live comment counts
- Issue detail view
- Create, edit, and delete issues
- Keyword search across issue fields
- Statistics by status and priority
- Add comments with To and CC recipients
- Reply to a comment
- MySQL persistence with UTF-8 support

## Technology

- Java 21
- Maven
- Jakarta Servlet API 6.0
- JSP
- MySQL 8.0+
- Apache Tomcat 11 for local development

## Project Structure

```text
technical-issue-manager-java/
├── pom.xml
├── README.md
├── docs/
│   └── phase1-manual-test-checklist.md
└── src/
    └── main/
        ├── java/com/example/technicalissuemanager/
        │   ├── dao/
        │   ├── model/
        │   ├── servlet/
        │   └── util/
        ├── resources/sql/
        │   ├── schema.sql
        │   └── sample-data.sql
        └── webapp/WEB-INF/
            ├── jsp/
            └── web.xml
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

See [Phase 1 manual test checklist](docs/phase1-manual-test-checklist.md).

## Scope

This repository currently covers Phase 1 only. Phase 2 will address UI refinement, stronger validation, error-handling cleanup, and search or comment-display improvements. Phase 3 will cover formal tests, presentation preparation, final refactoring, and deployment preparation.
