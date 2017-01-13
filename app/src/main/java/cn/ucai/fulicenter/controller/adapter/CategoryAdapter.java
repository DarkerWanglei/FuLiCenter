package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/13.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mGroupList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.mContext = mContext;
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        this.mGroupList = mGroupList;
        this.mChildList = mChildList;
//        mGroupList.addAll(mGroupList);
//        mChildList.addAll(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null ? mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder gvh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_category_group, null);
            gvh = new GroupViewHolder(convertView);
            convertView.setTag(gvh);
        } else {
            gvh = (GroupViewHolder) convertView.getTag();
        }

        ImageLoader.downloadImg(mContext, gvh.ivGroupPic, mGroupList.get(groupPosition).getImageUrl());
        gvh.tvGroupName.setText(mGroupList.get(groupPosition).getName());
        gvh.expand.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder cvh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_category_child, null);
            cvh = new ChildViewHolder(convertView);
            convertView.setTag(cvh);
        } else {
            cvh = (ChildViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(mContext, cvh.ivChildPic, getChild(groupPosition, childPosition).getImageUrl());
        cvh.tvChildName.setText(getChild(groupPosition, childPosition).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoCategoryChild(mContext, getChild(groupPosition, childPosition).getId());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.mGroupList.clear();
        this.mChildList.clear();
        mGroupList.addAll(groupList);
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.ivGroupPic)
        ImageView ivGroupPic;
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.expand)
        ImageView expand;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.ivChildPic)
        ImageView ivChildPic;
        @BindView(R.id.tvChildName)
        TextView tvChildName;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
