package com.deepshooter.birthdayapp.utils;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.deepshooter.birthdayapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class Util {


    public static void copy(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            vibrate(context);
        }
    }

    public static void share(Context context, String text) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_via)));
        vibrate(context);
    }

    private static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(40);
            }
        }
    }

    public static void rateApp(Context context) {
        try {
            Intent rateIntent = rateIntentForUrl("market://details", context);
            context.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", context);
            context.startActivity(rateIntent);
        }
    }

    private static Intent rateIntentForUrl(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public static void shareApp(Context context) {
        try {
            final String appPackageName = context.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_via)));
        } catch (Exception e) {
            //e.toString();
        }
    }


    public static void shareImage(Context context, View view) {

        Bitmap bitmap;
        View v1 = view.findViewById(R.id.content_card_view);
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/BirthdayApp");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "FILENAME-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.getApplicationContext().sendBroadcast(mediaScanIntent);

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(myDir + "/" + fname));
        context.startActivity(Intent.createChooser(share, "Share via"));

    }

}
