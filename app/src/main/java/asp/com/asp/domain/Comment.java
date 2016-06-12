package asp.com.asp.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 评论
 */
public class Comment extends BmobObject implements Serializable{
	
	public static final String TAG = "Comment";

	private User user;
	private QiangItem qiang;
	private QiangItemDg dgqiang;
	private String replyTo;
	private String commentContent;

	public String getReplyTo() {
		return this.replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public QiangItem getQiang() {
		return this.qiang;
	}
	public void setQiang(QiangItem qiang) {
		this.qiang = qiang;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public QiangItemDg getQiangDg() {
		return dgqiang;
	}

	public void setQiangDg(QiangItemDg qiangDg) {
		this.dgqiang = qiangDg;
	}
}
