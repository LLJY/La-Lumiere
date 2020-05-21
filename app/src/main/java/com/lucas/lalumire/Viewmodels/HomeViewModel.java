package com.lucas.lalumire.Viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.Repositories.FirestoreRepository;

import java.util.List;
import java.util.logging.Handler;

public class HomeViewModel extends ViewModel {
    //gets the hot items
    FirestoreRepository firestoreRepository;
    public HomeViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
    }
    public LiveData<List<Item>> getHotItems(){
        final MutableLiveData<List<Item>> mutableItems = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //let fragment know that the value is ready
                mutableItems.postValue(firestoreRepository.getItems());
            }
        }).start();
        //return so we can observe it for changes in fragment
        return mutableItems;
    }

}
