package com.lucas.lalumire.Models;

import android.net.Uri;

import androidx.fragment.app.Fragment;

//this is the object for menu items.
public class MenuItem {

    public Class itemFragment;
    public String itemName;
    //if it is -1 it is not set.
    public int itemPicture = -1;
    public Uri itemImageUrl;
    public MenuItem(Class itemFragment, String itemName, Uri itemUrl) {
        this.itemFragment = itemFragment;
        this.itemName = itemName;
        itemImageUrl = itemUrl;

    }
    public MenuItem(Class itemFragment, String itemName, int itemPicture) {
        this.itemFragment = itemFragment;
        this.itemName = itemName;
        this.itemPicture = itemPicture;

    }

}
