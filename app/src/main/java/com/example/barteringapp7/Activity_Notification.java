package com.example.barteringapp7;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.barteringapp7.databinding.FragmentNotificationsBinding;
import com.example.barteringapp7.ui.Notifications.NotificationsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Notification extends AppCompatActivity {

    private Activity_Notification binding;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);

// Set the title based on the activity
        toolbarTitle.setText("Notifications");
        getVerificationNotifications();

    }


    public void getVerificationNotifications(){

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email= GlobalVariables.getInstance().getEmail();
        Call<List<Items>> call = apiService.getNotifications(email);
        call.enqueue(new Callback<List<Items>>() {

            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> Items = (List<Items>) response.body();
                    for(Items b:Items){
                        Log.e("API Call", "Response Message: " + b.getItem_name());
                        Log.e("API Call", "image name Message: " + b.getImage_01());
                        Log.e("API Call", "Price " + b.getPrice());
                    }
                    recyclerView = findViewById(R.id.NotificationsRecyclerView);
                    layoutManager = new LinearLayoutManager(Activity_Notification.this);
                    recyclerView.setLayoutManager(layoutManager);

                    // Corrected the variable name here
                    ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                    NotificationsAdapter objAdapter = new NotificationsAdapter(Activity_Notification.this, ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(Activity_Notification.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }

        });

    }
}