package com.deepshooter.birthdayapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "birthdays")
public class BirthdaysInfo implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int birthdayId;

    private String name;

    private String birthday;

    private String type;

    public BirthdaysInfo(String name, String birthday, String type) {
        this.name = name;
        this.birthday = birthday;
        this.type = type;
    }

    protected BirthdaysInfo(Parcel in) {
        birthdayId = in.readInt();
        name = in.readString();
        birthday = in.readString();
        type = in.readString();
    }

    public static final Creator<BirthdaysInfo> CREATOR = new Creator<BirthdaysInfo>() {
        @Override
        public BirthdaysInfo createFromParcel(Parcel in) {
            return new BirthdaysInfo(in);
        }

        @Override
        public BirthdaysInfo[] newArray(int size) {
            return new BirthdaysInfo[size];
        }
    };

    public int getBirthdayId() {
        return birthdayId;
    }

    public void setBirthdayId(int birthdayId) {
        this.birthdayId = birthdayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(birthdayId);
        dest.writeString(name);
        dest.writeString(birthday);
        dest.writeString(type);
    }
}
