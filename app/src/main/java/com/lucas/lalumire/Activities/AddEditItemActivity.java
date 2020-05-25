package com.lucas.lalumire.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lucas.lalumire.R;
import com.lucas.lalumire.Viewmodels.AddEditItemViewModel;

import kotlin.Lazy;
import static org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel;
import static org.koin.java.KoinJavaComponent.inject;

public class AddEditItemActivity extends AppCompatActivity {
    private Lazy<AddEditItemViewModel> addEditItemViewModelLazy = viewModel(this, AddEditItemViewModel.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }
}
