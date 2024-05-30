package com.example.barteringapp7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

public class RecyclerView_OfferedItems_Adapter extends RecyclerView.Adapter<Recycler_OfferedItems_ViewHolder>{

    private ArrayList<Items> ItemsList;
    private Context context;
    public RecyclerView_OfferedItems_Adapter(Context context, ArrayList<Items> ItemsList){
        this.context = context;
        this.ItemsList = ItemsList;

    }

    @NonNull
    @Override
    public Recycler_OfferedItems_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_offereditems, parent, false);
        return new Recycler_OfferedItems_ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_OfferedItems_ViewHolder holder, int position) {
        Items currentItem = ItemsList.get(position);
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.ItemImage);
        holder.ItemName.setText(currentItem.getItem_name());
        // Convert integer price to String
        holder.ItemPrice.setText(String.valueOf(currentItem.getPrice()+" PKR"));
        if(currentItem.getVerification_status()!="Verified"){
            holder.VerficationTick.setVisibility(View.GONE);
        }else{
            holder.VerficationTick.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}
