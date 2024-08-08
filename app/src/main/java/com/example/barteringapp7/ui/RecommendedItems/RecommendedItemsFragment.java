package com.example.barteringapp7.ui.RecommendedItems;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ViewRequestsInformation;
import com.example.barteringapp7.databinding.FragmentHistoryBinding;
import com.example.barteringapp7.databinding.FragmentRecommendedItemsBinding;
import com.example.barteringapp7.ui.History.HistoryAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedItemsFragment extends Fragment {

    private FragmentRecommendedItemsBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecommendedItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String email= GlobalVariables.getInstance().getEmail();
        getRecommendedItems(email);


        return root;
    }

    public void getRecommendedItems(String email){
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        View root = binding.getRoot();

        Call<List<Items>> call = apiService.getRecommendedPosts(email);

        call.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                List<Items> Items = null;
                if (response.isSuccessful()) {
                    Log.e("Raw JSON", new Gson().toJson(response.body()));

                    Items = (List<Items>) response.body();
                    for (Items b : Items) {
                        Log.e("API Call", "getSender_name: " + b.getItem_name());
//                        for(String s:b.getBarterForList()){
//                            Log.e("BarterForItems",s);
//                        }


                    }
                    Log.e("API Call", "emptyList");

                    recyclerView = root.findViewById(R.id.RecommendedPostsRecyclerView);
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);


                    // Corrected the variable name here
                    ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                    Adapter objAdapter = new Adapter(getContext(),  ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);


                }else{
                    Log.e("API Response","Unsuccessful");
                }



                // Corrected the variable name here


            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }
        });
    }
}