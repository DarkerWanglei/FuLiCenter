package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/16.
 */

public class CatFilterButton extends Button {

    boolean isExpand;
    PopupWindow mPopupWindow;
    Context mContext;
    CatFilterAdapter mAdapter;
    GridView mGridView;
    String mGroupName;


    public CatFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void initCatFilterButton(String groupName, ArrayList<CategoryChildBean> list) {
        this.mGroupName = groupName;
        this.setText(mGroupName);
        setCatFilterButtonListener();
        mAdapter = new CatFilterAdapter(mContext, list);
        initGridView();
    }

    private void initGridView() {
        mGridView = new GridView(mContext);
        mGridView.setVerticalSpacing(10);
        mGridView.setHorizontalSpacing(10);
        mGridView.setAdapter(mAdapter);
    }

    private void setCatFilterButtonListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpand) {
                    initPopup();
                } else {
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                setArrow();
            }
        });
    }

    private void initPopup() {
        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        mPopupWindow.setContentView(mGridView);
        mPopupWindow.showAsDropDown(this);
    }

    private void setArrow() {
        Drawable right;
        if (isExpand) {
            right = getResources().getDrawable(R.mipmap.arrow2_up);
        } else {
            right = getResources().getDrawable(R.mipmap.arrow2_down);
        }
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
        isExpand = !isExpand;
    }

    class CatFilterAdapter extends BaseAdapter {
        Context context;
        ArrayList<CategoryChildBean> list;

        public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CategoryChildBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CatFilterViewHolder cv = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_cat_filter, null);
                cv = new CatFilterViewHolder(convertView);
                convertView.setTag(cv);
            } else {
                cv = (CatFilterViewHolder) convertView.getTag();
            }
            cv.bind(position);
            return convertView;
        }

        class CatFilterViewHolder {
            @BindView(R.id.ivCategoryChildThumb)
            ImageView ivCategoryChildThumb;
            @BindView(R.id.tvCategoryChildName)
            TextView tvCategoryChildName;
            @BindView(R.id.layout_category_child)
            RelativeLayout layoutCategoryChild;

            CatFilterViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void bind(final int position) {
                ImageLoader.downloadImg(mContext, ivCategoryChildThumb, list.get(position).getImageUrl());
                tvCategoryChildName.setText(list.get(position).getName());
                layoutCategoryChild.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MFGT.gotoCategoryChild(mContext
                                , list.get(position).getId()
                                , mGroupName
                                , list);
                        MFGT.finishActivity((Activity) mContext);
                    }
                });
            }
        }
    }

}
