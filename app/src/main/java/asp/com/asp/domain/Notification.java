package asp.com.asp.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/6/12.
 */
public class Notification extends BmobObject {

    private User user;
    private String notification;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
