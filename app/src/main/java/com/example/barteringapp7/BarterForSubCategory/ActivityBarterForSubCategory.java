package com.example.barteringapp7.BarterForSubCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.BarterForAttributes.ActivityBarterForAttributes;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.SubCategory;
import com.example.barteringapp7.UploadActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBarterForSubCategory extends AppCompatActivity {
    RecyclerView recyclerView;
    String categoryName;
    Adapter objAdapter;
    ImageButton tick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter_for_sub_category);

//
//        tick=findViewById(R.id.imageButtonSubCategory);
//
//        tick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ActivityBarterForSubCategory.this, UploadActivity.class);
//                startActivity(intent);
//            }
//        });



        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Subcategory");

        recyclerView = findViewById(R.id.BarterForRecyclerViewSubCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category")) {
            categoryName = intent.getStringExtra("category");
            getSubCategoriesFromApi(categoryName);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    private void getSubCategoriesFromApi(String categoryName) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<SubCategory>> call = apiService.getSubCategories(categoryName);

        call.enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                List<SubCategory> subCategories = response.body();
                if (subCategories != null) {
                    ArrayList<SubCategory> subCategoryArrayList = new ArrayList<>(subCategories);
                    objAdapter = new Adapter(ActivityBarterForSubCategory.this, subCategoryArrayList);
                    recyclerView.setAdapter(objAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
