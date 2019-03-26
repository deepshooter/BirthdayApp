package com.deepshooter.birthdayapp.ui.greetings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.ui.adapter.ViewPagerAdapter;
import com.deepshooter.birthdayapp.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GreetingsActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.transitions_container)
    LinearLayout mTransitionsContainer;
    private FirebaseFirestore mFireBaseFireStore;
    private ArrayList<String> mBirthdayWishList, mmAnniversaryWishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        ButterKnife.bind(this);
        initViews();
        getBirthdayGreetings();
    }


    private void initViews() {
        setUpToolbar();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
    }


    private void setUpTabLayout() {
        TransitionManager.beginDelayedTransition(mTransitionsContainer);
        mTabLayout.setVisibility(View.VISIBLE);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle birthdayBundle = new Bundle();
        birthdayBundle.putStringArrayList(AppConstants.IntentKey.BIRTHDAY_WISH, mBirthdayWishList);
        Bundle anniversaryBundle = new Bundle();
        anniversaryBundle.putStringArrayList(AppConstants.IntentKey.ANNIVERSARY_WISH, mmAnniversaryWishList);
        BirthdayGreetingsFragment birthdayGreetingsFragment = new BirthdayGreetingsFragment();
        birthdayGreetingsFragment.setArguments(birthdayBundle);
        AnniversaryGreetingsFragment anniversaryGreetingsFragment = new AnniversaryGreetingsFragment();
        anniversaryGreetingsFragment.setArguments(anniversaryBundle);
        mViewPagerAdapter.addFragment(birthdayGreetingsFragment, getString(R.string.birthday));
        mViewPagerAdapter.addFragment(anniversaryGreetingsFragment, getString(R.string.anniversary));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.greetings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBirthdayGreetings() {

        mProgressBar.setVisibility(View.VISIBLE);
        final ArrayList<String> birthdayDataList = new ArrayList<>();
        mFireBaseFireStore.collection(AppConstants.FirestoreKey.BIRTHDAY_COLLECTION_PATH)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    birthdayDataList.add(document.getData().get(AppConstants.FirestoreKey.BIRTHDAY_WISH_FIELD).toString());
                                }

                        } else {
                            Timber.e(getString(R.string.error_getting_documents));
                        }
                        mBirthdayWishList = birthdayDataList;
                        mProgressBar.setVisibility(View.GONE);
                        getAnniversaryGreetings();
                    }
                });
    }

    private void getAnniversaryGreetings() {
        mProgressBar.setVisibility(View.VISIBLE);
        final ArrayList<String> anniversaryDataList = new ArrayList<>();
        mFireBaseFireStore.collection(AppConstants.FirestoreKey.ANNIVERSARY_COLLECTION_PATH)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    anniversaryDataList.add(document.getData().get(AppConstants.FirestoreKey.ANNIVERSARY_WISH_FIELD).toString());
                                }
                            mmAnniversaryWishList = anniversaryDataList;

                            setUpTabLayout();
                        } else {
                            Timber.e(getString(R.string.error_getting_documents));
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }


}
