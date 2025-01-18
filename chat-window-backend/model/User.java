package com.base.model;

import java.sql.Timestamp;

public class User {
    private Integer id;
    private String mail;
    private String userName;
    private UserStatus status;
    private Timestamp cratedAt;
    private Timestamp lastSeen;

    public User(Integer id, String mail, String userName, String status, Timestamp cratedAt, Timestamp lastSeen) {
        this.id = id;
        this.mail = mail;
        this.userName = userName;
        this.status = UserStatus.valueOf(status);
        this.cratedAt = cratedAt;
        this.lastSeen = lastSeen;
    }

    public Integer getId(){
        return id;
    }

    public String getEmail(){
        return mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Timestamp getCratedAt() {
        return cratedAt;
    }

    public void setCratedAt(Timestamp cratedAt) {
        this.cratedAt = cratedAt;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }
}
