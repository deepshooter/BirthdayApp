package com.deepshooter.birthdayapp.ui.choosecard;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.ui.home.CardsFragment;

import butterknife.ButterKnife;

public class CardsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        initViews();
        setValues();
    }

    private void initViews() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardsFragment cardsFragment = new CardsFragment();
        fragmentTransaction.replace(R.id.container, cardsFragment);
        fragmentTransaction.commit();
    }

    private void setValues() {
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.choose_a_card));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
