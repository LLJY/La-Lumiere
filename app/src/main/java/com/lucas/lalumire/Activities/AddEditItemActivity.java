package com.lucas.lalumire.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.AddEditItemViewModel;
import com.lucas.lalumire.databinding.ActivityAddItemBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.Observer;

import kotlin.Lazy;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;
import static org.koin.java.KoinJavaComponent.inject;

public class AddEditItemActivity extends AppCompatActivity {
    private Lazy<AddEditItemViewModel> addEditItemViewModelLazy = viewModel(this, AddEditItemViewModel.class);
    ActivityAddItemBinding binding;
    private EasyImage easyImage;
    private Observer imageObserver;
    private int selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        easyImage = new EasyImage.Builder(this)
                .allowMultiple(true)
                .setChooserTitle("Choose your Image")
                .build();
        if (getIntent() != null) {
            addEditItemViewModelLazy.getValue().isAddNotEdit = Objects.requireNonNull(getIntent().getExtras()).getBoolean("Add Not Edit");
        }
        if (addEditItemViewModelLazy.getValue().isAddNotEdit) {
            // do not show the delete button if it is not editing
            binding.deleteButton.setVisibility(View.GONE);
        } else {
            // TODO make Item model serializable and get it here.
        }
        // set on click listeners
        binding.Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // open the imagepicker
                easyImage.openChooser(AddEditItemActivity.this);
                selectedImage = 0;
            }
        });
        binding.Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyImage.openChooser(AddEditItemActivity.this);
                selectedImage = 1;
            }
        });
        binding.Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyImage.openChooser(AddEditItemActivity.this);
                selectedImage = 2;
            }
        });
        binding.Image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyImage.openChooser(AddEditItemActivity.this);
                selectedImage = 3;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(@NotNull MediaFile[] imageFiles, @NotNull MediaSource source) {
                // call the method from viewmodel to set the images
                addEditItemViewModelLazy.getValue().setMultiple(imageFiles);
                // the list will never exceed 4 anyway
                if (imageFiles.length > 1) {
                    for (int i = 0; i < addEditItemViewModelLazy.getValue().imageFiles.size(); i++) {
                        // set the imageview images
                        File file = addEditItemViewModelLazy.getValue().imageFiles.get(i);
                       setImages(i, file);
                    }
                }else{
                    // we only selected one image so set the image to the file.
                    setImages(selectedImage, imageFiles[0].getFile());
                }
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                Snackbar.make(binding.coordinator, "An Error has occured", Snackbar.LENGTH_SHORT);
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }
    private void setImages(int i, File file){
        switch (i) {
            case 0:
                binding.Image1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                break;
            case 1:
                binding.Image2.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                break;
            case 2:
                binding.Image3.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                break;
            case 3:
                binding.Image4.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                break;
        }
    }
}
