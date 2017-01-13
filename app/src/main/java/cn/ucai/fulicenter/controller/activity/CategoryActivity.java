package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.fragment.BoutiqueChildFragment;
import cn.ucai.fulicenter.controller.fragment.CategoryChildFragment;

public class CategoryActivity extends AppCompatActivity {


//    @BindView(R.id.ivBack)
//    ImageView ivBack;
//    @BindView(R.id.tvName)
//    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
//        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_category_child, new CategoryChildFragment())
                .commit();
    }
}
