package com.ischoolbar.programmer.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.management.loading.PrivateClassLoader;

import com.ischoolbar.programmer.dao.CurrentAttendDAO;
import com.ischoolbar.programmer.dao.NoticeDao;
import com.ischoolbar.programmer.model.Notice;
/**
 * 
 * @author llq
 *���ݿ���util
 */
public class DbUtil {

	private String dbUrl = "jdbc:mysql://localhost:3306/student_manager?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
	private String dbUser = "sa";
	private String dbPassword = "123456";
	private String jdbcName = "com.mysql.jdbc.Driver";
	private Connection connection = null;
	public Connection getConnection(){
		try {
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			System.out.println("���ݿ����ӳɹ���");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeCon(){
		if(connection != null)
			try {
				connection.close();
				System.out.println("���ݿ������ѹرգ�");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbUtil dbUtil = new DbUtil();
		dbUtil.getConnection();
		NoticeDao noticeDao=new NoticeDao();
		ArrayList<Notice> notices=null;
		notices=noticeDao.findNotice();
		CurrentAttendDAO currentAttendDAO=new CurrentAttendDAO();
		System.out.println(notices.get(0).getTitle());
		System.out.println(currentAttendDAO.getCurrentAttend(2,DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd")));
	}

}
