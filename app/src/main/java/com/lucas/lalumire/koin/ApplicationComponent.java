package com.lucas.lalumire.koin;

import android.app.Application;

import org.koin.core.Koin;

public class ApplicationComponent extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //start the DI
        JavaAppKoinKt.start(this);
    }
}