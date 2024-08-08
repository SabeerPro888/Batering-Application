package com.example.barteringapp7.BarterForAttributes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barteringapp7.R;
import com.example.barteringapp7.SelectedItemsManager;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<String> ItemsList;
    private Context context;
    private SelectedItemsManager selectedItemsManager;

    public Adapter(Context context, ArrayList<String> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.selectedItemsManager = SelectedItemsManager.getInstance(); // Get the singleton instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_barterforattributes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentItem = ItemsList.get(position);
        holder.attributeName.setText(currentItem);

        holder.attributecheckbox.setOnCheckedChangeListener(null); // Clear previous listeners to avoid unexpected behavior

        holder.attributecheckbox.setChecked(selectedItemsManager.getSelectedItems().contains(currentItem));

        holder.attributecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedItemsManager.addItem(currentItem);
                } else {
                    selectedItemsManager.removeItem(currentItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }
}
