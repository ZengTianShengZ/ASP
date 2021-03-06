package asp.com.asp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import asp.com.appbase.adapter.BaseRecycleViewAdapter;
import asp.com.asp.R;
import asp.com.asp.adapter.CommentAdapter;
import asp.com.asp.adapter.CommentRecycleviewAdapter;
import asp.com.asp.adapter.ImagePagerAdapter;
import asp.com.asp.domain.Comment;
import asp.com.asp.domain.Goods;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.QiangItemDg;
import asp.com.asp.domain.User;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.DialogUtils;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.view.SnackbarUtil;
import cn.bmob.v3.BmobUser;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/26.
 */
@EActivity(R.layout.activity_goods_datail_cs)
public class GoodsDetailActivityCs extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    @ViewById(R.id.common_top_bar_back_btn)
    ImageView back_btn;
    @ViewById(R.id.common_top_bar_right_titleTv)
    TextView right_titleTv;
    @ViewById(R.id.common_top_bar_center_titleTv)
    TextView center_titleTv;

    @ViewById(R.id.goods_detail_recycleview)
    RecyclerView comment_recycleview;
    @ViewById(R.id.goods_detail_ScrollView)
    ScrollView scrollView;
    @ViewById(R.id.goods_detail_cs_SwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.common_top_bar_parent)
    RelativeLayout top_bar_parent;

    @ViewById(R.id.goods_detail_pinlunBtn)
    TextView pinlunBtn;
    @ViewById(R.id.goods_detail_callPhoneBtn)
    TextView callPhoneBtn;

    @ViewById(R.id.item_qiang_logo)
    SimpleDraweeView qiang_logo;
    @ViewById(R.id.item_qiang_nameTv)
    TextView nameTv;
    @ViewById(R.id.item_qiang_time_Tv)
    TextView time_Tv;
    @ViewById(R.id.item_qiang_comment_Tv)
    TextView comment_Tv;

    @ViewById(R.id.goods_content_goodsTv)
    TextView goodsTv;
    @ViewById(R.id.goods_content_sortTv)
    TextView sortTv;
    @ViewById(R.id.goods_detail_priceTv)
    TextView priceTv;
    @ViewById(R.id.goods_detail_vp)
    ViewPager img_Viewpager;


    @ViewById(R.id.goods_content_descTv)
    TextView descTv;
    @ViewById(R.id.goods_detail_totalImgNumTv)
    TextView totalImgNumTv;
    @ViewById(R.id.goods_detail_imgNumTv)
    TextView imgNumTv;

    private View progress_loading;

    private CommentRecycleviewAdapter mCommentRecycleviewAdapter;
    private QiangItem item;
    private QiangItemDg itemDg;
    private List<Comment> commentDatalist = new ArrayList<Comment>();
    private OperationBmobDataUtil mOperationBmobDataUtil;
    private String sailerPhone;
    private int comment_Count;

    private DialogUtils dlg_pingLun_View;

    private AlertDialog dlg_Comment_alert;

    private User bmobUser;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mContext = getApplicationContext();
        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);
    }

    @AfterViews
    void AfterActivityViews() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setEnabled(false);

        initView();
        initData();
        initEvent();
    }
    private void initView(){

        comment_recycleview.setLayoutManager( new LinearLayoutManager(comment_recycleview.getContext()));

        dlg_pingLun_View = new DialogUtils.Builder().titleStr("评论")
                .leftBtnStr("取消").rightBtnStr("确定").build();

    }
    private void initData() {

        Intent intent = getIntent();
        bmobUser = BmobUser.getCurrentUser(this, User.class);
        comment_Count = intent.getIntExtra("comment_Count",0);

        item = (QiangItem)intent.getSerializableExtra("itemQiang");
        if(item == null){
            itemDg = (QiangItemDg)intent.getSerializableExtra("itemDgQiang");
            initQiangDgData();
        }else {
            initQiangData();
        }

    }


    private void initQiangData() {
        String Imageurl = null;
        if (item.getAuthor().getAvatar() != null) {
            Imageurl = item.getAuthor().getAvatar().getFileUrl(mContext);
            qiang_logo.setImageURI(Uri.parse(Imageurl));
        }

        nameTv.setText(item.getAuthor().getNickname() + "");
        time_Tv.setText(item.getCreatedAt() + "");
        descTv.setText(item.getContent() + "");
        comment_Tv.setText(" "+comment_Count);

        mOperationBmobDataUtil.queryGoods(mContext,item.getGoods().getObjectId());


        if (null == item.getContentfigureurl()) {
            return;
        } else {

            ArrayList<String> paths = new ArrayList<String>();
            if (item.getContentfigureurl() != null)
                paths.add(item.getContentfigureurl().getFileUrl(mContext));
            if (item.getContentfigureurl1() != null)
                paths.add(item.getContentfigureurl1().getFileUrl(mContext));
            if (item.getContentfigureurl2() != null)
                paths.add(item.getContentfigureurl2().getFileUrl(mContext));
            if (item.getContentfigureurl3() != null)
                paths.add(item.getContentfigureurl3().getFileUrl(mContext));
            if (item.getContentfigureurl4() != null)
                paths.add(item.getContentfigureurl4().getFileUrl(mContext));
            if (item.getContentfigureurl5() != null)
                paths.add(item.getContentfigureurl5().getFileUrl(mContext));
            if (item.getContentfigureurl6() != null)
                paths.add(item.getContentfigureurl6().getFileUrl(mContext));
            if (item.getContentfigureurl7() != null)
                paths.add(item.getContentfigureurl7().getFileUrl(mContext));
            if (item.getContentfigureurl8() != null)
                paths.add(item.getContentfigureurl8().getFileUrl(mContext));

            ImagePagerAdapter mImagePagerAdapter = new ImagePagerAdapter(mContext, paths);
            img_Viewpager.setAdapter(mImagePagerAdapter);
            totalImgNumTv.setText(paths.size() + "");
            imgNumTv.setText("1");

        }

        center_titleTv.setText("商品详情");
        right_titleTv.setVisibility(View.GONE);

        mCommentRecycleviewAdapter = new CommentRecycleviewAdapter(mContext,commentDatalist);
        comment_recycleview.setAdapter(mCommentRecycleviewAdapter);
        mOperationBmobDataUtil.loadCommentData(chatHandle, commentDatalist, item);
    }
    private void initQiangDgData() {
        String Imageurl = null;
        if (itemDg.getAuthor().getAvatar() != null) {
            Imageurl = itemDg.getAuthor().getAvatar().getFileUrl(mContext);
            qiang_logo.setImageURI(Uri.parse(Imageurl));
        }

        nameTv.setText(itemDg.getAuthor().getNickname() + "");
        time_Tv.setText(itemDg.getCreatedAt() + "");
        descTv.setText(itemDg.getContent() + "");
        comment_Tv.setText(" "+comment_Count);

        mOperationBmobDataUtil.queryGoods(mContext,itemDg.getGoods().getObjectId());


        if (null == itemDg.getContentfigureurl()) {
            return;
        } else {

            ArrayList<String> paths = new ArrayList<String>();
            if (itemDg.getContentfigureurl() != null)
                paths.add(itemDg.getContentfigureurl().getFileUrl(mContext));
            if (itemDg.getContentfigureurl1() != null)
                paths.add(itemDg.getContentfigureurl1().getFileUrl(mContext));
            if (itemDg.getContentfigureurl2() != null)
                paths.add(itemDg.getContentfigureurl2().getFileUrl(mContext));
            if (itemDg.getContentfigureurl3() != null)
                paths.add(itemDg.getContentfigureurl3().getFileUrl(mContext));
            if (itemDg.getContentfigureurl4() != null)
                paths.add(itemDg.getContentfigureurl4().getFileUrl(mContext));
            if (itemDg.getContentfigureurl5() != null)
                paths.add(itemDg.getContentfigureurl5().getFileUrl(mContext));
            if (itemDg.getContentfigureurl6() != null)
                paths.add(itemDg.getContentfigureurl6().getFileUrl(mContext));
            if (itemDg.getContentfigureurl7() != null)
                paths.add(itemDg.getContentfigureurl7().getFileUrl(mContext));
            if (itemDg.getContentfigureurl8() != null)
                paths.add(itemDg.getContentfigureurl8().getFileUrl(mContext));

            ImagePagerAdapter mImagePagerAdapter = new ImagePagerAdapter(mContext, paths);
            img_Viewpager.setAdapter(mImagePagerAdapter);
            totalImgNumTv.setText(paths.size() + "");
            imgNumTv.setText("1");

        }

        center_titleTv.setText("商品详情");
        right_titleTv.setVisibility(View.GONE);

        mCommentRecycleviewAdapter = new CommentRecycleviewAdapter(mContext,commentDatalist);
        comment_recycleview.setAdapter(mCommentRecycleviewAdapter);
        mOperationBmobDataUtil.loadCommentData_Dg(chatHandle, commentDatalist, itemDg);
    }

    private void initEvent() {

        img_Viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imgNumTv.setText(position+1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //scrollView.getScrollY() 可以得到 scrollView 滑动了 多少距离（从顶部开始计算）
                    //view.getHeight() 得到的是 view 的 px 值（更屏幕密度有关），不是 dp值
                    if(scrollView.getScrollY()<=0){
                       // mSwipeRefreshLayout.setEnabled(true);
                        //Toast.makeText(mContext, "到达顶部了", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        mCommentRecycleviewAdapter.setReplayTvOnClickListener(new CommentRecycleviewAdapter.ReplayTvOnClickListener() {
            @Override
            public void replayTvClick(final String nickname) {
                if(bmobUser != null){
                    DialogUtils dlg_replay_View = new DialogUtils.Builder().titleStr("回复 ："+nickname)
                            .leftBtnStr("取消").rightBtnStr("确定").build();
                    dlg_Comment_alert = new AlertDialog.Builder(GoodsDetailActivityCs.this).setView(dlg_replay_View.getDialogView(GoodsDetailActivityCs.this)).show();

                    dlg_replay_View.leftButtonClickListener(new DialogUtils.DialogLeftButtonClick(){

                        @Override
                        public void dialogLeftButtonClickListener() {
                            dlg_Comment_alert.dismiss();
                        }
                    });
                    dlg_replay_View.rightButtonClickListener(new DialogUtils.DialogRightButtonClick(){

                        @Override
                        public void dialogRightButtonClickListener(String editStr) {
                            dlg_Comment_alert.dismiss();
                            if(TextUtils.isEmpty(editStr)){
                                SnackbarUtil.GreenSnackbar(mContext,pinlunBtn,"评论不能为空");
                            }else{
                                if(item == null){
                                    mOperationBmobDataUtil.publishComment_Dg(mContext,bmobUser,"[@"+nickname+"]"+editStr,null,itemDg,chatHandle);

                                }else{
                                    mOperationBmobDataUtil.publishComment(mContext,bmobUser,"[@"+nickname+"]"+editStr,null,item,chatHandle);

                                }
                             }
                            dlg_Comment_alert.dismiss();
                        }
                    } );
                }else{

                    SnackbarUtil.GreenSnackbar(mContext,pinlunBtn,"请先登录");
                }
            }
        });

        // 点击评论加载更多
        mCommentRecycleviewAdapter.setOnLoadingListener(new BaseRecycleViewAdapter.OnLoadingListener() {
            @Override
            public void loading(View loadingview) {
                if(progress_loading ==null){
                    progress_loading = loadingview;
                }
                progress_loading.setVisibility(View.VISIBLE);
                if(item==null){
                    mOperationBmobDataUtil.loadCommentData_Dg(chatHandle, commentDatalist, itemDg);
                }else{
                    mOperationBmobDataUtil.loadCommentData(chatHandle, commentDatalist, item);
                }

            }
        });
    }

    @Subscribe
    public void onEventMainThread(Goods goods) {

        goodsTv.setText(goods.getName()+"");
        sortTv.setText(goods.getCategory()+"");
        priceTv.setText("价格："+goods.getPrice()+"");
        sailerPhone = goods.getCellphone();
    }


    /**
     * 按推出按钮
     */
    @Click(R.id.common_top_bar_back_btn)
    void backBtnClick(){

        finish();
    }
    @Click(R.id.goods_detail_pinlunBtn)
    void pinlunBtnClick(final View clickedView){

        if(bmobUser != null){

            dlg_Comment_alert = new AlertDialog.Builder(GoodsDetailActivityCs.this).setView(dlg_pingLun_View.getDialogView(this)).show();

            dlg_pingLun_View.leftButtonClickListener(new DialogUtils.DialogLeftButtonClick(){

                @Override
                public void dialogLeftButtonClickListener() {
                    dlg_Comment_alert.dismiss();
                }
            });
            dlg_pingLun_View.rightButtonClickListener(new DialogUtils.DialogRightButtonClick(){

                @Override
                public void dialogRightButtonClickListener(String editStr) {
                    dlg_Comment_alert.dismiss();
                    if(TextUtils.isEmpty(editStr)){
                        SnackbarUtil.GreenSnackbar(mContext,clickedView,"评论不能为空");
                    }else{
                        mOperationBmobDataUtil.publishComment(mContext,bmobUser,editStr,null,item,chatHandle);
                    }
                    dlg_Comment_alert.dismiss();
                }
            } );
        }else{

            SnackbarUtil.GreenSnackbar(mContext,clickedView,"请先登录");
        }

    }
    @Click(R.id.goods_detail_callPhoneBtn)
    void callPhoneBtnClick(final View clickedView){
       // mSwipeRefreshLayout.setEnabled(false);
      //  mSwipeRefreshLayout.setRefreshing(false);

        if(bmobUser != null) {
            Uri uri = Uri.parse("tel:" + sailerPhone);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        }else{
            SnackbarUtil.GreenSnackbar(mContext,clickedView,"请先登录");
        }

    }
    @Override
    public void onRefresh() {
        Log.i("onRefresh","........onRefresh............");
    }
    /**
     * 更新刷新 数据
     */
    private Handler chatHandle = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("Message","..........Message..............."+ msg.obj);
            if(msg.obj!=null){
                Comment comment = (Comment) msg.obj;
                mCommentRecycleviewAdapter.insertedItem(comment,0);

            }

            switch (msg.what){

                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(mContext,"已加载完所有评论~",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :
                    mCommentRecycleviewAdapter.notifyDataSetChanged();
                    break;
                case ConfigConstantUtil.loadingFault :
                    Toast.makeText(mContext,"获取评论失败。请检查网络~~",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.commentSuccess :
                    Toast.makeText(mContext,"评论成功~~",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.commentFault :
                    Toast.makeText(mContext,"评论失败~~",Toast.LENGTH_LONG).show();
                    break;

            }

            if(progress_loading!=null){
                progress_loading.setVisibility(View.GONE);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOperationBmobDataUtil.cleachatPageNum();
        EventBus.getDefault().unregister(this);
    }
}
