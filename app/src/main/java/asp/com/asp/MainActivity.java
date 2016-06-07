package asp.com.asp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import asp.com.appbase.view.BottomTagView;
import asp.com.appbase.view.CircleImageView;
import asp.com.asp.activity.AboutMeActivity_;
import asp.com.asp.activity.AddGdActivity_;
import asp.com.asp.activity.EditQiangActivity_;
import asp.com.asp.activity.NotificationActivity_;
import asp.com.asp.activity.ZhihuNewsActivity;
import asp.com.asp.adapterPop.ImageLoader;
import asp.com.asp.domain.User;
import asp.com.asp.fragment.HomeFragment_;
import asp.com.asp.fragment.MeFragment_;
import asp.com.asp.fragment.ZhiHuNewsFragment;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.SharedPreferencesUtil;
import asp.com.asp.weibo.WBAuthActivity_;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/11.
 */
@EActivity(R.layout.activity_main2)
public class MainActivity extends FragmentActivity  implements  View.OnClickListener {
//NavigationView.OnNavigationItemSelectedListener,
    private FragmentManager mFragmentManager ;
    private FragmentTransaction mTransaction;

    private HomeFragment_ mHomeFragment;
    //private NewsFragment_ mNewsFragment;
    private MeFragment_ mMeFragment;

    private ZhiHuNewsFragment mZhiHuNewsFragment;

   /* private View headerView;
    private CircleImageView userImg;
    private TextView nameTv,contentTv;*/

    private SharedPreferencesUtil mSharedPreferencesUtil;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
/*    @ViewById(R.id.nav_view)
    NavigationView navigationView;*/

    @ViewById(R.id.nav_header_mian_cirImg)
    CircleImageView userImg;
    @ViewById(R.id.nav_header_mian_nameTv)
    TextView nameTv;
    @ViewById(R.id.nav_header_mian_contentTv)
    TextView contentTv;

