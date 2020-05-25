package com.lucas.lalumire.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucas.lalumire.Activities.AddEditItemActivity;
import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.AddEditItemViewModel;
import com.lucas.lalumire.Viewmodels.ManageListingsViewModel;
import com.lucas.lalumire.databinding.ManageListingsFragmentBinding;

import java.util.List;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;
import static org.koin.java.KoinJavaComponent.inject;
public class ManageListingsFragment extends Fragment {
    private ManageListingsFragmentBinding binding;

    private Lazy<ManageListingsViewModel> manageListingsViewModelLazy = inject(ManageListingsViewModel.class);

    public static ManageListingsFragment newInstance() {
        return new ManageListingsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manageListingsViewModelLazy.getValue().getListItemsLive().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if(binding.itemRecycler.getAdapter() == null){
                    // set it to the created adapter from viewmodel
                    binding.itemRecycler.setAdapter(manageListingsViewModelLazy.getValue().bigItemAdapter);
                    binding.itemRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                }
                // do nothing otherwise because viewmodel will update the adapter for us.
            }
        });
        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditItemActivity.class);
                intent.putExtra("Add Not Edit", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ManageListingsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


}
