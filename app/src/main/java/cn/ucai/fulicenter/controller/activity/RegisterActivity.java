package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.ivReturn)
    ImageView ivReturn;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etNick)
    EditText etNick;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    IModelUser mModelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivReturn, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivReturn:
                MFGT.finishActivity(this);
                break;
            case R.id.btnRegister:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String userName = etUserName.getText().toString().trim();
        String userNick = etNick.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();
        String userFirmPassword = etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            etUserName.setError(getResources().getString(R.string.user_name_connot_be_empty));
            etUserName.requestFocus();
        } else if (!userName.matches("[a-zA-z]\\w{5,15}")) {
            etUserName.setError(getResources().getString(R.string.illegal_user_name));
            etUserName.requestFocus();
        } else if (TextUtils.isEmpty(userNick)) {
            etNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            etNick.requestFocus();
        } else if (TextUtils.isEmpty(userPassword)) {
            etPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            etPassword.requestFocus();
        } else if (TextUtils.isEmpty(userFirmPassword)) {
            etConfirmPassword.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            etConfirmPassword.requestFocus();
        } else if (!userPassword.equals(userFirmPassword)) {
            etConfirmPassword.setError(getResources().getString(R.string.two_input_password));
            etConfirmPassword.requestFocus();
        } else {
            register(userName, userNick, userPassword);
        }
    }

    private void register(String userName, String userNick, String userPassword) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.registering));
        mModelUser = new ModelUser();
        mModelUser.register(this, userName, userNick, userPassword, new onCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            CommonUtils.showLongToast(getResources().getString(R.string.register_success));
                            MFGT.finishActivity(RegisterActivity.this);
                        } else {
                            CommonUtils.showLongToast(getResources().getString(R.string.register_fail_exists));
                        }
                    } else {
                        CommonUtils.showLongToast(getResources().getString(R.string.register_fail));
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showLongToast(error);
            }
        });
    }
}
