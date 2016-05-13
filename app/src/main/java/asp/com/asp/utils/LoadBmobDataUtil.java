package asp.com.asp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/5/13.
 */
public class LoadBmobDataUtil {

    public static final int NUMBERS_PER_PAGE = 15;
    private static int pageNum;
     private List<QiangItem> mListItems = new ArrayList<QiangItem>();

    private   Context mContext;


    private static LoadBmobDataUtil s = null;
    private LoadBmobDataUtil() {
    }

    public static LoadBmobDataUtil getInstance() {
        if (s == null)// 双重否定
        {
            synchronized (LoadBmobDataUtil.class) {
                if (s == null)

                s = new LoadBmobDataUtil();
            }
        }
        return s;
    }

    public  void initData(Context context) {
        mContext = context ;
    }

    /**
     *  下拉刷新 拉取数据
     * @param refresHandle
     */
    public  List<QiangItem> loadQiangData(final Handler refresHandle){
        Log.i("loadQiangData","lllll+++" );
        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
        query.setLimit(NUMBERS_PER_PAGE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.setSkip(NUMBERS_PER_PAGE*(pageNum++));
        query.include("author");
        query.findObjects(mContext, new FindListener<QiangItem>() {

            @Override
            public void onError(int arg0, String arg1) {
                // TODO 自动生成的方法存根
                pageNum--;
                Log.i("loadQiangData","eeeeeeee+++"+arg1 );
                refresHandle.sendEmptyMessage(0);
            }

            @Override
            public void onSuccess(List<QiangItem> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    mListItems.addAll(list);
                    Log.i("loadQiangData","ssssssssssssssssssss+++"  );
                    Log.i("mListItems","mListItems+++"+mListItems.size());
                    Toast.makeText(mContext,"xxxxxxxxxxx"+mListItems.size(),Toast.LENGTH_LONG).show();
                    refresHandle.sendEmptyMessage(1);
                }else{
                    pageNum--;
                   /* Message message = new Message();
                    message.obj = 0;
                    refresHandle.sendMessage(message);*/

                }
            }

        });

        return mListItems;
    }

}
