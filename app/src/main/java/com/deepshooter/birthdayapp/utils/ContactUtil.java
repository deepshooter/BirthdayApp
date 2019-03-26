package com.deepshooter.birthdayapp.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.deepshooter.birthdayapp.background.PeriodicWork;
import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.database.AppExecutors;
import com.deepshooter.birthdayapp.model.ContactName;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import timber.log.Timber;

public class ContactUtil {

    public static void getContactNames(final Context ctx) {
        final List<ContactName> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {

                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        ContactName contactName = new ContactName(name);
                        list.add(contactName);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = AppDatabase.getInstance(ctx);
                appDatabase.appDao().deleteAllContactNames();
                appDatabase.appDao().insertContactName(list);
                Timber.i("Data insertion done !");
            }
        });


    }


    public static void startPeriodicWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                PeriodicWork.class, 7, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }

    public static void getContactList(final Context context) {
        final List<ContactName> list = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                Timber.i("Hello " + name);
                ContactName contactName = new ContactName(name);
                list.add(contactName);
            }
        }
        if (cur != null) {
            cur.close();
        }


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = AppDatabase.getInstance(context);
                appDatabase.appDao().deleteAllContactNames();
                appDatabase.appDao().insertContactName(list);
                Timber.i("Data insertion done !");
            }
        });

    }
}
