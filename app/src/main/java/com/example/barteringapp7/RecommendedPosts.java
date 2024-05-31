package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedPosts extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CardView cardView;
    ImageView noRecommendedItemimage;
    TextView NoRecommendedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_posts);

        cardView = findViewById(R.id.recommendedCardView);
        getRecommendedPosts();

        noRecommendedItemimage=findViewById(R.id.imgNoRecommendedItems);
        NoRecommendedItem=findViewById(R.id.txtNoRecmmendedItems);
        recyclerView = findViewById(R.id.RecyclerView);





        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RecommendedPosts.this,NavigationActivity.class);
                startActivity(intent);


            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Recommended Items");
    }



//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//



    public void getRecommendedPosts() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email = GlobalVariables.getInstance().getEmail();
        Call<List<Items>> call = apiService.GetRecommendationLastUploaded(email);
        call.enqueue(new Callback<List<Items>>() {

            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> Items = response.body();
                    if (Items.size()!=0) {
                        Log.d("Items Size", "Items size: " + Items.size());

                        for (Items b : Items) {
                            Log.e("API Call", "Response Message: " + b.getItem_name());
                            Log.e("API Call", "Image Name: " + b.getImage_01());
                            Log.e("API Call", "Price: " + b.getPrice());
                        }
                        layoutManager = new LinearLayoutManager(RecommendedPosts.this);
                        recyclerView.setLayoutManager(layoutManager);
                        ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);
                        ViewItemsAdapter objAdapter = new ViewItemsAdapter(RecommendedPosts.this, ItemsArrayList);
                        recyclerView.addItemDecoration(new DividerItemDecoration(RecommendedPosts.this, LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(objAdapter);

                        noRecommendedItemimage.setVisibility(View.GONE);
                        NoRecommendedItem.setVisibility(View.GONE);


                    } else {

                        cardView.setVisibility(View.GONE);


                        noRecommendedItemimage.setVisibility(View.VISIBLE);
                        NoRecommendedItem.setVisibility(View.VISIBLE);


                    }

                }else{
                    Log.e("API Call", "Response not Successful");
                    noRecommendedItemimage.setVisibility(View.VISIBLE);
                    NoRecommendedItem.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);



                }

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                Log.e("API Call", "Error fetching recommended posts", t);
            }
        });
    }

}