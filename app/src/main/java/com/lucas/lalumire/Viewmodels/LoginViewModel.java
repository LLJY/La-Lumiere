package com.lucas.lalumire.Viewmodels;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.Repositories.FirebaseAuthRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LoginViewModel extends ViewModel {
    private @NonNull
    FirebaseAuthRepository firebaseAuthRepository;
    private MutableLiveData<Boolean> mutableSuccess = new MutableLiveData<>();
    //store variables
    public String Email = "";
    public String Username = "";
    public String Name = "";
    public boolean agreedToTerms = false;
    //determines if currently on the signup screen;
    public boolean isSignUp = false;
    /* NEVER NEVER NEVER expose MutableLiveData, so expose a LiveData version of it.
     * This is supposed to look like val livedata : LiveData get() = mutableSuccess
     * But the school is retarded and we can't use Kotlin, so deal with the stupid fucking boilerplates.
     */
    public LiveData getLiveData() {
        return this.mutableSuccess;
    }



    //constructor includes an instance of the authentication repository.
    public LoginViewModel(@NotNull FirebaseAuthRepository firebaseRepo) {
        this.firebaseAuthRepository = firebaseRepo;
    }

    public void SignUp(final String email, final String password, final String name, final String username) {
        //set the livedata to the one returned by the function to observe for errors
        new Thread(new Runnable() {
            @Override
            public void run() {
                final LiveData<Boolean> result = firebaseAuthRepository.SignUp(email, password, name, username);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result.observeForever(new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                mutableSuccess.postValue(aBoolean);
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
        /* Okay seriously, threading is fucking cancer and garbage in Java. Coroutines are much better in kotlin.
         * This seriously affects efficiency and performance. Not to fucking mention that we are going to write this in TS/Ionic
         */
        new Thread(new Runnable() {
            @Override
            //run the login method in a thread to avoid blocking the ui
            public void run() {
                final LiveData<Boolean> result = firebaseAuthRepository.Login(email, password);
                //observe on the mainthread as you cannot run observeForever on a background thread.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result.observeForever(new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                mutableSuccess.postValue(aBoolean);
                                //we don't need to observe it anymore
                                result.removeObserver(this);
                            }
                        });
                    }
                });
            }
        }).start();
        //same code in kt...
//        livedata = async{
//            firebaseAuthRepository.Login(username, password)
//        }.await()
//        liveDataArrayList.add(livedata)
//        livedata.observeForever(new Observer{
//            mutableSuccess.postValue(it)
//            liveDataArrayList.remove(it)
//        }


    }
    /* When the Activity is restarted livedata erroneously sends an update to the observers.
     * This is crucial as the activity listens for updates to call showSnackError()
     * Stop this behaviour by setting the value to null as the app only checks for true/false.
     * This is bad for null safety in general, however in this case it should be perfectly fine.
     */
    public void resetLiveData(){
        //set value does not trigger the observers.
        mutableSuccess.setValue(null);
    }

}


