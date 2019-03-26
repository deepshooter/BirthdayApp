package com.deepshooter.birthdayapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.model.ContactName;

import java.util.List;


@Dao
public interface AppDao {

    //Contacts
    @Query("SELECT name FROM ContactName")
    LiveData<List<String>> loadAllContactNames();

    @Insert
    void insertContactName(List<ContactName> contactNameList);

    @Query("DELETE FROM ContactName")
    void deleteAllContactNames();

    //Birthdays

    @Insert
    void insertBirthday(BirthdaysInfo birthdaysInfo);

    @Delete
    void deleteBirthday(BirthdaysInfo birthdaysInfo);

    @Query("SELECT * FROM birthdays")
    LiveData<List<BirthdaysInfo>> loadAllBirthdays();

    @Query("SELECT * FROM birthdays")
    List<BirthdaysInfo> loadAllBirthdaysFromDB();

  /*  @Query("update  birthdays set name = :name , birthday = :birthday  WHERE birthdayId = :birthdayId")
    void updateBirthday(String name, String birthday, int birthdayId);*/

    @Query("SELECT * FROM birthdays WHERE birthdayId = :birthdayId")
    LiveData<BirthdaysInfo> getBirthdayById(int birthdayId);

    @Update
    void update(BirthdaysInfo birthdaysInfo);
}
