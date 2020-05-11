package com.lucas.lalumire;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.R.*;
public class FragmentTransactions {
    public static void LaunchFragment(@NonNull Fragment fragment, int container, @NonNull FragmentActivity context, boolean animation){
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, fragment);
        if(animation){
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right );
        }
        transaction.commit();
    }
    public static void ReplaceFragment(@NonNull Fragment fragment, int container, @NonNull FragmentActivity context, @NonNull String backState, boolean animation){
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(animation) {
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        transaction.replace(container, fragment);
        //add previous to backstack
        transaction.addToBackStack(backState);
        transaction.commit();
    }
}
