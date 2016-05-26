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
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/25.
 */
@EFragment
public class PersonalMeFragment extends Fragment{


    private RecyclerView qiangMe_recycleview;
    private View progress_loading;

    private PersonQiangRecycleAdapter mPersonMeRecycleAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<QiangItem> mListItems =  new ArrayList<QiangItem>();

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
        qiangMe_recycleview = (RecyclerView) mRootview.findViewById(R.id.personal_qiang_recycleview);
        mLinearLayoutManager = new LinearLayoutManager(qiangMe_recycleview.getContext());
        qiangMe_recycleview.setLayoutManager( mLinearLayoutManager);

    }
    private void initData() {

        mUser  = BmobUser.getCurrentUser(mContext, User.class);

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);

        mPersonMeRecycleAdapter = new PersonQiangRecycleAdapter(mContext,mListItems);
        qiangMe_recycleview.setAdapter(mPersonMeRecycleAdapter);

        if(mListItems.size() == 0){

            mOperationBmobDataUtil.loadPersonData(refresHandle,mUser,mListItems);


        }
    }
    private void initEvent() {

        qiangMe_recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mPersonMeRecycleAdapter.setOnLoadingListener(new BaseRecycleViewAdapter.OnLoadingListener() {
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

                    mPersonMeRecycleAdapter.notifyDataSetChanged();

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
        }
    };
    @Override
    public void onStop() {
        super.onStop();
        mOperationBmobDataUtil.clearPersonQiangDgPageNum();
    }

}
