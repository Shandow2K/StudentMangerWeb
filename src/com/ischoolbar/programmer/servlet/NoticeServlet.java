package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.dao.NoticeDao;
import com.ischoolbar.programmer.model.Notice;
import com.ischoolbar.programmer.model.Teacher;

/**
 * Servlet implementation class notice
 */
@WebServlet("/notice")
public class NoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 request.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html;UTF-8");
		 String action = request.getParameter("action");
		 switch (action) {
		case "push":
			pushnotice(request, response);
			break;
		case "del":
			deletenotice(request,response);
			break;
		case "inquire":
			inquirenotice(request,response);
			break;
		case "refresh":
			refreshnotice(request,response);
			break;
		default:
			break;
		}	 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	/**
	 * 发布通知
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void pushnotice(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 String id=request.getParameter("id");
		 String flag=(String) request.getParameter("flag");
		 String title=request.getParameter("title");
		 String content=request.getParameter("content");
		 Teacher teacher=(Teacher) request.getSession().getAttribute("user");
		 NoticeDao noticeDao=new NoticeDao();
		 System.out.println(flag);
			//获取当前时间
			 Date time= new java.sql.Date(new java.util.Date().getTime());
			 String type= request.getSession().getAttribute("userType").toString();
			 System.out.println(id);
			 System.out.println(type);
			 System.out.println(title);
			 System.out.println(time);
			 //将通知信息存入数据库
			 Notice notice=new Notice();
			 notice.setAuthor(teacher.getName());
			 notice.setContent(content);
			 notice.setnDate(time);
			 notice.setTitle(title);
			 System.out.println(notice.getnDate());;
			 noticeDao.insertNotice(notice);
			 request.getRequestDispatcher("view/system.jsp").forward(request, response);
	
	}
	/**
	 * 
	 * 删除通知
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deletenotice(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		     NoticeDao noticeDao=new NoticeDao();
             String nid=(String) request.getParameter("nid");
		     noticeDao.deleteNotice(nid);
		     System.out.println(nid);
		     request.getRequestDispatcher("view/system.jsp").forward(request, response);
	}
	/*
	 * 关键字查询
	 * @key  关键字
	 * 
	 */
	private void inquirenotice(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		     String key=request.getParameter("key");
		     System.out.println(key);
		     NoticeDao noticeDao=new NoticeDao();
		     String str="select * from s_notice where content like '%"+key+"%' order by nid desc ";
		     ArrayList<Notice> note=noticeDao.findNotice(str);
		     request.setAttribute("note", note);
		     request.getRequestDispatcher("view/system.jsp").forward(request, response);
	}
	/**
	 * 刷新通知
	 */
	private void refreshnotice(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		NoticeDao noticeDao=new NoticeDao();
		ArrayList<Notice> note=noticeDao.findNotice();
	     request.setAttribute("note", note);
		request.getRequestDispatcher("view/system.jsp").forward(request, response);
	}
}
