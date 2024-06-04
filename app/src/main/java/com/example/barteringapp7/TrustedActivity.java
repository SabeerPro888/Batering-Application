package com.example.barteringapp7;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrustedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView noVerificationRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        noVerificationRequests=findViewById(R.id.noVerificationRequests);


        toolbarTitle.setText("Verification Requests");
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);


        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<List<Items>> call = apiService.viewverificationRequests();
        call.enqueue(new Callback<List<Items>>() {

            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> Items = (List<Items>) response.body();
                    for (Items b : Items) {
                        Log.e("API Call", "Response Message: " + b.getItem_name());
                        Log.e("API Call", "image name Message: " + b.getImage_01());
                        Log.e("API Call", "Price " + b.getPrice());
                        Log.e("API Call", "Price " + b.getVerification_id());


                    }

                    if(Items.size()!=0){
                        recyclerView = findViewById(R.id.TrustedRecyclerView);
                        layoutManager = new LinearLayoutManager(TrustedActivity.this);
                        recyclerView.setLayoutManager(layoutManager);

                        // Corrected the variable name here
                        ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                        ViewItemsAdapter objAdapter = new ViewItemsAdapter(TrustedActivity.this, ItemsArrayList);
                        recyclerView.addItemDecoration(new DividerItemDecoration(TrustedActivity.this, LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(objAdapter);
                        noVerificationRequests.setVisibility(View.GONE);
                    }else{
                        noVerificationRequests.setVisibility(View.VISIBLE);

                    }



                }

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }
        });

    }

    public void onLogoutClick(View view) {
        // Clear any user session data here, if applicable
        // For example, clear shared preferences or any cached user data

        // Navigate to the login screen
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}