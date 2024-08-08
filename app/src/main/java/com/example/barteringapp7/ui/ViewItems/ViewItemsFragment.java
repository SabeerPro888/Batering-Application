package com.example.barteringapp7.ui.ViewItems;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.AdvancedSearchActivity;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.databinding.FragmentViewItemsBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewItemsFragment extends Fragment {
    private FragmentViewItemsBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ViewItemsAdapter objAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentViewItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ShapeableImageView imgAdvancedSearch = root.findViewById(R.id.imgAdvancedSearch);
        imgAdvancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SearchActivity
                Intent intent = new Intent(getActivity(), AdvancedSearchActivity.class);
                startActivity(intent);
            }
        });









        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email= GlobalVariables.getInstance().getEmail();

        Call<List<Items>> call = apiService.vewAllItems(email);
        call.enqueue(new Callback<List<Items>>() {

            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> Items = (List<Items>) response.body();
                    Log.e("Raw JSON", new Gson().toJson(response.body()));

                    for(Items b:Items){
                        Log.e("API Call", "Response Message: " + b.getItem_name());
                        Log.e("API Call", "image name Message: " + b.getImage_01());
                        Log.e("API Call", "Price " + b.getPrice());
                        Log.e("API Call", "Price " + b.getRating());
                        Log.e("API CALL","User ID"+b.getUserId());

                    }
                    recyclerView = root.findViewById(R.id.RecyclerView);
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);

                    // Corrected the variable name here
                    ArrayList<Items> ItemsArrayList = new ArrayList<>(Items);

                     objAdapter = new ViewItemsAdapter(getContext(), ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);

                    TextView emptyView = root.findViewById(R.id.emptyView);
                    objAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            if (objAdapter.getItemCount() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                emptyView.setVisibility(View.GONE);
                            }
                        }
                    });


                }


            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }

        });


        EditText searchEditText = root.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();

                    objAdapter.getFilter().filter(searchText);


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
