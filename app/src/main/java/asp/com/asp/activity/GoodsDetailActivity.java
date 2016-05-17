package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import asp.com.asp.R;
import asp.com.asp.adapter.ImagePagerAdapter;
import asp.com.asp.adapter.QiangItemGridViewAdapter;
import asp.com.asp.domain.Goods;
import asp.com.asp.domain.QiangItem;

/**
 * Created by Administrator on 2016/5/17.
 */
@EActivity(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends Activity {

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
    @ViewById(R.id.goods_detail_vp)
    ViewPager img_Viewpager;

    @ViewById(R.id.goods_content_descTv)
    TextView descTv;

    private QiangItem item;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

    }

    @AfterViews
    void updateQiangDate() {
        initData();
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
        }
    }

}
