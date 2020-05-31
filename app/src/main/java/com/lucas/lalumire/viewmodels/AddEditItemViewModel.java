package com.lucas.lalumire.viewmodels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.repositories.FirestoreRepository;

import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.MediaFile;

public class AddEditItemViewModel extends ViewModel {
    // true = add, false = edit;
    public Boolean isAddNotEdit;
    public Bitmap image1;
    public Bitmap image2;
    public Bitmap image3;
    public Bitmap image4;
    public String Title;
    public String Category;
    public boolean isUsed;
    public double Price;
    public int Stock;
    public String Description;
    public String Location;
    public List<String> categories;
    public List<String> paymentTypes;
    public List<String> procurementTypes;
    public int categoriesSelectedIndex;
    public int paymentTypesSelectedIndex;
    public int procurementTypesSelectedIndex;
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

    public LiveData<List<String>> getCategories(){
        final MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post for the observers down the line
                mutableLiveData.postValue(firestoreRepository.getCategories());
            }
        }).start();
        return mutableLiveData;
    }
    public LiveData<List<String>> getPaymentTypes(){
        final MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post for the observers down the line
                mutableLiveData.postValue(firestoreRepository.getPaymentTypes());
            }
        }).start();
        return mutableLiveData;
    }
    public LiveData<List<String>> getProcurementTypes(){
        final MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post for the observers down the line
                mutableLiveData.postValue(firestoreRepository.getProcurementTypes());
            }
        }).start();
        return mutableLiveData;
    }
    public LiveData<Boolean> getAllCombos(){
        final MutableLiveData<Boolean> returnLiveData = new MutableLiveData<>();
        // ensure we are only getting data when it is null
        if(categories !=null && paymentTypes !=null && procurementTypes!=null) {
            // immediately post true if we have all the values
            returnLiveData.postValue(true);
        }else{
            final LiveData<List<String>> categoriesLive = getCategories();
            final LiveData<List<String>> paymentTypesLive = getPaymentTypes();
            final LiveData<List<String>> procurementTypesLive = getProcurementTypes();
            categoriesLive.observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    categories = strings;
                    // when all of them finish observing, post the value to state that we are done.
                    if (categories != null && paymentTypes != null && procurementTypes != null) {
                        returnLiveData.postValue(true);
                        // remove observer to prevent memory leak
                        categoriesLive.removeObserver(this);
                    }
                }
            });
            paymentTypesLive.observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    paymentTypes = strings;
                    if (categories != null && paymentTypes != null && procurementTypes != null) {
                        returnLiveData.postValue(true);
                        // remove observer to prevent memory leak
                        paymentTypesLive.removeObserver(this);
                    }
                }
            });
            procurementTypesLive.observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    procurementTypes = strings;
                    if (categories != null && paymentTypes != null && procurementTypes != null) {
                        returnLiveData.postValue(true);
                        // remove observer to prevent memory leak
                        procurementTypesLive.removeObserver(this);
                    }
                }
            });
        }
        return returnLiveData;
    }

    public LiveData<Boolean> addItem(){
        final MutableLiveData<Boolean> returnLiveData = new MutableLiveData<>();
        // avoid blocking the ui thread
                ArrayList<Bitmap> Images = new ArrayList<>();
                Images.add(image1);
                Images.add(image2);
                Images.add(image3);
                Images.add(image4);
                Item addingItem = new Item(null, Title, null, null,null,0, null, (float)Price, 0f, Description, paymentTypes.get(paymentTypesSelectedIndex), procurementTypes.get(procurementTypesSelectedIndex), categories.get(categoriesSelectedIndex), Stock, null, false,false, isUsed, Location);
                final LiveData<Boolean> actionLiveData = firestoreRepository.addItem(addingItem, Images);
                actionLiveData.observeForever(new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        returnLiveData.postValue(aBoolean);
                        // remove observer to prevent memory leak
                        actionLiveData.removeObserver(this);
                    }
                });
        return returnLiveData;

    }

    /**
     * Function that clears the viewmodel.
     */
    public void clearEverything(){
        image1 = null;
        image2=null;
        image3=null;
        image4=null;
        Title=null;
        Category=null;
        Price=0;
        Stock=0;
        Description=null;
        Location=null;
        categories=null;
        paymentTypes=null;
        procurementTypes=null;
        categoriesSelectedIndex=0;
        paymentTypesSelectedIndex=0;
        procurementTypesSelectedIndex=0;
    }
}
