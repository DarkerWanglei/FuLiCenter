package cn.ucai.fulicenter.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CartAdapter;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;
import cn.ucai.fulicenter.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    LinearLayoutManager linearLayoutManager;
    CartAdapter mAdapter;
    ArrayList<CartBean> mList;
    IModelUser mModel;
    User user;
    UpdateReceiver mReceiver;
    int sumPrice;
    int payPrice;

    @BindView(R.id.tv_cart_buy)
    TextView tvCartBuy;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.tv_nothing)
    TextView tvNothing;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tvSumPrice)
    TextView tvSumPrice;
    @BindView(R.id.tvSavePrice)
    TextView tvSavePrice;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        initView();
        mModel = new ModelUser();
        user = FuLiCenterApplication.getUser();
        initData(I.ACTION_DOWNLOAD);
        setListener();
        setReceiverListener();
        return layout;
    }

    private void setReceiverListener() {
        mReceiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setPrice();
    }

    private void setListener() {
        setPullDownListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }


    private void initData(final int action) {
        if (user != null) {
            mModel.getCart(getContext(), user.getMuserName(), new onCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    srl.setVisibility(View.VISIBLE);
                    tvNothing.setVisibility(View.GONE);
                    if (result != null && result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mList.addAll(list);
                        switch (action) {
                            case I.ACTION_DOWNLOAD:
                                mAdapter.initData(list);
                                break;
                            case I.ACTION_PULL_DOWN:
                                srl.setRefreshing(false);
                                tvRefresh.setVisibility(View.GONE);
                                mAdapter.initData(list);
                                break;
                            default:
                                srl.setVisibility(View.GONE);
                                tvNothing.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                }
            });
        }
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_blue)
        );
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(getContext(), mList);
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SpaceItemDecoration(15));
    }

    @OnClick(R.id.tv_nothing)
    public void onClick() {
        initData(I.ACTION_DOWNLOAD);
    }

    @OnClick(R.id.tv_cart_buy)
    public void onOrderClick() {
        if (sumPrice > 0) {
            MFGT.gotoOrder(getActivity(), payPrice);
        } else {
            CommonUtils.showLongToast(R.string.order_nothing);
        }
    }


    private void setPrice() {
        sumPrice = 0;
        payPrice = 0;
        int savePrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean cart : mList) {
                GoodsDetailsBean goods = cart.getGoods();
                if (cart.isChecked() && goods != null) {
                    sumPrice += cart.getCount() * getPrice(goods.getCurrencyPrice());
                    savePrice += cart.getCount() *
                            (getPrice(goods.getCurrencyPrice()) - getPrice(goods.getRankPrice()));
                }
            }
            tvSumPrice.setText(sumPrice + "");
            tvSavePrice.setText(savePrice + "");
            mAdapter.notifyDataSetChanged();
            payPrice = sumPrice - savePrice;
        }
    }

    int getPrice(String price) {
        return Integer.valueOf(price.substring(price.indexOf("￥") + 1));
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("main", "onReceive");
            setPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }
}
