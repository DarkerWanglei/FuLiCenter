package cn.ucai.fulicenter.model.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NavUtils;

/**
 * Created by Administrator on 2017/1/16.
 */

public class SharePrefrenceUtils {
    private static final String SHARE_PREFRENCE_NAME = "cn.ucai.fulicenter_user";
    private static final String SHARE_PREFRENCE_NAME_USERNAME = "cn.ucai.fulicenter_user_username";
    private static SharePrefrenceUtils instance;
    private static SharedPreferences preferences;

    public SharePrefrenceUtils(Context context) {
        preferences = context.getSharedPreferences(SHARE_PREFRENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharePrefrenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefrenceUtils(context);
        }
        return instance;
    }

    public void saveUser(String username) {
        preferences.edit().putString(SHARE_PREFRENCE_NAME_USERNAME, username).commit();
    }

    public String getUser() {
        return preferences.getString(SHARE_PREFRENCE_NAME_USERNAME, null);
    }

    public void removeUser() {
        preferences.edit().remove(SHARE_PREFRENCE_NAME_USERNAME).commit();
    }
}
