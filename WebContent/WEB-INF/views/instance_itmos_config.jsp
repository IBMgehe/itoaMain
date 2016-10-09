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
</head>
<style type="text/css">
body{ 
	overflow:hidden; 
}
label,.input140,.mr20{
	font-size:13px;
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
label > div.radio {
    position: relative;
    top:4px;
}
.current1,.current1:hover {
    color: #444444;
}
</style>

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
						
						<form action="toItmOsNextPage.do?serId=${serId}&platform=${platform}&type=${ptype}&status=confirm" 
						      method="post" id="submits" name="submits" class="form-horizontal">
							<input type="hidden" id="serId" name="serId" value="${serId}">
							<input type="hidden" id="ptype" name="ptype" value="${ptype}">
							<input type="hidden" id="hostId" name="hostId" value="${hostId}">
							<input type="hidden" id="platform" name="platform" value="${platform}">
							<input type="hidden" id="itmosversion" name="itmosversion">
							
							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>基本信息
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">产品类型</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="itm_type"  readonly="readonly"
													   name="itm_type" value="OS Agent" />
											</div>										
											<div class="inputb2l">
												<span class="input140 mr20">软件版本</span> 
												<select style="width: 47.5%; font-size: 13px;" class="w48"
													    id="itm_version"  name="itm_version" 
													    onchange="showfix(this);">
													<option value="-1" selected="selected">请选择...</option>
												</select>											
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">产品代码</label>
										<div class="controls">
											<div class="inputb2l">
											    <c:if test="${platform eq 'linux' }">
											    <input type="text" class="w45" id="itm_code"  readonly="readonly" name="itm_code" value="lz" />
												
												</c:if>
												<c:if test="${platform eq 'aix' }">
												 <input type="text" class="w45" id="itm_code"  readonly="readonly" name="itm_code" value="ux" />
												</c:if>
											</div>										
											<div class="inputb2l">
												<span class="input140 mr20">补丁版本</span> 
												<select style="width: 47.5%; font-size: 13px;" id="itm_fp"
													class="w48" name="itm_fp" onchange="getVer(this)">
													<option value="-">请选择...</option>
												</select> 
												<input type="hidden" id="itmosfix" name="itmosfix" value="-">
											</div>
										</div>
									</div>

									<div class="control-group">
										<label class="control-label">管理用户</label>
										<div class="controls">
											<div class="inputb2l" >
												<input type="text" class="w45" id="itm_manager_user"  
													   name="itm_manager_user" value="tivoli" />
											</div>										
											<div class="inputb2l">
												<span class="input140 mr20">部署路径</span> 
												<input type="text" class="w45" id="itm_inst_path" 
												       name="itm_inst_path" value="/opt/IBM/ITM" />
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">管理用户属组</label>
										<div class="controls">
											<div class="inputb2l" >
												<input type="text" class="w45" id="itm_manager_group"  
													   name="itm_manager_group" value="tivoligrp" />
											</div>										
										</div>
									</div>
								</div>
							</div>

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>配置信息
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">是否主备TEMS</label>
										<div class="controls">
											<div class="inputb2l">
												<div class="inblock mr20">
													<label><input type="radio" name="itm_ha_tems" value="yes" onClick="sortRadio1()" />是</label>
												</div>
												<div class="inblock mr20">
													<label><input type="radio" name="itm_ha_tems" value="no" onClick="sortRadio2()" checked />否</label>
												</div>
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">主TEMS IP</label>
										<div class="controls ">
											<div class="inputb28">
												<input type="text" class="w45" id="pri_tems"  
													   name="pri_tems" value="" />
											</div>	
											<div class="inputb30">
												<span class="mr20 ">网络协议</span> 
												<select name="pri_protocol" class="pri_protocol" style="width:46%;">
													<option value="IP.PIPE" selected="selected">IP.PIPE</option>
													<option value="TCP/IP">TCP/IP</option>
												</select>
											</div>									
											<div class="inputb29">
												<span class="mr20 ">连接端口</span> 
												<input type="text" class="w45" id="pri_port" 
												       name="pri_port" value="1918" />
											</div>
										</div>
									</div> 
									
									<div class="control-group min" style="display:none;">
										<label class="control-label">备TEMS IP</label>
										<div class="controls ">
											<div class="inputb28">
												<input type="text" class="w45" id="sec_tems"
													   name="sec_tems" value="" />
											</div>	
											<div class="inputb30">
												<span class="mr20 ">网络协议</span> 
												<select name="sec_protocol" class="sec_protocol" style="width:46%;">
													<option value="IP.PIPE" selected="selected">IP.PIPE</option>
													<option value="TCP/IP">TCP/IP</option>
												</select>
											</div>									
											<div class="inputb29">
												<span class="mr20 ">连接端口</span> 
												<input type="text" class="w45" id="sec_port" 
												       name="sec_port" value="1918" />
											</div>
										</div>
									</div>	
									
									<c:forEach items="${servers }" var="ser">
									<div class="control-group">
										<label class="control-label">主机名</label>
										<div class="controls ">
											<div class="inputb29">
												<input type="text" class="w45" readonly="readonly"
													   name="all_hostnames" value="${ser.name }" />
											</div>	
											<div class="inputb29">
												<span class="mr20 ">IP地址</span> 
												&nbsp;
												<input type="text" class="w45" readonly="readonly"
												       name="all_ips" value="${ser.ip }" />
											</div>									
											<div class="inputb29">
												<span class="mr20 ">TEPS名称</span> 
												<input type="text" class="w45 teps_name" 
												       name="all_teps_names" value="${ser.name }" />
											</div>
										</div>
									</div>
									</c:forEach>							  							 																																				
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
		<a class="btn btn-info fr btn-down" onclick="CheckInput();"> <span>下一页</span>
			<i class="icon-btn-next"></i>
		</a>
	</div>
	<!--footer end-->
	
<script>
	/* 提取sweet提示框代码，以便后面方便使用，减少代码行数 */ 
	function sweet(te,ty,conBut)
	{
		swal({ title: "", text: te,  type: ty, confirmButtonText: conBut, });
	}
</script>		
	
<script type="text/javascript">
    function sortRadio1(){      //按钮"是" 
    	//$(".min").show();  //点击"是"的时候显示备TEMS IP信息 
    	$(".min").css("display","block");
    }
    
    function sortRadio2(){    //按钮"否"   
    	//$(".min").hide();  //点击"否"的时候隐藏备TEMS IP信息 
    	$(".min").css("display","none");
    }
    
    function check_path(){   //检测部署路径的合法性（路径必须以"/"开头） 
    	var install_path = $("#install_path").val().trim();
        var first = install_path.substr(0, 1);  
        if(first != "/"){
        	$(".wh").css("display","block");
        	$("#install_path").val("");
        	return ;
        }
        else{
        	$(".wh").css("display","none");
        }
    }
</script>
    
<script type="text/javascript">
    function CheckInput()
    {	
    	if($("#itm_version").val()== -1)
		{
			sweet("请选择ITM安装版本!","warning","确定");
			return;
		}
    	
    	var install_path = $("#install_path").val();
    	var manager_user = $("#manager_user").val();
    	//var pri_tems = $("#pri_tems").val();
    	//var sec_tems = $("#sec_tems").val(); 
    	var pri_port = $("#pri_port").val();
    	var sec_port = $("#sec_port").val();
		var ha_tems= $("#itm_ha_tems").val();
		
		var pri_tems = $("#pri_tems").val().trim();
    	var sec_tems = $("#sec_tems").val().trim();
    	var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    	
    	if(install_path == "")
    	{
    		sweet("部署路径不能为空!","warning","确定");
    		return ;
    	}
    	if(manager_user == "")
    	{
    		sweet("管理用户不能为空!","warning","确定");
    		return ;
    	}
    	if(pri_tems == "")
    	{ 
    		sweet("主TEMS IP不能为空!","warning","确定");
    		return ;
    	}
    	if(($(".min").css("display")=="block") && sec_tems == "")
    	{
    		sweet("备TEMS IP不能为空!","warning","确定");
    		return ;
    	}
    	if(pri_port == "" || sec_port == "")
    	{
    		sweet("连接端口不能为空!","warning","确定");
    		return ;
    	}
    	
    	if((pri_tems != "") && (!exp.test(pri_tems)))
        {
    		sweet("主IP地址不合法，请重新输入!","warning","确定");
    		$("#pri_tems").val("");
            return ;
        }
    	if(($(".min").css("display")=="block") && (sec_tems != "") && (!exp.test(sec_tems)))
        { 
    		sweet("备IP地址不合法，请重新输入!","warning","确定");
    		$("#sec_tems").val("");
            return ;
        }
    	
    	var numArr = []; // 定义一个空数组
        var txt = $(".teps_name"); //获取所有的TEPS名称文本框 
        for (var i = 0; i < txt.length; i++) {
            numArr.push(txt.eq(i).val()); // 将文本框的值添加到数组中
        }
        for(var j=0;j<numArr.length;j++){
        	if(numArr[j] == "")
        	{
        		sweet("TEPS名称不能为空!","warning","确定");
        		return ;
        	}       		 
        }      
        $("#submits").submit();
    }
</script>
    
<script type="text/javascript">
	//显示os agent版本补丁
	function showfix(obj) 
	{
		var ver = obj.value;//获取版本
		var checkText=$(obj).find("option:selected").text();
		var platform1=$('#platform').val();
		$.ajax({
					url : "<%=path%>/getItmOSFix.do",
					data : { version : checkText,platform : platform1, pName:'itmos'},
					type : 'post',
					dataType : 'json',
					success : function(result) 
					{
						$("#itm_fp").empty();
						$("#itmosfix").val('-');
						if(result.length==0)
						{
							var str="<option value='-'>"+ "请选择..." + "</option>";
							$("#itm_fp").append(str);
							$("#itmosversion").val(checkText=$(obj).find("option:selected").text());
							return;
						}
						var str="<option value='-'>"+ "请选择..." + "</option>";
						for (var i = 0; i < result.length; i++) 
						{										
							for ( var key in result[i])
							{
							str += "<option value='" + result[i][key] + "'>" + key+ "</option>";
							}						
						}
						$("#itm_fp").append(str);
						$("#itmosversion").val(checkText=$(obj).find("option:selected").text());
					}
				});
	}

	//自动加载
	function getVer(obj) 
	{
		if ($("#itm_fp").find("option:selected").text() == '请选择...') 
		{
			$("#itmosfix").val('-');
			$("#itm_fp").val('-');
		} 
		else
			$("#itmosfix").val($("#itm_fp").find("option:selected").text());
	}
	
	window.onload = function() 
	{
		var platform1=$('#platform').val();
		$.ajax({
			url : '<%=path%>/getItmOsVerion1.do',
			data : {platform : platform1,pName : 'itmos'},
			type : 'post',
			dataType : 'json',
			success : function(result) 
			{
				var str;
				for (var i = 0; i < result.length; i++) 
				{
					for ( var key in result[i])
					{
						str += "<option value='" + result[i][key] + "'>" + key+ "</option>";
					}
				}
				$("#itm_version").append(str);
			},failure:function(err){
				alert(err);
			}
		});
	}
</script>
</body>
</html>
