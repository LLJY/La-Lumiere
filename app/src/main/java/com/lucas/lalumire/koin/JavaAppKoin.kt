package com.lucas.lalumire.koin

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lucas.lalumire.repositories.FirebaseAuthRepository
import com.lucas.lalumire.repositories.FirestoreRepository
import com.lucas.lalumire.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

@JvmField
val loginViewModelModule : Module = module{
    //singleton because we cannot use kotlin and take advantage of property delegation for sharedViewModel
    single { LoginViewModel(FirebaseAuthRepository()) }
}
val mainViewModelModule : Module = module{
    single{ MainViewModel(Application(), FirestoreRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance())) }
}
val homeViewModelModule : Module = module {
    single{ HomeViewModel(FirestoreRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance())) }
}
val manageListingsViewModelModule : Module = module{
    single { ManageListingsViewModel(FirestoreRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance())) }
}
val addEditItemViewModelModule : Module = module{
    single {AddEditItemViewModel(FirestoreRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance())) }
}
val itemActivityViewModelModule: Module = module{
    single {ItemViewModel(FirestoreRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance()))}
}
fun start(applicationComponent: ApplicationComponent){
    startKoin{
        androidLogger(Level.DEBUG)
        androidContext(applicationComponent)
        modules(loginViewModelModule, mainViewModelModule, homeViewModelModule, manageListingsViewModelModule, addEditItemViewModelModule, itemActivityViewModelModule)
    }
}