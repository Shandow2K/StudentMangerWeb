package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ischoolbar.programmer.dao.AttendanceDao;
import com.ischoolbar.programmer.dao.CourseDao;
import com.ischoolbar.programmer.dao.CurrentAttendDAO;
import com.ischoolbar.programmer.dao.SelectedCourseDao;
import com.ischoolbar.programmer.model.Attendance;
import com.ischoolbar.programmer.model.Course;
import com.ischoolbar.programmer.model.CurrentAttend;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.SelectedCourse;
import com.ischoolbar.programmer.model.Student;
import com.ischoolbar.programmer.util.DateFormatUtil;

public class AttendanceServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		//����ǩ������ʱ��
		if(request.getSession().getAttribute("limitDate")==null) {
			request.getSession().setAttribute("limitDate", " ");	
		}
		if("toAttendanceServletListView".equals(method)){
			try {
				request.getRequestDispatcher("view/attendanceList.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("AddAttendance".equals(method)){
			AddAttendance(request,response);
		}else if("AttendanceList".equals(method)){
			attendanceList(request,response);
		}else if("DeleteAttendance".equals(method)){
			deleteAttendance(request,response);
		}else if("getStudentSelectedCourseList".equals(method)){
			getStudentSelectedCourseList(request, response);
		}
	}
	/*
	 * 
	 * ɾ��ǩ����¼
	 */
	private void deleteAttendance(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		AttendanceDao attendanceDao = new AttendanceDao();
		String msg = "success";
		if(!attendanceDao.deleteAttendance(id)){
			msg = "error";
		}
		attendanceDao.closeCon();
		response.getWriter().write(msg);
	}
	/*
	 * ǩ���б�
	 */
	private void attendanceList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		String type = request.getParameter("type");
		String date = request.getParameter("date");
		System.out.println(date);
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Attendance attendance = new Attendance();
		//��ȡ��ǰ��¼�û�����
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 2){
			//�����ѧ����ֻ�ܲ鿴�Լ�����Ϣ
			Student currentUser = (Student)request.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		attendance.setCourseId(courseId);
		attendance.setStudentId(studentId);
		attendance.setDate(date);
		attendance.setType(type);
		AttendanceDao attendanceDao = new AttendanceDao();
		List<Attendance> attendanceList = attendanceDao.getSelectedCourseList(attendance, new Page(currentPage, pageSize));
		int total = attendanceDao.getAttendanceListTotal(attendance);
		attendanceDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", attendanceList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(attendanceList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * ���ǩ����¼
	 */
	private void AddAttendance(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		String type = request.getParameter("type").toString();
		String validation = request.getParameter("validation").toString();
		//ѧ��ǩ����Ϣ����
		AttendanceDao attendanceDao = new AttendanceDao();
		Attendance attendance = new Attendance();
		attendance.setCourseId(courseId);
		attendance.setStudentId(studentId);
		attendance.setType(type);
		attendance.setDate(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH"));
		attendance.setState("ǩ���ɹ�");
		//��ʦ��Ӵ�ǩ���γ���Ϣ
		CurrentAttendDAO currentAttendDAO=new CurrentAttendDAO();
		CurrentAttend currentAttend=new CurrentAttend();
		currentAttend.setCourseId(courseId);
		currentAttend.setValidation(validation);
		currentAttend.setState("true");
		currentAttend.setDate(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"));
		String msg = "success";
		response.setCharacterEncoding("UTF-8");
	    //���´�ǩ���γ���Ϣ
		List<CurrentAttend> currentAttends=currentAttendDAO.getCurrentAttendList();
		for(int i=0;i<currentAttends.size();i++) {
			System.out.println(currentAttends.size());
			if(!DateFormatUtil.isvalid(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), currentAttends.get(i).getLimit_date())) {
				currentAttendDAO.modify(currentAttends.get(i).getId(), "false");
			}
		}
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		//��֤ѧ��ǩ��
		if(userType == 2){
		//��֤�Ƿ����ǩ��
		if(currentAttendDAO.isadded(courseId, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"))){
		CurrentAttend testCurrentAttend=currentAttendDAO.getCurrentAttend(courseId, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"));
		//ǩ����ֹʱ��
		request.getSession().setAttribute("limitDate", "��ֹʱ��:"+testCurrentAttend.getLimit_date());
		//��֤ǩ����+ǩ��ʱ��
		if(!validation.equals(testCurrentAttend.getValidation())||!DateFormatUtil.isvalid(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), testCurrentAttend.getLimit_date())) {
		attendance.setState("ǩ��ʧ��");
		//��֤�Ƿ����м�¼
		if(attendanceDao.isAttendanced(studentId, courseId, type,DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH"))){
			msg = "���б���ǩ����¼�������ظ�ǩ����";
		}else if(!attendanceDao.addAttendance(attendance)){
			msg = "ϵͳ�ڲ���������ϵ����Ա��";
		}
		}else if(attendanceDao.isAttendanced(studentId, courseId, type,DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH"))){
			msg = "���б���ǩ����¼�������ظ�ǩ����";
		}else if(!attendanceDao.addAttendance(attendance)){
			msg = "ϵͳ�ڲ���������ϵ����Ա��";
		}
	}else {
		request.getSession().setAttribute("limitDate", " ");
		msg="�ÿγ�ǩ��ϵͳ��ʱû�п��ţ�";	
	}
	 }
		//��֤��ʦ��Ӵ�ǩ���γ���Ϣ
		if(userType == 3){
			String limitTime = request.getParameter("limit_date").toString();
			currentAttend.setLimit_date(DateFormatUtil.addDate(new Date(), Integer.parseInt(limitTime), "yyyy-MM-dd HH:mm:ss"));
			if (currentAttendDAO.isadded(courseId, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"))) {
				msg = "����Ӹ���Ϣ�������ظ���ӣ�";
			}else if(currentAttendDAO.addAttendance(currentAttend)) {
				request.getSession().setAttribute("limitDate", "��ֹʱ��:"+DateFormatUtil.addDate(new Date(), Integer.parseInt(limitTime), "yyyy-MM-dd HH:mm:ss"));
			}else {
				msg = "ϵͳ�ڲ���������ϵ����Ա��";
			}
		}
		try {
			response.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * ��ȡѧ��ѡ���б�
	 */
	private void getStudentSelectedCourseList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("student_id") == null ? 0 : Integer.parseInt(request.getParameter("student_id").toString());
		SelectedCourse selectedCourse = new SelectedCourse();
		selectedCourse.setStudentId(studentId);
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		List<SelectedCourse> selectedCourseList = selectedCourseDao.getSelectedCourseList(selectedCourse, new Page(1, 999));
		selectedCourseDao.closeCon();
		String courseId = "";
		for(SelectedCourse sc : selectedCourseList){
			courseId += sc.getCourseId()+ ",";
		}
		courseId = courseId.substring(0,courseId.length()-1);
		CourseDao courseDao = new CourseDao();
		List<Course> courseList = courseDao.getCourse(courseId);
		courseDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(JSONArray.fromObject(courseList).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
