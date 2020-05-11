package com.lucas.lalumire.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.lucas.lalumire.FragmentTransactions;
import com.lucas.lalumire.Fragments.LoginFragment;
import com.lucas.lalumire.Fragments.SignUpFragment;
import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.LoginViewModel;
import com.lucas.lalumire.databinding.ActivityMainBinding;

import kotlin.Lazy;
import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;

public class LoginActivity extends AppCompatActivity {
    private Lazy<LoginViewModel> loginViewModel = viewModel(this, LoginViewModel.class);
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /* Typically I should leave the bulk of this "Business Logic" in the ViewModel, however ViewModel is NOT context aware
         * which is by design. Hence, the initialization of fragments shall remain in the activity.
         */
        if(!loginViewModel.getValue().isSignUp) {
            FragmentTransactions.LaunchFragment(new LoginFragment(), R.id.login_fragment_container, this, false);
        }else{
            //the back stack should be cleared and fragments restarted when the activity restarts as the fragments are no longer available.
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransactions.LaunchFragment(loginFragment, R.id.login_fragment_container, this, false);
            FragmentTransactions.ReplaceFragment(new SignUpFragment(), R.id.login_fragment_container, this,loginFragment.getClass().getName(), true);
        }
        loginViewModel.getValue().getLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d("bb", String.valueOf(o));
                //sometimes observers can be triggered more than once, check if null as we could have
                //already called resetLiveData(), leading to an NRE.
                if(o != null) {
                    //no smart casting in Java so cast manually. Fuck Java.
                    boolean it = (boolean) o;
                    if (it) {
                        showSnackError("Success");
                    } else {
                        showSnackError("Error");
                    }
                    //stop triggering the observers.
                    loginViewModel.getValue().resetLiveData();
                }
            }
        });


    }
    public void showSnackError(String message){
        Log.d("a", message);
        Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //clear all previous fragments
        this.getSupportFragmentManager().popBackStack();
    }
}
