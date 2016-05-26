package asp.com.asp.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import asp.com.asp.domain.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BmobUserUtil {

    private static boolean loginSuccess = false;

    public static boolean BmobUserLogin(final Context context, final String userName,final  String password){
        User bu2 =   new User();
        bu2.setNickname(userName);
        bu2.setUsername(userName);
        bu2.setPassword(password);
        bu2.login(context, new SaveListener() {
            @Override
            public void onSuccess() {

                //通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
                loginSuccess = true;
            }
            @Override
            public void onFailure(int code, String msg) {
                Log.i("onFailure",code+".........................."+msg);
                BmobSignUp(context,userName,password);
            }
        });
        return loginSuccess;
    }

    private static void BmobSignUp(Context context,String userName,String password){
        User bu  =   new User();
        bu.setNickname(userName);
        bu.setUsername(userName);
        bu.setPassword(password);

        //注意：不能用save方法进行注册
        bu.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                loginSuccess = true;
                //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
            }
            @Override
            public void onFailure(int code, String msg) {
                Log.i("onFailure",code+"....=.....=...=..=...=..=...=...."+msg);
                // TODO Auto-generated method stub
                loginSuccess = false;
            }
        });
    }

    public static boolean uploadblockLogo(final Context context,String picPath){
        Log.i("uploadblockLogo", "././.11111111111111111././././"+picPath);
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                User bmobUser = BmobUser.getCurrentUser(context, User.class);
                bmobUser.setAvatar(bmobFile);
                bmobUser.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        loginSuccess = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        loginSuccess = false;
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
}
