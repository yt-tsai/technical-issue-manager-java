package com.example.technicalissuemanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A comment posted on an issue.
 */
public class Comment {

    private int commentId;
    private Integer replyTo;
    private Comment replyToComment;
    private int threadDepth;
    private String author;
    private String to;
    private List<String> cc = new ArrayList<>();
    private String content;
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(int commentId, Integer replyTo, String author, String to,
                   List<String> cc, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.replyTo = replyTo;
        this.author = author;
        this.to = to;
        setCc(cc);
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Integer getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }

    public Comment getReplyToComment() {
        return replyToComment;
    }

    public void setReplyToComment(Comment replyToComment) {
        this.replyToComment = replyToComment;
    }

    public int getThreadDepth() {
        return threadDepth;
    }

    public void setThreadDepth(int threadDepth) {
        this.threadDepth = threadDepth;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc == null ? new ArrayList<>() : new ArrayList<>(cc);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
