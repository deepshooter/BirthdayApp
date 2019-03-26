package com.deepshooter.birthdayapp;

import android.app.Application;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        myApplication = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }
}