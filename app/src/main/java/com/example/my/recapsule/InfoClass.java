package com.example.my.recapsule;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

public class InfoClass {
	int cnt;
	private String title;
	private String text;
	private String id;
	private String icon;
	private String num;
	private String addr;
	private String nick;
	private String profile;
	private String strPrivate;
	private int swcount;
	public int flimg;
	public String buttontext;
	int swimg;

	public InfoClass(int cnt,String title, String text, String id, String icon, String num, String addr, String nick, String profile,String strPrivate,int fcolor, String buttontext,int swcount,int swimg) {
		this.cnt = cnt;
		this.title = title;
		this.text = text;
		this.id = id;
		this.icon = icon;
		this.num = num;
		this.addr = addr;
		this.nick = nick;
		this.profile = profile;
		this.strPrivate=strPrivate;
		this.flimg = fcolor;
		this.buttontext = buttontext;
		this.swcount = swcount;
		this.swimg = swimg;
	}


	public int getSwimg() {
		return swimg;
	}

	public void setSwimg(int swimg) {
		this.swimg = swimg;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getSwcount() {
		return swcount;
	}

	public void setSwcount(int swcount) {
		this.swcount = this.swcount+swcount;
	}

	public int getFlimg() {
		return flimg;
	}

	public void setFlimg(int flimg) {
		this.flimg = flimg;
	}

	public String getButtontext() {
		return buttontext;
	}

	public void setButtontext(String buttontext) {
		this.buttontext = buttontext;
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getStrPrivate() {
		return strPrivate;
	}

	public void setStrPrivate(String strPrivate) {
		this.strPrivate = strPrivate;
	}
}
