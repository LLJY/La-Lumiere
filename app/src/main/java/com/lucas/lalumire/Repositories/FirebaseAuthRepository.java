package com.lucas.lalumire.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class FirebaseAuthRepository {
    private FirebaseAuth mAuth;
    public FirebaseAuthRepository(){
        mAuth = FirebaseAuth.getInstance();
    }
    public MutableLiveData<Boolean> SignUp(String email, String password, String name, final String username){
        final MutableLiveData<Boolean> isSuccessfulLiveData = new MutableLiveData<>();
        Log.d("asd", "he");
        mAuth.signOut();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    assert user != null;

                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //sign out so user has to sign in again.
                            mAuth.signOut();
                            Log.d("a", String.valueOf(task.isSuccessful()));
                            //post the value of isSuccessful to the observers
                            isSuccessfulLiveData.postValue(task.isSuccessful());
                        }
                    });
                }else{
                    isSuccessfulLiveData.postValue(false);
                }
            }

        });
        //let ViewModel deal with the business logic when onComplete returns successful or not
        return isSuccessfulLiveData;
    }
    public MutableLiveData<Boolean> Login(String email, String password){
        final MutableLiveData<Boolean> isSuccessfulLiveData = new MutableLiveData<>();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //post the value of isSuccessful to the observers
                isSuccessfulLiveData.postValue(task.isSuccessful());
            }
        });
        return isSuccessfulLiveData;

    }
}
