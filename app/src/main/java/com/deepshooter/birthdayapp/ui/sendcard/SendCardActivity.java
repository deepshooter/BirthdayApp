package com.deepshooter.birthdayapp.ui.sendcard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.CustomEditText;
import com.deepshooter.birthdayapp.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.deepshooter.birthdayapp.utils.ToastUtils.showToast;

public class SendCardActivity extends BaseActivity {

    @BindView(R.id.card_imageView)
    ImageView mCardImageView;
    @BindView(R.id.message_editText)
    CustomEditText mMessageEditText;
    @BindView(R.id.message_textView)
    TextView mMessageTextView;
    @BindView(R.id.content_card_view)
    CardView mContentCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_card);
        ButterKnife.bind(this);
        initViews();
        setValues();
    }

    private void initViews() {
        mMessageEditText.setFocusable(true);
        mMessageEditText.requestFocus();
    }

    private void setValues() {
        setUpToolbar();
        moveTextView();

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.IntentKey.CARD)) {
            Integer imageResource = intent.getIntExtra(AppConstants.IntentKey.CARD, R.drawable.balloons);
            mCardImageView.setBackground(getDrawable(imageResource));
        }

        mMessageEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mMessageTextView.setText(s);
            }
        });

        CustomEditText.OnKeyPreImeListener onKeyPreImeListener = new CustomEditText.OnKeyPreImeListener() {
            @Override
            public void onBackPressed() {
                finish();
            }
        };

        mMessageEditText.setOnKeyPreImeListener(onKeyPreImeListener);


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.action_send) {
            shareCard();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_card_menu, menu);
        return true;
    }

    private void shareCard() {
        if (mMessageEditText.getText() != null) {
            String text = mMessageEditText.getText().toString().trim();
            if (!text.isEmpty()) {
                Util.shareImage(this, mContentCardView);
                mMessageEditText.setText("");
                finish();
            } else {
                showToast(getString(R.string.please_enter_text), this);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void moveTextView() {
        mMessageTextView.setOnTouchListener(new View.OnTouchListener() {
            float lastX = 0, lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        lastX = event.getX();
                        lastY = event.getY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - lastX;
                        float dy = event.getY() - lastY;
                        float finalX = v.getX() + dx;
                        float finalY = v.getY() + dy + v.getHeight();
                        v.setX(finalX);
                        v.setY(finalY);
                        break;
                }
                return true;
            }
        });
    }

}
