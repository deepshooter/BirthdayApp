package com.deepshooter.birthdayapp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {


    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    OnKeyPreImeListener onKeyPreImeListener;

    public void setOnKeyPreImeListener(OnKeyPreImeListener onKeyPreImeListener) {
        this.onKeyPreImeListener = onKeyPreImeListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if(onKeyPreImeListener != null)
                onKeyPreImeListener.onBackPressed();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public interface OnKeyPreImeListener {
        void onBackPressed();
    }
}