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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lucas.lalumire.Models.LoginActivityStatus;
import com.google.firebase.auth.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FirebaseAuthRepository {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    public FirebaseAuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<LoginActivityStatus> SignUp(String email, String password, final String name, final String username) {
        final MutableLiveData<LoginActivityStatus> isSuccessfulLiveData = new MutableLiveData<>();
        //sign out if any user is signed in.
        mAuth.signOut();
        //start create user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //it is redundant to continue with any of this if login is unsuccesful.
                if (task.isSuccessful()) {
                    //firebase automatically logs in when sign up is successful, so get current user
                    FirebaseUser user = mAuth.getCurrentUser();
                    //update the user profile to include username
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    //null safety
                    assert user != null;
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //add name and user type to firestore
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("Type", "Buyer");
                            user.put("UID", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                            mStore.collection("users")
                                    .add(user)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Log.d("a", String.valueOf(task.isSuccessful()));
                                            //sign out so user has to sign in again.
                                            mAuth.signOut();
                                            if (task.isSuccessful()) {
                                                isSuccessfulLiveData.postValue(LoginActivityStatus.STATUS_SIGN_UP_SUCCESS);
                                            } else {
                                                isSuccessfulLiveData.postValue(LoginActivityStatus.STATUS_ERR);
                                            }

                                        }
                                    });
                        }
                    });
                } else {
                    String errorCode = "";
                    LoginActivityStatus returnCode;
                    try {
                        //throw the firebase exceptions so we can catch it and display a correct error to the user
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        returnCode = LoginActivityStatus.STATUS_WEAK_PASSWORD;
                    }catch(FirebaseAuthUserCollisionException e){
                        returnCode = LoginActivityStatus.STATUS_ACCOUNT_EXISTS;
                    }catch(Exception e){
                        returnCode = LoginActivityStatus.STATUS_ERR;
                    }
                    isSuccessfulLiveData.postValue(returnCode);
                }
            }

        });
        //let ViewModel deal with the business logic when onComplete returns successful or not
        return isSuccessfulLiveData;
    }

    public MutableLiveData<LoginActivityStatus> Login(String email, String password) {
        final MutableLiveData<LoginActivityStatus> isSuccessfulLiveData = new MutableLiveData<>();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String errorCode = "";
                LoginActivityStatus returnCode = LoginActivityStatus.STATUS_LOADING;
                if (!task.isSuccessful()) {
                    try {
                        //throw the firebase exceptions so we can catch it and display a correct error to the user
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        returnCode = LoginActivityStatus.STATUS_INVALID_CREDENTIALS;
                    } catch (FirebaseAuthInvalidUserException e) {
                        returnCode = LoginActivityStatus.STATUS_INVALID_USER;
                    } catch (Exception e) {
                        returnCode = LoginActivityStatus.STATUS_ERR;
                    }

                } else {
                    returnCode = LoginActivityStatus.STATUS_LOGIN_SUCCESS;
                }
                isSuccessfulLiveData.postValue(returnCode);
            }
        });
        return isSuccessfulLiveData;

    }
//    public User getCurrentUser(){
//
//    }
}
