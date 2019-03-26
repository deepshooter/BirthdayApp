package com.deepshooter.birthdayapp.viemmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.deepshooter.birthdayapp.database.AppDatabase;

public class ProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mBirthdayId;

    public ProfileViewModelFactory(AppDatabase database, int birthdayId) {
        mDb = database;
        mBirthdayId = birthdayId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ProfileViewModel(mDb, mBirthdayId);
    }

}
