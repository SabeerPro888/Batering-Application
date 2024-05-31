package com.example.barteringapp7.ui.ViewUploads;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.databinding.FragmentViewItemsBinding;
import com.example.barteringapp7.databinding.FragmentViewUploadsBinding;
import com.example.barteringapp7.ui.ViewItems.ViewItemsAdapter;
import com.example.barteringapp7.ui.ViewItems.ViewItemsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUploadsFragment extends Fragment {
    private FragmentViewUploadsBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentViewUploadsBinding.inflate(inflater, container, false);
         root = binding.getRoot();
         String email=GlobalVariables.getInstance().getEmail();
         getUploadedItems(email);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getUploadedItems(String email) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Items>> call = apiService.ViewUploads(email);
        call.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    List<Items> itemslist = (List<Items>)response.body();
                    recyclerView = root.findViewById(R.id.ViewUploadRecyclerView);
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);

                    // Corrected the variable name here
                    ArrayList<Items> ItemsArrayList = new ArrayList<>(itemslist);

                    ViewUploadsAdapter objAdapter = new ViewUploadsAdapter(getContext(), ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);

                    Log.e("Uploaded Items","Successfull Respose");



                } else {
                    Log.e("Uploaded Items",email);

                }



            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {

            }
        });

    }

}
