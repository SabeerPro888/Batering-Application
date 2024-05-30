package com.example.barteringapp7.ui.History;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class HistoryViewholder extends RecyclerView.ViewHolder {
    ImageView senderImage,ReceiverImage;
    TextView SenderName,ReceiverName,RequestedItem,OfferStatus;

    public HistoryViewholder(@NonNull View itemView) {
        super(itemView);

        this.SenderName=(TextView)itemView.findViewById(R.id.txtRequestSenderName);
        this.ReceiverName=(TextView)itemView.findViewById(R.id.txtRequestReceiverName);
        this.RequestedItem=(TextView)itemView.findViewById(R.id.RequestedItemName);
        this.OfferStatus=(TextView)itemView.findViewById(R.id.OfferStatus);
        this.ReceiverImage=(ImageView)itemView.findViewById(R.id.imageView8);
        this.senderImage=(ImageView)itemView.findViewById(R.id.imageView7);
    }
}
