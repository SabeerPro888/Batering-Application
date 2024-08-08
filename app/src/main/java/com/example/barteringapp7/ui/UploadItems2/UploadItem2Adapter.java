package com.example.barteringapp7.ui.UploadItems2;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.Category;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.SubCategorySelectionActivity;
import com.example.barteringapp7.ui.ViewItems.ViewItemsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadItem2Adapter extends RecyclerView.Adapter<UploadItems2ViewHolder> {


    private ArrayList<Category> ItemsList;
    private Context context;

    // Constructor
    public UploadItem2Adapter(Context context, ArrayList<Category> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
    }

    @NonNull
    @Override
    public UploadItems2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoriesrecyclerview_layout, parent, false);
        return new UploadItems2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadItems2ViewHolder holder, int position) {
        Category currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage();
        Picasso.get().load(imagePath).into(holder.img);
        holder.txtcategory.setText(currentItem.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubCategorySelectionActivity.class);
                intent.putExtra("categoryName", currentItem.getCategory_name());
                GlobalVariables.getInstance().setCategory(currentItem.getCategory_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }



}
