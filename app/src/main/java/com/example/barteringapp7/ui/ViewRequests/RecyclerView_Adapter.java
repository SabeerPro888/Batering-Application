package com.example.barteringapp7.ui.ViewRequests;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.Activity_Offer_Details;
import com.example.barteringapp7.R;
import com.example.barteringapp7.ViewRequestsInformation;

import java.util.ArrayList;

public class RecyclerView_Adapter<R extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView_ViewHolder> {
    private ArrayList<ViewRequestsInformation> ItemsList;
    private Context context;


    public RecyclerView_Adapter(Context context, ArrayList<ViewRequestsInformation> ItemsList){
        this.context = context;
        this.ItemsList = ItemsList;
    }
    @NonNull
    @Override
    public RecyclerView_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.barteringapp7.R.layout.recyclerview_requests, parent, false);
        return new RecyclerView_ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_ViewHolder holder, int position) {
        ViewRequestsInformation currentItem = ItemsList.get(position);
        holder.txtSenderName.setText(currentItem.getSender_name());
        holder.txtRequestedItemName.setText(currentItem.getRequestedItemName());
        Double ratingDouble=currentItem.getRating();
        float ratingFloat = ratingDouble != null ? ratingDouble.floatValue() : 0.0f; // Convert to float, with a default value if null

        holder.ratingBar.setRating(ratingFloat);

        holder.btndetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click here
                // Start new activity and pass item details
                Intent intent = new Intent(context, Activity_Offer_Details.class);
                intent.putExtra("Offer_Details", currentItem);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}
