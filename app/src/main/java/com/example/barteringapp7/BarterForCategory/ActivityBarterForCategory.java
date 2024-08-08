package com.example.barteringapp7.BarterForCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.BarterForAttributes.ActivityBarterForAttributes;
import com.example.barteringapp7.Category;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.UploadActivity;
import com.example.barteringapp7.ui.UploadItems2.UploadItem2Adapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBarterForCategory extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageButton tick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter_for_category);
        getCategoriesFromApi();
//
//        tick=findViewById(R.id.imageButtonCategory);
//
//        tick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ActivityBarterForCategory.this, UploadActivity.class);
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
        toolbarTitle.setText("Choose Category");
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    private void getCategoriesFromApi() {
        // Implement your API call here to retrieve categories
        // This is a placeholder method, replace it with your actual API call
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if (categories != null) {
                    recyclerView = findViewById(R.id.BarterForRecyclerViewCategory);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

                    // Set the layout manager to the RecyclerView
                    recyclerView.setLayoutManager(layoutManager);


                    ArrayList<Category> categoryArrayList = new ArrayList<>(categories);

                    Adapter objAdapter = new Adapter(getApplicationContext(), categoryArrayList);
                    recyclerView.setAdapter(objAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });

    }
}