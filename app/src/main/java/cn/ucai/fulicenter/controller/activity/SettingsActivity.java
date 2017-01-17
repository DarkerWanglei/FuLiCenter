package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            MFGT.gotoLogin(this);
        }
    }

    private void loadUserInfo(User user) {
        ImageLoader.downloadImg(this, ivAvatar, user.getAvatarPath());
        tvUsername.setText(user.getMuserName());
        tvNick.setText(user.getMuserNick());
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        FuLiCenterApplication.setUser(null);
        SharePrefrenceUtils.getInstance(this).removeUser();
        MFGT.gotoLogin(this);
        finish();
    }

    @OnClick(R.id.ivReturn)
    public void onClick() {
        finish();
    }
}
