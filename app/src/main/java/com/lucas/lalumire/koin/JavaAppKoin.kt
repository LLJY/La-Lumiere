package com.lucas.lalumire.koin

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lucas.lalumire.Viewmodels.HomeViewModel
import com.lucas.lalumire.Viewmodels.MainViewModel
import com.lucas.lalumire.Repositories.FirebaseAuthRepository
import com.lucas.lalumire.Repositories.FirestoreRepository
import com.lucas.lalumire.Viewmodels.LoginViewModel
import com.lucas.lalumire.Viewmodels.ManageListingsViewModel
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

fun start(applicationComponent: ApplicationComponent){
    startKoin{
        androidLogger(Level.DEBUG)
        androidContext(applicationComponent)
        modules(loginViewModelModule, mainViewModelModule, homeViewModelModule, manageListingsViewModelModule)
    }
}