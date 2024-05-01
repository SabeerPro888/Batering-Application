package com.example.barteringapp7.ui.UploadItems2;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class UploadItems2ViewHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView txtcategory;
    public UploadItems2ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.img=(ImageView) itemView.findViewById(R.id.catimg);
        this.txtcategory=(TextView) itemView.findViewById(R.id.txtCatname);


    }
}
