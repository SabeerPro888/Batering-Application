package com.example.barteringapp7.ui.ViewItems;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.ItemDetailsActivity;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewItemsAdapter extends RecyclerView.Adapter<ViewItemsViewHolder> implements Filterable {
    private ArrayList<Items> ItemsList;
    private Context context;

    private List<Items> itemListFiltered;


    // Constructor
    public ViewItemsAdapter(Context context, ArrayList<Items> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.itemListFiltered = new ArrayList<>(ItemsList);    }

    @NonNull
    @Override
    public ViewItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_allitems, parent, false);
        return new ViewItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewItemsViewHolder holder, int position) {
        Items currentItem = itemListFiltered.get(position); // Change here
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.image);
        holder.Title.setText(currentItem.getItem_name());
        holder.User.setText(currentItem.getUser_name());
        holder.BarterFor.setText( currentItem.getBarter_for());
        String price = String.valueOf(currentItem.getPrice());
        holder.Price.setText(price);
        Double ratingDouble = currentItem.getRating(); // Assuming this returns a Double
        float ratingFloat = ratingDouble != null ? ratingDouble.floatValue() : 0.0f; // Convert to float, with a default value if null
        Log.e("Rating value",""+ratingDouble+"");
        holder.ratingBar.setRating(ratingFloat); // Set the rating to the RatingBar
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
        return itemListFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase().trim();

                if (charString.isEmpty()) {
                    itemListFiltered = ItemsList;
                } else {
                    List<Items> filteredList = new ArrayList<>();
                    for (Items item : ItemsList) {
                        if (item.getItem_name().toLowerCase().contains(charString)) {
                            filteredList.add(item);
                        }
                    }
                    itemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (List<Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    }

