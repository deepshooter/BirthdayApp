package com.deepshooter.birthdayapp.widget;

import android.content.Context;
import android.support.design.card.MaterialCardView;
import android.util.AttributeSet;

public class WidthFitCardView extends MaterialCardView {
    public WidthFitCardView(Context context) {
        super(context);
    }

    public WidthFitCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidthFitCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
