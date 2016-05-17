package asp.com.asp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.R;
import asp.com.asp.adapter.QiangListAdapter;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.utils.SwipeRefreshFooterLoading;

/**
 * Created by Administrator on 2016/5/12.
 */
@EFragment
public class QiangFragment  extends Fragment implements SwipeRefreshLayout.OnRefreshListener,SwipeRefreshFooterLoading.OnSwipeLoadListener {

    private SwipeRefreshLayout qiang_refresh;

    private ListView qiang_listview;
    private View mRootview;

    private List<QiangItem> mListItems =  new ArrayList<QiangItem>();
    private QiangListAdapter mQiangListAdapter;

    private OperationBmobDataUtil mOperationBmobDataUtil;

    private SwipeRefreshFooterLoading mSwipeRefreshFooterLoading;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_qiang, container, false);
        initView();
        initData();



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
        qiang_refresh = (SwipeRefreshLayout) mRootview.findViewById(R.id.qiang_refresh);
        qiang_refresh.setOnRefreshListener(this);

        qiang_listview = (ListView) mRootview.findViewById(R.id.qiang_listview);

        mSwipeRefreshFooterLoading = new SwipeRefreshFooterLoading(getActivity(),qiang_listview);
        mSwipeRefreshFooterLoading.setOnLoadListener(this);
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

    @Override
    public void onRefresh() {

        qiang_refresh.setRefreshing(true);

       // mListItems = mOperationBmobDataUtil.loadQiangData(refresHandle);
        mOperationBmobDataUtil.refreshQiangData(refresHandle,mListItems.get(0).getCreatedAt(),mListItems);
/*        (new Handler()).postDelayed(new Runnable() {

            @Override
            public void run() {
                //3秒后停止刷新
                qiang_refresh.setRefreshing(false);
                int num = (int)(Math.random() * 100 + 1);
             *//*   Snackbar.make(qiang_refresh, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setAction("Action", null).show();*//*

             //   Toast.makeText(getActivity(),"num",Toast.LENGTH_LONG).show();
            }
        }, 3000);*/
    }

    @Override
    public void onSwipeLoading() {

        mOperationBmobDataUtil.loadQiangData(refresHandle, mListItems);
    }
    /**
     * 更新刷新 数据
     */
    private Handler refresHandle = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){

                case ConfigConstantUtil.loadingNotNew :
                    qiang_refresh.setRefreshing(false);
                    Toast.makeText(getActivity(),"数据已经最新！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :
                    qiang_refresh.setRefreshing(false);
                   // mQiangListAdapter.addAll(mListItems);
                    mQiangListAdapter.notifyDataSetChanged();
                    break;
                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(getActivity(),"数据到底啦！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(getActivity(),"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                    break;
            }

            mSwipeRefreshFooterLoading.setLoading(false);
        }
    };

}
