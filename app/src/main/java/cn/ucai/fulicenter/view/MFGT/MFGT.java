package cn.ucai.fulicenter.view.MFGT;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/1/10.
 */

public class MFGT {
    public static void startActivity(Activity activity,Class<?> clz) {
        activity.startActivity(new Intent(activity,clz));
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
