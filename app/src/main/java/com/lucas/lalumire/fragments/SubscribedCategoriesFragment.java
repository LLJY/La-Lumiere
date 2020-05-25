package com.lucas.lalumire.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucas.lalumire.R;
import com.lucas.lalumire.viewmodels.SubscribedCategoriesViewModel;

public class SubscribedCategoriesFragment extends Fragment {

    private SubscribedCategoriesViewModel mViewModel;

    public static SubscribedCategoriesFragment newInstance() {
        return new SubscribedCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.subscribed_categories_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SubscribedCategoriesViewModel.class);
        // TODO: Use the ViewModel
    }

}
