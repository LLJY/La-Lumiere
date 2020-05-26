package com.lucas.lalumire.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lucas.lalumire.R;
import com.lucas.lalumire.databinding.ActivityAddItemBinding;
import com.lucas.lalumire.viewmodels.AddEditItemViewModel;

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
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

        }else if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){

        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 42069);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

        }else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 69420);
        }
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
        // get the UI values from view model
        getUIFromViewModel();
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

        // set the on change listeners and update activity accordingly
        binding.titleText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(binding.titleText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // update the viewmodel with the text.
                addEditItemViewModelLazy.getValue().Title = s.toString();
            }
        });
        binding.usedRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // set the boolean to a statement checking if the used radio was ticked
                addEditItemViewModelLazy.getValue().isUsed = checkedId == R.id.used_radio;
            }
        });
        binding.priceBox.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(binding.priceBox);
            }

            @Override
            public void afterTextChanged(Editable s) {
                addEditItemViewModelLazy.getValue().Price = Double.parseDouble(s.toString());
            }
        });
        binding.stockBox.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(binding.stockBox);
            }

            @Override
            public void afterTextChanged(Editable s) {
                addEditItemViewModelLazy.getValue().Stock = Integer.parseInt(s.toString());
            }
        });
        binding.descBox.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(binding.descBox);
            }

            @Override
            public void afterTextChanged(Editable s) {
                addEditItemViewModelLazy.getValue().Description = s.toString();
            }
        });
        binding.locationBox.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(binding.locationBox);
            }

            @Override
            public void afterTextChanged(Editable s) {
                addEditItemViewModelLazy.getValue().Location = s.toString();
            }
        });
        // most important portion!! add onclick functions!!
        binding.bottomSheet.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addEditItemViewModelLazy.getValue().isAddNotEdit){
                    if(checkForEmpty()){
                        pd = ProgressDialog.show(AddEditItemActivity.this, "wait", "lol");
                        addEditItemViewModelLazy.getValue().addItem().observe(AddEditItemActivity.this, new androidx.lifecycle.Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                pd.dismiss();
                                if(aBoolean) {
                                    // clear when succeeded
                                    addEditItemViewModelLazy.getValue().clearEverything();
                                    AddEditItemActivity.this.onBackPressed();
                                }else{
                                    showSnack("An Error has occured");
                                }
                            }
                        });
                    }
                }else{
                    //TODO add EDIT FUNCTIONS
                }
            }
        });
        binding.bottomSheet.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show a dialog
                new AlertDialog.Builder(AddEditItemActivity.this)
                        .setTitle("Cancel")
                        .setMessage("Are you sure you want to leave now? All changes will be lost!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // clear when cancelled
                                addEditItemViewModelLazy.getValue().clearEverything();
                                // go back after clearning
                                AddEditItemActivity.this.onBackPressed();
                            }
                        })
                        .setNegativeButton("No", null).show();
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
    private void getUIFromViewModel(){
        final AddEditItemViewModel viewModel = addEditItemViewModelLazy.getValue();
        if(viewModel.Title != null){
            binding.titleText.getEditText().setText(viewModel.Title);
        }
        if(viewModel.Price != 0){
            binding.priceBox.getEditText().setText(String.valueOf(viewModel.Price));
        }
        if(viewModel.Stock != 0){
            binding.stockBox.getEditText().setText(String.valueOf(viewModel.Stock));
        }
        if(viewModel.Description != null){
            binding.descBox.getEditText().setText(viewModel.Description);
        }
        if(viewModel.Location != null){
            binding.locationBox.getEditText().setText(viewModel.Location);
        }
        // get the comboboxes
        final LiveData<Boolean> isDoneLive = viewModel.getAllCombos();
        pd = ProgressDialog.show(AddEditItemActivity.this, "Loading", "Please be patient");
        isDoneLive.observeForever(new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                pd.dismiss();
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.categories);
                ArrayAdapter<String> paymentTypesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.paymentTypes);
                ArrayAdapter<String> procurementTypesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.procurementTypes);
                binding.categorySpinner.setAdapter(categoryAdapter);
                binding.paymentTypeSpinner.setAdapter(paymentTypesAdapter);
                binding.procurementTypeSpinner.setAdapter(procurementTypesAdapter);
                // set the selected index values, do not animate if it is 0
                binding.categorySpinner.setSelection(viewModel.categoriesSelectedIndex, viewModel.categoriesSelectedIndex!=0);
                binding.paymentTypeSpinner.setSelection(viewModel.paymentTypesSelectedIndex, viewModel.paymentTypesSelectedIndex!=0);
                binding.procurementTypeSpinner.setSelection(viewModel.procurementTypesSelectedIndex, viewModel.procurementTypesSelectedIndex!=0);
                // once adapter is set, add the listeners
                // save the comboboxes state when changed
                binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        addEditItemViewModelLazy.getValue().categoriesSelectedIndex = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        addEditItemViewModelLazy.getValue().paymentTypesSelectedIndex = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.procurementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        addEditItemViewModelLazy.getValue().procurementTypesSelectedIndex = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                // remove observer to prevent memory leak
                isDoneLive.removeObserver(this);
            }
        });
    }

    /**
     * function to check and warn user for empty fields
     * @return true = successful
     */
    private boolean checkForEmpty() {
        boolean returnBool = true;
        if (addEditItemViewModelLazy.getValue().image1 == null) {
            returnBool = false;
            showSnack("All 4 Images Required");
        }
        if (addEditItemViewModelLazy.getValue().image1 == null) {
            returnBool = false;
            showSnack("All 4 Images Required");
        }
        if (addEditItemViewModelLazy.getValue().image1 == null) {
            returnBool = false;
            showSnack("All 4 Images Required");
        }
        if (addEditItemViewModelLazy.getValue().image1 == null) {
            returnBool = false;
            showSnack("All 4 Images Required");
        }
        if (binding.titleText.getEditText().getText().toString().isEmpty()) {
            returnBool = false;
            setRequiredError(binding.titleText);
        }
        if (binding.priceBox.getEditText().getText().toString().isEmpty()) {
            returnBool = false;
            setRequiredError(binding.priceBox);
        }
        if (binding.stockBox.getEditText().getText().toString().isEmpty()) {
            returnBool = false;
            setRequiredError(binding.stockBox);
        }
        if (binding.descBox.getEditText().getText().toString().isEmpty()) {
            returnBool = false;
            setRequiredError(binding.descBox);
        }
        if (binding.locationBox.getEditText().getText().toString().isEmpty()) {
            returnBool = false;
            setRequiredError(binding.locationBox);
        }
        return returnBool;
    }

    private void setRequiredError(TextInputLayout layout){
        layout.setError("Required");
    }
    private void clearError(TextInputLayout layout){
        layout.setError(null);
    }

    private void showSnack(String message){
        Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // we might do different things for each permission but i'm kinda lazy right now
        switch (requestCode) {
            case 42069: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //eh
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(AddEditItemActivity.this, "Sorry, permissions must be available to do that", Toast.LENGTH_LONG).show();
                    // kick the user out
                    AddEditItemActivity.this.onBackPressed();
                }
                return;
            }
            case 69420: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //eh
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(AddEditItemActivity.this, "Sorry, permissions must be available to do that", Toast.LENGTH_LONG).show();
                    // kick the user out
                    AddEditItemActivity.this.onBackPressed();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
