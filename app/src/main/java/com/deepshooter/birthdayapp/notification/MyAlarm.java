package com.deepshooter.birthdayapp.notification;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class MyAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Timber.d("Okay Alarm just fired");
        startJobScheduler(context);

    }

    private void startJobScheduler(Context context) {
        int JOB_ID = 100;
        ComponentName componentName = new ComponentName(context, NotificationJobService.class);

        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setOverrideDeadline(0)
                .build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            int resultCode = jobScheduler.schedule(jobInfo);

            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Timber.d("Okay Job Scheduled");
            } else {
                Timber.d("Okay Job Scheduling Failed");
            }
        }
    }
}