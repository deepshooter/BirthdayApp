package com.deepshooter.birthdayapp.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.database.AppExecutors;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.ui.addbirthday.AddBirthdayActivity;
import com.deepshooter.birthdayapp.ui.choosecard.CardsActivity;
import com.deepshooter.birthdayapp.ui.greetings.GreetingsActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.FireBaseAnalyticsUtil;
import com.deepshooter.birthdayapp.utils.ageutils.Age;
import com.deepshooter.birthdayapp.viemmodel.ProfileViewModel;
import com.deepshooter.birthdayapp.viemmodel.ProfileViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.deepshooter.birthdayapp.utils.DateUtils.birthdayMonth;
import static com.deepshooter.birthdayapp.utils.DateUtils.getComingBirthdayFullFormat;
import static com.deepshooter.birthdayapp.utils.DateUtils.getFullBirthday;
import static com.deepshooter.birthdayapp.utils.DateUtils.getZodiacSign;
import static com.deepshooter.birthdayapp.utils.ageutils.AgeCalculator.calculateAge;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.send_fab_speed_dial)
    FabSpeedDial mSendFabSpeedDial;
    @BindView(R.id.name_textView)
    TextView mNameTextView;
    @BindView(R.id.days_textView)
    TextView mDaysTextView;
    @BindView(R.id.birthday_textView)
    TextView mBirthdayTextView;
    @BindView(R.id.zodiac_textView)
    TextView mZodiacTextView;
    @BindView(R.id.gift_textView)
    TextView mGiftTextView;
    @BindView(R.id.zodiac_Layout)
    LinearLayout mZodiacLayout;
    @BindView(R.id.type_textView)
    TextView mTypeTextView;
    @BindView(R.id.profile_header_imageView)
    ImageView mProfileHeaderImageView;
    @BindView(R.id.profile_imageView)
    ImageView mProfileImageView;
    private BirthdaysInfo mBirthdaysInfo;
    private AppDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initViews();
        setValues();
    }

    private void initViews() {
        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        mDaysTextView.setSelected(true);
        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.IntentKey.BIRTHDAY_DATA)) {
            mBirthdaysInfo = intent.getParcelableExtra(AppConstants.IntentKey.BIRTHDAY_DATA);
        }
    }

    private void setValues() {
        setUpToolbar();
        mNameTextView.setText(mBirthdaysInfo.getName());
        if (mBirthdaysInfo.getType().equalsIgnoreCase(AppConstants.TYPE_BIRTHDAY)) {
            String birthdayMonth = birthdayMonth(mBirthdaysInfo.getBirthday());
            int date = Integer.parseInt(mBirthdaysInfo.getBirthday().substring(0, 2));
            if (birthdayMonth != null) {
                String zodiacSign = getZodiacSign(date, birthdayMonth);
                mZodiacTextView.setText(zodiacSign);
            }
        } else {
            mZodiacLayout.setVisibility(View.GONE);
            mTypeTextView.setText(R.string.anniversary_date);
            mProfileHeaderImageView.setBackgroundResource(R.drawable.anniversary_background);
            mProfileImageView.setBackgroundResource(R.drawable.couple_icon);
        }
        mBirthdayTextView.setText(getFullBirthday(mBirthdaysInfo.getBirthday()));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date birthDate = null;
        try {
            birthDate = sdf.parse(mBirthdaysInfo.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (birthDate != null) {
            Age age = calculateAge(birthDate);
            int comingAge = age.getYears() + 1;
            if (mBirthdaysInfo.getType().equalsIgnoreCase(AppConstants.TYPE_BIRTHDAY)) {
                mDaysTextView.setText(getString(R.string.turns) + getString(R.string.space) + comingAge + getString(R.string.space) + getString(R.string.on) + getString(R.string.space) + getComingBirthdayFullFormat(mBirthdaysInfo.getBirthday()));
            } else {
                mDaysTextView.setText(getString(R.string.anniversary) + getString(R.string.space) + comingAge + getString(R.string.space) + getString(R.string.on) + getString(R.string.space) + getComingBirthdayFullFormat(mBirthdaysInfo.getBirthday()));
            }

        }


        mSendFabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.action_send_card) {
                    FireBaseAnalyticsUtil.getInstance(ProfileActivity.this).setAnalyticsEvent(FireBaseAnalyticsUtil.SEND_CARD);
                    Intent intent = new Intent(ProfileActivity.this, CardsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.action_send_wish) {
                    FireBaseAnalyticsUtil.getInstance(ProfileActivity.this).setAnalyticsEvent(FireBaseAnalyticsUtil.SEND_WISH);
                    Intent intent = new Intent(ProfileActivity.this, GreetingsActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        ProfileViewModelFactory factory = new ProfileViewModelFactory(mAppDatabase, mBirthdaysInfo.getBirthdayId());
        final ProfileViewModel viewModel
                = ViewModelProviders.of(this, factory).get(ProfileViewModel.class);
        viewModel.getBirthdaysInfo().observe(this, new Observer<BirthdaysInfo>() {
            @Override
            public void onChanged(@Nullable BirthdaysInfo birthdaysInfo) {
                viewModel.getBirthdaysInfo().removeObserver(this);
                if (birthdaysInfo != null) {
                    mNameTextView.setText(birthdaysInfo.getName());
                }
            }
        });


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.action_edit) {
            FireBaseAnalyticsUtil.getInstance(ProfileActivity.this).setAnalyticsEvent(FireBaseAnalyticsUtil.EDIT_PROFILE);
            edit();
            finish();
        } else if (itemId == R.id.action_delete) {
            FireBaseAnalyticsUtil.getInstance(ProfileActivity.this).setAnalyticsEvent(FireBaseAnalyticsUtil.DELETE_PROFILE);
            delete();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void delete() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.appDao().deleteBirthday(mBirthdaysInfo);
            }
        });
    }

    private void edit() {
        Intent intent = new Intent(ProfileActivity.this, AddBirthdayActivity.class);
        intent.putExtra(AppConstants.IntentKey.IS_EDIT, true);
        intent.putExtra(AppConstants.IntentKey.BIRTHDAY_DATA, mBirthdaysInfo);
        startActivity(intent);
    }
}
