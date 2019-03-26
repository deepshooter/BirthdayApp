package com.deepshooter.birthdayapp.ui.addbirthday;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.deepshooter.birthdayapp.utils.ToastUtils.showToast;

public class EnterDateDialogFragment extends DialogFragment {


    Dialog dialog;
    boolean isBirthDay;
    @BindView(R.id.dialog_title_textView)
    TextView mDialogTitleTextView;
    @BindView(R.id.dialog_month_editText)
    EditText mDialogMonthEditText;
    @BindView(R.id.dialog_date_editText)
    EditText mDialogDateEditText;
    @BindView(R.id.dialog_year_editText)
    EditText mDialogYearEditText;
    @BindView(R.id.dialog_subTitle_textView)
    TextView mDialogSubTitleTextView;
    @BindView(R.id.dialog_age_editText)
    EditText mDialogAgeEditText;
    @BindView(R.id.dialog_cancel_button)
    Button mDialogCancelButton;
    @BindView(R.id.dialog_save_button)
    Button mDialogSaveButton;
    Unbinder unbinder;
    private OnSaveClickListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnSaveClickListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_enter_date);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        unbinder = ButterKnife.bind(this, dialog);
        initializeValue();

        return dialog;
    }


    private void initializeValue() {

        setValues();
        editTextMovements();
    }

    private void setValues() {

        mDialogMonthEditText.requestFocus();

        if (getArguments() != null) {
            isBirthDay = getArguments().getBoolean(AppConstants.IntentKey.IS_BIRTHDAY);
            boolean isEdit = getArguments().getBoolean(AppConstants.IntentKey.IS_EDIT);
            if (isEdit) {
                String date = getArguments().getString(AppConstants.IntentKey.DATE);
                if (date != null && date.length() == 10) {
                    mDialogMonthEditText.setText(date.substring(0, 2));
                    mDialogDateEditText.setText(date.substring(3, 5));
                    mDialogYearEditText.setText(date.substring(6, 10));
                    mDialogYearEditText.requestFocus();
                }
            }
        }

        if (isBirthDay) {
            mDialogTitleTextView.setText(getString(R.string.enter_birthday));
            mDialogSubTitleTextView.setText(getString(R.string.don_t_know_the_year_of_birth_type_in_the_age));
            mDialogAgeEditText.setHint(getString(R.string.age));
        } else {
            mDialogTitleTextView.setText(R.string.enter_date);
            mDialogSubTitleTextView.setText(getString(R.string.dont_know_the_year_which_anniversary_year_was_the_latest));
            mDialogAgeEditText.setHint(getString(R.string.last_anniversary));
        }

        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mDialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String month = mDialogMonthEditText.getText().toString().trim();
                String date = mDialogDateEditText.getText().toString().trim();
                String year = mDialogYearEditText.getText().toString().trim();
                String age = mDialogAgeEditText.getText().toString().trim();

                if (month.isEmpty() || date.isEmpty()) {
                    showToast(getString(R.string.please_enter_date), getActivity());
                } else if (Integer.parseInt(month) > 12) {
                    showToast(getString(R.string.please_enter_correct_month), getActivity());
                } else if (Integer.parseInt(date) > 31) {
                    showToast(getString(R.string.please_enter_correct_date), getActivity());
                } else if (year.isEmpty() && age.isEmpty()) {
                    showToast(getString(R.string.please_enter_correct_date), getActivity());
                } else {

                    String birthday = "";
                    if (!year.isEmpty() && year.length() == 4 && age.isEmpty()) {
                        birthday = month + getString(R.string.dash) + date + getString(R.string.dash) + year;
                    } else if (year.isEmpty() || year.length() < 4 && !age.isEmpty()) {
                        birthday = month + getString(R.string.dash) + date + getString(R.string.dash) + calculateYearOfBirthFromAge(Integer.parseInt(age));
                    } else {
                        birthday = month + getString(R.string.dash) + date + getString(R.string.dash) + year;
                    }

                    mListener.onComplete(birthday);
                    dialog.dismiss();

                }

            }
        });

    }


    private void editTextMovements() {
        mDialogMonthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    mDialogDateEditText.requestFocus();
                }
            }
        });

        mDialogDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    mDialogYearEditText.requestFocus();
                }
                if (s.length() == 0) {
                    mDialogMonthEditText.requestFocus();
                }
            }
        });

        mDialogYearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mDialogDateEditText.requestFocus();
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    interface OnSaveClickListener {
        void onComplete(String birthday);
    }

    private String calculateYearOfBirthFromAge(int age) {

        int totalDays = age * 365;
        Date c = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(c);
        cal.add(Calendar.DATE, -totalDays);
        Date dateBefore30Days = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String formattedDate = df.format(dateBefore30Days);
        return formattedDate;
    }

}
