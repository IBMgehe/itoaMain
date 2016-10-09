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
.input140,.graytxt{
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
</style>

<script language="javascript" type="text/javascript">
	//操作
	function CheckInput() 
	{
		swal({
            title: "",
            text: "是否确认要在目标主机立即执行任务？",
            type: "warning",
            showCancelButton: true,
            closeOnConfirm: false,
            closeOnCancel: false,
            confirmButtonText: "是",
            cancelButtonText: "否",  
            confirmButtonColor: "#ec6c62"
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
			<a href="getAllServers.do" class="current1"><i class="icon-home"></i>IBM IHS</a> 
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
							<p class="twotit" style="padding-left:15px;">
								<em class="majornode">单</em>节点1
							</p>
							<c:forEach items="${servers }" var="ser" varStatus="num">
								<div class="column" style="margin-left: 15px;">
									<div class="span12">
										<p>
											<b>主机名:</b> <span class="column_txt"> ${ser.name } </span> 
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<b>IP地址:</b><span class="column_txt">${ser.ip}</span>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
											<b>操作系统:</b><span class="column_txt"><em>${ser.os}</em></span>
										</p>
										<p>
											<b>系统配置:</b><span class="column_txt">${ser.hconf }</span> 
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
											<b>状态:</b><span class="column_txt"><em>${ser.status }</em></span>
										</p>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
						
						<form action="installIhs.do?serId=${serId}&platform=${platform}" 
						      method="post" id="submits" class="form-horizontal">
							<div class="form-horizontal">
							    <input type="hidden" id="ptype" name="ptype" value="${ptype}">
							    <input type="hidden" id="hostId" name="hostId" value="${hostId}">
							    <input type="hidden" id="serId" name="serId" value="${serId}">
							    <input type="hidden" id="ihsfix" name="ihsfix" value="${ihsfix}">
							    <input type="hidden" id="platform" name="platform" value="${platform}">						    
							    <input type="hidden" id="ihs_binary" name="ihs_binary" value="${ihs_binary}">
							    <input type="hidden" id="ihs_im" name="ihs_im" value="${ihs_im}">
							    
							    <input type="hidden" id="ihs_user" name="ihs_user" value="${ihs_user}">
							    <input type="hidden" id="ihs_fp" name="ihs_fp" value="${ihs_fp}">
							    <input type="hidden" id="ihs_version" name="ihs_version" value="${ihs_version}">
							    <input type="hidden" id="ihs_inst_path" name="ihs_inst_path" value="${ihs_inst_path}"> 
							    <input type="hidden" id="ihs_im_path" name="ihs_im_path" value="${ihs_im_path}">
							    <input type="hidden" id="ihs_plu_path" name="ihs_plu_path" value="${ihs_plu_path}">
							    							    							    
								<input type="hidden" id="all_hostnames" name="all_hostnames" value="${all_hostnames}"> 																				 								
								<input type="hidden" id="all_ips" name="all_ips" value="${all_ips}">
	
							 	<input type="hidden" id="ihs_fsize" name="ihs_fsize" value="${ihs_fsize }">
							 	<input type="hidden" id="ihs_nofile" name="ihs_nofile" value="${ihs_nofile }">
							 	<input type="hidden" id="ihs_core" name="ihs_core" value="${ihs_core }">
							 	<input type="hidden" id="ihs_nproc" name="ihs_nproc" value="${ihs_nproc }">
							 
								<input type="hidden" id="allservers" name="allservers" value="${allservers }">
								<input type="hidden" id="servers" name="servers" value="${servers}">
								
								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>IHS 基本信息
									</h5>
									<div class="form-horizontal">
										<c:forEach items="${allservers }" var="sers">
										<div class="control-group">
											<label class="control-label">主机名</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${sers.name }</span>
												</div>												
												<div class="inputb2l">
													<span class="input140 mr20">IP地址</span> 
													<span class="graytxt">${sers.ip }</span>
												</div>
											</div>
										</div>
										</c:forEach>
										<div class="control-group">
											<label class="control-label">IHS安装版本</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ihs_version }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">IHS安装补丁</span> 
													<span class="graytxt">${ihsfix }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">安装用户</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ihs_user }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">IM安装路径</span> 
													<span class="graytxt">${ihs_im_path }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">IHS安装路径</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ihs_inst_path }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">Plugin安装路径</span> 
													<span class="graytxt">${ihs_plu_path }</span>
												</div>
											</div>
										</div>										
									</div>
								</div>

								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>IHS 系统属性
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">打开文件数限制</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ihs_nofile }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">用户创建进程数限制</span> 
													<span class="graytxt">${ihs_nproc }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">文件大小限制</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ihs_fsize }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">Coredump大小限制</span> 
													<span class="graytxt">${ihs_core }</span>
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
