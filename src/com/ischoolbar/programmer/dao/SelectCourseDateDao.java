package com.ischoolbar.programmer.dao;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.ischoolbar.programmer.model.SelectCourseDate;
import com.ischoolbar.programmer.model.SelectedCourse;
import com.sun.org.apache.regexp.internal.recompile;

public class SelectCourseDateDao extends BaseDao {
	/**
	 * 获取选课时间
	 * 
	 * @author Yan
	 *
	 */
	public SelectCourseDate getSelectCourseDate() {
		SelectCourseDate ret = new SelectCourseDate();
		String sql = "select * from s_select_course_date order by id DESC LIMIT 1";
		ResultSet resultSet = query(sql);
		try {	
			if(resultSet.next()) {
			ret.setId(resultSet.getInt("id"));
			ret.setStartDate(resultSet.getDate("startdate"));
			ret.setEndDate(resultSet.getDate("enddate"));
			return ret;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public boolean editSelectCourseDate(String startdate ,String  enddate) {
		System.out.println(startdate);
		String sql="insert into s_select_course_date values(null,'"+startdate+"','"+enddate+"')";		
		return update(sql);
	} 
}
