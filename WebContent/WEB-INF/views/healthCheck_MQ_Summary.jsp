<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.fasterxml.jackson.databind.*"%>
<%@ page import="com.fasterxml.jackson.databind.node.*"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="header.jsp" flush="true" />
<title>云计算基础架构平台</title>
<style>
body,ul li { 
	font-size: 13px; 
}
.trBottomCol{
	border-bottom:1px solid #dfdfe4;
}
.content {
	position:relative;
	float:right;
	width:calc(100% - 57px);
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
}
.current1,.current1:hover {
    color: #444444;
}
</style>
</head>

<body>
	<!--header start-->
	<div class="header">
		<jsp:include page="topleft_close.jsp" flush="true" />
	</div>
	<!--header end-->

	<!--content start-->
	<div class="content">
		<div class="breadcrumb">
			<a href="" class="current">
				<i class="icon-home"></i>实例一览
			</a>
			<a href="#" class="current1">IBM 健康检查</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="widget-box collapsible">
						<div class="widget-title">
							<a data-toggle="collapse" href="#collapseOne">
								<span class="icon"> <i class="icon-arrow-right"></i></span>
								<h5>说明：</h5>
							</a>
						</div>
						<div id="collapseOne" class="collapse in">
							<div class="widget-content">MQ健康检查详细信息.</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 移除按钮 -->
		<div>
		<div style="margin-top: 8px;float: left;">
			<span style="margin-right: 15px;"></span>
			<button onclick="downloadInfo('os');" class="btn btn-sm" style="background-color: rgb(68, 143, 200);">
				<font color="white">下载OS详细巡检资料</font>
			</button>
		</div>
		<div style="margin-top: 8px; float: left;" >
			<span style="margin-right: 12px;"></span>
			<button onclick="downloadInfo('mq');" class="btn btn-sm" style="background-color: rgb(68, 143, 200);">
				<font color="white">下载MQ详细巡检资料</font>
			</button>
		</div>
		</div>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
				  <div class="columnauto">
				    <div class="tabtitle">
							<ul class="tabnav">
								<li class="active">OS巡检报告</li>
								<li>MQ巡检报告</li>
							</ul>
					</div>
					
				 <div class="tabcontent tabnow">
					<div class="mainmodule">
					    <div style="background-color:rgb(243,243,243);height:33px;line-height:33px;font-size:13px;">
					    	<strong>&nbsp;&nbsp;基本信息</strong>
					    </div>
						<table style="line-height:23px;font-size:12px;">
							<tr>
								<td style="width:200px;">&nbsp;&nbsp;主机名</td>
								<td>${OSMap.OSHostname }</td>
							</tr>
							<tr>
								<td style="width:200px;">&nbsp;&nbsp;IP</td>
								<td>${OSMap.OSIP }</td>
							</tr>
							<tr>
								<td style="width:200px;">&nbsp;&nbsp;操作系统版本</td>
								<td>${OSMap.OSVersion }</td>
							</tr>
							<tr>
								<td style="width:200px;">&nbsp;&nbsp;系统运行时间</td>
								<td>${OSMap.OSStart }</td>
							</tr>
						</table> 
						
						<div style="margin-top: 30px;margin-bottom: 5px;"></div>
						<table class="table table-hover" style="line-height:25px;">
							<thead>
								<tr>
									<th>资源使用率</th>
									<th>健康基线</th>
									<th>巡检情况</th>
									<th>健康状态</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="osSystemInfo" items="${ OSSystemInfoList}">
									<c:if test="${ osSystemInfo.healthStatus eq 'GOOD' }">
										<tr style="color: green;">
											<td>${osSystemInfo.healthInfo}</td>
											<td>${osSystemInfo.healthLine}</td>
											<td>${osSystemInfo.healthValue}</td>
											<td>${osSystemInfo.healthStatus}</td>
										</tr>
									</c:if>
									<c:if test="${ osSystemInfo.healthStatus eq 'WARN' }">
										<tr style="color: yellow;">
											<td>${osSystemInfo.healthInfo}</td>
											<td>${osSystemInfo.healthLine}</td>
											<td>${osSystemInfo.healthValue}</td>
											<td>${osSystemInfo.healthStatus}</td>
										</tr>
									</c:if>
									<c:if test="${ osSystemInfo.healthStatus eq 'BAD' }">
										<tr style="color: RED;">
											<td>${osSystemInfo.healthInfo}</td>
											<td>${osSystemInfo.healthLine}</td>
											<td>${osSystemInfo.healthValue}</td>
											<td>${osSystemInfo.healthStatus}</td>
										</tr>
									</c:if>
								
								
								
								</c:forEach>
							</tbody>
						</table>
						
						<div style="margin-top: 30px;margin-bottom: 5px;"></div>
						<div style="background-color:rgb(243,243,243);height:33px;line-height:33px;font-size:13px;">
					    	<strong>&nbsp;&nbsp;系统环境变量</strong>
					    </div>
					   <table style="font-size:12px;">
					   		<c:forEach items="${OSENVList}" var="env">
					   			<tr><td>${ env}</td></tr>
					   		</c:forEach>
					   
					   </table>
					   <div style="margin-top: 30px;margin-bottom: 5px;"></div>
						<table class="table table-hover" style="line-height:25px;">
							<thead>
							<tr>
								<th>CPU使用率TOP10进程</th>
								<th>运行时间（分）</th>
								<th>进程ID</th>
								<th>CPU使用率</th>
								<th>所属用户</th>
							</tr>
							</thead>
							<tbody>
								<c:forEach var="ostop10" items="${OSTop10List }">
									<tr>
										<td>${ ostop10.command}</td>
										<td>${ ostop10.elapese}</td>
										<td>${ ostop10.pid}</td>
										<td>${ ostop10.cpu}</td>
										<td>${ ostop10.user}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div style="margin-top: 30px;margin-bottom: 5px;"></div>
						<div style="background-color:rgb(243,243,243);height:33px;line-height:33px;font-size:13px;">
					    	<strong>&nbsp;&nbsp;Crontab计划任务</strong>
					    </div>
						<table  id="cronjobs" name="cronjobs" style="font-size:12px;">
							<c:forEach var="cronjob"  items="${OSCronjobs }">
								<tr><td style="width:200px;">${cronjob.key}<td style="width:600px;">${cronjob.value}</td></tr>
							</c:forEach>
							
						</table>

					  </div>
					</div>
					
					<div class="tabcontent">
						<div class="mainmodule">
						    <div style="background-color:rgb(243,243,243);height:33px;line-height:33px;">
						    	<strong>&nbsp;&nbsp;基本信息</strong>
						    </div>
							<table style="line-height:23px;font-size:12px;">
								<tr>
									<td style="width:200px;">&nbsp;&nbsp;MQ版本</td>
									<td>${MQMap.MQVersion }</td>
								</tr>
								<tr>
									<td style="width:200px;">&nbsp;&nbsp;MQ安装路径</td>
									<td>${MQMap.MQInstallPath }</td>
								</tr>
							</table> 
							
							<div style="margin-top: 30px;margin-bottom: 5px;"></div>
						    <table class="table table-hover">
								<thead>
									<tr>
										<th style="width:15%;">MQ系统参数</th>	
										<th style="width:40%;">参数说明</th>			
										<th style="width:15%;">健康基线</th>
										<!-- <th style="width:15%;">巡检情况</th> -->
										<th style="width:15%;">健康状态</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Semmns</td>	
										<td>设置系统中信号灯的最大数量</td>										
										<td>${MQMap.MQsemmns_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQsemmns }</td>								
									</tr>
									<tr>
										<td>Semmsl</td>	
										<td>设置每个信号灯组中信号灯最大数量</td>	
										<td>${MQMap.MQsemmsl_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQsemmsl }</td>								
									</tr>
									<tr>
										<td>nofile</td>	
										<td>设置可以打开最大文件描述符的资源限制数量</td>	
										<td>${MQMap.MQnofile_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQnofile }</td>								
									</tr>
									<tr>
										<td>shmmni</td>		
										<td>设置系统级最大共享内存段数量</td>										
										<td>${MQMap.MQshmmni_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQshmmni }</td>								
									</tr>
									<tr>
										<td>file-max</td>	
										<td>设置系统所有进程一共可以打开的文件数量</td>										
										<td>${MQMap.MQfile_max_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQfile_max }</td>														
									</tr>
									<tr>
										<td>shmmax</td>		
										<td>设置Linux进程可以分配的单独共享内存段的最大值</td>									
										<td>${MQMap.MQshmmax_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQshmmax }</td>								
									</tr>
									<tr>
										<td>nproc</td>		
										<td>设置可以打开最大文件描述符的资源限制数量</td>									
										<td>${MQMap.MQnproc_line}</td>
										<td>&nbsp;&nbsp;${MQMap.MQnproc}</td>								
									</tr>
									<tr>
										<td>semmni</td>	
										<td>设置系统中信号灯组的最大数量</td>										
										<td>${MQMap.MQsemmni_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQsemmni }</td>								
									</tr>
									<tr>
										<td>shmall</td>	
										<td>设置共享内存总页数</td>										
										<td>${MQMap.MQshmall_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQshmall }</td>								
									</tr>
									<tr>
										<td>semopm</td>	
										<td>设置每次系统调用可以同时执行的最大信号灯操作的数量</td>										
										<td>${MQMap.MQsemopm_line }</td>
										<td>&nbsp;&nbsp;${MQMap.MQsemopm }</td>								
									</tr>
								</tbody>
							</table>
							
							<div style="margin-top: 30px;margin-bottom: 5px;"></div>						    
						    <table class="table table-hover">
								<thead>
									<tr>
										<th style="width:20%;">队列管理器名称</th>
										<th style="width:16%;">队列管理器状态</th>
										<th style="width:16%;">是否定义死信队列</th>
										<th style="width:16%;">是否有消息堆积</th>
										<th style="width:16%;">通道状态是否正常</th>
										<th style="width:16%;">监听器状态是否正常</th>
									</tr>
								</thead>
									<tbody>
									<c:forEach items="${ MQQMGRList}" var="mqmqgr">
										<tr>
											<td>${mqmqgr.qmgrName }</td>
											<td>${mqmqgr.qmgr }</td>
											<td>${mqmqgr.dlq }</td>
											<td>${mqmqgr.que }</td>
											<td>${mqmqgr.chl }</td>
											<td>${mqmqgr.lstr }</td>
										</tr>
									
									</c:forEach>
																
									</tbody>
								</table>
							
						</div>
					</div>
				  </div>
				</div>
			</div>
		</div>
		<div>
			<form action="" id="tempForm" name="tempForm">
				<input type="hidden" id="healthJobsRunResult_ip" name="healthJobsRunResult_ip" value=" ${OSMap.OSIP } ">
				<input type="hidden" id="healthJobsRunResult_uuid" name="healthJobsRunResult_uuid" value="${healthJobsRunResult_uuid }">
			</form>
		
		</div>
	</div>
<script type="text/javascript">
		function downloadInfo(pro) {
			var uuid=$("#healthJobsRunResult_uuid").val();
			var ip=$("#healthJobsRunResult_ip").val();
			var form = $("<form>");//定义一个form表单
			form.attr("style", "display:none");
			form.attr("target", "download");
			form.attr("method", "post");
			form.attr("action", "healthCheck_download_summary_info.do");
			var input1 = $("<input>");
			input1.attr("type", "hidden");
			input1.attr("name", "healthJobsRunResult_uuid");
			input1.attr("value", uuid);
			var input2 = $("<input>");
			input2.attr("type", "hidden");
			input2.attr("name", "healthJobsRunResult_ip");
			input2.attr("value", ip);
			var input3 = $("<input>");
			input3.attr("type", "hidden");
			input3.attr("name", "product");
			input3.attr("value", pro);
			$("body").append(form);//将表单放置在web中
			form.append(input1);
			form.append(input2);
			form.append(input3);
			form.submit();//表单提交 

		}
	</script>
</body>
</html>
