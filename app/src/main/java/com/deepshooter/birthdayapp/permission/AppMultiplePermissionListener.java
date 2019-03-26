
package com.deepshooter.birthdayapp.permission;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class AppMultiplePermissionListener implements MultiplePermissionsListener {

  private final PermissionActivity activity;

  public AppMultiplePermissionListener(PermissionActivity activity) {
    this.activity = activity;
  }

  @Override
  public void onPermissionsChecked(MultiplePermissionsReport report) {
    for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
      activity.showPermissionGranted(response.getPermissionName());
    }

    for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
      activity.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
    }
  }

  @Override
  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                 PermissionToken token) {
    activity.showPermissionRationale(token);
  }
}
