package com.lucas.lalumire.Adapters;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.lucas.lalumire.Models.Item;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback{

    List<Item> oldItems;
    List<Item> newItems;

    public DiffUtilCallback(List<Item> newItems, List<Item> oldItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // ListingID is always unique
        return oldItems.get(oldItemPosition).ListingID == newItems.get(newItemPosition).ListingID;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
