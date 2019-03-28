package com.deepshooter.birthdayapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.ui.profile.ProfileActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.ageutils.Age;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.deepshooter.birthdayapp.utils.DateUtils.convertDateInDayFormat;
import static com.deepshooter.birthdayapp.utils.DateUtils.getComingBirthday;
import static com.deepshooter.birthdayapp.utils.DateUtils.getCurrentDate;
import static com.deepshooter.birthdayapp.utils.DateUtils.getRemainingMonth;
import static com.deepshooter.birthdayapp.utils.ageutils.AgeCalculator.calculateAge;

public class BirthdaysAdapter extends RecyclerView.Adapter<BirthdaysAdapter.BirthdaysViewHolder> {

    private Context mContext;
    private List<BirthdaysInfo> mBirthdaysInfoList;


    public BirthdaysAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BirthdaysViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BirthdaysViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_birthdays, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BirthdaysViewHolder holder, int i) {

        final BirthdaysInfo birthdaysInfo = mBirthdaysInfoList.get(i);
        holder.mNameTextView.setText(birthdaysInfo.getName());
        holder.mMonthsTextView.setText(getRemainingMonth(birthdaysInfo.getBirthday()) + mContext.getString(R.string.new_line) + mContext.getString(R.string.months));


        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date birthDate = null;
        try {
            birthDate = sdf.parse(birthdaysInfo.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (birthDate != null) {
            Age age = calculateAge(birthDate);
            int comingAge = age.getYears() + 1;
            if (birthdaysInfo.getType().equalsIgnoreCase(AppConstants.TYPE_BIRTHDAY)) {
                holder.mProfileImageView.setBackgroundResource(R.drawable.user_profile);
                holder.mDaysTextView.setText(mContext.getString(R.string.turns) + mContext.getString(R.string.space) + comingAge + mContext.getString(R.string.space) + mContext.getString(R.string.on) + mContext.getString(R.string.space) + getComingBirthday(birthdaysInfo.getBirthday()));
            } else {
                holder.mDaysTextView.setText(mContext.getString(R.string.anniversary) + mContext.getString(R.string.space) + comingAge + mContext.getString(R.string.space) + mContext.getString(R.string.on) + mContext.getString(R.string.space) + getComingBirthday(birthdaysInfo.getBirthday()));
                holder.mProfileImageView.setBackgroundResource(R.drawable.couple_icon);
            }

        }


        holder.mBirthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(AppConstants.IntentKey.BIRTHDAY_DATA, birthdaysInfo);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mBirthdaysInfoList) return 0;
        return mBirthdaysInfoList.size();
    }

    public class BirthdaysViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_textView)
        TextView mNameTextView;
        @BindView(R.id.days_textView)
        TextView mDaysTextView;
        @BindView(R.id.months_textView)
        TextView mMonthsTextView;
        @BindView(R.id.birthday_layout)
        LinearLayout mBirthdayLayout;
        @BindView(R.id.profile_imageView)
        ImageView mProfileImageView;

        public BirthdaysViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setBirthdayData(List<BirthdaysInfo> birthdaysInfoList) {
        mBirthdaysInfoList = birthdaysInfoList;
        notifyDataSetChanged();
    }
}
