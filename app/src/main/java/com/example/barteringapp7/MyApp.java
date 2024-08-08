package com.example.barteringapp7;

import android.app.Application;
import java.util.List;

public class MyApp extends Application {
    private List<Items> itemsList;

    public List<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }
}
