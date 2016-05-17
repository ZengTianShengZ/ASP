package asp.com.asp.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import asp.com.asp.R;

/**
 * Created by Administrator on 2016/5/16.
 */
public class SwipeRefreshFooterLoading  {

    private Context mContext;
    private  ListView mListView;
    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;
    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnSwipeLoadListener mOnLoadListener;


    public SwipeRefreshFooterLoading(Context context, ListView listView){
        this.mContext = context;
        this.mListView = listView;

        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.swipe_refresh_footer, null,
                false);

        initToucEvent();
    }

    private void initToucEvent() {

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 按下
                        mYDown = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // 移动
                        mLastY = (int) event.getRawY();
                        // 抬起
                        if (canLoad()) {
                            Log.i("MotionEvent", ".....llooo");
                            Toast.makeText(mContext,"hhhhhhh    "+(mYDown - mLastY),Toast.LENGTH_SHORT).show();

                            loadData();
                        }

                        break;

                    case MotionEvent.ACTION_UP:


                        break;
                }
                return false;
            }

        });
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onSwipeLoading();
        }

    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;
    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */

    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }
    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop = 30;
    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }
    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnSwipeLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnSwipeLoadListener {
        public void onSwipeLoading();
    }

}
