package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.controller.activity.CategoryActivity;
import cn.ucai.fulicenter.controller.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.controller.activity.LoginActivity;
import cn.ucai.fulicenter.controller.activity.RegisterActivity;
import cn.ucai.fulicenter.controller.activity.SettingsActivity;
import cn.ucai.fulicenter.controller.activity.UpdateNickActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;

/**
 * Created by Administrator on 2017/1/10.
 */

/**
 * 开机
 */
public class MFGT {
    public static void startActivity(Activity activity, Class<?> clz) {
        activity.startActivity(new Intent(activity, clz));
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 结束activity
     * @param activity
     */
    public static void finishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 启动另一个activity
     * @param activity
     * @param intent
     */
    public static void startActivity(Activity activity,Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 精选页面进入
     * @param context
     * @param boutiqueBean
     */
    public static void gotoBoutiqueChild(Context context, BoutiqueBean boutiqueBean) {
        Intent intent = new Intent(context, BoutiqueChildActivity.class);
        intent.putExtra(I.NewAndBoutiqueGoods.CAT_ID, boutiqueBean.getId());
        intent.putExtra(I.Boutique.NAME, boutiqueBean.getTitle());
        startActivity((Activity) context,intent);
    }

    /**
     * 进入商品详情
     * @param context
     * @param goodsId
     */
    public static void gotoBoutiqueChild(Context context,int goodsId) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId);
        startActivity((Activity) context,intent);
    }

    public static void gotoCategoryChild(Context context, int catId,
                                         String groupName, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID, catId);
        intent.putExtra(I.CategoryGroup.NAME, groupName);
        intent.putExtra(I.CategoryChild.DATA, list);
        startActivity((Activity) context,intent);
    }

    public static void gotoLogin(Activity context) {
        context.startActivityForResult(new Intent(context,LoginActivity.class),I.REQUEST_CODE_LOGIN);
//        startActivity(context, LoginActivity.class);
    }

    public static void gotoRegister(LoginActivity loginActivity) {
        startActivity(loginActivity,RegisterActivity.class);
    }

    public static void gotoSettings(Activity activity) {
        startActivity(activity, SettingsActivity.class);
    }

    public static void gotoUpDateNick(Activity activity) {
        activity.startActivityForResult(new Intent(activity, UpdateNickActivity.class), I.REQUEST_CODE_NICK);
    }
}
