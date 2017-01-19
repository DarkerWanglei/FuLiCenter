package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/1/17.
 */

public class DisplayUtils {
    // id需要重新find
    public static void initBack(final Activity activity) {
        activity.findViewById(R.id.backClickArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

//    id需要重新find
    public static void initBackWithTitle(Activity activity, String title) {
        TextView textView = (TextView) activity.findViewById(R.id.tv_common_title);
        textView.setText(title);
        initBack(activity);
    }
}
