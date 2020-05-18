package com.lucas.lalumire.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.R.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.lalumire.Models.MenuItem;
import com.lucas.lalumire.R;
import com.lucas.lalumire.databinding.BottomSheetRowLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

//this is the adapter AND viewholder for the menu
public class MenuBottomSheetAdapter extends RecyclerView.Adapter<MenuBottomSheetAdapter.ItemHolder> {
    //this cannot be null
    private List<MenuItem> menuItems;
    private int selectedIndex;
    private String accentColor = "#4fc3f7";
    private String defaultTextColor = "#000000";
    private void selectItem(int position){
        selectedIndex = position;
        //update all the fields, bad practice but our recyclerview is pretty small so it should be fine.
        notifyDataSetChanged();

    }
    private MutableLiveData<Class> fragmentClassMLV = new MutableLiveData<Class>();
    public LiveData<Class> getFragmentClassLiveData(){
        return fragmentClassMLV;
    }
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
    public void onBindViewHolder(@NonNull MenuBottomSheetAdapter.ItemHolder holder, final int position) {
        final MenuItem item = menuItems.get(position);
        holder.menuLabel.setText(item.itemName);
        //-1 means the image is unset
        if(item.itemPicture != -1) {
            holder.menuImage.setImageResource(item.itemPicture);
        }else{
            //if unset, it likely means it is the user profile picture, which is a url
            //picasso will get the image asynchronously for us.
            Picasso.get().load(item.itemImageUrl).into(holder.menuImage);
        }
        //post livedata if clicked so activity can handle the click.
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentClassMLV.postValue(item.itemFragment);
                selectItem(position);
            }
        });
        if(selectedIndex == position){
            //highlight the layout
            holder.clickLayout.setBackgroundColor(Color.parseColor(accentColor));
        }else{
            //reset to default parameters
            holder.clickLayout.setBackgroundColor(Color.WHITE);
        }
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
        private ConstraintLayout clickLayout;
        public ItemHolder(BottomSheetRowLayoutBinding binding){
            //pass it the view from binding
            super(binding.getRoot());
            menuImage = binding.menuImage;
            menuLabel = binding.menuTitle;
            clickLayout = binding.clickableLayout;
        }

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
