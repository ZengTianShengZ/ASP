package asp.com.asp.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;

import asp.com.asp.domain.Notification;
import asp.com.asp.domain.User;
import asp.com.asp.view.SnackbarUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BmobUserUtil {

    private static boolean loginSuccess = false;
    public static String Bmob_Wb_ID = null;

    public static boolean BmobUserLogin(final Context context, final String userName,final  String password){

        loginSuccess = false;

        BmobUser bu2 = new BmobUser();
       // bu2.setNickname(userName);
        bu2.setUsername(userName);
        bu2.setPassword(password);
        bu2.login(context, new SaveListener() {
            @Override
            public void onSuccess() {

                //通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息


                Notification notification = new Notification();
                notification.setUser((User)BmobUser.getCurrentUser(context));
                notification.setNotification("登陆成功，感谢您的加入！！！");
                notification.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        loginSuccess = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        loginSuccess = false;
                        Log.i("BmobUserLogin",i+".........Notificationd ..fail................."+s);
                    }
                });


                Log.i("BmobUserLogin", "............loginSuccess.............."+loginSuccess);
            }
            @Override
            public void onFailure(int code, String msg) {
                Log.i("BmobUserLogin",code+"...........Login//fff..............."+msg);
                BmobSignUp(context,userName,password);
            }
        });
        return loginSuccess;
    }

    public static void BmobSignUp(final Context context,String userName,String password){
        User bu  =   new User();
        bu.setNickname(userName);
        bu.setUsername(userName);
        bu.setPassword(password);

        //注意：不能用save方法进行注册
        bu.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Notification notification = new Notification();
                notification.setUser((User)BmobUser.getCurrentUser(context));
                notification.setNotification("感谢您的加入！！！");
                notification.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        loginSuccess = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        loginSuccess = false;
                    }
                });
                //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
                Log.i("BmobUserLogin","....=.....=...=loginSuccess..=...=..=...=...."+loginSuccess);
            }
            @Override
            public void onFailure(int code, String msg) {
                Log.i("BmobUserLogin",code+"....=.....=...=..=...=..=...=...."+msg);
                // TODO Auto-generated method stub
                loginSuccess = false;
            }
        });

    }

    public static boolean uploadblockLogo(final Context context, final String nickname,final String id_password, String picPath){
        Log.i("uploadblockLogo",nickname+ ".....uploadblockLogo.........."+picPath);

        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {

            @Override
            public void onSuccess() {

                Log.i("uploadblockLogo", ".....onSuccess.........."+loginSuccess);

                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                User bmobUser = BmobUser.getCurrentUser(context, User.class);
                bmobUser.setNickname(nickname+"");
                bmobUser.setPassword(id_password+"");
                bmobUser.setAvatar(bmobFile);
                bmobUser.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        loginSuccess = true;
                        Log.i("uploadblockLogo", ".....loginSuccess.........."+loginSuccess);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        loginSuccess = false;
                        Log.i("uploadblockLogo",i+ ".....bmobUser.update.........."+s);
                    }
                });

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                loginSuccess = false;
                Log.i("uploadblockLogo",code+ "././././/./333333333333333././././"+msg);
            }
        });
        return  loginSuccess ;
    }

    /**
     * bmob 使用第三方登录接口
     * @param context
     * @param accessToken
     * @param expiresIn
     * @param userId
     */
    public static String bmobWbLoggin(final Context context, String accessToken, String expiresIn, String userId){
        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("weibo",accessToken, expiresIn,userId);
        BmobUser.loginWithAuthData(context, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                Log.i("bmobWbLoggin","..........bmobWbLoggin......"+userAuth.toString());
                BmobUser bmobUser = BmobUser.getCurrentUser(context);
                Bmob_Wb_ID = bmobUser.getUsername();
                Log.i("bmobWbLoggin","..........bmobWbLoggin......"+bmobUser.getUsername());
//bmobWbLoggin: ..........bmobWbLoggin......{"weibo":{"uid":"2876232957","expires_in":1465844401353,"access_token":"2.00Ja4eIDjAHrDC3fc5a0956dYP1ecD"}}
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i("smile","第三方登陆失败："+msg);
                Bmob_Wb_ID = null;
            }

        });
        return  Bmob_Wb_ID;
    }


}
