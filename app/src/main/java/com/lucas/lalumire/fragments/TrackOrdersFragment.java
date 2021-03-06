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
import com.lucas.lalumire.viewmodels.TrackOrdersViewModel;

public class TrackOrdersFragment extends Fragment {

    private TrackOrdersViewModel mViewModel;

    public static TrackOrdersFragment newInstance() {
        return new TrackOrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.track_orders_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TrackOrdersViewModel.class);
        // TODO: Use the ViewModel
    }

}
