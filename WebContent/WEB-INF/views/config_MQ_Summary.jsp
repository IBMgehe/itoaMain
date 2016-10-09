<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.fasterxml.jackson.databind.*"%>
<%@ page import="com.fasterxml.jackson.databind.node.*"%>
<%@ page import="com.ibm.automation.core.util.StringUtil"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="header.jsp" flush="true" />
<title>云计算基础架构平台</title>
<style>
body {
	font-size: 13px;
}

.table th, .table td {
	line-height: 15px;
}

.bgc242 {
	background-color: #F2F2F2;
}

.tbc {
	text-align: center;
	background-color: #FFC000;
}

.tbc1 {
	text-align: center;
	background-color: #92D050;
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
			<a href="#" class="current1">IBM MQ配置跟踪汇总</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<table class="table table-bordered">
						<tr id="shuoming" style="font-weight: bolder;">
							<td class="bgc242" rowspan="2">参数</td>
							<td class="bgc242" rowspan="2">参数说明</td>
						</tr>
						<tr id="ccdatetime" style="font-weight: bolder;">
						</tr>

						<tr>
							<td style="text-align: center;" colspan="4"></td>
						</tr>
						<tr id="LogPrimaryFiles">
							<td>LogPrimaryFiles</td>
							<td>MQ队列管理器主事务日志文件个数</td>
						</tr>

						<tr id="LogSecondaryFiles">
							<td>LogSecondaryFiles</td>
							<td>MQ队列管理器次事务日志文件个数</td>
						</tr>
						<tr id="LogFilePages">
							<td>LogFilePages</td>
							<td>MQ队列管理器单个事务日志文件大小（单位为4KB）</td>
						</tr>
						<tr id="LogType">
							<td>LogType</td>
							<td>Q队列管理器事务日志类型，包括循环日志（CIRCULAR）和线性日志（LINEAR）两种类型</td>
						</tr>
						<!-- <tr id="ErrorLogSize">
							<td>ErrorLogSize</td>
							<td>Q队列管理器事务日志类型，包括循环日志（CIRCULAR）和线性日志（LINEAR）两种类型</td>
						</tr> -->
						<tr id="MaxChannels">
							<td>MaxChannels</td>
							<td>MQ队列管理器支持的最大通道连接数</td>
						</tr>
						<tr id="MaxActiveChannels">
							<td>MaxActiveChannels</td>
							<td>MQ队列管理器支持的最大同时活动通道连接数</td>
						</tr>
						<tr id="KeepAlive">
							<td>KeepAlive</td>
							<td>操作系统的TCP/IP参数设置对WebSphere MQ是否生效</td>
						</tr>
						<tr id="SndBuffSize">
							<td>SndBuffSize</td>
							<td>MQ发送通道SOCKET发送缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

						<tr id="RcvBuffSize">
							<td>RcvBuffSize</td>
							<td>MQ发送通道SOCKET接收缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

						<tr id="RcvSndBuffSize">
							<td>RcvSndBuffSize</td>
							<td>MQ接收通道SOCKET发送缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

						<tr id="RcvRcvBuffSize">
							<td>RcvRcvBuffSize</td>
							<td>MQ接收通道SOCKET接收缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

						<tr id='ClntSndBuffSize'>
							<td>ClntSndBuffSize</td>
							<td>MQ客户端连接通道SOCKET发送缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>


						<tr id="ClntRcvBuffSize">
							<td>ClntRcvBuffSize</td>
							<td>MQ客户端连接通道SOCKET接收缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

						<tr id="SvrSndBuffSize">
							<td>SvrSndBuffSize</td>
							<td>MQ服务器连接通道SOCKET发送缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

						<tr id="SvrRcvBuffSize">
							<td>SvrRcvBuffSize</td>
							<td>MQ服务器连接通道SOCKET接收缓冲区大小，默认值为0，代表由操作系统负责管理调节大小</td>
						</tr>

					</table>


					<input id="responseData" type="hidden" value="${mqcclistbase64 }">

				</div>
			</div>
		</div>

	</div>
</body>
<script type="text/javascript">
	var baseStr = base64decode($("#responseData").val());
	var ccObj = eval("(" + baseStr + ")");
	for (var i = 0; i < ccObj.length; i++) {
		$("#shuoming").append(
				"<td style=\"text-align: center;\">" + ccObj[i].name + "</td>");
		$("#ccdatetime").append(
				"<td style=\"text-align: center;\">" + ccObj[i].compDate
						+ "</td>");
		$("#LogPrimaryFiles").append(
				"<td style=\"text-align: center;\">" + ccObj[i].LogPrimaryFiles
						+ "</td>");

		$("#LogSecondaryFiles").append(
				"<td style=\"text-align: center;\">"
						+ ccObj[i].LogSecondaryFiles + "</td>");

		$("#LogFilePages").append(
				"<td style=\"text-align: center;\">" + ccObj[i].LogFilePages
						+ "</td>");

		$("#LogType").append(
				"<td style=\"text-align: center;\">" + ccObj[i].LogType
						+ "</td>");
		/* $("#ErrorLogSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].ErrorLogSize
						+ "</td>"); */
		$("#MaxChannels").append(
				"<td style=\"text-align: center;\">" + ccObj[i].MaxChannels
						+ "</td>");
		$("#MaxActiveChannels").append(
				"<td style=\"text-align: center;\">" + ccObj[i].MaxActiveChannels
						+ "</td>");
		$("#KeepAlive").append(
				"<td style=\"text-align: center;\">" + ccObj[i].KeepAlive
						+ "</td>");
		
		
		$("#SndBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SndBuffSize
						+ "</td>");
		
	
		
		
		$("#RcvBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].RcvBuffSize
						+ "</td>");
		
		
		$("#RcvSndBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].RcvSndBuffSize
						+ "</td>");
		
		
		$("#RcvRcvBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].RcvRcvBuffSize
						+ "</td>");
		
		
		$("#ClntSndBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].ClntSndBuffSize
						+ "</td>");
		
		$("#ClntRcvBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].ClntRcvBuffSize
						+ "</td>");
		
		$("#SvrSndBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SvrSndBuffSize
						+ "</td>");
		
		$("#SvrRcvBuffSize").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SvrRcvBuffSize
						+ "</td>");

	}
</script>






</html>
