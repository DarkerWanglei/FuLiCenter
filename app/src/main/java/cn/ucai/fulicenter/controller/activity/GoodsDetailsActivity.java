package cn.ucai.fulicenter.controller.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailsActivity extends AppCompatActivity {

    int mGoodId;
    IModelGoods mModel;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCart)
    ImageView ivCart;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_currencyPrice)
    TextView tvGoodCurrencyPrice;
    @BindView(R.id.slav)
    SlideAutoLoopView slav;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    @BindView(R.id.tv_good_shopPrice)
    TextView tvGoodShopPrice;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;

    boolean isCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetails);
        ButterKnife.bind(this);
        mGoodId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (mGoodId == 0) {
            MFGT.finishActivity(this);
        } else {
            initData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initCollectStatus();

    }

    private void setCollectStatus() {
        if (isCollect) {
            ivCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    private void initCollectStatus() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            mModel.isCollect(this, mGoodId, user.getMuserName(), new onCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollect = true;
                    } else {
                        isCollect = false;
                    }
//                    setCollectStatus();
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    private void initData() {
        mModel = new ModelGoods();
        mModel.downData(this, mGoodId, new onCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodsDetail(result);
                } else {
                    MFGT.finishActivity(GoodsDetailsActivity.this);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GoodsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showGoodsDetail(GoodsDetailsBean good) {
        tvGoodName.setText(good.getGoodsName());
        tvGoodNameEnglish.setText(good.getGoodsEnglishName());
        tvGoodCurrencyPrice.setText(good.getCurrencyPrice());
        tvGoodShopPrice.setText(good.getShopPrice());

        wvGoodBrief.loadDataWithBaseURL(null, good.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
        slav.startPlayLoop(indicator, getAlbumUrl(good), getAlbumCount(good));
    }

    private int getAlbumCount(GoodsDetailsBean good) {
        if (good != null && good.getProperties() != null && good.getProperties().length > 0) {
            return good.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumUrl(GoodsDetailsBean good) {
        if (good != null && good.getProperties() != null && good.getProperties().length > 0) {
            AlbumsBean[] albums = good.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[i].getImgUrl();
                }
                return urls;
            }
        }
        return new String[0];
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.ivCollect)
    public void setCollectListener() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {

        } else {
            MFGT.gotoLogin(this);
        }
    }
}
