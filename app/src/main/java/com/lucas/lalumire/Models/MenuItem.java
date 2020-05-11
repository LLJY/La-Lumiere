package com.lucas.lalumire.Models;

import androidx.fragment.app.Fragment;

//this is the object for menu items.
public class MenuItem {

    public Class itemFragment;
    public String itemName;
    public int itemPicture;

    public MenuItem(Class itemFragment, String itemName, int itemPicture) {
        this.itemFragment = itemFragment;
        this.itemName = itemName;
        this.itemPicture = itemPicture;
    }

    public Class getItemFragment() {
        return itemFragment;
    }

    public void setItemFragment(Class itemFragment) {
        this.itemFragment = itemFragment;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(int itemPicture) {
        this.itemPicture = itemPicture;
    }

}
