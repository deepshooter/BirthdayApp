package com.deepshooter.birthdayapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

public final class FireBaseAnalyticsUtil {


    private FirebaseAnalytics mFireBaseAnalytics;
    private static FireBaseAnalyticsUtil instance;


    public static final String ADD_BIRTHDAY = "add_birthday";
    public static final String ADD_ANNIVERSARY = "add_anniversary";
    public static final String EDIT_PROFILE = "edit_profile";
    public static final String DELETE_PROFILE = "delete_profile";
    public static final String SEND_CARD = "send_card";
    public static final String SEND_WISH = "send_wish";
    public static final String SHARE_WISH = "share_wish";
    public static final String COPY_WISH = "copy_wish";


    private FireBaseAnalyticsUtil(@NonNull Context context) {
        mFireBaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static FireBaseAnalyticsUtil getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new FireBaseAnalyticsUtil(context);
        }
        return instance;
    }

    public void setAnalyticsEvent(String eventKey) {
        mFireBaseAnalytics.logEvent(eventKey, null);
    }

}
