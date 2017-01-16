package cn.ucai.fulicenter.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/1/10.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    private static FuLiCenterApplication user;

    public static FuLiCenterApplication getUser() {
        return user;
    }

    public static void setUser(FuLiCenterApplication user) {
        FuLiCenterApplication.user = user;
    }

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        instance = this;
    }
}
