package com.lucas.lalumire.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.Viewmodels.HomeViewModel;
import com.lucas.lalumire.Viewmodels.MainViewModel;
import com.lucas.lalumire.databinding.HomeFragmentBinding;

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
        mainViewModel.getValue().getUserInfo();
        mainViewModel.getValue().getUserLiveData().observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(Object o) {
                if(o!=null) {
                    User it = (User)o;
                    binding.welcomeMessage.setText("Welcome Back " + it.Name +" Your id is "+it.uid);
                }
            }
        });
    }

}
