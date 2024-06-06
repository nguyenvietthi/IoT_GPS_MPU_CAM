package com.example.followvehicle.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String account_name;
    private String cookie;

    public LoggedInUser(String userId, String displayName, String account_name, String cookie) {
        this.userId = userId;
        this.displayName = displayName;
        this.account_name = account_name;
        this.cookie = cookie;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}