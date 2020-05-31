package com.lucas.lalumire.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lucas.lalumire.R;
import com.lucas.lalumire.adapters.ItemImagesSliderAdapter;
import com.lucas.lalumire.databinding.ActivityItemBinding;
import com.lucas.lalumire.models.Item;
import com.lucas.lalumire.viewmodels.ItemViewModel;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import kotlin.Lazy;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;

public class ItemActivity extends AppCompatActivity {
    ActivityItemBinding binding;
    Lazy<ItemViewModel> viewModelLazy = viewModel(this, ItemViewModel.class);
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // if null, set the item to viewmodel and we will fetch it later.
        if(viewModelLazy.getValue().item == null){
            // cast to item
            viewModelLazy.getValue().item = (Item) getIntent().getSerializableExtra("Item");
        }
        Item item = viewModelLazy.getValue().item;
        // setup the images slider.
        binding.imageSlider.setSliderAdapter(new ItemImagesSliderAdapter(viewModelLazy.getValue().item.images));
        binding.imageSlider.startAutoCycle();
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        // set the title, seller name and seller image
        binding.titleText.setText(item.Title);
        binding.sellerNameText.setText(item.sellerName);
        binding.collapseToolbar.setTitle(item.sellerName);
        Picasso.get().load(item.sellerImageURL).into(binding.sellerImage);
        // set the rest of the ui
        binding.likesAmountText.setText(String.valueOf(item.Likes) +" Likes");
        binding.usedText.setText(item.isUsed ? "Used" : "New");
        binding.categoryNameText.setText(item.Category);
        binding.descText.setText(item.Description);
        binding.paymentText.setText(item.TransactionInformation);
        binding.locationText.setText(item.Location);
        // set bottom sheet onclick and other stuff
        binding.bottomSheet.menuLabel.setText("Chat and edit buttons not implemented");
        binding.bottomSheet.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemActivity.this.onBackPressed();
            }
        });


    }
}
