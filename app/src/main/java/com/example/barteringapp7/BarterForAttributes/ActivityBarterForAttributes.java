package com.example.barteringapp7.BarterForAttributes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.BarterForSubCategory.ActivityBarterForSubCategory;
import com.example.barteringapp7.Category;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.SubCategory;
import com.example.barteringapp7.UploadActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBarterForAttributes extends AppCompatActivity {



    RecyclerView recyclerView;

    String subCategory;

    com.example.barteringapp7.BarterForAttributes.Adapter objAdapter;

    ImageButton tick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter_for_attributes);
        tick=findViewById(R.id.imageButtonAttributes);

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityBarterForAttributes.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Items");


        recyclerView = findViewById(R.id.BarterForRecyclerViewAttributes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("subcategory")) {
            subCategory = intent.getStringExtra("subcategory");
            getSubCategoriesFromApi();
        }





    }

    private void getSubCategoriesFromApi() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<String>> call = apiService.GetAttributes(subCategory);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> subCategories = response.body();
                if (subCategories != null) {
                    ArrayList<String> subCategoryArrayList = new ArrayList<>(subCategories);
                    objAdapter = new Adapter(ActivityBarterForAttributes.this, subCategoryArrayList);
                    recyclerView.setAdapter(objAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // Handle failure
            }
        });
    }

}