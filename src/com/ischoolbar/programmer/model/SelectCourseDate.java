package com.ischoolbar.programmer.model;

import java.sql.Date;
/**
 * ѡ��ʱ����
 * @author Yan
 *
 */
public class SelectCourseDate {
	private int id;
	Date startDate;
	Date endDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
