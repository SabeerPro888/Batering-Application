package com.example.barteringapp7;

import java.util.List;

public class GlobalVariables {
    private static GlobalVariables instance;
    private String userId;
    private String email;

    public List<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    private List<Items> itemsList;
    private String category;
    private String SubCategory;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

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
