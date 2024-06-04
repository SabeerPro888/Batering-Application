package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_MakeBarterOffer extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    int ItemId;
    int SenderId;

    TextView Price;
    RecyclerView_MakeBarterOfferAdapter objAdapter;

    EditText descriptionBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_barter_offer);

        descriptionBox=findViewById(R.id.descriptionBox);

        Button btnMakeBarterOffer = findViewById(R.id.btnSendOffer);
        Items item = (Items) getIntent().getSerializableExtra("item_details");
        if (item != null) {
            ItemId = item.getItem_id();
        } else {
            Log.e("Activity_MakeBarterOffer", "item_details is null");
        }

        Price = findViewById(R.id.txtMoney);


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Make Offer");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        populateRecyclerView();

        btnMakeBarterOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objAdapter != null) {
                    MakeOffer();
                    Intent intent=new Intent(Activity_MakeBarterOffer.this,NavigationActivity.class);

                    startActivity(intent);

                } else {
                    Toast.makeText(Activity_MakeBarterOffer.this, "Please wait until the items are loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void populateRecyclerView() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Items>> call = apiService.ViewUploads(GlobalVariables.getInstance().getEmail().toString());
        call.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> itemslist1 = response.body();
                    List<Items> itemslist = new ArrayList<>();
                    for(Items i:itemslist1){
                        if("No".equals(i.getIsSold())){
                            itemslist.add(i);
                        }
                    }


                    recyclerView = findViewById(R.id.yourItemsRecyclerView);
                    layoutManager = new LinearLayoutManager(Activity_MakeBarterOffer.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ArrayList<Items> ItemsArrayList = new ArrayList<>(itemslist);
                    objAdapter = new RecyclerView_MakeBarterOfferAdapter(Activity_MakeBarterOffer.this, ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(Activity_MakeBarterOffer.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);

                    Log.e("Uploaded Items", "Successful Response");
                } else {
                    Log.e("Uploaded Items", GlobalVariables.getInstance().getEmail().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                Log.e("Uploaded Items Failure", t.toString());
            }
        });
    }

    public void MakeOffer() {
        if (objAdapter == null) {
            Toast.makeText(this, "Please wait until the items are loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> selectedItemsIds = objAdapter.getSelectedItemsIds();
        if (selectedItemsIds.isEmpty()) {
            Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(Price.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the sender ID asynchronously
        getSenderIdAndSendOffer(selectedItemsIds, ItemId, price);
    }

    private void getSenderIdAndSendOffer(List<Integer> selectedItemsIds, int itemId, int price) {
        String email = GlobalVariables.getInstance().getEmail().toString();
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.getUserId(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int senderId = Integer.parseInt(response.body());
                    // Now that you have the sender ID, make the SendOffer call
                    sendOffer(selectedItemsIds, senderId, itemId, price);
                } else {
                    Log.e("GetUserId", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("GetUserId", "Failed to get sender ID", t);
            }
        });
    }

    private void sendOffer(List<Integer> selectedItemsIds, int senderId, int itemId, int price) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        int[] selectedItemsArray = new int[selectedItemsIds.size()];
        for (int i = 0; i < selectedItemsIds.size(); i++) {
            selectedItemsArray[i] = selectedItemsIds.get(i);
        }
        JSONArray jsonArray = new JSONArray();
        for (int i : selectedItemsArray) {
            jsonArray.put(i);
        }
        String jsonStr = jsonArray.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);

        String RequestDescription=descriptionBox.getText().toString();
        Call<String> call = apiService.SendOffer(requestBody, senderId, itemId, price,RequestDescription);

        for(int i:selectedItemsIds){
            Log.e("Select Item ids",String.valueOf(i));
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_MakeBarterOffer.this, "Offer Sent", Toast.LENGTH_SHORT).show();
                    Log.e("Response Message",response.body().toString());
                } else {
                    Toast.makeText(Activity_MakeBarterOffer.this, "Failed to send offer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("SendOffer", t.toString());

            }
        });
    }
}
