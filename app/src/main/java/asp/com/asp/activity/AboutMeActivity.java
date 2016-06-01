package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import asp.com.asp.R;

/**
 * Created by Administrator on 2016/6/1.
 */
@EActivity(R.layout.activity_about_me)
public class AboutMeActivity  extends Activity {

    @ViewById(R.id.common_top_bar_back_btn)
    ImageView back_btn;
    @ViewById(R.id.common_top_bar_right_titleTv)
    TextView right_titleTv;
    @ViewById(R.id.common_top_bar_center_titleTv)
    TextView center_titleTv;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }

    @AfterViews
    void updateQiangDate() {
        center_titleTv.setText("关于");
        right_titleTv.setVisibility(View.GONE);
    }
    @Click(R.id.common_top_bar_back_btn)
    void back_btnClick(){

        finish();
    }
}
