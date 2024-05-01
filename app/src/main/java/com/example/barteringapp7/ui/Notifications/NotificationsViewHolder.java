package com.example.barteringapp7.ui.Notifications;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

import org.w3c.dom.Text;

public class NotificationsViewHolder extends RecyclerView.ViewHolder {
    TextView Title;
    TextView Message;
    ImageView image;
    TextView Price;

    TextView BarterFor;
    public NotificationsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.Title=(TextView)itemView.findViewById(R.id.txtTitle);
        this.Message=(TextView)itemView.findViewById(R.id.txtMessage);
        this.image=(ImageView)itemView.findViewById(R.id.imageViewItem);
        this.Price=(TextView)itemView.findViewById(R.id.textView7);
        this.BarterFor=(TextView)itemView.findViewById(R.id.txtBarterFor);


    }
}
