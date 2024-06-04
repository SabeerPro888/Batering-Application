package com.example.barteringapp7.ui.ViewUploads;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class ViewUploadsHolder extends RecyclerView.ViewHolder {
    TextView Title;
    TextView BarterFor;
    TextView Price;
    ImageView image;
    TextView User;
    TextView isSold;
    RatingBar ratingBar;

    ImageView verificationTickImageView;


    public ViewUploadsHolder(@NonNull View itemView) {
        super(itemView);
        this.Title=(TextView)itemView.findViewById(R.id.txtTitle);
        this.BarterFor=(TextView)itemView.findViewById(R.id.txtBarterFor);
        this.User=(TextView)itemView.findViewById(R.id.txtuser);
        this.image=(ImageView)itemView.findViewById(R.id.imageViewItem);
        this.Price=(TextView)itemView.findViewById(R.id.textView7);
        this.verificationTickImageView=(ImageView)itemView.findViewById(R.id.verificationTickImageView);
        this.ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar);
        this.isSold=(TextView)itemView.findViewById(R.id.txtisSold);
    }
}
