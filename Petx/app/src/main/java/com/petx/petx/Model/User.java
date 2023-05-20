package com.petx.petx.Model;

public class User {
    private String userId;
    private String name;
    private String email;
    private boolean isSubscribed;

    public User() {
        // Default constructor required for Firebase Realtime Database
    }

    public User(String userId, String name, String email, boolean isSubscribed) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.isSubscribed = isSubscribed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
