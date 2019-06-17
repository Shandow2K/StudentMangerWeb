<%@page import="com.ischoolbar.programmer.model.Teacher"%>
<%@page import="com.ischoolbar.programmer.model.Admin"%>
<%@page import="com.ischoolbar.programmer.model.Notice"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ischoolbar.programmer.dao.NoticeDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>通知发布</title>
<style type="text/css">
table {
	border: 2px #CCCCCC solid;
	width: 700px;
}

td,th {
	height: 30px;
	border: lightblue 1px solid;
}

</style>
<script>
 function check(){
	 var temp=/^[1-9]+[0-9]*]*$/;
	   if(document.getElementById("title").value==""){
		   alert("请填写通知标题！");  
		   document.getElementById("title").focus();
		   return false;
	   }
	   if(document.getElementById("content").value==""){
		   alert("请填写通知内容！");  
		   document.getElementById("content").focus();
		   return false;
	   }
	   }</script>
</head>
<body>

<%  NoticeDao noticeDao=new NoticeDao();
    Teacher user=(Teacher) request.getSession().getAttribute("user");
    String sql="select * from s_notice  where author='"+user.getName()+"'"+"order by nid desc";
    ArrayList<Notice> notices=noticeDao.findNotice(sql);
%>
<div title="通知发布" style="padding:20px;overflow:auto; width:800px; height:1200px" >
   <form action="notice?action=push" method="post" onsubmit="return check()">
		<table align="center" border="0" cellPadding="2" cellSpacing="0" width="95%">
              <TR> 
                <TD width="12%"><FONT color="red">*</FONT>标　　题：</TD>
                <TD><input name="title" type="text" id="title" size="50" maxlength="50" dataType="Require" placeholder="请填写通知标题，不超过50个字。"></TD>
              </TR>
              <TR> 
                <TD><FONT color="red">*</FONT>内　　容：</TD>
                <TD><textarea cols="60" name="content" rows="9" dataType="Limit" min="20" max="2000"  placeholder="请填写通知内容,字数不能少于20。"></textarea></TD>
              </TR>
              <TR> 
                <TD colspan="2"  align="center"><input class="form_button" type="submit" name="Submitor" value=" 发布 " > 
                	<input class="form_button" type="reset" name="reset" value=" 重 置 ">
                </TD>
              </TR>
        </table>
   </form>
   <h1 align="center" style="font-size: 20px;">已发布通知</h1>
   <%for(int i=0;i<notices.size();i++){ %>
   <div align="center" style=" position:relative;text-align: center; white-space:normal;margin:0 auto;width:73%;height:auto;margin-bottom:10px;border:1px solid lightblue;border-radius:10px;font-size: 14px;">
    <h2 align="center" style="font-size: 16px;">  <%=notices.get(i).getTitle()%></h2>
    <%=notices.get(i).getContent() %><br><br>
    <%=notices.get(i).getAuthor() %><br>
    <%=notices.get(i).getnDate() %>
    <div style="position:absolute;width:30px;height:15px;border:1px solid lightblue;right:0px;top:0px;z-index:100;border-radius:10px;"><a href="notice?action=del&nid=<%=notices.get(i).getNid()%>" style="font-size: 14px;">删除</a></div>
    </div>
<%} %>
</div>
</body>
</html>