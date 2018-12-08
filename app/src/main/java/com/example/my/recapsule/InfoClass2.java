package com.example.my.recapsule;

import android.graphics.Bitmap;

public class InfoClass2 {
	private String title;
	private String text;
	private String id;
	private String icon;
	private String num;
	private String gps;
	private int date;
	private String deadlind;
	public InfoClass2(String title, String text, String id, String icon, String num,String gps,int date,String deadline) {
		this.title = title;
		this.text = text;
		this.id = id;
		this.icon = icon;
		this.num = num;
		this.gps=gps;
		this.date=date;
		this.deadlind=deadline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getDeadlind() {
		return deadlind;
	}

	public void setDeadlind(String deadlind) {
		this.deadlind = deadlind;
	}
}
