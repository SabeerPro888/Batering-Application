package com.example.barteringapp7.ui.ViewRequests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ViewRequestsInformation;
import com.example.barteringapp7.databinding.FragmentViewRequestsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewRequestsFragment extends Fragment {
    private FragmentViewRequestsBinding binding;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewRequestsViewModel viewUploadsViewModel =
                new ViewModelProvider(this).get(ViewRequestsViewModel.class);

        binding = FragmentViewRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getRequests();

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getRequests(){
        String email= GlobalVariables.getInstance().getEmail();
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        View root = binding.getRoot();

        Call<List<ViewRequestsInformation>> call = apiService.Viewrequests(email);

        call.enqueue(new Callback<List<ViewRequestsInformation>>() {
            @Override
            public void onResponse(Call<List<ViewRequestsInformation>> call, Response<List<ViewRequestsInformation>> response) {
                if (response.isSuccessful()) {
                    List<ViewRequestsInformation> Items = (List<ViewRequestsInformation>) response.body();
                    for (ViewRequestsInformation b : Items) {
                        Log.e("API Call", "getSender_name: " + b.getSender_name());
                        Log.e("API Call", "getRequestedItemName: " + b.getRequestedItemName());
                        Log.e("API Call", "getOffer_id: " + b.getOffer_id());



                    }
                    recyclerView = root.findViewById(R.id.RequestsRecyclerView);
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);

                    // Corrected the variable name here
                    ArrayList<ViewRequestsInformation> ItemsArrayList = new ArrayList<>(Items);

                    RecyclerView_Adapter<RecyclerView.ViewHolder> objAdapter = new RecyclerView_Adapter<RecyclerView.ViewHolder>(getContext(), ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ViewRequestsInformation>> call, Throwable t) {

            }
        });


        }
}
