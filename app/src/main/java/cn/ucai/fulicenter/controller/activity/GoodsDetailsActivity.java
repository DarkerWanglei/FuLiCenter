package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailsActivity extends AppCompatActivity {

    int mGoodId;
    IModelGoods mModel;
    IModelUser mModelUser;

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
        initCollectStatus();
    }

    private void setCollectStatus() {
        if (isCollect) {
            ivCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivCollect.setImageResource(R.mipmap.bg_collect_in);
        }
        ivCollect.setEnabled(true);
    }

    private void initCollectStatus() {
//        ivCollect.setEnabled(false);
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
                    setCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                    setCollectStatus();
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

    @OnClick(R.id.ivShare)
    public void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    @OnClick(R.id.ivCart)
    public void Cart() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            mModelUser = new ModelUser();
            mModelUser.updateCart(this, I.ACTION_CART_ADD, user.getMuserName(), mGoodId, 1, 1, new onCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null) {
                        CommonUtils.showLongToast(R.string.add_goods_success);
                    }
                }

                @Override
                public void onError(String error) {
                    ivCollect.setEnabled(true);
                }
            });
        } else {
            MFGT.gotoLogin(this);
        }
    }

    @OnClick(R.id.ivCollect)
    public void setCollectListener() {
        ivCollect.setEnabled(false);
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            setCollect(user);
        } else {
//            ivCollect.setEnabled(true);
            MFGT.gotoLogin(this);
        }
    }

    private void setCollect(User user) {
        mModel.setCollect(this, mGoodId, user.getMuserName(), isCollect ? I.ACTION_DELETE_COLLECT : I.ACTION_ADD_COLLECT, new onCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    isCollect = !isCollect;
                    setCollectStatus();
                    CommonUtils.showLongToast(result.getMsg());
                    sendBroadcast(new Intent(I.BROADCAST_UPDATE_COLLECT).putExtra(I.Collect.GOODS_ID, mGoodId));
                }
            }

            @Override
            public void onError(String error) {
                Log.i("main", error);
                CommonUtils.showLongToast(error);
                ivCollect.setEnabled(true);
            }
        });
    }
}
