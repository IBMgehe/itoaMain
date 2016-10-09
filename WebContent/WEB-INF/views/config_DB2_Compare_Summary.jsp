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
			<a href="#" class="current1">IBM DB2配置跟踪汇总</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<!-- 实例 -->
					<table id='compTable' class="table table-bordered">
						<tr id="shuoming" style="font-weight: bolder;">
							<td class="bgc242" rowspan="2">参数</td>
							<td class="bgc242" rowspan="2">参数说明</td>
						</tr>
						<tr id="ccdatetime" style="font-weight: bolder;">
						</tr>

						<tr>
							<td style="text-align: center;" colspan="4"></td>
						</tr>
						<tr id="SYSMON_GROUP">
							<td>SYSMON_GROUP(memory)</td>
							<td>系统监视权限组名</td>
						</tr>

						<tr id="RESTBUFSZ">
							<td>RESTBUFSZ(4KB)</td>
							<td>备份缓冲池默认大小</td>
						</tr>
						<tr id="DIAGPATH">
							<td>DIAGPATH (memory)</td>
							<td>诊断数据目录路径</td>
						</tr>
						<tr id="ASLHEAPSZ">
							<td>ASLHEAPSZ(4KB)</td>
							<td>应用程序支持层堆大小</td>
						</tr>
						<tr id="MAX_CONNECTIONS">
							<td>MAX_CONNECTIONS</td>
							<td>最大客户机连接数</td>
						</tr>
						<tr id="BACKBUFSZ">
							<td>BACKBUFSZ(4KB)</td>
							<td>恢复缓冲池默认大小</td>
						</tr>
						<tr id="INTRA_PARALLEL">
							<td>INTRA_PARALLEL</td>
							<td>启用分区内并行性</td>
						</tr>
						<tr id="UTIL_IMPACT_LIM">
							<td>UTIL_IMPACT_LIM</td>
							<td>实例影响策略</td>
						</tr>
						<tr id="ALT_DIAGPATH">
							<td>ALT_DIAGPATH(memory)</td>
							<td>备用诊断数据目录路径</td>
						</tr>
						<tr id="FENCED_POOL">
							<td>FENCED_POOL</td>
							<td>最大受防护进程数</td>
						</tr>
						<tr id="INSTANCE_MEMORY">
							<td>INSTANCE_MEMORY(4KB)</td>
							<td>实例内存</td>
						</tr>
						<tr id="SYSMAINT_GROUP">
							<td>SYSMAINT_GROUP(memory)</td>
							<td>系统维护权限组名</td>
						</tr>
						<tr id="MAX_QUERYDEGREE">
							<td>MAX_QUERYDEGREE</td>
							<td>最大查询并行度</td>
						</tr>
						<tr id="SYSCTRL_GROUP">
							<td>SYSCTRL_GROUP(memory)</td>
							<td>系统控制权限组名</td>
						</tr>
						<tr id="DFT_MON_STMT">
							<td>DFT_MON_STMT</td>
							<td>快照监视器的语句开关</td>
						</tr>
						<tr id="NOTIFYLEVEL">
							<td>NOTIFYLEVEL</td>
							<td>通知级别</td>
						</tr>
						<tr id="DFT_MON_LOCK">
							<td>DFT_MON_LOCK</td>
							<td>快照监视器的锁定开关</td>
						</tr>
						<tr id="DFT_MON_BUFPOOL">
							<td>DFT_MON_BUFPOOL</td>
							<td>快照监视器的缓冲池开关</td>
						</tr>
						<tr id="DFT_MON_SORT">
							<td>DFT_MON_SORT</td>
							<td>快照监视器的排序开关</td>
						</tr>
						<tr id="DIAGSIZE">
							<td>DIAGSIZE(MB)</td>
							<td>轮换诊断日志和管理通知日志</td>
						</tr>
						<tr id="AUTHENTICATION">
							<td>AUTHENTICATION</td>
							<td>认证类型</td>
						</tr>
						<tr id="SYSADM_GROUP">
							<td>SYSADM_GROUP(memory)</td>
							<td>系统管理权限组名</td>
						</tr>
						<tr id="DIAGLEVEL">
							<td>DIAGLEVEL</td>
							<td>诊断错误捕获级别</td>
						</tr>
						<tr id="DFT_MON_TABLE">
							<td>DFT_MON_TABLE</td>
							<td>快照监视器的表开关</td>
						</tr>
						<tr id="DISCOVER">
							<td>DISCOVER</td>
							<td>发现方式</td>
						</tr>
						<tr id="JAVA_HEAP_SZ">
							<td>JAVA_HEAP_SZ(4KB)</td>
							<td>最大 Java 解释器堆大小</td>
						</tr>
						<tr id="MAX_COORDAGENTS">
							<td>MAX_COORDAGENTS</td>
							<td>最大协调代理程序数</td>
						</tr>
						<tr id="INDEXREC">
							<td>INDEXREC</td>
							<td>索引重新创建时间</td>
						</tr>
						<tr id="DFT_MON_UOW">
							<td>DFT_MON_UOW</td>
							<td>快照监视器的工作单元 (UOW) 开关</td>
						</tr>
						<tr id="HEALTH_MON">
							<td>HEALTH_MON</td>
							<td>运行状况监视器</td>
						</tr>
						<tr id="RESYNC_INTERVAL">
							<td>RESYNC_INTERVAL(secs)</td>
							<td>事务再同步时间间隔</td>
						</tr>
						<tr id="SHEAPTHRES">
							<td>SHEAPTHRES(4KB)</td>
							<td>排序堆阈值</td>
						</tr>
						<tr id="AUDIT_BUF_SZ">
							<td>AUDIT_BUF_SZ(4KB)</td>
							<td>审计缓冲区大小</td>
						</tr>
						<tr id="NUM_INITAGENTS">
							<td>NUM_INITAGENTS</td>
							<td>池中的初始代理程序数</td>
						</tr>
						<tr id="QUERY_HEAP_SZ">
							<td>QUERY_HEAP_SZ(4KB)</td>
							<td>查询堆大小</td>
						</tr>
						<tr id="DFT_MON_TIMESTAMP">
							<td>DFT_MON_TIMESTAMP</td>
							<td>快照监视器的时间戳记开关</td>
						</tr>
						<tr id="RQRIOBLK">
							<td>RQRIOBLK(bytes)</td>
							<td>客户机 I/O 块大小</td>
						</tr>
						<tr id="KEEPFENCED">
							<td>KEEPFENCED</td>
							<td>保留受防护进程</td>
						</tr>
						<tr id="NUM_POOLAGENTS">
							<td>NUM_POOLAGENTS</td>
							<td>代理程序池大小</td>
						</tr>
						<tr id="NUM_INITFENCED">
							<td>NUM_INITFENCED</td>
							<td>池中的初始代理程序数</td>
						</tr>
						<tr id="DFTDBPATH">
							<td>DFTDBPATH(memory)</td>
							<td>缺省数据库路径</td>
						</tr>
						<tr id="MON_HEAP_SZ">
							<td>MON_HEAP_SZ(4KB)</td>
							<td>数据库系统监视器堆大小</td>
						</tr>
						<tr id="AGENTPRI">
							<td>AGENTPRI</td>
							<td>代理程序的优先级</td>
						</tr>
						<tr id="DISCOVER_INST">
							<td>DISCOVER_INST</td>
							<td>发现服务器实例</td>
						</tr>
						<tr id="SVCENAME">
							<td>SVCENAME</td>
							<td>TCP/IP 服务名称</td>
						</tr>
					</table>
					<div style="margin-top:30px;"></div>
					
					
					<!-- DateBase -->
					<c:if test="${srcDB ne '-'}"> 					
					<table id='compTable_DB' class="table table-bordered">
						<tr id="shuoming_DB" style="font-weight: bolder;">
							<td class="bgc242" rowspan="3">参数</td>
							<td class="bgc242" rowspan="3">参数说明</td>
						</tr>
						<tr id="ccdatetime_DB" style="font-weight: bolder;">
						</tr>
						<!-- <tr id="parent_DB" style="font-weight: bolder;">
						</tr> -->
						<tr>
							<td style="text-align: center;" colspan="4"></td>
						</tr>
						<tr id="ENABLE_XMLCHAR">
							<td>ENABLE_XMLCHAR</td>
							<td>启用以 XML 为目标的转换</td>
						</tr>

						<tr id="AUTORESTART">
							<td>AUTORESTART</td>
							<td>允许自动重新启动</td>
						</tr>
						<tr id="LOCKTIMEOUT">
							<td>LOCKTIMEOUT (secs)</td>
							<td>锁定超时</td>
						</tr>
						<tr id="AUTO_PROF_UPD">
							<td>AUTO_PROF_UPD</td>
							<td>统计信息概要文件更新</td>
						</tr>
						<tr id="Log_retain_for_recovery_status">
							<td>Log retain for recovery status</td>
							<td>恢复状态的日志保留</td>
						</tr>
						<tr id="MINCOMMIT">
							<td>MINCOMMIT</td>
							<td>要分组的落实数</td>
						</tr>
						<tr id="REC_HIS_RETENTN">
							<td>REC_HIS_RETENTN (days)</td>
							<td>恢复历史记录保留期</td>
						</tr>
						<tr id="User_exit_for_logging_status">
							<td>User exit for logging status</td>
							<td>日志记录状态的用户出口</td>
						</tr>
						<tr id="UTIL_HEAP_SZ">
							<td>UTIL_HEAP_SZ (4KB)</td>
							<td>实用程序堆大小</td>
						</tr>
						<tr id="DFT_SQLMATHWARN">
							<td>DFT_SQLMATHWARN</td>
							<td>出现算术异常时继续</td>
						</tr>
						<tr id="HADR_database_role">
							<td>HADR database role</td>
							<td>HADR状态</td>
						</tr>
						<tr id="SELF_TUNING_MEM">
							<td>SELF_TUNING_MEM</td>
							<td>自调整内存功能</td>
						</tr>
						<tr id="SEQDETECT">
							<td>SEQDETECT</td>
							<td>顺序检测和提前读标志</td>
						</tr>
						<tr id="TSM_MGMTCLASS">
							<td>TSM_MGMTCLASS</td>
							<td>Tivoli Storage Manager 管理类</td>
						</tr>
						<tr id="MAX_LOG">
							<td>MAX_LOG</td>
							<td>每个事务的最大日志数</td>
						</tr>
						<tr id="AUTO_REVAL">
							<td>AUTO_REVAL</td>
							<td>自动重新验证和失效</td>
						</tr>
						<tr id="HADR_TIMEOUT">
							<td>HADR_TIMEOUT</td>
							<td>HADR 超时值</td>
						</tr>
						<tr id="AUTO_DEL_REC_OBJ">
							<td>AUTO_DEL_REC_OBJ</td>
							<td>自动删除恢复对象</td>
						</tr>
						<tr id="NUM_QUANTILES">
							<td>NUM_QUANTILES</td>
							<td>列的分位数</td>
						</tr>
						<tr id="HADR_REMOTE_SVC">
							<td>HADR_REMOTE_SVC</td>
							<td>HADR 远程服务名称</td>
						</tr>
						<tr id="MON_PKGLIST_SZ">
							<td>MON_PKGLIST_SZ</td>
							<td>监视程序包列表大小</td>
						</tr>
						<tr id="NUM_DB_BACKUPS">
							<td>NUM_DB_BACKUPS</td>
							<td>数据库备份数</td>
						</tr>
						<tr id="VARCHAR2_COMPAT">
							<td>VARCHAR2_COMPAT</td>
							<td>Varchar2 兼容性</td>
						</tr>
						<tr id="DB_MEM_THRESH">
							<td>DB_MEM_THRESH</td>
							<td>数据库内存阈值</td>
						</tr>
						<tr id="AUTO_REORG">
							<td>AUTO_REORG</td>
							<td>自动重组</td>
						</tr>
						<tr id="ARCHRETRYDELAY">
							<td>ARCHRETRYDELAY (secs)</td>
							<td>出错时的归档重试延迟</td>
						</tr>
						<tr id="VENDOROPT">
							<td>VENDOROPT</td>
							<td>供应商选项</td>
						</tr>
						<tr id="DFT_EXTENT_SZ">
							<td>DFT_EXTENT_SZ (pages)</td>
							<td>表空间的缺省扩展数据块大小</td>
						</tr>
						<tr id="NUM_FREQVALUES">
							<td>NUM_FREQVALUES</td>
							<td>保留的高频值数目</td>
						</tr>
						<tr id="MAXAPPLS">
							<td>MAXAPPLS</td>
							<td>最大活动应用程序数</td>
						</tr>
						<tr id="DECFLT_ROUNDING">
							<td>DECFLT_ROUNDING</td>
							<td>十进制浮点数舍入</td>
						</tr>
						<tr id="MON_OBJ_METRICS">
							<td>MON_OBJ_METRICS</td>
							<td>监视对象度量值</td>
						</tr>
						<tr id="AUTO_MAINT">
							<td>AUTO_MAINT</td>
							<td>自动维护</td>
						</tr>
						<tr id="ALT_COLLATE">
							<td>ALT_COLLATE</td>
							<td>备用整理顺序</td>
						</tr>
						<tr id="DBHEAP">
							<td>DBHEAP (4KB)</td>
							<td>数据库堆</td>
						</tr>
						<tr id="NUMBER_COMPAT">
							<td>NUMBER_COMPAT</td>
							<td>数字兼容性</td>
						</tr>
						<tr id="DISCOVER_DB">
							<td>DISCOVER_DB</td>
							<td>发现数据库</td>
						</tr>
						<tr id="INDEXRECDB">
							<td>INDEXREC</td>
							<td>索引重新创建时间</td>
						</tr>
						<tr id="MAXLOCKS">
							<td>MAXLOCKS</td>
							<td>锁定升级前锁定列表的最大百分比</td>
						</tr>
						<tr id="Default_number_of_containers">
							<td>Default number of containers</td>
							<td>容器的缺省数目</td>
						</tr>
						<tr id="BLK_LOG_DSK_FUL">
							<td>BLK_LOG_DSK_FUL</td>
							<td>日志磁盘满时阻止进行日志记录</td>
						</tr>
						<tr id="TSM_PASSWORD">
							<td>TSM_PASSWORD</td>
							<td>Tivoli Storage Manager 密码</td>
						</tr>
						<tr id="LOGRETAIN">
							<td>LOGRETAIN</td>
							<td>启用日志保留</td>
						</tr>
						<tr id="BUFFPAGE">
							<td>BUFFPAGE</td>
							<td>缓冲池大小</td>
						</tr>
						<tr id="STMTHEAP">
							<td>STMTHEAP (4KB)</td>
							<td>语句堆大小</td>
						</tr>
						<tr id="LOGPRIMARY">
							<td>LOGPRIMARY</td>
							<td>主日志文件数</td>
						</tr>
						<tr id="Database_territory">
							<td>Database territory</td>
							<td>数据库地域</td>
						</tr>
						<tr id="Database_code_page">
							<td>Database code page</td>
							<td>数据库代码页</td>
						</tr>
						<tr id="DFT_QUERYOPT">
							<td>DFT_QUERYOPT</td>
							<td>缺省查询优化类</td>
						</tr>
						<tr id="Database_country_region_code">
							<td>Database country/region code</td>
							<td>数据库国家/地域代码</td>
						</tr>
						<tr id="First_active_log_file">
							<td>First active log file</td>
							<td>首个活动日志</td>
						</tr>
						<tr id="LOGARCHMETH1">
							<td>LOGARCHMETH1 (memory)</td>
							<td>主日志归档方法</td>
						</tr>
						<tr id="CHNGPGS_THRESH">
							<td>CHNGPGS_THRESH</td>
							<td>已更改页数阈值</td>
						</tr>
						<tr id="DFT_LOADREC_SES">
							<td>DFT_LOADREC_SES</td>
							<td>缺省装入恢复会话数</td>
						</tr>
						<tr id="TSM_OWNER">
							<td>TSM_OWNER</td>
							<td>Tivoli Storage Manager 所有者名称</td>
						</tr>
						<tr id="AUTO_STATS_PROF">
							<td>AUTO_STATS_PROF</td>
							<td>自动进行统计信息概要分析</td>
						</tr>
						<tr id="DEC_TO_CHAR_FMT">
							<td>DEC_TO_CHAR_FMT</td>
							<td>小数到字符函数</td>
						</tr>
						<tr id="CATALOGCACHE_SZ">
							<td>CATALOGCACHE_SZ (4KB)</td>
							<td>目录高速缓存大小</td>
						</tr>
						<tr id="Path_to_log_files">
							<td>Path to log files (memory)</td>
							<td>日志文件路径</td>
						</tr>
						<tr id="DFT_DEGREE">
							<td>DFT_DEGREE</td>
							<td>缺省级别</td>
						</tr>
						<tr id="LOGFILSIZ">
							<td>LOGFILSIZ (4KB)</td>
							<td>日志文件大小</td>
						</tr>
						<tr id="Backup_pending">
							<td>Backup pending</td>
							<td>备份暂挂</td>
						</tr>
						<tr id="SHEAPTHRES_SHR">
							<td>SHEAPTHRES_SHR (4KB)</td>
							<td>共享排序的排序堆阈值</td>
						</tr>
						<tr id="Restrict_access">
							<td>Restrict access</td>
							<td>限制访问</td>
						</tr>
						<tr id="Statement_concentrator">
							<td>Statement concentrator</td>
							<td>语句集中器</td>
						</tr>
						<tr id="DATE_COMPAT">
							<td>DATE_COMPAT</td>
							<td>日期兼容性</td>
						</tr>
						<tr id="HADR_PEER_WINDOW">
							<td>HADR_PEER_WINDOW (secs)</td>
							<td>HADR 对等窗口</td>
						</tr>
						<tr id="MAXFILOP">
							<td>MAXFILOP</td>
							<td>每个应用程序打开的最大数据库文件数</td>
						</tr>
						<tr id="LOGSECOND">
							<td>LOGSECOND</td>
							<td>辅助日志文件数</td>
						</tr>
						<tr id="AUTO_TBL_MAINT">
							<td>AUTO_TBL_MAINT</td>
							<td>自动表维护</td>
						</tr>
						<tr id="HADR_REMOTE_INST">
							<td>HADR_REMOTE_INST</td>
							<td>远程服务器的 HADR 实例名</td>
						</tr>
						<tr id="AUTO_DB_BACKUP">
							<td>AUTO_DB_BACKUP</td>
							<td>自动数据库备份</td>
						</tr>
						<tr id="DFT_PREFETCH_SZ">
							<td>DFT_PREFETCH_SZ (pages)</td>
							<td>缺省预取大小</td>
						</tr>
						<tr id="MON_LOCKWAIT">
							<td>MON_LOCKWAIT</td>
							<td>监视锁定等待</td>
						</tr>
						<tr id="NUM_IOCLEANERS">
							<td>NUM_IOCLEANERS</td>
							<td>异步页清除程序的数目</td>
						</tr>
						<tr id="NUM_LOG_SPAN">
							<td>NUM_LOG_SPAN</td>
							<td>1个活动UOW的活动日志文件数</td>
						</tr>
						<tr id="MON_LOCKTIMEOUT">
							<td>MON_LOCKTIMEOUT</td>
							<td>监视锁定超时</td>
						</tr>
						<tr id="TRACKMOD">
							<td>TRACKMOD</td>
							<td>启用跟踪已修改页</td>
						</tr>
						<tr id="NUM_IOSERVERS">
							<td>NUM_IOSERVERS</td>
							<td>I/O 服务器数</td>
						</tr>
						<tr id="LOGINDEXBUILD">
							<td>LOGINDEXBUILD</td>
							<td>创建的日志索引页数</td>
						</tr>
						<tr id="MON_LCK_MSG_LVL">
							<td>MON_LCK_MSG_LVL</td>
							<td>监视锁定事件通知消息</td>
						</tr>
						<tr id="MON_REQ_METRICS">
							<td>MON_REQ_METRICS</td>
							<td>监视请求度量值</td>
						</tr>
						<tr id="LOGARCHMETH2">
							<td>LOGARCHMETH2 (memory)</td>
							<td>辅助日志归档方法</td>
						</tr>
						<tr id="DLCHKTIME">
							<td>DLCHKTIME (ms)</td>
							<td>检查死锁的时间间隔</td>
						</tr>
						<tr id="SOFTMAX">
							<td>SOFTMAX</td>
							<td>恢复范围和软检查点时间间隔</td>
						</tr>
						<tr id="MIRRORLOGPATH">
							<td>MIRRORLOGPATH (memory)</td>
							<td>镜像日志路径</td>
						</tr>
						<tr id="Multipage_File_alloc_enabled">
							<td>Multi-page File alloc enabled</td>
							<td>启动多页文件分配</td>
						</tr>
						<tr id="NUMARCHRETRY">
							<td>NUMARCHRETRY</td>
							<td>出错时重试次数</td>
						</tr>
						<tr id="AUTO_STMT_STATS">
							<td>AUTO_STMT_STATS</td>
							<td>实时统计信息</td>
						</tr>
						<tr id="FAILARCHPATH">
							<td>FAILARCHPATH (memory)</td>
							<td>故障转移日志归档路径</td>
						</tr>
						<tr id="USEREXIT">
							<td>USEREXIT</td>
							<td>启用用户出口</td>
						</tr>
						<tr id="Database_page_size">
							<td>Database page size</td>
							<td>数据库页大小</td>
						</tr>
						<tr id="HADR_LOCAL_HOST">
							<td>HADR_LOCAL_HOST</td>
							<td>HADR 本地主机</td>
						</tr>
						<tr id="MON_ACT_METRICS">
							<td>MON_ACT_METRICS</td>
							<td>监视活动度量值</td>
						</tr>
						<tr id="AVG_APPLS">
							<td>AVG_APPLS</td>
							<td>平均活动应用程序数</td>
						</tr>
						<tr id="WLM_COLLECT_INT">
							<td>WLM_COLLECT_INT (mins)</td>
							<td>工作负载管理收集时间间隔</td>
						</tr>
						<tr id="LOGBUFSZ">
							<td>LOGBUFSZ (4KB)</td>
							<td>日志缓冲区大小</td>
						</tr>
						<tr id="DFT_MTTB_TYPES">
							<td>DFT_MTTB_TYPES</td>
							<td>为优化缺省维护的表类型</td>
						</tr>
						<tr id="Database_is_consistent">
							<td>Database is consistent</td>
							<td>数据库处于一致状态</td>
						</tr>
						<tr id="STAT_HEAP_SZ">
							<td>STAT_HEAP_SZ (4KB)</td>
							<td>统计信息堆大小</td>
						</tr>
						<tr id="Database_code_set">
							<td>Database code set</td>
							<td>数据库代码集</td>
						</tr>
						<tr id="SORTHEAP">
							<td>SORTHEAP (4KB)</td>
							<td>排序堆大小</td>
						</tr>						
						<tr id="CUR_COMMIT">
							<td>CUR_COMMIT</td>
							<td>当前已落实</td>
						</tr>
						<tr id="MON_LW_THRESH">
							<td>MON_LW_THRESH</td>
							<td>监视锁定等待阈值</td>
						</tr>
						<tr id="DATABASE_MEMORY">
							<td>DATABASE_MEMORY (4KB)</td>
							<td>数据库共享内存大小</td>
						</tr>
						<tr id="MON_UOW_DATA">
							<td>MON_UOW_DATA</td>
							<td>监视工作单元事件</td>
						</tr>
						<tr id="BLOCKNONLOGGED">
							<td>BLOCKNONLOGGED</td>
							<td>禁止创建允许不进行日志记录的活动的表</td>
						</tr>
						<tr id="TSM_NODENAME">
							<td>TSM_NODENAME</td>
							<td>Tivoli Storage Manager 节点名</td>
						</tr>
						<tr id="PCKCACHESZ">
							<td>PCKCACHESZ (4KB)</td>
							<td>程序包高速缓存大小</td>
						</tr>
						<tr id="HADR_REMOTE_HOST">
							<td>HADR_REMOTE_HOST</td>
							<td>HADR 远程主机名</td>
						</tr>
						<tr id="APPL_MEMORY">
							<td>APPL_MEMORY (4KB)</td>
							<td>应用程序内存</td>
						</tr>
						<tr id="Restore_pending">
							<td>Restore pending</td>
							<td>恢复暂挂</td>
						</tr>
						<tr id="DFT_REFRESH_AGE">
							<td>DFT_REFRESH_AGE</td>
							<td>缺省刷新持续时间</td>
						</tr>
						<tr id="HADR_SYNCMODE">
							<td>HADR_SYNCMODE</td>
							<td>HADR 同步方式</td>
						</tr>
						<tr id="OVERFLOWLOGPATH">
							<td>OVERFLOWLOGPATH (memory)</td>
							<td>溢出日志路径</td>
						</tr>
						<tr id="MON_DEADLOCK">
							<td>MON_DEADLOCK</td>
							<td>监视死锁</td>
						</tr>
						<tr id="AUTO_RUNSTATS">
							<td>AUTO_RUNSTATS</td>
							<td>自动 runstats</td>
						</tr>
						<tr id="LOCKLIST">
							<td>LOCKLIST (4KB)</td>
							<td>锁定列表的最大存储量</td>
						</tr>
						<tr id="Rollforward_pending">
							<td>Rollforward pending</td>
							<td>前滚暂挂</td>
						</tr>
						<tr id="Database_collating_sequence">
							<td>Database collating sequence</td>
							<td>数据库整理顺序</td>
						</tr>
						<tr id="NEWLOGPATH">
							<td>NEWLOGPATH (memory)</td>
							<td>更改数据库日志路径</td>
						</tr>
						<tr id="DYN_QUERY_MGMT">
							<td>DYN_QUERY_MGMT</td>
							<td>动态SQL语句查询管理</td>
						</tr>
						<tr id="INDEXSORT">
							<td>INDEXSORT</td>
							<td>索引排序标志</td>
						</tr>
						<tr id="APPLHEAPSZ">
							<td>APPLHEAPSZ (4KB)</td>
							<td>应用程序堆大小</td>
						</tr>
						<tr id="HADR_LOCAL_SVC">
							<td>HADR_LOCAL_SVC</td>
							<td>HADR 本地服务名称</td>
						</tr>
					</table>	
					</c:if>	 		

					<input id="responseData" type="hidden" value="${db2cclistbase64 }">
					<input id="responseData_DB" type="hidden" value="${db2dbcclistbase64 }">
					<input id="srcDB" type="hidden" value="${srcDB }">

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
		$("#SYSMON_GROUP").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SYSMON_GROUP + "</td>");
		$("#RESTBUFSZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].RESTBUFSZ + "</td>");
		$("#DIAGPATH").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DIAGPATH + "</td>");
		$("#ASLHEAPSZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].ASLHEAPSZ + "</td>");
		$("#MAX_CONNECTIONS").append(
				"<td style=\"text-align: center;\">" + ccObj[i].MAX_CONNECTIONS + "</td>");
		$("#BACKBUFSZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].BACKBUFSZ + "</td>");
		$("#INTRA_PARALLEL").append(
				"<td style=\"text-align: center;\">" + ccObj[i].INTRA_PARALLEL + "</td>");
		$("#UTIL_IMPACT_LIM").append(
				"<td style=\"text-align: center;\">" + ccObj[i].UTIL_IMPACT_LIM + "</td>");
		$("#ALT_DIAGPATH").append(
				"<td style=\"text-align: center;\">" + ccObj[i].ALT_DIAGPATH + "</td>");
		$("#FENCED_POOL").append(
				"<td style=\"text-align: center;\">" + ccObj[i].FENCED_POOL + "</td>");
		$("#INSTANCE_MEMORY").append(
				"<td style=\"text-align: center;\">" + ccObj[i].INSTANCE_MEMORY + "</td>");
		$("#SYSMAINT_GROUP").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SYSMAINT_GROUP + "</td>");
		$("#MAX_QUERYDEGREE").append(
				"<td style=\"text-align: center;\">" + ccObj[i].MAX_QUERYDEGREE + "</td>");
		$("#SYSCTRL_GROUP").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SYSCTRL_GROUP + "</td>");
		$("#DFT_MON_STMT").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_STMT + "</td>");
		$("#NOTIFYLEVEL").append(
				"<td style=\"text-align: center;\">" + ccObj[i].NOTIFYLEVEL + "</td>");
		$("#DFT_MON_LOCK").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_LOCK + "</td>");
		$("#DFT_MON_BUFPOOL").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_BUFPOOL + "</td>");
		$("#DFT_MON_SORT").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_SORT + "</td>");
		$("#DIAGSIZE").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DIAGSIZE + "</td>");
		$("#AUTHENTICATION").append(
				"<td style=\"text-align: center;\">" + ccObj[i].AUTHENTICATION + "</td>");
		$("#SYSADM_GROUP").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SYSADM_GROUP + "</td>");
		$("#DIAGLEVEL").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DIAGLEVEL + "</td>");
		$("#DFT_MON_TABLE").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_TABLE + "</td>");
		$("#DISCOVER").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DISCOVER + "</td>");
		$("#JAVA_HEAP_SZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].JAVA_HEAP_SZ + "</td>");
		$("#MAX_COORDAGENTS").append(
				"<td style=\"text-align: center;\">" + ccObj[i].MAX_COORDAGENTS + "</td>");
		$("#INDEXREC").append(
				"<td style=\"text-align: center;\">" + ccObj[i].INDEXREC + "</td>");
		$("#DFT_MON_UOW").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_UOW + "</td>");
		$("#HEALTH_MON").append(
				"<td style=\"text-align: center;\">" + ccObj[i].HEALTH_MON + "</td>");
		$("#RESYNC_INTERVAL").append(
				"<td style=\"text-align: center;\">" + ccObj[i].RESYNC_INTERVAL + "</td>");
		$("#SHEAPTHRES").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SHEAPTHRES + "</td>");
		$("#AUDIT_BUF_SZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].AUDIT_BUF_SZ + "</td>");
		$("#NUM_INITAGENTS").append(
				"<td style=\"text-align: center;\">" + ccObj[i].NUM_INITAGENTS + "</td>");
		$("#QUERY_HEAP_SZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].QUERY_HEAP_SZ + "</td>");
		$("#DFT_MON_TIMESTAMP").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFT_MON_TIMESTAMP + "</td>");
		$("#RQRIOBLK").append(
				"<td style=\"text-align: center;\">" + ccObj[i].RQRIOBLK + "</td>");
		$("#KEEPFENCED").append(
				"<td style=\"text-align: center;\">" + ccObj[i].KEEPFENCED + "</td>");
		$("#NUM_POOLAGENTS").append(
				"<td style=\"text-align: center;\">" + ccObj[i].NUM_POOLAGENTS + "</td>");
		$("#NUM_INITFENCED").append(
				"<td style=\"text-align: center;\">" + ccObj[i].NUM_INITFENCED + "</td>");
		$("#DFTDBPATH").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DFTDBPATH + "</td>");
		$("#MON_HEAP_SZ").append(
				"<td style=\"text-align: center;\">" + ccObj[i].MON_HEAP_SZ + "</td>");
		$("#AGENTPRI").append(
				"<td style=\"text-align: center;\">" + ccObj[i].AGENTPRI + "</td>");
		$("#DISCOVER_INST").append(
				"<td style=\"text-align: center;\">" + ccObj[i].DISCOVER_INST + "</td>");		
		$("#SVCENAME").append(
				"<td style=\"text-align: center;\">" + ccObj[i].SVCENAME + "</td>");
	}
	//上面语句执行列赋值
	//下面语句执行数值不同的颜色区分
	var tableID = document.getElementById("compTable");
	var arr1 = [];//第三列的值存放在数组arr1里
	var arr2 = [];//第四列的值存放在数组arr2里
	for (var i = 3; i < tableID.rows.length; i++)//从第四行起遍历第三列的值 
	{
		var content = tableID.rows[i].cells[2].innerText;
		arr1.push(content);
	}
	for (var i = 3; i < tableID.rows.length; i++)//从第四行起遍历第四列的值  
	{
		var content = tableID.rows[i].cells[3].innerText;
		arr2.push(content);
	}
	for(var i=0;i<arr1.length;i++)  //开始比较两列的值 
	{
		if(arr1[i] != arr2[i])
		{
			var index = i+3;  //当前行tr的索引 
			$("#compTable tr:eq("+index+") td:eq(2)").css("color","red");
			$("#compTable tr:eq("+index+") td:eq(3)").css("color","red");
		}
	} 
