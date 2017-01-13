package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.CategoryChildFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class CategoryActivity extends AppCompatActivity {


    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    CategoryChildFragment mCategoryChildFragment;
    boolean priceAsc = false;
    boolean addTimeAsc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mCategoryChildFragment = new CategoryChildFragment();
        initView();
    }

    private void initView() {
        tvName.setText(getIntent().getStringExtra(I.CategoryChild.NAME));
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
    }
}
