package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.fragment.BoutiqueChildFragment;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;

public class BoutiqueChildActivity extends AppCompatActivity {


    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        BoutiqueBean boutique = (BoutiqueBean) getIntent().getSerializableExtra("boutique");
        tvName.setText(boutique.getName());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_boutique_child, new BoutiqueChildFragment())
                .commit();
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
//      onBackPressed();
//        BoutiqueChildActivity.this.finish();
        finish();
    }
}
