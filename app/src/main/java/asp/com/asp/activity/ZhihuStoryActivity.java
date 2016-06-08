package asp.com.asp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.InvocationTargetException;

import asp.com.asp.R;
import asp.com.asp.domain.ZhihuStory;
import asp.com.asp.presenter.ZhihuStoryPresenterImpl;
import asp.com.asp.utils.WebUtil;
import asp.com.asp.view.SnackbarUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ZhihuStoryActivity extends AppCompatActivity implements ZhihuStoryPresenterImpl.ZhihuStoryImpl {


    private  WebView wvZhihu;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private SimpleDraweeView news_imgview;

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

        initView();
        initData();
        initEvent();
    }


    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.zhihu_story_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        news_imgview = (SimpleDraweeView) findViewById(R.id.zhihu_story_SimpleDraweeView);

        wvZhihu= (WebView) findViewById(R.id.wv_zhihu);

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

    }

    private void initData() {

        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        collapsingToolbar.setTitle(title+"");

        mZhihuStoryPresenterImpl = new ZhihuStoryPresenterImpl();
        mZhihuStoryPresenterImpl.setZhihuStoryImpl(this);

        mZhihuStoryPresenterImpl.getZhihuStory(id);
    }


    private void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void RxZhihuStoryNext(ZhihuStory zhihuStory) {
       // ImageLoader.loadImage(ZhihuStoryActivity.this,zhihuStory.getImage(),ivZhihuStory);

        if(zhihuStory.getTitle()!=null){
            Log.i("getTitle","....getTitle......"+zhihuStory.getTitle());
        }
        news_imgview.setImageURI(Uri.parse(zhihuStory.getImage()));
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
        SnackbarUtil.GreenSnackbar(mContext,wvZhihu,"网络异常，加载失败！！！");
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
