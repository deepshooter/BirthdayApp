package com.deepshooter.birthdayapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.ui.adapter.ViewPagerAdapter;
import com.deepshooter.birthdayapp.ui.addbirthday.AddBirthdayActivity;
import com.deepshooter.birthdayapp.ui.greetings.GreetingsActivity;
import com.deepshooter.birthdayapp.ui.settings.SettingsActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.deepshooter.birthdayapp.utils.FireBaseAnalyticsUtil;
import com.deepshooter.birthdayapp.utils.SharedPreferenceUtil;
import com.deepshooter.birthdayapp.utils.Util;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {


    @BindView(R.id.fab_speed_dial)
    FabSpeedDial mFabSpeedDial;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setUpNavigationDrawer();
        setValues();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setValues() {
        setUpTabLayout();
        mFabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_new_birthday) {
                    FireBaseAnalyticsUtil.getInstance(HomeActivity.this).setAnalyticsEvent(FireBaseAnalyticsUtil.ADD_BIRTHDAY);
                    Intent intent = new Intent(HomeActivity.this, AddBirthdayActivity.class);
                    intent.putExtra(AppConstants.IntentKey.IS_BIRTHDAY, true);
                    startActivity(intent);
                } else if (id == R.id.action_new_anniversary) {
                    FireBaseAnalyticsUtil.getInstance(HomeActivity.this).setAnalyticsEvent(FireBaseAnalyticsUtil.ADD_ANNIVERSARY);
                    Intent intent = new Intent(HomeActivity.this, AddBirthdayActivity.class);
                    intent.putExtra(AppConstants.IntentKey.IS_BIRTHDAY, false);
                    startActivity(intent);
                }

                return true;
            }
        });

        if (SharedPreferenceUtil.getBirthdaysFromSharedPreferences(this) == null || SharedPreferenceUtil.getBirthdaysFromSharedPreferences(this).size() == 0) {
            initializeTapTargetView();
        }
    }

    private void setUpTabLayout() {
        mTabLayout.setVisibility(View.VISIBLE);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_greetings) {
            startActivity(new Intent(this, GreetingsActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {
            Util.shareApp(this);
        } else if (id == R.id.nav_rate_us) {
            Util.rateApp(this);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeTapTargetView() {
        final SpannableString spannedDesc = new SpannableString(getString(R.string.never_forget_to_wish_your_loved_ones));
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fab_speed_dial), getString(R.string.add_birthdays_and_anniversaries), spannedDesc)
                .cancelable(true)
                .drawShadow(true)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                if (!mFabSpeedDial.isMenuOpen())
                    mFabSpeedDial.openMenu();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                if (!mFabSpeedDial.isMenuOpen())
                    mFabSpeedDial.openMenu();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());

        if (tab.getPosition() == 0) {
            mFabSpeedDial.setVisibility(View.VISIBLE);
        } else if (tab.getPosition() == 1) {
            mFabSpeedDial.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BirthdaysFragment(), getString(R.string.birthdays));
        adapter.addFragment(new CardsFragment(), getString(R.string.cards));
        viewPager.setAdapter(adapter);
    }

}

