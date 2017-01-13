package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/1/13.
 */

public interface IModelCategoryChild {
    void downData(Context context, int catId, int pageId, onCompleteListener<NewGoodsBean[]> listener);
}
