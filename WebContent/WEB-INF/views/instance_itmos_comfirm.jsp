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
.content {
	position:relative;
	float:right;
	width:calc(100% - 57px);
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
}
.one{
	display:none;
}
.wh{
	width:225px;
	line-height:8px;
	float:right;
	display:none;
}
/* new add */
.inputb28{
	width:27.9%;display:inline-block;
}
.inputb29{
	width:29%;display:inline-block;
}
.inputb30{
	width:30.4%;display:inline-block;
}
.sweet-alert button.cancel { 
	background-color: #ec6c62; 
}
.sweet-alert button.cancel:hover { 
	background-color: #E56158; 
}
.current1,.current1:hover {
    color: #444444;
}
</style>

<script type="text/javascript">
	//操作
	function CheckInput() {
/* ymPrompt.win({
			message : '&nbsp;提交任务后是否在目标主机立即运行脚本，创建环境？',
			title : '创建提示！',
			handler : function(tp) {
				if (tp == "no") {
					window.history.go(0);
					var type = document.getElementById("type");
					type.setAttribute("value", "no");
				}
				else{
					$("#submits").submit();
				}
			},
			btn : [ [ '是', 'yes' ], [ '否', 'no' ] ],
			icoCls : 'ymPrompt_alert'
		}); */
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
			<a href="getAllServers.do" class="current1"><i class="icon-home"></i>IBM OS Agent</a> 
			<a class="current">实例配置详细</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="columnauto">
						<div class="mainmodule">     <!--**************** 拓扑结构 ****************-->
						   <h5 class="swapcon">
								<i class="icon-chevron-down icon-chevron-right"></i>拓扑结构
						   </h5>
						   <div class="form-horizontal" style="display:none;">
						   <c:forEach items="${servers }" var="ser" varStatus="num">
							 <p class="twotit" style="padding-left: 15px;">
								节点 <c:out value="${num.count }" />
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
						
						<form action="installItmOsInfo.do?serId=${serId}&platform=${platform}" 
						      method="post" id="submits" name="submits" class="form-horizontal">
							<div class="form-horizontal">
							    <input type="hidden" id="ptype" name="ptype" value="${ptype}">
								<input type="hidden" id="hostId" name="hostId" value="${hostId}">
								<input type="hidden" id="serId" name="serId" value="${serId}">
								<input type="hidden" id="itm_fix" name="itm_fix" value="${itm_fix }" >
								<input type="hidden" id="platform" name="platform" value="${platform}">
								
								<input type="hidden" id="itm_type" name="itm_type" value="${itm_type}"> 
								<input type="hidden" id="itm_version" name="itm_version" value="${itm_version}"> 
								<input type="hidden" id="itm_code" name="itm_code" value="${itm_code}"> 
								<input type="hidden" id="itm_fp" name="itm_fp" value="${itm_fp}"> 
								<input type="hidden" id="itm_binary" name="itm_binary" value="${itm_binary}"> 
								<input type="hidden" id="itm_inst_path" name="itm_inst_path" value="${itm_inst_path}"> 
								<input type="hidden" id="itm_manager_user" name="itm_manager_user" value="${itm_manager_user}"> 
								<input type="hidden" id="itm_manager_group" name="itm_manager_group" value="${itm_manager_group}">
								
								<input type="hidden" id="itm_ha_tems" name="itm_ha_tems" value="${itm_ha_tems}">
								<input type="hidden" id="pri_tems" name="pri_tems" value="${pri_tems}">
								<input type="hidden" id="pri_protocol" name="pri_protocol" value="${pri_protocol}">
								<input type="hidden" id="pri_port" name="pri_port" value="${pri_port}">								
								<input type="hidden" id="sec_tems" name="sec_tems" value="${sec_tems}">
								<input type="hidden" id="sec_protocol" name="sec_protocol" value="${sec_protocol}">
								<input type="hidden" id="sec_port" name="sec_port" value="${sec_port}">
								
								<input type="hidden" id="all_hostnames" name="all_hostnames" value="${all_hostnames}">
								<input type="hidden" id="all_ips" name="all_ips" value="${all_ips}">
								<input type="hidden" id="all_tepsnames" name="all_tepsnames" value="${all_tepsnames}">
								
								<input type="hidden" id="servers" name="servers" value="${servers}"> 
								<input type="hidden" id="allservertotaljson" name="allservertotaljson" value="${allservertotaljson}">								
								
							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>基本信息
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">产品类型</label>
										<div class="controls">
											<div class="inputb2l">											
												<span class="graytxt">${itm_type }</span>
											</div>										
											<div class="inputb2l">
												<span class="input140 mr20">软件版本</span> 
												<span class="graytxt">${itm_version }</span>											
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">产品代码</label>
										<div class="controls">
											<div class="inputb2l">
											    <span class="graytxt">${itm_code }</span>
											</div>										
											<div class="inputb2l">
												<span class="input140 mr20">补丁版本</span>
												<span class="graytxt">${itm_fix }</span> 
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">管理用户</label>
										<div class="controls">
											<div class="inputb2l" >
											    <span class="graytxt">${itm_manager_user }</span> 
											</div>										
											<div class="inputb2l">
												<span class="input140 mr20">部署路径</span> 
												<span class="graytxt">${itm_inst_path }</span> 
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">管理用户属组</label>
										<div class="controls">
											<div class="inputb2l" >
											    <span class="graytxt">${itm_manager_group }</span> 
											</div>										
										</div>
									</div>
								</div>
							</div>

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>配置信息
								</h5>
								<div class="form-horizontal" >
									<div class="control-group">
										<label class="control-label">是否主备TEMS</label>
										<div class="controls">
											<div class="inputb2l">
												<span class="graytxt">${itm_ha_tems }</span>
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">主TEMS IP</label>
										<div class="controls ">
											<div class="inputb28">
												<span class="graytxt">${pri_tems }</span>
											</div>	
											<div class="inputb30">
												<span class="mr20 ">网络协议</span> 
												<span class="graytxt">${pri_protocol }</span>
											</div>									
											<div class="inputb29">
												<span class="mr20 ">连接端口</span> 
												<span class="graytxt">${pri_port }</span>
											</div>
										</div>
									</div> 
								
								<c:if test="${itm_ha_tems eq 'yes' }">	
									<div class="control-group">
										<label class="control-label">备TEMS IP</label>
										<div class="controls ">
											<div class="inputb28">
												<span class="graytxt">${sec_tems }</span>
											</div>	
											<div class="inputb30">
												<span class="mr20 ">网络协议</span> 
												<span class="graytxt">${sec_protocol }</span>
											</div>									
											<div class="inputb29">
												<span class="mr20 ">连接端口</span> 
												<span class="graytxt">${sec_port }</span>
											</div>
										</div>
									</div>
								</c:if>	
								
							<c:forEach items="${allservers }" var="sers">
								<div class="control-group">
									<label class="control-label">主机名</label>
									<div class="controls ">
										<div class="inputb29">
											<span class="graytxt">${sers.name }</span>
										</div>	
										<div class="inputb29">
											<span class="mr20 ">IP地址</span> 
											<span class="graytxt">${sers.ip }</span>
										</div>									
										<div class="inputb29">
											<span class="mr20 ">TEPS名称</span> 
											<span class="graytxt">${sers.tepsname }</span>
										</div>
									</div>
								</div>	
							  </c:forEach>																													
									
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
		</a> 
		<a class="btn btn-info fr btn-down" onclick="CheckInput();"> 
		    <span>创建</span><i class="icon-btn-next"></i>
		</a>
	</div>
	<!--footer end-->		
</body>
</html>
