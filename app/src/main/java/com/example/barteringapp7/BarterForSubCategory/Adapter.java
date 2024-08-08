package com.example.barteringapp7.BarterForSubCategory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.BarterForAttributes.ActivityBarterForAttributes;
import com.example.barteringapp7.R;
import com.example.barteringapp7.SelectedItemsManager;
import com.example.barteringapp7.SubCategory;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<SubCategory> ItemsList;
    private Context context;
    private SelectedItemsManager selectedItemsManager;

    public Adapter(Context context, ArrayList<SubCategory> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.selectedItemsManager = SelectedItemsManager.getInstance(); // Get the singleton instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_barterforsubcategory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubCategory currentItem = ItemsList.get(position);

        holder.SubcategoryName.setText(currentItem.getSubcategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!holder.Subcategorycheckbox.isChecked()) {
//                    Intent intent = new Intent(context, ActivityBarterForAttributes.class);
//                    intent.putExtra("subcategory", currentItem.getSubcategory_name());
//                    if (!(context instanceof android.app.Activity)) {
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    }
//                    context.startActivity(intent);
//                }
//            }
//        });
//
//        holder.Subcategorycheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    selectedItemsManager.addItem(currentItem.getSubcategory_name());
//                    holder.itemView.setEnabled(false);
//                } else {
//                    selectedItemsManager.removeItem(currentItem.getSubcategory_name());
//                    holder.itemView.setEnabled(true);
//                }
//            }
//        });

                // Set the checkbox state based on whether the item is in the selectedItems set
//        holder.Subcategorycheckbox.setChecked(selectedItemsManager.getSelectedItems().contains(currentItem.getSubcategory_name()));
                Intent intent = new Intent(context, ActivityBarterForAttributes.class);
                intent.putExtra("subcategory", currentItem.getSubcategory_name());
                if (!(context instanceof android.app.Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}
