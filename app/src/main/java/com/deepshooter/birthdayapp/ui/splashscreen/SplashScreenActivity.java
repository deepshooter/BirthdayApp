package com.deepshooter.birthdayapp.ui.splashscreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.notification.MyAlarm;
import com.deepshooter.birthdayapp.permission.PermissionActivity;
import com.deepshooter.birthdayapp.ui.home.HomeActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.ContactUtil;
import com.deepshooter.birthdayapp.utils.SharedPreferenceUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.splash_imageView)
    ImageView mSplashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Fabric.with(this, new Crashlytics());
        ButterKnife.bind(this);
        //showGif();
        setNotificationAlarm();
        scheduleSplashScreen();
    }

    private void scheduleSplashScreen() {
        int SPLASH_DURATION = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SharedPreferenceUtil.getInstance(SplashScreenActivity.this).getIsFirstTime()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        startActivity(new Intent(SplashScreenActivity.this, PermissionActivity.class));
                        SharedPreferenceUtil.getInstance(SplashScreenActivity.this).setIsFirstTime(false);
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                        SharedPreferenceUtil.getInstance(SplashScreenActivity.this).setIsFirstTime(false);
                        ContactUtil.getContactList(SplashScreenActivity.this);
                    }

                } else {
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));

                }

                finish();
            }
        }, SPLASH_DURATION);
    }


    private void setNotificationAlarm() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String notificationTime = sharedPreferences.getString(AppConstants.IntentKey.NOTIFICATION_TIME, "8");
        Timber.d("Okay Notification Time "+notificationTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(notificationTime));
        calendar.set(Calendar.MINUTE, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, MyAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        if (am != null) {
            am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
            Timber.d("Okay Alarm Set");
        }
    }

    private void showGif() {
        Glide.with(this).asGif().load(R.raw.splash).into(mSplashImageView);
    }


}
