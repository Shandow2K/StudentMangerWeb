package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.apache.catalina.startup.Embedded;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ischoolbar.programmer.dao.CourseDao;
import com.ischoolbar.programmer.dao.SelectedCourseDao;
import com.ischoolbar.programmer.model.Course;
import com.ischoolbar.programmer.model.SelectCourseDate;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.SelectedCourse;
import com.ischoolbar.programmer.model.Student;
import com.ischoolbar.programmer.dao.SelectCourseDateDao;
import com.ischoolbar.programmer.util.DateFormatUtil;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.ischoolbar.programmer.util.DateCompareUtil;

public class SelectedCourseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7120913402001186955L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		String method = request.getParameter("method");
		int selectCourseState;
		SelectCourseDateDao selectCourseDateDao = new SelectCourseDateDao();
		SelectCourseDate selectCourseDate = selectCourseDateDao.getSelectCourseDate();
		// 判断当前时间是否可以选课
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		Calendar begin = Calendar.getInstance();
		begin.setTime(selectCourseDate.getStartDate());
		Calendar end = Calendar.getInstance();
		end.setTime(selectCourseDate.getEndDate());
		if (date.after(begin) && date.before(end)) {
			selectCourseState = 1;
		} else {
			selectCourseState = 0;
		}
		request.getSession().setAttribute("selectCourseState", selectCourseState);
		if ("toSelectedCourseListView".equals(method)) {
			try {
				request.getRequestDispatcher("view/selectedCourseList.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("AddSelectedCourse".equals(method)) {
			addSelectedCourse(request, response);
		} else if ("SelectedCourseList".equals(method)) {
			getSelectedCourseList(request, response);
		} else if ("DeleteSelectedCourse".equals(method)) {
			deleteSelectedCourse(request, response);
		} else if ("isSelectCourseDate".equals(method)) {
			isSelectCourseDate(request, response);
		} else if ("toSetSelectCourseDateView".equals(method)) {
			try {
				request.getRequestDispatcher("view/setSelectCourseDate.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("setSelectCourseDate".equals(method)) {
			try {
				setSelectCourseDate(request, response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void deleteSelectedCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		SelectedCourse selectedCourse = selectedCourseDao.getSelectedCourse(id);
		String msg = "success";
		if (selectedCourse == null) {
			msg = "not found";
			response.getWriter().write(msg);
			selectedCourseDao.closeCon();
			return;
		}
		if (selectedCourseDao.deleteSelectedCourse(selectedCourse.getId())) {
			CourseDao courseDao = new CourseDao();
			courseDao.updateCourseSelectedNum(selectedCourse.getCourseId(), -1);
			courseDao.closeCon();
		} else {
			msg = "error";
		}
		selectedCourseDao.closeCon();
		response.getWriter().write(msg);
	}

	private void addSelectedCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msg = "success";
		int studentId = request.getParameter("studentid") == null ? 0
				: Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0
				: Integer.parseInt(request.getParameter("courseid").toString());
		CourseDao courseDao = new CourseDao();

		if (courseDao.isFull(courseId)) {
			msg = "courseFull";
			response.getWriter().write(msg);
			courseDao.closeCon();
			return;
		}
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		if (selectedCourseDao.isSelected(studentId, courseId)) {
			msg = "courseSelected";
			response.getWriter().write(msg);
			selectedCourseDao.closeCon();
			return;
		}
		SelectedCourse selectedCourse = new SelectedCourse();
		selectedCourse.setStudentId(studentId);
		selectedCourse.setCourseId(courseId);
		if (selectedCourseDao.addSelectedCourse(selectedCourse)) {
			msg = "success";
		}
		courseDao.updateCourseSelectedNum(courseId, 1);
		courseDao.closeCon();
		selectedCourseDao.closeCon();
		response.getWriter().write(msg);
	}

	private void getSelectedCourseList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("studentid") == null ? 0
				: Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0
				: Integer.parseInt(request.getParameter("courseid").toString());
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		SelectedCourse selectedCourse = new SelectedCourse();
		// 获取当前登录用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if (userType == 2) {
			// 如果是学生，只能查看自己的信息
			Student currentUser = (Student) request.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		selectedCourse.setCourseId(courseId);
		selectedCourse.setStudentId(studentId);
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		List<SelectedCourse> courseList = selectedCourseDao.getSelectedCourseList(selectedCourse,
				new Page(currentPage, pageSize));
		int total = selectedCourseDao.getSelectedCourseListTotal(selectedCourse);
		selectedCourseDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", courseList);
		try {
			String from = request.getParameter("from");
			if ("combox".equals(from)) {
				response.getWriter().write(JSONArray.fromObject(courseList).toString());
			} else {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void isSelectCourseDate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msg = null;
		SelectCourseDateDao selectCourseDateDao = new SelectCourseDateDao();
		SelectCourseDate selectCourseDate = selectCourseDateDao.getSelectCourseDate();
		// 判断当前时间是否可以选课
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		Calendar begin = Calendar.getInstance();
		begin.setTime(selectCourseDate.getStartDate());
		Calendar end = Calendar.getInstance();
		end.setTime(selectCourseDate.getEndDate());

		if (date.after(begin) && date.before(end)) {

			msg = DateFormatUtil.getFormatDate(selectCourseDate.getStartDate(), "yyyy-MM-dd") + "  至 "
					+ DateFormatUtil.getFormatDate(selectCourseDate.getEndDate(), "yyyy-MM-dd");

		} else {
			msg = "fail";

		}
		selectCourseDateDao.closeCon();
		response.getWriter().write(msg);

	}

	/*
	 * 设置选课时间
	 */
	private void setSelectCourseDate(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		String msg = "success";
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		// 检查选课时间是否正确
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar begin = Calendar.getInstance();
		begin.setTime(sdFormat.parse(startdate));
		Calendar end = Calendar.getInstance();
		end.setTime(sdFormat.parse(enddate));
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		if (begin.before(end)) {
			if (date.before(begin) || DateCompareUtil.isTheSameDay(new Date(), sdFormat.parse(startdate))) {
				SelectCourseDateDao selectCourseDateDao = new SelectCourseDateDao();
				if (selectCourseDateDao.editSelectCourseDate(startdate, enddate)) {
					msg = "success";

				} else {
					msg = "设置失败！";
				}
				selectCourseDateDao.closeCon();
			} else {
				msg = "开始时间不能在今天之前！";
			}
		} else {
			msg = "开始时间必须大于结束时间！";
		}
		response.getWriter().write(msg);
	}
}
