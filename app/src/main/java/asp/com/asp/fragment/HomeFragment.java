package asp.com.asp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import asp.com.asp.MainActivity;
import asp.com.asp.R;
import asp.com.asp.adapter.HomePagerAdapter;
import asp.com.asp.adapter.MyFragmentPagerAdapter;

/**
 * Created by Administrator on 2016/5/12.
 */
@EFragment
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener {


    @ViewById(R.id.topbar)
    RelativeLayout topbar;
    @ViewById(R.id.topbar_back_btn)
    ImageView back_btn;
    @ViewById(R.id.topbar_search_btn)
    ImageView search_btn;
    @ViewById(R.id.fragment_home_VP)
    ViewPager home_viewPager;
    @ViewById(R.id.title_tv_rel)
    RelativeLayout title_tv_rel;
    @ViewById(R.id.title_tv_secondTv)
    TextView secondTv;
    @ViewById(R.id.top_bar_curcor_view)
    View slide_ling;

    DrawerLayout drawerLayout;
    private ImageView search_btn2;
    private View mRootview;

    private ArrayList<Fragment> fragments;
    private static boolean showMenuFlag = true;

    private Context context;
    private float screenWidth;//屏幕宽度
    private int tv_ll_width;
    private int layoutlWidth;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        initView();
        return mRootview;
    }

    private void initView() {
        drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        fragments = new ArrayList<Fragment>();
        fragments.add(QiangFragment.newInstance(0));
        fragments.add(QiangFragment2.newInstance(1));

        HomePagerAdapter pgAdapter = new HomePagerAdapter(getFragmentManager(), fragments);
        home_viewPager.setAdapter(pgAdapter);
        home_viewPager.addOnPageChangeListener(this);

        initSlideView();
        initMeasureView();
    }


    /**
     *  初始化 滑动条的
     */
    private void initSlideView() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        slide_ling.setTranslationX(0);
    }

    /**
     * 测量 标题 的 宽，并设置 滑动条的宽
     */
    private void initMeasureView() {
        ViewTreeObserver vto = title_tv_rel.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                tv_ll_width = title_tv_rel.getMeasuredWidth();
                layoutlWidth = (int) (screenWidth / tv_ll_width * 2);
                slide_ling.getLayoutParams().width = tv_ll_width / 2;
                title_tv_rel.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    @Click(R.id.topbar_back_btn)
    void backBtn() {
        if (showMenuFlag) {

            drawerLayout.openDrawer(Gravity.LEFT);
            showMenuFlag = false;
        } else {
            drawerLayout.closeDrawers();
            showMenuFlag = true;
        }
    }

    @Click(R.id.topbar_search_btn)
    void searchBtn(View clickedView) {
        Snackbar.make(clickedView, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .setAction("Action", null).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffsetPixels > 1)
            slide_ling.setTranslationX(positionOffsetPixels / layoutlWidth);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
