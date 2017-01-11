package cn.ucai.fulicenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.controller.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
    int mIndex, mCurrentIndex;
    FragmentTransaction ft;
    RadioButton[] mrbS;
    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    Fragment[] fragments;

    @BindView(R.id.layout_new_good)
    RadioButton mLayoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton mLayoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton mLayoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton mLayoutCart;
    @BindView(R.id.layout_personal_center)
    RadioButton mLayoutPersonalCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mrbS = new RadioButton[5];
        fragments = new Fragment[5];
        initView();
        newGoodsFragment = new NewGoodsFragment();
        boutiqueFragment = new BoutiqueFragment();
        fragments[0] = newGoodsFragment;
        fragments[1] = boutiqueFragment;
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, newGoodsFragment)
                .add(R.id.fragment_container, boutiqueFragment)
//                .show(newGoodsFragment)
                .hide(boutiqueFragment).commit();
    }

    private void initView() {
        mrbS[0] = mLayoutNewGood;
        mrbS[1] = mLayoutBoutique;
        mrbS[2] = mLayoutCategory;
        mrbS[3] = mLayoutCart;
        mrbS[4] = mLayoutPersonalCenter;

    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                mIndex = 0;
                break;
            case R.id.layout_boutique:
                mIndex = 1;
                break;
            case R.id.layout_category:
                mIndex = 2;
                break;
            case R.id.layout_cart:
                mIndex = 3;
                break;
            case R.id.layout_personal_center:
                mIndex = 4;
                break;
        }

        if (mIndex != mCurrentIndex) {
            setFragment();
            setRadioStatus();
        }
    }

    private void setFragment() {
        getSupportFragmentManager().beginTransaction().show(fragments[mIndex])
                .hide(fragments[mCurrentIndex]).commit();

    }

    private void setRadioStatus() {
        for (int i = 0; i < mrbS.length; i++) {
            if (mIndex != i) {
                mrbS[i].setChecked(false);
            } else {
                mrbS[i].setChecked(true);
            }
        }
        mCurrentIndex = mIndex;
    }
}
