package com.lucas.lalumire.Fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.ManageListingsViewModel;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;
import static org.koin.java.KoinJavaComponent.inject;
public class ManageListingsFragment extends Fragment {

    private Lazy<ManageListingsViewModel> manageListingsViewModelLazy = inject(ManageListingsViewModel.class);

    public static ManageListingsFragment newInstance() {
        return new ManageListingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manage_listings_fragment, container, false);
    }

}
