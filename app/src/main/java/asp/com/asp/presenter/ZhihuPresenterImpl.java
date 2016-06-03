package asp.com.asp.presenter;

import android.util.Log;

import java.util.ArrayList;

import asp.com.asp.domain.ZhihuDaily;
import asp.com.asp.domain.ZhihuDailyItem;
import asp.com.asp.zhihu.ZhihuRequest;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ZhihuPresenterImpl {

    public Observable<ZhihuDaily> getLastZhihuNews() {

       return ZhihuRequest.getZhihuApi().getLastDaily()
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()) {
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                });

        /*Subscription subscription = ZhihuRequest.getZhihuApi().getLastDaily()
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()) {
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {

                    }
                }); */

    }

    public Observable<ZhihuDaily> getTheDaily(String date) {

          return ZhihuRequest.getZhihuApi().getTheDaily(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        Log.i("subscription",".......subscription1......"+zhihuDaily.getStories().size());
                        Log.i("subscription",".......subscription2......"+zhihuDaily.getStories().get(0).getTitle());
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()) {
                            Log.i("subscription",".......subscription3......"+zhihuDailyItem.getTitle());
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                });
        /*Subscription subscription =  ZhihuRequest.getZhihuApi().getTheDaily(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        Log.i("subscription",".......subscription1......"+zhihuDaily.getStories().size());
                        Log.i("subscription",".......subscription2......"+zhihuDaily.getStories().get(0).getTitle());
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()) {
                            Log.i("subscription",".......subscription3......"+zhihuDailyItem.getTitle());
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                       if(mZhihuDailyImpl != null) {
                           mZhihuDailyImpl.RxZhihuNewsonCompleted();
                       }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if(mZhihuDailyImpl != null) {
                            mZhihuDailyImpl.RxZhihuonNewsError();
                        }
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        Log.i("subscription",".......subscription4......"+zhihuDaily.getStories().size());
                        if(mZhihuDailyImpl != null) {
                            Log.i("subscription",".......subscription555......"+zhihuDaily.getStories().size());
                            mZhihuDailyImpl.RxZhihuonNewsNext(zhihuDaily.getZhihuDailyItems());
                        }
                    }
                });*/

    }

    public interface ZhihuDailyImpl{
        public void RxZhihuonNewsNext(ArrayList<ZhihuDailyItem> getStories);
        public void RxZhihuonNewsError();
        public void RxZhihuNewsonCompleted();
    }
    private ZhihuDailyImpl mZhihuDailyImpl;
    public  void setZhihuDailyImpl(ZhihuDailyImpl mZhihuDailyImpl){
        this. mZhihuDailyImpl = mZhihuDailyImpl;
    }

}
