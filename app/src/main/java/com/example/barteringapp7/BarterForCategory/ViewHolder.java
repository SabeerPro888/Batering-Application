package com.example.barteringapp7.BarterForCategory;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView categoryName;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.categoryName=(TextView)itemView.findViewById(R.id.txtCategoryName);

    }
}
