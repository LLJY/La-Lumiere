package com.lucas.lalumire.Viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.Adapters.CategoryAdapter;
import com.lucas.lalumire.Adapters.SmallItemAdapter;
import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.Repositories.FirestoreRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    //gets the hot items
    public List<Item> listHotItems;
    public List<String> listCategories;
    public List<Item> listFollowItems;
    public CategoryAdapter categoryAdapter;
    public SmallItemAdapter hotItemsAdapter;
    public SmallItemAdapter followingItemsAdapter;
    FirestoreRepository firestoreRepository;

    public HomeViewModel(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
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
                listHotItems = items;
                hotItemsAdapter = new SmallItemAdapter(items);
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
                listCategories = strings;
                categoryAdapter = new CategoryAdapter(strings);
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
                listFollowItems = items;
                followingItemsAdapter = new SmallItemAdapter(items);
                //remove the prevent memory leak
                listMutableItems.removeObserver(this);
            }
        });
        //return so we can observe it for changes in fragment
        return listMutableItems;
    }

}
