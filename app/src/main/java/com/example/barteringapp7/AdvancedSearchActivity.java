package com.example.barteringapp7;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barteringapp7.AdvanceSearchResult.AdvanceSearchResult;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancedSearchActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerSubcategory;
    private RatingBar ratingBar;
    private EditText etMinPrice, etMaxPrice;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        // Initialize the views
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubcategory = findViewById(R.id.spinnerSubcategory);
        ratingBar = findViewById(R.id.ratingBar);
        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);
        btnSearch = findViewById(R.id.btnAdavcneSearch);



        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Filter");

        // Fetch data from the database
        fetchCategoriesFromDatabase();
        Log.e("Activity Check","In search activity");


        // Set the search button click listener
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void fetchCategoriesFromDatabase() {
        // This is a placeholder method, replace it with your actual API call
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if (categories != null) {
                    List<String> categoryNames = new ArrayList<>();
                    categoryNames.add("All"); // Add the "All" placeholder
                    for (Category category : categories) {
                        categoryNames.add(category.getCategory_name());
                    }

                    // Populate the category spinner
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(AdvancedSearchActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(categoryAdapter);

                    // Set an empty adapter for subcategory spinner until categories are selected
                    ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(AdvancedSearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                    subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategory.setAdapter(subcategoryAdapter);

                    // Set category selection listener to update subcategories
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedCategory = categoryNames.get(position);
                            if (!selectedCategory.equals("All")) {
                                fetchSubcategoriesFromDatabase(selectedCategory);
                            } else {
                                // Clear subcategory spinner if "All" is selected
                                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(AdvancedSearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                                emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubcategory.setAdapter(emptyAdapter);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Handle the error
                Toast.makeText(AdvancedSearchActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSubcategoriesFromDatabase(String category) {
        // This is a placeholder method, replace it with your actual API call
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<SubCategory>> call = apiService.getSubCategories(category);

        call.enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                List<SubCategory> subcategories = response.body();
                if (subcategories != null) {
                    List<String> subcategoryNames = new ArrayList<>();
                    subcategoryNames.add("All"); // Add the "All" placeholder
                    for (SubCategory subcategory : subcategories) {
                        subcategoryNames.add(subcategory.getSubcategory_name());
                    }

                    // Populate the subcategory spinner
                    ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(AdvancedSearchActivity.this, android.R.layout.simple_spinner_item, subcategoryNames);
                    subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategory.setAdapter(subcategoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                // Handle the error
                Toast.makeText(AdvancedSearchActivity.this, "Failed to fetch subcategories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch() {
        String subcategory;
        String category = spinnerCategory.getSelectedItem().toString();
        if(category.equals("All")){
            subcategory="All";
        }else{
             subcategory = spinnerSubcategory.getSelectedItem().toString();
        }
        float rating = ratingBar.getRating();
        String minPrice = etMinPrice.getText().toString();
        String maxPrice = etMaxPrice.getText().toString();
        Log.e("Rating",String.valueOf(rating));

        // Validate inputs
        if (minPrice.isEmpty()) {
            minPrice = "0";
        }
        if (maxPrice.isEmpty()) {
            maxPrice = String.valueOf(Float.MAX_VALUE);
        }
        float minPriceValue = Float.parseFloat(minPrice);
        float maxPriceValue = Float.parseFloat(maxPrice);

        if (minPriceValue > maxPriceValue) {
            Toast.makeText(this, "Minimum price cannot be greater than maximum price", Toast.LENGTH_SHORT).show();
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        String email = GlobalVariables.getInstance().getEmail();

        if(email!=null){
            Log.e("Email",email);
        }
        if(category!=null){
            Log.e("category",category);
        }
        if(subcategory!=null){
            Log.e("subcategory",subcategory);
        }


        Call<List<Items>> call = apiService.advanceSearch(category, subcategory, rating, minPrice, maxPrice, email);

        call.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> items = response.body();
                    // Handle your items list here
                    for (Items item : items) {
                        Log.e("AdvancedSearchActivity", "Item: " + item.getItem_name() + ", Price: " + item.getPrice());
                        // Process each item as needed
                    }
                    GlobalVariables.getInstance().setItemsList(items);
                    Intent intent = new Intent(AdvancedSearchActivity.this, AdvanceSearchResult.class);
                    startActivity(intent);

                } else {
                    Log.e("AdvancedSearchActivity", "Failed to retrieve items. Error: " + response.message());
                    Toast.makeText(AdvancedSearchActivity.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                Log.e("AdvancedSearchActivity", "Error fetching items: " + t.getMessage());
                Toast.makeText(AdvancedSearchActivity.this, "Error fetching items", Toast.LENGTH_SHORT).show();
            }
        });

        // Log search parameters for debugging purposes
        Log.d("AdvancedSearchActivity", "Searching with Category: " + category + ", Subcategory: " + subcategory + ", Rating: " + rating + ", Price: " + minPrice + " - " + maxPrice);
    }

}
