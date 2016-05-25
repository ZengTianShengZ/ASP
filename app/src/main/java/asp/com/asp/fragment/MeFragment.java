package asp.com.asp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import asp.com.appbase.view.CircleImageView;
import asp.com.asp.R;
import asp.com.asp.adapter.PersonalMeViewPagerAdapter;
import asp.com.asp.adapter.PersonalViewPagerAdapter;
import asp.com.asp.adapterPop.ImageLoader;
import asp.com.asp.domain.User;
import asp.com.asp.utils.BlurUtil;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/5/12.
 */
@EFragment
public class MeFragment extends Fragment {

    @ViewById(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @ViewById(R.id.head_layout)
    LinearLayout head_layout;
    @ViewById(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @ViewById(R.id.toolbar_tab)
    TabLayout toolbar_tab;
    @ViewById(R.id.main_vp_container)
    ViewPager main_vp_container;
    @ViewById(R.id.personal_qiang_info_logo)
    SimpleDraweeView info_logo;
    @ViewById(R.id.personal_qiang_info_circleLogo)
    CircleImageView circleLogo;
    @ViewById(R.id.personal_qiang_nameTv)
    TextView nameTv;
    @ViewById(R.id.personal_qiang_signatureTv)
    TextView signatureTv;
    @ViewById(R.id.personal_qiang_contentTv)
    TextView contentTv;

    private User mUser;
    private String userName;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_qiang, container, false);
        mContext = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
        initEvent();
    }



    private void initView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sh_big_bg);
        head_layout.setBackgroundDrawable(new BitmapDrawable(BlurUtil.fastblur(mContext, bitmap, 180)));
        mCollapsingToolbarLayout.setContentScrim(new BitmapDrawable(BlurUtil.fastblur(mContext, bitmap, 180)));
        PersonalMeViewPagerAdapter vpAdapter = new PersonalMeViewPagerAdapter(getFragmentManager());
        main_vp_container.setAdapter(vpAdapter);

        info_logo.setVisibility(View.GONE);
        circleLogo.setVisibility(View.VISIBLE);
    }

    private void initData() {


        SharedPreferencesUtil mSharedPreferencesUtil = SharedPreferencesUtil.getInstance(mContext.getApplicationContext(),mContext.getPackageName());
        SharedPreferences spf =  mSharedPreferencesUtil.getPreferences();
        userName = spf.getString(ConfigConstantUtil.UserName,"");
        if(!userName.equals("")){
            nameTv.setText(userName+"name");
            contentTv.setText(spf.getString(ConfigConstantUtil.UserPassword,"content"));
            // userImg.setImageURI();
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).
                    loadImage(spf.getString(ConfigConstantUtil.UserLogStr,""),circleLogo);
        }
    }

    private void initEvent() {
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle(userName+"");

                } else {
                    mCollapsingToolbarLayout.setTitle(" ");
                }
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
