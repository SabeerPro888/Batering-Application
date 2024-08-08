package com.example.barteringapp7.BarterForCategory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.BarterForSubCategory.ActivityBarterForSubCategory;
import com.example.barteringapp7.Category;
import com.example.barteringapp7.R;
import com.example.barteringapp7.SelectedItemsManager;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<Category> ItemsList;
    private Context context;
    private SelectedItemsManager selectedItemsManager;

    public Adapter(Context context, ArrayList<Category> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.selectedItemsManager = SelectedItemsManager.getInstance(); // Get the singleton instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclierview_barterforcategory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category currentItem = ItemsList.get(position);

        holder.categoryName.setText(currentItem.getCategory_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!holder.categorycheckbox.isChecked()) {
//                    Intent intent = new Intent(context, ActivityBarterForSubCategory.class);
//                    intent.putExtra("category", currentItem.getCategory_name());
//                    if (!(context instanceof android.app.Activity)) {
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    }
//                    context.startActivity(intent);
//                }
//            }
//        });
//
//        holder.categorycheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    selectedItemsManager.addItem(currentItem.getCategory_name());
//                    holder.itemView.setEnabled(false);
//                } else {
//                    selectedItemsManager.removeItem(currentItem.getCategory_name());
//                    holder.itemView.setEnabled(true);
//                }
//            }
//        });
//
//        // Set the checkbox state based on whether the item is in the selectedItems set
//        holder.categorycheckbox.setChecked(selectedItemsManager.getSelectedItems().contains(currentItem.getCategory_name()));

                Intent intent = new Intent(context, ActivityBarterForSubCategory.class);
                intent.putExtra("category", currentItem.getCategory_name());
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
