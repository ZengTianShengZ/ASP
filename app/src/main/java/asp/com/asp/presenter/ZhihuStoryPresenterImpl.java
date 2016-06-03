package asp.com.asp.presenter;

import asp.com.asp.domain.ZhihuStory;
import asp.com.asp.zhihu.ZhihuRequest;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ZhihuStoryPresenterImpl {

    public void getZhihuStory(String id) {
        Subscription s = ZhihuRequest.getZhihuApi().getZhihuStory(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuStory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhihuStory zhihuStory) {

                    }
                });

    }

}
