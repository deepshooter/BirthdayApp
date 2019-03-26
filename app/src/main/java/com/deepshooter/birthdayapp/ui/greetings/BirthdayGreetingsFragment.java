package com.deepshooter.birthdayapp.ui.greetings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.ui.adapter.BirthdayGreetingsAdapter;
import com.deepshooter.birthdayapp.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BirthdayGreetingsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_greetings, null);
        unbinder = ButterKnife.bind(this, view);
        setValues();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setValues() {
        if (getArguments() != null) {
            ArrayList<String> stringArrayList = getArguments().getStringArrayList(AppConstants.IntentKey.BIRTHDAY_WISH);
            BirthdayGreetingsAdapter birthdayGreetingsAdapter = new BirthdayGreetingsAdapter(getActivity());
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(birthdayGreetingsAdapter);
            birthdayGreetingsAdapter.setBirthdayGreetingListData(stringArrayList);
        }

    }
}
