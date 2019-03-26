package com.deepshooter.birthdayapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.ui.sendcard.SendCardActivity;
import com.deepshooter.birthdayapp.utils.AppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    private Context mContext;
    private List<Integer> mCardList;

    public CardsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CardsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cards, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CardsViewHolder holder,  int i) {

        holder.mCardImageView.setBackgroundResource(mCardList.get(i));

        holder.mCardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SendCardActivity.class);
                intent.putExtra(AppConstants.IntentKey.CARD,mCardList.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mCardList) return 0;
        return mCardList.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_imageView)
        ImageView mCardImageView;

        public CardsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setCardData(List<Integer> cardList) {
        mCardList = cardList;
        notifyDataSetChanged();
    }
}
