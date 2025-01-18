package com.base.model;

import java.sql.Timestamp;

public class Friend {
    private final Integer id;
    private final Integer userId;
    private final Integer friendId;
    private FriendsStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Friend(Integer id, Integer userId, Integer friendId, FriendsStatus status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public FriendsStatus getStatus() {
        return status;
    }

    public void setStatus(FriendsStatus status) {
        this.status = status;
    }
}
