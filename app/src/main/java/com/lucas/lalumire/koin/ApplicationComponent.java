package com.lucas.lalumire.koin;

import android.app.Application;

import org.koin.core.Koin;

/*
 * Because Temasek Poly is retarded as fuck, we can't use Kotlin for our apps.
 * To save my time and sanity I am going to use Koin Java, which will contain
 *  some kotlin components because fuck you. My models will also be in Kotlin because yes
 * fuck you. Rest assured the rest of this fucking app is written in Java because
 * I want my GPA. Fuck you otherwise.
 */
public class ApplicationComponent extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //start the DI
        JavaAppKoinKt.start(this);
    }
}