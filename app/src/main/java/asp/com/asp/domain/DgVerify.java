package asp.com.asp.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 代购验证
 * Created by Administrator on 2016/5/31.
 */
public class DgVerify extends BmobObject implements Serializable {

    private User author;

    private BmobFile contentfigureurl;
    private BmobFile contentfigureurl1;
    private BmobFile contentfigureurl2;
    private BmobFile contentfigureurl3;
    private BmobFile contentfigureurl4;
    private BmobFile contentfigureurl5;
    private BmobFile contentfigureurl6;
    private BmobFile contentfigureurl7;
    private BmobFile contentfigureurl8;


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BmobFile getContentfigureurl8() {
        return contentfigureurl8;
    }

    public void setContentfigureurl8(BmobFile contentfigureurl8) {
        this.contentfigureurl8 = contentfigureurl8;
    }

    public BmobFile getContentfigureurl7() {
        return contentfigureurl7;
    }

    public void setContentfigureurl7(BmobFile contentfigureurl7) {
        this.contentfigureurl7 = contentfigureurl7;
    }

    public BmobFile getContentfigureurl5() {
        return contentfigureurl5;
    }

    public void setContentfigureurl5(BmobFile contentfigureurl5) {
        this.contentfigureurl5 = contentfigureurl5;
    }

    public BmobFile getContentfigureurl6() {
        return contentfigureurl6;
    }

    public void setContentfigureurl6(BmobFile contentfigureurl6) {
        this.contentfigureurl6 = contentfigureurl6;
    }

    public BmobFile getContentfigureurl4() {
        return contentfigureurl4;
    }

    public void setContentfigureurl4(BmobFile contentfigureurl4) {
        this.contentfigureurl4 = contentfigureurl4;
    }

    public BmobFile getContentfigureurl3() {
        return contentfigureurl3;
    }

    public void setContentfigureurl3(BmobFile contentfigureurl3) {
        this.contentfigureurl3 = contentfigureurl3;
    }

    public BmobFile getContentfigureurl2() {
        return contentfigureurl2;
    }

    public void setContentfigureurl2(BmobFile contentfigureurl2) {
        this.contentfigureurl2 = contentfigureurl2;
    }

    public BmobFile getContentfigureurl1() {
        return contentfigureurl1;
    }

    public void setContentfigureurl1(BmobFile contentfigureurl1) {
        this.contentfigureurl1 = contentfigureurl1;
    }

    public BmobFile getContentfigureurl() {
        return contentfigureurl;
    }

    public void setContentfigureurl(BmobFile contentfigureurl) {
        this.contentfigureurl = contentfigureurl;
    }
    public void setBmobFileList(BmobFile contentfigureurls,int item){
        switch (item) {
            case 0:
                setContentfigureurl(contentfigureurls);
                break;
            case 1:
                setContentfigureurl1(contentfigureurls);
                break;
            case 2:
                setContentfigureurl2(contentfigureurls);
                break;
            case 3:
                setContentfigureurl3(contentfigureurls);
                break;

            case 4:
                setContentfigureurl4(contentfigureurls);
                break;
            case 5:
                setContentfigureurl5(contentfigureurls);
                break;
            case 6:
                setContentfigureurl6(contentfigureurls);
                break;
            case 7:
                setContentfigureurl7(contentfigureurls);
                break;
            case 8:
                setContentfigureurl8(contentfigureurls);
                break;

            default:
                break;
        }
    }



}