    @ViewById(R.id.main_base_hong_btn)
    BottomTagView hong_btn;
    @ViewById(R.id.main_base_new_btn)
    BottomTagView new_btn;
    @ViewById(R.id.main_base_me_btn)
    BottomTagView me_btn;
/*
    @ViewById(R.id.nav_header_mian_simpImg)
    SimpleDraweeView userImg;
    @ViewById(R.id.nav_header_mian_nameTv)
    TextView nameTv;
    @ViewById(R.id.nav_header_mian_contentTv)
    TextView contentTv;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void afterViews() {

        initView();
        initData();

    }


    private void initView() {
       /* headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userImg = (CircleImageView) headerView.findViewById(R.id.nav_header_mian_cirImg);
        nameTv = (TextView) headerView.findViewById(R.id.nav_header_mian_nameTv);
        contentTv = (TextView) headerView.findViewById(R.id.nav_header_mian_contentTv);
*/


    }

    private void initData() {
        mSharedPreferencesUtil =SharedPreferencesUtil.getInstance(getApplicationContext(),getPackageName());

        SharedPreferences spf =  mSharedPreferencesUtil.getPreferences();

        String userName = spf.getString(ConfigConstantUtil.UserName,"");
        if(!userName.equals("")){
            nameTv.setText(userName+"name");
            contentTv.setText(spf.getString(ConfigConstantUtil.UserPassword,"content"));
           // userImg.setImageURI();
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).
                    loadImage(spf.getString(ConfigConstantUtil.UserLogStr,""),userImg);
        }

        // 使用 @EActivity 控件的注册事件要放在 onStart（），不然会抛空指针
        //navigationView.setNavigationItemSelectedListener(this);
        hong_btn.setIconAlpha(1);

        mFragmentManager =getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        if(mHomeFragment==null){
            mHomeFragment =new HomeFragment_();
            mTransaction.add(R.id.main_base_fragment, mHomeFragment);
        }else{
            mTransaction.show(mHomeFragment);
        }
        mTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();




    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Click(R.id.main_base_hong_btn)
    void hongBtnClick(){
        setIconAlpha_0();
        hong_btn.setIconAlpha(1);
        setSelected(0);
    }
    @Click(R.id.main_base_new_btn)
    void newBtnClick(){
        setIconAlpha_0();
        new_btn.setIconAlpha(1);
        setSelected(1);
    }
    @Click(R.id.main_base_me_btn)
    void editBtnClick(){
        setIconAlpha_0();
        me_btn.setIconAlpha(1);
        setSelected(2);
    }

    /*****************侧拉菜单点击事件*******************/
    @Click(R.id.main_drawer_singin)
    void singinBtnClick(){

        Intent intent = new Intent(this,WBAuthActivity_.class);
        startActivityForResult(intent,123);
        drawerLayout.closeDrawers();
    }
    @Click(R.id.main_drawer_add_v)
    void add_vBtnClick(){
        drawerLayout.closeDrawers();
        Intent intent = new Intent(this,AddGdActivity_.class);
        startActivity(intent);

    }
    @Click(R.id.main_drawer_send_goods)
    void send_goodsBtnClick(){
        drawerLayout.closeDrawers();
        Intent intent = new Intent(this,EditQiangActivity_.class);
        startActivity(intent);
    }
    @Click(R.id.main_drawer_dialog)
    void dialogBtnClick(){
        drawerLayout.closeDrawers();

        Intent intent = new Intent(this,NotificationActivity_.class);
        startActivity(intent);
    }
    @Click(R.id.main_drawer_about_me)
    void about_meBtnClick(){
        drawerLayout.closeDrawers();
        /*Intent intent = new Intent(this,AboutMeActivity_.class);
        startActivity(intent);*/
        Intent intent = new Intent(this,ZhihuNewsActivity.class);
        startActivity(intent);
    }
    @Click(R.id.main_drawer_share)
    void shareBtnClick(){
        drawerLayout.closeDrawers();
        Intent intent = new Intent(this,ZhihuNewsActivity.class);
        startActivity(intent);


    }
    @Click(R.id.include_activity_main_drawer)
    void include_activity_main_drawerClick(){
        drawerLayout.closeDrawers();
    }

 /*   @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_singin) {
            Intent intent = new Intent(this,WBAuthActivity_.class);
            startActivityForResult(intent,123);

        } else if (id == R.id.nav_add_v) {

        } else if (id == R.id.nav_send_goods) {
            Intent intent = new Intent(this,EditQiangActivity_.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_ous) {


        } else if (id == R.id.nav_share_app) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void setSelected(int index) {

        mTransaction = mFragmentManager.beginTransaction();

        hideFragment(mTransaction);
        switch(index){
            case 0:
                if(mHomeFragment==null){
                    mHomeFragment =new HomeFragment_();
                    mTransaction.add(R.id.main_base_fragment, mHomeFragment);
                }else{
                    mTransaction.show(mHomeFragment);
                }


                break;
            case 1:
                if(mZhiHuNewsFragment==null){
                    mZhiHuNewsFragment =new  ZhiHuNewsFragment();

                    mTransaction.add(R.id.main_base_fragment, mZhiHuNewsFragment);
                }else{
                    mTransaction.show(mZhiHuNewsFragment);
                }

                break;
            case 2:
                if(mMeFragment==null){
                    mMeFragment =new MeFragment_();
                    mTransaction.add(R.id.main_base_fragment, mMeFragment);
                }else{
                    mTransaction.show(mMeFragment);
                }

                break;

        }
        mTransaction.commit();
    }

    private void hideFragment(FragmentTransaction mTransaction) {

        if(mHomeFragment!=null){
            mTransaction.hide(mHomeFragment);
        }
        if(mZhiHuNewsFragment!=null){
            mTransaction.hide(mZhiHuNewsFragment);
        }
        if(mMeFragment!=null){
            mTransaction.hide(mMeFragment);
        }

    }
    protected void setIconAlpha_0(){
        hong_btn.setIconAlpha(0);
        new_btn.setIconAlpha(0);
        me_btn.setIconAlpha(0);
    }

    public DrawerLayout getDrawerLayout(){
        return drawerLayout;
    }
}
