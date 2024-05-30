package com.example.barteringapp7.ui.ViewUploads;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.ItemDetailsActivity;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ui.ViewItems.ViewItemsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewUploadsAdapter extends RecyclerView.Adapter<ViewUploadsHolder> {
    private ArrayList<Items> ItemsList;
    private Context context;

    public ViewUploadsAdapter(Context context, ArrayList<Items> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
    }


    @NonNull
    @Override
    public ViewUploadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_view_uploads, parent, false);
        return new ViewUploadsHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewUploadsHolder holder, int position) {
        Items currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.image);
        holder.Title.setText(currentItem.getItem_name());
        holder.BarterFor.setText( currentItem.getBarter_for());
        String price = String.valueOf(currentItem.getPrice());
        holder.Price.setText(price);
        String verificationStatus=currentItem.getVerification_status();



        if ("Verified".equals(verificationStatus)) {
            holder.verificationTickImageView.setVisibility(View.VISIBLE);
        } else {
            holder.verificationTickImageView.setVisibility(View.GONE);
        }
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
