package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Attendance;
import com.ischoolbar.programmer.model.CurrentAttend;
import com.ischoolbar.programmer.model.SelectedCourse;
/*
 * 待签到信息操作
 * 
 * 
 */
public class CurrentAttendDAO extends BaseDao {

	/*
	 * 添加待签到课程信息
	 * @param currentattend
	 * @return
	*/
	public boolean addAttendance(CurrentAttend currentAttend ){
		//测试state
		String sql = "insert into c_attendence values(null,"+currentAttend.getCourseId()+","+currentAttend.getValidation()+",'"+currentAttend.getLimit_date()+"','"+currentAttend.getDate()+"','"+currentAttend.getState()+"')";
		return update(sql);
	}
	/*
	 * 判断是否已添加待签到课程信息
	 * @param courseId 
	 * @param date
	 * @return
	 */
	public boolean isadded(int courseId,String date){
		boolean ret = false;
		String sql = "select * from c_attendence where  course_id = " + courseId +" and date = '" + date + "'and state = 'true'";
		ResultSet query = query(sql);
		try {
			if(query.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 获取一条待签到课程数据
	 * @param id
	 * @return
	 */
    public CurrentAttend getCurrentAttend(int courseId,String date) {
    	CurrentAttend a = new CurrentAttend();
		String sql = "select * from c_attendence where  course_id = " + courseId +" and date = '" + date + "' and state = 'true'";
		ResultSet query = query(sql);
		try {
			if(query.next()){
				a.setId(query.getInt("id"));
				a.setCourseId(query.getInt("course_id"));
				a.setValidation(query.getString("validation"));
				a.setLimit_date(query.getString("limit_date"));
				a.setDate(query.getString("date"));
				a.setState(query.getString("state"));
				return a;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

    }
    /**
     * 
     *TODO 获取可签到课程列表
     * @param @param courseId
     * @param @param date
     * @param @return
     * @return List<CurrentAttend>
     *
     */
    public List<CurrentAttend> getCurrentAttendList() {
    	List<CurrentAttend> ret = new ArrayList<CurrentAttend>();
		String sql = "select * from c_attendence where state = 'true'";
		ResultSet query = query(sql);
		try {
			while(query.next()){
				CurrentAttend a = new CurrentAttend();
				a.setId(query.getInt("id"));
				a.setCourseId(query.getInt("course_id"));
				a.setValidation(query.getString("validation"));
				a.setLimit_date(query.getString("limit_date"));
				a.setDate(query.getString("date"));
				a.setState(query.getString("state"));
				ret.add(a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return ret;
    }
	/*
	 * 更新待签到课程信息
	 * @param state  待签到课程状态
	 * @return
	 */
	public boolean modify(int id, String state) {
		String sql = "update c_attendence set state = '"+state+"'"+" where id = " + id;
		return update(sql);
	}
}
