package com.example.barteringapp7;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Items implements Serializable {
    public String getItem_name() {
        return Item_name;
    }

    public void setItem_name(String item_name) {
        Item_name = item_name;
    }

    public String getBarter_for() {
        return Barter_for;
    }

    public void setBarter_for(String barter_for) {
        Barter_for = barter_for;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage_01() {
        return Image_01;
    }

    public void setImage_01(String image_01) {
        Image_01 = image_01;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }
    public int getItem_id() {
        return Item_id;
    }

    public void setItem_id(int item_id) {
        Item_id = item_id;
    }
    public String getVerification_status() {
        return Verification_status;
    }

    public void setVerification_status(String verification_status) {
        Verification_status = verification_status;
    }
    public String getImage_02() {
        return Image_02;
    }

    public void setImage_02(String image_02) {
        Image_02 = image_02;
    }

    public String getImage_03() {
        return Image_03;
    }

    public void setImage_03(String image_03) {
        Image_03 = image_03;
    }

    public String getImage_04() {
        return Image_04;
    }

    public void setImage_04(String image_04) {
        Image_04 = image_04;
    }

    public String getImage_05() {
        return Image_05;
    }

    public void setImage_05(String image_05) {
        Image_05 = image_05;
    }
    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getVerification_id() {
        return Verification_id;
    }

    public void setVerification_id(int verification_id) {
        Verification_id = verification_id;
    }


    public String getAttributesJson() {
        return AttributesJson;
    }

    public void setAttributesJson(String attributesJson) {
        AttributesJson = attributesJson;
    }

    String AttributesJson;
    String Item_name;

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String Barter_for;
    String Description;
    String Image_01;
    String Image_02;
    String Image_03;
    String Image_04;
    String Image_05;
    String User_name;
    String Verification_status;
    int Verification_id;

    double Rating;

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    int Price;

    String isSold;
    String Category;
    String subCategory;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    private int User_id;

    public int getUserId() {
        return User_id;
    }

    public void setUserId(int userId) {
        this.User_id = userId;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    String ProfilePic;


    public String getIsSold() {
        return isSold;
    }

    public void setIsSold(String isSold) {
        this.isSold = isSold;
    }

    int Item_id;


}
