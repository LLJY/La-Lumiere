package com.lucas.lalumire.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.lucas.lalumire.adapters.CategoryAdapter;
import com.lucas.lalumire.adapters.SmallItemAdapter;
import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.repositories.FirestoreRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    //gets the hot items
    private MutableLiveData<List<Item>> listHotItems = new MutableLiveData<>();
    private MutableLiveData<List<String>>  listCategories = new MutableLiveData<>();
    private MutableLiveData<List<Item>>  listFollowItems = new MutableLiveData<>();
    private MutableLiveData<List<Item>>  listSuggestedItems = new MutableLiveData<>();
    public CategoryAdapter categoryAdapter;
    public SmallItemAdapter hotItemsAdapter;
    public SmallItemAdapter followingItemsAdapter;
    public SmallItemAdapter suggestedItemsAdapter;
    FirestoreRepository firestoreRepository;

    public HomeViewModel(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
        observeDataSets();
    }
    // getters for live data
    public LiveData<List<Item>> getHotItemsLiveData(){
        return listHotItems;
    }

    public LiveData<List<String>> getCategoriesLiveData(){
        return listCategories;
    }

    public LiveData<List<Item>> getFollowItemsLiveData(){
        return listFollowItems;
    }

    public LiveData<List<Item>> getSuggestedItemsLiveData(){
        return listSuggestedItems;
    }

    public LiveData<List<Item>> getHotItems() {
        final MutableLiveData<List<Item>> listMutableItems = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //let fragment know that the value is ready
                listMutableItems.postValue(firestoreRepository.getHottestItems());
            }
        }).start();
        listMutableItems.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                hotItemsAdapter = new SmallItemAdapter(items);
                listHotItems.postValue(items);
                //remove the prevent memory leak
                listMutableItems.removeObserver(this);
            }
        });
        //return so we can observe it for changes in fragment
        return listMutableItems;
    }

    public LiveData<List<String>> getCategories() {
        final MutableLiveData<List<String>> mutableCategories = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //observe and assign value automatically so activity can check if the string exists.
                //notify the observers in activity or fragment
                mutableCategories.postValue(firestoreRepository.getCategories());
            }
        }).start();
        mutableCategories.observeForever(new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                categoryAdapter = new CategoryAdapter(strings);
                listCategories.postValue(strings);
                //remove the observer to prevent memory leak
                mutableCategories.removeObserver(this);
            }
        });
        return mutableCategories;
    }

    public LiveData<List<Item>> getFollowingItems() {
        final MutableLiveData<List<Item>> listMutableItems = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //let fragment know that the value is ready
                listMutableItems.postValue(firestoreRepository.getFollowingItems());
            }
        }).start();
        listMutableItems.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                followingItemsAdapter = new SmallItemAdapter(items);
                listFollowItems .postValue(items);
                //remove the prevent memory leak
                listMutableItems.removeObserver(this);
            }
        });
        //return so we can observe it for changes in fragment
        return listMutableItems;
    }

    public LiveData<List<Item>> getSuggestedItems(){
        final MutableLiveData<List<Item>> listMutableItems =  new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //let fragment know that the value is ready
                listMutableItems.postValue(firestoreRepository.getSuggestedItems());
            }
        }).start();
        listMutableItems.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                suggestedItemsAdapter = new SmallItemAdapter(items);
                listSuggestedItems.postValue(items);
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
                getFollowingItems();
                getHotItems();
                getSuggestedItems();
            }
        });
        // this observes for category changes
        db.collection("Categories").addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("update!!!", "update");
                getCategories();
            }
        });
    }

}
