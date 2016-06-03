package asp.com.asp.zhihu;

import asp.com.asp.domain.ZhihuDaily;
import asp.com.asp.domain.ZhihuStory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/3.
 */
public interface ZhihuApi {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);

/*    @GET("http://lab.zuimeia.com/wallpaper/category/1/?page_size=1")
    Observable<ImageResponse> getImage();

    @GET("http://caiyao.name/releases/MrUpdate.json")
    Observable<UpdateItem> getUpdateInfo();*/
}