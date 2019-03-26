package com.deepshooter.birthdayapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.utils.SharedPreferenceUtil;

import java.util.List;


public class AppWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, List<BirthdaysInfo> birthdaysInfo) {
        SharedPreferenceUtil.setBirthdaysFromSharedPreferences(context, birthdaysInfo);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidget.class));
        AppWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        return new ListRemoteViewsFactory(getApplicationContext());
    }

}