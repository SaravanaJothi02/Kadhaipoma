package com.base.model;

public enum MessageStatus {
    SENT, DELIVERED, READ, DELETED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
