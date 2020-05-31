package com.lucas.lalumire.viewmodels;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.repositories.FirestoreRepository;

public class ItemViewModel extends ViewModel {
    public Item item;
    private FirestoreRepository firestoreRepository;
    public ItemViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
    }

}
