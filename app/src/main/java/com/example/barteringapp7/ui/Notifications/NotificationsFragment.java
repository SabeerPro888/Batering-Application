package com.example.barteringapp7.ui.Notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.databinding.FragmentNotificationsBinding;
import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                    recyclerView = root.findViewById(R.id.NotificationsRecyclerView);
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);

                    // Corrected the variable name here
                    ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                    NotificationsAdapter objAdapter = new NotificationsAdapter(getContext(), ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);

                }


            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }

        });








        return root;
    }
}
