package com.example.technicalissuemanager.dao;

import com.example.technicalissuemanager.model.Comment;
import com.example.technicalissuemanager.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes comments for an issue.
 */
public class CommentDao {

    private static final String FIND_BY_ISSUE_ID_SQL =
            "SELECT comment_id, reply_to, author, `to`, content, created_at "
                    + "FROM comments WHERE issue_id = ? ORDER BY comment_id";
    private static final String FIND_CC_SQL =
            "SELECT cc_name FROM comment_cc WHERE comment_id = ? ORDER BY cc_order";
    private static final String INSERT_COMMENT_SQL =
            "INSERT INTO comments (issue_id, reply_to, author, `to`, content, created_at) "
                    + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
    private static final String INSERT_CC_SQL =
            "INSERT INTO comment_cc (comment_id, cc_order, cc_name) VALUES (?, ?, ?)";
    private static final String UPDATE_ISSUE_TIMESTAMP_SQL =
            "UPDATE issues SET updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    public List<Comment> findByIssueId(int issueId) throws SQLException {
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ISSUE_ID_SQL)) {

            statement.setInt(1, issueId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(resultSet.getInt("comment_id"));
                    int replyTo = resultSet.getInt("reply_to");
                    comment.setReplyTo(resultSet.wasNull() ? null : replyTo);
                    comment.setAuthor(resultSet.getString("author"));
                    comment.setTo(resultSet.getString("to"));
                    comment.setContent(resultSet.getString("content"));
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    if (createdAt != null) {
                        comment.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    comment.setCc(findCc(connection, comment.getCommentId()));
                    comments.add(comment);
                }
            }
        }

        return comments;
    }

    public int save(int issueId, Comment comment) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement commentStatement = connection.prepareStatement(
                    INSERT_COMMENT_SQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ccStatement = connection.prepareStatement(INSERT_CC_SQL);
                 PreparedStatement timestampStatement = connection.prepareStatement(UPDATE_ISSUE_TIMESTAMP_SQL)) {

                commentStatement.setInt(1, issueId);
                if (comment.getReplyTo() == null) {
                    commentStatement.setNull(2, Types.BIGINT);
                } else {
                    commentStatement.setInt(2, comment.getReplyTo());
                }
                commentStatement.setString(3, comment.getAuthor());
                commentStatement.setString(4, comment.getTo());
                commentStatement.setString(5, comment.getContent());
                commentStatement.executeUpdate();

                int commentId;
                try (ResultSet generatedKeys = commentStatement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("登録したコメントのIDを取得できませんでした。");
                    }
                    commentId = generatedKeys.getInt(1);
                }

                int ccOrder = 1;
                for (String ccName : comment.getCc()) {
                    ccStatement.setInt(1, commentId);
                    ccStatement.setInt(2, ccOrder++);
                    ccStatement.setString(3, ccName);
                    ccStatement.addBatch();
                }
                ccStatement.executeBatch();

                timestampStatement.setInt(1, issueId);
                timestampStatement.executeUpdate();
                connection.commit();
                return commentId;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private List<String> findCc(Connection connection, int commentId) throws SQLException {
        List<String> cc = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_CC_SQL)) {
            statement.setInt(1, commentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cc.add(resultSet.getString("cc_name"));
                }
            }
        }

        return cc;
    }
}
