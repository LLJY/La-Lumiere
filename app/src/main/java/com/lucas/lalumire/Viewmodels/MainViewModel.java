package com.lucas.lalumire.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.Repositories.FirebaseAuthRepository;
import com.lucas.lalumire.Repositories.FirestoreRepository;

/** This is called the MainViewModel as it should contain the information that is shared between all fragments.
 * this should be the largest block of Business logic through out the entire app and everything should reside here.
 * Fragment/Screen specific variables will be kept in their own viewmodels.
 */
public class MainViewModel extends AndroidViewModel {
    //should be injected by koin
    private FirestoreRepository firestoreRepository;
    //logged in user's details
    private MutableLiveData<User> mutableLoggedInUser = new MutableLiveData<>();
    //getter method to provide LiveData version of mutableLoggedInUser
    public LiveData getUserLiveData(){
        return this.mutableLoggedInUser;
    }
    public MainViewModel(@NonNull Application application, @NonNull final FirestoreRepository firestoreRepository) {
        super(application);
        this.firestoreRepository = firestoreRepository;
        //automatically get the loggedInUser
        firestoreRepository.getUserInfo().observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //post the value to activity
                mutableLoggedInUser.postValue(user);
            }
        });
    }
    public void getUserInfo(){
        //post true when done so activity can fetch the data from viewmodel.
        final LiveData<User> observable = firestoreRepository.getUserInfo();
        observable.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User a) {
                //post value to observer in mainactivity
                mutableLoggedInUser.postValue(a);
                //we don't need this observer anymore, remove it.
                observable.removeObserver(this);

            }
        });
    }
}
