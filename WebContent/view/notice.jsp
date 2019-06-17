<%@page import="com.ischoolbar.programmer.model.Notice"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ischoolbar.programmer.dao.NoticeDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%  
    ArrayList<Notice> notices ;
if(request.getAttribute("note")==null ){
	NoticeDao noticeDao=new NoticeDao();
    notices=noticeDao.findNotice();
}else{
	notices=(ArrayList<Notice>)request.getAttribute("note");
}
    
    
%>
<div title="通知中心"  style="padding:20px;overflow:auto; width:800px;height:1500px;" >
   
	<form action="notice?action=inquire" method="post" onsubmit="return check()">
		<table align="center" border="0" cellPadding="3" cellSpacing="0" width="35%">
              <TR> 
                <TD width="20%" ><FONT color="red">*</FONT>关键字查询:</TD>
                <TD><input name="key" type="text" id="key" size="40" maxlength="30" dataType="Require" placeholder="请填写查询关键字"></TD>
                <td><input class="form_button" type="submit" name="Submitor" value=" 查询 " ></td>
                <td><a href="notice?action=refresh" style="border: 2px grey solid; font-size: 14px;">刷新</a></td>
              </TR>
        </table>
   </form>
	<h2 align="center" style="font-size: 20px;">已发布通知</h2>
   <%
	for(int i=0;i<notices.size();i++){
		%>
   <div  style="position:relative;text-align: center; white-space:normal;margin:0 auto;width:73%;height:auto;margin-bottom:10px;border:1px solid lightblue;border-radius:10px;font-size: 14px;">
    <h2 align="center" style="font-size: 16px;">  <%=notices.get(i).getTitle()%></h2>
    <%=notices.get(i).getContent() %><br><br>
    <%=notices.get(i).getAuthor() %><br>
    <%=notices.get(i).getnDate() %>
    </div>
    
    <%} %>
</div>
</body>
</html>