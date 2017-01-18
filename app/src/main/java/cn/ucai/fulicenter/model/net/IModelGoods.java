package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/1/12.
 */

public interface IModelGoods {
    void downData(Context context, int goodId, onCompleteListener<GoodsDetailsBean> listener);

    void isCollect(Context context, int goodId, String username, onCompleteListener<MessageBean> listener);

    void setCollect(Context context, int goodId, String username, int action, onCompleteListener<MessageBean> listener);

}
