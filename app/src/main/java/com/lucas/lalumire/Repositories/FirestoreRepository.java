package com.lucas.lalumire.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.Models.UserType;

import java.util.HashMap;
import java.util.Objects;

import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.inject;

public class FirestoreRepository{
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    //let koin inject the instances for us
    public FirestoreRepository(FirebaseFirestore firestore, FirebaseAuth auth){
        db = firestore;
        mAuth = auth;
    }
    public LiveData<User> getUserInfo(){
        Log.d("firebaserepo", "getting User");
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        final MutableLiveData<User> returnLiveData = new MutableLiveData<>();
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("UID", uid);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.isSuccessful()){
                             //there is only one record expected so, select the first value in the array
                             DocumentSnapshot document = Objects.requireNonNull(task.getResult()).getDocuments().get(0);
                             String uid = document.getString("UID");
                             String name = document.getString("Name");
                             String userTypeString = document.getString("Type");
                             UserType userType;
                             //null safety
                             assert userTypeString != null;
                             //so any fuck ups in case will be spotted.
                             userTypeString = userTypeString.toUpperCase();
                             switch (userTypeString){
                                 case "ADMIN":
                                     userType = UserType.ADMIN;
                                     break;
                                 case "BUYER":
                                     userType = UserType.BUYER;
                                     break;
                                 case "SELLER":
                                     userType = UserType.SELLER;
                                     break;
                                 default:
                                     throw new IllegalStateException("Unexpected value: " + userTypeString);
                             }
                             //create the new user object and post it to livedata.
                             User user = new User(uid,name, Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(),userType);
                             returnLiveData.postValue(user);

                         }else{
                             //post null so we can check for it later on error.
                            returnLiveData.postValue(null);
                         }
                    }
                });
        //cast to LiveData, we should never expose the mutable livedata
        return (LiveData)returnLiveData;
    }
}