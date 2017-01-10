package cn.ucai.fulicenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    int mIndex, mCurrentIndex;
    RadioButton mrbNewGoods, mrbBoutique, mrbCategory, mrbCate, mrbPersonal;
    RadioButton[] mrbS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrbS = new RadioButton[5];
        initView();
    }

    private void initView() {
        mrbNewGoods = (RadioButton) findViewById(R.id.layout_new_good);
        mrbBoutique = (RadioButton) findViewById(R.id.layout_boutique);
        mrbCategory = (RadioButton) findViewById(R.id.layout_category);
        mrbCate = (RadioButton) findViewById(R.id.layout_cart);
        mrbPersonal = (RadioButton) findViewById(R.id.layout_personal_center);

        mrbS[0] = mrbNewGoods;
        mrbS[1] = mrbBoutique;
        mrbS[2] = mrbCategory;
        mrbS[3] = mrbCate;
        mrbS[4] = mrbPersonal;
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
            setRadioStatus();
        }
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
