package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/19.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    IModelUser mModelUser;
    User user;

    public CartAdapter(Context mContext, ArrayList<CartBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mModelUser = new ModelUser();
        user = FuLiCenterApplication.getUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_cart, null);
        return new CartViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder cvh = (CartViewHolder) holder;
        cvh.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<CartBean> list) {
        if (mList != null) {
            mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<CartBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chkSelect)
        CheckBox chkSelect;
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvCartCount)
        TextView tvCartCount;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;

        int listPosition;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            listPosition = position;
            chkSelect.setChecked(mList.get(listPosition).isChecked());
            GoodsDetailsBean goods = mList.get(position).getGoods();
            if (goods != null) {
                ImageLoader.downloadImg(mContext, ivGoodsThumb, goods.getGoodsThumb());
                tvGoodsName.setText(goods.getGoodsName());
                tvGoodsPrice.setText(goods.getCurrencyPrice());
            }
            tvCartCount.setText("(" + mList.get(position).getCount() + ")");
        }

        @OnCheckedChanged(R.id.chkSelect)
        public void checkListener(boolean checked) {
            mList.get(listPosition).setChecked(checked);
            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
        }

        @OnClick(R.id.ivAddCart)
        public void addCart() {
            mModelUser.updateCart(mContext, I.ACTION_CART_ADD, user.getMuserName(),
                    mList.get(listPosition).getGoodsId(), 1, mList.get(listPosition).getId(), new onCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                mList.get(listPosition).setCount(mList.get(listPosition).getCount() + 1);
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
//                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }

        @OnClick(R.id.ivReduceCart)
        public void delCart() {
            int action = I.ACTION_CART_UPDATE;
            final int count = mList.get(listPosition).getCount();
            if (count <= 1) {
                action = I.ACTION_CART_DELETE;
            }
            mModelUser.updateCart(mContext, action, user.getMuserName(),
                    mList.get(listPosition).getGoodsId(), count - 1, mList.get(listPosition).getId(), new onCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                if (count <= 1) {
                                    mList.remove(listPosition);
                                } else {
                                    mList.get(listPosition).setCount(count - 1);
                                }
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
//                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
