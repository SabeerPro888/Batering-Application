package com.example.barteringapp7.ui.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.ViewRequestsInformation;
import com.example.barteringapp7.ui.Notifications.NotificationsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewholder> {

    private ArrayList<ViewRequestsInformation> ItemsList;
    private Context context;

    public HistoryAdapter(Context context, ArrayList<ViewRequestsInformation> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
    }
    @NonNull
    @Override
    public HistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_history, parent, false);

        return new HistoryViewholder(view);    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewholder holder, int position) {
        ViewRequestsInformation currentItem = ItemsList.get(position);
        holder.SenderName.setText(currentItem.getSender_name());
        holder.ReceiverName.setText(currentItem.getReceiver_name());
        holder.RequestedItem.setText(currentItem.getRequestedItemName());
        // Convert integer price to String
        holder.OfferStatus.setText(currentItem.getstatus());

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}
