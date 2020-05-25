package com.lucas.lalumire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lucas.lalumire.fragments.FragmentTransactions;
import com.lucas.lalumire.fragments.LoginFragment;
import com.lucas.lalumire.fragments.SignUpFragment;
import com.lucas.lalumire.models.LoginActivityStatus;
import com.lucas.lalumire.R;
import com.lucas.lalumire.viewmodels.LoginViewModel;
import com.lucas.lalumire.databinding.ActivityMainBinding;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;

public class LoginActivity extends AppCompatActivity {
    private Lazy<LoginViewModel> loginViewModel = viewModel(this, LoginViewModel.class);
    private ActivityMainBinding binding;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /* Typically I should leave the bulk of this "Business Logic" in the ViewModel, however ViewModel is NOT context aware
         * which is by design. Hence, the initialization of fragments shall remain in the activity.
         */
        //check if user is logged in, if they are logged in, open up MainActivity.
        if (!loginViewModel.getValue().isLoggedIn()) {
            if (!loginViewModel.getValue().isSignUp) {
                FragmentTransactions.LaunchFragment(new LoginFragment(), R.id.login_fragment_container, this, false);
            } else {
                //the back stack should be cleared and fragments restarted when the activity restarts as the fragments are no longer available.
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransactions.LaunchFragment(loginFragment, R.id.login_fragment_container, this, false);
                FragmentTransactions.ReplaceFragment(new SignUpFragment(), R.id.login_fragment_container, this, loginFragment.getClass().getName(), true);
            }
        } else {
            startLoginActivity();
        }
        loginViewModel.getValue().getLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (o != null) {
                    //cast object o to loginstatus
                    LoginActivityStatus it = (LoginActivityStatus) o;
                    switch (it) {
                        //if an error has occured, just print and error on the snackbar.
                        case STATUS_LOADING:
                            pd = ProgressDialog.show(LoginActivity.this, "Loading...", "Please Wait...");
                            break;
                        case STATUS_BAD_EMAIL:
                            pd.dismiss();
                            showSnackMessage("Invalid email format");
                            break;
                        case STATUS_INVALID_USER:
                            pd.dismiss();
                            showSnackMessage("User not found!");
                            break;
                        case STATUS_WEAK_PASSWORD:
                            pd.dismiss();
                            showSnackMessage("Weak password, must be 6 characters or longer!");
                            break;
                        case STATUS_ACCOUNT_EXISTS:
                            pd.dismiss();
                            showSnackMessage("Account already exists!");
                            break;
                        case STATUS_INVALID_CREDENTIALS:
                            pd.dismiss();
                            showSnackMessage("Invalid email or password");
                            break;
                        case STATUS_ERR:
                            pd.dismiss();
                            showSnackMessage("An error has occurred");
                            break;
                        case STATUS_LOGIN_SUCCESS:
                            pd.dismiss();
                            //when login success, open mainactivity
                            showSnackMessage("Welcome back!");
                            startLoginActivity();
                            break;
                        case STATUS_SIGN_UP_SUCCESS:
                            pd.dismiss();
                            //exit the fragment
                            onBackPressed();
                            break;
                    }
                    loginViewModel.getValue().resetLiveData();
                }
            }
        });


    }

    /**
     * Function to show error to the user and add error to the textfield.
     *
     * @param message
     * @param input
     * @param textFieldMessage
     */
    public void showTextError(String message, TextInputLayout input, String textFieldMessage) {
        Log.d("snackErr", message);
        Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_LONG).show();
        input.setError(textFieldMessage);
    }

    public void showSnackMessage(String message) {
        Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_LONG).show();
    }

    private void startLoginActivity() {
        Intent startLoginActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        //clear the backstack as it is undesirable for the user to enter the login screen again
        startLoginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //TODO Pass MainActivity user's uid or token
        startActivity(startLoginActivityIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //clear all previous fragments
        this.getSupportFragmentManager().popBackStack();
    }
}
