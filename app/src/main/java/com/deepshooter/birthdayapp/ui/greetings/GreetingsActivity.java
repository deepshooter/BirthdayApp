package com.deepshooter.birthdayapp.ui.greetings;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.base.BaseActivity;
import com.deepshooter.birthdayapp.ui.adapter.ViewPagerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GreetingsActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private FirebaseFirestore mFireBaseFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        ButterKnife.bind(this);
        initViews();
        //getBirthdayGreetings();
    }


    private void initViews() {
        setUpToolbar();
        setUpTabLayout();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
    }


    private void setUpTabLayout() {
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new BirthdayGreetingsFragment(), getString(R.string.birthday));
        mViewPagerAdapter.addFragment(new AnniversaryGreetingsFragment(), getString(R.string.anniversary));
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

    /*private void getBirthdayGreetings() {

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
    }*/


}
