package com.example.barteringapp7;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_MakeBarterOfferViewHolder extends RecyclerView.ViewHolder {
    TextView Title;
    ImageView image;
    TextView Price;
    TextView BarterFor;
    CheckBox cbx;
    public RecyclerView_MakeBarterOfferViewHolder(@NonNull View itemView) {
        super(itemView);

        this.Title=(TextView)itemView.findViewById(R.id.txtTitle);
        this.image=(ImageView)itemView.findViewById(R.id.imageViewItem);
        this.Price=(TextView)itemView.findViewById(R.id.textView7);
        this.BarterFor=(TextView)itemView.findViewById(R.id.txtBarterFor);
        this.cbx=(CheckBox) itemView.findViewById(R.id.cbx1);
    }
}
