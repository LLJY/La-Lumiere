package com.lucas.lalumire.Viewmodels;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.lucas.lalumire.Models.LoginActivityStatus;
import com.lucas.lalumire.Repositories.FirebaseAuthRepository;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.inject;

public class LoginViewModel extends ViewModel {
    private @NonNull
    FirebaseAuthRepository firebaseAuthRepository;
    Lazy<FirebaseAuth> mAuthLazy = inject(FirebaseAuth.class);
    private MutableLiveData<LoginActivityStatus> mutableSuccess = new MutableLiveData<>();
    //store variables
    public String Email = "";
    public String Username = "";
    //this is to be stored if the user has succesfully signed up, so we fill the username textbox
    public String loginUsername = "";
    public String Name = "";
    public boolean agreedToTerms = false;
    //determines if currently on the signup screen;
    public boolean isSignUp = false;
    /* NEVER NEVER NEVER expose MutableLiveData, so expose a LiveData version of it.
     * This is supposed to look like val livedata : LiveData get() = mutableSuccess
     */
    public LiveData getLiveData() {
        return this.mutableSuccess;
    }



    //constructor includes an instance of the authentication repository.
    public LoginViewModel(@NotNull FirebaseAuthRepository firebaseRepo) {
        this.firebaseAuthRepository = firebaseRepo;
    }

    public void SignUp(final String email, final String password, final String name, final String username) {
        mutableSuccess.postValue(LoginActivityStatus.STATUS_LOADING);
        //set the livedata to the one returned by the function to observe for errors
        new Thread(new Runnable() {
            @Override
            public void run() {
                final LiveData<LoginActivityStatus> result = firebaseAuthRepository.SignUp(email, password, name, username);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result.observeForever(new Observer<LoginActivityStatus>() {
                            @Override
                            public void onChanged(LoginActivityStatus loginActivityStatus) {
                                //post value to observers in Activity
                                mutableSuccess.postValue(loginActivityStatus);
                                result.removeObserver(this);
                            }
                        });
                    }
                });
            }
        }).start();

        Log.d("bb", String.valueOf(mutableSuccess.getValue()));
    }

    public void Login(final String email, final String password) {
        //set the livedata to the one returned by the function to observe for errors
        mutableSuccess.postValue(LoginActivityStatus.STATUS_LOADING);
        new Thread(new Runnable() {
            @Override
            //run the login method in a thread to avoid blocking the ui
            public void run() {
                final LiveData<LoginActivityStatus> result = firebaseAuthRepository.Login(email, password);
                //observe on the mainthread as you cannot run observeForever on a background thread.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result.observeForever(new Observer<LoginActivityStatus>() {
                            @Override
                            public void onChanged(LoginActivityStatus a) {
                                //notify the observers in activity
                                mutableSuccess.postValue(a);
                                //we do not need the observer anymore
                                result.removeObserver(this);
                            }
                        });
                    }
                });
            }
        }).start();

    }

    public Boolean isLoggedIn(){
        //check repository if user is logged in
        return firebaseAuthRepository.isLoggedIn();
    }
    /** When the Activity is restarted livedata erroneously sends an update to the observers.
     * This is crucial as the activity listens for updates to call showSnackError()
     * Stop this behaviour by setting the value to null as the app only checks for true/false.
     * This is bad for null safety in general, however in this case it should be perfectly fine.
     */
    public void resetLiveData(){
        //set value does not trigger the observers.
        mutableSuccess.setValue(null);
    }

    /** Quality of life function that clears the sign up screen variables and assigns a value to
     * loginUsername, so that the username textbox is pre-filled for the user to sign in.
     */
    public void onSignUpSuccess(){
        this.loginUsername = this.Email;
        this.Email = "";
        this.Username = "";
        this.Name = "";
        this.agreedToTerms = false;
    }

}


