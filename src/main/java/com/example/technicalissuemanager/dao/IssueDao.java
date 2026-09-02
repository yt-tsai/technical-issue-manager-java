package com.example.technicalissuemanager.dao;

import com.example.technicalissuemanager.model.Issue;
import com.example.technicalissuemanager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads Issue records from MySQL.
 */
public class IssueDao {

    private static final String FIND_ALL_SQL =
            "SELECT id, title, customer, product, priority, status, progress, "
                    + "assignee, due_date, description, created_at, updated_at "
                    + "FROM issues ORDER BY id";

    public List<Issue> findAll() throws SQLException {
        List<Issue> issues = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                issues.add(mapIssue(resultSet));
            }
        }

        return issues;
    }

    private Issue mapIssue(ResultSet resultSet) throws SQLException {
        Issue issue = new Issue();
        issue.setId(resultSet.getInt("id"));
        issue.setTitle(resultSet.getString("title"));
        issue.setCustomer(resultSet.getString("customer"));
        issue.setProduct(resultSet.getString("product"));
        issue.setPriority(resultSet.getString("priority"));
        issue.setStatus(resultSet.getString("status"));
        issue.setProgress(resultSet.getInt("progress"));
        issue.setAssignee(resultSet.getString("assignee"));

        Date dueDate = resultSet.getDate("due_date");
        if (dueDate != null) {
            issue.setDueDate(dueDate.toLocalDate());
        }

        issue.setDescription(resultSet.getString("description"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            issue.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            issue.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return issue;
    }
}
