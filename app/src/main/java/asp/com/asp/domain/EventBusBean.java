package asp.com.asp.domain;

/**
 * Created by Administrator on 2016/5/26.
 */
public class EventBusBean {

    private String mMsg;
    public EventBusBean(String msg) {
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }


}
