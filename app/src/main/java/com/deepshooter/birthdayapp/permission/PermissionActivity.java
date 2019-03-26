package com.deepshooter.birthdayapp.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.ui.home.HomeActivity;
import com.deepshooter.birthdayapp.utils.ContactUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PermissionActivity extends BaseActivity {


    @BindView(R.id.storage_permission_feedback)
    TextView storagePermissionFeedbackView;
    @BindView(R.id.contacts_permission_feedback)
    TextView contactsPermissionFeedbackView;
    @BindView(android.R.id.content)
    View contentView;
    @BindView(R.id.done_button)
    Button mDoneButton;

    private MultiplePermissionsListener allPermissionsListener;
    private PermissionListener cameraPermissionListener;
    private PermissionListener contactsPermissionListener;
    private PermissionRequestErrorListener errorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
        createPermissionListeners();
        setValues();
    }

    @OnClick(R.id.all_permissions_button)
    public void onAllPermissionsButtonClicked() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS)
                .withListener(allPermissionsListener)
                .withErrorListener(errorListener)
                .check();
    }

    @OnClick(R.id.storage_permission_button)
    public void onStoragePermissionButtonClicked() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dexter.withActivity(PermissionActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(cameraPermissionListener)
                        .withErrorListener(errorListener)
                        .onSameThread()
                        .check();
            }
        }).start();
    }

    @OnClick(R.id.contacts_permission_button)
    public void onContactsPermissionButtonClicked() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(contactsPermissionListener)
                .withErrorListener(errorListener)
                .check();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(final PermissionToken token) {
        new AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }

    public void showPermissionGranted(String permission) {
        TextView feedbackView = getFeedbackViewForPermission(permission);
        feedbackView.setText(R.string.permission_granted_feedback);
        feedbackView.setTextColor(ContextCompat.getColor(this, R.color.permission_granted));
    }

    public void showPermissionDenied(String permission, boolean isPermanentlyDenied) {
        TextView feedbackView = getFeedbackViewForPermission(permission);
        feedbackView.setText(isPermanentlyDenied ? R.string.permission_permanently_denied_feedback
                : R.string.permission_denied_feedback);
        feedbackView.setTextColor(ContextCompat.getColor(this, R.color.permission_denied));
    }

    private void createPermissionListeners() {
        PermissionListener feedbackViewPermissionListener = new AppPermissionListener(this);
        MultiplePermissionsListener feedbackViewMultiplePermissionListener =
                new AppMultiplePermissionListener(this);

        allPermissionsListener =
                new CompositeMultiplePermissionsListener(feedbackViewMultiplePermissionListener,
                        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(contentView,
                                R.string.all_permissions_denied_feedback)
                                .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                                .build());
        contactsPermissionListener = new CompositePermissionListener(feedbackViewPermissionListener,
                SnackbarOnDeniedPermissionListener.Builder.with(contentView,
                        R.string.contacts_permission_denied_feedback)
                        .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                        .withCallback(new Snackbar.Callback() {
                            @Override
                            public void onShown(Snackbar snackbar) {
                                super.onShown(snackbar);
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                            }
                        })
                        .build());
        cameraPermissionListener = new AppBackgroundThreadPermissionListener(this);

        errorListener = new AppErrorListener();
    }

    private TextView getFeedbackViewForPermission(String name) {
        TextView feedbackView;

        switch (name) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                feedbackView = storagePermissionFeedbackView;
                break;
            case Manifest.permission.READ_CONTACTS:
                feedbackView = contactsPermissionFeedbackView;
                break;
            default:
                throw new RuntimeException("No feedback view for this permission");
        }

        return feedbackView;
    }

    private void setValues() {
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PermissionActivity.this, HomeActivity.class));
                ContactUtil.getContactList(PermissionActivity.this);
                finish();
            }
        });
    }
}
