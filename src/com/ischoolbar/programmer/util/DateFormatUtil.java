package com.ischoolbar.programmer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {
	public static String getFormatDate(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	public static Date getFormatdate(String date,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date currentDate=null;
		try {
			currentDate = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return currentDate;
	}
	public static String addDate(Date date,int limit,String format) {
		Calendar nowTime=Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, limit);
		Date currentDate=nowTime.getTime();
		System.out.println(currentDate);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(currentDate);
	}
	public static boolean isvalid(String nowtime,String limittime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate=null;
		Date limitDate=null;
		try {
			nowDate=sdf.parse(nowtime);
			limitDate=sdf.parse(limittime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nowDate.before(limitDate);
	}
	 
}
