package com.lucas.lalumire.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.lucas.lalumire.Fragments.FragmentTransactions;
import com.lucas.lalumire.Fragments.HomeFragment;
import com.lucas.lalumire.Models.MenuItem;
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
        View.OnClickListener expandBottomSheetOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //expand when menu is clicked
                BottomSheetBehavior.from(binding.bottomSheet.bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        };
        binding.bottomSheet.menuButton.setOnClickListener(expandBottomSheetOnClick);
        binding.bottomSheet.menuBar.setOnClickListener(expandBottomSheetOnClick);
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
                    //observe for clicks
                    mainViewModelLazy.getValue().menuBottomSheetAdapter.getFragmentClassLiveData().observe(MainActivity.this, new Observer<MenuItem>() {
                        @Override
                        public void onChanged(MenuItem item) {
                            if(item != null){
                                try {
                                    //collapse the bottomsheet
                                    BottomSheetBehavior.from(binding.bottomSheet.bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    //start the fragment
                                    FragmentTransactions.LaunchFragment((Fragment) item.itemFragment.newInstance(), R.id.main_fragment_holder, MainActivity.this,true);
                                    //set the name of the active fragment
                                    binding.bottomSheet.menuLabel.setText(item.itemName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }else{
                    Intent startLoginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    //clear the backstack as it is undesirable for the user to enter the login screen again
                    startLoginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //TODO Pass MainActivity user's uid or token
                    startActivity(startLoginActivityIntent);
                }
            }
        });
    }
}
