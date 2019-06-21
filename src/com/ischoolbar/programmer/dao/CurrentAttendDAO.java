package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Attendance;
import com.ischoolbar.programmer.model.CurrentAttend;
import com.ischoolbar.programmer.model.SelectedCourse;
/*
 * ��ǩ����Ϣ����
 * 
 * 
 */
public class CurrentAttendDAO extends BaseDao {

	/*
	 * ��Ӵ�ǩ���γ���Ϣ
	 * @param currentattend
	 * @return
	*/
	public boolean addAttendance(CurrentAttend currentAttend ){
		//����state
		String sql = "insert into c_attendence values(null,"+currentAttend.getCourseId()+","+currentAttend.getValidation()+",'"+currentAttend.getLimit_date()+"','"+currentAttend.getDate()+"','"+currentAttend.getState()+"')";
		return update(sql);
	}
	/*
	 * �ж��Ƿ�����Ӵ�ǩ���γ���Ϣ
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
	 * ��ȡһ����ǩ���γ�����
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
     *TODO ��ȡ��ǩ���γ��б�
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
	 * ���´�ǩ���γ���Ϣ
	 * @param state  ��ǩ���γ�״̬
	 * @return
	 */
	public boolean modify(int id, String state) {
		String sql = "update c_attendence set state = '"+state+"'"+" where id = " + id;
		return update(sql);
	}
}
