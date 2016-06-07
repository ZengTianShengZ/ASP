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

                        if(mZhihuStoryImpl!=null){
                            mZhihuStoryImpl.RxZhihuStoryCompleted();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mZhihuStoryImpl!=null){
                            mZhihuStoryImpl.RxZhihuStoryError();
                        }
                    }

                    @Override
                    public void onNext(ZhihuStory zhihuStory) {
                        if(mZhihuStoryImpl!=null){
                            mZhihuStoryImpl.RxZhihuStoryNext(zhihuStory);
                        }
                    }
                });

    }
    public interface ZhihuStoryImpl{
        public void RxZhihuStoryNext(ZhihuStory zhihuStory);
        public void RxZhihuStoryError();
        public void RxZhihuStoryCompleted();
    }
    private ZhihuStoryImpl mZhihuStoryImpl;
    public  void setZhihuStoryImpl(ZhihuStoryImpl mZhihuStoryImpl){
        this.mZhihuStoryImpl = mZhihuStoryImpl;
    }
}
