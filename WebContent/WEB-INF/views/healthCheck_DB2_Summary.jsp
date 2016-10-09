<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
							<div class="widget-content">DB2健康检查详细信息.</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		 
		<div>
			<div style="margin-top: 8px;float: left;">
				<span style="margin-right: 15px;"></span>
				<button onclick="downloadInfo('os');" class="btn btn-sm" style="background-color: rgb(68, 143, 200);">
					<font color="white">下载OS详细巡检资料</font>
				</button>
			</div>
			<div style="margin-top: 8px; float: left;" >
				<span style="margin-right: 12px;"></span>
				<button onclick="downloadInfo('db2');" class="btn btn-sm" style="background-color: rgb(68, 143, 200);">
					<font color="white">下载DB2详细巡检资料</font>
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
								<li>DB2巡检报告</li>
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
						<table class="table table-hover table-bordered" style="line-height:25px;">
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
						<table class="table table-hover table-bordered" style="line-height:25px;">
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
					
		     <!-- 产品信息 -->
					<div class="tabcontent">
						<table class="table table-hover" style="line-height:25px;border:1px solid #dfdfe4;">
							<thead>
								<tr>
									<th>DB2系统参数</th>
									<th>参数说明</th>
									<th>巡检基线</th>
									<th>巡检结果</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>kernel.msgmax</td>
									<td>最大消息长度</td>
									<td>=${DB2Map.db2_msgmax_line }</td>
									<td>${DB2Map.db2_msgmax }</td>
								</tr>
								<tr>
									<td>kernel.sem</td>
									<td>系统信号量参数</td>
									<td>=${DB2Map.db2_sem_line}</td>
									<td>${DB2Map.db2_sem }</td>
								</tr>
								<tr>
									<td>kernel.shmall</td>
									<td>设置共享内存总页数</td>
									<td>=${DB2Map.db2_shmall_line}</td>
									<td>${DB2Map.db2_shmall }</td>
								</tr>
								<tr>
									<td>kernel.msgmnb</td>
									<td>消息队列中的最大字节数</td>
									<td>=${DB2Map.db2_msgmnb_line}</td>
									<td>${DB2Map.db2_msgmnb }</td>
								</tr>
								<tr>
									<td>kernel.shmmax</td>
									<td>设置Linux进程可以分配的单独共享内存段的最大值</td>
									<td>=${DB2Map.db2_shmmax_line}</td>
									<td>${DB2Map.db2_shmmax }</td>
								</tr>
								<tr>
									<td>kernel.shmmni</td>
									<td>设置系统级最大共享内存段数量</td>
									<td>=${DB2Map.db2_shmmni_line }</td>
									<td>${DB2Map.db2_shmmni }</td>
								</tr>
								<tr>
									<td>kernel.msgmni</td>
									<td>最大消息队列数</td>
									<td>=${DB2Map.db2_msgmni_line }</td>
									<td>${DB2Map.db2_msgmni }</td>
								</tr>
							</tbody>
						</table>	
						
					  <div style="margin-top: 30px;"></div>		
			 <c:forEach var="instance" items="${instanceList }">	
					  <table class="table" style="line-height:25px;border-top:1px solid #dfdfe4;border-left:1px solid #dfdfe4;border-right:1px solid #dfdfe4;">
					     <thead>
					     	 <tr>
						  	 	<th style="width:12%;">实例名称</th>
						  	 	<th>${instance.db2_instanceName }</th>
						  	 </tr>
					     </thead>
					     <tbody>
						  	 <tr>
						  	 	<td><strong>产品安装路径</strong></td>
						  	 	<td>${instance.db2_instancePath }</td>
						  	 </tr>	
						  	 <tr style="border-bottom:1px solid #dfdfe4;">
						  	 	<td><strong>产品版本</strong></td>
						  	 	<td>${instance.db2_instanceLevel }</td>
						  	 </tr>			     
					     </tbody>				  					  	 
					  </table>
					  
					  <table class="table table-bordered" style="line-height:25px;border-bottom:1px solid #dfdfe4;margin-top:-6px;">
						<tr style="font-weight:bolder;">
							<td style="text-align:center;width:6%;" rowspan="2" class="trBottomCol">数据库</td>
							<td style="text-align:center;width:6%;" rowspan="2" class="trBottomCol">Status</td>
							<td style="text-align:center;" colspan="2">Tablespace</td>
							<td style="text-align:center;">BufferPool</td>											
							<td style="text-align:center;" colspan="6">Snapshot</td>
							<td style="text-align:center;" rowspan="2" class="trBottomCol">LOGMETHOD</td>
						</tr>
						<tr style="font-weight:bolder;">
							<td style="text-align:center;" class="trBottomCol">Status</td>
							<td style="text-align:center;" class="trBottomCol">percent</td>
							<td style="text-align:center;" class="trBottomCol">hitratio</td>
							<td style="text-align:center;" class="trBottomCol">connections</td>
							<td style="text-align:center;" class="trBottomCol">deadlock</td>
							<td style="text-align:center;" class="trBottomCol">lockeasl</td>
							<td style="text-align:center;" class="trBottomCol">locktimeout</td>
							<td style="text-align:center;" class="trBottomCol">sortoverflow</td>
							<td style="text-align:center;" class="trBottomCol">lastbackup</td>
						</tr>
						 <c:forEach items="${instance.db2_instaceList }" var="inst"> 
							<tr>
								<td style="text-align:center;">${inst.db2_dbName }</td>
								<td style="text-align:center;">${inst.db2_dbStatus }</td>
								<td style="text-align:center;">${inst.db2_dbTablespaceStatus}</td>
								<td style="text-align:center;">${inst.db2_dbTablespacePercent}</td>											
								<td style="text-align:center;">${inst.db2_dbbufferpool_hitratio}</td>
								<td style="text-align:center;">${inst.db2_dbconnections}</td>
								<td style="text-align:center;">${inst.db2_dbdeadlock}</td>
								<td style="text-align:center;">${inst.db2_dblockeasl}</td>
								<td style="text-align:center;">${inst.db2_dblocktimeout}</td>
								<td style="text-align:center;">${inst.db2_dbsortoverflow}</td>
								<td style="text-align:center;">${inst.db2_dblastbackup}</td>
								<td style="text-align:center;">${inst.db2_dbLOGMETHOD}</td>
							</tr>
						</c:forEach> 
								  					  	 
					  </table>
				</c:forEach>					
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
	function downloadInfo(pro) 
	{
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
