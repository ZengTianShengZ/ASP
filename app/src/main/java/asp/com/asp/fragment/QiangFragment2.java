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
import asp.com.asp.adapter.QiangDgListAdapter;
import asp.com.asp.adapter.QiangListAdapter;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.QiangItemDg;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.utils.SwipeRefreshFooterLoading;

/**
 * Created by Administrator on 2016/5/12.
 */

@EFragment
public class QiangFragment2  extends Fragment  implements SwipeRefreshLayout.OnRefreshListener,SwipeRefreshFooterLoading.OnSwipeLoadListener{

    private SwipeRefreshLayout qiang_refresh;

    private ListView qiang_listview;
    private View mRootview;

    private List<QiangItemDg> mListItems =  new ArrayList<QiangItemDg>();
    private QiangDgListAdapter mQiangDgListAdapter;

    private OperationBmobDataUtil mOperationBmobDataUtil;

    private SwipeRefreshFooterLoading mSwipeRefreshFooterLoading;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_qiang, container, false);
        initView();
        initData();

        return mRootview;
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
        qiang_refresh = (SwipeRefreshLayout) mRootview.findViewById(R.id.qiang_refresh);
        qiang_refresh.setOnRefreshListener(this);

        qiang_listview = (ListView) mRootview.findViewById(R.id.qiang_listview);

        mSwipeRefreshFooterLoading = new SwipeRefreshFooterLoading(getActivity(),qiang_listview);
        mSwipeRefreshFooterLoading.setOnLoadListener(this);
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
    @Override
    public void onRefresh() {

        qiang_refresh.setRefreshing(true);
        mOperationBmobDataUtil.refreshDgQiangData(refresHandleDg,mListItems.get(0).getCreatedAt(),mListItems);
    }

    @Override
    public void onSwipeLoading() {
        mOperationBmobDataUtil.loadDgQiangData(refresHandleDg, mListItems);
    }

    /**
     * 更新刷新 数据
     */
    private Handler refresHandleDg = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){

                case ConfigConstantUtil.loadingNotNew :
                    qiang_refresh.setRefreshing(false);
                    Toast.makeText(getActivity(),"数据已经最新！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :
                    qiang_refresh.setRefreshing(false);
                    mQiangDgListAdapter.notifyDataSetChanged();
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