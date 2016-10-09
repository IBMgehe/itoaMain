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
		if($("#was_version").val()== -1)
		{
			sweet("请选择WAS安装版本!","warning","确定");
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
			<a href="getAllServers.do" class="current1"><i class="icon-home"></i>IBM WAS</a> 
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
						
						<form action="toWasNextPage.do?serId=${serId}&platform=${platform}&type=${ptype}&status=confirm" 
						      method="post" id="submits" name="submits" class="form-horizontal">
						    <input type="hidden" id="serId" name="serId" value="${serId}">
							<input type="hidden" id="ptype" name="ptype" value="${ptype}">
							<input type="hidden" id="hostId" name="hostId" value="${hostId}">
							<input type="hidden" id="platform" name="platform" value="${platform}">
							<input type="hidden" id="wasversion" name="wasversion">
							
							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>基本信息
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">安装用户</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="was_user" name="was_user" value="root" />
											</div>
											<div class="inputb2l">
											<span class="input140 mr20">WAS安装路径</span> 
												<input type="text" class="w45" id="was_inst_path"
													name="was_inst_path" value="/opt/IBM/WebSphere/AppServer" />
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">WAS安装版本</label>
										<div class="controls">
											<div class="inputb2l">
												<select style="width: 47.5%; font-size: 13px;"
													id="was_version" class="w48" name="was_version" onchange="showfix(this);">
													<option value="-1" selected="selected">请选择...</option>													
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">WAS安装补丁</span> 
												<select style="width: 47.5%; font-size: 13px;" id="was_fp"
													class="w48" name="was_fp" onchange="getVer(this)">
													<option value="-1" selected="selected">请选择...</option>
												</select> 
												<input type="hidden" id="wasfix" name="wasfix" value="-">
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">主机名</label>
										<div class="controls">
											<div class="inputb2l">
												<c:forEach items="${servers }" var="ser">
													<input type="text" class="w45" id="all_hostnames"
													       name="all_hostnames" value="${ser.name  }" />
													<span style="font-size: 13px; color: red;">&nbsp;&nbsp;*注：若编辑可修改主机名</span>
												</c:forEach>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">IM安装路径</span> 
												<input type="text" class="w45" id="was_im_path"
													name="was_im_path" value="/opt/IBM/InstallationManager" />
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
								    <c:forEach items="${servers }" var="ser">
									<div class="control-group">
										<label class="control-label">IP地址</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="all_ips" name="all_ips"
													readonly="readonly" value="${ser.ip }" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">Profile类型</span>
												<select name="all_profile_types" class="all_profile_class" style="width: 47.5%; font-size: 13px;">													
													<option value="default" selected="selected">AppServer</option>
													<option value="cell">Dmgr + AppServer </option>
												</select> 
											</div>
										</div>
									</div>									
									
									<div class="control-group">
										<label class="control-label">Profile名称</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="all_profile_names"
													name="all_profile_names" value="AppSrv01" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">Profile路径</span> 
												<input type="text" class="w45" id="was_profile_path" 
													name="was_profile_path" value="/opt/IBM/WebSphere/AppServer/profiles" />
											</div>
										</div>
									</div>
									</c:forEach>

									<div class="control-group">
										<label class="control-label">管理员用户</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="was_userid"
													name="was_userid" value="admin" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">管理员密码</span> 
												<input type="text" class="w45" id="was_password" name="was_password"
													value="admin" />
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
												<input type="text" class="w45" id="was_nofile"
													name="was_nofile" value="20480" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">用户创建进程数限制</span> 
												<input type="text" class="w45" id="was_nproc"
													name="was_nproc" value="10240" />
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">文件大小限制</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="was_fsize"
													name="was_fsize" value="unlimited" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">Coredump大小限制</span> 
												<input type="text" class="w45" id="was_core"
													name="was_core" value="unlimited" />
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
		</a> <a class="btn btn-info fr btn-down" onclick="CheckInput();"> <span>下一页</span>
			<i class="icon-btn-next"></i>
		</a>
	</div>
	<!--footer end-->

	<script type="text/javascript">
		$(document).ready(function() 
		{
			$("#was_user").blur(function() 
			{
				if ($("#was_user").val() != 'root') 
				{
					$("#was_im_path").val('/home/' + $("#was_user").val() + '/IBM/InstallationManager');
					$("#was_inst_path").val('/home/'+ $("#was_user").val()+'/IBM/WebSphere/AppServer');
					$("#was_profile_path").val('/home/'+$('#was_user').val() + '/IBM/WebSphere/AppServer/profiles');
				}
				if ($("#was_user").val() == 'root') 
				{
					$("#was_im_path").val('/opt/IBM/InstallationManager');
					$("#was_inst_path").val('/opt/IBM/WebSphere/AppServer');
					$("#was_profile_path").val('/opt/IBM/WebSphere/AppServer/profiles');
			    }
			})
		});
	</script>
	
	<script type="text/javascript">
		//显示was版本补丁
		function showfix(obj) 
		{	
			var ver = obj.value;//获取版本
			var checkText=$(obj).find("option:selected").text();
			var platform1=$('#platform').val();
			$.ajax({
				url : "<%=path%>/getWasFix.do",
				data : { version : checkText,platform : platform1, pName:'was'},
				type : 'post',
				dataType : 'json',
				success : function(result) 
				{
					$("#was_fp").empty();
					$("#wasfix").val('-');
					if(result.length==0)
					{
						var str="<option value='-'>"+ "请选择..." + "</option>";
						$("#was_fp").append(str);
						$("#wasversion").val(checkText=$(obj).find("option:selected").text());
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
					$("#was_fp").append(str);
					$("#wasversion").val(checkText=$(obj).find("option:selected").text());
				}
			});
		}

		//自动加载
		function getVer(obj) 
		{
			if ($("#was_fp").find("option:selected").text() == '请选择...') 
			{
				$("#wasfix").val('-');
				$("was_fp").val('-');
			} 
			else
				$("#wasfix").val($("#was_fp").find("option:selected").text());
		}
		
		window.onload = function() {
			var platform1=$('#platform').val();
			$.ajax({
				url : '<%=path%>/getWasVerion1.do',
				data : {platform : platform1,pName : 'was'},
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
					$("#was_version").append(str);
				},failure:function(err){
					alert(err);
				}
			})
		}
	</script>
	
</body>
</html>
