package com.base.model;

public enum FriendsStatus {
    PENDING, ACCEPTED, REJECTED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
