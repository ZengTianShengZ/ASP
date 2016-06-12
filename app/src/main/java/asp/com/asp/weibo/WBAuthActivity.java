package asp.com.asp.weibo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import asp.com.asp.R;
import asp.com.asp.domain.User;
import asp.com.asp.utils.BmobUserUtil;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.ImageLoaderUtil;
import asp.com.asp.utils.SharedPreferencesUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/18.
 */
@EActivity(R.layout.activity_wb_auth)
public class WBAuthActivity extends Activity {

  /*  @ViewById(R.id.wb_toolbar)
    Toolbar wb_toolbar;*/

    @ViewById(R.id.wb_auth_btn)
    Button wb_auth_btn;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    private ImageLoaderUtil mImageLoaderUtil;
    private SharedPreferencesUtil mSharedPreferencesUtil;

    private String ImgUrl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @AfterViews
    void updateQiangDate() {

      /*  setSupportActionBar(wb_toolbar);
        wb_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/


        mImageLoaderUtil = ImageLoaderUtil.getInstance();
        mImageLoaderUtil.initData(getApplicationContext());
        mSharedPreferencesUtil =SharedPreferencesUtil.getInstance(getApplicationContext(),getPackageName());
        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);


        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        Toast.makeText(WBAuthActivity.this,mAccessToken.getUid(), Toast.LENGTH_LONG).show();

        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }


    }
    @Click(R.id.wb_auth_btn)
    void wb_auth_btnClick(){
        mSsoHandler.authorizeClientSso(new AuthListener());
    }


    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
                Toast.makeText(WBAuthActivity.this,
                        //授权成功
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                //授权失败
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(WBAuthActivity.this, message, Toast.LENGTH_LONG).show();
            }
            UsersAPI usersAPI = new UsersAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);
            usersAPI.show(Long.valueOf(mAccessToken.getUid()), new SinaRequestListener());

        }

        @Override
        public void onCancel() {
            Toast.makeText(WBAuthActivity.this,
                    //取消授权
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBAuthActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }
    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        //Token：%1$s \n有效期：%2$s
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
       // mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            //Token 仍在有效期内，无需再次登录。
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        //mTokenText.setText(message);
    }

    class SinaRequestListener implements RequestListener { //新浪微博请求接口

        @Override
        public void onComplete(String response) {
            // TODO Auto-generated method stub
            try {

                JSONObject jsonObject = new JSONObject(response);
                final String idStr = jsonObject.getString("idstr");// 唯一标识符(uid)
                final String name = jsonObject.getString("name");// 姓名
                final String avatarHd = jsonObject.getString("avatar_hd");// 头像

                Log.i("loginSuccess",name+".......///////........."+idStr+"/////"+avatarHd);
                if(name != null) {

                    new Thread() {
                        public void run() {
                            boolean loginSuccess = BmobUserUtil.BmobUserLogin(getApplicationContext(), name, idStr);
                            Log.i("loginSuccess", ".... sssssssssssssss........"+loginSuccess);
                            if(loginSuccess) {
                                Bitmap bitmap = mImageLoaderUtil.getNetWorkBitmap(avatarHd);
                                ImgUrl = mImageLoaderUtil.saveToSdCard(bitmap);
                                boolean upSuccess = BmobUserUtil.uploadblockLogo(getApplicationContext(),ImgUrl);
                                if(upSuccess){
                                    SharedPreferences.Editor editor =  mSharedPreferencesUtil.getEditor();
                                    editor.putString(ConfigConstantUtil.UserName,name);
                                    editor.putString(ConfigConstantUtil.UserLogStr,ImgUrl);
                                    editor.putString(ConfigConstantUtil.UserPassword,idStr);
                                    editor.commit();
                                    Log.i("uploadblockLogo",name+ "././././/./4444444444444././././"+ImgUrl);
                                }
                            }

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                }
                            });
                        };

                    }.start();

                }
            }  catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 05-24 17:34:51.231 5046-5046/asp.com.asp W/ViewRootImpl: mView has no focus, use com.android.internal.policy.impl.PhoneWindow$DecorView{42d84168 V.E..... R.....ID 0,0-1080,1800} instead
         05-24 17:34:51.586 5046-5046/asp.com.asp I/loginSuccess: 曾田生..........................2876232957/////http://tva4.sinaimg.cn/crop.85.16.180.180.1024/ab6fd4fdjw1e9e5obce5cj208c05h74f.jpg
         05-24 17:34:51.741 5046-5131/asp.com.asp I/System.out: discardStream skip 0 bytes
         05-24 17:34:51.751 5046-5046/asp.com.asp I/onFailure: 109..........................login data required.
         05-24 17:34:51.846 5046-5133/asp.com.asp I/System.out: discardStream skip 0 bytes
         05-24 17:34:51.851 5046-5046/asp.com.asp I/onFailure: 304..........................username or password is null.
         * @param e
         */
        @Override
        public void onWeiboException(WeiboException e) {

        }
    }

}
