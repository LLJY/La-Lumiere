package com.lucas.lalumire.Viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lucas.lalumire.Adapters.BigItemAdapter;
import com.lucas.lalumire.Adapters.SmallItemAdapter;
import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.Repositories.FirestoreRepository;

import java.util.List;

public class ManageListingsViewModel extends ViewModel {
    FirestoreRepository firestoreRepository;
    FirebaseAuth mAuth;
    MutableLiveData<List<Item>> listItemsLive = new MutableLiveData<>();
    public BigItemAdapter bigItemAdapter;
    public ManageListingsViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
        mAuth = FirebaseAuth.getInstance();
        getSellerItems();
    }

    public LiveData<List<Item>> getListItemsLive() {
        return listItemsLive;
    }

    public LiveData<List<Item>> getSellerItems(){
        final MutableLiveData<List<Item>> listMutableItems =  new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //let fragment know that the value is ready
                listMutableItems.postValue(firestoreRepository.getSellerItems());
            }
        }).start();
        listMutableItems.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                // if adapter is null, create it, otherwise, just update it.
                Log.d("testing", "again");
                if(bigItemAdapter == null) {
                    bigItemAdapter = new BigItemAdapter(items, mAuth.getUid());
                }else{
                    bigItemAdapter.updateList(items);
                }
                listItemsLive.postValue(items);
                //remove the prevent memory leak
                listMutableItems.removeObserver(this);
            }
        });
        //return so we can observe it for changes in fragment
        return listMutableItems;
    }
}
