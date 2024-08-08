package com.example.barteringapp7.AdvanceSearchResult;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView Title;
    TextView BarterFor;
    TextView Price;
    ImageView image;
    TextView User;
    ImageView profile;
    RatingBar ratingBar;

    TextView PriceTextView;

    ImageView verificationTickImageView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.Title=(TextView)itemView.findViewById(R.id.AdvancedtxtTitle);
        this.BarterFor=(TextView)itemView.findViewById(R.id.AdvancedtxtBarterFor);
        this.User=(TextView)itemView.findViewById(R.id.Advancedtxtuser);
        this.image=(ImageView)itemView.findViewById(R.id.AdvancedimageViewItem);
        this.Price=(TextView)itemView.findViewById(R.id.AdvancedtextView7);
        this.verificationTickImageView=(ImageView)itemView.findViewById(R.id.AdvancedverificationTickImageView);
        this.profile=(ImageView)itemView.findViewById(R.id.AdvancedimageView7);
        this.ratingBar=(RatingBar) itemView.findViewById(R.id.AdvancedratingBar);
        this.PriceTextView=(TextView)itemView.findViewById(R.id.AdvancedtextView4);
    }
}
