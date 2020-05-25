package com.lucas.lalumire.Viewmodels;

import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.Repositories.FirestoreRepository;

public class AddEditItemViewModel extends ViewModel {
    FirestoreRepository firestoreRepository;
    public AddEditItemViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
    }
}
