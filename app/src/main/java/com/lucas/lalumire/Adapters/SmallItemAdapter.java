package com.lucas.lalumire.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.lalumire.Models.Item;
import com.lucas.lalumire.databinding.SmallItemCardBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SmallItemAdapter extends RecyclerView.Adapter<SmallItemAdapter.SmallCardViewHolder> {
    List<Item> itemsList;
    public SmallItemAdapter(List<Item> items){
        this.itemsList = items;
    }
    @NonNull
    @Override
    public SmallCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return the view holder
        return new SmallCardViewHolder(SmallItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SmallCardViewHolder holder, int position) {
        Item item = itemsList.get(position);
        //use picasso to asynchronously load the images
        Picasso.get().load(item.images.get(0)).into(holder.itemImage);
        holder.itemNameText.setText(item.Title);
        Picasso.get().load(item.sellerImageURL).into(holder.sellerProfileImage);
        //it will look like "Likes 14"
        holder.likedText.setText("Likes"+String.valueOf(item.Likes));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class SmallCardViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemNameText;
        TextView likedText;
        ImageView sellerProfileImage;
        ImageView likeImage;

        public SmallCardViewHolder(@NonNull SmallItemCardBinding binding) {
            super(binding.getRoot());
            //make use of view binding
            itemImage = binding.itemImage;
            itemNameText = binding.itemNameText;
            likedText = binding.likesAmountText;
            sellerProfileImage = binding.itemProfileImage;
            likeImage = binding.likeImage;
        }
    }
}