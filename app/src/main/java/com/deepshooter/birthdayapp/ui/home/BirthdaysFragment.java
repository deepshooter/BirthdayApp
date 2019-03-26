package com.deepshooter.birthdayapp.ui.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.deepshooter.birthdayapp.database.AppDatabase;
import com.deepshooter.birthdayapp.model.BirthdaysInfo;
import com.deepshooter.birthdayapp.ui.adapter.BirthdaysAdapter;
import com.deepshooter.birthdayapp.viemmodel.MainViewModel;
import com.deepshooter.birthdayapp.widget.AppWidgetService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class BirthdaysFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private List<BirthdaysInfo> mBirthdaysInfoList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birthdays, null);
        unbinder = ButterKnife.bind(this, view);
        initValues();
        return view;
    }

    private void initValues() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getBirthdaysList().observe(this, new Observer<List<BirthdaysInfo>>() {
            @Override
            public void onChanged(@Nullable List<BirthdaysInfo> birthdaysInfoList) {
                mBirthdaysInfoList = birthdaysInfoList;
                AppWidgetService.updateWidget(getActivity(), mBirthdaysInfoList);
                setValues();
            }
        });
    }

    private void setValues() {
        BirthdaysAdapter birthdaysAdapter = new BirthdaysAdapter(getActivity());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(birthdaysAdapter);
        birthdaysAdapter.setBirthdayData(mBirthdaysInfoList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
