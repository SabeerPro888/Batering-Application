package com.example.barteringapp7.ui.ViewRequests;

import android.media.Rating;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;


public class RecyclerView_ViewHolder extends RecyclerView.ViewHolder {
    TextView txtRequestedItemName;
    TextView txtSenderName;

    ImageView Profile;
    RatingBar ratingBar;
    Button btndetails;
    public RecyclerView_ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.txtRequestedItemName=(TextView)itemView.findViewById(R.id.RequestedItemName);
        this.txtSenderName=(TextView)itemView.findViewById(R.id.txtRequestSenderName);
        this.btndetails=(Button)itemView.findViewById(R.id.btnViewRequestDetails);
        this.ratingBar=(RatingBar)itemView.findViewById(R.id.ratingBarRequests);
        this.Profile=(ImageView) itemView.findViewById(R.id.imageView7);




    }
}
