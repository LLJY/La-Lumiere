package com.lucas.lalumire.viewmodels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.repositories.FirestoreRepository;

import pl.aprilapps.easyphotopicker.MediaFile;

public class AddEditItemViewModel extends ViewModel {
    // true = add, false = edit;
    public Boolean isAddNotEdit;
    public Bitmap image1;
    public Bitmap image2;
    public Bitmap image3;
    public Bitmap image4;
    FirestoreRepository firestoreRepository;

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

    public void setImage(int i, String path) {
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
