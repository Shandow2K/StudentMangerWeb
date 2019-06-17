package com.ischoolbar.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ischoolbar.programmer.model.Notice;
import com.ischoolbar.programmer.util.DbUtil;

public class NoticeDao extends BaseDao {
      public ArrayList<Notice> findNotice(){
		  String sql="select * from s_notice order by nid desc";
		  ResultSet rs = query(sql);
//		  try {
//			if(rs.next()) {
//			  System.out.println(rs.getString(1).getClass());}
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    	  ArrayList<Notice> notices=new ArrayList<Notice>();
    	  try {
			while (rs.next()) {
				Notice notice=new Notice();
				notice.setNid(Integer.parseInt(rs.getString(1)));
				notice.setTitle(rs.getString(2));
				notice.setContent(rs.getString(3));
				notice.setAuthor(rs.getString(4));
				notice.setnDate(rs.getDate("ndate"));
				System.out.println(notice.getnDate());
				notices.add(notice);
			}
			return notices;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  return null;
    	  
      }
      public ArrayList<Notice> findNotice(String sql){
		  ResultSet rs = query(sql);
//		  try {
//			if(rs.next()) {
//			  System.out.println(rs.getString(1).getClass());}
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    	  ArrayList<Notice> notices=new ArrayList<Notice>();
    	  try {
			while (rs.next()) {
				Notice notice=new Notice();
				notice.setNid(Integer.parseInt(rs.getString(1)));
				notice.setTitle(rs.getString(2));
				notice.setContent(rs.getString(3));
				notice.setAuthor(rs.getString(4));
				notice.setnDate(rs.getDate("ndate"));
				notices.add(notice);
			}
			return notices;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  return null;
    	  
      }
      public void insertNotice(Notice notice) {
    	  DbUtil dbUtil=new DbUtil();
    	  String sql="insert into s_notice (`title`,`content`,`author`,`ndate`) values('"+notice.getTitle()+"','"
    			  +notice.getContent()+"','"+notice.getAuthor()+"','"+notice.getnDate().toString()+"')";
    	  try {
			dbUtil.getConnection().prepareStatement(sql).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      public void deleteNotice(String nid) {
    	  DbUtil dbUtil=new DbUtil();
    	  int id=Integer.parseInt(nid);
    	  String sql="delete from s_notice where nid="+id+"";
    	  try {
			dbUtil.getConnection().prepareStatement(sql).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
}
