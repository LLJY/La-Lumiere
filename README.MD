# La Lumiere
I am running short on time so here is a quick README.

## Architecture
This application is built using MVVM + Repository architechture where:

Repository is meant for getting data and converting it to the application's model

View Model is meant for storing Data and backend business logic.

View is meant for frontend business logic (highlighting textboxes etc)

## View Binding
My Application uses view binding, which may seem confusing as there is no findViewById but here is an explanation of how it works:

an XML is named card_item_view. View binding will generate a class that looks like this CardItemViewBinding.
It will also convert XML naming scheme of var_name to the Java naming scheme of varName so this:

```XML
 <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/hot_items"
        android:layout_width="match_parent"
        app:layout_constraintTop_toBottomOf="@id/all_hot_buttons"
        android:layout_height="230dp"/>
```
will look like this in Java

```Java
binding.hotItems
```
I will not get too far into viewbinding here, there are several resources online for how UI inflation is done etc.

## LiveData
I made use of LiveData to wait for an asynchronous operation to be completed.
Due to the lack of awaitables(or rather my unwillingness to touch anything but coroutines)
we have to do this:
```Java
// in the viewmodel
public LiveData<Item> getItem(){
        // define the live data we will return here
        MutableLiveData<Item> returnLiveData = new MutableLiveData<>();
        // since threads are non blocking it will just skip over this whole part and return.
        new Thread(new Runnable(){
                @Override
                public void run(){
                        // post the value of Item so we are able to observe the livedata and "await" for the value
                        returnLiveData.postValue(returnLiveData);
                }
        }).start();
        return returnLiveData;
}
// in the activity/fragment
// observe for changes in the value.
 homeViewModelLazy.getValue().hotItemsAdapter.getLikedItem().observe(getViewLifecycleOwner(), new Observer<Item>() {
                    @Override
                    public void onChanged(Item item) {
                        // Sometimes when the activity is just created, livedata can act weird and post NULL as a value, so do a null check
                        if(item!=null) {
                        // do whatever we need to do with the item
                            homeViewModelLazy.getValue().likeItem(item);
                        }
                    }
                });
```
Sometimes if only observing once is needed, I will remove the observer like so to prevent memory leaks
```Java
 LiveData<Item> itemsLive = homeViewModelLazy.getValue().hotItemsAdapter.getLikedItem()
 itemsLive.observeForever(new Observer<Item>() {
                    @Override
                    public void onChanged(Item item) {
                        // Sometimes when the activity is just created, livedata can act weird and post NULL as a value, so do a null check
                        if(item!=null) {
                        // do whatever we need to do with the item
                            homeViewModelLazy.getValue().likeItem(item);
                            // remove the observer
                            itemsLive.removeObserver(this);
                        }
                    }
                });
```

## Dependency Injection
I chose to use Koin for DI as it is more clean of an implementation
to declare a DI in Koin all I have to do is this
```Kotlin
// declare the variable for module
val loginViewModelModule : Module = module{
    // declare the singleton class instance and inject needed dependencies
    single { LoginViewModel(FirebaseAuthRepository()) }
} 
// start koin
 startKoin{
         // log koin's actions
        androidLogger(Level.DEBUG)
        // pass it context
        androidContext(applicationComponent)
        // give it all the defined modules
        modules(loginViewModelModule, mainViewModelModule, homeViewModelModule, manageListingsViewModelModule, addEditItemViewModelModule, itemActivityViewModelModule)
    }
```
Part of koin is also Kotlin's Lazy property delegate. It is the reason why you would see all my viewmodels have to use .getValue before accessing its functions.
Kotlin Lazy is for lazy initialization, only when the property is first needed then it initializes the viewmodel/class/whatever else.
It can be used in Java, but is not as powerful.

## Custom Bottomsheet Navigation
The app uses a persistent bottomsheet with a recyclerview. Honestly I am quite proud of this portion.
the bottomsheet uses a recyclerview (Adapter is MenuBottomSheetAdapter, layout is bottom_sheet_row_layout).

When a user clicks on a menu item, it triggers a livedata observer, which is observed in main activity.

it passes over a menuItem object, which contains the Class of the Fragment to instantiate. So I made use of Java reflection to instantiate the fragment.
```Java
FragmentTransactions.LaunchFragmentFade((Fragment) item.itemFragment.newInstance(), R.id.main_fragment_holder, MainActivity.this, true);
```

## Live reloading firebase data
I observed the firebase snapshot and reloaded data accordingly by sending requests to the backend/firebase cloud functions when an item is updated.
Honestly this is really simple and probably the coolest part of my app
```Java
// this observes for items changes
        db.collectionGroup("Items").addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // these are functions in HomeViewModel to get data from post requests
                getFollowingItems();
                getHotItems();
                getSuggestedItems();
            }
        });
        // this observes for category changes
        db.collection("Categories").addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("update!!!", "update");
                getCategories();
            }
        });
```
# Important Files (which file is for what)
Lastly, this just tells you which page is which
MainActivity MainViewModel - Activity that hosts the fragments and bottomsheet

HomeFragment HomeViewModel - Home Page

ManageListingsFragment ManageListingsViewModel - Manage Listings Page (Seller only)

LoginActivity LoginViewModel - Hosts LoginFragment and SignUpFragment

LoginFragment - Login Page

SignUpFragment - Signup Page

AddEditItemActivity - Add / Edit Items page (edit is non functional due to backend)

ItemActivity - Individual Items activity

BigItemAdapter - Recycler Adapter for manage listings / more items(unimplemented)

SmallItemAdapter - Recycler Adapter for home recyclerviews

ItemImagesSliderAdapter - Adapter for external library (for images in items page)

Loading - contains static function to show random loading messages

FragmentTransactions - contains static functions to launch fragments

