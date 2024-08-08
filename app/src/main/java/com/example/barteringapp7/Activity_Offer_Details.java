package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barteringapp7.ui.ViewItems.ViewItemsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Offer_Details extends AppCompatActivity {

    TextView senderName;
    TextView price;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView_OfferedItems_Adapter objAdapter;

    TextView Description;
    Button AcceptButton, RejectButton;

    RatingBar rating;
   int receiverId;
   int senderId;

   ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        ViewRequestsInformation item = (ViewRequestsInformation) getIntent().getSerializableExtra("Offer_Details");
        if (item != null) {
            Log.e("Offer Id", String.valueOf(item.getOffer_id()));
        } else {
            Log.e("Activity Offer Details", "getintent is null");
        }
        GetSenderId(item.Offer_id, new OnSenderIdReceivedListener() {
            @Override
            public void onSenderIdReceived(int Id) {
                // Handle the received senderId here
                senderId=Id;
            }
        });



        GetReceiverId(item.Offer_id, new OnReceiverIdReceivedListener() {
            @Override
            public void onReceiverIdReceived(int id) {
                // Handle the received receiverId here
                receiverId=id;
            }
        });

        senderName = findViewById(R.id.txtRequestSenderName);
        price = findViewById(R.id.textView9);
        Description = findViewById(R.id.OfferdescriptionBox);
        Description.setText(item.getRequestInformation());
        senderName.setText(item.getSender_name().toString());
        price.setText(String.valueOf(item.getPrice()));
        AcceptButton = findViewById(R.id.btnAcceptOffer);
        RejectButton = findViewById(R.id.btnRejectOffer);
        rating = findViewById(R.id.ratingBarOfferDetails);
        profilepic=findViewById(R.id.imageView7);
        Double ratingDouble = item.getRating();
        float ratingFloat = ratingDouble != null ? ratingDouble.floatValue() : 0.0f; // Convert to float, with a default value if null

        rating.setRating(ratingFloat);

        if(item.getSenderProfilePic()!=null){
            String ProfilePic = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + item.getSenderProfilePic();
            Picasso.get().load(ProfilePic).into(profilepic);

        }




        populateRecyclerView(item.getOffer_id());
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Offer Details");
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1 means Accepted
                AcceptOrRejectOffer(1, item.getOffer_id());

            }
        });

        RejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0 means rejected
                AcceptOrRejectOffer(0, item.getOffer_id());

            }
        });


    }

    private void showRatingDialog(int senderId) {
        Dialog dialog = new Dialog(Activity_Offer_Details.this);
        dialog.setContentView(R.layout.dialog_rating);

        RatingBar ratingBar = dialog.findViewById(R.id.dialog_rating_bar);
        Button submitButton = dialog.findViewById(R.id.submit_rating_button);
        TextView txtSenderName = dialog.findViewById(R.id.dialog_title);
        txtSenderName.setText("Rate " + senderName.getText().toString());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue = ratingBar.getRating();
                submitRating(senderId, ratingValue);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void submitRating(int senderid, float ratingValue) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Void> call = apiService.GiveRating(receiverId,senderid, ratingValue);
        Log.e("Receiver Id",String.valueOf(receiverId));
        Log.e("Sender Id",String.valueOf(senderid));
        Log.e("Rating Value",String.valueOf(ratingValue));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(Activity_Offer_Details.this,"Thank you for rating",Toast.LENGTH_SHORT);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Failure","Api went to On Failure");
            }
        });



    }


    public void AcceptOrRejectOffer(int status, int Offerid) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Void> call = apiService.AcceptOrRejectOffer(status, Offerid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (status == 1) {
//                      Toast.makeText(Activity_Offer_Details.this, "Accepted The Offer", Toast.LENGTH_SHORT).show();
//                      Intent intent=new Intent(Activity_Offer_Details.this,ViewItemsFragment.class);
//                      startActivity(intent);


                        Toast.makeText(Activity_Offer_Details.this, "Accepted The Offer", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Offer_Details.this, NavigationActivity.class);
                        startActivity(intent);



                    } else {
                        Toast.makeText(Activity_Offer_Details.this, "Rejected Offer", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Activity_Offer_Details.this, NavigationActivity.class);
                        startActivity(intent);

                    }
                } else {
                    Log.e("Response Check", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("On Failure", "On Failure");

            }
        });


    }

    public void populateRecyclerView(int offerid) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Items>> call = apiService.getAllItemsForOfferId(offerid);
        call.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> itemslist = response.body();
                    recyclerView = findViewById(R.id.RequestsRecyclerView);
                    layoutManager = new LinearLayoutManager(Activity_Offer_Details.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ArrayList<Items> ItemsArrayList = new ArrayList<>(itemslist);
                    objAdapter = new RecyclerView_OfferedItems_Adapter(Activity_Offer_Details.this, ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(Activity_Offer_Details.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);

                    Log.e("Offered Items", "Successful Response");
                    for (Items s : ItemsArrayList) {
                        Log.e("Offered Items", s.getItem_name());

                    }
                } else {
                    Log.e("Offer Id", String.valueOf(offerid));
                }
            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                Log.e("Offer Items Details", t.toString());
            }
        });
    }

    public void GetSenderId(int OfferId, final OnSenderIdReceivedListener listener) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Integer> call = apiService.GetSenderId(OfferId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int senderId = response.body();
                    // Pass the received senderId to the listener
                    listener.onSenderIdReceived(senderId);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Handle failure if needed
            }
        });
    }

    // Define an interface for callback
    interface OnSenderIdReceivedListener {
        void onSenderIdReceived(int senderId);
    }

    public void GetReceiverId(int OfferId, final OnReceiverIdReceivedListener listener) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Integer> call = apiService.GetReceiverId(OfferId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int receiverId = response.body();
                    // Pass the received receiverId to the listener
                    listener.onReceiverIdReceived(receiverId);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Handle failure if needed
            }
        });
    }

    // Define an interface for callback
    interface OnReceiverIdReceivedListener {
        void onReceiverIdReceived(int receiverId);
    }



}