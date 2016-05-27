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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import asp.com.appbase.view.CircleImageView;
import asp.com.asp.MainActivity_;
import asp.com.asp.R;
import asp.com.asp.adapter.PersonalMeViewPagerAdapter;
import asp.com.asp.adapter.PersonalViewPagerAdapter;
import asp.com.asp.adapterPop.ImageLoader;
import asp.com.asp.domain.User;
import asp.com.asp.utils.BlurUtil;
import asp.com.asp.utils.BmobUserUtil;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.utils.SharedPreferencesUtil;
import asp.com.asp.view.SnackbarUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

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
    TextView detailTv;
    @ViewById(R.id.personal_qiang_SettingTv)
    TextView SettingTv;
    @ViewById(R.id.personal_qiang_ErCountTv)
    TextView ErCountTv;
    @ViewById(R.id.personal_qiang_DgCountTv)
    TextView DgCountTv;

    private AlertDialog dlg_Comment_alert;

    private OperationBmobDataUtil mOperationBmobDataUtil;

    private User mUser;
    private String userName;
    private String userLogoPath;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {

        }
    }

    private void initView() {
      /*  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sh_big_bg);
        head_layout.setBackgroundDrawable(new BitmapDrawable(BlurUtil.fastblur(mContext, bitmap, 180)));
        mCollapsingToolbarLayout.setContentScrim(new BitmapDrawable(BlurUtil.fastblur(mContext, bitmap, 180)));*/



        PersonalMeViewPagerAdapter vpAdapter = new PersonalMeViewPagerAdapter(getFragmentManager());
        main_vp_container.setAdapter(vpAdapter);

        info_logo.setVisibility(View.GONE);
        circleLogo.setVisibility(View.VISIBLE);
        SettingTv.setVisibility(View.VISIBLE);
    }

    private void initData() {

        mUser =  BmobUser.getCurrentUser(mContext, User.class);

        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(mContext);

        SharedPreferencesUtil mSharedPreferencesUtil = SharedPreferencesUtil.getInstance(mContext.getApplicationContext(),mContext.getPackageName());
        SharedPreferences spf =  mSharedPreferencesUtil.getPreferences();
        userName = spf.getString(ConfigConstantUtil.UserName,"");
        if(!userName.equals("")){

            userLogoPath = spf.getString(ConfigConstantUtil.UserLogStr,"");
            nameTv.setText(userName);
            signatureTv.setText(mUser.getSignature()+"");
            detailTv.setText(mUser.getDetails()+"");
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO). loadImage(userLogoPath,circleLogo);

            mOperationBmobDataUtil.queryEsCount(mContext,mUser.getObjectId(),ErCountTv);
            mOperationBmobDataUtil.queryDgCount(mContext,mUser.getObjectId(),DgCountTv);
        }


        if(userLogoPath!=null){
            new Thread() {
                public void run() {

                   Bitmap bitmap = ImageLoader.getInstance(3, ImageLoader.Type.LIFO).decodeSampledBitmapFromResource(userLogoPath,260,260);
                   final Bitmap blurBitmap =   BlurUtil.fastblur(mContext, bitmap, 70);
                    getActivity(). runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            head_layout.setBackgroundDrawable(new BitmapDrawable(blurBitmap));
                            mCollapsingToolbarLayout.setContentScrim(new BitmapDrawable(blurBitmap));
                        }
                    });
                };

            }.start();
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

    @Click(R.id.personal_qiang_SettingTv)
    void SettingTvClick(final View clickView) {

        View dlg_set_user_data = LayoutInflater.from(mContext).inflate(R.layout.dlg_set_user_data, null);
        dlg_Comment_alert  = new AlertDialog.Builder(mContext).setView(dlg_set_user_data).show();

        final Button sureBtn = (Button) dlg_set_user_data.findViewById(R.id.set_user_data_sureBtn);
        final EditText signatureEv = (EditText) dlg_set_user_data.findViewById(R.id.set_user_data_signatureTv);
        final EditText detailEv = (EditText) dlg_set_user_data.findViewById(R.id.set_user_data_detailTv);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser!=null){
                    mUser.setSignature(signatureTv.getText()+"");
                    mUser.setDetails(detailTv.getText()+"");

                    mUser.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            SnackbarUtil.GreenSnackbar(mContext,clickView,"修改成功！！！");
                            signatureTv.setText(signatureEv.getText()+"");
                            detailTv.setText(detailEv.getText()+"");
                            dlg_Comment_alert.dismiss();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            SnackbarUtil.GreenSnackbar(mContext,clickView,"修改失败！！！");
                            dlg_Comment_alert.dismiss();
                        }
                    });
                }
            }
        });
    }
}
