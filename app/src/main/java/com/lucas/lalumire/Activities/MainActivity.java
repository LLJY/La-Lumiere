package com.lucas.lalumire.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lucas.lalumire.FragmentTransactions;
import com.lucas.lalumire.Fragments.HomeFragment;
import com.lucas.lalumire.Viewmodels.MainViewModel;
import com.lucas.lalumire.R;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;

public class MainActivity extends AppCompatActivity {
    //inject the viewmodel
    private Lazy<MainViewModel> mainViewModelLazy = viewModel(this, MainViewModel.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //start the home fragment
        FragmentTransactions.LaunchFragment(new HomeFragment(), R.id.main_fragment_holder, this, false);
    }
}
