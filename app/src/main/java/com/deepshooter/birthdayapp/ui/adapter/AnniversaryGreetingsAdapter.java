package com.deepshooter.birthdayapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepshooter.birthdayapp.R;
import com.deepshooter.birthdayapp.utils.FireBaseAnalyticsUtil;
import com.deepshooter.birthdayapp.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnniversaryGreetingsAdapter extends RecyclerView.Adapter<AnniversaryGreetingsAdapter.AnniversaryGreetingsViewHolder> {

    private Context mContext;
    private List<String> mAnniversaryGreetingList;

    public AnniversaryGreetingsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AnniversaryGreetingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AnniversaryGreetingsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_greetings, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AnniversaryGreetingsViewHolder holder, int i) {

        holder.mGreetingsTextView.setText(mAnniversaryGreetingList.get(i));

        holder.mCopyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseAnalyticsUtil.getInstance(mContext).setAnalyticsEvent(FireBaseAnalyticsUtil.COPY_WISH);
                String text = holder.mGreetingsTextView.getText().toString();
                Util.copy(mContext, text);

            }
        });

        holder.mShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseAnalyticsUtil.getInstance(mContext).setAnalyticsEvent(FireBaseAnalyticsUtil.SHARE_WISH);
                String text = holder.mGreetingsTextView.getText().toString();
                Util.share(mContext, text);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mAnniversaryGreetingList) return 0;
        return mAnniversaryGreetingList.size();
    }

    public class AnniversaryGreetingsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.greetings_textView)
        TextView mGreetingsTextView;
        @BindView(R.id.copy_imageView)
        ImageView mCopyImageView;
        @BindView(R.id.share_imageView)
        ImageView mShareImageView;

        public AnniversaryGreetingsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void setAnniversaryGreetingListData(List<String> anniversaryGreetingList) {
        mAnniversaryGreetingList = anniversaryGreetingList;
        notifyDataSetChanged();
    }

}