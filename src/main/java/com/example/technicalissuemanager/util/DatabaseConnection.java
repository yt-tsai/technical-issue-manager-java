package com.example.technicalissuemanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates JDBC connections to the MySQL database.
 *
 * Configuration is read from environment variables first, then Java system
 * properties. The database password is never stored in the source code.
 */
public final class DatabaseConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/technical_issue_manager"
                    + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getValue("DB_URL", "db.url", DEFAULT_URL);
        String user = getRequiredValue("DB_USER", "db.user");
        String password = getValue("DB_PASSWORD", "db.password", "");

        return DriverManager.getConnection(url, user, password);
    }

    private static String getRequiredValue(String environmentName, String propertyName) {
        String value = getValue(environmentName, propertyName, "");

        if (value.isBlank()) {
            throw new IllegalStateException(
                    "Database configuration is missing: set " + environmentName
                            + " or -D" + propertyName);
        }

        return value;
    }

    private static String getValue(String environmentName, String propertyName, String defaultValue) {
        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        return defaultValue;
    }
}
