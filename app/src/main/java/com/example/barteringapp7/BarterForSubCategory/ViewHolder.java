package com.example.barteringapp7.BarterForSubCategory;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView SubcategoryName;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.SubcategoryName=(TextView)itemView.findViewById(R.id.txtSubCategoryName);
    }
}
