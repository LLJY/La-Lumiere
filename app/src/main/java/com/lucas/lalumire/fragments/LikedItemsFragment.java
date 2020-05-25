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
import com.lucas.lalumire.viewmodels.LikedItemsViewModel;

public class LikedItemsFragment extends Fragment {

    private LikedItemsViewModel mViewModel;

    public static LikedItemsFragment newInstance() {
        return new LikedItemsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.liked_items_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LikedItemsViewModel.class);
        // TODO: Use the ViewModel
    }

}
