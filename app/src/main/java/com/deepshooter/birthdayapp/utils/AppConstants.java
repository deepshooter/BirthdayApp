package com.deepshooter.birthdayapp.utils;

public interface AppConstants {

    int CARD_GRID_SIZE = 2;
    String TYPE_BIRTHDAY = "birthday";
    String TYPE_ANNIVERSARY = "anniversary";

    interface IntentKey {
        String IS_BIRTHDAY = "is_birthday";
        String BIRTHDAY_DATA = "birthday_data";
        String CARD = "card";
        String IS_EDIT = "is_edit";
        String DATE = "date";
        String BIRTHDAY_WISH = "birthday_wish";
        String ANNIVERSARY_WISH = "anniversary_wish";
        String NOTIFICATION_BIRTHDAY_REMINDER = "notifications_birthday_reminder";
        String NOTIFICATION_TIME = "notification_time";
    }


    interface FirestoreKey {
        String BIRTHDAY_COLLECTION_PATH = "BirthdayWishes";
        String ANNIVERSARY_COLLECTION_PATH = "AnniversaryWishes";
        String BIRTHDAY_WISH_FIELD = "birthday";
        String ANNIVERSARY_WISH_FIELD = "anniversary";
    }
}