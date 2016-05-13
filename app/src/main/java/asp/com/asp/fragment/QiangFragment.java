package asp.com.asp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import asp.com.asp.R;
import asp.com.asp.adapter.QiangListAdapter;
import asp.com.asp.domain.Person;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.utils.LoadBmobDataUtil;
import asp.com.asp.view.SnackbarUtil;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/12.
 */
@EFragment
public class QiangFragment  extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout qiang_refresh;

    private ListView qiang_listview;
    private View mRootview;

    private ArrayList<QiangItem> mListItems;
    private QiangListAdapter mQiangListAdapter;

    private LoadBmobDataUtil mLoadBmobDataUtil;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_qiang, container, false);
        initView();
        initData();

        textData();
        return mRootview;
    }

    private void textData() {
        Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");
        p2.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {

                Toast.makeText(getActivity(),"添加数据成功，返回objectId为",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i("loadQiangData","ppppppppppppppeeeeeee+++"+ msg  );
                Toast.makeText(getActivity(),"创建数据失败"+ msg,Toast.LENGTH_LONG).show();
            }
        });

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
    }

    private void initData() {

        mLoadBmobDataUtil = LoadBmobDataUtil.getInstance();
        mLoadBmobDataUtil.initData(getActivity());

        mListItems =new ArrayList<QiangItem>();//cold swaps
        mQiangListAdapter = new QiangListAdapter(getActivity(),mListItems);
        Log.i("loadQiangData","bbbbbbbbbbbb+++"  );
         if(mListItems.size() == 0){

            mListItems.addAll(mLoadBmobDataUtil.loadQiangData(refresHandle));

        }
    }

    @Override
    public void onRefresh() {
        qiang_refresh.setRefreshing(true);

        (new Handler()).postDelayed(new Runnable() {

            @Override
            public void run() {
                //3秒后停止刷新
                qiang_refresh.setRefreshing(false);
                int num = (int)(Math.random() * 100 + 1);
             /*   Snackbar.make(qiang_refresh, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setAction("Action", null).show();*/

                Toast.makeText(getActivity(),"num",Toast.LENGTH_LONG).show();
            }
        }, 3000);
    }

    /**
     * 更新刷新 数据
     */
    private Handler refresHandle = new Handler() {
        public void handleMessage(Message msg) {

            if(msg.what!=0 ){
                Log.i("mListItems","mListItems-----------+++"+mListItems.size());
                qiang_refresh.setRefreshing(false);
            }else{
                qiang_refresh.setRefreshing(false);
                Toast.makeText(getActivity(),"网络异常，请检查网络！",Toast.LENGTH_LONG).show();
                /*
                SnackbarUtil.LongSnackbar(qiang_refresh,"网络异常，请检查网络！",
                        getResources().getColor(R.color.colorAccent),
                        getResources().getColor(R.color.colorPrimaryDark));*/
            }
        }
    };
}
