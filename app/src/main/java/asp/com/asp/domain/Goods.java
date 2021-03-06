package asp.com.asp.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Goods extends BmobObject implements Serializable {
	/**
	 * @author Mzone
	 */
	private String name;
	private String category;
	private float price;
	private int count;
	private String details;
	private String cellphone;

	private QiangItem qiang;
	private QiangItemDg DgQiang;
	
	public String getCellphone() {
		return this.cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getDetails() {
		return this.details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return this.category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public float getPrice() {
		return this.price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getCount() {
		return this.count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public QiangItem getQiang() {
		return qiang;
	}

	public void setQiang(QiangItem qiang) {
		this.qiang = qiang;
	}

	public QiangItemDg getDgQiang() {
		return DgQiang;
	}

	public void setDgQiang(QiangItemDg dgQiang) {
		DgQiang = dgQiang;
	}
}
