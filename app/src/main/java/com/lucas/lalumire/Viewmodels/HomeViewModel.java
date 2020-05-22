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
    MutableLiveData<List<Item>> listMutableItems = new MutableLiveData<>();
    FirestoreRepository firestoreRepository;
    public HomeViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
    }
    public LiveData<List<Item>> getHotItems(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //let fragment know that the value is ready
                listMutableItems.postValue(firestoreRepository.getHottestItems());
            }
        }).start();
        //return so we can observe it for changes in fragment
        return listMutableItems;
    }

}
