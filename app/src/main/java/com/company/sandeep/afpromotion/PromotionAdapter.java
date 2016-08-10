package com.company.sandeep.afpromotion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import pojo.Promotions;

/**
 * Created by chsan_000 on 8/3/2016.
 */
public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {
    private Context mContext;
    List<Promotions> mList;
    int resource;
    private static String TAG = "PromotionAdapter";

    public PromotionAdapter(List<Promotions> mList, int resource, Context mContext) {
        this.mContext = mContext;
        this.mList = mList;
        this.resource = resource;
    }

    @Override
    public PromotionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.promotion_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textTitle.setText(mList.get(position).getTitle());
        Glide.with(mContext).load(mList.get(position).getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imagePromotion);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        public ImageView imagePromotion;
        public TextView textTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagePromotion = (ImageView) itemView.findViewById(R.id.image_promotion);
            textTitle = (TextView) itemView.findViewById(R.id.image_title);
        }
    }
}
