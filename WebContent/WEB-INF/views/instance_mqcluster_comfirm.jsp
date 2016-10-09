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
label,.input140,.graytxt,.mr20{
	font-size: 13px;
}
.one {
	display: none;
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
.inputb17{
	width:17%;display:inline-block;
}
.inputb22{
	width:22%;display:inline-block;
}
.inputb20{
	width:20%;display:inline-block;
}
.inputb23{
	width:23%;display:inline-block;
}
.inputb15{
	width:15%;display:inline-block;
}
.current1,.current1:hover {
    color: #444444;
}
.tooltip-inner {
	color: black;
	background-color: #FFB073;
}
.tooltip.right .tooltip-arrow {
	border-right-color: #FFB073;
}
.sweet-alert button.cancel { 
	background-color: #ec6c62; 
}
.sweet-alert button.cancel:hover { 
	background-color: rgb(229,97,88); 
}
</style>

<script type="text/javascript">
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
			<a href="getAllServers.do" class="current1"><i class="icon-home"></i>IBM MQCluster</a> 
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
									<p class="twotit" style="padding-left: 15px;">
										节点<c:out value="${num.count }" />
									</p>
									<div class="column" style="margin-left:15px;">
										<div class="span12">
											<p>
												<b>主机名:</b><span class="column_txt"> ${ser.name } </span>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<b>IP地址:</b><span class="column_txt">${ser.ip}</span>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<b>操作系统:</b><span class="column_txt"><em>${ser.os}</em></span>
											</p>
											<p>
												<b>系统配置:</b><span class="column_txt">${ser.hconf }</span>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<b>状态:</b><span class="column_txt"><em>${ser.status }</em></span>
											</p>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>

						<form action="installMqClusterInfo.do?serId=${serId}&platform=${platform}" method="post" id="submits"
							class="form-horizontal">
							<div class="form-horizontal">
							<input type="hidden" id="mqtotalFileName" name="mqtotalFileName" value=${mqtotalFileName }>
							<input type="hidden" id="ptype" name="ptype" value="${ptype}">
							<input type="hidden" id="hostId" name="hostId" value="${hostId}">
							<input type="hidden" id="serId" name="serId" value="${serId}">
							<input type="hidden" id="mqfix" name="mqfix" value="${mqfix}"> 
							<input type="hidden" id="platform" name="platform" value="${platform}"> 
							<input type="hidden" id="mq_version" name="mq_version" value="${mq_version}">
							<input type="hidden" id="mq_fp" name="mq_fp" value="${mq_fp}">
							<input type="hidden" id="mq_inst_path" name="mq_inst_path" value="${mq_inst_path}"> 
							<input type="hidden" id="mq_user" name="mq_user" value="${mq_user}"> 
							<input type="hidden" id="mq_cluster" name="mq_cluster" value="${mq_cluster }" /> 
							<input type="hidden" id="qmgr_method" name="qmgr_method" value="${qmgr_method}">
							<input type="hidden" id="all_hostnames" name="all_hostnames" value="${all_hostnames}"> 
							<input type="hidden" id="all_ips" name="all_ips" value="${all_ips}"> 
							<input type="hidden" id="all_qmgrnames" name="all_qmgrnames" value="${all_qmgrnames}"> 
							<input type="hidden" id="all_completesaves" name="all_completesaves" value="${all_completesaves}"> 
							<input type="hidden" id="mq_mon_port" name="mq_mon_port" value="${mq_mon_port}">
							<input type="hidden" id="mq_qmgr_plog" name="mq_qmgr_plog" value="${mq_qmgr_plog}"> 
							<input type="hidden" id="mq_data_path" name="mq_data_path" value="${mq_data_path}">
							<input type="hidden" id="mq_qmgr_slog" name="mq_qmgr_slog" value="${mq_qmgr_slog}"> 
							<input type="hidden" id="mq_log_path" name="mq_log_path" value="${mq_log_path}">
							<input type="hidden" id="mq_log_psize" name="mq_log_psize" value="${mq_log_psize}"> 
							<input type="hidden" id="mq_chl_kalive" name="mq_chl_kalive" value="${mq_chl_kalive}"> 
							<input type="hidden" id="mq_chl_max" name="mq_chl_max" value="${mq_chl_max}">
							<input type="hidden" id="allservers" name="allservers" value="${allservers }"> 
							<input type="hidden" id="lin_semmsl" name="lin_semmsl" value="${lin_semmsl}">
							<input type="hidden" id="lin_semmns" name="lin_semmns" value="${lin_semmns}"> 
							<input type="hidden" id="lin_semopm" name="lin_semopm" value="${lin_semopm}">
							<input type="hidden" id="lin_semmni" name="lin_semmni" value="${lin_semmni}"> 
							<input type="hidden" id="lin_shmax" name="lin_shmax" value="${lin_shmax}"> 
							<input type="hidden" id="lin_shmni" name="lin_shmni" value="${lin_shmni}"> 
							<input type="hidden" id="lin_shmall" name="lin_shmall" value="${lin_shmall}">
							<input type="hidden" id="lin_filemax" name="lin_filemax" value="${lin_filemax}"> 
							<input type="hidden" id="lin_nofile" name="lin_nofile" value="${lin_nofile}">
							<input type="hidden" id="lin_nproc" name="lin_nproc" value="${lin_nproc}">
							<%-- <input type="hidden" id="lin_tcptime" name="lin_tcptime" value="${lin_tcptime}"> --%>
							<input type="hidden" id="mq_lstr_port" name="mq_lstr_port" value="${mq_lstr_port}"> 
							<input type="hidden" id="allservers" name="allservers" value="${allservers}">
							<input type="hidden" id="servers" name="servers" value="${servers}"> 
							<input type="hidden" id="allservertotaljson" name="allservertotaljson" value="${allservertotaljson}">	
							<input type="hidden" id="aix_maxuproc" name="aix_maxuproc" value="${aix_maxuproc}">
							<input type="hidden" id="aix_nofiles" name="aix_nofiles" value="${aix_nofiles}">									
							<input type="hidden" id="aix_data" name="aix_data" value="${aix_data}">
							<input type="hidden" id="aix_stack" name="aix_stack" value="${aix_stack}">
									
								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>基本属性
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">MQ安装版本</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${mq_version }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">MQ安装补丁</span> 
													<span class="graytxt">${mqfix }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">MQ安装路径</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${mq_inst_path }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">MQ管理用户</span> 
													<span class="graytxt">${mq_user }</span>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>队列管理器属性
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">队列管理器创建方式</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${qmgr_method }</span>
												</div>
												<c:if test="${qmgr_method == 'yes' }">
												<div class="inputb2l">
													<span class="input140 mr20">集群名</span> 
													<span class="graytxt">${mq_cluster }</span>
												</div>
												</c:if>
											</div>
										</div>
										
										<c:forEach items="${allservers }" var="sers">
											<div class="control-group">
												<label class="control-label">主机名</label>
												<div class="controls">
													<div class="inputb17">
														<span class="graytxt">${sers.name }</span>
													</div>	
													<div class="inputb22">
														<span class="mr20 ">IP地址</span> 
														<span class="graytxt">${sers.ip }</span>
													</div>									
								 					<div class="inputb20">
														<span class="mr20 ">QMGR名称</span> 
														<span class="graytxt">${sers.qmgrname }</span>
													</div>
													<div class="inputb23">
														<span class="mr20 ">完全存储库</span> 
														<span class="graytxt">${sers.completesave }</span>
													</div>												
												</div>
											</div>
										</c:forEach>
										
										<c:if test="${qmgr_method eq 'yes' }">
											<div class="control-group">
												<label class="control-label">监听端口</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${mq_lstr_port }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">主日志文件数</span> 
														<span class="graytxt">${mq_qmgr_plog }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">QMGR数据路径</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${mq_data_path }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">次日志文件数</span> 
														<span class="graytxt">${mq_qmgr_slog }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">QMGR日志路径</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${mq_log_path }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">日志文件页数(4KB)</span> 
														<span class="graytxt">${mq_log_psize }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">通道连接是否保持</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${mq_chl_kalive }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">最大通道数</span> 
														<span class="graytxt">${mq_chl_max }</span>
													</div>
												</div>
											</div>
										</c:if>
									</div>
								</div>

								<c:if test="${platform=='linux' }">
									<div class="mainmodule">
										<h5 class="swapcon">
											<i class="icon-chevron-down"></i>系统属性
										</h5>
										<div class="form-horizontal">
											<div class="control-group">
												<label class="control-label">semmsl</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${lin_semmsl }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">semmns</span> 
														<span class="graytxt">${lin_semmns }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">semopm</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${lin_semopm }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">semmni</span> 
														<span class="graytxt">${lin_semmni }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">shmmax</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${lin_shmax }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">shmmni</span> 
														<span class="graytxt">${lin_shmni }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">shmall</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${lin_shmall }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">file-max</span> 
														<span class="graytxt">${lin_filemax }</span>
													</div>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label">nofile</label>
												<div class="controls">
													<div class="inputb2l">
														<span class="graytxt">${lin_nofile }</span>
													</div>
													<div class="inputb2l">
														<span class="input140 mr20">nproc</span> 
														<span class="graytxt">${lin_nproc }</span>
													</div>
												</div>
											</div>

										</div>
									</div>
								</c:if>
							</div>
							
							<c:if test="${platform =='aix'}">
							  <div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>系统属性
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">maxuproc</label>
										<div class="controls">
											<div class="inputb2l">
												<span class="graytxt">${aix_maxuproc }</span>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">nofiles</span> 
												<span class="graytxt">${aix_nofiles }</span>
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">data</label>
										<div class="controls">
											<div class="inputb2l">
												<span class="graytxt">${aix_data }</span>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">stack</span> 
												<span class="graytxt">${aix_stack }</span>
											</div>
										</div>
									</div>
								</div>
							</div>
								
						</c:if>
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
