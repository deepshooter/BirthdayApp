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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class BirthdayGreetingsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    View progressBar;
    Unbinder unbinder;

    private FirebaseFirestore mFireBaseFireStore;
    private BirthdayGreetingsAdapter birthdayGreetingsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_greetings, null);
        unbinder = ButterKnife.bind(this, view);
        setValues();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        loadCollections();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setValues() {
        birthdayGreetingsAdapter = new BirthdayGreetingsAdapter(getActivity());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(birthdayGreetingsAdapter);
    }

    private void loadCollections() {
        mFireBaseFireStore.collection(AppConstants.FirestoreKey.BIRTHDAY_COLLECTION_PATH)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> greetingListData = new ArrayList<>();
                        if (task.isSuccessful()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    greetingListData.add(document.getData().get(AppConstants.FirestoreKey.BIRTHDAY_WISH_FIELD).toString());
                                }

                        } else {
                            Timber.e(getString(R.string.error_getting_documents));
                        }
                        progressBar.setVisibility(View.GONE);
                        birthdayGreetingsAdapter.setBirthdayGreetingListData(greetingListData);
                    }
                });
    }
}
