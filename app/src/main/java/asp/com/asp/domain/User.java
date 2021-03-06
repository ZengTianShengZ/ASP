package asp.com.asp.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 
 * User��JavaBean
 * @author MZone
 *
 */
public class User extends BmobUser implements Serializable{

	private QiangItem mQiangItem;

	private String nickname;
	private String signature;
	private String details;
	private BmobFile avatar;
	private BmobRelation favorite;
	private BmobRelation focus;
	private String sex;
	private String location;
	private boolean addV;

	
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BmobRelation getFavorite() {
		return favorite;
	}

	public void setFavorite(BmobRelation favorite) {
		this.favorite = favorite;
	}

	public BmobRelation getFocus() {
		return this.focus;
	}

	public void setFocus(BmobRelation focus) {
		this.focus = focus;
	}

	public BmobFile getAvatar() {
		return avatar;
	}

	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}


	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}


	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public QiangItem getmQiangItem() {
		return mQiangItem;
	}

	public void setmQiangItem(QiangItem mQiangItem) {
		this.mQiangItem = mQiangItem;
	}

	public boolean isAddV() {
		return addV;
	}

	public void setAddV(boolean addV) {
		this.addV = addV;
	}
}
