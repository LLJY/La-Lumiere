package com.lucas.lalumire.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.lucas.lalumire.Adapters.MenuBottomSheetAdapter;
import com.lucas.lalumire.Fragments.AboutFragment;
import com.lucas.lalumire.Fragments.HomeFragment;
import com.lucas.lalumire.Fragments.InboxFragment;
import com.lucas.lalumire.Fragments.LikedItemsFragment;
import com.lucas.lalumire.Fragments.ManageListingsFragment;
import com.lucas.lalumire.Fragments.ProfileFragment;
import com.lucas.lalumire.Fragments.SettingsFragment;
import com.lucas.lalumire.Fragments.SubscribedCategoriesFragment;
import com.lucas.lalumire.Fragments.TrackOrdersFragment;
import com.lucas.lalumire.Models.MenuItem;
import com.lucas.lalumire.Models.User;
import com.lucas.lalumire.R;
import com.lucas.lalumire.Repositories.FirebaseAuthRepository;
import com.lucas.lalumire.Repositories.FirestoreRepository;

import java.util.ArrayList;
import java.util.Objects;

/** This is called the MainViewModel as it should contain the information that is shared between all fragments.
 * this should be the largest block of Business logic through out the entire app and everything should reside here.
 * Fragment/Screen specific variables will be kept in their own viewmodels.
 */
public class MainViewModel extends AndroidViewModel {
    FirebaseAuth mAuth;
    //should be injected by koin
    private FirestoreRepository firestoreRepository;
    //logged in user's details
    private MutableLiveData<User> mutableLoggedInUser = new MutableLiveData<>();
    //we will initialize this once we get our User
    public MenuBottomSheetAdapter menuBottomSheetAdapter;
    //getter method to provide LiveData version of mutableLoggedInUser
    public LiveData getUserLiveData(){
        return this.mutableLoggedInUser;
    }
    public MainViewModel(@NonNull Application application, @NonNull final FirestoreRepository firestoreRepository) {
        super(application);
        this.firestoreRepository = firestoreRepository;
        mAuth = FirebaseAuth.getInstance();
        //automatically get the loggedInUser
        final LiveData<User> userObserveOnce = firestoreRepository.getUserInfo();
        userObserveOnce.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                ArrayList<MenuItem> menuItems = new ArrayList<>();
                //add items based off the order of items in the recyclerview
                menuItems.add(new MenuItem(ProfileFragment.class, user.getName(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()));
                switch(user.userType){
                    case ADMIN:
                        menuItems.add(new MenuItem(ManageListingsFragment.class, "Manage Listings", R.drawable.ic_edit_black_24dp));
                        break;
                    case BUYER:
                        break;
                    case SELLER:
                        menuItems.add(new MenuItem(ManageListingsFragment.class, "Manage Listings", R.drawable.ic_edit_black_24dp));
                        break;
                }
                menuItems.add(new MenuItem(TrackOrdersFragment.class, "Track Orders", R.drawable.ic_near_me_black_24dp));
                menuItems.add(new MenuItem(HomeFragment.class, "Home", R.drawable.ic_home_black_24dp));
                menuItems.add(new MenuItem(InboxFragment.class, "Inbox", R.drawable.ic_inbox_black_24dp));
                menuItems.add(new MenuItem(LikedItemsFragment.class, "Liked Items", R.drawable.ic_favorite_black_24dp));
                menuItems.add(new MenuItem(SubscribedCategoriesFragment.class, "Subscribed Categories", R.drawable.ic_format_list_bulleted_black_24dp));
                menuItems.add(new MenuItem(SettingsFragment.class, "Settings", R.drawable.ic_settings_black_24dp));
                menuItems.add(new MenuItem(AboutFragment.class, "About", R.drawable.ic_info_black_24dp));
                menuBottomSheetAdapter = new MenuBottomSheetAdapter(menuItems);
                //post the value to activity
                mutableLoggedInUser.postValue(user);
                //we only need to observe once
                userObserveOnce.removeObserver(this);

            }
            //listen for authentication changes
            FirebaseAuth.AuthStateListener  a = new FirebaseAuth.AuthStateListener(){
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    mutableLoggedInUser.postValue(null);
                }
            };
        });
    }
}
