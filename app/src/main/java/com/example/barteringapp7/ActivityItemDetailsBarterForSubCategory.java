package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityItemDetailsBarterForSubCategory extends AppCompatActivity {


    ListView subcategoriesListView;

    Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_barter_for_sub_category);


        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Subcategory");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        subcategoriesListView = findViewById(R.id.subcategoriesListView);
        Intent intent = getIntent();



        if (intent != null && intent.hasExtra("CategoryName") && intent.hasExtra("item_details")) {
            String categoryName = intent.getStringExtra("CategoryName");
            item = (Items) getIntent().getSerializableExtra("item_details");

            Log.e("Item Id",String.valueOf(item.getItem_id()));

            fetchSubcategories(categoryName);

            // Find the TextView in your layout and set the categoryName to it


            subcategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    Intent intent = new Intent(ActivityItemDetailsBarterForSubCategory.this, ItemDetailsBarterAttributesSelection.class);
                    intent.putExtra("subcategoryName", selectedItem);
                    intent.putExtra("item_details", item);


                    GlobalVariables.getInstance().setSubCategory(selectedItem);

                    startActivity(intent);
                }
            });

        }

    }

    private void fetchSubcategories(String categoryName) {
        // Make an API call to fetch subcategories based on the selected category
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<SubCategory>> call = apiService.getSubCategories(categoryName);

        call.enqueue(new Callback<List<SubCategory>>() {

            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                if (response.isSuccessful()) {
                    List<SubCategory> subcategories = response.body();
                    // Populate ListView with subcategories
                    if (subcategories != null) {
                        List<String> subcatnames = new ArrayList<>(); // Initialize the list
                        for (SubCategory s : subcategories) {
                            subcatnames.add(s.getSubcategory_name());
                        }
                        populateListView(subcatnames);
                        ;
                    }
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                // Handle network error
            }
        });
    }

    private void populateListView(List<String> subcategories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, subcategories);
        subcategoriesListView.setAdapter(adapter);
    }
}