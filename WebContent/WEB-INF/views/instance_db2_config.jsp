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
.input140,label{
	font-size:13px;
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
	//操作
	function modify() 
	{
		var db2insusr = $("#db2_db2insusr").val().trim();
		$("#db2_db2insusr1").val(db2insusr);
	}

	function CheckInput() 
	{
		if($("#db2_version").val()== -1)
		{
			sweet("请选择DB2安装版本 !","warning","确定");
			return;
		}

		var db2fix=$("#db2fix").val();
		if (db2fix == '-' || db2fix == '')
		{
			sweet("安装DB2必须选择补丁版本 !","warning","确定");
			return;
		}
		if ($("#db2_db2insusr").val().trim() == "") 
		{
			sweet("请输入DB2实例名 !","warning","确定");
			return;
		}
		if ($("#db2_svcename").val().trim() == "") 
		{
			sweet("请输入DB2监听端口 !","warning","确定");
			return;
		}
		var version = $("#db2_version").val();
		var db2base = $("#db2_fixpack").val();
		var value = $("#db2_fixpack option:selected").attr("value");

		/* if (version.substring(1) != value.substring(0, 4)) {
			swal({
				title: "",
				text: "DB2版本和补丁不一致",
				type: "warning",
				confirmButtonText: "确定",  
				confirmButtonColor: "rgb(174,222,244)"
			});
			return;
		} */
		
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
			<a href="getAllServers.do" class="current1"><i class="icon-home"></i>IBM DB2</a> 
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
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<b>状态:</b><span class="column_txt"><em>${ser.status }</em></span>
											</p>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
						
						<form action="toDb2NextPage.do?serId=${serId}&platform=${platform}&type=${ptype}&status=confirm"
							  method="post" id="submits" name="submits" class="form-horizontal">
							  <input type="hidden" id="serId" name="serId" value="${serId}">
							  <input type="hidden" id="ptype" name="ptype" value="${ptype}">
							  <input type="hidden" id="hostId" name="hostId" value="${hostId}">
							  <input type="hidden" id="platform" name="platform" value="${platform}">
							  <input type="hidden" id="db2version" name="db2version">
							  
							  <!-- 主机名和IP地址 -->
							  <c:forEach items="${servers }" var="ser">
							      <input type="hidden" id="all_hostnames" name="all_hostnames" value="${ser.name  }">
							      <input type="hidden" id="all_ips" name="all_ips" value="${ser.ip }">
							  </c:forEach>
							  
							  <!-- IP Begin -->
							  <input type="hidden" id="ip" name="ip" value="${ip}"> 
							  <input type="hidden" id="bootip" name="bootip" value="${bootip}"> 
							  <!-- IP End -->
							  <!--old VG BEGIN -->
							  <input type="hidden" id="hdiskname" name="hdiskname" value="${hdisknames }"> 
							  <input type="hidden" id="hdiskid" name="hdiskid" value="${hdiskids }"> 
							  <input type="hidden" id="vgtype" name="vgtype" value="${vgtypes }">
							  <!--old VG END -->
							  <!-- VG BEGIN -->
							  <input type="hidden" id="vgdb2home" name="vgdb2home" value="${vgdb2home }"> 
							  <input type="hidden" id="vgdb2log" name="vgdb2log" value="${vgdb2log }"> 
							  <input type="hidden" id="vgdb2archlog" name="vgdb2archlog" value="${vgdb2archlog }">
							  <input type="hidden" id="vgdataspace" name="vgdataspace" value="${vgdataspace }">
							  <input type="hidden" id="ha_vgcaap" name="ha_vgcaap" value="${ha_vgcaap }">
							  <!-- VG END -->
							  <!-- PV BEGIN -->
							  <input type="hidden" id="db2homepv" name="db2homepv" value="${db2homepv }"> 
							  <input type="hidden" id="db2logpv" name="db2logpv" value="${db2logpv }"> 
							  <input type="hidden" id="db2archlogpv" name="db2archlogpv" value="${db2archlogpv }"> 
							  <input type="hidden" id="db2backuppv" name="db2backuppv" value="${db2backuppv }">
							  <input type="hidden" id="dataspacepv" name="dataspacepv" value="${dataspacepv }"> 
							  <input type="hidden" id="dataspace2pv" name="dataspace2pv" value="${dataspace2pv }">
							  <input type="hidden" id="dataspace3pv" name="dataspace3pv" value="${dataspace3pv }"> 
							  <input type="hidden" id="dataspace4pv" name="dataspace4pv" value="${dataspace4pv }">
							  <input type="hidden" id="ha_caappv" name="ha_caappv" value="${ha_caappv }">
							  <input type="hidden" id="db2_ppsize" name="db2_ppsize" value="128">
							  <input type="hidden" id="data_pv" name="data_pv" value="${data_pv }">
							  <input type="hidden" id="log_pv" name="log_pv" value="${log_pv }">
							  <input type="hidden" id="archlog_pv" name="archlog_pv" value="${archlog_pv }">
							  <input type="hidden" id="db2home_pv" name="db2home_pv" value="${db2home_pv }">
							  
							  <!-- PV END -->
							  <!-- VG 创建方式  BEGIN -->
							  <input type="hidden" id="db2homemode" name="db2homemode" value="${db2homemode }"> 
							  <input type="hidden" id="db2logmode" name="db2logmode" value="${db2logmode }">
							  <input type="hidden" id="db2archlogmode" name="db2archlogmode" value="${db2archlogmode }">
							  <input type="hidden" id="dataspace1mode" name="dataspacemode" value="${dataspacemode }">
							  <!-- VG 创建方式 END -->
							  <!-- NFS BEGIN -->
							  <input type="hidden" id="nfsON" name="nfsON" value="${nfsON}">
							  <input type="hidden" id="nfsIP1" name="nfsIP1" value="${nfsIP1}">
						  	  <input type="hidden" id="nfsSPoint1" name="nfsSPoint1" value="${nfsSPoint1}"> 
						  	  <input type="hidden" id="nfsCPoint1" name="nfsCPoint1" value="${nfsCPoint1}">
							  <input type="hidden" id="nfsIP2" name="nfsIP2" value="${nfsIP2}">
							  <input type="hidden" id="nfsSPoint2" name="nfsSPoint2" value="${nfsSPoint2}"> 
							  <input type="hidden" id="nfsCPoint2" name="nfsCPoint2" value="${nfsCPoint2}">
							  <input type="hidden" id="nfsIP3" name="nfsIP3" value="${nfsIP3}">
							  <input type="hidden" id="nfsSPoint3" name="nfsSPoint3" value="${nfsSPoint3}"> 
							  <input type="hidden" id="nfsCPoint3" name="nfsCPoint3" value="${nfsCPoint3}">
							  <input type="hidden" id="nfsIP4" name="nfsIP4" value="${nfsIP4}">
							  <input type="hidden" id="nfsSPoint4" name="nfsSPoint4" value="${nfsSPoint4}"> 
							  <input type="hidden" id="nfsCPoint4" name="nfsCPoint4" value="${nfsCPoint4}">
							  <input type="hidden" id="nfsIP5" name="nfsIP5" value="${nfsIP5}">
							  <input type="hidden" id="nfsSPoint5" name="nfsSPoint5" value="${nfsSPoint5}"> 
							  <input type="hidden" id="nfsCPoint5" name="nfsCPoint5" value="${nfsCPoint5}">
							  <input type="hidden" id="nfsIP6" name="nfsIP6" value="${nfsIP6}">
							  <input type="hidden" id="nfsSPoint6" name="nfsSPoint6" value="${nfsSPoint6}"> 
							  <input type="hidden" id="nfsCPoint6" name="nfsCPoint6" value="${nfsCPoint6}">
							  <input type="hidden" id="nfsIP7" name="nfsIP7" value="${nfsIP7}">
							  <input type="hidden" id="nfsSPoint7" name="nfsSPoint7" value="${nfsSPoint7}"> 
							  <input type="hidden" id="nfsCPoint7" name="nfsCPoint7" value="${nfsCPoint7}">
							  <input type="hidden" id="nfsIP8" name="nfsIP8" value="${nfsIP8}">
							  <input type="hidden" id="nfsSPoint8" name="nfsSPoint8" value="${nfsSPoint8}"> 
							  <input type="hidden" id="nfsCPoint8" name="nfsCPoint8" value="${nfsCPoint8}">
							  <input type="hidden" id="nfsIP9" name="nfsIP9" value="${nfsIP9}">
							  <input type="hidden" id="nfsSPoint9" name="nfsSPoint9" value="${nfsSPoint9}"> 
							  <input type="hidden" id="nfsCPoint9" name="nfsCPoint9" value="${nfsCPoint9}">
							  <input type="hidden" id="nfsIP10" name="nfsIP10" value="${nfsIP10}"> 
							  <input type="hidden" id="nfsSPoint10" name="nfsSPoint10" value="${nfsSPoint10}">
							  <input type="hidden" id="nfsCPoint10" name="nfsCPoint10" value="${nfsCPoint10}"> 
							  <input type="hidden" id="nfsIP11" name="nfsIP11" value="${nfsIP11}"> 
							  <input type="hidden" id="nfsSPoint11" name="nfsSPoint11" value="${nfsSPoint11}"> 
							  <input type="hidden" id="nfsCPoint11" name="nfsCPoint11" value="${nfsCPoint11}">
							  <input type="hidden" id="nfsIP12" name="nfsIP12" value="${nfsIP12}"> 
							  <input type="hidden" id="nfsSPoint12" name="nfsSPoint12" value="${nfsSPoint12}">
							  <input type="hidden" id="nfsCPoint12" name="nfsCPoint12" value="${nfsCPoint12}"> 
							  <input type="hidden" id="nfsIP13" name="nfsIP13" value="${nfsIP13}"> 
							  <input type="hidden" id="nfsSPoint13" name="nfsSPoint13" value="${nfsSPoint13}"> 
							  <input type="hidden" id="nfsCPoint13" name="nfsCPoint13" value="${nfsCPoint13}">
							  <input type="hidden" id="nfsIP14" name="nfsIP14" value="${nfsIP14}"> 
							  <input type="hidden" id="nfsSPoint14" name="nfsSPoint14" value="${nfsSPoint14}">
							  <input type="hidden" id="nfsCPoint14" name="nfsCPoint14" value="${nfsCPoint14}"> 
							  <input type="hidden" id="nfsIP15" name="nfsIP15" value="${nfsIP15}"> 
							  <input type="hidden" id="nfsSPoint15" name="nfsSPoint15" value="${nfsSPoint15}"> 
							  <input type="hidden" id="nfsCPoint15" name="nfsCPoint15" value="${nfsCPoint15}">
							  <!-- NFS END -->

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>基本信息
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">DB2安装版本</label>
										<div class="controls">
											<div class="inputb2l">
												<select style="width: 47.5%" class="w48" id="db2_version" 
														name="db2_version" onchange="showfix(this);">
													<option value="-1" selected="selected">请选择...</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">DB2版本补丁</span>
												<select class="form-control"  onchange="getVer(this)"
														style="width: 47.5%" name="db2_fixpack" id="db2_fixpack">
													<option value="-1" selected="selected">请选择...</option>
												</select>
												<input type="hidden" id="db2fix" name="db2fix" value="-">
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">DB2安装路径</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="db2_db2base"
													name="db2_db2base" value=""/>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">DB2实例目录</span> 
												<input type="text" class="w45" id="db2_dbpath" name="db2_dbpath"
													value="/db2home"/>
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">DB2实例名</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="db2_db2insusr"
													name="db2_db2insusr" value="db2inst1" onblur="modify()" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">监听端口</span> 
												<input type="text" class="w45" id="db2_svcename" 
												       name="db2_svcename" value="60000" />
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">DB2数据库名</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45"
													id="db2_dbname" name="db2_dbname" value="sample" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">字符集</span> 
												<select style="width: 47.5%" id="styleSelect" class="w48"
													onChange="changeDivShow('styleSelect')" name="db2_codeset">
													<option value="GBK" selected="selected">GBK</option>
													<option value="UTF-8">UTF-8</option>
												</select>
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">DB2数据目录</label>
										<div class="controls">
										   <div class="inputb2l">
											 <input type="text" class="w45" id="db2_dbdatapath" name="db2_dbdatapath"
												    value="/db2space1" />
										   </div>
										   <div class="inputb2l">
												<span class="input140 mr20">主机名</span> 
												<input type="text" class="w45" id="hostname" name="hostname"
													value="${hostname }" />
											</div>
										</div>
										
									</div>
								</div>
							</div>

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>实例高级属性
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">实例用户</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_db2insusr1" name="db2_db2insusr1"
													value="db2inst1" readonly="readonly" class="w45" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">实例用户组</span> 
												<input type="text" id="db2_db2insgrp" name="db2_db2insgrp"
													value="db2igrp" readonly="readonly" class="w45" />
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">fence用户</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_db2fenusr" class="w45"
													name="db2_db2fenusr" value="db2fenc" readonly="readonly" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">fence用户组</span> 
												<input type="text" id="db2_db2fengrp" class="w45"
													name="db2_db2fengrp" value="db2fgrp" readonly="readonly" />
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">DB2COMM</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_db2comm" class="w45"
													name="db2_db2comm" value="tcpip" readonly="readonly" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">DB2CODEPAGE</span> 
												<input type="text" id="db2_db2codepage" class="w45 codeVal"
													name="db2_db2codepage" value="1386" readonly="readonly" />
											</div>
										</div>
									</div>
									<!-- <div class="control-group">
										<label class="control-label">NUM_INITAGENTS</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_initagents" class="w45"
													name="db2_initagents" value="0" readonly="readonly" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">NUM_POOLAGENTS</span> 
												<input type="text" id="db2_poolagents" class="w45"
													name="db2_poolagents" value="Automatic" readonly="readonly" />
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">MAX_COORDAGENTS</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_max_coordagents" class="w45"
													name="db2_max_coordagents" value="Automatic" readonly="readonly" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">MAX_CONNECTIONS</span> 
												<input type="text" id="db2_max_connectings" class="w45"
													name="db2_max_connectings" value="Automatic" readonly="readonly" />
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">DIAGSIZE</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_diagsize"
													   class="w45" name="db2_diagsize" value="50" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">DFT_MON_BUFPOOL</span> 
												<select style="width: 47.5%" class="w48" name="db2_mon_buf">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">DFT_MON_LOCK</label>
										<div class="controls">
											<div class="inputb2l">
												<select class="w48" name="db2_mon_lock" style="width: 47.5%">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">DFT_MON_SORT</span> 
												<select style="width: 47.5%" class="w48" name="db2_mon_sort">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">DFT_MON_STMT</label>
										<div class="controls">
											<div class="inputb2l">
												<select class="w48" name="db2_mon_stmt" style="width: 47.5%">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">DFT_MON_TABLE</span> 
												<select style="width: 47.5%" class="w48" name="db2_mon_table">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">DFT_MON_UOW</label>
										<div class="controls">
											<div class="inputb2l">
												<select class="w48" name="db2_mon_uow" style="width: 47.5%">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">HEACTH_MON</span> 
												<select style="width: 47.5%" class="w48" name="db2_health_mon">
													<option value="ON">ON</option>
													<option value="OFF" selected="selected">OFF</option>
												</select>
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">MON_HEAP_SZ</label>
										<div class="controls">
										  <div class="inputb2l">
											<input type="text" id="db2_mon_heap" name="db2_mon_heap" class="w45"
												   value="Automatic(500)" readonly="readonly" />
										  </div>
										</div>
									</div> -->
									
								</div>
							</div>

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>数据库高级属性
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">DB2日志路径</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_db2log" class="w45"
													name="db2_db2log" value="/db2log" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">归档日志路径</span> 
												<input type="text" id="db2_logarchpath" class="w45" name="db2_logarchpath"
													value="/db2archlog" />
											</div>
										</div>
									</div>
									
									<!-- <div class="control-group">
										<label class="control-label">DB2备份路径</label>
										<div class="controls">
											<input type="text" id="db2_backuppath" name="db2_backuppath"
												style="width: 78%" value="/db2backup" readonly="readonly" />
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">SELF_TUNING_MEM</label>
										<div class="controls">
											<div class="inputb2l" class="w45">
												<select class="w48" name="db2_stmm" style="width: 47.5%">
													<option value="ON" selected="selected">ON</option>
													<option value="OFF">OFF</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">LOCKLIST</span> 
												<input type="text" id="db2_locklist" class="w45"
													name="db2_locklist" value="Automatic" readonly="readonly" />
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">MAXLOCKS</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_maxlocks" name="db2_maxlocks"
													class="w45" value="Automatic" readonly="readonly" />
											</div>
										</div>
									</div> -->
									
									<!-- <div class="control-group">
										<label class="control-label">SORTHEAP</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_sortheap" name="db2_sortheap" class="w45"
													value="Automatic" readonly="readonly" />
											</div>
										</div>
									</div> -->
									
									<div class="control-group">
										<label class="control-label">LOCKTIMEOUT</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_locktimeout" name="db2_locktimeout"
													class="w45" value="60" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">LOGFILESIZ</span> 
												<select style="width: 47.5%" class="w48" name="db2_logfilesize">
													<option value="200">200</option>
													<option value="500">500</option>
												</select> MB
											</div>
										</div>
									</div>		
									
									<div class="control-group">
										<label class="control-label">LOGPRIMARY</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_logprimary" class="w45"
													name="db2_logprimary" value="30" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">LOGSECOND</span> 
												<input type="text" id="db2_logsecond" class="w45"
													name="db2_logsecond" value="20" />
											</div>
										</div>
									</div>
									
									<!-- <div class="control-group">
										<label class="control-label">LOGBUFSZ</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_logbuff" class="w45"
													name="db2_logbuff" value="200" />&nbsp;MB
											</div>
										</div>
									</div> -->
																		
									<div class="control-group">
										<label class="control-label">TRACKMOD</label>
										<div class="controls">
											<div class="inputb2l">
												<select class="w48" name="db2_trackmod" style="width: 47.5%">
													<option value="YES" selected="selected">YES</option>
													<option value="NO">NO</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">PAGESIZE</span> 
												<select style="width: 47.5%" class="w48" name="db2_pagesize">
													<option value="4">4</option>
													<option value="8" selected="selected">8</option>
													<option value="16">16</option>
													<option value="32">32</option>
												</select>
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">SOFTMAX</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" id="db2_softmax" class="w45"
													name="db2_softmax" value="100" readonly="readonly" />
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
		//显示was版本补丁
		function showfix(obj) 
		{
			var checkText=$(obj).find("option:selected").text();
			var platform1=$('#platform').val();		
			
			$("#db2_db2base").val('/opt/IBM/db2/v'+checkText)
			$.ajax({
				url : "<%=path%>/getDb2Fix.do",
				data : { version : checkText,platform : platform1, pName:'db2'},
				type : 'post',
				dataType : 'json',
				success : function(result) 
				{
					$("#db2_fixpack").empty();
					$("#db2fix").val('-');
					if(result.length==0)
					{
						var str="<option value='-'>"+ "请选择..." + "</option>";
						$("#db2_fixpack").append(str);
						$("#db2version").val(checkText=$(obj).find("option:selected").text());
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
					$("#db2_fixpack").append(str);
					$("#db2version").val(checkText=$(obj).find("option:selected").text());
				}
			});
		}

		//自动加载
		function getVer(obj) 
		{
			if ($("#db2_fixpack").find("option:selected").text() == '请选择...') 
			{
				$("#db2fix").val('-');
				$("db2_fixpack").val('-');
			} 
			else
				$("#db2fix").val($("#db2_fixpack").find("option:selected").text());
		}
		
		window.onload = function() {
			var platform1=$('#platform').val();
			$.ajax({
				url : '<%=path%>/getDb2Verion1.do',
				data : {platform : platform1,pName : 'db2'},
				type : 'post',
				dataType : 'json',
				success : function(result) 
				{
					
					var str="";
					for (var i = 0; i < result.length; i++) 
					{
						for ( var key in result[i])
						{
							str += "<option value='" + result[i][key] + "'>" + key+ "</option>";}
						}
					$("#db2_version").append(str);
				},failure:function(err){
					alert(err);
				}
			})
		}
	</script>
</body>
</html>
