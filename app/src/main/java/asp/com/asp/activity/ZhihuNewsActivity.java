package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import asp.com.asp.R;
import asp.com.asp.adapter.ZhihuNewsRecycleAdapter;
import asp.com.asp.domain.ZhihuDaily;
import asp.com.asp.domain.ZhihuDailyItem;
import asp.com.asp.presenter.ZhihuPresenterImpl;
import asp.com.asp.utils.ConfigConstantUtil;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ZhihuNewsActivity  extends Activity
        implements SwipeRefreshLayout.OnRefreshListener,
        ZhihuPresenterImpl.ZhihuDailyImpl{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mNewRecyclerView;
    private ZhihuNewsRecycleAdapter mZhihuNewsRecycleAdapter;
    private List<ZhihuDailyItem> mZhihuDailyItemList = new ArrayList<ZhihuDailyItem>();

    private ZhihuPresenterImpl mZhihuPresenterImpl;

    private String date;

    private Context mContext;
    private  int data_flag = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_news);
        mContext = getApplicationContext();
        initView();

         initData();
    }



    private void initView() {



        mNewRecyclerView = (RecyclerView) findViewById(R.id.acitivity_news_RecyclerView);
        mNewRecyclerView.setHasFixedSize(false);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mNewRecyclerView.setLayoutManager(llm);

        mZhihuNewsRecycleAdapter = new ZhihuNewsRecycleAdapter(getApplicationContext(),mZhihuDailyItemList);
        mNewRecyclerView.setAdapter(mZhihuNewsRecycleAdapter);
    }
    private void initData() {

        Calendar dateToGetUrl = Calendar.getInstance();
        dateToGetUrl.add(Calendar.DAY_OF_YEAR, data_flag-- );
        date = ConfigConstantUtil.Dates.simpleDateFormat.format(dateToGetUrl.getTime());
        mZhihuPresenterImpl = new ZhihuPresenterImpl();

       // mZhihuPresenterImpl.setZhihuDailyImpl(this);

        mZhihuPresenterImpl.getTheDaily(date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                        Log.i("getTheDaily","..........onCompleted.........");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("getTheDaily","..........onError........."+e);
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {

                        mZhihuDailyItemList.addAll(zhihuDaily.getStories());
                        mZhihuNewsRecycleAdapter.notifyDataSetChanged();
                        Log.i("getTheDaily","..........onNext........."+zhihuDaily.getStories().size());
                    }
                });

    }
    @Override
    public void onRefresh() {

    }


    @Override
    public void RxZhihuonNewsNext(ArrayList<ZhihuDailyItem> getStories) {
        Log.i("getStories",".......getStories....."+getStories.size());
        mZhihuDailyItemList.addAll(getStories);
        mZhihuNewsRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void RxZhihuonNewsError() {

    }

    @Override
    public void RxZhihuNewsonCompleted() {

    }


}
