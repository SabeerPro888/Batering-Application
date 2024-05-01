package com.example.barteringapp7.ui.Notifications;

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
import com.example.barteringapp7.RecommendedPosts;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ui.ViewItems.ViewItemsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder>{

    private ArrayList<Items> ItemsList;
    private Context context;
    public NotificationsAdapter(Context context, ArrayList<Items> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_recycler_view, parent, false);

        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        Items currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.image);
        holder.Title.setText(currentItem.getItem_name());
        holder.Message.setText(currentItem.getMessage());
        holder.BarterFor.setText(currentItem.getBarter_for());
        // Convert integer price to String
        holder.Price.setText(String.valueOf(currentItem.getPrice()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click here
                // Start new activity and pass item details
                Intent intent = new Intent(context, RecommendedPosts.class);
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
