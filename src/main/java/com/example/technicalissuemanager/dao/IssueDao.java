package com.example.technicalissuemanager.dao;

import com.example.technicalissuemanager.model.Issue;
import com.example.technicalissuemanager.model.IssueStatistics;
import com.example.technicalissuemanager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reads Issue records from MySQL.
 */
public class IssueDao {

    private static final String FIND_ALL_SQL =
            "SELECT id, title, customer, product, priority, status, progress, "
                    + "assignee, due_date, description, created_at, updated_at, (SELECT COUNT(*) FROM comments WHERE comments.issue_id = issues.id) AS comment_count "
                    + "FROM issues ORDER BY id";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, title, customer, product, priority, status, progress, "
                    + "assignee, due_date, description, created_at, updated_at, (SELECT COUNT(*) FROM comments WHERE comments.issue_id = issues.id) AS comment_count "
                    + "FROM issues WHERE id = ?";

    private static final String INSERT_SQL =
            "INSERT INTO issues (title, customer, product, priority, status, progress, "
                    + "assignee, due_date, description, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

    private static final String UPDATE_SQL =
            "UPDATE issues SET title = ?, customer = ?, product = ?, priority = ?, "
                    + "status = ?, progress = ?, assignee = ?, due_date = ?, description = ?, "
                    + "updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String DELETE_SQL = "DELETE FROM issues WHERE id = ?";

    private static final String SEARCH_SQL =
            "SELECT id, title, customer, product, priority, status, progress, "
                    + "assignee, due_date, description, created_at, updated_at FROM issues "
                    + "WHERE LOWER(title) LIKE ? OR LOWER(customer) LIKE ? OR LOWER(product) LIKE ? "
                    + "OR LOWER(priority) LIKE ? OR LOWER(status) LIKE ? OR LOWER(assignee) LIKE ? "
                    + "OR CAST(due_date AS CHAR) LIKE ? OR LOWER(description) LIKE ? ORDER BY id";

    private static final String STATISTICS_SQL =
            "SELECT COUNT(*) AS total_count, "
                    + "SUM(CASE WHEN status = 'Open' THEN 1 ELSE 0 END) AS open_count, "
                    + "SUM(CASE WHEN status = 'In Progress' THEN 1 ELSE 0 END) AS in_progress_count, "
                    + "SUM(CASE WHEN status = 'Resolved' THEN 1 ELSE 0 END) AS resolved_count, "
                    + "SUM(CASE WHEN priority = 'High' THEN 1 ELSE 0 END) AS high_count, "
                    + "SUM(CASE WHEN priority = 'Medium' THEN 1 ELSE 0 END) AS medium_count, "
                    + "SUM(CASE WHEN priority = 'Low' THEN 1 ELSE 0 END) AS low_count "
                    + "FROM issues";

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

    public Optional<Issue> findById(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapIssue(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public int save(Issue issue) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, issue.getTitle());
            statement.setString(2, issue.getCustomer());
            statement.setString(3, issue.getProduct());
            statement.setString(4, issue.getPriority());
            statement.setString(5, issue.getStatus());
            statement.setInt(6, issue.getProgress());
            statement.setString(7, issue.getAssignee());
            statement.setDate(8, Date.valueOf(issue.getDueDate()));
            statement.setString(9, issue.getDescription());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("課題の登録に失敗しました。");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        throw new SQLException("登録した課題のIDを取得できませんでした。");
    }

    public boolean update(Issue issue) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, issue.getTitle());
            statement.setString(2, issue.getCustomer());
            statement.setString(3, issue.getProduct());
            statement.setString(4, issue.getPriority());
            statement.setString(5, issue.getStatus());
            statement.setInt(6, issue.getProgress());
            statement.setString(7, issue.getAssignee());
            statement.setDate(8, Date.valueOf(issue.getDueDate()));
            statement.setString(9, issue.getDescription());
            statement.setInt(10, issue.getId());

            return statement.executeUpdate() == 1;
        }
    }

    public List<Issue> findByKeyword(String keyword) throws SQLException {
        List<Issue> issues = new ArrayList<>();
        String searchKeyword = "%" + keyword.toLowerCase() + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_SQL)) {

            for (int index = 1; index <= 8; index++) {
                statement.setString(index, searchKeyword);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    issues.add(mapIssue(resultSet));
                }
            }
        }

        return issues;
    }

    public IssueStatistics getStatistics() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(STATISTICS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            return new IssueStatistics(
                    resultSet.getInt("total_count"),
                    resultSet.getInt("open_count"),
                    resultSet.getInt("in_progress_count"),
                    resultSet.getInt("resolved_count"),
                    resultSet.getInt("high_count"),
                    resultSet.getInt("medium_count"),
                    resultSet.getInt("low_count"));
        }
    }

    public boolean deleteById(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
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
        issue.setCommentCount(resultSet.getInt("comment_count"));

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
