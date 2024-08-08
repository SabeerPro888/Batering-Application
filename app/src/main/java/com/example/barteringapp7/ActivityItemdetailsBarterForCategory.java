package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.barteringapp7.BarterForCategory.Adapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityItemdetailsBarterForCategory extends AppCompatActivity {

    ListView categoriesListView;

    Items item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetails_barter_for_category2);
        categoriesListView = findViewById(R.id.categoriesListView);
            Intent intent=getIntent();
        if (intent != null && intent.hasExtra("item_details")) {
            item = (Items) getIntent().getSerializableExtra("item_details");

            Log.e("Item Id",String.valueOf(item.getItem_id()));
        }

        getCategoriesFromApi();

        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Category");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(ActivityItemdetailsBarterForCategory.this, ActivityItemDetailsBarterForSubCategory.class);
                intent.putExtra("CategoryName", selectedItem);
                GlobalVariables.getInstance().setSubCategory(selectedItem);
                intent.putExtra("item_details", item);

                startActivity(intent);
            }

        });
    }

    private void getCategoriesFromApi() {
        // Implement your API call here to retrieve categories
        // This is a placeholder method, replace it with your actual API call
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Category>> call = apiService.getCategories();

        Log.e("Categories fucntion", "In it ");

       call.enqueue(new Callback<List<Category>>() {
           @Override
           public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
               if (response.isSuccessful()) {
                   List<Category> categories = response.body();
                   // Populate ListView with subcategories
                   if (categories != null) {
                       List<String> catnames = new ArrayList<>(); // Initialize the list
                       for(Category s : categories){
                           catnames.add(s.getCategory_name());
                       }
                       populateListView(catnames);;
                   }
               } else {
                   // Handle API error
               }
           }

           @Override
           public void onFailure(Call<List<Category>> call, Throwable t) {

           }
       });

    }

    private void populateListView(List<String> subcategories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, subcategories);
        categoriesListView.setAdapter(adapter);
    }
}
