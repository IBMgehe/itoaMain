<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.fasterxml.jackson.databind.*"%>
<%@ page import="com.fasterxml.jackson.databind.node.*"%>
<%@ page import="com.ibm.automation.core.util.*"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="header.jsp" flush="true" />
<title>自动化部署平台</title>
<style type="text/css">
.content {
	position:relative;
	float:right;
	width:calc(100% - 57px);
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
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
			<a href="getLogInfo.do" class="current">
				<i class="icon-home"></i>历史执行记录
			</a>
			<a href="#" class="current">任务列表</a>
		</div>
		
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="widget-box collapsible">
						<div class="widget-title">
							<a data-toggle="collapse" href="#collapseOne">
								<span class="icon"> <i class="icon-arrow-right"></i></span>
								<h5>说明：</h5>
							</a>
						</div>
						<div id="collapseOne" class="collapse in">
							<div class="widget-content">历史任务信息列表</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="columnauto">
						<div class="widget-box nostyle">
							<table id="myTable"
								class="table table-bordered data-table with-check table-hover no-search no-select">
								<thead>
									<tr>
										<th style="text-align: center;">序号</th>
										<th style="text-align: center;">主节点</th>
										<th style="text-align: center;">副节点</th>
										<th style="text-align: center;">任务</th>
										<th style="text-align: center;">时间</th>
										<th style="text-align: center;">结果</th>
										<th style="display: none">任务唯一序号</th>
										<th style="text-align: center;">操作</th>
									</tr>
								</thead>
								<tbody>
									<%
										String items = String.valueOf(request.getAttribute("items"));
										ObjectMapper om = new ObjectMapper();
										ArrayNode list = (ArrayNode) om.readTree(items);

										int i = 1;
										for (JsonNode jn : list) {
											ObjectNode node = (ObjectNode) jn;
											String uuid = node.get("uuid").asText();
											String type = node.get("type").asText();
											String mainNodes = "";//= ((ArrayNode) node.get("mainNodes")).toString();
											String subNodes = "";
											ArrayNode anMainnodes = (ArrayNode) node.get("mainNodes");
											ArrayNode anSubnodes = (ArrayNode) node.get("subNodes");
											if (anMainnodes.size() == 0) {
												mainNodes = "-";
											} else {
												for (JsonNode jnNode : anMainnodes) {
													mainNodes += jnNode.asText().toString() + " ";
												}
											}
											if (anSubnodes.size() == 0) {
												subNodes = "-";
											} else {
												for (JsonNode jnNode : anSubnodes) {
													subNodes += jnNode.asText().toString() + " ";
												}
											}

											String created_at = node.get("created_at").asText();
											String status = node.get("status").asText().trim();
									%>
									<tr>
										<td style="text-align: center;"><%=i++%></td>
										<td style="text-align: center;"><%=mainNodes%></td>
										<td style="text-align: center;"><%=subNodes%></td>
										<!-- 原来的值是subNode -->
										<td style="text-align: center;"><%=type%></td>
										<td style="text-align: center;"><%=created_at%></td>

										<%
											if ("0".equals(status)) {
										%>
										<td style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp; <img
											src="img/icons/common/icon_paused.png"> <b>未执行</b>
										</td>
										<%
											} else if ("1".equals(status)) {
										%>
										<td style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp; <img
											src="img/icons/common/icon_resized.png"> <b>执行中</b>
										</td>
										<%
											} else if ("2".equals(status)) {
										%>
										<td style="text-align: center;"><img src="img/icon_success.png"> <b>成功</b></td>
										<%
											} else {
										%>
										<td style="text-align: center;"><img src="img/icon_error.png"> <b>失败</b></td>
										<%
											}
										%>
										<td style="display: none"><%=uuid%></td>
										<td style="text-align: center;"><a
												href="getLogInfoDetail.do?uuid=<%=uuid%>&type=<%=type%>" style="color:#08c;">详细</a></td>

									</tr>

									<%
										}
									%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<%-- <script type="text/javascript">
	function refreshTable()
	{
		var uuids=[];  
		$("#myTable tbody").find('tr').each(function(){
			var tdArr = $(this).children();
			 if (tdArr.eq(5).text().trim() != '失败' && tdArr.eq(5).text().trim() != '成功')
			    {
				 //uuid={};
				 uuids.push(tdArr.eq(6).text().trim());
							}
			 if(uuids.length > 0 )  //大于0 表示存在需要更新的playbook-uuid
			   {
				 $.ajax({
					   url:'<%=path%>/getPlaybookStatus.do',
					   type:'post',
					   dataType:'json',
					  
					   data:{'playbookuuids':uuids},
					   traditional: true, //传递数组用
					   success:function(result)
					   {
						 //  alert(result.msg);
						   if(result.status == 1)  //获取到了数据
						   {
							  
								   $("#myTable tbody").find('tr').each(function(){
									   var tdArr = $(this).children();
									   var obj = eval('(' + result['msg'] + ')'); //转为对象
									   for ( var i = 0 ; i < obj.length;i++)
									   {
										   for(var k in obj[i])
										   {
											   if ( tdArr.eq(6).text().trim() == k){
												   	if(obj[i][k] == 1)  //运行中
												   	{
												   		tdArr.eq(5).html("&nbsp;&nbsp;&nbsp;&nbsp; <img src=\"img/icons/common/icon_resized.png\"> <b>执行中</b>");
												   	}else if (obj[i][k] == 0 )//未运行
												   	{
												   		tdArr.eq(5).html("&nbsp;&nbsp;&nbsp;&nbsp; <img src=\"img/icons/common/icon_paused.png\"><b>未执行</b>");
												   	}else if (obj[i][k] == 2)//完成
												   	{
												   		tdArr.eq(5).html("<img src=\"img/icon_success.png\"> <b>成功</b>");
												   	}else if (obj[i][k] == 3)//失败
												   	{
												   		tdArr.eq(5).html("<img src=\"img/icon_error.png\"> <b>失败</b>");
												   	}
												  	//tdArr.eq(5).text(obj[i][k]);
											   }
										   }
										   
									   }
									  
								   });
						       
						   }
					   },
					   failure:function(errmsg)
					   {
						   
					   }
					   
				   })
			   }
			//alert(tdArr.text())
			
		})
	}
	
	function refreshTable1()
	{
		$("#myTable tbody").find('tr').each(function(){
			var tdArr = $(this).children();
			 if (tdArr.eq(5).text().trim() != '失败' && tdArr.eq(5).text().trim() != '成功')
			 {
				 window.location.reload();  //重新加载页面
			 }
		});
	}
	//window.setInterval('refreshTable()', 3000); //每隔10秒自动刷新一次
</script> --%>


<script type="text/javascript">
<!-- 获取playbook运行状态-->
var websocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
	
    websocket = new WebSocket("ws://127.0.0.1/itoa/updatePlaybookStatus");
}
else {
    alert('当前浏览器 Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    //setMessageInnerHTML("WebSocket连接发生错误");
};

//连接成功建立的回调方法
websocket.onopen = function () {
    //setMessageInnerHTML("WebSocket连接成功");
}

//接收到消息的回调方法
websocket.onmessage = function (event) {
    setMessageInnerHTML(event.data);
}

//连接关闭的回调方法
websocket.onclose = function () {
   // setMessageInnerHTML("WebSocket连接关闭");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
}

//将消息显示在网页上
function setMessageInnerHTML(retData) {
	//uuid,1
	
	var dataList = retData.split(',');
	$("#myTable tbody").find('tr').each(function(){
		 var tdArr = $(this).children();
		 var uuid = tdArr.eq(6).text().trim();//取出uuid
		 if(uuid == dataList[0])
		 {
			 if(dataList[1] == 1){
				 tdArr.eq(5).html("&nbsp;&nbsp;&nbsp;&nbsp; <img src=\"img/icons/common/icon_resized.png\"> <b>执行中</b>"); 
			 }
			else if(dataList[1] == 2){
				 tdArr.eq(5).html("<img src=\"img/icon_success.png\"> <b>成功</b>");
			}
			else if (dataList[1] == 0){
				 tdArr.eq(5).html("&nbsp;&nbsp;&nbsp;&nbsp; <img src=\"img/icons/common/icon_paused.png\"><b>未执行</b>");
		 	}
			else if (dataList[1] == 3){
				 tdArr.eq(5).html("<img src=\"img/icon_error.png\"> <b>失败</b>");
		 	}
		 }
			 
	})
   // document.getElementById('message').innerHTML += innerHTML + '<br/>';
}

//关闭WebSocket连接
function closeWebSocket() {
    websocket.close();
}

/* //发送消息
function send() {
    var message = document.getElementById('text').value;
    websocket.send(message);
} */






</script>


</html>
