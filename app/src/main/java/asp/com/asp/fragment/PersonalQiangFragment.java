package asp.com.asp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.R;
import asp.com.asp.activity.PersonalQiangActivity;
import asp.com.asp.adapter.HomePagerAdapter;
import asp.com.asp.adapter.PersonQiangAdapter;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.User;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PersonalQiangFragment extends Fragment {


    private PullToRefreshListView mPullRefreshListView;;

    private ListView personQ_listview;
    private PersonQiangAdapter mPersonQiangAdapter;
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
        mPullRefreshListView = (PullToRefreshListView)mRootview. findViewById(R.id.person_qiang_refresh_list);
        personQ_listview = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);



    }
    private void initData() {

        mUser = ((PersonalQiangActivity)mContext).getUser();
       // mUser = (User)getIntent().getSerializableExtra(ConfigConstantUtil.intentDtat_Author);

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);

        mPersonQiangAdapter = new PersonQiangAdapter(mContext,mListItems);
        personQ_listview.setAdapter(mPersonQiangAdapter);

        if(mListItems.size() == 0){

            mOperationBmobDataUtil.loadPersonData(refresHandle,mUser,mListItems);


        }
    }
    private void initEvent() {

        Toast.makeText(getActivity(),"yyyy",Toast.LENGTH_LONG).show();
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                mOperationBmobDataUtil.refreshPersonData(refresHandle,mUser, mListItems);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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

                    mPersonQiangAdapter.notifyDataSetChanged();
                    break;
                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(mContext,"数据到底啦！！！",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(mContext,"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                    break;

            }
            mPullRefreshListView.onRefreshComplete();

        }
    };

}
