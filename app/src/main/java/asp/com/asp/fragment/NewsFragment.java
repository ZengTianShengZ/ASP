package asp.com.asp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import asp.com.asp.AspApplications;
import asp.com.asp.R;
import asp.com.asp.adapter.ZhihuNewsRecycleAdapter;
import asp.com.asp.domain.ZhihuDaily;
import asp.com.asp.domain.ZhihuDailyItem;
import asp.com.asp.presenter.ZhihuPresenterImpl;
import asp.com.asp.utils.ConfigConstantUtil;
import cn.bingoogolapple.bgabanner.BGABanner;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/12.
 */
@EFragment
public class NewsFragment extends Fragment {

    @ViewById(R.id.fragment_news_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.fragment_news_BGABanner)
    BGABanner mFlipBanner;
    @ViewById(R.id.fragment_news_RecyclerView)
    RecyclerView mNewRecyclerView;

    private List<SimpleDraweeView> mFlipViews;

    private ZhihuNewsRecycleAdapter mZhihuNewsRecycleAdapter;
    private List<ZhihuDailyItem> mZhihuDailyItemList = new ArrayList<ZhihuDailyItem>();
    private List<ZhihuDailyItem> viewpagerNewsList = new ArrayList<ZhihuDailyItem>();
    private List<String> viewpagerTips = new ArrayList<String>();

    private ZhihuPresenterImpl mZhihuPresenterImpl;

    private String date;
    private  int data_flag = 2;
    private View rootView;
    private Context mContext;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mContext = getActivity();
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
        //initEvent();



    }



    private void initView() {
        mNewRecyclerView.setHasFixedSize(false);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mNewRecyclerView.setLayoutManager(llm);

        mZhihuNewsRecycleAdapter = new ZhihuNewsRecycleAdapter(mContext,mZhihuDailyItemList);
        mNewRecyclerView.setAdapter(mZhihuNewsRecycleAdapter);

        mFlipViews = getViews(3);
    }
    private List<SimpleDraweeView> getViews(int count) {
        List<SimpleDraweeView> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            views.add((SimpleDraweeView) rootView.inflate(mContext,R.layout.view_image, null));
        }
        return views;
    }
    private void initData() {

        Calendar dateToGetUrl = Calendar.getInstance();
        dateToGetUrl.add(Calendar.DAY_OF_YEAR, data_flag-- );
        date = ConfigConstantUtil.Dates.simpleDateFormat.format(dateToGetUrl.getTime());
        mZhihuPresenterImpl = new ZhihuPresenterImpl();

        mZhihuPresenterImpl.getTheDaily(date)
                .subscribeOn(Schedulers.newThread())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {

                        for(int i=0;i<3;i++){

                            ZhihuDailyItem zhihuDailyItem = zhihuDaily.getStories().get(0);
                            viewpagerNewsList.add(zhihuDailyItem);
                            viewpagerTips.add(zhihuDailyItem.getTitle());

                            zhihuDaily.getStories().remove(0);
                        }
                        Log.i("refresHandle","........refresHandle2........");
                        mZhihuDailyItemList.addAll(zhihuDaily.getStories());
                        refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                        Log.i("refresHandle","........refresHandle....2....");

                        //2  ---  3   --- 2 说明有发送sendEmptyMessage， 但数据就是不能及时更新
                        // 延迟很严重， 2016-6-3
                     }
                });

    }

    private void initFlip() {

        for(int i=0;i<3;i++){
           String imgUrl = viewpagerNewsList.get(i).getImages()[0];
            mFlipViews.get(i).setImageURI(Uri.parse(imgUrl));
        }
        mFlipBanner.setViews(mFlipViews);
        mFlipBanner.setTips(viewpagerTips);
    }

    /**
     * 更新刷新 数据
     */
    private Handler refresHandle = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){

                case ConfigConstantUtil.loadingNotNew :

                    Toast.makeText(getActivity(),"数据已经最新！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :
                    Log.i("refresHandle","........refresHandle....3....");
                    mZhihuNewsRecycleAdapter.notifyDataSetChanged();
                    initFlip();
                    Toast.makeText(getActivity(),"loadingSuccess！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(getActivity(),"数据到底啦！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(getActivity(),"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                    break;

            }

        }
    };
}
