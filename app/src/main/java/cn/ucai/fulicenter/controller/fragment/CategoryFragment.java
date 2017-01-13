package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.adapter.CategoryAdapter;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.IModelCategory;
import cn.ucai.fulicenter.model.net.ModelCategory;
import cn.ucai.fulicenter.model.net.onCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    IModelCategory mModel;
    int mGroupCount = 0;
    CategoryAdapter mAdapter;

    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    @BindView(R.id.tv_noMore)
    TextView tvNoMore;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        initData();
        initView();
        setView(false);
        return layout;
    }

    private void initView() {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext(), mGroupList, mChildList);
        elvCategory.setAdapter(mAdapter);
        elvCategory.setGroupIndicator(null);
    }

    private void initData() {
        mModel = new ModelCategory();
        mModel.downData(getContext(), new onCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null) {
                    setView(true);
                    ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    mGroupList.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        downloadChildData(list.get(i).getId(),i);
                    }
                } else {
                    setView(false);
                }
            }

            @Override
            public void onError(String error) {
                setView(false);
                Log.i("main", error);
            }
        });
    }

    private void downloadChildData(int id, final int index) {
        mModel.downData(getContext(), id, new onCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                mGroupCount++;
                if (result != null) {
                    ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                    mChildList.add(list);
                    mChildList.set(index, list);
                }
                if (mGroupCount == mGroupList.size()) {
                    mAdapter.initData(mGroupList, mChildList);
                }
            }

            @Override
            public void onError(String error) {
                Log.i("main", error);
            }
        });
    }

    private void setView(boolean hasData) {
        tvNoMore.setVisibility(hasData ? View.GONE : View.VISIBLE);
        elvCategory.setVisibility(hasData ? View.VISIBLE : View.GONE);
    }

}
