package com.lucas.lalumire.Repositories;

import android.net.Uri;
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
import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.Models.UserType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kotlin.Lazy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                        try {
                            if (task.isSuccessful()) {
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
                                switch (userTypeString) {
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
                                User user = new User(uid, name, Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(), userType);
                                returnLiveData.postValue(user);

                            } else {
                                //post null so we can check for it later on error.
                                returnLiveData.postValue(null);
                            }
                            //sometimes if the document is somehow deleted, it will return a list of size 0, causing this exception
                        } catch (IndexOutOfBoundsException e) {
                            //TODO show the user an error
                            returnLiveData.postValue(null);
                        }
                    }
                });
        //cast to LiveData, we should never expose the mutable livedata
        return (LiveData)returnLiveData;
    }

    /**
     * Get the list of items from firebase cloud store MUST BE RUN ASYNCHRONOUSLY!
     * @return
     */
    public List<Item> getItems(){

        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getUserItems";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try{
            //get this synchronously, we will do it asynchronously in ViewModel
            Response response = client.newCall(request).execute();
            JSONArray jsonArray = new JSONArray(response.toString());
            ArrayList<Item> listOfItems = new ArrayList<>();
            for(int i =0; i<jsonArray.length(); i++){
                ArrayList<String> itemImages = new ArrayList<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray imagesArray = jsonObject.getJSONArray("Images");
                //parse the images, i is taken, so use l
                for(int l=0; l<imagesArray.length(); l++){
                    String imageURL = imagesArray.getString(l);
                    itemImages.add(imageURL);
                }
                Item item = new Item(jsonObject.getString("Title"), jsonObject.getString("sellerName"),jsonObject.getString("sellerUID"), Uri.parse(jsonObject.getString("sellerImageURL")),jsonObject.getInt("Likes"),LocalDateTime.parse(jsonObject.getString("ListedTime")), (float) jsonObject.getDouble("Rating"),jsonObject.getString("Description"),jsonObject.getString("TransactionInformation"),jsonObject.getString("ProcurementInformation"),jsonObject.getString("Category"), jsonObject.getInt("Stock"), itemImages, jsonObject.getBoolean("isAdvert"));
                //add the item to the list.
                listOfItems.add(item);
            }
            return listOfItems;
        }catch(Exception e){

        }

        return null;
    }
}