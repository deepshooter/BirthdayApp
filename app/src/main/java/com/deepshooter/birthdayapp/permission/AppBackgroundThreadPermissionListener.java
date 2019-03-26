package com.deepshooter.birthdayapp.permission;

import android.os.Handler;
import android.os.Looper;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

/**
 * Sample listener that shows how to handle permission request callbacks on a background thread
 */
public class AppBackgroundThreadPermissionListener extends AppPermissionListener {

  private Handler handler = new Handler(Looper.getMainLooper());

  public AppBackgroundThreadPermissionListener(PermissionActivity activity) {
    super(activity);
  }

  @Override
  public void onPermissionGranted(final PermissionGrantedResponse response) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        AppBackgroundThreadPermissionListener.super.onPermissionGranted(response);
      }
    });
  }

  @Override
  public void onPermissionDenied(final PermissionDeniedResponse response) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        AppBackgroundThreadPermissionListener.super.onPermissionDenied(response);
      }
    });
  }

  @Override
  public void onPermissionRationaleShouldBeShown(final PermissionRequest permission,
                                                 final PermissionToken token) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        AppBackgroundThreadPermissionListener.super.onPermissionRationaleShouldBeShown(
            permission, token);
      }
    });
  }
}
