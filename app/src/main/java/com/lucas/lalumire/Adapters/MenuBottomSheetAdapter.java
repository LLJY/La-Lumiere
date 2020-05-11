package com.lucas.lalumire.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.lalumire.Models.MenuItem;
import com.lucas.lalumire.databinding.BottomSheetRowLayoutBinding;

import java.util.List;

//this is the adapter AND viewholder for the menu
public class MenuBottomSheetAdapter extends RecyclerView.Adapter<MenuBottomSheetAdapter.ItemHolder> {
    //this cannot be null
    List<MenuItem> menuItems;
    //constructor, to pass adapter the list
    public MenuBottomSheetAdapter(@NonNull List<MenuItem> menuItems){
        //assign constructor variables to class variables.
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuBottomSheetAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BottomSheetRowLayoutBinding binding = BottomSheetRowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuBottomSheetAdapter.ItemHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.menuLabel.setText(item.itemName);
        holder.menuImage.setImageResource(item.itemPicture);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
    public class ItemHolder extends RecyclerView.ViewHolder{
        //making use of viewbinding
        private BottomSheetRowLayoutBinding binding;
        private ImageView menuImage;
        private TextView menuLabel;
        public ItemHolder(BottomSheetRowLayoutBinding binding){
            //pass it the view from binding
            super(binding.getRoot());
            menuImage = binding.menuImage;
            menuLabel = binding.menuTitle;
        }

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
