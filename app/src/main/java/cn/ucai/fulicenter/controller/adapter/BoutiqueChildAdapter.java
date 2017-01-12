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
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/12.
 */

public class BoutiqueChildAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;


    public BoutiqueChildAdapter(Context mContext, ArrayList<BoutiqueBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_boutique_child, null);
        return new BoutiqueChildViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        BoutiqueChildViewHolder bcv = (BoutiqueChildViewHolder) holder;
//        bcv.tvTitle.setText(mList.get(position).getTitle());

//        bcv.tvName.setText(mList.get(position).getName());
//        bcv.tvDescription.setText(mList.get(position).getDescription());
//        ImageLoader.downloadImg(mContext, bcv.ivBoutique, mList.get(position).getImageurl());
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

    static class BoutiqueChildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutique)
        ImageView ivBoutique;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        BoutiqueChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
