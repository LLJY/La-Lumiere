package com.lucas.lalumire.repositories;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.models.User;
import com.lucas.lalumire.models.UserType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirestoreRepository {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    //let koin inject the instances for us
    public FirestoreRepository(FirebaseFirestore firestore, FirebaseAuth auth) {
        db = firestore;
        mAuth = auth;
    }

    public LiveData<User> getUserInfo() {
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
        return returnLiveData;
    }

    /**
     * Get the list of items from firebase cloud store MUST BE RUN ASYNCHRONOUSLY!
     *
     * @return
     */
    public List<Item> getHottestItems() {
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getHottestItems";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userID", mAuth.getCurrentUser().getUid())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        List<Item> listOfItems = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            //convert the response to a list of items
            listOfItems = processItemJson(response.body().string());
            if (listOfItems == null) {
                // try again
                getHottestItems();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfItems;

    }

    /**
     * Get the list of items from firebase cloud store MUST BE RUN ASYNCHRONOUSLY!
     * specifically the list of items from the sellers that the user follows
     *
     * @return
     */
    public List<Item> getFollowingItems() {
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getItemByFollowed";
        OkHttpClient client = new OkHttpClient();
        //send request with current userID as a parameter
        RequestBody formBody = new FormBody.Builder()
                .add("userID", mAuth.getCurrentUser().getUid())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        List<Item> listOfItems = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            //convert the response to a list of items
            listOfItems = processItemJson(response.body().string());
            if (listOfItems == null) {
                // try again
                getFollowingItems();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfItems;

    }

    /**
     * This is for the "Thing you might like" section of the app
     *
     * @return
     */
    public List<Item> getSuggestedItems() {
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getItemBySuggestion";
        OkHttpClient client = new OkHttpClient();
        //send request with current userID as a parameter
        RequestBody formBody = new FormBody.Builder()
                .add("userID", mAuth.getCurrentUser().getUid())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        List<Item> listOfItems = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            listOfItems = processItemJson(response.body().string());
            if (listOfItems == null) {
                // try again
                getSuggestedItems();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfItems;

    }

    private List<Item> processItemJson(String responseBody) {
        ArrayList<Item> returnList = new ArrayList<>();
        try {
            //convert the response body to a json array
            JSONArray itemArray = new JSONArray(responseBody);
            for (int i = 0; i < itemArray.length(); i++) {
                ArrayList<String> itemImages = new ArrayList<>();
                JSONObject jsonObject = itemArray.getJSONObject(i);
                JSONArray imagesArray = jsonObject.getJSONArray("Images");
                //parse the images, i is taken, so use l
                for (int l = 0; l < imagesArray.length(); l++) {
                    String imageURL = imagesArray.getString(l);
                    itemImages.add(imageURL);
                }
                Item item = new Item(jsonObject.getString("ListingID"), jsonObject.getString("Title"), jsonObject.getString("sellerName"), jsonObject.getString("sellerUID"), Uri.parse(jsonObject.getString("sellerImageURL")), jsonObject.getInt("Likes"), LocalDateTime.parse(jsonObject.getString("ListedTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")), (float) jsonObject.getDouble("Price"), (float) jsonObject.getDouble("Rating"), jsonObject.getString("Description"), jsonObject.getString("TransactionInformation"), jsonObject.getString("ProcurementInformation"), jsonObject.getString("Category"), jsonObject.getInt("Stock"), itemImages, jsonObject.getBoolean("isAdvert"), jsonObject.getBoolean("userLiked"));
                //add the item to the list.
                returnList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return returnList;
    }

    public List<String> getCategories() {
        //get from firebase cloud
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getCategories";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ArrayList<String> categoryList = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            JSONArray responseArray = new JSONArray(response.body().string());
            for (int i = 0; i < responseArray.length(); i++) {
                //get the string and add it to the array
                categoryList.add(responseArray.getString(i));
            }
        } catch (Exception e) {
            //just print the error for now, proper error handling will be implemented later.
            e.printStackTrace();
        }
        return categoryList;
    }

    public List<String> getProcurementTypes(){
        //get from firebase cloud
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getProcurementTypes";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ArrayList<String> procurementList = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            JSONArray responseArray = new JSONArray(response.body().string());
            for (int i = 0; i < responseArray.length(); i++) {
                //get the string and add it to the array
                procurementList.add(responseArray.getString(i));
            }
        } catch (Exception e) {
            //just print the error for now, proper error handling will be implemented later.
            e.printStackTrace();
        }
        return procurementList;
    }

    public List<String> getPaymentTypes(){
        //get from firebase cloud
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getPaymentTypes";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ArrayList<String> paymentTypes = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            JSONArray responseArray = new JSONArray(response.body().string());
            for (int i = 0; i < responseArray.length(); i++) {
                //get the string and add it to the array
                paymentTypes.add(responseArray.getString(i));
            }
        } catch (Exception e) {
            //just print the error for now, proper error handling will be implemented later.
            e.printStackTrace();
        }
        return paymentTypes;
    }

    /**
     * function to get the seller's items by the user's uid.
     * for the manage listings page
     */
    public List<Item> getSellerItems() {
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/getSellerItems";
        OkHttpClient client = new OkHttpClient();
        //send request with current userID as a parameter
        RequestBody formBody = new FormBody.Builder()
                .add("userID", mAuth.getCurrentUser().getUid())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        List<Item> listOfItems = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            listOfItems = processItemJson(response.body().string());
            if (listOfItems == null) {
                // try again
                getSellerItems();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfItems;
    }

    public LiveData<Boolean> addItem(final Item item, final List<Bitmap> uploadImages){
        final MutableLiveData<Boolean> returnLivedata = new MutableLiveData<>();
        final ArrayList<String> imageURLs = new ArrayList<>();
        for (int i = 0; i<uploadImages.size() ; i++) {
            //generate a random UUID for the image name.
            UUID uuid = UUID.randomUUID();
            //add a storage reference for the image
            final StorageReference imageRef = storageReference.child("Images"+uuid.toString()+".jpg");
            // just following the firebase tutorial for uploading images
            Bitmap imageBitmap = uploadImages.get(i);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageURLs.add(downloadUri.toString());
                        Log.d("a", downloadUri.toString());
                        if(imageURLs.size()==4){
                            // run the network operation on a seperate thread
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    returnLivedata.postValue(completeAddItem(item, imageURLs));
                                }
                            }).start();
                        }
                    } else {
                        returnLivedata.postValue(false);
                    }
                }
            });
        }
        return returnLivedata;
    }
    private boolean completeAddItem(Item item, List<String> imagesURL){
        String url = "https://asia-east2-la-lumire.cloudfunctions.net/addItem";
        //send request with current userID as a parameter
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userID", mAuth.getCurrentUser().getUid())
                .add("Title", item.Title)
                .add("Category", item.Category)
                .add("Description", item.Description)
                .add("Price", String.valueOf(item.Price))
                .add("ProcurementInformation", item.ProcurementInformation)
                .add("Images", imagesURL.toString())
                .add("Stock", String.valueOf(item.Stock))
                .add("TransactionInformation", String.valueOf(item.TransactionInformation))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            return response.body().string().equals("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}