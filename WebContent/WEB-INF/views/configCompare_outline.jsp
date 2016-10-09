<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.fasterxml.jackson.databind.*"%>
<%@ page import="com.fasterxml.jackson.databind.node.*"%>
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
.div_cursor{ 
	cursor:pointer; 
}
.table_color { 
	background: rgba(0, 0, 0, 0) linear-gradient(#fdfefe, #f5f9fc) repeat scroll 0 0; 
}
.tooltip { 
	width:110px;word-wrap:break-word; 
}
.label-info{
	background-color:#5bc0de;
}
.label-success{
	background-color:#5cb85c;
}
.label-danger{
	background-color:#d9534f;
}
.scroll{
	overflow-y:scroll;
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
	/* 优化巡检对象IP显示 */ 
	$(document).ready(function(){
		var tableInfo = [];
		var targetStr = $("#tooltipa1").html();
		if(targetStr != null)
		{
			len = targetStr.split(",").length-1;
			if(len >= 3)
			{
				var a = targetStr.split(",");
				var b = a[0] + "&nbsp;&nbsp;&nbsp;" + a[1] + "&nbsp;&nbsp;&nbsp;"+ a[2] + "..."; //获取前3个IP
				$("#tooltipa1").html(b);//将获取到的3个IP替换原来的值   
				$("#tooltipa1").attr('title',(targetStr.split(',').join('<br>')));
			}
			else 
			{
				var c = targetStr.split(',').join('&nbsp;&nbsp;&nbsp;');
				$("#tooltipa1").html(c);//将获取到的3个IP替换原来的值   
			}
		}
		else{
			$("#tooltipa1").html(targetStr);
		}		 
	});
	
	$(function () { $("[data-toggle='tooltip']").tooltip({html : true }); });  //use html true 可以使用<br> 识别br
</script>
<script type="text/javascript">
	$(document).ready(function(){
		//定义所有的ip以及ip对应的巡检信息 
		var allinfo_ip;
		//获取所有的ip以及ip对应的巡检信息 开始
		var confCompDetail_uuid=$("#confCompDetail_uuid").val().trim();
		var confComp_if_daily=$("#confCompDetail_if_daily").val().trim();
		var confCompJobRunResult_datetime=convertdatetime($("#timeselect").find("option:selected").text().trim());
		$.ajax({
			url : "<%=path%>/getconfCompOtherDateData.do?confCompDetail_uuid="
					+ confCompDetail_uuid + "&confCompRunResult_datetime="
					+ confCompJobRunResult_datetime+"&confCompDetail_if_daily="+confComp_if_daily, //获取其他时间段的数据
			type : 'get',
			dataType : 'json',
			success : function(result) {
				//alert(result)
				allinfo_ip = result;
			},
			failure : function(errmsg) {
			}
		})
		console.log(allinfo_ip)
		//获取所有的ip以及ip对应的巡检信息 结束
		//正则表达式过滤ip 开始
		function filterIpByInput(filtersource){
			var filter = $("#filter").val();
			var re =new RegExp("" + filter + "","i");
			return re.test(filtersource);
		}
		//开始筛选ip
		$('#filter').keyup(function(){
			$("#iptable tbody").html(""); //清空table 数据 
			var str = "<tr>";;
			var filterNum = 0;
			$.each(allinfo_ip.iplist, function(index, item) {			
				var filterResult = false;
				filterResult = filterIpByInput(item.confCompJobRunResult_ip);
				if (filterNum != 0 && (filterNum ) % 6 == 0) {
					str += '<tr>';
				}
				if(item.confCompJobRunResult_result == 0 && filterResult){
					str += "<td style='border:0px;'><a style='color: #7BC41D;' href='"+item.confCompJobRunResult_detail+"'>"
						+ item.confCompJobRunResult_ip + "</a></td>";
					filterNum +=1;
				}
				if(item.confCompJobRunResult_result == 1 && filterResult){
					str += "<td style='border:0px;'><a style='color: #249BE0;' href='"+item.confCompJobRunResult_detail+"'>"
					+ item.confCompJobRunResult_ip + "</a></td>";
					filterNum +=1;
				}
				if(item.confCompJobRunResult_result == 2 && filterResult){
					str += "<td style='border:0px;'><a style='color: #F7B007;' href='"+item.confCompJobRunResult_detail+"'>"
						+ item.confCompJobRunResult_ip + "</a></td>";
					filterNum +=1;
				}
				if(item.confCompJobRunResult_result == 3 && filterResult){
					str += "<td style='border:0px;'><div style='color: #F83F03;' id='"+item.confCompJobRunResult_uuid+"' class='div_cursor' onclick='getIPErrMsg(this)'>"
						+ item.confCompJobRunResult_ip + "</div></td>";
					filterNum +=1;
				}
				
				if (filterNum % 6 == 0 || filterNum == 6) {//status.count % 6 eq 0 || status.count eq 6
					str += '</tr>';
				}
				
			});
			$("#iptable tbody").html(str);
			//ip显示部分如果超过5行，添加滚动条
			addScoller();
		})
		//筛选ip 结束
		//ip显示部分如果超过5行，添加滚动条
		addScoller();
	})
	
	//如果超过五行,添加滚动条
	function addScoller(){
		var a = document.getElementById('iptable').getElementsByTagName('tr').length;
		if(a >5)
			$("#flow").addClass("scroll");
		else
			$("#flow").removeClass("scroll");
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
			<a href="" class="current"><i class="icon-home"></i>实例一览</a>
			<a href="#" class="current1">IBM 配置比对</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid" style="border: 1px;">
				<div class="span12">
					<div class="mainmodule">
						<div style="width: 50%; float: left;">
							<div style="height: 33px; line-height: 33px; width: 150px;font-size:13px;">
								<strong>&nbsp;&nbsp;${requestScope.confCompType}比对任务</strong>
							</div>
						</div>
						<div style="width: 50%; float: right;">
							<div style="height: 33px; line-height: 33px; width: 150px;font-size:13px;">
								<strong>&nbsp;&nbsp;任务结果统计</strong>
							</div>
						</div>

						<div style="width: 100%; height: 250px;">
							<table id="sel_tab" class="table table_color" style="line-height: 15px; width: 40%; float: left;">
								<tr>
									<td style="border: 0; width: 150px;">任务执行时间</td>
									<td style="border: 0;">
										<select id="timeselect" style="width: 50%;" onchange="getOtherTimeData(this);">
											<c:forEach items="${runTimeList }" var="time">
												<option value="${time }">${time}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								 <tr>
									<td style="border: 0; width: 150px;">执行对象</td>
									<!-- 去除掉最后一个空格 -->
									<td style="border: 0;">
									    <c:if test="${confCompDetail_groupOrIP eq 'ip' }">
									         <span href="#" id="tooltipa1" data-toggle="tooltip" data-placement="right" title="">${confCompDetail_target }</span>
									    </c:if>
										<c:if test="${confCompDetail_groupOrIP ne 'ip' }">
											 <span>${confCompDetail_groupOrIP }</span>
										</c:if>
									</td>
								</tr> 
								<tr>
									<td style="border: 0; width: 150px;">任务提交人</td>
									<td id="confCompDetail_submited_by" style="border: 0;">${confCompDetail_submited_by }</td>
								</tr>
								<tr>
									<td style="border: 0; width: 150px;">任务执行状态</td>
									<td id="confCompDetail_status" style="border: 0;">
										<c:if test="${confCompDetail_status eq 2 }"><span class="label label-danger">完成</span></c:if>
										<c:if test="${confCompDetail_status eq 1 }"><span class="label label-success">完成</span></c:if> 
										<c:if test="${confCompDetail_status eq 0 }"><span class="label label-info">未开始</span></c:if>
										<c:if test="${confCompDetail_status eq 3 }"><span class="label label-info">运行中</span></c:if>
									</td>
								</tr>
								<tr>
									<td style="border: 0; width: 150px;">任务类型</td>
									<c:if test="${confCompDetail_if_daily == 2 }">
										<td style="border: 0;">每日重复</td>
									</c:if>
									<c:if test="${confCompDetail_if_daily == 1 }">
										<td style="border: 0;">定时触发</td>
									</c:if>
									<c:if test="${confCompDetail_if_daily == 0 }">
										<td style="border: 0;">立即执行</td>
									</c:if>
								</tr>
							</table>

							<div id="main" style="float: right; width: 60%; height: 200px;"></div>
						</div>						
					</div>
				 </div>
				</div>
			</div>
						

		<div class="container-fluid">
			<div class="row-fluid" style="border: 1px;">
				<div class="span12">
					<div class="mainmodule">						
						<!-- add some table -->
						<input id="filter" type="text" class="form-control" placeholder="请输入IP查找" style="height:28px;">
		
						<div id="flow" style="height:180px;">
							<table id="iptable" class="table" style="background-color:#F7FAFD;">
								<c:forEach var="ipstatus" items="${ IPStatusList}" varStatus="status">
									<c:if test="${status.count eq 1 || (status.count-1) % 6 eq 0}">
										<tr>
									</c:if>
									<c:if test="${ipstatus.confCompJobRunResult_result eq 0}">
										<td style="border:0;"><a style="color: #7BC41D;" href="${ipstatus.confCompJobRunResult_detail }">${ipstatus.confCompJobRunResult_ip }</a></td>
									</c:if>
									<c:if test="${ipstatus.confCompJobRunResult_result eq 1}">
										<td style="border:0;"><a style="color: #249BE0;" href="${ipstatus.confCompJobRunResult_detail }">${ipstatus.confCompJobRunResult_ip }</a></td>
									</c:if>
									<c:if test="${ipstatus.confCompJobRunResult_result eq 2}">
										<td style="border:0;"><a style="color: #F7B007;" href="${ipstatus.confCompJobRunResult_detail }">${ipstatus.confCompJobRunResult_ip }</a></td>
									</c:if>
									<c:if test="${ipstatus.confCompJobRunResult_result eq 3}">
										<td style="border:0;"><div id="${ipstatus.confCompJobRunResult_uuid }" class="div_cursor"  style="color: #F83F03;" onclick="getIPErrMsg(this);">${ipstatus.confCompJobRunResult_ip }</div></td>
									</c:if>
									<c:if test="${status.count % 6 eq 0 || status.count eq 6}">
										</tr>
									</c:if>
								</c:forEach>
							</table>
						</div>
		
						<form id="hiddenForm">
							<input type="hidden" id="confCompDetail_uuid" name="confCompDetail_uuid" value="${confCompDetail_uuid }">
							<input type="hidden" id="goodNum" name="goodNum" value="${pieMap.goodNum }">
							<input type="hidden" id="warnNum" name="warnNum" value="${pieMap.warnNum }">
							<input type="hidden" id="errorNum" name="errorNum" value="${pieMap.errorNum }">
							<input type="hidden" id="unreachableNum" name="unreachableNum" value="${pieMap.unreachableNum }">
							<input type="hidden" id="confCompDetail_if_daily" name="confCompDetail_if_daily" value="${confCompDetail_if_daily }">
						</form>
					</div>
				</div>
			</div>
		</div>		

	</div>
</body>

<script type="text/javascript">
//产生pie的数据配置
function createData(value,color,name)
{
	var goodData =  new Object();
	var outerData = new Object();
	var innerData = new Object();
	innerData['color']=color;      //颜色
	outerData['normal']=innerData;
	goodData['value']=value;   //具体数值
	goodData['name']=name;   //good  warn error unreachable
	goodData['itemStyle']=outerData;
	return goodData;
}
option = {
		series : [ {
			name : '访问来源',
			type : 'pie',
			radius : '90%',
			data : []
		} ]
	};
	//基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));
	totalNum = parseInt($("#goodNum").val())+ parseInt($("#warnNum").val())+ parseInt($("#errorNum").val())+parseInt($("#unreachableNum").val());
	if ( $("#goodNum").val() > 0  )
	{
		num4=$("#goodNum").val();
		rate4 = (parseInt(num4)/totalNum).toFixed(2)*100;
		option['series'][0]['data'].push(createData(parseInt(num4),'#7BC41D','良好('+rate4+'%)'));
	}
	if ($("#warnNum").val() > 0 )
	{
		num3=$("#warnNum").val();
		rate3 = (parseInt(num3)/totalNum).toFixed(2)*100;
		option['series'][0]['data'].push(createData(parseInt(num3),'#249BE0','警告('+rate3+'%)'));
	}
	if ($("#errorNum").val() > 0 )
	{
		num2=$("#errorNum").val();
		rate2 = (parseInt(num2)/totalNum).toFixed(2)*100;
		option['series'][0]['data'].push(createData(parseInt(num2),'#F7B007','严重('+rate2+'%)'));
	}
	if ($("#unreachableNum").val() > 0 )
	{
		num1=$("#unreachableNum").val();
		rate1 = (parseInt(num1)/totalNum).toFixed(2)*100;
		option['series'][0]['data'].push(createData(parseInt(num1),'#F83F03','失败('+rate1+'%)'));
	}
	myChart.setOption(option); // 使用刚指定的配置项和数据显示图表
</script>

<script type="text/javascript">
/* 回退时，根据执行时间重新IP */ 
$(document).ready(function(){
	var timeselect = document.getElementById("timeselect");
	getOtherTimeData(timeselect);
}) 

    function convertdatetime(datetime)
    {
    	 return datetime.replace(/[^0-9]/mg, '').match(/.{12}/); 
    }
	function getOtherTimeData(obj){
		var confCompDetail_uuid=$("#confCompDetail_uuid").val().trim();
		var confComp_if_daily=$("#confCompDetail_if_daily").val().trim();
		var confCompJobRunResult_datetime=convertdatetime($(obj).find("option:selected").text().trim());
		$.ajax({
			url : "<%=path%>/getconfCompOtherDateData.do?confCompDetail_uuid="
					+ confCompDetail_uuid + "&confCompRunResult_datetime="
					+ confCompJobRunResult_datetime+"&confCompDetail_if_daily="+confComp_if_daily, //获取其他时间段的数据
			type : 'get',
			dataType : 'json',
			success : function(result) {
				switch(result.confCompDetail_status)
				{
				
				case '0':
					$("#confCompDetail_status").html("<span class='label label-info'>未开始</span>");
					break;
				case '1':
					$("#confCompDetail_status").html("<span class='label label-success'>完成</span>");					
					break;
				case '2':
					$("#confCompDetail_status").html("<span class='label label-danger'>完成</span>"); 
					break;
				default:
					$("#confCompDetail_status").text("未知");					
				}				
				
				$("#confCompDetail_submited_by").text(result.jobDetail_submited_by);
				$("#iptable tbody").html(""); //清空table 数据
				var str;
				$.each(result.iplist, function(index, item) {
					if (index == 0 || (index ) % 6 == 0) {
						str += '<tr>';
					}
					if(item.confCompJobRunResult_result == 0){
						str += "<td><a style='color: #7BC41D;' href='"+item.confCompJobRunResult_detail+"'>"
							+ item.confCompJobRunResult_ip + "</a></td>";
					}
					if(item.confCompJobRunResult_result == 1){
						str += "<td><a style='color: #249BE0;' href='"+item.confCompJobRunResult_detail+"'>"
						+ item.confCompJobRunResult_ip + "</a></td>";
					}
					if(item.confCompJobRunResult_result == 2){
						str += "<td><a style='color: #F7B007;' href='"+item.confCompJobRunResult_detail+"'>"
							+ item.confCompJobRunResult_ip + "</a></td>";
					}
					if(item.confCompJobRunResult_result == 3){
						str += "<td><div id='"+item.confCompJobRunResult_uuid+"' class='div_cursor' style='color: #F83F03;' onclick='getIPErrMsg(this)'>"
							+ item.confCompJobRunResult_ip + "</div></td>";
					}					
					if ((index+1) % 6 == 0 || index == 5) {//status.count % 6 eq 0 || status.count eq 6
						str += '</tr>';
					}
				});
				$("#iptable tbody").html(str);
			},
			failure : function(errmsg) {

			}
		})
	}
</script>
<script type="text/javascript">
	function getIPErrMsg(obj)
	{
		var uuid=$(obj).attr('id');
		$.ajax({
			url : '<%=path%>/getconfCompIPErrMsg.do',
			data:{confCompJobRunResult_uuid:uuid},
			type : 'get',
			dataType : 'json',
			success:function(retMsg)
			{
				ymPrompt.win({
					width: 500, //宽 
					height: 250, //高 
					message : retMsg['errmsg'],
					title : '错误提示！',
					maxBtn:true,
					handler : function(tp) {},
					btn : [ [ '关闭', 'yes' ]]
				});
			},
			failure:function(errMsg)
			{
				
			}
		})
	}
</script>
</html>
