package com.example.barteringapp7;

import java.util.HashSet;
import java.util.Set;

public class SelectedItemsManager {

    private static SelectedItemsManager instance;
    private Set<String> selectedItems;

    private SelectedItemsManager() {
        selectedItems = new HashSet<>();
    }

    public static synchronized SelectedItemsManager getInstance() {
        if (instance == null) {
            instance = new SelectedItemsManager();
        }
        return instance;
    }

    public Set<String> getSelectedItems() {
        return selectedItems;
    }

    public void addItem(String item) {
        selectedItems.add(item);
    }

    public void removeItem(String item) {
        selectedItems.remove(item);
    }

    public void clearItems() {
        selectedItems.clear();
    }
}
