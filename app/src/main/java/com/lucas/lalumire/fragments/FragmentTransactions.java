package com.lucas.lalumire.fragments;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.R.*;
public class FragmentTransactions {
    /**
     * Function that launches fragment and puts it into the defined container
     * @param fragment new instance of fragment i.e. new LoginFragment()
     * @param container the container that will host the fragment i.e. R.id.constraintLayout
     * @param context the context of the activity i.e getApplicationContext()
     * @param animation true/false to specify if animation is needed
     */
    public static void LaunchFragment(@NonNull Fragment fragment, int container, @NonNull FragmentActivity context, boolean animation){
        //get fragment manager
        FragmentManager manager = context.getSupportFragmentManager();
        //begin the transaction
        FragmentTransaction transaction = manager.beginTransaction();
        if(animation){
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right );
        }
        //replace the fragment into the container, container is just a layout which the fragment will reside in
        transaction.replace(container, fragment);
        transaction.commit();
    }
    public static void LaunchFragmentFade(@NonNull Fragment fragment, int container, @NonNull FragmentActivity context, boolean animation){
        //get fragment manager
        FragmentManager manager = context.getSupportFragmentManager();
        //begin the transaction
        FragmentTransaction transaction = manager.beginTransaction();
        if(animation){
            transaction.setCustomAnimations(anim.fade_in, anim.fade_out, anim.fade_in, anim.fade_out );
        }
        //replace the fragment into the container, container is just a layout which the fragment will reside in
        transaction.replace(container, fragment);
        transaction.commit();
    }

    /**
     * Function that replaces fragment and adds it to the backstack
     * @param fragment new instance of fragment i.e. new LoginFragment()
     * @param container the container that will host the fragment i.e. R.id.constraintLayout
     * @param context the context of the activity i.e getApplicationContext()
     * @param backState the previous fragment's name
     * @param animation true/false to specify if animation is needed
     */
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
