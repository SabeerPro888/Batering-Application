package com.example.barteringapp7.ui.RecommendedItems;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Items> ItemsList;
    private Context context;

    private List<Items> itemListFiltered;

    public Adapter(Context context, ArrayList<Items> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.itemListFiltered = new ArrayList<>(ItemsList);    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_recommended_items, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Items currentItem = itemListFiltered.get(position); // Change here

        Log.e("CurrentItem", new Gson().toJson(currentItem));
        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getImage_01();
        Picasso.get().load(imagePath).into(holder.image);
        if(currentItem.getProfilePic()!=null){
            String ProfilePic = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + currentItem.getProfilePic();
            Picasso.get().load(ProfilePic).into(holder.profile);

        }
        List<String> barterForList=currentItem.getBarterForList();

        String concatenatedBarterFor = "";

        if (barterForList != null && !barterForList.isEmpty()) {
            // Take the first 2 elements
            List<String> firstTwoBarterForList = barterForList.size() > 2 ? barterForList.subList(0, 2) : barterForList;
            concatenatedBarterFor = TextUtils.join(", ", firstTwoBarterForList);

            if (barterForList.size() > 2) {
                concatenatedBarterFor += "...";
            }
        }
        Log.e("concatenatedBarterFor",concatenatedBarterFor);

        // Assuming you have a TextView in your ViewHolder to display the concatenated barter items
        holder.BarterFor.setText(concatenatedBarterFor);



        holder.Title.setText(currentItem.getItem_name());
        holder.User.setText(currentItem.getUser_name());
//        holder.BarterFor.setText( currentItem.getBarter_for());
        String price = String.valueOf(currentItem.getPrice());
        holder.Price.setText(price + "PKR");
        Double ratingDouble = currentItem.getRating(); // Assuming this returns a Double
        float ratingFloat = ratingDouble != null ? ratingDouble.floatValue() : 0.0f; // Convert to float, with a default value if null
        Log.e("Rating value",""+ratingDouble+"");
        holder.ratingBar.setRating(ratingFloat); // Set the rating to the RatingBar
        String verificationStatus=currentItem.getVerification_status();
        if(currentItem.getCategory().equals("Grains")){

            holder.PriceTextView.setText("Price/Kg ");
        }




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


                Log.d("API Response", new Gson().toJson(currentItem));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }
}
