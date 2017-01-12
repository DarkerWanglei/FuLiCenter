package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2017/1/11.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;


    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_boutique, null);
        return new BoutiqueViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiqueViewHolder bv = (BoutiqueViewHolder) holder;
        bv.mtvTitle.setText(mList.get(position).getTitle());
        bv.mtvName.setText(mList.get(position).getName());
        bv.mtvDescription.setText(mList.get(position).getDescription());

        ImageLoader.downloadImg(mContext, bv.mivBoutique, mList.get(position).getImageurl());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void initData(ArrayList<BoutiqueBean> list) {
        if (mList != null) {
            mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<BoutiqueBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    static class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutique)
        ImageView mivBoutique;
        @BindView(R.id.tvTitle)
        TextView mtvTitle;
        @BindView(R.id.tvName)
        TextView mtvName;
        @BindView(R.id.tvDescription)
        TextView mtvDescription;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
