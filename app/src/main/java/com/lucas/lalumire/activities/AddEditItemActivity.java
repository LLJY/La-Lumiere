package com.lucas.lalumire.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lucas.lalumire.viewmodels.AddEditItemViewModel;
import com.lucas.lalumire.databinding.ActivityAddItemBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.Observer;

import kotlin.Lazy;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;

public class AddEditItemActivity extends AppCompatActivity {
    ActivityAddItemBinding binding;
    private Lazy<AddEditItemViewModel> addEditItemViewModelLazy = viewModel(this, AddEditItemViewModel.class);
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
        // if image files are not null, set them
        if (addEditItemViewModelLazy.getValue().image1 != null) {
            // realistically
            binding.Image1.setImageBitmap(addEditItemViewModelLazy.getValue().image1);
        }
        if (addEditItemViewModelLazy.getValue().image2 != null) {
            // realistically
            binding.Image2.setImageBitmap(addEditItemViewModelLazy.getValue().image2);
        }
        if (addEditItemViewModelLazy.getValue().image3 != null) {
            // realistically
            binding.Image3.setImageBitmap(addEditItemViewModelLazy.getValue().image3);
        }
        if (addEditItemViewModelLazy.getValue().image4 != null) {
            // realistically
            binding.Image4.setImageBitmap(addEditItemViewModelLazy.getValue().image4);
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
                    for (int i = 0; i < imageFiles.length; i++) {
                        // set the imageview images
                        setImages(i, imageFiles[i].getFile());
                    }
                } else {
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

    private void setImages(int i, File file) {
        switch (i) {
            case 0:
                binding.Image1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                addEditItemViewModelLazy.getValue().setImage(i, file.getPath());
                break;
            case 1:
                binding.Image2.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                addEditItemViewModelLazy.getValue().setImage(i, file.getPath());
                break;
            case 2:
                binding.Image3.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                addEditItemViewModelLazy.getValue().setImage(i, file.getPath());
                break;
            case 3:
                binding.Image4.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                addEditItemViewModelLazy.getValue().setImage(i, file.getPath());
                break;
        }
    }
}
