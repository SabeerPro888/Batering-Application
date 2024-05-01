package com.example.barteringapp7.ui.ViewRequests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewRequestsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;



    public ViewRequestsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is View Requests fragment");    }

    public LiveData<String> getText() {
        return mText;
    }
}
