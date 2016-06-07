package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.InvocationTargetException;

import asp.com.asp.R;
import asp.com.asp.domain.ZhihuStory;
import asp.com.asp.presenter.ZhihuStoryPresenterImpl;
import asp.com.asp.utils.WebUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ZhihuStoryActivity extends Activity implements ZhihuStoryPresenterImpl.ZhihuStoryImpl {


    private  WebView wvZhihu;


 /*   Toolbar toolbar;
    ImageView ivZhihuStory;

    CollapsingToolbarLayout ctl;

    NestedScrollView nest;*/

    //FloatingActionButton fabButton;

    private ZhihuStoryPresenterImpl mZhihuStoryPresenterImpl;

    private int type;
    private String id;
    private String title;
    private String url;

    public static final int TYPE_ZHIHU = 1;
    public static final int TYPE_GUOKR = 2;

    private Context mContext;
    private  int data_flag = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_story);
        mContext = getApplicationContext();


        initData();
        initView();
        initEvent();
    }


    private void initView() {


        wvZhihu= (WebView) findViewById(R.id.wv_zhihu);

 /*       toolbar = (Toolbar) findViewById(R.id.toolbar);

        ivZhihuStory= (ImageView) findViewById(R.id.iv_zhihu_story);
        ctl= (CollapsingToolbarLayout) findViewById(R.id.ctl);
        nest= (NestedScrollView) findViewById(R.id.nest);

        toolbar.setTitle(title);*/


        //setSupportActionBar(toolbar);
        WebSettings settings = wvZhihu.getSettings();
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
        wvZhihu.setWebChromeClient(new WebChromeClient());

        //  1000 0000
        //  1000 0000
        //1 0000 0000  = 0
    }

    private void initData() {

        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        mZhihuStoryPresenterImpl = new ZhihuStoryPresenterImpl();
        mZhihuStoryPresenterImpl.setZhihuStoryImpl(this);

        mZhihuStoryPresenterImpl.getZhihuStory(id);
    }


    private void initEvent() {

    }

    @Override
    public void RxZhihuStoryNext(ZhihuStory zhihuStory) {
       // ImageLoader.loadImage(ZhihuStoryActivity.this,zhihuStory.getImage(),ivZhihuStory);
        url = zhihuStory.getShareUrl();
        if (TextUtils.isEmpty(zhihuStory.getBody())) {
            wvZhihu.loadUrl(zhihuStory.getShareUrl());
        } else {
            String data = WebUtil.buildHtmlWithCss(zhihuStory.getBody(), zhihuStory.getCss(),false);
            wvZhihu.loadDataWithBaseURL(WebUtil.BASE_URL, data, WebUtil.MIME_TYPE, WebUtil.ENCODING, WebUtil.FAIL_URL);
        }
    }

    @Override
    public void RxZhihuStoryError() {

    }

    @Override
    public void RxZhihuStoryCompleted() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            wvZhihu.getClass().getMethod("onPause").invoke(wvZhihu, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        //webview内存泄露
        if (wvZhihu != null) {
            ((ViewGroup) wvZhihu.getParent()).removeView(wvZhihu);
            wvZhihu.destroy();
            wvZhihu = null;
        }

        super.onDestroy();
    }


}
