package com.example.barteringapp7;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrustedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted);

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
                    recyclerView = findViewById(R.id.TrustedRecyclerView);
                    layoutManager = new LinearLayoutManager(TrustedActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    // Corrected the variable name here
                    ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                    ViewItemsAdapter objAdapter = new ViewItemsAdapter(TrustedActivity.this, ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(TrustedActivity.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);


                }

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }
        });

    }
}