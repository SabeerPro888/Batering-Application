package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barteringapp7.BarterForAttributes.ActivityBarterForAttributes;
import com.example.barteringapp7.BarterForAttributes.Adapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsBarterAttributesSelection extends AppCompatActivity {
    ImageButton tick;
    RecyclerView recyclerView;

    String subCategory;

    Items item;

    AdapterItemDetailsBarterForAttributes objAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_barter_attributes_selection);

        tick=findViewById(R.id.imageButtonAttributes);

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDatabase();

            }
        });

        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        recyclerView = findViewById(R.id.BarterForRecyclerViewAttributes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("subcategoryName")) {
            subCategory = intent.getStringExtra("subcategoryName");
            item = (Items) getIntent().getSerializableExtra("item_details");

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

                    for(String s : subCategoryArrayList){
                        Log.e("Attribute check",s);
                    }
                    objAdapter = new AdapterItemDetailsBarterForAttributes(ItemDetailsBarterAttributesSelection.this, subCategoryArrayList);
                    recyclerView.setAdapter(objAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void UpdateDatabase(){
        Set<String> selectedItems = SelectedItemsManager.getInstance().getSelectedItems();
        List<String> BarterForItems = new ArrayList<>(selectedItems);


        String json = new Gson().toJson(BarterForItems);
        RequestBody barterForItemsJsonBody = RequestBody.create(MediaType.parse("application/json"), json);
        RequestBody ItemId1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(item.getItem_id()));



        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Void> call=apiService.updateWishList(barterForItemsJsonBody,ItemId1);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Intent intent=new Intent(ItemDetailsBarterAttributesSelection.this,NavigationActivity.class);
                    Toast.makeText(ItemDetailsBarterAttributesSelection.this,"Updated WishList", Toast.LENGTH_SHORT);
                    startActivity(intent);
                }else{
                    Log.e("Api check","Response not successful");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });


    }
}