package com.example.barteringapp7;

import static android.widget.FrameLayout.*;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    Button btnMakeOffer;
    TextView tvOwnerName;
    TextView tvRating;
    TextView tvDescription;
    TextView tvTitle;
    TextView tvBarterWith;
    TextView tvValue;
    Button btnaccept;
    Button btnReject;

    TextView txtVerification;

    LinearLayout attributesContainer;
    ImageView verificationImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Items item = (Items) getIntent().getSerializableExtra("item_details");

        attributesContainer = findViewById(R.id.attributesContainer);
        txtVerification = findViewById(R.id.txtVerificationStatus);
//        img1=findViewById(R.id.imageView1);
//        img2=findViewById(R.id.imageView2);
//        img3=findViewById(R.id.imageView3);
//        img4=findViewById(R.id.imageView4);
//        img5=findViewById(R.id.imageView5);
        tvOwnerName = findViewById(R.id.txtOwnerName);
        tvRating = findViewById(R.id.txtRating);
        tvDescription = findViewById(R.id.txtDescriptionItemDetails);
        tvTitle = findViewById(R.id.txtItemName);
        tvBarterWith = findViewById(R.id.txtBarterWith);
        tvValue = findViewById(R.id.txtItemValue);
        btnaccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnMakeOffer=findViewById(R.id.btnMakeOffer);

        btnMakeOffer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemDetailsActivity.this,Activity_MakeBarterOffer.class);
                intent.putExtra("item_details", item);
                startActivity(intent);            }
        });


        // Inside your activity or fragment


        TextView toolbarTitle = findViewById(R.id.toolbar_title);

// Set the title based on the activity
        toolbarTitle.setText("Item Details");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Retrieve item details from intent

        displayItemDetails(item);












        String email = GlobalVariables.getInstance().getEmail();
        if (email.equals("Trusted")) {
            btnaccept.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
            btnMakeOffer.setVisibility(View.GONE);
        } else {
            btnaccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);


                Call<String> call = apiService.verifyItem(item.getVerification_id(), "Verified", "Item is verified");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(ItemDetailsActivity.this, TrustedActivity.class);
                            startActivity(intent);

                        } else {
                            Log.e("API Call", "Response was unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);

                // Set the title and message of the dialog
                builder.setTitle("Reject Reason");
                builder.setMessage("Please provide a reason for rejecting the item:");

                // Create an EditText to allow the user to input the reason
                final EditText input = new EditText(ItemDetailsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the text entered by the user
                        String reason = input.getText().toString();

                        Call<String> call = apiService.verifyItem(item.getVerification_id(), "Not Verified", reason);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Intent intent = new Intent(ItemDetailsActivity.this, TrustedActivity.class);
                                    startActivity(intent);

                                } else {
                                    Log.e("API Call", "Response was unsuccessful");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }
                });
                // Process the rejection with the reason provided

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if Cancel is clicked
                        dialog.cancel();
                    }
                });

                // Show the AlertDialog
                builder.show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Call the superclass implementation for default behavior (going back to the previous activity)
        // Add any additional functionality here if needed
    }


