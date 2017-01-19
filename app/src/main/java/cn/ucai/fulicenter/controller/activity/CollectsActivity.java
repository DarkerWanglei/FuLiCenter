package cn.ucai.fulicenter.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CollectAdapter;
import cn.ucai.fulicenter.controller.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;
import cn.ucai.fulicenter.view.DisplayUtils;

public class CollectsActivity extends AppCompatActivity {

    @BindView(R.id.tv_refresh)
    TextView mtvRefresh;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;

    IModelUser mModelUser;
    User user;
    int mPageId = 1;
    CollectAdapter mAdapter;
    GridLayoutManager mGm;
    ArrayList<CollectBean> mList;
    UpdateCollectReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this, "收藏的宝贝");
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            initView();
            initData(I.ACTION_DOWNLOAD);
            setListener();
            setReceiverListener();
        }
    }

    private void setReceiverListener() {
        mReceiver = new UpdateCollectReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_COLLECT);
        registerReceiver(mReceiver, filter);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        initData(I.ACTION_DOWNLOAD);
//    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        mList = new ArrayList<>();
        mAdapter = new CollectAdapter(this, mList);
        mRv.setAdapter(mAdapter);
        mGm = new GridLayoutManager(this, I.COLUM_NUM);
        mRv.setLayoutManager(mGm);
        mRv.setHasFixedSize(true);
        mRv.addItemDecoration(new SpaceItemDecoration(30));

        mGm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mGm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore() && lastPosition == mAdapter.getItemCount() - 1) {
                    mPageId++;
                    initData(I.ACTION_PULL_UP);
                }
            }
        });
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mtvRefresh.setVisibility(View.VISIBLE);
                mSrl.setRefreshing(true);
                mPageId = 1;
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData(final int action) {
        mModelUser = new ModelUser();
        mModelUser.getCollects(this, user.getMuserName(), mPageId, I.PAGE_SIZE_DEFAULT, new onCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                mAdapter.setMore(result != null && result.length > 0);
                if (!mAdapter.isMore()) {
                    if (action == I.ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据");
                    }
                    return;
                }
                mAdapter.setFooter("加载更多数据");
                switch (action) {
                    case I.ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_DOWN:
                        mSrl.setRefreshing(false);
                        mtvRefresh.setVisibility(View.GONE);
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                mSrl.setRefreshing(false);
                mtvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                Toast.makeText(CollectsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class UpdateCollectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int goodsId = intent.getIntExtra(I.Collect.GOODS_ID, 0);
            mAdapter.removeItem(goodsId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}
