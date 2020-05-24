package com.lucas.lalumire.Viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lucas.lalumire.Repositories.FirestoreRepository;

public class ManageListingsViewModel extends ViewModel {
    FirestoreRepository firestoreRepository;
    public ManageListingsViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
    }
}
