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
<title>自动化运维平台详细日志展现</title>
<style>
.bcg { background: #00ec00; }
.bcr { background: #ff8080; }
.bcb { background: #8080ff; }
.bcy { background: yellow; }

.input200 {
	display: inline-block;
	width: 200px;
	margin-top: 2px;
	text-align: right;
}
#logTable3 tr{height:25px;}
body{font-size:13px;}
.input140{font-size:13px;}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		giveTdColor1();//主要用于手动页面刷新
		
		//用于获取点击失败、的原因		
		$("#logTable3 tbody").on("click", "tr td", function () {
			var uuid = $("#uuid").val().trim();
			selectedRecord = $(this).html().trim();
			if(selectedRecord=='失败')
			{
			$.ajax({
				url : '<%=path%>/getErrMsg.do?playbook_uuid='+uuid,
				type : 'get',
				dataType : "json",
				error : function(err) {
					//alert(err['errmsg'])
				},
				success:function(data)
				{
					
					ymPrompt.win({
						width: 500, //宽 
						height: 250, //高 
						message : data['errmsg'],
						title : '错误提示！',
						maxBtn:true,
						handler : function(tp) {},
						btn : [ [ '是', 'yes' ]]
					});
				}
			})
		}
		});
	});
</script>

<script type="text/javascript">
function giveTdColor1()
{
	//给td添加颜色
	trObject = $("#logTable3 tbody").find("tr");
	for (var i = 0; i < trObject.length; i++) 
	{
		var tdText = $(trObject[i]).find("td:last");
		if (tdText.html().trim() == "成功") 
		{
			tdText.removeClass();
			tdText.addClass("bcg");
			$("#progress_my").attr("class","progress progress-s progress-striped progress-success ine-block w100");			
		}
		if (tdText.html().trim() == "失败") 
		{
			tdText.removeClass();
			tdText.addClass("bcr");
			$("#progress_my").attr("class","progress progress-s progress-striped progress-danger ine-block w100");
		}
		if (tdText.html().trim() == "运行中") 
		{
			tdText.removeClass();
			tdText.addClass("bcb");
			$("#progress_my").attr("class","progress progress-s progress-striped progress-primary ine-block w100 active");
		}
		if (tdText.html().trim() == "跳过")  
		{
			tdText.removeClass();
			tdText.addClass("bcy");
		}
	}
}

//ajax 更新每个task的状态信息
function giveTdColor(data)
{
	console.log(data);
	//给td添加颜色
	$('#logTable3 tbody tr').each(function()
	{
	   var tdText=	$(this).find("td:last");
	   var tdStep = $(this).find("td:first");
	   var tdStep_next  = $(this).find('td:first').find("input");
	   tdText.text(data[tdStep_next.val()]);
	  // tdText.text(data[tdStep.html().trim()+tdStep_next.html().trim()]);
	  // alert(tdStep_next.val())
	  	
		if (tdText.html().trim() == "成功") 
		{
			tdText.removeClass();
			tdText.addClass("bcg");
			$("#progress_my").attr("class","progress progress-s progress-striped progress-success ine-block w100");				
		}
		if (tdText.html().trim() == "失败") 
		{
			tdText.removeClass();
			tdText.addClass("bcr");
			$("#progress_my").attr("class","progress progress-s progress-striped progress-danger ine-block w100");
		}
		if (tdText.html().trim() == "运行中") 
		{
			tdText.removeClass();
			tdText.addClass("bcb");
			$("#progress_my").attr("class","progress progress-s progress-striped progress-primary ine-block w100 active");
		}			
		if (tdText.html().trim() == "跳过")  
		{
			$(this).remove();//删除跳过的行
			tdText.removeClass();
			tdText.addClass("bcy");
		}
	})
}

function getInstallMsg() 
{
	var uuid = $("#uuid").val().trim();
	var type = $("#type").val().trim();
	$.ajax({
		url : '<%=path%>/nodeInstall.do',
		type : 'post',
		data : {
			uuid:uuid,
			type:type
		},
		dataType : "json",
		error : function(err) {
			//alert("获取安装信息异常！");
		},
		success : function(data) {
			giveTdColor(data);//给TD上颜色	
			$("#progress_my").prev().html("主机安装进度 :&nbsp;&nbsp;&nbsp;" + data.percent);//安装进度
			$(".bar").attr('style', 'width: ' + data.percent + ';') //percent
			$("#percent").val(data.percent);
			$("#status").val(data.status);
		}
	});
}

function myrefresh() 
{
	if (($("#logmsg").attr("class") == 'tabcontent tabnow')) 
	{
		if ($("#status").val().trim() != 2 && $("#status").val().trim() != 3)
		{
			getInstallMsg();			
		}
	}
}   

window.setInterval('myrefresh()',10000);  //每隔10秒自动刷新一次
</script>
</head>

<body>
	<!--header start-->
	<div class="header">
		<jsp:include page="topinfo.jsp" flush="true" />
	</div>
	<!--header end-->

	<!--content start-->
	<div class="content">
		<div class="breadcrumb">
			<a href="getLogInfo.do" title="历史执行日志" class="tip-bottom"><i
				class="icon-home"></i>历史执行日志</a> <a href="#" class="current">任务信息</a>
		</div>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="widget-box collapsible">
						<div class="widget-title">
							<a data-toggle="collapse" href="#collapseOne"> <span
								class="icon"> <i class="icon-arrow-right"></i>
							</span>
								<h5>说明：</h5>
							</a>	
						</div>
						<div id="collapseOne" class="collapse in">
							<div class="widget-content">详细信息.</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="columnauto">
						<div class="tabtitle">
							<ul class="tabnav">
								<li>主机节点</li>
								<li>环境参数</li>
								<li class="active">执行日志</li>
							</ul>
						</div>

						<div class="tabcontent">
								<div class="mainmodule">
									<h5 class="stairtit swapcon">拓扑结构</h5>
									
									<c:forEach items="${servers }" var="ser" varStatus="num">
						                <p class="twotit" style="padding-left: 0px;">
						                                                               节点<c:out  value="${num.count }"/>
						                </p>
						                <div class="column">
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
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<b>状态:</b><span class="column_txt"><em>${ser.status }</em></span>
												</p>
						                  </div>
						                </div>
					                </c:forEach>
								</div>
						</div>

						<div class="tabcontent">
							<div class="mainmodule">							
							    <div class="mainmodule">
					              	<h5 class="swapcon"><i class="icon-chevron-down"></i>Host信息</h5>
					                 <div class="form-horizontal">
					                     <div class="control-group">
											<label class="control-label">HA名称</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${haname }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">资源组主节点</span> 
													<span class="graytxt">${ha_primaryNode }</span>
												</div>
											</div>
										</div>										
										<div class="control-group">
											<label class="control-label">节点1 IP</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ha_ip1 }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">节点1 主机名</span> 
													<span class="graytxt">${ha_hostname1 }</span>
												</div>
											</div>
										</div>										
										<div class="control-group">
											<label class="control-label">节点1 Boot IP</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ha_bootip1 }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">节点1 Boot主机别名</span> 
													<span class="graytxt">${ha_bootalias1 }</span>
												</div>
											</div>
										</div>										
										<div class="control-group">
											<label class="control-label">节点2 IP</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ha_ip2 }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">节点2 主机名</span> 
													<span class="graytxt">${ha_hostname2 }</span>
												</div>
											</div>
										</div>										
										<div class="control-group">
											<label class="control-label">节点2 Boot IP</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ha_bootip2 }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">节点2 Boot主机别名</span> 
													<span class="graytxt">${ha_bootalias2 }</span>
												</div>
											</div>
										</div>										
										<div class="control-group">
											<label class="control-label">HA Service IP</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${ha_svcip }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">Service主机别名</span> 
													<span class="graytxt">${ha_svcalias }</span>
												</div>
											</div>
										</div>										
										<div class="control-group">
											<label class="control-label">子网掩码</label>
											<div class="controls">
												<span class="graytxt">${ha_netmask }</span>
											</div>
										</div>
					                 </div>
					               </div>
					               
					               <div class="mainmodule">
						              	<h5 class="swapcon"><i class="icon-chevron-down icon-chevron-right"></i>VG信息</h5>
						                 <div class="form-horizontal" style="display:none;">
						                     <div class="control-group groupborder divnfs">
												<div class="controls controls-mini" style="margin-left: 0.7%;">
													<span class="input140 mr20" style="width: 142px;">VG名称</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${ha_vgdb2inst }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">包含PV</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_inst_pv }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">VG创建方式</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_insthome }</span>
													</div>
												</div>
											</div>
											
											<div class="control-group groupborder divnfs">
												<div class="controls controls-mini" style="margin-left: 0.7%;">
													<span class="input140 mr20" style="width: 142px;">VG名称</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${ha_vgdb2data }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">包含PV</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_data_pv }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">VG创建方式</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_insthome }</span>
													</div>
												</div>
											</div>
											
											<div class="control-group groupborder divnfs">
												<div class="controls controls-mini" style="margin-left: 0.7%;">
													<span class="input140 mr20" style="width: 142px;">VG名称</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${ha_vgdb2log }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">包含PV</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_log_pv }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">VG创建方式</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_insthome }</span>
													</div>
												</div>
											</div>
											
											<div class="control-group groupborder divnfs">
												<div class="controls controls-mini" style="margin-left: 0.7%;">
													<span class="input140 mr20" style="width: 142px;">VG名称</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${ha_vgdb2archlog }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">包含PV</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_archlog_pv }</span>
													</div>
													<span class="input140 mr20" style="width: 128px;">VG创建方式</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${db2_insthome }</span>
													</div>
												</div>
											</div>
											
											<c:if test="${ha_nfsON eq 'yes' }">
												<div class="controls controls-mini" style="margin-left: 0.7%;">
													<span class="input140 mr20" style="width: 142px;">是否挂载NFS文件系统</span>
													<div class="inputb4" style="width: 20%;">
														<span class="graytxt">${ha_nfsON }</span>
													</div>
												</div>
											</c:if>
						                  </div>
						               </div>						
							
								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down icon-chevron-right"></i>基本信息
									</h5>
									<div class="form-horizontal" style="display: none;">										
										<div class="control-group">
											<label class="control-label">DB2安装版本</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_version }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">DB2版本补丁</span> 
													<span class="graytxt">${db2_binary }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">DB2安装路径</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_db2base }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">DB2实例目录</span> 
													<span class="graytxt">${db2_dbpath }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">DB2实例名</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_db2insusr }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">监听端口</span> 
													<span class="graytxt">${db2_svcename }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">DB2数据库名</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_dbname }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">字符集</span> 
													<span class="graytxt">${db2_codeset }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">DB2数据目录</label>
											<div class="controls">
												<span class="graytxt">${db2_dbdatapath }</span>
											</div>
										</div>
									</div>
								</div>

								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down icon-chevron-right"></i>实例高级属性
									</h5>
									<div class="form-horizontal" style="display: none;">
										<div class="control-group">
											<label class="control-label">实例用户</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_db2insusr }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">实例用户组</span> 
													<span class="graytxt">${db2_db2insgrp }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">fence用户</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_db2fenusr }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">fence用户组</span> 
													<span class="graytxt">${db2_db2fengrp }</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">DB2COMM</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_db2comm }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">DB2CODEPAGE</span> 
													<span class="graytxt">${db2_db2codepage }</span>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down icon-chevron-right"></i>数据库高级属性
									</h5>
									<div class="form-horizontal" style="display: none;">
										<div class="control-group">
											<label class="control-label">DB2日志路径</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_db2log }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">归档日志路径</span> 
													<span class="graytxt">${db2_logarchpath }</span>
												</div>
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label">LOCKTIMEOUT</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_locktimeout }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">LOGFILESIZ</span> 
													<span class="graytxt">${db2_logfilesize } MB</span>
												</div>
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label">LOGPRIMARY</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_logprimary }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">LOGSECOND</span> 
													<span class="graytxt">${db2_logsecond }</span>
												</div>
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label">TRACKMOD</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_trackmod }</span>
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">PAGESIZE</span> 
													<span class="graytxt">${db2_pagesize }</span>
												</div>
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label">SOFTMAX</label>
											<div class="controls">
												<div class="inputb2l">
													<span class="graytxt">${db2_softmax }</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						
						<input type="hidden" id="type" name="type" value="${type }">
						<input type="hidden" id="uuid" name="uuid" value="${uuid }">
						<input type="hidden" id="percent" value="${percent}"> 
						<input type="hidden" id="status" value="${status}">
						<div id="logmsg" class="tabcontent tabnow">
							<p class="columntxt2">主机安装进度 :&nbsp;&nbsp;&nbsp;${percent }</p>
							<div id="progress_my" class="progress progress-s progress-striped progress-success ine-block w100">
								<div class="bar" name="progress" style="width: ${percent};"></div>
							</div>
							<h5>详细任务执行状态：</h5>
							<table id="logTable3" class="table table-bordered  table-hover table-condensed no-search no-select" style="line-height: 20px">
								<thead>
									<tr>
									<th style="text-align:left;display: none;">序号</th> 
										<th style="text-align: left;">IP地址</th>
										<th style="text-align: left;">操作步骤</th>
										<th style="text-align: center;">状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${allServerStatus }" var="ser">
										<c:if test="${ser.status != '跳过' }">
										<tr>
											<td style="display:none;"><input type="hidden" value="${ser.uuid }"/></td>
											<td>${ser.host }</td>
											<td>${ser.name }</td>
											<td style="text-align: center;">${ser.status}</td>
										</tr>
									</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
