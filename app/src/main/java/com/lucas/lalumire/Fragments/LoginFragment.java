package com.lucas.lalumire.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucas.lalumire.Activities.LoginActivity;
import com.lucas.lalumire.FragmentTransactions;
import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.LoginViewModel;
import com.lucas.lalumire.databinding.LoginFragmentBinding;

import java.util.Objects;

import kotlin.Lazy;
import static org.koin.java.KoinJavaComponent.inject;

public class LoginFragment extends Fragment {

    public LoginFragment(){

    }
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }
    //inject an instance of the viewmodel singleton
    private Lazy<LoginViewModel> loginViewModel = inject(LoginViewModel.class);
    //setup View Binding.
    private LoginFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(binding.usernameText.getEditText()).getText().toString();
                String password = Objects.requireNonNull(binding.passwordText.getEditText()).getText().toString();
                //check if the individual textfields are empty
                if(email.isEmpty()){
                    ((LoginActivity)requireActivity()).showSnackError("Error, one or more fields are empty!", binding.usernameText, "Required");
                }if(password.isEmpty()){
                    ((LoginActivity)requireActivity()).showSnackError("Error, one or more fields are empty!", binding.passwordText, "Required");
                }//if both are not empty
                if(!email.isEmpty() && !password.isEmpty()){
                    loginViewModel.getValue().Login(email, password);
                }
            }
        });
        binding.usernameText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.usernameText.setError(null);
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
                binding.usernameText.setError(null);
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
