package com.lucas.lalumire.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.lucas.lalumire.adapters.BigItemAdapter;
import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.repositories.FirestoreRepository;

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
        // observe the data from firestore
        observeDataSets();
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
                if(bigItemAdapter == null) {
                    bigItemAdapter = new BigItemAdapter(items, mAuth.getCurrentUser().getUid());
                }else{
                    bigItemAdapter.updateList(items) ;
                }
                listItemsLive.postValue(items);
                //remove the prevent memory leak
                listMutableItems.removeObserver(this);
            }
        });
        //return so we can observe it for changes in fragment
        return listMutableItems;
    }

    /**
     * This function observes firebase for data changes and reloads recyclerview data if anything changes.
     */
    private void observeDataSets(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // this observes for items changes
        db.collectionGroup("Items").addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                getSellerItems();
            }
        });
    }
}
