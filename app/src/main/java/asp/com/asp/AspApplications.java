package asp.com.asp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import asp.com.asp.domain.User;
import asp.com.asp.weibo.AccessTokenKeeper;
import cn.bmob.push.config.Constant;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/13.
 */
public  class AspApplications extends Application {


    private static AspApplications mAspApplications;

    private static String BMOB_APP_ID = "cb23185f8c0196d590fe317d74689f34";
    /**
     * 判断当前是否有 user 登陆
     */
    public static boolean GET_User = false;

    public static  boolean Send_ER_Goods = false;
    public static  boolean Send_DG_Goods = false;

    @Override
    public void onCreate() {
        // TODO 自动生成的方法存根

        Bmob.initialize(getApplicationContext(),BMOB_APP_ID);
        Fresco.initialize(this);

        initUser();

        super.onCreate();

    }

    private void initUser() {
       /* Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getApplicationContext());
        if(token.getUid()!= null){
            GET_User = true;
        }*/
        User bmobUser = BmobUser.getCurrentUser(this, User.class);
        if(bmobUser != null){
            // 允许用户使用应用
            GET_User = true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            GET_User = false;
        }

    }
    public static AspApplications getInstance() {
        return mAspApplications;
    }
    public void initFresco(){
//        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
//        GenericDraweeHierarchy hierarchy = builder
//                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
//                .setPlaceholderImage(getResources().getDrawable(R.drawable.image1), ScalingUtils.ScaleType.FIT_CENTER)
//                .setFailureImage(getResources().getDrawable(R.drawable.image1), ScalingUtils.ScaleType.FIT_CENTER)
//                .build();
//        mSimpleDraweeView.setHierarchy(hierarchy);

    }
}
