package com.lucas.lalumire.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lucas.lalumire.activities.AddEditItemActivity;
import com.lucas.lalumire.activities.ItemActivity;
import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.viewmodels.ManageListingsViewModel;
import com.lucas.lalumire.databinding.ManageListingsFragmentBinding;

import java.util.List;

import kotlin.Lazy;

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
                if (binding.itemRecycler.getAdapter() == null) {
                    // set it to the created adapter from viewmodel
                    binding.itemRecycler.setAdapter(manageListingsViewModelLazy.getValue().bigItemAdapter);
                    binding.itemRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                }
                // do nothing otherwise because viewmodel will update the adapter for us.
                manageListingsViewModelLazy.getValue().bigItemAdapter.getCardViewClickedItem().observe(getViewLifecycleOwner(), new Observer<Item>() {
                    @Override
                    public void onChanged(Item item) {
                        if(item !=null) {
                            startItemActivity(item);
                            manageListingsViewModelLazy.getValue().bigItemAdapter.resetCardClick();
                        }
                    }
                });
                // observe the like/unlike click
                manageListingsViewModelLazy.getValue().bigItemAdapter.getItemLikeButtonClickedItem().observe(getViewLifecycleOwner(), new Observer<Item>() {
                    @Override
                    public void onChanged(Item item) {
                        manageListingsViewModelLazy.getValue().likeItem(item);
                    }
                });
                manageListingsViewModelLazy.getValue().bigItemAdapter.getItemUnlikeButtonClickedItem().observe(getViewLifecycleOwner(), new Observer<Item>() {
                    @Override
                    public void onChanged(Item item) {
                        manageListingsViewModelLazy.getValue().unlikeItem(item);
                    }
                });
                manageListingsViewModelLazy.getValue().bigItemAdapter.getItemEditButtonClickedItem().observe(getViewLifecycleOwner(), new Observer<Item>() {
                    @Override
                    public void onChanged(Item item) {
                        if(item !=null) {
                            // start the add Item activity
                            Intent intent = new Intent(getActivity(), AddEditItemActivity.class);
                            intent.putExtra("Item", item);
                            intent.putExtra("Add Not Edit", false);
                            startActivity(intent);
                            // reset to prevent updates from DiffUtil from launching multiple activities
                            manageListingsViewModelLazy.getValue().bigItemAdapter.resetEditClick();
                        }
                    }
                });
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
    private void startItemActivity(Item item){
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra("Item", item);
        startActivity(intent);
    }

}
