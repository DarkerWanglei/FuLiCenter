package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
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
    int mPageId=1;
    NewGoodsAdapter mAdapter;
    GridLayoutManager mGm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this,"收藏的宝贝");
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            initView();
            initData();
        }
    }

    private void initView() {
//        mList = new ArrayList<>();
//        mAdapter = new NewGoodsAdapter(this, new ArrayList<NewGoodsBean>());
//        mRv.setAdapter(mAdapter);
//        mGm = new GridLayoutManager(this, I.COLUM_NUM);
//        mRv.setLayoutManager(mGm);
//        mRv.setHasFixedSize(true);
//        mRv.addItemDecoration(new SpaceItemDecoration(30));
    }

    private void initData() {
        mModelUser = new ModelUser();
        mModelUser.getCollects(this, user.getMuserName(), mPageId, I.PAGE_SIZE_DEFAULT, new onCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                if (result == null) {

                } else {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    Log.i("main","-----"+ list.toString());
                    Log.i("main","-----"+ list.size());
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
