package asp.com.asp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import asp.com.appbase.adapter.BaseRecycleViewAdapter;
import asp.com.asp.R;
import asp.com.asp.activity.ZhihuStoryActivity;
import asp.com.asp.adapter.ZhihuNewsRecycleAdapter;
import asp.com.asp.domain.ZhihuDaily;
import asp.com.asp.domain.ZhihuDailyItem;
import asp.com.asp.presenter.ZhihuPresenterImpl;
import asp.com.asp.utils.ConfigConstantUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/7.
 */
public class ZhiHuNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ImageView back_btn;
    private TextView center_titleTv;
    private TextView right_titleTv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
   // private BGABanner mFlipBanner;
   // private ViewPager img_Viewpager;
    private SimpleDraweeView topView;
    private TextView top_title;
    private ScrollView scrollView;
    private View progress_loading;

    private RecyclerView mNewRecyclerView;

    private ZhihuNewsRecycleAdapter mZhihuNewsRecycleAdapter;
    private List<ZhihuDailyItem> mZhihuDailyItemList = new ArrayList<ZhihuDailyItem>();

    private ZhihuPresenterImpl mZhihuPresenterImpl;
    private ZhihuDailyItem topZhihuDailyItem;

    private String date;
    private  int data_flag = 1;
    private Calendar dateToGetUrl;

    private View mRootview;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_news, container, false);

        mContext = getActivity();

        initView();
        initData();
        initEvent();


        return mRootview;
    }

    private void initView() {

        back_btn = (ImageView) mRootview.findViewById(R.id.common_top_bar_back_btn);
        center_titleTv = (TextView) mRootview.findViewById(R.id.common_top_bar_center_titleTv);
        right_titleTv = (TextView) mRootview.findViewById(R.id.common_top_bar_right_titleTv);
        top_title = (TextView) mRootview.findViewById(R.id.fragment_news_top_title);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootview.findViewById(R.id.fragment_news_refresh_layout);
        scrollView = (ScrollView) mRootview.findViewById(R.id.fragment_news_scrollView);
        topView = (SimpleDraweeView) mRootview.findViewById(R.id.fragment_news_top_view);
        //img_Viewpager = (ViewPager) mRootview.findViewById(R.id.fragment_news_vp);
        //mFlipBanner = (BGABanner) mRootview.findViewById(R.id.fragment_news_BGABanner);
        mNewRecyclerView = (RecyclerView) mRootview.findViewById(R.id.fragment_news_RecyclerView);

        back_btn.setVisibility(View.GONE);
        center_titleTv.setText("知乎日报");
        right_titleTv.setVisibility(View.GONE);

        mNewRecyclerView.setHasFixedSize(false);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mNewRecyclerView.setLayoutManager(llm);

        mZhihuNewsRecycleAdapter = new ZhihuNewsRecycleAdapter(mContext,mZhihuDailyItemList);
        mNewRecyclerView.setAdapter(mZhihuNewsRecycleAdapter);
    }

    private void initData() {

        dateToGetUrl = Calendar.getInstance();

        mZhihuPresenterImpl = new ZhihuPresenterImpl();

        getZhiHuNewsData();


    }

    private void getZhiHuNewsData(){
        dateToGetUrl.add(Calendar.DAY_OF_YEAR, data_flag-- );
        date = ConfigConstantUtil.Dates.simpleDateFormat.format(dateToGetUrl.getTime());
        mZhihuPresenterImpl.getTheDaily(date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                        if(progress_loading !=null){
                            progress_loading.setVisibility(View.GONE);
                        }
                        //mSwipeRefreshLayout.setEnabled(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {

                        if(data_flag == 0) {
                            topZhihuDailyItem = zhihuDaily.getStories().get(0);
                            topView.setImageURI(Uri.parse(topZhihuDailyItem.getImages()[0]));
                            top_title.setText(topZhihuDailyItem.getTitle()+"");
                            zhihuDaily.getStories().remove(0);

                        }

                        mZhihuDailyItemList.addAll(zhihuDaily.getStories());
                        mZhihuNewsRecycleAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void initEvent() {

        mSwipeRefreshLayout.setOnRefreshListener(this);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //scrollView.getScrollY() 可以得到 scrollView 滑动了 多少距离（从顶部开始计算）
                    //view.getHeight() 得到的是 view 的 px 值（更屏幕密度有关），不是 dp值
                    if(scrollView.getScrollY()<=0){
                        //开启手势
                        //mSwipeRefreshLayout.setEnabled(true);
                        //开启旋转效果
                        mSwipeRefreshLayout.setRefreshing(true);
                        //Toast.makeText(mContext, "到达顶部了", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


        mZhihuNewsRecycleAdapter.setOnLoadingListener(new BaseRecycleViewAdapter.OnLoadingListener() {
            @Override
            public void loading(View loadingview) {
                if(progress_loading ==null){
                    progress_loading = loadingview;
                }
                progress_loading.setVisibility(View.VISIBLE);
                //mSwipeRefreshLayout.setEnabled(false);
                mSwipeRefreshLayout.setRefreshing(false);

                if(data_flag == 0){
                    data_flag--;
                }
                getZhiHuNewsData();
            }
        });

        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topZhihuDailyItem!=null){
                    Intent intent = new Intent(mContext, ZhihuStoryActivity.class);
                    intent.putExtra("type", ZhihuStoryActivity.TYPE_ZHIHU);
                    intent.putExtra("id", topZhihuDailyItem.getId());
                    intent.putExtra("title", topZhihuDailyItem.getTitle());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        Log.i("onRefresh","............mSwipeRefreshLayout.......onRefresh...............");
        data_flag = 1;
        getZhiHuNewsData();
    }
}
