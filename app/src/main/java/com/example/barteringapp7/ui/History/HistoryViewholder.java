package com.example.barteringapp7.ui.History;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class HistoryViewholder extends RecyclerView.ViewHolder {
    ImageView senderImage,ReceiverImage;
    TextView SenderName,ReceiverName,RequestedItem,OfferStatus;

    Button btnRequestSenderComfirmation, btnRequestReceiverConfirmation;

    public HistoryViewholder(@NonNull View itemView) {
        super(itemView);

        this.SenderName=(TextView)itemView.findViewById(R.id.txtRequestSenderName);
        this.ReceiverName=(TextView)itemView.findViewById(R.id.txtRequestReceiverName);
        this.RequestedItem=(TextView)itemView.findViewById(R.id.RequestedItemName);
        this.ReceiverImage=(ImageView)itemView.findViewById(R.id.imageView8);
        this.senderImage=(ImageView)itemView.findViewById(R.id.imageView7);
        this.btnRequestSenderComfirmation=(Button)itemView.findViewById(R.id.btnConfirmOfferRequestSender);
        this.btnRequestReceiverConfirmation=(Button)itemView.findViewById(R.id.btnConfirmOfferRequestReceiver);

    }
}
