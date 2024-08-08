package com.example.barteringapp7.AdvanceSearchResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.Items;
import com.example.barteringapp7.MyApp;
import com.example.barteringapp7.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdvanceSearchResult extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Adapter objAdapter;
    ArrayList<Items> itemsList = new ArrayList<>(); // Changed to ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search_result);


        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Search Results");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.advancedSearchRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // Get items list from MyApp singleton

        List<Items> fetchedItemsList = GlobalVariables.getInstance().getItemsList();

        // Convert List<Items> to ArrayList<Items>
        itemsList.addAll(fetchedItemsList);

        // Initialize and set adapter
        objAdapter = new Adapter(this, itemsList);
        recyclerView.setAdapter(objAdapter);

        // Add divider decoration between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Spinner sortSpinner = findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sortItemsByPrice();
                        break;
                    case 1:
                        sortItemsByTitle();
                        break;
                    case 2:
//                        sortItemsByDate();
                        break;
                    case 3:
                        sortItemsByPriceDescending(); // Descending order for price
                        break;
                    case 4:
                        sortItemsByTitleDescending(); // Descending order for title
                        break;
                    case 5:
//                        sortItemsByDateDescending(); // Descending order for date
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }
    private void sortItemsByPrice() {
        Collections.sort(itemsList, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });
        objAdapter.notifyDataSetChanged();
    }

    private void sortItemsByTitle() {
        Collections.sort(itemsList, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                return o1.getItem_name().compareTo(o2.getItem_name());
            }
        });
        objAdapter.notifyDataSetChanged();
    }
    private void sortItemsByPriceDescending() {
        Collections.sort(itemsList, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                return Double.compare(o2.getPrice(), o1.getPrice()); // Compare in descending order
            }
        });
        objAdapter.notifyDataSetChanged();
    }
    private void sortItemsByTitleDescending() {
        Collections.sort(itemsList, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                return o2.getItem_name().compareTo(o1.getItem_name()); // Compare in descending order
            }
        });
        objAdapter.notifyDataSetChanged();
    }
//    private void sortItemsByDateDescending() {
//        Collections.sort(itemsList, new Comparator<Items>() {
//            @Override
//            public int compare(Items o1, Items o2) {
//                // Compare dates in descending order
//                return o2.getDate().compareTo(o1.getDate());
//            }
//        });
//        objAdapter.notifyDataSetChanged();
//    }


//    private void sortItemsByDate() {
//        Collections.sort(itemsList, new Comparator<Items>() {
//            @Override
//            public int compare(Items o1, Items o2) {
//                return o1.getDate().compareTo(o2.getDate()); // Assuming you have a getDate() method in Items
//            }
//        });
//        objAdapter.notifyDataSetChanged();
//    }

    // Method to get context for adapter
    public Context getContext() {
        return AdvanceSearchResult.this;
    }
}
