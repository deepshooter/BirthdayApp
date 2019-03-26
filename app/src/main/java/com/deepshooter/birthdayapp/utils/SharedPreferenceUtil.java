package com.deepshooter.birthdayapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class SharedPreferenceUtil {

    private static final String PREFS_TAG = "SharedPrefs";
    private static final String PRODUCT_TAG = "MyProduct";
    private static final String IS_FIRST_TIME = "is_first_time";

    private static SharedPreferenceUtil instance;
    private SharedPreferences mSharedPreferences;

    private SharedPreferenceUtil(@NonNull Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferenceUtil getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtil(context);
        }
        return instance;
    }


    public void setIsFirstTime(boolean isFirstTime) {
        mSharedPreferences.edit()
                .putBoolean(IS_FIRST_TIME, isFirstTime)
                .apply();
    }

    public boolean getIsFirstTime() {
        return mSharedPreferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void clearData() {
        mSharedPreferences.edit().clear().apply();
    }


    public static List<BirthdaysInfo> getBirthdaysFromSharedPreferences(Context context) {
        Gson gson = new Gson();
        List<BirthdaysInfo> productFromShared;
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(PRODUCT_TAG, "");

        Type type = new TypeToken<List<BirthdaysInfo>>() {
        }.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        return productFromShared;
    }

    public static void setBirthdaysFromSharedPreferences(Context context, List<BirthdaysInfo> birthdaysInfo) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(birthdaysInfo);

        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(PRODUCT_TAG, jsonCurProduct);
        editor.apply();
    }
}
