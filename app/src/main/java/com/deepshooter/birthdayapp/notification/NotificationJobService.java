package com.deepshooter.birthdayapp.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.database.AppExecutors;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.ui.home.HomeActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.DateUtils;
import com.deepshooter.birthdayapp.utils.ageutils.Age;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.deepshooter.birthdayapp.utils.ageutils.AgeCalculator.calculateAge;

public class NotificationJobService extends JobService {


    String NOTIFICATION_CHANNEL_ID = "Birthday_Notification_REMINDER";
    String NOTIFICATION_CHANNEL_NAME = "Birthday_Notification";
    int NOTIFICATION_ID = 1;
    private List<BirthdaysInfo> birthdaysInfoList;

    @Override
    public boolean onStartJob(JobParameters params) {

        Timber.d("Job Started");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean notificationTime = sharedPreferences.getBoolean(AppConstants.IntentKey.NOTIFICATION_BIRTHDAY_REMINDER, true);
        if (notificationTime) {
            doBackgroundWork(params);
        }


        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {

        Timber.d("Okay Job Stopped");

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {


        final String currentDate = DateUtils.getCurrentDate().substring(0, 5);


        final AppDatabase mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                birthdaysInfoList = mAppDatabase.appDao().loadAllBirthdaysFromDB();
                Timber.d("Okay Data Fetching done !");
                Timber.d("Okay Current Date " + currentDate);

                if (birthdaysInfoList != null)
                    for (int i = 0; i < birthdaysInfoList.size(); i++) {

                        Timber.d("Okay DB Date " + birthdaysInfoList.get(i).getBirthday().substring(0, 5));
                        if (currentDate.equalsIgnoreCase(birthdaysInfoList.get(i).getBirthday().substring(0, 5))) {
                            showNotification(NotificationJobService.this, birthdaysInfoList.get(i));
                        }
                    }
            }
        });


        jobFinished(params, false);
        Timber.d("Okay Job Finished");

    }

    public void showNotification(Context context, BirthdaysInfo birthdaysInfo) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(mChannel);
        }

        String contentText;
        if (birthdaysInfo.getType().equalsIgnoreCase(AppConstants.TYPE_BIRTHDAY)) {
            contentText = "Turning " + calculateCurrentAge(birthdaysInfo.getBirthday()) + " Today !";
        } else {
            contentText = "Celebrating " + calculateCurrentAge(birthdaysInfo.getBirthday()) + " Anniversary Today !";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(birthdaysInfo.getName())
                .setContentText(contentText);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            Timber.d("Okay Notification Triggered");
        } else {
            Timber.d("Okay Notification Not Triggered");
        }

    }

    private String calculateCurrentAge(String birthday) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date birthDate = null;
        try {
            birthDate = sdf.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (birthDate != null) {
            Age age = calculateAge(birthDate);
            int comingAge = age.getYears() + 1;

            return comingAge + "";
        }

        return null;
    }

    private void sendMyNotification() {

        Timber.d("Okay Notification Called");

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("message")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0, notificationBuilder.build());


    }
}
