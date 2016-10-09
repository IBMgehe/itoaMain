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
.input140,label {
	font-size: 13px;
}
body{
	overflow:hidden;
}
input[type="text"] {
	height:28px;
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

<script>
	/* 提取sweet提示框代码，以便后面方便使用，减少代码行数 */ 
	function sweet(te,ty,conBut)
	{
		swal({ title: "", text: te,  type: ty, confirmButtonText: conBut, });
	}
</script>

<script type="text/javascript">
	function CheckInput() 
	{
		if($("#ihs_version").val()== -1)
		{
			sweet("请选择IHS安装版本!","warning","确定");
			return;
		}
		$("#submits").submit();
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
								<p class="twotit" style="padding-left: 15px;">
									<em class="majornode">单</em>节点1
								</p>
								<c:forEach items="${servers }" var="ser" varStatus="num" begin="0" end="0">
									<div class="column" style="margin-left: 15px;">
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
												<b>状态:</b><span class="column_txt"><em>${ser.status }</em></span>
											</p>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
						
						<form action="toIhsNextPage.do?serId=${serId}&platform=${platform}&type=${ptype}&status=confirm" 
						      method="post" id="submits" name="submits" class="form-horizontal">
						    <input type="hidden" id="serId" name="serId" value="${serId}">
							<input type="hidden" id="ptype" name="ptype" value="${ptype}">
							<input type="hidden" id="hostId" name="hostId" value="${hostId}">
							<input type="hidden" id="platform" name="platform" value="${platform}">
							<input type="hidden" id="ihsversion" name="ihsversion">
							
							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>基本信息
								</h5>
								<div class="form-horizontal">
									<c:forEach items="${servers }" var="ser">
									<div class="control-group">
										<label class="control-label">主机名</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="all_hostnames" name="all_hostnames" value="${ser.name }" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">IP地址</span> 
												<input type="text" class="w45" id="all_ips" name="all_ips" value="${ser.ip}" />
											</div>
										</div>
									</div>
									</c:forEach>
									
									<div class="control-group">
										<label class="control-label">IHS安装版本</label>
										<div class="controls">
											<div class="inputb2l">
												<select style="width: 47.5%; font-size: 13px;"
													id="ihs_version" class="w48" name="ihs_version" onchange="showfix(this);">
													<option value="-1" selected="selected">请选择...</option>		
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">IHS安装补丁</span> 
												<select style="width: 47.5%; font-size: 13px;" id="ihs_fp"
													class="w48" name="ihs_fp" onchange="getVer(this)">
													<option value="-1" selected="selected">请选择...</option>
												</select> 
												<input type="hidden" id="ihsfix" name="ihsfix" value="-">
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">安装用户</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="ihs_user"
													   name="ihs_user" value="root" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">IM安装路径</span> 
												<input type="text" class="w45" id="ihs_im_path" 
													name="ihs_im_path" value="/opt/IBM/InstallationManager" />
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">IHS安装路径</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="ihs_inst_path" 
													   name="ihs_inst_path" value="/opt/IBM/HTTPServer" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">Plugin安装路径</span> 
												<input type="text" class="w45" id="ihs_plu_path" 
													name="ihs_plu_path" value="/opt/IBM/WebSphere/Plugins" />
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
												<input type="text" class="w45" id="ihs_nofile"
													name="ihs_nofile" value="20480" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">用户创建进程数限制</span> 
												<input type="text" class="w45" id="ihs_nproc"
													name="ihs_nproc" value="10240" />
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">文件大小限制</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="ihs_fsize"
													name="ihs_fsize" value="unlimited" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">Coredump大小限制</span> 
												<input type="text" class="w45" id="ihs_core"
													name="ihs_core" value="unlimited" />
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
		</a> <a class="btn btn-info fr btn-down" onclick="CheckInput();"> 
			<span>下一页</span><i class="icon-btn-next"></i>
		</a>
	</div>
	<!--footer end-->

	<script type="text/javascript">
		$(document).ready(function() 
		{
			$("#ihs_user").blur(function() 
			{
				if ($("#ihs_user").val() != 'root') 
				{
					$("#ihs_im_path").val('/home/' + $("#ihs_user").val() + '/IBM/InstallationManager');
					$("#ihs_inst_path").val('/home/' + $("#ihs_user").val() + '/opt/IBM/HTTPServer');
					$("#ihs_plu_path").val('/home/' + $("#ihs_user").val() + '/opt/IBM/WebSphere/Plugins');
				}
				if ($("#ihs_user").val() == 'root') 
				{
					$("#ihs_im_path").val('/opt/IBM/InstallationManager');
					$("#ihs_inst_path").val('/opt/IBM/HTTPServer');
					$("#ihs_plu_path").val('/opt/IBM/WebSphere/Plugins');
			    }
			});
		});
	</script>
	
	<script type="text/javascript">
		//显示ihs版本补丁
		function showfix(obj) 
		{
			var ver = obj.value;//获取版本
			var checkText=$(obj).find("option:selected").text();
			var platform1=$('#platform').val();
			$.ajax({
				url : "<%=path%>/getIhsFix.do",
				data : { version : checkText,platform : platform1, pName:'ihs'},
				type : 'post',
				dataType : 'json',
				success : function(result) 
				{
					$("#ihs_fp").empty();
					$("#ihsfix").val('-');
					if(result.length==0)
					{
						var str="<option value='-'>"+ "请选择..." + "</option>";
						$("#ihs_fp").append(str);
						$("#ihsversion").val(checkText=$(obj).find("option:selected").text());
						return;
					}	
					var str = "<option value='-'>" + "请选择..." + "</option>";
					for (var i = 0; i < result.length; i++) 
					{										
						for ( var key in result[i])
						{
							str += "<option value='" + result[i][key] + "'>" + key+ "</option>";
						}							
					}
					$("#ihs_fp").append(str);
					$("#ihsversion").val(checkText=$(obj).find("option:selected").text());
				}
			});
		}

		//自动加载
		function getVer(obj) 
		{
			if ($("#ihs_fp").find("option:selected").text() == '请选择...') 
			{
				$("#ihsfix").val('-');
				$("ihs_fp").val('-');
			} 
			else
				$("#ihsfix").val($("#ihs_fp").find("option:selected").text());
		}
		
		window.onload = function() {
			var platform1=$('#platform').val();
			$.ajax({
				url : '<%=path%>/getIhsVerion1.do',
				data : {platform : platform1,pName : 'ihs'},
				type : 'post',
				dataType : 'json',
				success : function(result) 
				{
					var str;
					for (var i = 0; i < result.length; i++) 
					{
						for ( var key in result[i])
						{
							str += "<option value='" + result[i][key] + "'>" + key+ "</option>";}
						}
					$("#ihs_version").append(str);
				},failure:function(err){
					alert(err);
				}
			})
		}
	</script>
</body>
</html>
