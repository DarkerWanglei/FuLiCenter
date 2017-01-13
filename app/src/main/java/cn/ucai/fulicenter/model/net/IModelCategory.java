package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/1/13.
 */

public interface IModelCategory {
   void downData(Context context, onCompleteListener<CategoryGroupBean[]> listener);
    void downData(Context context, int parentId, onCompleteListener<CategoryChildBean[]> listener);
}
