package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    OnSetAvatarListener mOnSetAvatarListener;
    IModelUser mModelUser;

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
//        ImageLoader.downloadImg(this, ivAvatar, user.getAvatarPath());
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, ivAvatar);
        tvUsername.setText(user.getMuserName());
        tvNick.setText(user.getMuserNick());
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        FuLiCenterApplication.setUser(null);
        SharePrefrenceUtils.getInstance(this).removeUser();
        MFGT.gotoLogin(this);
        MFGT.finishActivity(this);
    }

    @OnClick(R.id.ivReturn)
    public void onClick() {
        finish();
    }

    @OnClick(R.id.rl_username)
    public void onClickUserName() {
        CommonUtils.showLongToast(R.string.username_connot_be_modify);
    }

    @OnClick(R.id.rl_nick)
    public void updateNick() {
        MFGT.gotoUpDateNick(this);
    }

    @OnClick(R.id.rl_avatar)
    public void onClickAvatar() {
        mOnSetAvatarListener = new OnSetAvatarListener(this,
                R.id.rl_avatar,
                FuLiCenterApplication.getUser().getMuserName(),
                I.AVATAR_TYPE_USER_PATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("main", "resultCode=" + resultCode);
        Log.i("main", "requestCode=" + requestCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == I.REQUEST_CODE_NICK) {
            tvNick.setText(FuLiCenterApplication.getUser().getMuserNick());
        } else if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        } else {
            mOnSetAvatarListener.setAvatar(requestCode, data, ivAvatar);
        }
    }

    private void updateAvatar() {
        User user = FuLiCenterApplication.getUser();
        mModelUser = new ModelUser();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_avatar));
        dialog.show();
        File file = new File(String.valueOf(OnSetAvatarListener.getAvatarFile
                (this, I.AVATAR_TYPE_USER_PATH + "/" + user.getMuserName() + user.getMavatarSuffix())));
        Log.i("main", "file=" + file.getAbsolutePath());
        mModelUser.updateAvatar(this, user.getMuserName(),
                file, new onCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        int msg = R.string.update_user_avatar_fail;
                        if (s != null) {
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result != null) {
                                if (result.isRetMsg()) {
                                    msg = R.string.update_user_avatar_success;
                                }
                            }
                        }
                        CommonUtils.showLongToast(msg);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("main", "======" + error);
                        CommonUtils.showLongToast(error);
                        dialog.dismiss();
                    }
                });
    }
}
