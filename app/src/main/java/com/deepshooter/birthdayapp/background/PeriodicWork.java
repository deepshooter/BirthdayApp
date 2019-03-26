package com.deepshooter.birthdayapp.background;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepshooter.birthdayapp.utils.ContactUtil;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import timber.log.Timber;

public class PeriodicWork extends Worker {

    private Context mContext;

    public PeriodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Timber.i("Background work started");
        ContactUtil.getContactList(mContext);

        return Result.success();
    }
}