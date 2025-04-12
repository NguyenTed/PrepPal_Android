package com.group5.preppal.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageUtils {

    public static Context setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }

    public static void saveLanguage(Context context, String languageCode) {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("app_language", languageCode)
                .apply();
    }

    public static String getSavedLanguage(Context context) {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("app_language", "en");
    }
}
