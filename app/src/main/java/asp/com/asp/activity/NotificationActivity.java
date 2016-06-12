package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.R;
import asp.com.asp.adapter.QiangListAdapter;
import asp.com.asp.domain.Notification;
import asp.com.asp.domain.User;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/6/1.
 */
@EActivity(R.layout.activity_notification)
public class NotificationActivity extends AppCompatActivity {

    @ViewById(R.id.common_top_bar_back_btn)
    ImageView back_btn;
    @ViewById(R.id.common_top_bar_right_titleTv)
    TextView right_titleTv;
    @ViewById(R.id.common_top_bar_center_titleTv)
    TextView center_titleTv;

    @ViewById(R.id.acitivity_notification_refresh_list)
    PullToRefreshListView mPullRefreshListView;

    private OperationBmobDataUtil mOperationBmobDataUtil;
    private ListView notifiListView;
    private List<Notification> mListItems = new ArrayList<Notification>();

    private User bmobUser;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }


    @AfterViews
    void updateQiangDate() {

        initView();
        initData();
        // initEvent();
    }

    private void initView() {


        center_titleTv.setText("通知");
        right_titleTv.setVisibility(View.GONE);
        notifiListView = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initData() {

        bmobUser = BmobUser.getCurrentUser(this, User.class);

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);
        /*mQiangListAdapter = new QiangListAdapter(getActivity(),mListItems);
        qiang_listview.setAdapter(mQiangListAdapter);
        if(mListItems.size() == 0){

            mOperationBmobDataUtil.loadQiangData(refresHandle,mListItems);


        } */
        mOperationBmobDataUtil.loadNotificationData(refresHandle,bmobUser,mListItems);
    }

    private void initEvent() {
    /*    mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("refreshQiangData", "0000");
                mOperationBmobDataUtil.refreshQiangData(refresHandle, mListItems.get(0).getCreatedAt(), mListItems);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mOperationBmobDataUtil.loadQiangData(refresHandle, mListItems);
            }
        });*/

    }

    @Click(R.id.common_top_bar_back_btn)
    void back_btnClick(){

        finish();
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
                    Log.i("loadNotificationData","...............loadNotificationData.........."+mListItems.size());

                   // mQiangListAdapter.notifyDataSetChanged();
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
