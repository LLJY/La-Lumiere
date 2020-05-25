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
    public Bitmap image1;
    public Bitmap image2;
    public Bitmap image3;
    public Bitmap image4;

    public AddEditItemViewModel(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
    }

    public void setMultiple(MediaFile[] images) {
        // we can only have a maximum of 4 images
        for (int i = 0; i < 4 && i < images.length; i++) {
            String file = images[i].getFile().getPath();
            setImage(i, file);
        }
    }

    public void setImage(int i, String path){
        switch (i) {
            case 0:
                // include fiter to increase quality
                image1 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), 512, 512, true);
                break;
            case 1:
                // include fiter to increase quality
                image2 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), 512, 512, true);
                break;
            case 2:
                // include fiter to increase quality
                image3 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), 512, 512, true);
                break;
            case 3:
                // include fiter to increase quality
                image4 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), 512, 512, true);
                break;
        }
    }
}
