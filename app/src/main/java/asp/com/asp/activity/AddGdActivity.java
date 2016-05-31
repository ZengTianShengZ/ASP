package asp.com.asp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import asp.com.asp.R;
import asp.com.asp.adapterPop.ZssMyAdapter;
import asp.com.asp.domain.Goods;
import asp.com.asp.domain.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/5/31.
 */
@EActivity(R.layout.activity_add_dg)
public class AddGdActivity extends Activity {

    @ViewById(R.id.activity_add_dagridView)
    GridView gridView;
    @ViewById(R.id.activity_add_da_btn)
    Button add_da_btn;

    private ZssMyAdapter zssMyAadapter;

    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    private String[] targeturls=null;
    private String dateTime;

    public static ArrayList<String>  imgItem = new ArrayList<String>();
    public static ArrayList<String>  imgDirPath = new ArrayList<String>();
    public static ArrayList<BmobFile>  BmobFileList = new ArrayList<BmobFile>();
    private ArrayList<String> sourcepathlist;

    private boolean sendDgGoods = false;
    private Goods goods;
    private User bmobUser;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }


    @AfterViews
    void updateQiangDate() {

        initData();
       // initEvent();
    }

    private void initData() {

        bmobUser = BmobUser.getCurrentUser(this, User.class);

        sourcepathlist =new ArrayList<String>();
        sourcepathlist.add("First");
        zssMyAadapter = new ZssMyAdapter(getApplicationContext(),sourcepathlist,
                R.layout.zss_show_image,this);
        gridView.setAdapter(zssMyAadapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    imgItem = data.getStringArrayListExtra("imgItem");
                    imgDirPath = data.getStringArrayListExtra("imgDirPath");
                    //图片太大，先显示原图片，发送时压缩发送

                    for(int i=0;i<imgItem.size();i++){
                        sourcepathlist.add(imgDirPath.get(i)+"/"+imgItem.get(i));
                    }
                    zssMyAadapter.notifyDataSetChanged();

                    break;
            }
        }
    }
}
