package com.lucas.lalumire.koin

import android.app.Application
import com.lucas.lalumire.Repositories.FirebaseAuthRepository
import com.lucas.lalumire.Viewmodels.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
@JvmField
val module : Module = module{
    //singleton because we cannot use kotlin and take advantage of property delegation for sharedViewModel
    single { LoginViewModel(FirebaseAuthRepository()) }
}
fun start(applicationComponent: ApplicationComponent){
    startKoin{
        androidContext(applicationComponent)
        modules(module)
    }
}