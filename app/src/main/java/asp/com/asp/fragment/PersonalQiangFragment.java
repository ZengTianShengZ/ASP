package asp.com.asp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import asp.com.appbase.adapter.BaseRecycleViewAdapter;
import asp.com.asp.R;
import asp.com.asp.activity.PersonaQiangActivity;
import asp.com.asp.adapter.PersonQiangRecycleAdapter;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.User;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.utils.SwipeRefreshFooterLoading;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PersonalQiangFragment extends Fragment implements SwipeRefreshFooterLoading.OnSwipeLoadListener {


  //  private PullToRefreshListView mPullRefreshListView;;

   // private InnerListView personQ_listview;
    private RecyclerView qiang_recycleview;
    private View progress_loading;
  //  private PersonQiangAdapter mPersonQiangAdapter;
    private PersonQiangRecycleAdapter mPersonQiangRecycleAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<QiangItem> mListItems =  new ArrayList<QiangItem>();

    private User mUser;

    private OperationBmobDataUtil mOperationBmobDataUtil;

    private Context mContext;
    private View mRootview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_personal_qiang, container, false);

        mContext = getActivity();




        return mRootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        qiang_recycleview = (RecyclerView) mRootview.findViewById(R.id.personal_qiang_recycleview);
        mLinearLayoutManager = new LinearLayoutManager(qiang_recycleview.getContext());
        qiang_recycleview.setLayoutManager( mLinearLayoutManager);
    /*    mPullRefreshListView = (PullToRefreshListView)mRootview. findViewById(R.id.person_qiang_refresh_list);
        personQ_listview = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);*/

    }
    private void initData() {


        mUser = ((PersonaQiangActivity)mContext).getUser();
       // mUser = (User)getIntent().getSerializableExtra(ConfigConstantUtil.intentDtat_Author);

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);

        mPersonQiangRecycleAdapter = new PersonQiangRecycleAdapter(mContext,mListItems);
        qiang_recycleview.setAdapter(mPersonQiangRecycleAdapter);

        if(mListItems.size() == 0){

            mOperationBmobDataUtil.loadPersonData(refresHandle,mUser,mListItems);


        }
    }
    private void initEvent() {
      /* SwipeRefreshFooterLoading mSwipeRefreshFooterLoading = new SwipeRefreshFooterLoading(mContext,qiang_recycleview);
        mSwipeRefreshFooterLoading.setOnLoadListener(this);*/
/*        Toast.makeText(getActivity(),"yyyy",Toast.LENGTH_LONG).show();
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                mOperationBmobDataUtil.refreshPersonData(refresHandle,mUser, mListItems);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mOperationBmobDataUtil.loadPersonData(refresHandle,mUser, mListItems);
            }
        });*/
        qiang_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mPersonQiangRecycleAdapter.setOnLoadingListener(new BaseRecycleViewAdapter.OnLoadingListener() {
            @Override
            public void loading(View loadingview) {
                if(progress_loading ==null){
                    progress_loading = loadingview;
                }
                progress_loading.setVisibility(View.VISIBLE);
                mOperationBmobDataUtil.loadPersonData(refresHandle,mUser, mListItems);

            }
        });

    }
    /**
     * 更新刷新 数据
     */
    private Handler refresHandle = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what){

                case ConfigConstantUtil.loadingNotNew :

                    Toast.makeText(mContext,"数据已经最新！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :

                    mPersonQiangRecycleAdapter.notifyDataSetChanged();

                    break;
                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(mContext,"数据到底啦！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(mContext,"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                    break;

            }
            if(progress_loading!=null) {
                progress_loading.setVisibility(View.GONE);
            }
         //   mPullRefreshListView.onRefreshComplete();

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        mOperationBmobDataUtil.clearnPageNum();
    }

    @Override
    public void onSwipeLoading() {

    }
}
