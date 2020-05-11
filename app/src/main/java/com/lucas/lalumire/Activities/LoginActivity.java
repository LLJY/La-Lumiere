package com.lucas.lalumire.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lucas.lalumire.FragmentTransactions;
import com.lucas.lalumire.Fragments.LoginFragment;
import com.lucas.lalumire.Fragments.SignUpFragment;
import com.lucas.lalumire.Models.LoginActivityStatus;
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
                if(o!=null){
                    //cast object o to loginstatus
                    LoginActivityStatus it = (LoginActivityStatus) o;
                    switch(it){
                        //if an error has occured, just print and error on the snackbar.
                        case STATUS_ERR:
                            showSnackMessage("An error has occurred");
                            break;
                        case STATUS_LOGIN_SUCCESS:
                            //when login success, open mainactivity
                            showSnackMessage("Success");
                            //TODO finish MainActivity
                        case STATUS_SIGN_UP_SUCCESS:
                            //exit the fragment
                            onBackPressed();
                    }
                    loginViewModel.getValue().resetLiveData();
                }
            }
        });


    }
    public void showSnackError(String message, TextInputLayout input, String textFieldMessage){
        Log.d("snackErr", message);
        Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_LONG).show();
        input.setError(textFieldMessage);
    }

    public void showSnackMessage(String message){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //clear all previous fragments
        this.getSupportFragmentManager().popBackStack();
    }
}