</script>

<script type="text/javascript">
	if($("#srcDB").val() != "-")
	{
		
	/* DB */
	var baseStr_DB = base64decode($("#responseData_DB").val());
	var ccObjDB = eval("(" + baseStr_DB + ")");

	for (var i = 0; i < ccObjDB.length; i++) {
		$("#shuoming_DB").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].name + "</td>");
		$("#ccdatetime_DB").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].compDate
						+ "</td>");
	//	$("#parent_DB").append(
	//			"<td style=\"text-align: center;\">" +ccObjDB[i].parent+ "</td>");
		$("#ENABLE_XMLCHAR").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].ENABLE_XMLCHAR + "</td>");
		$("#AUTORESTART").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTORESTART + "</td>");
		$("#LOCKTIMEOUT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOCKTIMEOUT + "</td>");
		$("#AUTO_PROF_UPD").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_PROF_UPD + "</td>");
		$("#Log_retain_for_recovery_status").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Log_retain_for_recovery_status + "</td>");
		$("#MINCOMMIT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MINCOMMIT + "</td>");
		$("#REC_HIS_RETENTN").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].REC_HIS_RETENTN + "</td>");
		$("#User_exit_for_logging_status").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].User_exit_for_logging_status + "</td>");
		$("#UTIL_HEAP_SZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].UTIL_HEAP_SZ + "</td>");
		$("#DFT_SQLMATHWARN").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_SQLMATHWARN + "</td>");
		$("#HADR_database_role").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_database_role + "</td>");
		$("#SELF_TUNING_MEM").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].SELF_TUNING_MEM + "</td>");
		$("#SEQDETECT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].SEQDETECT + "</td>");
		$("#TSM_MGMTCLASS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].TSM_MGMTCLASS + "</td>");
		$("#MAX_LOG").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MAX_LOG + "</td>");
		$("#AUTO_REVAL").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_REVAL + "</td>");
		$("#HADR_TIMEOUT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_TIMEOUT + "</td>");
		$("#AUTO_DEL_REC_OBJ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_DEL_REC_OBJ + "</td>");
		$("#NUM_QUANTILES").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUM_QUANTILES + "</td>");
		$("#HADR_REMOTE_SVC").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_REMOTE_SVC + "</td>");
		$("#MON_PKGLIST_SZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_PKGLIST_SZ + "</td>");
		$("#NUM_DB_BACKUPS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUM_DB_BACKUPS + "</td>");
		$("#VARCHAR2_COMPAT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].VARCHAR2_COMPAT + "</td>");
		$("#DB_MEM_THRESH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DB_MEM_THRESH + "</td>");
		$("#AUTO_REORG").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_REORG + "</td>");
		$("#ARCHRETRYDELAY").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].ARCHRETRYDELAY + "</td>");
		$("#VENDOROPT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].VENDOROPT + "</td>");
		$("#DFT_EXTENT_SZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_EXTENT_SZ + "</td>");
		$("#NUM_FREQVALUES").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUM_FREQVALUES + "</td>");
		$("#MAXAPPLS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MAXAPPLS + "</td>");
		$("#DECFLT_ROUNDING").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DECFLT_ROUNDING + "</td>");
		$("#MON_OBJ_METRICS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_OBJ_METRICS + "</td>");
		$("#AUTO_MAINT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_MAINT + "</td>");
		$("#ALT_COLLATE").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].ALT_COLLATE + "</td>");
		$("#DBHEAP").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DBHEAP + "</td>");
		$("#NUMBER_COMPAT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUMBER_COMPAT + "</td>");
		$("#DISCOVER_DB").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DISCOVER_DB + "</td>");
		$("#INDEXRECDB").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].INDEXREC + "</td>");
		$("#MAXLOCKS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MAXLOCKS + "</td>");
		$("#Default_number_of_containers").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Default_number_of_containers + "</td>");
		$("#BLK_LOG_DSK_FUL").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].BLK_LOG_DSK_FUL + "</td>");
		$("#TSM_PASSWORD").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].TSM_PASSWORD + "</td>");
		$("#LOGRETAIN").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGRETAIN + "</td>");
		$("#BUFFPAGE").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].BUFFPAGE + "</td>");
		$("#STMTHEAP").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].STMTHEAP + "</td>");
		$("#LOGPRIMARY").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGPRIMARY + "</td>");
		$("#Database_territory").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_territory + "</td>");
		$("#Database_code_page").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_code_page + "</td>");
		$("#DFT_QUERYOPT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_QUERYOPT + "</td>");
		$("#Database_country_region_code").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_country_region_code + "</td>");
		$("#First_active_log_file").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].First_active_log_file + "</td>");
		$("#LOGARCHMETH1").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGARCHMETH1 + "</td>");
		$("#CHNGPGS_THRESH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].CHNGPGS_THRESH + "</td>");
		$("#DFT_LOADREC_SES").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_LOADREC_SES + "</td>");
		$("#TSM_OWNER").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].TSM_OWNER + "</td>");
		$("#AUTO_STATS_PROF").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_STATS_PROF + "</td>");
		$("#DEC_TO_CHAR_FMT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DEC_TO_CHAR_FMT + "</td>");
		$("#CATALOGCACHE_SZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].CATALOGCACHE_SZ + "</td>");
		$("#Path_to_log_files").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Path_to_log_files + "</td>");
		$("#DFT_DEGREE").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_DEGREE + "</td>");
		$("#LOGFILSIZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGFILSIZ + "</td>");
		$("#Backup_pending").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Backup_pending + "</td>");
		$("#SHEAPTHRES_SHR").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].SHEAPTHRES_SHR + "</td>");
		$("#Restrict_access").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Restrict_access + "</td>");
		$("#Statement_concentrator").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Statement_concentrator + "</td>");
		$("#DATE_COMPAT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DATE_COMPAT + "</td>");
		$("#HADR_PEER_WINDOW").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_PEER_WINDOW + "</td>");
		$("#MAXFILOP").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MAXFILOP + "</td>");
		$("#LOGSECOND").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGSECOND + "</td>");
		$("#AUTO_TBL_MAINT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_TBL_MAINT + "</td>");
		$("#HADR_REMOTE_INST").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_REMOTE_INST + "</td>");
		$("#AUTO_DB_BACKUP").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_DB_BACKUP + "</td>");
		$("#DFT_PREFETCH_SZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_PREFETCH_SZ + "</td>");
		$("#MON_LOCKWAIT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_LOCKWAIT + "</td>");
		$("#NUM_IOCLEANERS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUM_IOCLEANERS + "</td>");
		$("#NUM_LOG_SPAN").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUM_LOG_SPAN + "</td>");
		$("#MON_LOCKTIMEOUT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_LOCKTIMEOUT + "</td>");
		$("#TRACKMOD").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].TRACKMOD + "</td>");
		$("#NUM_IOSERVERS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUM_IOSERVERS + "</td>");
		$("#LOGINDEXBUILD").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGINDEXBUILD + "</td>");
		$("#MON_LCK_MSG_LVL").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_LCK_MSG_LVL + "</td>");
		$("#MON_REQ_METRICS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_REQ_METRICS + "</td>");
		$("#LOGARCHMETH2").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGARCHMETH2 + "</td>");
		$("#DLCHKTIME").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DLCHKTIME + "</td>");
		$("#SOFTMAX").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].SOFTMAX + "</td>");
		$("#MIRRORLOGPATH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MIRRORLOGPATH + "</td>");
		$("#Multipage_File_alloc_enabled").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Multipage_File_alloc_enabled + "</td>");
		$("#NUMARCHRETRY").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NUMARCHRETRY + "</td>");
		$("#AUTO_STMT_STATS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_STMT_STATS + "</td>");
		$("#FAILARCHPATH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].FAILARCHPATH + "</td>");
		$("#USEREXIT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].USEREXIT + "</td>");
		$("#Database_page_size").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_page_size + "</td>");
		$("#HADR_LOCAL_HOST").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_LOCAL_HOST + "</td>");
		$("#MON_ACT_METRICS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_ACT_METRICS + "</td>");
		$("#AVG_APPLS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AVG_APPLS + "</td>");
		$("#WLM_COLLECT_INT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].WLM_COLLECT_INT + "</td>");
		$("#LOGBUFSZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOGBUFSZ + "</td>");
		$("#DFT_MTTB_TYPES").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_MTTB_TYPES + "</td>");
		$("#Database_is_consistent").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_is_consistent + "</td>");
		$("#STAT_HEAP_SZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].STAT_HEAP_SZ + "</td>");
		$("#Database_code_set").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_code_set + "</td>");
		$("#SORTHEAP").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].SORTHEAP + "</td>");
		$("#CUR_COMMIT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].CUR_COMMIT + "</td>");
		$("#MON_LW_THRESH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_LW_THRESH + "</td>");
		$("#DATABASE_MEMORY").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DATABASE_MEMORY + "</td>");
		$("#MON_UOW_DATA").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_UOW_DATA + "</td>");
		$("#BLOCKNONLOGGED").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].BLOCKNONLOGGED + "</td>");
		$("#TSM_NODENAME").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].TSM_NODENAME + "</td>");
		$("#PCKCACHESZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].PCKCACHESZ + "</td>");
		$("#HADR_REMOTE_HOST").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_REMOTE_HOST + "</td>");
		$("#APPL_MEMORY").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].APPL_MEMORY + "</td>");
		$("#Restore_pending").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Restore_pending + "</td>");
		$("#DFT_REFRESH_AGE").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DFT_REFRESH_AGE + "</td>");
		$("#HADR_SYNCMODE").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_SYNCMODE + "</td>");
		$("#OVERFLOWLOGPATH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].OVERFLOWLOGPATH + "</td>");
		$("#MON_DEADLOCK").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].MON_DEADLOCK + "</td>");
		$("#AUTO_RUNSTATS").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].AUTO_RUNSTATS + "</td>");
		$("#LOCKLIST").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].LOCKLIST + "</td>");
		$("#Rollforward_pending").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Rollforward_pending + "</td>");
		$("#Database_collating_sequence").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].Database_collating_sequence + "</td>");
		$("#NEWLOGPATH").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].NEWLOGPATH + "</td>");
		$("#DYN_QUERY_MGMT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].DYN_QUERY_MGMT + "</td>");
		$("#INDEXSORT").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].INDEXSORT + "</td>");
		$("#APPLHEAPSZ").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].APPLHEAPSZ + "</td>");
		$("#HADR_LOCAL_SVC").append(
				"<td style=\"text-align: center;\">" + ccObjDB[i].HADR_LOCAL_SVC + "</td>");		
	}
	//上面语句执行列赋值
	//下面语句执行数值不同的颜色区分
	var tableID_DB = document.getElementById("compTable_DB");
	var arr1_DB = [];//第三列的值存放在数组arr1里
	var arr2_DB = [];//第四列的值存放在数组arr2里
	for (var i = 3; i < tableID_DB.rows.length; i++)//从第四行起遍历第三列的值 
	{
		var content = tableID_DB.rows[i].cells[2].innerText;
		arr1_DB.push(content);
	}
	for (var i = 3; i < tableID_DB.rows.length; i++)//从第四行起遍历第四列的值  
	{
		var content = tableID_DB.rows[i].cells[3].innerText;
		arr2_DB.push(content);
	}
	for(var i=0;i<arr1_DB.length;i++)  //开始比较两列的值 
	{
		if(arr1_DB[i] != arr2_DB[i])
		{
			var index = i+3;  //当前行tr的索引 
			$("#compTable_DB tr:eq("+index+") td:eq(2)").css("color","red");
			$("#compTable_DB tr:eq("+index+") td:eq(3)").css("color","red");
		}
	} 
	
	}
</script>
</html>
