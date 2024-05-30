package com.example.barteringapp7;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Recycler_OfferedItems_ViewHolder extends RecyclerView .ViewHolder{
    ImageView ItemImage;
    ImageView VerficationTick;
    TextView ItemName;
    TextView ItemPrice;
    public Recycler_OfferedItems_ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.ItemName=(TextView)itemView.findViewById(R.id.textView11);
        this.ItemImage=(ImageView)itemView.findViewById(R.id.OfferedItemImage);
        this.ItemPrice=(TextView)itemView.findViewById(R.id.OfferedItemPrice);
        this.VerficationTick=(ImageView)itemView.findViewById(R.id.OfferedItemCheckImage);




    }
}
