package com.deepshooter.birthdayapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.deepshooter.birthdayapp.R;

public class ToastUtils {

    public static void showToast(String text, Context context){
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.plain_toast_layout, null);
        TextView toastTextView = view.findViewById(R.id.textViewToast);
        toastTextView.setText(text);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

}