//      private void displayItemDetails(Items item){
//          APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
//
//          Call<List<Items>> call = apiService.vewAllItems();
//          call.enqueue(new Callback<List<Items>>() {
//
//              @Override
//              public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
//                  if (response.isSuccessful()) {
//                      List<Items> Items = (List<Items>) response.body();
//                      ArrayList<String> imagePaths=new ArrayList<>();
//                      for(Items b:Items){
//
//                          if(b.getItem_id()== item.getItem_id()){
//                              if(item.getImage_01()!=null){
//                                  String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + item.getImage_01();
////                                  Picasso.get().load(imagePath).into(img1);
//                                  imagePaths.add(imagePath);
//                              }
//                              if(item.getImage_02()!=null){
//                                  String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + item.getImage_02();
////                                  Picasso.get().load(imagePath).into(im2);
//                                  imagePaths.add(imagePath);
//                              }
//                              if(item.getImage_03()!=null){
//                                  String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + item.getImage_03();
////                                  Picasso.get().load(imagePath).into(im3);
//                                  imagePaths.add(imagePath);
//                              }
//                              if(item.getImage_04()!=null){
//                                  String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + item.getImage_04();
////                                  Picasso.get().load(imagePath).into(im4);
//                                  imagePaths.add(imagePath);
//                              }if(item.getImage_05()!=null){
//                                  String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + item.getImage_05();
////                                  Picasso.get().load(imagePath).into(im5);
//                                  imagePaths.add(imagePath);
//                              }
//                              tvOwnerName.setText("Owner Name"+item.getUser_name());
//                              tvBarterWith.setText("Barter With: "+item.getBarter_for());
//                              tvDescription.setText("Description: "+item.getDescription());
//                              tvTitle.setText("Item Name: "+item.getItem_name());
//                              tvValue.setText("Item Value: "+item.getPrice());
//                              txtVerification.setText("Verification Status"+ item.getVerification_status());
//                              Log.e("API Call", String.valueOf(item.getPrice()));
//                              Log.e("API Call", String.valueOf(item.getImage_02()));
//                              // Find the ViewPager
//                              ViewPager viewPager = findViewById(R.id.imagePager);
//
//                              // Create the adapter and set it to the ViewPager
//                              ImageSliderAdapter adapter = new ImageSliderAdapter(getApplicationContext());
//                              viewPager.setAdapter(adapter);
//
//                              // Fetch image paths from the database and set them to the adapter
//                               // Replace this with your method to fetch image paths from the database
//                              adapter.setImagePaths(imagePaths);
//
//                          }
//
//                      }
//
//
//                  }else{
//                      Log.e("API Call", "Response unsucessful ");
//
//                  }
//
//              }
//
//              @Override
//              public void onFailure(Call<List<Items>> call, Throwable t) {
//                  Log.e("API Call", "Response Message: " + t);
//
//              }
//
//          });
//
//      }
private void displayItemDetails(Items item) {
    APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
    String itemid = String.valueOf(item.getItem_id());
    Call<Items> call = apiService.getItemDetails(itemid);
    call.enqueue(new Callback<Items>() {
        @Override
        public void onResponse(Call<Items> call, Response<Items> response) {
            Items itemDetails = response.body();
            if (itemDetails != null) {
                tvOwnerName.setText( itemDetails.getUser_name());
                tvBarterWith.setText("Barter With: " + itemDetails.getBarter_for());
                tvDescription.setText(itemDetails.getDescription());
                tvTitle.setText( itemDetails.getItem_name());
                tvValue.setText(String.valueOf(itemDetails.getPrice()));
                txtVerification.setText("Verification Status: " + itemDetails.getVerification_status());
                tvRating.setText(String.valueOf(itemDetails.getRating()));
                // Remove existing attributes views if any
                attributesContainer.removeAllViews();

                // Inside your activity
                String attributesJson = itemDetails.getAttributesJson();

                // Use Gson to parse JSON string to Map<String, String>
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> attributesMap = gson.fromJson(attributesJson, type);

                // Display attributes
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int colorPrimary = typedValue.data;
                for (Map.Entry<String, String> entry : attributesMap.entrySet()) {
                    LinearLayout keyValueLayout = new LinearLayout(ItemDetailsActivity.this);
                    keyValueLayout.setOrientation(LinearLayout.HORIZONTAL);
                    keyValueLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView keyTextView = new TextView(ItemDetailsActivity.this);
                    keyTextView.setText(entry.getKey() + ": ");
                    keyTextView.setTextSize(20);
                    keyTextView.setTextColor(colorPrimary);
                    keyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView valueTextView = new TextView(ItemDetailsActivity.this);
                    valueTextView.setText(entry.getValue());
                    valueTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    valueTextView.setTextSize(20);

                    keyValueLayout.addView(keyTextView);
                    keyValueLayout.addView(valueTextView);

                    attributesContainer.addView(keyValueLayout);
                }

                ViewPager viewPager = findViewById(R.id.imagePager);
                // Create the adapter and set it to the ViewPager


                // Load images into ViewPager
                ArrayList<String> imagePaths = new ArrayList<>();
                if (itemDetails.getImage_01() != null) {
                    imagePaths.add(RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + itemDetails.getImage_01());
                }
                if (itemDetails.getImage_02() != null) {
                    imagePaths.add(RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + itemDetails.getImage_02());
                }
                if (itemDetails.getImage_03() != null) {
                    imagePaths.add(RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + itemDetails.getImage_03());
                }
                if (itemDetails.getImage_04() != null) {
                    imagePaths.add(RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + itemDetails.getImage_04());
                }
                if (itemDetails.getImage_05() != null) {
                    imagePaths.add(RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + itemDetails.getImage_05());
                }
                ImageSliderAdapter adapter = new ImageSliderAdapter(ItemDetailsActivity.this);
                viewPager.setAdapter(adapter);
                // Set image paths to the adapter
                adapter.setImagePaths(imagePaths);
                DisplayVerificationTick(item);


            } else {
                Log.e("API Call", "Item details response is null");
            }
        }

        @Override
        public void onFailure(Call<Items> call, Throwable t) {
            Log.e("API Call", "Failed to fetch item details: " + t.getMessage());
        }
    });
}
    public void DisplayVerificationTick(Items item) {
        ViewPager viewPager = findViewById(R.id.imagePager);
        ImageSliderAdapter adapter = (ImageSliderAdapter) viewPager.getAdapter();

        if (adapter != null) {
            // Check the verification status
            String verificationStatus = item.getVerification_status();
            Log.d("VerificationStatus", "Status: " + verificationStatus);

            // Update the visibility of the verification tick ImageView in the adapter
            adapter.setShowVerificationTick("Verified".equalsIgnoreCase(verificationStatus));
        }
    }



}