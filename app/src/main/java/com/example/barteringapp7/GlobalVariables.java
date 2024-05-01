package com.example.barteringapp7;

public class GlobalVariables {
    private static GlobalVariables instance;
    private String userId;
    private String email;

    private GlobalVariables() {
        // Private constructor to prevent instantiation
    }

    public static synchronized GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        userId = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        email = value;
    }
}
