package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import asp.com.asp.R;

/**
 * Created by Administrator on 2016/6/13.
 */
@EActivity(R.layout.activity_official)
public class OfficialActivity extends Activity {

    @ViewById(R.id.common_top_bar_back_btn)
    ImageView back_btn;
    @ViewById(R.id.common_top_bar_right_titleTv)
    TextView right_titleTv;
    @ViewById(R.id.common_top_bar_center_titleTv)
    TextView center_titleTv;

    @ViewById(R.id.official_WebView)
    WebView official_WebView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }

    @AfterViews
    void updateQiangDate() {

        initView();


    }

    private void initView() {

        center_titleTv.setText("官网");
        right_titleTv.setVisibility(View.GONE);

        WebSettings settings = official_WebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //settings.setUseWideViewPort(true);造成文字太小
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        official_WebView.setWebChromeClient(new WebChromeClient());

        official_WebView.loadUrl("http://secondhands.bmob.cn/");
    }

    @Click(R.id.common_top_bar_back_btn)
    void back_btnClick(){

        finish();
    }
}
