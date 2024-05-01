package com.example.barteringapp7;

import android.app.Application;

public class MyApplication extends Application {

    private static String userId;
    private static String email;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String value) {
        userId = value;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String value) {
        email = value;
    }

}
