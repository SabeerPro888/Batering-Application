package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barteringapp7.ui.ViewItems.ViewItemsFragment;

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
    Button AcceptButton,RejectButton;

    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        ViewRequestsInformation item = (ViewRequestsInformation) getIntent().getSerializableExtra("Offer_Details");
        if(item!=null){
            Log.e("Offer Id",String.valueOf(item.getOffer_id()));
        }else{
            Log.e("Activity Offer Details","getintent is null");
        }

        senderName=findViewById(R.id.txtRequestSenderName);
        price=findViewById(R.id.textView9);
        Description=findViewById(R.id.OfferdescriptionBox);
        Description.setText(item.getRequestInformation());
        senderName.setText(item.getSender_name().toString());
        price.setText(String.valueOf(item.getPrice()));
        AcceptButton=findViewById(R.id.btnAcceptOffer);
        RejectButton=findViewById(R.id.btnRejectOffer);
        rating=findViewById(R.id.ratingBarOfferDetails);
        Double ratingDouble=item.getRating();
        float ratingFloat = ratingDouble != null ? ratingDouble.floatValue() : 0.0f; // Convert to float, with a default value if null

        rating.setRating(ratingFloat);


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
                AcceptOrRejectOffer(1,item.getOffer_id());
            }
        });

        RejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0 means rejected
                AcceptOrRejectOffer(0,item.getOffer_id());
            }
        });







    }

    public void AcceptOrRejectOffer(int status,int Offerid){
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Void> call = apiService.AcceptOrRejectOffer(status,Offerid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                  if(status==1){
//                      Toast.makeText(Activity_Offer_Details.this, "Accepted The Offer", Toast.LENGTH_SHORT).show();
//                      Intent intent=new Intent(Activity_Offer_Details.this,ViewItemsFragment.class);
//                      startActivity(intent);

                      Toast.makeText(Activity_Offer_Details.this, "Accepted The Offer", Toast.LENGTH_SHORT).show();
                      Intent intent=new Intent(Activity_Offer_Details.this,NavigationActivity.class);
                      startActivity(intent);




                  }else{
                      Toast.makeText(Activity_Offer_Details.this, "Rejected Offer", Toast.LENGTH_SHORT).show();

                  }
                }
                else{
                    Log.e("Response Check","Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("On Failure","On Failure");

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
                    for(Items s:ItemsArrayList){
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
}