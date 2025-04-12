package com.group5.preppal;

import android.app.Application;
import android.content.Context;

import com.group5.preppal.utils.LanguageUtils;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        String lang = LanguageUtils.getSavedLanguage(base);
        Context newContext = LanguageUtils.setLocale(base, lang);
        super.attachBaseContext(newContext);
    }
}
