package asp.com.asp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.R;
import asp.com.asp.activity.GoodsDetailActivity_;
import asp.com.asp.adapter.QiangListAdapter;
import asp.com.asp.domain.EventBusBean;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.utils.SwipeRefreshFooterLoading;

/**
 * Created by Administrator on 2016/5/12.
 */
@EFragment
public class QiangFragment  extends Fragment  {

    private PullToRefreshListView mPullRefreshListView;

    private ListView qiang_listview;


    private List<QiangItem> mListItems =  new ArrayList<QiangItem>();
    private QiangListAdapter mQiangListAdapter;

    private OperationBmobDataUtil mOperationBmobDataUtil;
    private View mRootview;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_qiang, container, false);

        EventBus.getDefault().register(this);

        mContext = getActivity();

        initView();
        initData();

        initEvent();


        return mRootview;
    }



    public static QiangFragment newInstance(int position) {
        // TODO 自动生成的方法存根
        QiangFragment fragment = new QiangFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshListView)mRootview. findViewById(R.id.qiang_pull_refresh_list);
        qiang_listview = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    private void initData() {

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(getActivity());
        mQiangListAdapter = new QiangListAdapter(getActivity(),mListItems);
        qiang_listview.setAdapter(mQiangListAdapter);
         if(mListItems.size() == 0){

             mOperationBmobDataUtil.loadQiangData(refresHandle,mListItems);


        }

    }

    private void initEvent() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("refreshQiangData", "0000");
                mOperationBmobDataUtil.refreshQiangData(refresHandle, mListItems.get(0).getCreatedAt(), mListItems);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mOperationBmobDataUtil.loadQiangData(refresHandle, mListItems);
            }
        });

    }
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        Log.i( "onEventMainThread","................onEventMainThread....................."+event.getMsg());
        mOperationBmobDataUtil.refreshQiangData(refresHandle, mListItems.get(0).getCreatedAt(), mListItems);
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

                    mQiangListAdapter.notifyDataSetChanged();
                    break;
                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(getActivity(),"数据到底啦！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(getActivity(),"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                    break;

            }
            mPullRefreshListView.onRefreshComplete();

        }
    };

}
