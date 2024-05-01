package com.example.barteringapp7.ui.ViewItems;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

import org.w3c.dom.Text;

public class ViewItemsViewHolder extends RecyclerView.ViewHolder {
    TextView Title;
    TextView BarterFor;
    TextView Price;
    ImageView image;
    TextView User;

    TextView VerificationStatus;

    public ViewItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.Title=(TextView)itemView.findViewById(R.id.txtTitle);
        this.BarterFor=(TextView)itemView.findViewById(R.id.txtBarterFor);
        this.User=(TextView)itemView.findViewById(R.id.txtuser);
        this.image=(ImageView)itemView.findViewById(R.id.imageViewItem);
        this.Price=(TextView)itemView.findViewById(R.id.textView7);


    }
}
