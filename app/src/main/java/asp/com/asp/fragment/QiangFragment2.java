package asp.com.asp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.MainActivity;
import asp.com.asp.R;
import asp.com.asp.adapter.QiangDgListAdapter;
import asp.com.asp.adapter.QiangListAdapter;
import asp.com.asp.domain.EventBusBean;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.QiangItemDg;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.utils.SwipeRefreshFooterLoading;

/**
 * Created by Administrator on 2016/5/12.
 */

@EFragment
public class QiangFragment2  extends Fragment  {

    private PullToRefreshListView mPullRefreshListView;

    private ListView qiang_listview;
    private View mRootview;

    private List<QiangItemDg> mListItems =  new ArrayList<QiangItemDg>();
    private QiangDgListAdapter mQiangDgListAdapter;

    private OperationBmobDataUtil mOperationBmobDataUtil;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_qiang, container, false);
        EventBus.getDefault().register(this);
        initView();

        return mRootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        initEvent();

    }

    public static QiangFragment2 newInstance(int position) {
        // TODO 自动生成的方法存根
        QiangFragment2 fragment = new QiangFragment2();
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
        mQiangDgListAdapter = new QiangDgListAdapter(getActivity(),mListItems);
        qiang_listview.setAdapter(mQiangDgListAdapter);
        if(mListItems.size() == 0){

            mOperationBmobDataUtil.loadDgQiangData(refresHandleDg,mListItems);


        }
    }
    private void initEvent() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                if(mListItems.size()==0){
                    mPullRefreshListView.onRefreshComplete();
                }else{
                    mOperationBmobDataUtil.refreshDgQiangData(refresHandleDg,  mListItems);

                }
             }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mOperationBmobDataUtil.loadDgQiangData(refresHandleDg, mListItems);
            }
        });

    }
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        Log.i( "onEventMainThread","................onEventMainThread....................."+event.getMsg());
        if((ConfigConstantUtil.Send_DG_Goods).equals(event.getMsg())){
            mOperationBmobDataUtil.refreshDgQiangData(refresHandleDg,  mListItems);
        }

    }
    /**
     * 更新刷新 数据
     */
    private Handler refresHandleDg = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){

                case ConfigConstantUtil.loadingNotNew :

                    Toast.makeText(getActivity(),"数据已经最新22！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :

                    mQiangDgListAdapter.notifyDataSetChanged();
                    break;
                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(getActivity(),"数据到底啦22！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(getActivity(),"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                 // onError+++object not found for QiangItemDg.
                    break;
            }
            mPullRefreshListView.onRefreshComplete();
            ((MainActivity)getActivity()).dismissShapeLoadingDialog();
        }
    };
}