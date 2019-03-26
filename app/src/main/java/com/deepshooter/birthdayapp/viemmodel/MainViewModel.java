package com.deepshooter.birthdayapp.viemmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<String>> contactNameList;
    private LiveData<List<BirthdaysInfo>> birthdaysList;


    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        contactNameList = database.appDao().loadAllContactNames();
        birthdaysList = database.appDao().loadAllBirthdays();
    }

    public LiveData<List<String>> getContactNamesList() {
        return contactNameList;
    }

    public LiveData<List<BirthdaysInfo>> getBirthdaysList() {
        return birthdaysList;
    }

}
