package com.deepshooter.birthdayapp.ui.addbirthday;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.database.AppExecutors;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.DataUtils;
import com.deepshooter.birthdayapp.utils.DateUtils;
import com.deepshooter.birthdayapp.viemmodel.MainViewModel;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.deepshooter.birthdayapp.utils.ToastUtils.showToast;

public class AddBirthdayActivity extends BaseActivity implements EnterDateDialogFragment.OnSaveClickListener {

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.person_name_textView)
    AutoCompleteTextView mPersonNameTextView;
    @BindView(R.id.date_editText)
    EditText mDateEditText;
    @BindView(R.id.save_Button)
    Button mSaveButton;
    private boolean isBirthday, isEdit;
    private List<String> mContactsNameList;
    private String mType;
    private AppDatabase mAppDatabase;
    private BirthdaysInfo mBirthdaysInfo;
    private String mBirthDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.IntentKey.IS_BIRTHDAY)) {
            isBirthday = intent.getBooleanExtra(AppConstants.IntentKey.IS_BIRTHDAY, true);
        }
        if (intent.hasExtra(AppConstants.IntentKey.IS_EDIT)) {
            isEdit = intent.getBooleanExtra(AppConstants.IntentKey.IS_EDIT, false);
            mBirthdaysInfo = intent.getParcelableExtra(AppConstants.IntentKey.BIRTHDAY_DATA);
            if (mBirthdaysInfo.getType().equalsIgnoreCase(AppConstants.TYPE_BIRTHDAY)) {
                isBirthday = true;
            }
            mPersonNameTextView.setText(mBirthdaysInfo.getName());
            mDateEditText.setText(mBirthdaysInfo.getBirthday());

        }

        initViews();
        setUpToolbar();
    }

    private void initViews() {

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getContactNamesList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> contactsNameList) {
                mContactsNameList = contactsNameList;
                Timber.i(getString(R.string.total_contact_list_size), mContactsNameList.size());
                setValues();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

    }

    private void setValues() {
        if (isBirthday) {
            mPersonNameTextView.setHint(R.string.name_of_the_person);
            mType = AppConstants.TYPE_BIRTHDAY;
        } else {
            mPersonNameTextView.setHint(R.string.anniversary_description);
            mImageView.setBackgroundResource(R.drawable.ic_toast);
            mType = AppConstants.TYPE_ANNIVERSARY;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, mContactsNameList);
        mPersonNameTextView.setThreshold(1);
        mPersonNameTextView.setAdapter(adapter);

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void saveInfo() {

        final String personName = mPersonNameTextView.getText().toString().trim();
        final String date = mDateEditText.getText().toString().trim();

        if (personName.isEmpty()) {
            if (isBirthday) {
                showToast(getString(R.string.please_enter_name), this);
            } else {
                showToast(getString(R.string.please_enter_anniversary), this);
            }
        } else if (date.isEmpty()) {
            showToast(getString(R.string.please_enter_date), this);
        } else {

            final BirthdaysInfo birthdaysInfo = new BirthdaysInfo(personName, mBirthDate, mType);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (!isEdit) {
                        mAppDatabase.appDao().insertBirthday(birthdaysInfo);
                    } else {
                        BirthdaysInfo info = mBirthdaysInfo;
                        info.setBirthday(date);
                        info.setName(personName);
                        mAppDatabase.appDao().update(info);
                    }

                    finish();
                }
            });


            if (!isEdit) {
                showToast(personName + " " + getString(R.string.added), this);
            } else {
                showToast(getString(R.string.info_updated), this);
            }

        }


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (isBirthday) {
            if (!isEdit) {
                setTitle(R.string.add_new_birthday);
            } else {
                setTitle(R.string.edit_birthday);
            }
        } else {
            if (!isEdit) {
                setTitle(R.string.add_new_anniversary);
            } else {
                setTitle(R.string.edit_anniversary);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.action_save) {
            saveInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_birthday_menu, menu);
        return true;
    }

    private void showDateDialog() {
        Bundle args = new Bundle();
        args.putBoolean(AppConstants.IntentKey.IS_BIRTHDAY, isBirthday);
        args.putBoolean(AppConstants.IntentKey.IS_EDIT, isEdit);
        if (isEdit && mBirthdaysInfo != null) {
            args.putString(AppConstants.IntentKey.DATE, mBirthdaysInfo.getBirthday());
        }
        EnterDateDialogFragment enterDateDialogFragment = new EnterDateDialogFragment();
        enterDateDialogFragment.setArguments(args);
        enterDateDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onComplete(String birthday) {
        mBirthDate = birthday;
        mDateEditText.setText(DateUtils.convertDateInMonthFormat(birthday));
    }
}
