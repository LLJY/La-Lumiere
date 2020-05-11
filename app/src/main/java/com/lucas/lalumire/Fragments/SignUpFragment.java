package com.lucas.lalumire.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.lucas.lalumire.Activities.LoginActivity;
import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.LoginViewModel;
import com.lucas.lalumire.databinding.FragmentSignUpBinding;

import java.util.Objects;

import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.inject;


public class SignUpFragment extends Fragment {
    //use kotlin's Lazy property to initialize the viewmodel
    private Lazy<LoginViewModel> loginViewModel = inject(LoginViewModel.class);
    private FragmentSignUpBinding binding;
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel.getValue().isSignUp = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //emulate back press
                requireActivity().onBackPressed();
            }
        });

        binding.agreeTermsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loginViewModel.getValue().agreedToTerms = isChecked;
                binding.doneButton.setEnabled(isChecked);
            }
        });
        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all the data from the fields
                String email = Objects.requireNonNull(binding.usernameBox.getEditText()).getText().toString();
                String password = Objects.requireNonNull(binding.passwordBox.getEditText()).getText().toString();
                String passwordAgain = Objects.requireNonNull(binding.passwordAgainBox.getEditText()).getText().toString();
                String name = binding.usernameBox.getEditText().getText().toString();
                String username = binding.usernameBox.getEditText().getText().toString();
                if(password.equals(passwordAgain)) {
                    loginViewModel.getValue().SignUp(email, password, name, username);
                    //observe the success variable
                    loginViewModel.getValue().getLiveData().observe(getViewLifecycleOwner(), new Observer() {
                        @Override
                        public void onChanged(Object o) {
                            if(o != null){
                                boolean it = (Boolean) o;
                                if(it){
                                    requireActivity().onBackPressed();
                                }
                            }
                        }
                    });
                }else{
                    ((LoginActivity)requireActivity()).showSnackError("Passwords do not match");
                    binding.passwordBox.setErrorEnabled(true);
                    binding.passwordAgainBox.setErrorEnabled(true);
                    binding.passwordBox.setError("Passwords do not match");
                    binding.passwordAgainBox.setError("Passwords do not match");
                }
            }
        });

        //Usually I would use databinding, but right now it's not very good on android.
        Objects.requireNonNull(binding.emailBox.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.getValue().Email = s.toString();

            }
        });
        Objects.requireNonNull(binding.usernameBox.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.getValue().Username = s.toString();
            }
        });
        Objects.requireNonNull(binding.nameBox.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.getValue().Name = s.toString();
            }
        });
        TextWatcher passwordFieldsWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.passwordBox.setError(null);
                binding.passwordAgainBox.setError(null);
            }
        };
        //set the same password box listener
        Objects.requireNonNull(binding.passwordAgainBox.getEditText()).addTextChangedListener(passwordFieldsWatcher);
        Objects.requireNonNull(binding.passwordBox.getEditText()).addTextChangedListener(passwordFieldsWatcher);

        syncWithViewModel();
    }
    /** activity/fragment gets destroyed when user leaves or screen is rotated but ViewModel stays active until app is killed by user or LMK/LMKD.
     * Create a function to get stored variables from ViewModel to sync the view up with the previous state before it was destroyed.
     */
    private void syncWithViewModel(){
        binding.agreeTermsCheck.setChecked(loginViewModel.getValue().agreedToTerms);
        binding.doneButton.setEnabled(loginViewModel.getValue().agreedToTerms);
        Objects.requireNonNull(binding.emailBox.getEditText()).setText(loginViewModel.getValue().Email);
        Objects.requireNonNull(binding.usernameBox.getEditText()).setText(loginViewModel.getValue().Username);
        Objects.requireNonNull(binding.nameBox.getEditText()).setText(loginViewModel.getValue().Name);
    }
}
