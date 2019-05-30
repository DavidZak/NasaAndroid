package com.mradmin.nasa_android.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String SHARED_PREFERENCES_NAME = "user_prefs";

    private final static String PHOTOS_LOAD_COUNT = "photos_load_count";

    private final SharedPreferences prefs;

    public SharedPrefs(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public int getPhotosLoadCount() {
        return prefs
                .getInt(PHOTOS_LOAD_COUNT, 20);
    }

    public boolean setPhotosLoadCount(int count){
        return prefs.edit()
                .putInt(PHOTOS_LOAD_COUNT, count)
                .commit();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}

