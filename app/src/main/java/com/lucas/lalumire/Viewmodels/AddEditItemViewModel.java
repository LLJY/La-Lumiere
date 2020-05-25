package com.lucas.lalumire.Viewmodels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.Repositories.FirestoreRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.aprilapps.easyphotopicker.MediaFile;

public class AddEditItemViewModel extends ViewModel {
    FirestoreRepository firestoreRepository;
    // true = add, false = edit;
    public Boolean isAddNotEdit;
    public ArrayList<File> imageFiles = new ArrayList<>();
    public AddEditItemViewModel(FirestoreRepository firestoreRepository){
        this.firestoreRepository = firestoreRepository;
    }
    public void setMultiple(MediaFile[] images){
        // we can only have a maximum of 4 images
        for (int i = 0; i < 4- imageFiles.size() && i < images.length; i++) {
            imageFiles.add(images[i].getFile());
        }
    }
}
