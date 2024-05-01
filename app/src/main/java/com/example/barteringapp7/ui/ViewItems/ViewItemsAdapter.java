package com.example.barteringapp7.ui.ViewItems;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.ItemDetailsActivity;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewItemsAdapter extends RecyclerView.Adapter<ViewItemsViewHolder> {
    private ArrayList<Items> ItemsList;
    private Context context;

    // Constructor
    public ViewItemsAdapter(Context context, ArrayList<Items> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
    }

    @NonNull
    @Override
    public ViewItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_allitems, parent, false);
        return new ViewItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewItemsViewHolder holder, int position) {
        Items currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.image);
        holder.Title.setText(currentItem.getItem_name());
        holder.User.setText(currentItem.getUser_name());
        holder.BarterFor.setText( currentItem.getBarter_for());
        String price = String.valueOf(currentItem.getPrice());
        holder.Price.setText(price);


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
