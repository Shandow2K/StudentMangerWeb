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
		//设置签到限制时间
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
	 * 删除签到记录
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
	 * 签到列表
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
		//获取当前登录用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 2){
			//如果是学生，只能查看自己的信息
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
	 * 添加签到记录
	 */
	private void AddAttendance(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		String type = request.getParameter("type").toString();
		String validation = request.getParameter("validation").toString();
		//学生签到信息操作
		AttendanceDao attendanceDao = new AttendanceDao();
		Attendance attendance = new Attendance();
		attendance.setCourseId(courseId);
		attendance.setStudentId(studentId);
		attendance.setType(type);
		attendance.setDate(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH"));
		attendance.setState("签到成功");
		//教师添加待签到课程信息
		CurrentAttendDAO currentAttendDAO=new CurrentAttendDAO();
		CurrentAttend currentAttend=new CurrentAttend();
		currentAttend.setCourseId(courseId);
		currentAttend.setValidation(validation);
		currentAttend.setState("true");
		currentAttend.setDate(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"));
		String msg = "success";
		response.setCharacterEncoding("UTF-8");
	    //更新待签到课程信息
		List<CurrentAttend> currentAttends=currentAttendDAO.getCurrentAttendList();
		for(int i=0;i<currentAttends.size();i++) {
			System.out.println(currentAttends.size());
			if(!DateFormatUtil.isvalid(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), currentAttends.get(i).getLimit_date())) {
				currentAttendDAO.modify(currentAttends.get(i).getId(), "false");
			}
		}
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		//验证学生签到
		if(userType == 2){
		//验证是否可以签到
		if(currentAttendDAO.isadded(courseId, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"))){
		CurrentAttend testCurrentAttend=currentAttendDAO.getCurrentAttend(courseId, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"));
		//签到截止时间
		request.getSession().setAttribute("limitDate", "截止时间:"+testCurrentAttend.getLimit_date());
		//验证签到码+签到时间
		if(!validation.equals(testCurrentAttend.getValidation())||!DateFormatUtil.isvalid(DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), testCurrentAttend.getLimit_date())) {
		attendance.setState("签到失败");
		//验证是否已有记录
		if(attendanceDao.isAttendanced(studentId, courseId, type,DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH"))){
			msg = "已有本次签到记录，请勿重复签到！";
		}else if(!attendanceDao.addAttendance(attendance)){
			msg = "系统内部出错。请联系管理员！";
		}
		}else if(attendanceDao.isAttendanced(studentId, courseId, type,DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd HH"))){
			msg = "已有本次签到记录，请勿重复签到！";
		}else if(!attendanceDao.addAttendance(attendance)){
			msg = "系统内部出错。请联系管理员！";
		}
	}else {
		request.getSession().setAttribute("limitDate", " ");
		msg="该课程签到系统暂时没有开放！";	
	}
	 }
		//验证老师添加待签到课程信息
		if(userType == 3){
			String limitTime = request.getParameter("limit_date").toString();
			currentAttend.setLimit_date(DateFormatUtil.addDate(new Date(), Integer.parseInt(limitTime), "yyyy-MM-dd HH:mm:ss"));
			if (currentAttendDAO.isadded(courseId, DateFormatUtil.getFormatDate(new Date(), "yyyy-MM-dd"))) {
				msg = "已添加该信息，请勿重复添加！";
			}else if(currentAttendDAO.addAttendance(currentAttend)) {
				request.getSession().setAttribute("limitDate", "截止时间:"+DateFormatUtil.addDate(new Date(), Integer.parseInt(limitTime), "yyyy-MM-dd HH:mm:ss"));
			}else {
				msg = "系统内部出错。请联系管理员！";
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
	 * 获取学生选课列表
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
