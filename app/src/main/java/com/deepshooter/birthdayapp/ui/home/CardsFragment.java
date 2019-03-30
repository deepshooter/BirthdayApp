package com.deepshooter.birthdayapp.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.ui.adapter.CardsAdapter;
import com.deepshooter.birthdayapp.utils.DataUtils;
import com.deepshooter.birthdayapp.utils.ItemDecorationAlbumColumns;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.deepshooter.birthdayapp.utils.AppConstants.CARD_GRID_SIZE;

public class CardsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, null);
        unbinder = ButterKnife.bind(this, view);
        setValues();
        return view;
    }

    private void setValues() {
        CardsAdapter cardsAdapter = new CardsAdapter(getActivity());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecyclerView.setAdapter(cardsAdapter);
        cardsAdapter.setCardData(DataUtils.cardList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
