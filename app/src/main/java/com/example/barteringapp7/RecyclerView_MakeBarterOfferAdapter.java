package com.example.barteringapp7;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecyclerView_MakeBarterOfferAdapter extends RecyclerView.Adapter<RecyclerView_MakeBarterOfferViewHolder> {

    private ArrayList<Items> ItemsList;
    private Context context;
    private Set<Integer> selectedItemsIds;


    public RecyclerView_MakeBarterOfferAdapter(Context context, ArrayList<Items> ItemsList){
        this.context = context;
        this.ItemsList = ItemsList;
        this.selectedItemsIds = new HashSet<>();

    }
    @NonNull
    @Override
    public RecyclerView_MakeBarterOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_make_barter_offer, parent, false);
        return new RecyclerView_MakeBarterOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_MakeBarterOfferViewHolder holder, int position) {
        Items currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.image);
        holder.Title.setText(currentItem.getItem_name());
        holder.BarterFor.setText(currentItem.getBarter_for());
        // Convert integer price to String
        holder.Price.setText(String.valueOf(currentItem.getPrice()));

        holder.cbx.setOnClickListener(v -> {
            if (selectedItemsIds.contains(currentItem.getItem_id())) {
                selectedItemsIds.remove(currentItem.getItem_id());
                holder.cbx.setChecked(false);
                Log.d("RecyclerViewAdapter", "Item deselected: " + currentItem.getItem_id());

            } else {
                selectedItemsIds.add(currentItem.getItem_id());
                holder.cbx.setChecked(true);
                Log.d("RecyclerViewAdapter", "Item selected: " + currentItem.getItem_id());

            }
        });
    }
    public List<Integer> getSelectedItemsIds() {
        return new ArrayList<>(selectedItemsIds);
    }



    @Override
    public int getItemCount() {
       return  ItemsList.size();
    }
}
