package com.lucas.lalumire.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.lucas.lalumire.activities.LoginActivity;
import com.lucas.lalumire.R;
import com.lucas.lalumire.databinding.SplashScreenBinding;
import com.lucas.lalumire.viewmodels.LoginViewModel;
import com.lucas.lalumire.databinding.LoginFragmentBinding;

import java.util.Objects;

import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.bind;
import static org.koin.java.KoinJavaComponent.inject;

public class LoginFragment extends Fragment {

    //inject an instance of the viewmodel singleton
    private Lazy<LoginViewModel> loginViewModel = inject(LoginViewModel.class);
    //setup View Binding.
    private LoginFragmentBinding binding;
    private SplashScreenBinding splashScreenBinding;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!loginViewModel.getValue().isStarted) {
            final ConstraintSet constraintSet1 = new ConstraintSet();
            constraintSet1.clone(binding.constraintLayout);
            ConstraintSet constraintSet2 = new ConstraintSet();
            constraintSet2.clone(getContext(), R.layout.splash_screen);
            // set the starting splash screen
            TransitionManager.beginDelayedTransition(binding.constraintLayout);
            constraintSet2.applyTo(binding.constraintLayout);
            binding.emailBox.setVisibility(View.INVISIBLE);
            binding.passwordText.setVisibility(View.INVISIBLE);
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.googleButton.setVisibility(View.INVISIBLE);
            binding.materialTextView.setVisibility(View.INVISIBLE);
            // delay for 3s before setting view visible
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition(binding.constraintLayout);
                    constraintSet1.applyTo(binding.constraintLayout);
                    binding.emailBox.setVisibility(View.VISIBLE);
                    binding.passwordText.setVisibility(View.VISIBLE);
                    binding.loginButton.setVisibility(View.VISIBLE);
                    binding.googleButton.setVisibility(View.VISIBLE);
                    binding.materialTextView.setVisibility(View.VISIBLE);
                }
            }, 1600);
            loginViewModel.getValue().isStarted = true;
        }
        //set the username text from viewmodel, worth noting that this is actually email.
        binding.emailBox.getEditText().setText(loginViewModel.getValue().loginUsername);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(binding.emailBox.getEditText()).getText().toString();
                String password = Objects.requireNonNull(binding.passwordText.getEditText()).getText().toString();
                //check if the individual textfields are empty
                if (email.isEmpty()) {
                    ((LoginActivity) requireActivity()).showTextError("Error, one or more fields are empty!", binding.emailBox, "Required");
                }
                if (password.isEmpty()) {
                    ((LoginActivity) requireActivity()).showTextError("Error, one or more fields are empty!", binding.passwordText, "Required");
                }//if both are not empty
                if (!email.isEmpty() && !password.isEmpty()) {
                    loginViewModel.getValue().Login(email, password);
                }
            }
        });
        binding.emailBox.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.emailBox.setError(null);
                binding.passwordText.setError(null);
            }
        });
        binding.passwordText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.emailBox.setError(null);
                binding.passwordText.setError(null);
            }
        });
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransactions.ReplaceFragment(new SignUpFragment(), R.id.login_fragment_container, requireActivity(), this.getClass().getName(), true);
            }
        });

    }

}
