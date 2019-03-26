package com.deepshooter.birthdayapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.ui.home.HomeActivity;
import com.deepshooter.birthdayapp.utils.SharedPreferenceUtil;

import java.util.List;


public class AppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        List<BirthdaysInfo> mBirthdaysInfoList;

        mBirthdaysInfoList = SharedPreferenceUtil.getBirthdaysFromSharedPreferences(context);

        if (mBirthdaysInfoList != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            if (mBirthdaysInfoList.size() != 0) {
                views.setTextViewText(R.id.birthday_widget_name_text, context.getString(R.string.birthdays_and_anniversaries));
            } else {
                views.setTextViewText(R.id.birthday_widget_name_text, context.getString(R.string.add_birthdays_and_anniversaries_widget));
            }


            views.setOnClickPendingIntent(R.id.birthday_widget_name_text, pendingIntent);


            Intent intent = new Intent(context, AppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            views.setRemoteAdapter(R.id.birthday_widget_listView, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.birthday_widget_listView);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

