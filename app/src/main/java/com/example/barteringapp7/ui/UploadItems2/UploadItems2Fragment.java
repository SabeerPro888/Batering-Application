package com.example.barteringapp7.ui.UploadItems2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.Category;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.databinding.FragmentUploadItems2Binding;
import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadItems2Fragment extends Fragment {

    FragmentUploadItems2Binding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUploadItems2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getCategoriesFromApi();

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getCategoriesFromApi() {
        // Implement your API call here to retrieve categories
        // This is a placeholder method, replace it with your actual API call
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if(response.isSuccessful()){
                    if (categories != null) {
                        recyclerView = binding.categoriesRecyclerView;

                        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

                        // Set the layout manager to the RecyclerView
                        recyclerView.setLayoutManager(layoutManager);


                        ArrayList<Category> categoryArrayList = new ArrayList<>(categories);

                        for(Category s:categories){
                            Log.e("Category",s.getCategory_name());
                        }
                        Log.e("On Response categories","sucessfull ");


                        UploadItem2Adapter objAdapter = new UploadItem2Adapter(getContext(), categoryArrayList);
                        recyclerView.setAdapter(objAdapter);
                }

                }else{
                    Log.e("On Response categories","No sucessfull ");

                }


            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            Log.e("On Failure Category",t.toString());
            }
        });

    }



}
