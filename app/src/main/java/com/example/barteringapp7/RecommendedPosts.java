package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_posts);

        cardView = findViewById(R.id.recommendedCardView);
        getRecommendedPosts();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Recommended Items");
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void getRecommendedPosts(){

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email=GlobalVariables.getInstance().getEmail();
        Call<List<Items>> call = apiService.getRecommendedPosts(email);
        call.enqueue(new Callback<List<Items>>() {

            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> Items = (List<Items>) response.body();
                  if(!Items.isEmpty()){


                      Log.d("Items Size", "Items size: " + Items.size());

                      for(Items b:Items){
                          Log.e("API Call", "Response Message: " + b.getItem_name());
                          Log.e("API Call", "image name Message: " + b.getImage_01());
                          Log.e("API Call", "Price " + b.getPrice());
                      }
                      recyclerView = findViewById(R.id.RecyclerView);
                      layoutManager = new LinearLayoutManager(RecommendedPosts.this);
                      recyclerView.setLayoutManager(layoutManager);

                      // Corrected the variable name here
                      ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                      ViewItemsAdapter objAdapter = new ViewItemsAdapter(RecommendedPosts.this, ItemsArrayList);
                      recyclerView.addItemDecoration(new DividerItemDecoration(RecommendedPosts.this, LinearLayoutManager.VERTICAL));
                      recyclerView.setAdapter(objAdapter);
                      cardView.setVisibility(View.VISIBLE);





                  }else{
                    cardView.setVisibility(View.GONE);
                      Log.e("API Call", "No recommended posts found");

                      // Create a TextView dynamically
                      TextView textView = new TextView(RecommendedPosts.this);
                      textView.setText("There are no Items currently that match your Uploaded Items.");

                      // Add any desired properties to the TextView
                      // For example, text color, text size, layout parameters, etc.

                      // Add the TextView to your layout
                      LinearLayout constraintLayout = findViewById(R.id.innerlayout);
                      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                              ConstraintLayout.LayoutParams.WRAP_CONTENT,
                              ConstraintLayout.LayoutParams.WRAP_CONTENT
                      );
                      textView.setLayoutParams(layoutParams);
                      constraintLayout.addView(textView);
                      Log.d("Items Size", "Items size: " + Items.size());



                  }

                }

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }

        });

    }
}