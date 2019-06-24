<%@ page language="java" contentType="text/html;  charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<<head>
	<meta charset="UTF-8">
	<title>选课时间设置</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
	
	<style type="text/css">
		.table th{font-weight:bold}
		.table th,.table td{padding:8px;line-height:20px}
		.table td{text-align:left}
		.table-border{border-top:1px solid #ddd}
		.table-border th,.table-border td{border-bottom:1px solid #ddd}
		.table-bordered{border:1px solid #ddd;border-collapse:separate;*border-collapse:collapse;border-left:0}
		.table-bordered th,.table-bordered td{border-left:1px solid #ddd}
		.table-border.table-bordered{border-bottom:0}
		.table-striped tbody > tr:nth-child(odd) > td,.table-striped tbody > tr:nth-child(odd) > th{background-color:#f9f9f9}
	</style>
	
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/js/validateExtends.js"></script>
	<script type="text/javascript">
	$(function() {	
		
		//设置时间窗口
	    $("#setDateDialog").dialog({
	    	title: "设置选课时间",
	    	width: 400,
	    	height: 200,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	  	    		{
	  					text:'提交',
	  					iconCls:'icon-user_add',
	  					handler:function(){
	  						var validate = $("#editDate").form("validate");
	  						if(!validate){
	  							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
	  							return;
	  						} else{
	  							$.ajax({
	  								type: "post",
	  								url: "SelectedCourseServlet?method=setSelectCourseDate",
	  								data: $("#editDate").serialize(),
	  								success: function(msg){
	  									if(msg == "success"){
	  										$.messager.alert("消息提醒","选课时间设置成功","info")
	  										
	  									}else{
	  										$.messager.alert("消息提醒",msg,"error")
	  									}
	  								}
	  							});
	  						}
	  					}
	  				},
	  				{
	  					text:'重置',
	  					iconCls:'icon-reload',
	  					handler:function(){
	  						//清空表单
	  						$("#start_date").textbox('setValue', "");
	  						$("#end_date").textbox('setValue', "");
	  						
	  					}
	  				}
	  			],
	    })
		
		//设置编辑窗口
	    $("#editDialog").dialog({
	    	title: "设置起止时间",
	    	width: 500,
	    	height: 250,
	    	fit: true,
	    	modal: false,
	    	noheader: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: false,
	    	toolbar: [
				{
					text:'设置起止时间',
					plain: true,
					iconCls:'icon-password',
					handler:function(){
						$("#setDateDialog").dialog("open");
					}
				}
			],
			
	    });
		
	    setTimeout(function(){
	    	$("#setDateDialog").dialog("open");
	    },1000);
		
		
	})
	</script>
	<script type="text/javascript">
		function myformatter(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
		}
		function myparser(s){
			if (!s) return new Date();
			var ss = (s.split('-'));
			var y = parseInt(ss[0],10);
			var m = parseInt(ss[1],10);
			var d = parseInt(ss[2],10);
			if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
				return new Date(y,m-1,d);
			} else {
				return new Date();
			}
		}
	</script>
</head>
<body>
	
	<div id="editDialog" style="padding: 20px;">
		
	</div>
	
	<!-- 设置时间窗口 -->
	<div id="setDateDialog" style="padding: 20px">
    	<form id="editDate">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>开始时间:</td>
	    			<td>
	    				<input id="start_date" name="startdate" data-options="formatter:myformatter,parser:myparser" class="easyui-datebox" name="date" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>结束时间:</td>
	    			<td>
	    				<input id="end_date" name="enddate" data-options="formatter:myformatter,parser:myparser" class="easyui-datebox" name="date" />
	    			</td>
	    		</tr>
	    	
	    	</table>
	    </form>
	</div>
	
</body>
</html>