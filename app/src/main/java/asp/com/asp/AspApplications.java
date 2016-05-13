package asp.com.asp;

import android.app.Application;

import cn.bmob.push.config.Constant;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/5/13.
 */
public class AspApplications extends Application {

    private static String BMOB_APP_ID = "cb23185f8c0196d590fe317d74689f34";
    @Override
    public void onCreate() {
        // TODO 自动生成的方法存根

        Bmob.initialize(getApplicationContext(),BMOB_APP_ID);

        super.onCreate();

    }
}
