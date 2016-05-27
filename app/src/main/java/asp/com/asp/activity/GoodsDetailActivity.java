package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import asp.com.asp.AspApplications;
import asp.com.asp.R;
import asp.com.asp.adapter.CommentAdapter;
import asp.com.asp.adapter.ImagePagerAdapter;
import asp.com.asp.domain.Comment;
import asp.com.asp.domain.Goods;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.User;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.DialogUtils;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.view.SnackbarUtil;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/17.
 */
@EActivity(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends Activity  {

    @ViewById(R.id.goods_detail_pull_refresh_scrollview)
    PullToRefreshScrollView mPullRefreshScrollView;

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
    @ViewById(R.id.goods_detail_pinlunBtn)
    TextView pinlunBtn;
    @ViewById(R.id.goods_detail_callPhoneBtn)
    TextView callPhoneBtn;

    @ViewById(R.id.goods_detail_totalImgNumTv)
    TextView totalImgNumTv;
    @ViewById(R.id.goods_detail_imgNumTv)
    TextView imgNumTv;
    @ViewById(R.id.goods_detail_listview)
    ListView chatistview;

    private DialogUtils dlg_view;
    private AlertDialog dlg_Comment_alert;

    private CommentAdapter mCommentAdapter;
    private QiangItem item;

    private List<Comment> commentDatalist = new ArrayList<Comment>();
    private OperationBmobDataUtil mOperationBmobDataUtil;
    private User bmobUser;


    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);
    }

    @AfterViews
    void updateQiangDate() {
        initData();
        initEvent();
    }
    private void initData() {
        item = (QiangItem) getIntent().getSerializableExtra("itemQiang");

        String Imageurl = null;
        if (item.getAuthor().getAvatar() != null) {
            Imageurl = item.getAuthor().getAvatar().getFileUrl(mContext);
            qiang_logo.setImageURI(Uri.parse(Imageurl));
        }
        nameTv.setText(item.getAuthor().getNickname() + "");

        time_Tv.setText(item.getCreatedAt() + "");

        descTv.setText(item.getContent() + "");

        Goods goods = item.getGoods();
        goodsTv.setText(goods.getName()+"");
        sortTv.setText(goods.getCategory()+"");
        priceTv.setText("价格："+goods.getPrice()+"");

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

        // 设置 评论 适配器
        mCommentAdapter = new CommentAdapter(this, commentDatalist,item);
        chatistview.setAdapter(mCommentAdapter);
        mOperationBmobDataUtil.loadCommentData(chatHandle, commentDatalist, item);

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

        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mOperationBmobDataUtil.loadCommentData(chatHandle, commentDatalist, item);
            }
        });
    }
    @Click(R.id.goods_detail_pinlunBtn)
    void pinlunBtnClick(final View clickedView){

        bmobUser = BmobUser.getCurrentUser(this, User.class);
        if(bmobUser != null){
            dlg_view = new DialogUtils.Builder().titleStr("评论")
                    .leftBtnStr("取消").rightBtnStr("确定").build();
            dlg_Comment_alert = new AlertDialog.Builder(GoodsDetailActivity.this).setView(dlg_view.getDialogView(this)).show();

            dlg_view.leftButtonClickListener(new DialogUtils.DialogLeftButtonClick(){

                @Override
                public void dialogLeftButtonClickListener() {
                    dlg_Comment_alert.dismiss();
                }
            });
            dlg_view.rightButtonClickListener(new DialogUtils.DialogRightButtonClick(){

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
    /**
     * 更新刷新 数据
     */
    private Handler chatHandle = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("Message","..........Message..............."+ msg.obj);
            if(msg.obj!=null){
                Comment comment = (Comment) msg.obj;
                commentDatalist.add(0,comment);
                mCommentAdapter.notifyDataSetChanged();
            }

            switch (msg.what){

                case ConfigConstantUtil.loadingNotOld :
                    Toast.makeText(mContext,"已加载完所有评论~",Toast.LENGTH_LONG).show();
                    break;
                case ConfigConstantUtil.loadingSuccess :
                    mCommentAdapter.notifyDataSetChanged();
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
            mPullRefreshScrollView.onRefreshComplete();
        }
    };

}
