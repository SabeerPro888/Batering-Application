package com.example.barteringapp7.ui.ViewItems;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewItemsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;


    public ViewItemsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is View Item fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
