package asp.com.asp.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import asp.com.asp.R;
import asp.com.asp.view.SnackbarUtil;

@EActivity(R.layout.activity_search_view)
public class SearchViewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @ViewById(R.id.search_searchView)
    SearchView searchView;
    @ViewById(R.id.search_back_btn)
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void init() {

        initView();

    }

    private void initView() {
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查找商品名");
        SearchView.SearchAutoComplete textView = ( SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("onQueryTextSubmit","........query................"+query);

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        SnackbarUtil.GreenSnackbar(getApplicationContext(),back_btn,"       没找到结果！！！");

                    }
                });
            };

        }.start();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * 按推出按钮
     */
    @Click(R.id.search_back_btn)
    void backBtnClick(){

        finish();
    }
}
