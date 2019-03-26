package com.deepshooter.birthdayapp.permission;

import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;

import timber.log.Timber;

public class AppErrorListener implements PermissionRequestErrorListener {
  @Override
  public void onError(DexterError error) {
    Timber.e("There was an error: " + error.toString());
  }
}
