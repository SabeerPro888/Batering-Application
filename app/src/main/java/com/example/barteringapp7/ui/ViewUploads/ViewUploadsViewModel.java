package com.example.barteringapp7.ui.ViewUploads;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewUploadsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;


    public ViewUploadsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is View Uploads fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
