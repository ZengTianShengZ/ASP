package asp.com.asp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import asp.com.asp.adapter.PersonQiangDgRecycleAdapter;
import asp.com.asp.adapter.PersonQiangRecycleAdapter;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.QiangItemDg;
import asp.com.asp.domain.User;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PersonQiangFragmentDg extends Fragment {

    private RecyclerView qiangDg_recycleview;
    private View progress_loading;
    private PersonQiangDgRecycleAdapter mPersonQiangDgRecycleAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<QiangItemDg> mListItems =  new ArrayList<QiangItemDg>();


    private User mUser;

    private OperationBmobDataUtil mOperationBmobDataUtil;

    private Context mContext;
    private View mRootview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_personal_qiang, container, false);

        mContext = getActivity();

        initView();
        initData();

        initEvent();


        return mRootview;
    }

    private void initView() {
        qiangDg_recycleview = (RecyclerView) mRootview.findViewById(R.id.personal_qiang_recycleview);
        mLinearLayoutManager = new LinearLayoutManager(qiangDg_recycleview.getContext());
        qiangDg_recycleview.setLayoutManager( mLinearLayoutManager);
    }
    private void initData() {
        mUser = ((PersonaQiangActivity)mContext).getUser();
        // mUser = (User)getIntent().getSerializableExtra(ConfigConstantUtil.intentDtat_Author);

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);

        mPersonQiangDgRecycleAdapter = new PersonQiangDgRecycleAdapter(mContext,mListItems);
        qiangDg_recycleview.setAdapter(mPersonQiangDgRecycleAdapter);

        if(mListItems.size() == 0){

            mOperationBmobDataUtil.loadPersonDgData(refresHandle,mUser,mListItems);


        }
    }
    private void initEvent() {

        mPersonQiangDgRecycleAdapter.setOnLoadingListener(new BaseRecycleViewAdapter.OnLoadingListener() {
            @Override
            public void loading(View loadingview) {
                if(progress_loading ==null){
                    progress_loading = loadingview;
                }
                progress_loading.setVisibility(View.VISIBLE);
                mOperationBmobDataUtil.loadPersonDgData(refresHandle,mUser, mListItems);

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

                    mPersonQiangDgRecycleAdapter.notifyDataSetChanged();

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
        mOperationBmobDataUtil.clearPersonQiangDgPageNum();
    }

}
