package com.example.barteringapp7.BarterForAttributes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView attributeName;
    CheckBox attributecheckbox;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.attributeName=(TextView)itemView.findViewById(R.id.txtAttributeName);
        this.attributecheckbox=(CheckBox) itemView.findViewById(R.id.cbxAttribute);
    }
}
