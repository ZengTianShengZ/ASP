package asp.com.asp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;


import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.R;
import asp.com.asp.adapter.PersonalViewPagerAdapter;
import asp.com.asp.domain.User;
import asp.com.asp.fragment.PersonQiangFragmentDg;
import asp.com.asp.fragment.PersonalQiangFragment;
import asp.com.asp.utils.BlurUtil;
import asp.com.asp.utils.ConfigConstantUtil;

public class PersonaQiangActivity extends AppCompatActivity {

    private LinearLayout head_layout;
    private TabLayout toolbar_tab;
    private ViewPager main_vp_container;
    private SimpleDraweeView info_logo;
    private AppBarLayout app_bar_layout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private PersonalQiangFragment mPersonalQiangFragment;
    private PersonQiangFragmentDg mPersonQiangFragmentDg;
    private List<Fragment> fragments =  new ArrayList<Fragment>();

    private static boolean onRefresh = false;

    private User mUser;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_qiang);
        mContext = getApplicationContext();
        initView();
        initData();
        initEvent();

    }


    private void initView() {
        app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sh_big_bg);
        head_layout.setBackgroundDrawable(new BitmapDrawable(BlurUtil.fastblur(this, bitmap, 180)));
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setContentScrim(new BitmapDrawable(BlurUtil.fastblur(this, bitmap, 180)));


        toolbar_tab = (TabLayout) findViewById(R.id.toolbar_tab);
        main_vp_container = (ViewPager) findViewById(R.id.main_vp_container);

        PersonalViewPagerAdapter vpAdapter = new PersonalViewPagerAdapter(getSupportFragmentManager(), this);
        main_vp_container.setAdapter(vpAdapter);

        //tablayout和viewpager建立联系为什么不用下面这个方法呢？自己去研究一下，可能收获更多
        //toolbar_tab.setupWithViewPager(main_vp_container);
        info_logo = (SimpleDraweeView) findViewById(R.id.personal_qiang_info_logo);
     }

    private void initData() {
        mUser =  (User)getIntent().getSerializableExtra(ConfigConstantUtil.intentDtat_Author);
        // 显示头像
        if (mUser.getAvatar()!=null) {
            info_logo.setImageURI(Uri.parse(mUser.getAvatar().getFileUrl(mContext)));
        }
    }
    public User getUser(){
        return  mUser;
    }

    private void initEvent() {
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle(mUser.getNickname()+"");

                } else {
                    mCollapsingToolbarLayout.setTitle(" ");
                }
                // SwipeRefreshLayout和CoordinatorLayout 冲突
            /*    if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }*/
            }
        });

        final TabLayout.TabLayoutOnPageChangeListener listener =
                new TabLayout.TabLayoutOnPageChangeListener(toolbar_tab);
        main_vp_container.addOnPageChangeListener(listener);
        toolbar_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                main_vp_container.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
