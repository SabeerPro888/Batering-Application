package com.example.barteringapp7.ui.ViewUploads;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.ItemDetailsActivity;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ui.ViewItems.ViewItemsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewUploadsAdapter extends RecyclerView.Adapter {
    private ArrayList<Items> ItemsList;
    private Context context;

    public ViewUploadsAdapter(Context context, ArrayList<Items> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_allitems, parent, false);

        return new ViewItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Items currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();



        // Setting click listener on the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click here
                // Start new activity and pass item details
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra("item_details", currentItem);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}
