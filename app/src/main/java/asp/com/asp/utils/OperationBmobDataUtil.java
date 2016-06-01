package asp.com.asp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asp.com.asp.domain.Comment;
import asp.com.asp.domain.DgVerify;
import asp.com.asp.domain.Goods;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.QiangItemDg;
import asp.com.asp.domain.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/16.
 */
public class OperationBmobDataUtil {
    public static final int NUMBERS_PER_PAGE = 2;
    private static int pageNum;
    private static int chatPageNum;
    private static int dgPageNum;
    private static int personQiangPageNum;
    private static int personMeQiangPageNum;
    private static int personQiangDgPageNum;
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
        mContext = context.getApplicationContext();
        ImageLoaderUtil.getInstance().initData(context);
    }

/*
   public List<QiangItem> getListItem(){
        return  mListItems;
    }*/

    /**
     *  上拉刷新 拉取 二手墙数据
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

    }

    /**
     *  下拉刷新 二手墙数据
     * @param refresHandle
     * @param mListItems
     */
    public void refreshQiangData(final Handler refresHandle, final String oldTime, final List<QiangItem> mListItems){
        Log.i("refreshQiangData","1111");
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
                Log.i("refreshQiangData", "222");
            }

            @Override
            public void onSuccess(List<QiangItem> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){
                    mListItems.clear();
                    pageNum = 0;
                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                    Log.i("refreshQiangData", "333");

                }else{
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotNew);
                    Log.i("refreshQiangData", "444");
                }
            }

        });

    }


    /**
     *  上拉刷新 拉取 代购墙数据
     * @param refresHandle
     * @param mListItems
     */
    public void loadDgQiangData(final Handler refresHandle, final List<QiangItemDg> mListItems){

        // this.mListItems.clear();
        BmobQuery<QiangItemDg> query = new BmobQuery<QiangItemDg>();
        query.order("-createdAt");
        query.setLimit(NUMBERS_PER_PAGE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.setSkip(NUMBERS_PER_PAGE*(dgPageNum++));
        query.include("author");
        query.findObjects(mContext, new FindListener<QiangItemDg>() {

            @Override
            public void onError(int arg0, String arg1) {
                Log.i("onError","onError+++"+arg1);
                dgPageNum--;

                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }

            @Override
            public void onSuccess(List<QiangItemDg> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    // OperationBmobDataUtil.this.mListItems.addAll(list);
                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                }else{
                    dgPageNum--;
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotOld);

                }
            }

        });

    }

    /**
     *  下拉刷新 代购墙数据
     * @param refresHandle
     * @param mListItems
     */
    public void refreshDgQiangData(final Handler refresHandle, final String oldTime, final List<QiangItemDg> mListItems){


        BmobQuery<QiangItemDg> query = new BmobQuery<QiangItemDg>();
        query.order("-createdAt");
        query.setLimit(NUMBERS_PER_PAGE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.setSkip(NUMBERS_PER_PAGE);
        query.include("author");
        query.findObjects(mContext, new FindListener<QiangItemDg>() {

            @Override
            public void onError(int arg0, String arg1) {
                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }

            @Override
            public void onSuccess(List<QiangItemDg> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    mListItems.clear();
                    dgPageNum = 0;
                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);

                }else{
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotNew);

                }
            }

        });

    }


    /**
     *  发布商品
     * @param finishandle
     * @param commitContent
     * @param sourcepathlist
     * @param goods
     * @param noVip
     */
    public void sendGoodData(final Handler finishandle, final String commitContent, ArrayList<String> sourcepathlist, final Goods goods, final boolean noVip){
        String[] targeturls=null;
        targeturls = new String[sourcepathlist.size()];
        int i =0;
        //压缩
        for(String path :sourcepathlist){

                Bitmap bitmap = ImageLoaderUtil.getInstance().compressImageFromFile(path);
                targeturls[i] = ImageLoaderUtil.getInstance().saveToSdCard(bitmap);
                i++;

        }

        BmobProFile.getInstance(mContext).uploadBatch(targeturls, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {
                // isFinish ：批量上传是否完成
                // fileNames：文件名数组
                // urls        : url：文件地址数组
                // files     : BmobFile文件数组，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                //注：若上传的是图片，url(s)并不能直接在浏览器查看（会出现404错误），需要经过`URL签名`得到真正的可访问的URL地址,当然，`V3.4.1`版本可直接从BmobFile中获得可访问的文件地址。

                if (isFinish) {
                    if(noVip){
                        final QiangItemDg qiangitem = new QiangItemDg();
                        publishDgWithoutFigure(finishandle, commitContent, files, goods,qiangitem);
                    }else{
                        final QiangItem qiangitem = new QiangItem();
                        publishWithoutFigure(finishandle, commitContent, files, goods,qiangitem);
                    }

                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）

            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Log.i("uploadBatch", "eee111  " + errormsg);
                finishandle.sendEmptyMessage(ConfigConstantUtil.sendFault);
            }
        });
    }
    private void publishWithoutFigure(final Handler finishandle, final String commitContent,
                                      final BmobFile[] BmobFileList, final Goods goods, final QiangItem qiangitem) {
        User user = BmobUser.getCurrentUser(mContext, User.class);


        // JavaBean  对数据set 到 JavaBean 里面
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

        goods.save(mContext, new SaveListener() {
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
    private void publishDgWithoutFigure(final Handler finishandle, final String commitContent,
                                      final BmobFile[] BmobFileList, final Goods goods, final QiangItemDg qiangitem) {
        User user = BmobUser.getCurrentUser(mContext, User.class);


        // JavaBean  对数据set 到 JavaBean 里面
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

        goods.save(mContext, new SaveListener() {
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
    /**
     * 发送 dg 墙验证照片
     * @param finishandle
     * @param sourcepathlist
     */
    public void sendDgAddV(final Handler finishandle, ArrayList<String> sourcepathlist){

        String[] targeturls=null;
        targeturls = new String[sourcepathlist.size()];
        int i =0;
        //压缩
        for(String path :sourcepathlist){

            Bitmap bitmap = ImageLoaderUtil.getInstance().compressImageFromFile(path);
            targeturls[i] = ImageLoaderUtil.getInstance().saveToSdCard(bitmap);
            i++;

        }

        BmobProFile.getInstance(mContext).uploadBatch(targeturls, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {

                if (isFinish) {
                    User user = BmobUser.getCurrentUser(mContext, User.class);
                    // JavaBean  对数据set 到 JavaBean 里面
                    final DgVerify dgVerify = new DgVerify();
                    dgVerify.setAuthor(user);

                    if (files != null) {
                        int flag=0;
                        for(BmobFile bf:files){
                            dgVerify.setBmobFileList(bf,flag);
                            flag++;

                        }

                        dgVerify.save(mContext, new SaveListener() {

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

                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                finishandle.sendEmptyMessage(ConfigConstantUtil.sendFault);
            }
        });
    }
    /**
     * 评论 记录
     */
    public void loadCommentData(final Handler chatHandle, final List<Comment> commentDatalist,QiangItem item) {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("qiang", item);
        query.include("user");
        query.order("-createdAt");
        query.setLimit(NUMBERS_PER_PAGE);
        query.setSkip(NUMBERS_PER_PAGE * (chatPageNum++));
        query.findObjects(mContext, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> data) {
                // TODO Auto-generated method stub
                if (data.size() != 0 && data.get(data.size() - 1) != null) {

                        commentDatalist.addAll(data);
                    chatHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);

                } else {
                    chatHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotOld);
                    chatPageNum--;
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                chatHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
                chatPageNum--;
            }
        });
    }

    /**
     * 上拉加载  加载用户 发布过的信息
     * @param refresHandle
     * @param mListItems
     */
    public void loadPersonData(final Handler refresHandle, User mUser,final List<QiangItem> mListItems) {

        //筛选 用户信息、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、//
        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.setLimit(NUMBERS_PER_PAGE); // 限制 15条 消息
        query.setSkip(NUMBERS_PER_PAGE* (personQiangPageNum++));
        query.order("-createdAt");
        query.include("author");
        query.addWhereEqualTo("author", mUser);
        query.findObjects(mContext, new FindListener<QiangItem>() {

            @Override
            public void onSuccess(List<QiangItem> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                }else{
                    personQiangPageNum--;
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotOld);

                }
            }
            @Override
            public void onError(int arg0, String arg1) {
                personQiangPageNum--;
                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }
        });

    }
    /**
     * 上拉加载  加载用户 发布过的信息
     * @param refresHandle
     * @param mListItems
     */
    public void loadPersonMeData(final Handler refresHandle, User mUser,final List<QiangItem> mListItems) {

        //筛选 用户信息、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、//
        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.setLimit(NUMBERS_PER_PAGE); // 限制 15条 消息
        query.setSkip(NUMBERS_PER_PAGE* (personMeQiangPageNum++));
        query.order("-createdAt");
        query.include("author");
        query.addWhereEqualTo("author", mUser);
        query.findObjects(mContext, new FindListener<QiangItem>() {

            @Override
            public void onSuccess(List<QiangItem> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                }else{
                    personMeQiangPageNum--;
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotOld);

                }
            }
            @Override
            public void onError(int arg0, String arg1) {
                personMeQiangPageNum--;
                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }
        });

    }
    /**
     * 下拉加载  加载用户 发布过的信息
     * @param refresHandle
     * @param mListItems
     */
    public void refreshPersonData(final Handler refresHandle, User mUser,final List<QiangItem> mListItems) {

        //筛选 用户信息、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、//
        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.setLimit(NUMBERS_PER_PAGE); // 限制 15条 消息
        query.setSkip(NUMBERS_PER_PAGE);
        query.order("-createdAt");
        query.include("author");
        query.addWhereEqualTo("author", mUser);
        query.findObjects(mContext, new FindListener<QiangItem>() {

            @Override
            public void onSuccess(List<QiangItem> list) {
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    mListItems.clear();
                    personQiangPageNum = 0;
                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);

                }else{
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotNew);

                }
            }
            @Override
            public void onError(int arg0, String arg1) {

                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }
        });

    }

    /**
     * 上拉加载  加载用户 发布过的信息
     * @param refresHandle
     * @param mListItems
     */
    public void loadPersonDgData(final Handler refresHandle, User mUser,final List<QiangItemDg> mListItems) {

        //筛选 用户信息、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、//
        BmobQuery<QiangItemDg> query = new BmobQuery<QiangItemDg>();
        query.setLimit(NUMBERS_PER_PAGE); // 限制 15条 消息
        query.setSkip(NUMBERS_PER_PAGE* (personQiangDgPageNum++));
        query.order("-createdAt");
        query.include("author");
        query.addWhereEqualTo("author", mUser);
        query.findObjects(mContext, new FindListener<QiangItemDg>() {

            @Override
            public void onSuccess(List<QiangItemDg> list) {
                // TODO 自动生成的方法存根
                User user = null;
                if(list.size()!=0&&list.get(list.size()-1)!=null){

                    mListItems.addAll(list);
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingSuccess);
                }else{
                    personQiangDgPageNum--;
                    refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingNotOld);

                }
            }
            @Override
            public void onError(int arg0, String arg1) {
                personQiangDgPageNum--;
                refresHandle.sendEmptyMessage(ConfigConstantUtil.loadingFault);
            }
        });

    }

    /**
     * 评论 以及 回复评论
     * @param user
     * @param content
     * @param mReplyTo   mReplyTo if null , mReplyTo = null
     * @param mQiangItem
     * @param chatHandle
     */
    public void publishComment(Context context, User user, String content, User mReplyTo, QiangItem mQiangItem, final Handler chatHandle) {

        final Comment comment = new Comment();
        comment.setUser(user);
        if(mReplyTo!=null){
            // 对谁评论，也就是 回复
            comment.setReplyTo(mReplyTo.getNickname());

        }else{
            // 没有的话 就是 评论而已
            comment.setReplyTo(null);
        }
        comment.setCommentContent(content);
        comment.setQiang(mQiangItem);
        comment.save(context, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //showToast("评论成功。");
                chatHandle.sendEmptyMessage(ConfigConstantUtil.commentSuccess);
                Message message = new Message();
                message.obj = comment;
                chatHandle.sendMessage(message);
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                chatHandle.sendEmptyMessage(ConfigConstantUtil.commentFault);
            }
        });
    }


    /**
     * 查询 指定 用户 发过的 二手 商品的总数  Es :二手
     * @param context
     * @param userId
     * @param countTv
     */
    public void queryEsCount(Context context, String userId, final TextView countTv){
        BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
        query.addWhereEqualTo("author", userId);
        query.count(context, QiangItem.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                countTv.setText(count+" 篇");
            }
            @Override
            public void onFailure(int code, String msg) {
                countTv.setText(null+" 篇");
            }
        });
    }
    /**
     * 查询 指定 用户 发过的 二手 商品的总数  Dg :代购
     * @param context
     * @param userId
     * @param countTv
     */
    public void queryDgCount(Context context, String userId, final TextView countTv){
        BmobQuery<QiangItemDg> query = new BmobQuery<QiangItemDg>();
        query.addWhereEqualTo("author", userId);
        query.count(context, QiangItemDg.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                countTv.setText(count+"");
            }
            @Override
            public void onFailure(int code, String msg) {
                countTv.setText(null+"");
            }
        });
    }
    public void clearnPageNum(){
        personQiangPageNum = 0;
    }
    public void clearPersonQiangDgPageNum(){
        personQiangDgPageNum = 0;
    }
    public void clearPageNum(){
        pageNum = 0;
    }
    public void clearPersonMeQiangPageNum(){
        personMeQiangPageNum = 0;
    }


}
