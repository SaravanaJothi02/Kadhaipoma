package com.base.model;

import java.sql.Timestamp;

public class Message {
    private final Integer id;
    private final Integer senderId;
    private final Integer receiverId;
    private String text;
    private MessageStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt; // future update

    public Message(Integer id, Integer senderId, Integer receiverId, String text, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.status = MessageStatus.valueOf(status.toUpperCase());
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
