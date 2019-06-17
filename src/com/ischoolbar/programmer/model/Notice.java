package com.ischoolbar.programmer.model;

import java.sql.Date;

public class Notice {
  private int nid;
  private String title;
  private String content;
  private String author;
  Date nDate;
  public int getNid() {
	return nid;
}
  public void setNid(int nid) {
	this.nid = nid;
}
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

  public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public Date getnDate() {
	return nDate;
}
  public void setnDate(Date nDate) {
	this.nDate = nDate;
}
}
