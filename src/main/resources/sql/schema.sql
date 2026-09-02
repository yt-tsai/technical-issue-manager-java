-- Technical Issue Manager - Phase 1 database schema
-- MySQL 8.0+

CREATE TABLE IF NOT EXISTS issues (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    customer VARCHAR(255) NOT NULL,
    product VARCHAR(255) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    progress TINYINT UNSIGNED NOT NULL DEFAULT 0,
    assignee VARCHAR(255) NOT NULL,
    due_date DATE NOT NULL,
    description TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_issues_progress CHECK (progress BETWEEN 0 AND 100),
    INDEX idx_issues_status (status),
    INDEX idx_issues_priority (priority),
    INDEX idx_issues_assignee (assignee),
    INDEX idx_issues_due_date (due_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT NOT NULL AUTO_INCREMENT,
    issue_id BIGINT NOT NULL,
    reply_to BIGINT NULL,
    author VARCHAR(255) NOT NULL,
    `to` VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_issue
        FOREIGN KEY (issue_id) REFERENCES issues (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_comments_reply
        FOREIGN KEY (reply_to) REFERENCES comments (comment_id)
        ON DELETE SET NULL,
    INDEX idx_comments_issue (issue_id),
    INDEX idx_comments_reply_to (reply_to)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comment_cc (
    comment_id BIGINT NOT NULL,
    cc_order INT NOT NULL,
    cc_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (comment_id, cc_order),
    CONSTRAINT fk_comment_cc_comment
        FOREIGN KEY (comment_id) REFERENCES comments (comment_id)
        ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
