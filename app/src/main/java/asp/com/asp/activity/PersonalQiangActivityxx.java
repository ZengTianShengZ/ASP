package asp.com.asp.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import asp.com.asp.R;
import asp.com.asp.domain.User;
import asp.com.asp.fragment.PersonQiangFragmentDg;
import asp.com.asp.fragment.PersonalQiangFragment;
import asp.com.asp.utils.ConfigConstantUtil;

/**
 * Created by Administrator on 2016/5/19.
 */
@EActivity(R.layout.activity_personal_qiangxx)
public class PersonalQiangActivityxx extends FragmentActivity {

    @ViewById(R.id.personal_qaing_ershouTv)
    TextView ershouTv;
    @ViewById(R.id.personal_qaing_daigouTv)
    TextView daigouTv;
    @ViewById(R.id.include_person_info_nameTV)
    TextView info_nameTV;
    @ViewById(R.id.include_person_info_logo)
    SimpleDraweeView info_logo;
    @ViewById(R.id.include_person_info_parentLL)
    LinearLayout info_parentLL;


    private FragmentManager mFragmentManager ;
    private FragmentTransaction mTransaction;

    private PersonalQiangFragment mPersonalQiangFragment;
    private PersonQiangFragmentDg mPersonQiangFragmentDg;

    private User mUser;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        mUser =  (User)getIntent().getSerializableExtra(ConfigConstantUtil.intentDtat_Author);

    }
    public User getUser(){
        return  mUser;
    }
    @AfterViews
    void init() {

        initView();
        initData();
        initEvent();
    }
    private void initView() {

        // 显示头像
        if (mUser.getAvatar()!=null) {
            info_logo.setImageURI(Uri.parse(mUser.getAvatar().getFileUrl(mContext)));
        }
        //显示名字
        info_nameTV.setText(mUser.getNickname()+"");

        //显示第一个个人墙 ，二手墙
        mFragmentManager =getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        if(mPersonalQiangFragment==null){
            mPersonalQiangFragment =new PersonalQiangFragment();
            mTransaction.add(R.id.personal_qiang_FL, mPersonalQiangFragment);
        }else{
            mTransaction.show(mPersonalQiangFragment);
        }
        mTransaction.commit();
    }

    private void initData() {

    }

    private void initEvent() {

    }
    @Click(R.id.personal_qaing_ershouTv)
    void ershouTvClick(){
        Toast.makeText(getApplicationContext(),"00000",Toast.LENGTH_LONG).show();
        setSelected(0);
        info_parentLL.setVisibility(View.VISIBLE);
    }
    @Click(R.id.personal_qaing_daigouTv)
    void daigouTvClick(){
        Toast.makeText(getApplicationContext(),"1111111",Toast.LENGTH_LONG).show();
        setSelected(1);
        info_parentLL.setVisibility(View.GONE);
    }


    private void setSelected(int index) {

        mTransaction = mFragmentManager.beginTransaction();

        hideFragment(mTransaction);
        switch(index){
            case 0:
                if(mPersonalQiangFragment==null){
                    mPersonalQiangFragment =new PersonalQiangFragment();
                    mTransaction.add(R.id.personal_qiang_FL, mPersonalQiangFragment);
                }else{
                    mTransaction.show(mPersonalQiangFragment);
                }


                break;
            case 1:
                if(mPersonQiangFragmentDg==null){
                    mPersonQiangFragmentDg =new PersonQiangFragmentDg();

                    mTransaction.add(R.id.personal_qiang_FL, mPersonQiangFragmentDg);
                }else{
                    mTransaction.show(mPersonQiangFragmentDg);
                }

                break;

        }
        mTransaction.commit();
    }

    private void hideFragment(FragmentTransaction mTransaction) {

        if(mPersonalQiangFragment!=null){
            mTransaction.hide(mPersonalQiangFragment);
        }
        if(mPersonQiangFragmentDg!=null){
            mTransaction.hide(mPersonQiangFragmentDg);
        }

    }

}
