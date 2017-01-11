package cn.ucai.fulicenter.controller.fragment;


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
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.IModelBoutique;
import cn.ucai.fulicenter.model.net.ModelBoutique;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.model.utils.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    LinearLayoutManager linearLayoutManager;
    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;
    IModelBoutique mModel;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, layout);
        mModel = new ModelBoutique();
        initView();
        initData();
        setListener();
        return layout;
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
                downloadBoutique(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        downloadBoutique(I.ACTION_DOWNLOAD);
    }

    private void downloadBoutique(final int action) {
        mModel.downData(getContext(), new onCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                Log.i("main", Arrays.toString(result));
                ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                mAdapter.setFooter("没有更多数据");
                switch (action) {
                    case I.ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_DOWN:
                        srl.setRefreshing(false);
                        tvRefresh.setVisibility(View.GONE);
                        mAdapter.initData(list);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
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
        mAdapter = new BoutiqueAdapter(getContext(), mList);
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SpaceItemDecoration(15));
    }

}
