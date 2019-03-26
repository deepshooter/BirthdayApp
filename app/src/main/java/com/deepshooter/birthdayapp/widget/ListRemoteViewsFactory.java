package com.deepshooter.birthdayapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.utils.SharedPreferenceUtil;

import java.util.List;

import static com.deepshooter.birthdayapp.utils.DateUtils.getComingBirthdayFullFormat;


public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<BirthdaysInfo> birthdaysInfos;

    public ListRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        birthdaysInfos = SharedPreferenceUtil.getBirthdaysFromSharedPreferences(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return birthdaysInfos.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.birthdays_app_widget_list_item);

        row.setTextViewText(R.id.birthday_item_text, birthdaysInfos.get(position).getName() + " ( " + getComingBirthdayFullFormat(birthdaysInfos.get(position).getBirthday())+" ) ");

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
