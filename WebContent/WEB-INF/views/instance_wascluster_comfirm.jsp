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
<title>自动化部署平台</title>
<style type="text/css">
*{
	font-style:normal;
}
label,.input140,.graytxt{
	font-size:13px;
}
.content {
	position:relative;
	float:right;
	width:calc(100% - 57px);
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
}
.sweet-alert button.cancel { 
	background-color: #AEDEF4; 
}
.sweet-alert button.cancel:hover { 
	background-color: #AEDEF4; 
} 
.current1,.current1:hover {
    color: #444444;
}
.col-md-1,.col-md-2,.col-md-3,.col-md-4,.col-md-5,.col-md-6,.col-md-7,.col-md-8,.col-md-9,.col-md-10,.col-md-11,.col-md-12{float:left;}
.col-sm-1,.col-sm-2,.col-sm-3,.col-sm-4,.col-sm-5,.col-sm-6,.col-sm-7,.col-sm-8,.col-sm-9,.col-sm-10,.col-sm-11,.col-sm-12{float:left;}
.col-md-3{width:25%;align:right;}
.col-sm-12{width:100%}.col-sm-11{width:91.66666667%}.col-sm-10{width:83.33333333%}.col-sm-9{width:75%}.col-sm-8{width:45%}.col-sm-7{width:58.33333333%}.col-sm-6{width:50%}.col-sm-5{width:41.66666667%}.col-sm-4{width:33.33333333%}.col-sm-3{width:23%}.col-sm-2{width:16.66666667%}.col-sm-1{width:8.33333333%}
.form-group{margin-right:-15px;margin-left:-15px}
.container{padding-right:15px;padding-left:15px;margin-right:auto;margin-left:auto}
@media (min-width:768px){.container{width:750px}}
@media (min-width:992px){.container{width:970px}}
@media (min-width:1200px){.container{width:1170px}}
.sweet-alert button.cancel { background-color: #ec6c62; }
.sweet-alert button.cancel:hover { background-color: rgb(229,97,88); }
</style>

<script language="javascript" type="text/javascript">
	//操作
	function CheckInput() {
		swal({
            title: "",
            text: "是否确认要在目标主机立即执行任务？",
            type: "warning",
            showCancelButton: true,
            closeOnConfirm: false,
            closeOnCancel: false,
            confirmButtonText: "是",
            cancelButtonText: "否",  
            //confirmButtonColor: "#ec6c62"
        }, 
        function(isConfirm)
        {
        	  if (isConfirm) 
        	  {
        		  $("#submits").submit();
        	  } 
        	  else 
        	  {
        		  window.history.go(0);
        	  }
        });	
	}
</script>
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
			<a href="getAllServers.do" class="current1"><i class="icon-home"></i>IBM WASCluster</a> 
			<a class="current">实例配置详细</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="columnauto">
					   <div class="mainmodule">
							<h5 class="swapcon">
								<i class="icon-chevron-down icon-chevron-right"></i>拓扑结构
							</h5>
							<div class="form-horizontal" style="display:none;">
								<c:forEach items="${servers }" var="ser" varStatus="num">
								<p class="twotit" style="padding-left:15px;">
								          节点<c:out  value="${num.count }"/>
								</p>
									<div class="column" style="margin-left:15px;">
										<div class="span12">
											<p>
												<b>主机名:</b> <span class="column_txt"> ${ser.name } </span> 
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
												<b>IP地址:</b><span class="column_txt">${ser.ip}</span> 
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<b>操作系统:</b><span class="column_txt"><em>${ser.os}</em></span>
											</p>
											<p>
												<b>系统配置:</b><span class="column_txt">${ser.hconf }</span>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
												<b>状态:</b><span class="column_txt"><em><em>${ser.status }</em></span>
											</p>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
						
						<form action="installWasClusterInfo.do?serId=${serId}&platform=${platform}" 
						      method="post" id="submits" class="form-horizontal">
						  <div class="form-horizontal">
							<input type="hidden" id="ptype" name="ptype" value="${ptype}">
							<input type="hidden" id="hostId" name="hostId" value="${hostId}">
							<input type="hidden" id="serId" name="serId" value="${serId}">
							<input type="hidden" id="wasfix" name="wasfix" value="${wasfix }" >
							<input type="hidden" id="platform" name="platform" value="${platform}">
								<input type="hidden" id="was_dmgr_ip" name="was_dmgr_ip" value="${was_dmgr_ip}">
						<input type="hidden" id="was_binary" name="was_binary" value="${was_binary}">  
							<input type="hidden" id="was_user" name="was_user" value="${was_user}">  
							<input type="hidden" id="was_version" name="was_version" value="${was_version}">
							<input type="hidden" id="was_inst_path" name="was_inst_path" value="${was_inst_path}">
							<input type="hidden" id="was_fp" name="was_fp" value="${was_fp}">
							<input type="hidden" id="was_im_path" name="was_im_path" value="${was_im_path}">							 							
							<input type="hidden" id="was_im" name="was_im" value="${was_im}">							 							
							
							<input type="hidden" id="all_ips" name="all_ips" value="${all_ips}"> 
							<input type="hidden" id="all_hostnames" name="all_hostnames" value="${all_hostnames}">
							<input type="hidden" id="all_profilenames" name="all_profilenames" value="${all_profilenames}"> 
							<input type="hidden" id="all_profiletypes" name="all_profiletypes" value="${all_profiletypes}"> 																									
							<input type="hidden" id="was_profile_path" name="was_profile_path" value="${was_profile_path}"> 							 						
							<input type="hidden" id="was_userid" name="was_userid" value="${was_userid}"> 
							<input type="hidden" id="was_password" name="was_password" value="${was_password}">
							
							<input type="hidden" id="was_nofile" name="was_nofile" value="${was_nofile}"> 
							<input type="hidden" id="was_nproc" name="was_nproc" value="${was_nproc}">  
							<input type="hidden" id="was_fsize" name="was_fsize" value="${was_fsize}"> 
							<input type="hidden" id="was_core_soft" name="was_core" value="${was_core}"> 	
							
							<input type="hidden" id="allservers" name="allservers" value="${allservers }">	
							<input type="hidden" id="servers" name="servers" value="${servers}"> 
							<input type="hidden" id="allservertotaljson" name="allservertotaljson" value="${allservertotaljson}">					
								
								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>基本信息
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">安装用户</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${was_user }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">WAS安装版本</span> 
													<span class="graytxt">${was_version }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">WAS安装路径</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${was_inst_path }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">WAS安装补丁</span> 
													<span class="graytxt">${wasfix }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">IM安装路径</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${was_im_path }</span>
												</div>
											</div>
										</div>
									</div>
								</div>

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>概要属性
								</h5>
								<div class="form-horizontal">	
									<c:forEach items="${allservers }" var="sers">		   
                                         <div class="control-group" style="padding-bottom: 10px;padding-top:10px;"> 
												<div class="col-md-3" style="margin-left:135px;">
													<div class="form-group">
														<label class="col-sm-2" style="padding-top: 2.5px;">IP地址</label>
														<div class="col-sm-8">																												
															 <span class="graytxt">${sers.ip }</span> 
														</div>
													</div>
												</div>
												<div class="col-md-3" style="margin-left:-80px;">
													<div class="form-group">
														<label class="col-sm-2" style="padding-top: 2.5px;">主机名</label>
														<div class="col-sm-8"> 														
															 <span class="graytxt">${sers.name }</span> 
														</div>
													</div>
												</div>
												<div class="col-md-3" style="margin-left:-80px;">
													<div class="form-group">
														<label class="col-sm-3" style="padding-top: 2.5px;">Profile类型</label>
														<div class="col-sm-8">														
															<span class="graytxt">
																<c:if test="${sers.profiletype eq 'cell'}">DMGR+AppServer</c:if>
																<c:if test="${sers.profiletype eq 'default'}">AppServer</c:if>
															</span>
														</div>
													</div>
												</div>
												<div class="col-md-3" style="margin-left:-30px;">
													<div class="form-group">
														<label class="col-sm-3" style="padding-top: 2.5px;">Profile名称</label>
														<div class="col-sm-8">														
															 <span class="graytxt" >${sers.profilename }</span> 
														</div>
													</div>
												</div>
											</div>
											</c:forEach>	
                                       											
		  									<div class="control-group">
												<label  class="control-label">Profile路径</label>
												<div class="controls">
													<div class="inputb2l" style="margin-left:-10px;">
														<span class="graytxt">${was_profile_path }</span>
													</div>
												</div>
											</div>											
																																	
		  									<div class="control-group">
												<label  class="control-label">管理员用户</label>
												<div class="controls">
													<div class="inputb2l" style="margin-left:-10px;">
														<span class="graytxt">${was_userid }</span>
													</div>
													<div class="inputb2l" style="margin-left:-140px;">
														<span  class="input140 mr20">管理员密码</span> 
														<span class="graytxt">${was_password }</span>
													</div>
												</div>
											</div>																																  									  									  									 									  
									</div>																																																																						
							</div>

								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>系统属性
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">打开文件数限制</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${was_nofile }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">用户创建进程数限制</span> 
													<span class="graytxt">${was_nproc }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">文件大小限制</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${was_fsize }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">Coredump大小限制</span> 
													<span class="graytxt">${was_core }</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--content end-->
	<!--footer start-->
	<div class="columnfoot" style="width: 93%; left: 5%;">
		<a class="btn btn-info btn-up" onclick="javascript:history.go(-1);">
			<i class="icon-btn-up"></i> <span>上一页</span>
		</a> <a class="btn btn-info fr btn-down" onclick="CheckInput();"> <span>创建</span>
			<i class="icon-btn-next"></i>
		</a>
	</div>
</body>
</html>
