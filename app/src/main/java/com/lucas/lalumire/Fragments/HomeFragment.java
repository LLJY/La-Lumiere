package com.lucas.lalumire.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //setup viewbinding
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainViewModel.getValue().getUserLiveData().getValue() == null) {
            mainViewModel.getValue().getUserLiveData().observe(getViewLifecycleOwner(), new Observer() {
                @Override
                public void onChanged(Object o) {
                    if (o != null) {
                        User it = (User) o;
                        //after user has loaded, get the items and display it
                        final LiveData<List<Item>> ItemsObservable = homeViewModelLazy.getValue().getHotItems();
                        ItemsObservable.observeForever(new Observer<List<Item>>() {
                            @Override
                            public void onChanged(List<Item> items) {
                                if (items != null) {
                                    Log.d("hehe", String.valueOf(items.size()));
                                    binding.hotItems.setAdapter(new SmallItemAdapter(items));
                                    binding.hotItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                    //we do not need to observe anymore
                                    ItemsObservable.removeObserver(this);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

}
