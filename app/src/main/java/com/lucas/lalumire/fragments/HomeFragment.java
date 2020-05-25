package com.lucas.lalumire.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.models.User;
import com.lucas.lalumire.viewmodels.HomeViewModel;
import com.lucas.lalumire.viewmodels.MainViewModel;
import com.lucas.lalumire.databinding.HomeFragmentBinding;

import java.util.List;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;
import static org.koin.java.KoinJavaComponent.inject;


public class HomeFragment extends Fragment {

    HomeFragmentBinding binding;
    private Lazy<HomeViewModel> homeViewModelLazy = viewModel(this, HomeViewModel.class);
    private Lazy<MainViewModel> mainViewModel = inject(MainViewModel.class);

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //if user does not exist yet, observe and wait.
        if (mainViewModel.getValue().getUserLiveData().getValue() == null) {
            mainViewModel.getValue().getUserLiveData().observe(getViewLifecycleOwner(), new Observer() {
                @Override
                public void onChanged(Object o) {
                    if (o != null) {
                        User it = (User) o;
                        //after user has loaded, get the items and display it
                        loadUI();
                    }
                }
            });
        } else {
            //this happens when the fragment has reopened or somehow the api was faster
            loadUI();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //setup viewbinding
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Function that is called on creation of fragment to ensure that the recyclerview(s) show
     * the correct information WITHOUT reloading information.
     */
    private void loadUI() {
        // if the adapters do not exist, call the viewmodel methods for getting data
        if (homeViewModelLazy.getValue().suggestedItemsAdapter == null || homeViewModelLazy.getValue().followingItemsAdapter == null || homeViewModelLazy.getValue().categoryAdapter == null || homeViewModelLazy.getValue().hotItemsAdapter == null) {
            homeViewModelLazy.getValue().getHotItems();
            homeViewModelLazy.getValue().getCategories();
            homeViewModelLazy.getValue().getFollowingItems();
            homeViewModelLazy.getValue().getSuggestedItems();
        }
        // observe all the recycler view's datasets.
        homeViewModelLazy.getValue().getHotItemsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                // if livedata has changed, viewmodel would have created an adapter.
                binding.hotItems.setAdapter(homeViewModelLazy.getValue().hotItemsAdapter);
                binding.hotItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            }
        });
        homeViewModelLazy.getValue().getCategoriesLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                // if livedata has changed, viewmodel would have created an adapter.
                binding.categoryItems.setAdapter(homeViewModelLazy.getValue().categoryAdapter);
                binding.categoryItems.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
            }
        });
        homeViewModelLazy.getValue().getFollowItemsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                // if livedata has changed, viewmodel would have created an adapter.
                binding.followItems.setAdapter(homeViewModelLazy.getValue().followingItemsAdapter);
                binding.followItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            }
        });
        homeViewModelLazy.getValue().getSuggestedItemsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                // if livedata has changed, viewmodel would have created an adapter.
                binding.interestItems.setAdapter(homeViewModelLazy.getValue().suggestedItemsAdapter);
                binding.interestItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            }
        });
    }


}
