package com.example.barteringapp7;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {

    @GET("BarteringAppAPI/api/Index/Login")
    Call<String> Login(
            @Query("Email") String email,
            @Query("Password") String password
    );

    @GET("BarteringAppAPI/api/Index/getCategories") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<List<Category>> getCategories();

    @GET("BarteringAppAPI/api/Index/getSubCategoriess") // Specify your endpoint path here
    Call<List<SubCategory>> getSubCategories(@Query("category") String category);
    @Multipart
    @POST("BarteringAppAPI/api/Index/UploadItem")
    Call<Integer> uploadItem(
            @Part("Email") RequestBody email,
            @Part("Item_name") RequestBody itemName,
            @Part("Category") RequestBody category,
            @Part("Description") RequestBody description,
            @Part("Barter_for") RequestBody barterFor,
            @Part("Price") RequestBody price,
            @Part MultipartBody.Part[] images
    );

    @GET("BarteringAppAPI/api/Index/getUserId") // Specify your endpoint path here
    Call<String> getUserId(@Query("email") String email);
    @GET("BarteringAppAPI/api/Index/ShowAllItems") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<List<Items>> vewAllItems(
            @Query("email") String email
                                              );
    @POST("BarteringAppAPI/api/Index/SendVerificationRequest") // Specify your endpoint path here
    Call<Void> sendVerificationRequest(@Query("Item_id") String item_id);

    @GET("BarteringAppAPI/api/Index/ViewVerificationRequests") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<List<Items>> viewverificationRequests();


    @POST("BarteringAppAPI/api/Index/VerifyRequest")
    Call<String> verifyItem(
            @Query("id") int verificationid,
            @Query("status") String verificationstatus,
            @Query("message") String Message
            );

    @GET("BarteringAppAPI/api/Index/MyItems") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<List<Items>> ViewUploads(
            @Query("Email") String email

    );

    @GET("BarteringAppAPI/api/Index/getSubcategorybrands") // Specify your endpoint path here
    Call<List<String>> getSubCategoriesBrand(@Query("subcategoryname") String category);

    @GET("BarteringAppAPI/api/Index/getmodels") // Specify your endpoint path here
    Call<List<String>> getModelsByBrand(@Query("brandname") String brandname);

    @GET("BarteringAppAPI/api/Index/getsubcategoryAttributes") // Specify your endpoint path here
    Call<List<String>> getsubcategoryattributes(@Query("subcategory") String subcategory);

    @GET("BarteringAppAPI/api/Index/getattributevalues") // Specify your endpoint path here
    Call<List<String>> getttributevalues(@Query("subcategory") String subcategory,
    @Query("attributename") String attributename
    );


    @Multipart
    @POST("BarteringAppAPI/api/Index/UploadItem2")
    Call<Integer> uploadItem2(
            @Part("Email") RequestBody email,
            @Part("Item_name") RequestBody itemName,
            @Part("subCategory") RequestBody subcategory,
            @Part("Category") RequestBody category,
            @Part("Description") RequestBody description,
            @Part("Barter_for") RequestBody barterFor,
            @Part("Price") RequestBody price,
            @Part("Attributes") RequestBody attributesJson, // New parameter for attributes JSON
            @Part MultipartBody.Part[] images,
            @Part("Brand") RequestBody brand,
            @Part("Model") RequestBody model
    );

    @GET("BarteringAppAPI/api/Index/getItemDetails") // Specify your endpoint path here
    Call<Items> getItemDetails(@Query("id") String itemid
    );

    @GET("BarteringAppAPI/api/Index/GetNotificationsWithItemsAndImages") // Specify your endpoint path here
    Call<List<Items>> getNotifications(@Query("email") String email
    );

    @GET("BarteringAppAPI/api/Index/GetNumOfNotifications") // Specify your endpoint path here
    Call<Integer> getNotificationsCount(@Query("email") String email
    );

    @GET("BarteringAppAPI/api/Index/getAverageOfItems") // Specify your endpoint path here
    Call<Double> getElectronicsAverage(@Query("attribute") String attribute
    );

    @GET("BarteringAppAPI/api/Index/GetRecommendation1") // Specify your endpoint path here
    Call<List<Items>> getRecommendedPosts(@Query("email") String email
    );

    @Multipart
    @POST("BarteringAppAPI/api/Index/SendOffer")
    Call<String> SendOffer(@Part("SelectedItemIds") RequestBody SelectedItemIds, @Query("senderId") int senderId, @Query("RequestItemId") int itemId, @Query("price") int price,@Query("RequestDescription")String RequestDescription);

      @GET("BarteringAppAPI/api/Index/viewRequest") // Specify your endpoint path here
    Call<List<ViewRequestsInformation>> Viewrequests(@Query("email") String email
    );

    @GET("BarteringAppAPI/api/Index/getAllItemsForOfferId") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<List<Items>> getAllItemsForOfferId(
            @Query("OfferId") int OfferId

    );

    @POST("BarteringAppAPI/api/Index/AcceptOrRejectOffer") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<Void> AcceptOrRejectOffer(
            @Query("Numstatus") int Status,
            @Query("Offerid") int OfferId

    );

    @GET("BarteringAppAPI/api/Index/ViewHistory") // This should match the endpoint defined in your ASP.NET Web API controller
    Call<List<ViewRequestsInformation>> ViewHistory(
            @Query("email") String Status

    );

    @GET("BarteringAppAPI/api/Index/GetRecommendationLastUploaded") // Specify your endpoint path here
    Call<List<Items>> GetRecommendationLastUploaded(@Query("email") String email
    );


}
