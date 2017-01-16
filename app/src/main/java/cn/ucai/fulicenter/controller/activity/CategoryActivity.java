package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.CategoryChildFragment;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.view.CatFilterButton;

public class CategoryActivity extends AppCompatActivity {


    boolean priceAsc = false;
    boolean addTimeAsc = false;

    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    CategoryChildFragment mCategoryChildFragment;
    @BindView(R.id.ivPrice)
    ImageView ivPrice;
    @BindView(R.id.ivAddTime)
    ImageView ivAddTime;
    @BindView(R.id.tvName)
    CatFilterButton tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mCategoryChildFragment = new CategoryChildFragment();
        initView();
    }

    private void initView() {
        String groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        ArrayList<CategoryChildBean> list = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.DATA);
        tvName.initCatFilterButton(groupName, list);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_category_child, mCategoryChildFragment)
                .commit();
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        onBackPressed();
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void onClick(View view) {
        int sortBy = I.SORT_BY_ADDTIME_ASC;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                }
                priceAsc = !priceAsc;
                break;
            case R.id.btn_sort_addtime:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                }
                addTimeAsc = !addTimeAsc;
                break;
        }
        mCategoryChildFragment.sortGoods(sortBy);
        ivPrice.setImageResource(priceAsc ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
        ivAddTime.setImageResource(addTimeAsc ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
    }
}
