package com.example.barteringapp7.ui.ViewRequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.barteringapp7.databinding.FragmentViewRequestsBinding;


public class ViewRequestsFragment extends Fragment {
    private FragmentViewRequestsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewRequestsViewModel viewUploadsViewModel =
                new ViewModelProvider(this).get(ViewRequestsViewModel.class);

        binding = FragmentViewRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        viewUploadsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
