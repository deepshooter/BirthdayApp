package com.deepshooter.birthdayapp.viemmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;

public class ProfileViewModel extends ViewModel {
    private LiveData<BirthdaysInfo> birthdaysInfo;

    public ProfileViewModel(AppDatabase database, int birthdayId) {
        birthdaysInfo = database.appDao().getBirthdayById(birthdayId);

    }

    public LiveData<BirthdaysInfo> getBirthdaysInfo() {
        return birthdaysInfo;
    }
}
