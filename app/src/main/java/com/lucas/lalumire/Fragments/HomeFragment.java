package com.lucas.lalumire.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucas.lalumire.Adapters.SmallItemAdapter;
import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.Viewmodels.HomeViewModel;
import com.lucas.lalumire.Viewmodels.MainViewModel;
import com.lucas.lalumire.databinding.HomeFragmentBinding;

import java.util.List;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;
import static org.koin.java.KoinJavaComponent.inject;


public class HomeFragment extends Fragment {

    private Lazy<HomeViewModel> homeViewModelLazy = viewModel(this, HomeViewModel.class);
    private Lazy<MainViewModel> mainViewModel = inject(MainViewModel.class);
    HomeFragmentBinding binding;

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
        }else{
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
    private void loadUI(){
        if(homeViewModelLazy.getValue().hotItemsAdapter == null) {
            final LiveData<List<Item>> ItemsObservable = homeViewModelLazy.getValue().getHotItems();
            ItemsObservable.observeForever(new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    if (items != null) {
                        Log.d("hehe", String.valueOf(items.size()));
                        //set the already created adapter
                        binding.hotItems.setAdapter(homeViewModelLazy.getValue().hotItemsAdapter);
                        binding.hotItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        //we do not need to observe anymore
                        ItemsObservable.removeObserver(this);
                    }
                }
            });
        }else{
            //if the adapter is not null, use it.
            binding.hotItems.setAdapter(homeViewModelLazy.getValue().hotItemsAdapter);
            //manually set layout manager in case we wanna do anything special, this doesn't take very long anyway
            binding.hotItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }
        if(homeViewModelLazy.getValue().categoryAdapter == null){
            final LiveData<List<String>> itemsObservable = homeViewModelLazy.getValue().getCategories();
            itemsObservable.observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    //when the value is posted, viewmodel would have automatically created an adapter for us
                    binding.categoryItems.setAdapter(homeViewModelLazy.getValue().categoryAdapter);
                    binding.categoryItems.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
                }
            });
        }else{
            //set the adapter that already exists.
            binding.categoryItems.setAdapter(homeViewModelLazy.getValue().categoryAdapter);
            binding.categoryItems.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        }
        //get followed seller's items
        if(homeViewModelLazy.getValue().followingItemsAdapter == null) {
            final LiveData<List<Item>> ItemsObservable = homeViewModelLazy.getValue().getFollowingItems();
            ItemsObservable.observeForever(new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    if (items != null) {
                        Log.d("hehe", String.valueOf(items.size()));
                        //set the already created adapter
                        binding.followItems.setAdapter(homeViewModelLazy.getValue().followingItemsAdapter);
                        binding.followItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        //we do not need to observe anymore
                        ItemsObservable.removeObserver(this);
                    }
                }
            });
        }else{
            //if the adapter is not null, use it.
            binding.followItems.setAdapter(homeViewModelLazy.getValue().followingItemsAdapter);
            //manually set layout manager in case we wanna do anything special, this doesn't take very long anyway
            binding.followItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }
        // and finally this one is for "Things you might like"
        if(homeViewModelLazy.getValue().suggestedItemsAdapter == null) {
            // call the method to get data from viewmodel
            final LiveData<List<Item>> ItemsObservable = homeViewModelLazy.getValue().getSuggestedItems();
            ItemsObservable.observeForever(new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    if (items != null) {
                        Log.d("hehe", String.valueOf(items.size()));
                        //set the already created adapter
                        binding.interestItems.setAdapter(homeViewModelLazy.getValue().suggestedItemsAdapter);
                        binding.interestItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        //we do not need to observe anymore
                        ItemsObservable.removeObserver(this);
                    }
                }
            });
        }else{
            //if the adapter is not null, use it.
            binding.interestItems.setAdapter(homeViewModelLazy.getValue().suggestedItemsAdapter);
            //manually set layout manager in case we wanna do anything special, this doesn't take very long anyway
            binding.interestItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }
    }


}
