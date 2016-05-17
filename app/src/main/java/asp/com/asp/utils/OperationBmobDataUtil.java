package asp.com.asp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asp.com.asp.domain.Goods;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/16.
 */
public class OperationBmobDataUtil {
    public static final int NUMBERS_PER_PAGE = 2;
    private static int pageNum;
   // private List<QiangItem> mListItems = new ArrayList<QiangItem>();

    private Context mContext;


    private static OperationBmobDataUtil s = null;
    private OperationBmobDataUtil() {
    }

    public static OperationBmobDataUtil getInstance() {
        if (s == null)// 双重否定
        {
            synchronized (OperationBmobDataUtil.class) {
                if (s == null)

                    s = new OperationBmobDataUtil();
            }
        }
        return s;
    }

    public  void initData(Context context) {
        mContext = context ;
        ImageLoaderUtil.getInstance().initData(context);
    }

/*
   public List<QiangItem> getListItem(){
        return  mListItems;
    }*/

    /**
     *  上拉刷新 拉取数据
     * @param refresHandle
     * @param mListItems
     */
    public void loadQiangData(final Handler refresHandle, final List<QiangItem> mListItems){

       // this.mListItems.clear();
        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.order("-createdAt");
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

                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }

            @Override
            public void onSuccess(List<QiangItem> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                   // OperationBmobDataUtil.this.mListItems.addAll(list);
                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                }else{
                    pageNum--;
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotOld);

                }
            }

        });

     //   return this.mListItems;
    }

    /**
     *  下拉刷新 拉取数据
     * @param refresHandle
     * @param mListItems
     */
    public void refreshQiangData(final Handler refresHandle, final String oldTime, final List<QiangItem> mListItems){


        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.order("-createdAt");
        query.setLimit(NUMBERS_PER_PAGE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.setSkip(NUMBERS_PER_PAGE);
        query.include("author");
        query.findObjects(mContext, new FindListener<QiangItem>() {

            @Override
            public void onError(int arg0, String arg1) {
                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }

            @Override
            public void onSuccess(List<QiangItem> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    if( list.get(0).getCreatedAt().equals(oldTime)) {
               /*         refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotNew);
                    }else{
                        mListItems.clear();
                        pageNum = 0;
                        mListItems.addAll(list);
                        refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                    }*/

                        mListItems.clear();
                        pageNum = 0;
                        mListItems.addAll(list);
                        refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                    }

                }else{
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotNew);

                }
            }

        });

    }

    public void sendGoodData(final Handler finishandle,final String commitContent, ArrayList<String> sourcepathlist,final Goods goods){
        String[] targeturls=null;
        targeturls = new String[sourcepathlist.size()];
        int i =0;
        //压缩
        for(String path :sourcepathlist){
            Bitmap bitmap = ImageLoaderUtil.getInstance().compressImageFromFile(path);
            targeturls[i] =ImageLoaderUtil.getInstance().saveToSdCard(bitmap);
            i++;
        }

        BmobProFile.getInstance(mContext).uploadBatch(targeturls, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish,String[] fileNames,String[] urls,BmobFile[] files) {
                // isFinish ：批量上传是否完成
                // fileNames：文件名数组
                // urls        : url：文件地址数组
                // files     : BmobFile文件数组，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                //注：若上传的是图片，url(s)并不能直接在浏览器查看（会出现404错误），需要经过`URL签名`得到真正的可访问的URL地址,当然，`V3.4.1`版本可直接从BmobFile中获得可访问的文件地址。

                if(isFinish) {
                    publishWithoutFigure(finishandle,commitContent,files,goods);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）

            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Log.i("uploadBatch","eee111  "+errormsg);
                finishandle.sendEmptyMessage(ConfigConstantUtil.sendFault);
            }
        });
    }
    private void publishWithoutFigure(final Handler finishandle, final String commitContent,
                                      final BmobFile[] BmobFileList, final Goods goods) {
        User user = BmobUser.getCurrentUser(mContext, User.class);

        // JavaBean  对数据set 到 JavaBean 里面
        final QiangItem qiangitem = new QiangItem();
        qiangitem.setAuthor(user);
        qiangitem.setContent(commitContent);

        if (BmobFileList != null) {
            int flag=0;
            for(BmobFile bf:BmobFileList){
                qiangitem.setBmobFileList(bf,flag);
                flag++;

            }
        }
        qiangitem.setLove(0);
        qiangitem.setHate(0);
        qiangitem.setShare(0);
        qiangitem.setComment(0);
        qiangitem.setPass(true);
        qiangitem.setFocus(false);

        goods.save(mContext,new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO 自动生成的方法存根
                qiangitem.setGoods(goods);
                // Bmob 添加  JavaBean 的  方法  ！！！
                qiangitem.save(mContext, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        finishandle.sendEmptyMessage(ConfigConstantUtil.sendSuccess);
                    }
                    @Override
                    public void onFailure(int arg0, String arg1) {
                        finishandle.sendEmptyMessage(ConfigConstantUtil.sendFault);

                    }
                });
            }
            @Override
            public void onFailure(int arg0, String arg1) {
                finishandle.sendEmptyMessage(ConfigConstantUtil.sendFault);
            }
        });

    }
}