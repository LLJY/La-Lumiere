package com.lucas.lalumire.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.lucas.lalumire.R;
import com.lucas.lalumire.databinding.SlideImageBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemImagesSliderAdapter extends SliderViewAdapter<ItemImagesSliderAdapter.SliderAdapterVH> {
    public List<String> images;
    public ItemImagesSliderAdapter(List<String> images){
        this.images = images;
    }
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        // create the viewbinding
        return new SliderAdapterVH(SlideImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        // asynchronously load the images from firestore
        Picasso.get().load(images.get(position)).into(viewHolder.image);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    class SliderAdapterVH extends com.smarteist.autoimageslider.SliderViewAdapter.ViewHolder{
        ImageView image;
        ConstraintLayout parentLayout;
        public SliderAdapterVH(SlideImageBinding binding) {
            // view binding
            super(binding.getRoot());
            image = binding.image;
            parentLayout = binding.parentLayout;
        }
    }
}
