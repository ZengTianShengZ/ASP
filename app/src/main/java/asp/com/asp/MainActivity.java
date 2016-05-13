package asp.com.asp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import asp.com.appbase.view.BottomTagView;
import asp.com.asp.fragment.HomeFragment;
import asp.com.asp.fragment.HomeFragment_;
import asp.com.asp.fragment.MeFragment;
import asp.com.asp.fragment.MeFragment_;
import asp.com.asp.fragment.NewsFragment;
import asp.com.asp.fragment.NewsFragment_;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/5/11.
 */
@EActivity(R.layout.activity_main2)
public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private FragmentManager mFragmentManager ;
    private FragmentTransaction mTransaction;

    private HomeFragment_ mHomeFragment;
    private NewsFragment_ mNewsFragment;
    private MeFragment_ mMeFragment;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.main_base_hong_btn)
    BottomTagView hong_btn;
    @ViewById(R.id.main_base_new_btn)
    BottomTagView new_btn;
    @ViewById(R.id.main_base_me_btn)
    BottomTagView me_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 使用 @EActivity 控件的注册事件要放在 onStart（），不然会抛空指针
        navigationView.setNavigationItemSelectedListener(this);
        hong_btn.setIconAlpha(1);


    }
    @Override
    protected void onResume() {
        super.onResume();
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
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
                if(mNewsFragment==null){
                    mNewsFragment =new NewsFragment_();

                    mTransaction.add(R.id.main_base_fragment, mNewsFragment);
                }else{
                    mTransaction.show(mNewsFragment);
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
        if(mNewsFragment!=null){
            mTransaction.hide(mNewsFragment);
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
