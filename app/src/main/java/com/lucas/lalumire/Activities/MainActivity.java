package com.lucas.lalumire.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.lucas.lalumire.Fragments.FragmentTransactions;
import com.lucas.lalumire.Fragments.HomeFragment;
import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.Models.UserType;
import com.lucas.lalumire.Viewmodels.MainViewModel;
import com.lucas.lalumire.R;
import com.lucas.lalumire.databinding.ActivityMainBinding;
import com.lucas.lalumire.databinding.ActivityMainScreenBinding;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;
    //inject the viewmodel
    private Lazy<MainViewModel> mainViewModelLazy = viewModel(this, MainViewModel.class);
    private ActivityMainScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentTransactions.LaunchFragment(new HomeFragment(), R.id.main_fragment_holder, MainActivity.this, false);
        pd = ProgressDialog.show(this, "Loading", "Please wait...");
        //start the home fragment
        LiveData observeOnce = mainViewModelLazy.getValue().getUserLiveData();
        observeOnce.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if(o != null) {
                    //stop loading
                    pd.dismiss();
                    //set contentview when everything is ready.
                    //set the adapter when the user data is ready.
                    binding.bottomSheet.menuRecycler.setAdapter(mainViewModelLazy.getValue().menuBottomSheetAdapter);
                    binding.bottomSheet.menuRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }
        });
    }
}
