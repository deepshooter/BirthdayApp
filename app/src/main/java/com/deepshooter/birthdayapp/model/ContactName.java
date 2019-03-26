package com.deepshooter.birthdayapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ContactName {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public ContactName(String name) {
        this.name = name;
    }
}
