package com.example.barteringapp7;

public class UserResponse {
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return UserType;
    }

    String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    String password;
    String UserType;
}
