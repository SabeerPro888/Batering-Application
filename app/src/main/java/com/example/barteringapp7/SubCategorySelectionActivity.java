package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class SubCategorySelectionActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_selection);

        listView=findViewById(R.id.listView);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("categoryName")) {
            String categoryName = intent.getStringExtra("categoryName");
            fetchSubcategories(categoryName);

            // Find the TextView in your layout and set the categoryName to it


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    Intent intent = new Intent(SubCategorySelectionActivity.this, UploadActivity.class);
                    intent.putExtra("subcategoryName", selectedItem);
                    intent.putExtra("CategoryName", categoryName);
                    GlobalVariables.getInstance().setSubCategory(selectedItem);

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

// Set the title based on the activity
            toolbarTitle.setText("Subcategory");
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Call the superclass implementation for default behavior (going back to the previous activity)
        // Add any additional functionality here if needed
    }
    private void populateListView(List<String> subcategories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, subcategories);
        listView.setAdapter(adapter);
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
                        for(SubCategory s : subcategories){
                            subcatnames.add(s.getSubcategory_name());
                        }
                        populateListView(subcatnames);;
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

}