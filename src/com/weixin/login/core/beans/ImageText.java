package com.weixin.login.core.beans;

import java.io.Serializable;

public class ImageText implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String title; // 标题
	
	private String content; // 内容
	
	private String digest; // 摘要
	
	private String author; // 作者
	
	private String fileid; // 文件（图片）id
	
	private String show_cover_pic; // 是否显示封面图片  1 or 0
	
	private String sourceurl;// 跳转的地址

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getShow_cover_pic() {
		return show_cover_pic;
	}

	public void setShow_cover_pic(String show_cover_pic) {
		this.show_cover_pic = show_cover_pic;
	}

	public String getSourceurl() {
		return sourceurl;
	}

	public void setSourceurl(String sourceurl) {
		this.sourceurl = sourceurl;
	}
}
