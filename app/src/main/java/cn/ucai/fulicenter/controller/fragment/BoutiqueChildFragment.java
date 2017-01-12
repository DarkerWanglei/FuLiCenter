package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.GoodsAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueChildFragment extends Fragment {
    static final int ACTION_DOWNLOAD = 0;
    static final int ACTION_PULL_DOWN = 1;
    static final int ACTION_PULL_UP = 2;

    @BindView(R.id.rv)
    RecyclerView mrv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_refresh)
    TextView mtvRefresh;

    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int mPageId;
    IModelNewGoods mModel;
    GridLayoutManager gm;

    public BoutiqueChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_boutique_child, container, false);
        ButterKnife.bind(this, layout);
        mModel = new ModelNewGoods();
        initData();
        initView();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mrv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore() && lastPosition == mAdapter.getItemCount() - 1) {
                    mPageId++;
                    downloadNewGoods(ACTION_PULL_UP, mPageId);
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
                downloadNewGoods(ACTION_PULL_DOWN, mPageId);
            }
        });
    }

    private void initData() {
        mPageId = 1;
        downloadNewGoods(ACTION_DOWNLOAD, mPageId);
    }

    private void downloadNewGoods(final int action, int pageId) {
        int catId= getActivity().getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID, I.CAT_ID);
        mModel.downData(getContext(), catId, pageId, new onCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                mAdapter.setMore(result != null && result.length > 0);
                if (!mAdapter.isMore()) {
                    if (action == ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据");
                    }
                    return;
                }
                mAdapter.setFooter("加载更多数据");
                switch (action) {
                    case ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        break;
                    case ACTION_PULL_DOWN:
                        mSrl.setRefreshing(false);
                        mtvRefresh.setVisibility(View.GONE);
                        mAdapter.initData(list);
                        break;
                    case ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(), mList);
        mrv.setAdapter(mAdapter);
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        mrv.setLayoutManager(gm);
        mrv.setHasFixedSize(true);
        mrv.addItemDecoration(new SpaceItemDecoration(30));

        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
    }

}
