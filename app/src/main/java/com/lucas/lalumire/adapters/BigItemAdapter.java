package com.lucas.lalumire.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.R;
import com.lucas.lalumire.databinding.BigItemCardBinding;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class BigItemAdapter extends RecyclerView.Adapter<BigItemAdapter.BigItemViewHolder> {
    private List<Item> itemList;
    private String userID;
    private MutableLiveData<Item> cardViewClickedItem = new MutableLiveData<>();
    private MutableLiveData<Item> itemEditButtonClickedItem = new MutableLiveData<>();
    private MutableLiveData<Item> itemLikeButtonClickedItem = new MutableLiveData<>();
    private MutableLiveData<Item> itemUnLikeButtonClickedItem = new MutableLiveData<>();
    public BigItemAdapter(List<Item> items, String userID) {
        this.itemList = items;
        this.userID = userID;
    }

    public LiveData<Item> getCardViewClickedItem() {
        return cardViewClickedItem;
    }

    public LiveData<Item> getItemEditButtonClickedItem() {
        return itemEditButtonClickedItem;
    }

    public LiveData<Item> getItemLikeButtonClickedItem() {
        return itemLikeButtonClickedItem;
    }

    public LiveData<Item> getItemUnlikeButtonClickedItem(){return itemUnLikeButtonClickedItem; }

    /**
     * Function that uses Diffutil to update only parts of the recyclerview list,
     * useful as BigItemAdapter is only used on the bigger parts of the app.
     *
     * @param newList
     */
    public void updateList(List<Item> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(this.itemList, newList));
        // update to the new list
        this.itemList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public BigItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BigItemViewHolder(BigItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BigItemViewHolder holder, int position) {
        final Item item = itemList.get(position);
        holder.itemNameText.setText(item.Title);
        holder.itemNameText.setSelected(true);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        holder.itemPriceText.setText(format.format(item.Price));
        Picasso.get().load(item.sellerImageURL).into(holder.itemSellerImage);
        Picasso.get().load(item.images.get(0)).into(holder.itemImage);
        holder.itemSellerName.setText(item.sellerName);
        holder.itemStock.setText(String.valueOf(item.Stock)+" in Stock");
        if (!userID.equals(item.sellerUID)) {
            holder.itemEditButton.setVisibility(View.GONE);
            item.isLiked = false;
        }
        if (item.isLiked) {
            holder.itemLikeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
            item.isLiked = true;
        }
        // set on click listeners
        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // notify any observers in fragment/activity and give them the item.
                cardViewClickedItem.postValue(item);
            }
        });
        holder.itemEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // notify any observers in fragment/activity and give them the item.
                itemEditButtonClickedItem.postValue(item);
            }
        });
        holder.itemLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isLiked){
                    holder.itemLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    item.isLiked = false;
                    item.Likes--;
                    itemUnLikeButtonClickedItem.postValue(item);
                }else{
                    holder.itemLikeButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                    item.isLiked = true;
                    item.Likes++;
                    itemLikeButtonClickedItem.postValue(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class BigItemViewHolder extends RecyclerView.ViewHolder {
        CardView mainCardView;
        TextView itemNameText;
        TextView itemPriceText;
        ImageView itemSellerImage;
        ImageView itemImage;
        TextView itemSellerName;
        TextView itemStock;
        ImageButton itemEditButton;
        ImageButton itemLikeButton;

        public BigItemViewHolder(@NonNull BigItemCardBinding binding) {
            super(binding.getRoot());
            mainCardView = binding.mainItemCard;
            itemNameText = binding.itemName;
            itemPriceText = binding.itemPrice;
            itemSellerImage = binding.itemSellerImage;
            itemImage = binding.itemImage;
            itemSellerName = binding.itemSellerName;
            itemStock = binding.itemStockLabel;
            itemEditButton = binding.itemEditButton;
            itemLikeButton = binding.itemLikeButton;
        }
    }
}
