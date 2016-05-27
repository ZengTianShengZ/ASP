package asp.com.asp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import asp.com.asp.R;
import asp.com.asp.adapterPop.ZssMyAdapter;
import asp.com.asp.domain.EventBusBean;
import asp.com.asp.domain.Goods;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.User;
import asp.com.asp.utils.CacheUtils;
import asp.com.asp.utils.DialogUtils;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.view.SnackbarUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/15.
 */
@EActivity(R.layout.activity_edit_qiang)
public class EditQiangActivity extends Activity {


    @ViewById(R.id.common_top_bar_back_btn)
    ImageView back_btn;
    @ViewById(R.id.common_top_bar_right_titleTv)
    TextView right_titleTv;

    @ViewById(R.id.edit_qiang_addImageview)
    ImageView add_imageview;
    @ViewById(R.id.edit_activity_gridView)
    GridView gridView;

    @ViewById(R.id.edit_content)
    EditText qiang_content;
    @ViewById(R.id.goods_name_edit)
    EditText goods_name_edit;
    @ViewById(R.id.goods_category_edit)
    EditText goods_category_edit;
    @ViewById(R.id.goods_price_edit)
    EditText goods_price_edit;
    @ViewById(R.id.sailer_phone_edit)
    EditText sailer_phone_edit;

    private ProgressDialog mProgressDialog;
    private DialogUtils dlg_view;
    private AlertDialog dlg_back_alert;

    private ZssMyAdapter zssMyAadapter;

    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    private String[] targeturls=null;
    private String dateTime;

    public static ArrayList<String>  imgItem = new ArrayList<String>();
    public static ArrayList<String>  imgDirPath = new ArrayList<String>();
    public static ArrayList<BmobFile>  BmobFileList = new ArrayList<BmobFile>();
    private ArrayList<String> sourcepathlist;
    private Goods goods;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        initData();
        initEvent();
        return super.onCreateView(parent, name, context, attrs);
    }


    private void initData() {
    }

    private void initEvent() {
    }

    /**
     * 按推出按钮
     */
    @Click(R.id.common_top_bar_back_btn)
    void backBtnClick(){

        backDialogCreat();
    }

    /**
     * 按物理推出按钮
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backDialogCreat();
    }

    @Click(R.id.common_top_bar_right_titleTv)
    void rightTitleTvClick(View clickedView){

        String commitContent = qiang_content.getText().toString().trim();
        String goods_name = goods_name_edit.getText().toString().trim();
        String goods_category = goods_category_edit.getText().toString().trim();
        String goods_price = goods_price_edit.getText().toString().trim();
        String sailer_phone = sailer_phone_edit.getText().toString().trim();


        if (TextUtils.isEmpty(goods_name)) {

            SnackbarUtil.LongSnackbar(clickedView,"商品名不能为空",
                    getResources().getColor(R.color.colorWhite),
                    getResources().getColor(R.color.colorPrimaryDark)).show();

            return;
        }
        if (TextUtils.isEmpty(goods_category)) {

            SnackbarUtil.LongSnackbar(clickedView,"商品种类不能为空",
                    getResources().getColor(R.color.colorWhite),
                    getResources().getColor(R.color.colorPrimaryDark)).show();
            return;
        }
        if (TextUtils.isEmpty(goods_price)) {

            SnackbarUtil.LongSnackbar(clickedView,"商品价格不能为空",
                    getResources().getColor(R.color.colorWhite),
                    getResources().getColor(R.color.colorPrimaryDark)).show();
            return;
        }
        if (TextUtils.isEmpty(commitContent)) {

            SnackbarUtil.LongSnackbar(clickedView,"内容不能为空",
                    getResources().getColor(R.color.colorWhite),
                    getResources().getColor(R.color.colorPrimaryDark)).show();
            return;
        }
        if (TextUtils.isEmpty(sailer_phone)) {

            SnackbarUtil.LongSnackbar(clickedView,"联系电话不能为空",
                    getResources().getColor(R.color.colorWhite),
                    getResources().getColor(R.color.colorPrimaryDark)).show();
            return;
        }

        goods = new Goods();
        goods.setCategory(goods_category);
        goods.setName(goods_name);
        goods.setPrice(Float.valueOf(goods_price));
        goods.setDetails(commitContent);
        goods.setCellphone(sailer_phone);
   /*
        if (sourcepathlist == null) {

            publishWithoutFigure(commitContent,null, goods);
        }
        else {
            publish(commitContent,goods);
        }*/
        mProgressDialog = ProgressDialog.show(this, null, "正在上传...");
        OperationBmobDataUtil.getInstance().initData(getApplicationContext());
        OperationBmobDataUtil.getInstance().sendGoodData(finishHandle,commitContent,sourcepathlist,goods);
       // publish(commitContent,goods);
    }
    @Click(R.id.edit_qiang_addImageview)
    void addImageviewBtnClick(){
        Intent intent  = new Intent(getApplicationContext(),ShowImageActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
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
                    sourcepathlist =new ArrayList<String>();
                    for(int i=0;i<imgItem.size();i++){
                        sourcepathlist.add(imgDirPath.get(i)+"/"+imgItem.get(i));
                    }
                    if(sourcepathlist != null){
                        gridView.setVisibility(View.VISIBLE);
                        zssMyAadapter = new ZssMyAdapter(getApplicationContext(),sourcepathlist,
                                R.layout.zss_show_image);
                        gridView.setAdapter(zssMyAadapter);
                    }else{
                        gridView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    /**
     * 更新刷新 数据
     */
    private Handler finishHandle = new Handler() {
        public void handleMessage(Message msg) {

            if(msg.what!=0 ){
                // 使用 开源EventBus 发布 成功消息
                EventBus.getDefault().post(new EventBusBean("postSucceed"));
                setResult(RESULT_OK);
                finish();
            }else{

                Toast.makeText(getApplication(),"发布失败",Toast.LENGTH_LONG).show();
            }

            mProgressDialog.dismiss();
        }
    };

    private void backDialogCreat(){
        dlg_view = new DialogUtils.Builder().titleStr("确定推出编辑")
                .leftBtnStr("取消").rightBtnStr("确定").setEditVisible(View.GONE).build();
        dlg_back_alert = new AlertDialog.Builder(EditQiangActivity.this).setView(dlg_view.getDialogView(this)).show();

        dlg_view.leftButtonClickListener(new DialogUtils.DialogLeftButtonClick(){

            @Override
            public void dialogLeftButtonClickListener() {
                dlg_back_alert.dismiss();
            }
        });
        dlg_view.rightButtonClickListener(new DialogUtils.DialogRightButtonClick(){

            @Override
            public void dialogRightButtonClickListener(String editStr) {
                finish();
            }
        } );
    }

}
