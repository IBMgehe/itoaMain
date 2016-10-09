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
<meta http-equiv="pragma" content="no-cache">    
<meta http-equiv="cache-control" content="no-cache">    
<meta http-equiv="expires" content="0">  
<jsp:include page="header.jsp" flush="true" />
<title>云计算基础架构平台</title>
<style type="text/css">
.sweet-alert button.cancel { 
	background-color: #ec6c62; 
}
.sweet-alert button.cancel:hover { 
	background-color: #E56158; 
}
.tooltip { 
	width:105px; 
	word-wrap:break-word; 
}
.mr20{
	font-size:14px;
}
.content {
	position:relative;
	float:right;
	width:calc(100% - 57px);
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
}
label > div.radio {
    position: relative;
    top:4px;
}
.current1,.current1:hover {
    color: #444444;
}
/* 配置比对样式 */ 
.mid{ vertical-align:middle; margin-right:10px; font-size:12px;}
.fw{ font-weight:normal; font-size:12px;}
.le_top{ width:303px;float:left;margin-left:20px;border:0px; }
.rt_top{ width:303px;float:right;margin-right:20px;border:0px; }
.comm_tit{ height: 33px; line-height: 33px; }
.comm_cont{ margin-top:5px;margin-left:20px; }
.comm_ip{ width:206px;margin-top:5px; }
.comm95{ width: 83px; }
.comm15{ margin-top:15px; }
.comm220{ width: 200px; } 
.commdb{ margin-top:15px;margin-left:-12px;display:none; }
.tabdiv{ margin-top: 10px;margin-left:15px;margin-right:15px; }
</style>

<script>
	$(document).ready(function() {
		(function($) {
			$('#filter').keyup(function() {
				var rex = new RegExp($(this).val(), 'i');
				$('.searchable tr').hide();
				$('.searchable tr').filter(function() {
					return rex.test($(this).text());
				}).show();
			})
		}(jQuery));
	});
</script>

<script>
	/* 优化巡检对象IP显示 */ 
	$(document).ready(function(){
		var tableInfo = [];
		var sel_tab = document.getElementById("sel_tab");
		if(sel_tab.rows.length == 2 && sel_tab.rows[1].cells[0].innerText == 'No data available in table')  //当行数为2 并且bootstrap 显示一条空的情况，不做下面的 
		{
			return;
		}
		for (var i = 1; i < sel_tab.rows.length; i++) //遍历Table的所有Row
		{    
	         tableInfo.push(sel_tab.rows[i].cells[2].innerText);   //获取Table中第三列单元格的内容 
	    }
		for(var j=0;j<tableInfo.length;j++)
		{
			var len = (tableInfo[j].split(",").length)-1;//获取每个td里头IP的个数 
			if(len >= 3)
			{
				var a = tableInfo[j].split(",");
				var b = a[0] + "&nbsp;&nbsp;&nbsp;" + a[1] + "&nbsp;&nbsp;&nbsp;"+ a[2] + "..."; //获取前3个IP
				$("#sel_tab tbody tr:eq("+j+") td:eq(2) span").html(b);//将获取到的3个IP替换原来的值   
				var sortedArray=tableInfo[j].split(",").join("<br>");
				$("#sel_tab tbody tr:eq("+j+") td:eq(2) span").attr('title',sortedArray);
			}
			else 
			{
				var c = tableInfo[j].split(',').join('&nbsp;&nbsp;&nbsp;');
				$("#sel_tab tbody tr:eq("+j+") td:eq(2)").html(c);//将获取到的3个IP替换原来的值   
			}
		} 
	});
	
	$(function () { $("[data-toggle='tooltip']").tooltip({html : true }); });  //use html true 可以使用<br>
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
							<div class="widget-content">配置比对概要信息.</div>
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
							<div class="col-sm-6 form-inline">
								<input id="filter" type="text" class="form-control" placeholder="请输入过滤项" style="height:28px;">
								<span style="margin-right: 4px;"></span>
								<button class="btn btn-sm" data-toggle="modal" data-target="#detail" style="background-color: #448FC8;">
									<font color="white">创建配置跟踪任务</font>
								</button>
								<span style="margin-right: 4px;"></span>
								<button onclick="deleteJobs();" class="btn btn-sm" data-toggle="modal" style="background-color: #448FC8;">
									<font color="white">删除任务</font>
								</button>	
								<span style="margin-right: 4px;"></span>
								<button class="btn btn-sm" data-toggle="modal" data-target="#create" style="background-color: #448FC8;">
									<font color="white">配置比对</font>
								</button>							
							</div>

							<div style="margin-bottom: 10px;"></div>
							<table id="sel_tab" class="table table-bordered data-table with-check table-hover no-search no-select">
								<thead>
									<tr>
										<th style="text-align: center;">序号</th>
										<th style="text-align: center;">检查类型</th>
										<th style="text-align: center;">检查对象</th>
										<th style="text-align: center;">计划时间</th>
										<th style="text-align: center;">上一次执行时间</th>
										<th style="text-align: center;">任务类型</th>
										<th style="text-align: center;">提交人</th>
										<th style="text-align: center;">操作</th> 
									</tr>
								</thead>

								<tbody class="searchable">
									 <c:forEach items="${jobs }" var="job" varStatus="status">
										<tr>
											<td style="text-align: center;">
												<input type="checkbox" name="servers"
													 value="${job.confComp_uuid }" onclick="isSelect(this);" />
											</td>
											<td style="text-align: center;">${job.confComp_type }</td>	
											<td style="text-align: center;">
											   <c:if test="${job.confComp_groupOrIP eq 'ip' }">
											 	 <span href="#" class="tooltipa1" data-toggle="tooltip" data-placement="right" title="">${job.confComp_target }</span>
											   </c:if>
											   <c:if test="${job.confComp_groupOrIP ne 'ip' }">
											 	 <span>${job.confComp_groupOrIP }</span>
											   </c:if>
			   								</td>										
											<td style="text-align: center;">${job.confComp_scheduled_at}</td>
											<td style="text-align: center;">${job.confComp_lastrun_at}</td>
											<c:if test="${job.confComp_if_daily == 0 }">
												<td style="text-align: center;">立即执行</td>
											</c:if>
											<c:if test="${job.confComp_if_daily == 2 }">
												<td style="text-align: center;">每日执行</td>
											</c:if>
											<td style="text-align: center;">${job.confComp_submited_by}</td>
										    <td style="text-align: center;"><a href="${job.confComp_detail }" style="color:#08c;">详情</a></td> 
											</tr>							
									</c:forEach> 
								</tbody>
							</table>


							<!-- 创建比对任务 -->
							<div class="modal fade" id="detail" tabindex="-1" role="dialog"
								aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<!-- 头部 -->
										<div class="modal-header">											
											<h5 class="modal-title" id="myModalLabel">
												<img src="<%=path%>/img/plus15.png">&nbsp;&nbsp;跟踪配置任务
											</h5>
										</div>

										<!-- 主体内容 -->
										<form action="" method="post" id="submits_jobs">
											<div class="modal-body">
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">跟踪类型：</span>
														<select id="confComp_type" class="w85" style="width: 220px;" name="confComp_type"
															onchange="change_type(this)">
															<option value="WAS">WAS</option>
															<option value="MQ">MQ</option>
															<option value="DB2">DB2</option>
															<option value="OS">OS</option>
														</select>
													</div>
												</div>
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">跟踪对象：</span>
														<div class="inblock mr20">
															<label><input type="radio" checked value="group" name="select" onClick="sortRadio1()">比对对象组</label>
														</div>
														&nbsp;&nbsp;
														<div class="inblock mr20">
															<label><input type="radio" value="ip" name="select" onClick="sortRadio2()">比对对象IP</label>
														</div>
													</div>
												</div>
												<div class="control-group" id="group_ip">
													<div class="controls">
														<span class="input140 mr20"></span>
													  	<input class="form-control" type="text" name="allip" id="allip"
														       value="" readonly="readonly" style="height:28px;width: 220px;"/> 
													</div>
												</div>
												<div class="control-group" id="single_id" style="display:none;">
													<div class="controls">														       
														<span class="input140 mr20"></span>
														<select id="confComp_target" class="w85" multiple style="width: 220px;" name="confComp_target">															
														</select>
													</div>
												</div>
												<!-- <div class="control-group">
													<div class="controls">
														<span class="input140 mr20">比对时间：</span>
														<input type="checkbox" id="confComp_date" name="confComp_date" onclick="isSelectDate(this);" />
														设置时间 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<div style="float: right; margin-right: 150px; display: none;" id="every">
															<input type="checkbox" id="repeat" name="repeat" onclick="selectTime(this)" />
															每日重复
														</div>
													</div>
												</div>  -->
												
												<div class="control-group">
													<div class="controls">
														<span class="input140 mr20">比对时间：</span>														
														<div class="inblock mr20">
															<label><input type="radio" id="repeat" name="repeat" value="0" onClick="isSelectDate(this);" checked />立即执行</label>
														</div>
														<div class="inblock mr20">
															<label><input type="radio" id="repeat" name="repeat" value="2" onClick="selectTime(this)" />每日重复</label>
														</div> 
													</div>
												</div>
												
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20"></span>
														<input class="form-control" type="text" id="pre" name="next" 
														       value="立即执行" readonly="readonly" style="height:28px;width: 220px;"/>
														<input class="form-control" type="text" id="datetimepicker" name="datetimepicker"
															   value="" readonly="readonly" style="display: none;height:28px;width: 220px;" />
														<!-- <input class="form-control" type="text" id="next" name="next" value=""
															readonly="readonly" style="display: none;" /> -->
													</div>
												</div>
											</div>
										</form>
									</div>

									<div class="modal-footer">
										<button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeModal();">关闭</button>
										<button type="button" class="btn" style="background-color: #448FC8;" onclick="CheckInput();">
											<font color="white">提交</font>
										</button>
									</div>
								</div>
							</div>
							<!-- 创建比对任务 -->
							

							<!-- 模态弹框：配置比对 -->
							<div class="modal fade" id="create" aria-labelledby="myModalLabel" aria-hidden="true" style="width:760px;left:42%;">
							   <div class="modal-dialog">
							      <div class="modal-content">
							      
							         <!-- 头部 -->
									 <div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										<h5 class="modal-title" id="myModalLabel">
											<img src="<%=path%>/img/plus15.png">&nbsp;&nbsp;配置比对
										</h5>
									 </div>
									 
									 <!-- 主体内容 -->
									 <form action="<%=path%>/compareConf.do" method="post" id="submits" autocomplete="off">
									    <div class="modal-body">
									    
									    	<!-- 左侧比对方 -->
											<div class="mainmodule le_top">
												<div class="comm_tit">
													<strong>&nbsp;&nbsp;请选择比对方：</strong>
												</div>	
												
												<div class="comm_cont">
													<div style="padding-left:35px;">
														<span class="mid">IP</span>
														<!-- <input type="text" id="srcIP" name="srcIP" autocomplete="off"  placeholder="请输入IP"
														       class="comm_ip awesomplete" list="srcip" />	
														<datalist id="srcip"></datalist>  -->					
													<select class="w85" multiple="multiple" style="width: 200px;" id="srcIP" name="srcIP" onchange="showSrcIPInfo(this)">
														<c:forEach items="${totalIPList }" var="ip">
															<option value="${ip }">${ip}</option>
														</c:forEach>
													
													</select>
														
													</div>
						
													<div style="margin-top:10px;">									
														<span class="mid">比对产品</span>
														<select class="comm95" id="srcProduct" name="srcProduct" onchange="showSrcParam(this,'src','version','srcProductVer')">		
														<option value="-" selected="selected">请选择</option>								
															<!-- <option value="MQ" selected="selected">MQ</option>
															<option value="WAS">WAS</option>
															<option value="DB2">DB2</option> -->
														</select>	
														&nbsp;—&nbsp;									
														<select class="comm95" id="srcProductVer" name="srcProductVer" onchange="showSrcParam(this,'src','datetime','srcDatetime')">
														<option value="-" selected="selected">请选择</option>
															<!-- <option value="9.7">V 9.7.0.2</option>
															<option value="10.5">V 10.5</option> -->
														</select>														
													</div>
													
													<div class="comm15">
														<span class="mid">比对日期</span>
														<select class="comm220" id="srcDatetime" name="srcDatetime" onchange="showSrcParam(this,'src','instance','srcInstance')">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="2016-08-08 10:20">2016-08-08 10:20</option> -->
														</select>							
													</div>
													
													<div class="comm15">								
														<span class="mid">比对实例</span>
														<select class="comm220" id="srcInstance" name="srcInstance" onchange="showSrcParam(this,'src','database','srcDB')">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="inst1">inst1</option>
															<option value="inst2">inst2</option> -->
														</select>															
													</div>
													
													<div class="commdb" id="dbLeft">	
														<span class="mid">比对数据库</span>							
														<select class="comm220" id="srcDB" name="srcDB">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="db1">db1</option>
															<option value="db2">db2</option> -->
														</select>															
													</div>
												</div>										
											</div>
											
											<!-- 右侧比对方 -->
											<div class="mainmodule rt_top">
												<div class="comm_tit">
													<strong>&nbsp;&nbsp;请选择被比对方：</strong>
												</div>	
												
												<div class="comm_cont">
													<div style="padding-left:35px;">
														<span class="mid">IP</span>
														<!-- <input type="text" id="targetIP" name="targetIP" placeholder="请输入IP"
														       class="comm_ip awesomplete" list="targetip"/>	
														<datalist id="targetip"></datalist> 	 -->	
														<select class="w85" multiple="multiple" style="width: 200px;" id="targetIP" name="targetIP" onchange="showTargetIPInfo(this)">
														<c:forEach items="${totalIPList }" var="ip">
															<option value="${ip }">${ip}</option>
														</c:forEach>
													
													</select>
													</div>
						
													<div style="margin-top:10px;">
														<span class="mid">比对产品</span>
														<select class="comm95" id="targetProduct" name="targetProduct" onchange="showTargetParam(this,'target','version','targetProductVer')">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="MQ" selected="selected">MQ</option>
															<option value="WAS">WAS</option>
															<option value="DB2">DB2</option> -->
														</select>	
														&nbsp;—&nbsp;									
														<select class="comm95" id="targetProductVer" name="targetProductVer" onchange="showTargetParam(this,'target','datetime','targetDatetime')">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="9.7">V 9.7.0.2</option>
															<option value="10.5">V 10.5</option> -->
														</select>					
													</div>
													
													<div class="comm15">
														<span class="mid">比对日期</span>
														<select class="comm220" id="targetDatetime" name="targetDatetime" onchange="showTargetParam(this,'target','instance','targetInstance')">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="2016-08-08 10:20">2016-08-08 10:20</option> -->
														</select>							
													</div>
													
													<div class="comm15">								
														<span class="mid">比对实例</span>
														<select class="comm220" id="targetInstance" name="targetInstance" onchange="showTargetParam(this,'target','database','targetDB')">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="inst1">inst1</option>
															<option value="inst2">inst2</option> -->
														</select>														
													</div>
													
													<div class="commdb" id="dbRight">
														<span class="mid">比对数据库</span>									
														<select class="comm220" id="targetDB" name="targetDB">
															<option value="-" selected="selected">请选择</option>
															<!-- <option value="db1">db1</option>
															<option value="db2">db2</option> -->
														</select>															
													</div>
												</div>										
											</div>							
															    	
									    </div>
									 </form> 
									 
									 <!-- 结尾 --> 
									 <div class="modal-footer">
										 <button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeModal();">关闭</button>
										 <button type="button" class="btn" style="background-color:#448FC8;" onclick="Check();">
											<font color="white">提交</font>
										 </button>
									 </div>
									            
							      </div>
							   </div>
							</div>
							<!-- 模态弹框：创建配置比对 -->
									
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script>
	/* 提取sweet提示框代码，以便后面方便使用，减少代码行数 */ 
	function sweet(te,ty,conBut)
	{
		swal({ title: "", text: te,  type: ty, confirmButtonText: conBut, });
	}
</script>

<script type="text/javascript">
	$('#datetimepicker').datetimepicker({
		datepicker:true,
		format:'y/m/d H:i'
	});
	var infoId = [];
	function isSelect(s) {
		if ($(s).attr("checked")) {
			infoId.push(s.value);
		} else {
			var index = 0;
			for (var i = 0; i < infoId.length; i++) {
				if (s.value == infoId[i]) {
					index = i;
				}
			}
			infoId.splice(index, 1);
		}
		console.log(infoId);
	}

    function change_type(obj)
    {
    	var jobtype = obj.value;
    	if(jobtype == "WAS")
    	{
    		$("#allip").val("WAS Group");
    	}   
    	if(jobtype == "MQ")
    	{
    		$("#allip").val("MQ Group");
    	}
    	if(jobtype == "DB2")
    	{
    		$("#allip").val("DB2 Group");
    	}
    	if(jobtype == "OS")
    	{
    		$("#allip").val("OS Group");
    	}
    	$("#confComp_target").empty();
    	$(".select2-search-choice").css("display","none");
    	$.ajax({
			url : '<%=path%>/getIPByGroup.do',
			data:{groupName :jobtype},
			type : 'get',
			dataType : 'json',
			success:function(result)
			{
				var str;
				for (var i = 0; i < result.length; i++) 
				{					
					str += "<option value='" + result[i] + "'>" + result[i]+ "</option>";
				}	
				$("#confComp_target").append(str);	
			},
			failure:function(errmsg)
			{
				alert(errmsg)
			}
    	});
    } 
    
	function isSelectDate(s)
	{
		$("#pre").show();
		$("#datetimepicker").hide();
		/* if ($(s).attr("checked"))
		{
			$("#every").show();//出现每日重复
			$("#pre").hide();
			$("#next").hide();
			$("#pre").val("");
			$("#next").val("");
			$("#datetimepicker").show();
			$("#datetimepicker").val("点击选择时间和日期"); 
			$('#datetimepicker').datetimepicker();
			$('#datetimepicker').datetimepicker({
				 minDate: 0,
				 minTime:0
			});
		}
		if(!($(s).attr("checked")))
		{
			$("#every").hide();//隐藏每日重复 
			$("#datetimepicker").hide();
			$("#pre").show();			
			$("#next").hide();
			$("#pre").val("立即执行");
			$("#next").val("");
			$("#datetimepicker").hide();
			$("#datetimepicker").val(""); 
		} */
	}
	
	function selectTime(s)
	{
		$("#pre").hide();
		$("#datetimepicker").show();
		$("#datetimepicker").val("点击选择时间");
		$('#datetimepicker').datetimepicker({
			datepicker:false,
			format:'H:i',
			step:10
		});
		$('#datetimepicker').datetimepicker({
			 minTime:0
		});
		/* if($(s).attr("checked"))
		{
			$("#datetimepicker").val("");
			$("#pre").hide();
			$("#pre").val("");			
			$('#datetimepicker').datetimepicker({
				datepicker:false,
				format:'H:i',
				step:10
			});
			$("#datetimepicker").val("点击选择时间");  
			$('#datetimepicker').datetimepicker({
				 minTime:0
			});
		}
		if(!($(s).attr("checked")))
		{
			$("#datetimepicker").val("");
			$('#datetimepicker').datetimepicker({
				datepicker:true,
				format:'y/m/d H:i'
			});
			$("#datetimepicker").show();
			$("#pre").hide();
			$("#pre").val("");
			$("#next").hide();
			$("#next").val("");
			$("#datetimepicker").val("点击选择时间和日期"); 
			$('#datetimepicker').datetimepicker({
				 minDate: 0,
				 minTime:0
			});
		} */
	}
	
	function queryJobDetail(s)
	{
		//根据每个job的uuid查询这个job的执行情况
		var jobUUID = $(s).find('td').find(':checkbox').val();
		$("#jobDetailForm tbody").empty("");
		$.ajax({
			url : '<%=path%>/getJobDetail.do',
			data:{jobUUID :jobUUID},
			type : 'post',
			dataType : 'json',
			success:function(result)
			{
				var tabLen=result.length;
				for(var i = 0 ; i < tabLen;i++)
				{
					var row ;
					if (result[i].jobDetail_status == 1)
						aRow="<tr><td>"+i+"</td><td>"+result[i].jobDetail_scheduled_at+"</td><td>成功</td><td>"+result[i].jobDetail_submited_by+"</td><td><a href="+result[i].jobDetail_detail+">详情</a></td></tr>";
					else if (result[i].jobDetail_status == 2)
						aRow="<tr><td>"+i+"</td><td>"+result[i].jobDetail_scheduled_at+"</td><td>失败</td><td>"+result[i].jobDetail_submited_by+"</td><td><a href="+result[i].jobDetail_detail+">详情</a></td></tr>";
					else if (result[i].jobDetail_status == 0)	
						aRow="<tr><td>"+i+"</td><td>"+result[i].jobDetail_scheduled_at+"</td><td>未开始</td><td>"+result[i].jobDetail_submited_by+"</td><td><a href="+result[i].jobDetail_detail+">详情</a></td></tr>";
					$("#jobDetailForm tbody").append(aRow);
				}				
			},
			failure:function(err)
			{
				
			}
		});
		$("#tab").modal();
	}
	
	//模态弹框，关闭按钮功能 
	function closeModal()  
	{
		window.location.href = "configCompare.do";
	}
	
	function transData(infoId){
    	var data="";
		for(var i = 0 ; i < infoId.length;i++)
		{
	   	    data+=infoId[i]+',';
		}
		var length=data.lastIndexOf(',')	
		return data.substr(0,length)
	}
	
	function deleteJobs()
	{
		if (infoId.length>=1)
		{
			swal({
				title: "", 
				text: "是否确认删除任务？", 
				type: "warning",
				showCancelButton: true,
				closeOnConfirm: false,
				confirmButtonText: "是",
	            cancelButtonText: "否",
			}, function() {
				$.ajax({
					url : "<%=path%>/deleteConfCompJob.do",
					data : {job_uuid :transData(infoId),operType:'deleteCfgCompJob' },
					type : 'post',
					dataType : 'json'
				}).done(function(result) {
					if(result['status']==1)
					{
						swal({
							title: "",
							text: "删除成功！", 
							type: "success",
							confirmButtonText: "确定",  
							confirmButtonColor: "#AEDEF4"
						},
						function(isConfirm)
						{
							  if (isConfirm) 
							  {
								 window.location.href = "configCompare.do";
							  } 
						});				
					}
				}).error(function(result) {
					swal({
						title: "",
						text: "删除失败！", 
						type: "error",
						confirmButtonText: "确定",  
						confirmButtonColor: "#AEDEF4"
					});
				});
			})
		}
		else
		{
			sweet("请至少选择一个任务!","warning","确定");
		}
	}
	
	function CheckInput()
	{
		var grouporip = $('input[name="select"]:checked ').val().trim(); //group or ip
		//如果选择了ip 巡检对象不能为空
		var confComp_target = $("#confComp_target").val();  //判断为null 表示没选IP
		if ( (grouporip=='ip' && confComp_target == null) )
		{
			sweet("巡检对象不能为空!","warning","确定");
			return;
		}
		//如果选择了组的话		
 		$.ajax({
			url : '<%=path%>/createConfCompJob.do',
				data : $('#submits_jobs').serialize(),
				type : 'post',
				dataType : 'json',
				success : function(result) {
					if (result.msg == 'success') {
						swal({
							title : "",
							text : "创建成功!",
							type : "success",
							confirmButtonText : "确定",
						},function(isConfirm){
							if (isConfirm) {
								window.location.href = "configCompare.do";
							}
						})
					} else {
						swal({
							title : "",
							text : "创建失败!",
							type : "error",
							showCancelButton : true,
							cancelButtonText : "取消",
						})
					}
				},
				failure : function(err) {
					alert(err)
				}
			}) 
		}
	</script>
	
<script>
	$(document).ready(function(){
		$("#allip").val("WAS Group");
	})

	function sortRadio1(){
		$("#single_id").hide();
		$("#group_ip").show();
		var jobtype = $("#confComp_type").val();
    	if(jobtype == "WAS")
    	{
    		$("#allip").val("WAS Group");
    	}   
    	if(jobtype == "MQ")
    	{
    		$("#allip").val("MQ Group");
    	}
    	if(jobtype == "DB2")
    	{
    		$("#allip").val("DB2 Group");
    	}
    	if (jobtype == 'OS')
    	{
    		$("#allip").val("OS Group");
    	}
	}
	
	function sortRadio2(){
		$("#group_ip").hide();
		$("#single_id").show();
		$("#allip").val("");
	}
	
	$("#detail").on("show",function(){
		var jobtype = $("#confComp_type").val();
		$("#confComp_target").empty();
    	$.ajax({
			url : '<%=path%>/getIPByGroup.do',
			data:{groupName :jobtype},
			type : 'get',
			dataType : 'json',
			success:function(result)
			{
				var str;
				for (var i = 0; i < result.length; i++) 
				{					
					str += "<option value='" + result[i] + "'>" + result[i]+ "</option>";
				}	
				$("#confComp_target").append(str);	
			},
			failure:function(errmsg)
			{
				alert(errmsg)
			}
    	});
	})
</script>
<!-- 配置比对按钮 -->
<script>
	//左侧比对产品，如果是DB2就显示"比对数据库"，否则不显示
	function product_left()
	{
		var srcProduct = $("#srcProduct").val().trim();
		if(srcProduct == "db2")  
		{
			$("#dbLeft").show();
		}
		else
			$("#dbLeft").hide();
	}
	
	//右侧比对产品，如果是DB2就显示"比对数据库"，否则不显示 
	function product_right(s)
	{
		var targetProduct = $("#targetProduct").val().trim(); 
		if(targetProduct == "db2")   
		{
			$("#dbRight").show();
		}
		else
			$("#dbRight").hide();
	}
</script>
<script>
	//提交校验 
	function Check()
	{
		//验证IP不为空
		var ipLeft = $("#srcIP").val();
		var ipRight = $("#targetIP").val();
		if((ipLeft == "") || (ipRight == ""))
		{
			swal("", "请选择比对IP", "warning");  
		}
		
		//验证"比对产品"和"版本"
		var proLeft = $("#srcProduct").val().trim();
		var proRight = $("#targetProduct").val().trim();
		var verLeft = $("#srcProductVer").val().trim();
		var verRight = $("#targetProductVer").val().trim();
		if(proLeft != proRight)
		{
			swal("", "请选择相同比对产品", "warning"); 
		}
		if((proLeft == proRight) && (verLeft != verRight))
		{
			swal("", "请选择相同比对版本", "warning");  
		}
		
		//比对日期,不能为空
		var dateLeft = $("#srcDatetime").val();
		var dateRight = $("#targetDatetime").val();
		if((dateLeft == "-")  || (dateRight == "-")) 
		{
			swal("", "请选择比对日期", "warning"); 
		}
		
		//比对实例，不能为空，且必须相同 
		var instLeft = $("#srcInstance").val();
		var instRight = $("#targetInstance").val();
		if((instLeft == "-") || (instRight == "-"))
		{
			swal("", "请选择比对实例", "warning");
		}
		if((instLeft != "-") && (instRight != "-") && (instLeft != instRight))
		{
			swal("", "请选择相同的比对实例", "warning");
		}
		
		//选择DB2时，比对数据库，不能为空且必须相同
		var DBLeft = $("#srcDB").val();
		var DBRight = $("#targetDB").val();
		if(((proLeft == "DB2") && (proRight == "DB2")) && ((DBLeft == "-") || (DBRight == "-")))
		{
			swal("", "请选择比对数据库", "warning");
		}
		if((DBLeft != "-") && (DBRight != "-") && (DBLeft != DBRight))
		{
			swal("", "请选择相同的比对数据库", "warning");
		}
		
	/*	$.ajax({
				url : '<%=path%>/compareConf.do',
				data : $('#submits').serialize(),
				type : 'post',
				dataType : 'json',
				success:function(msg){
					//alert(msg)
				},
				failure:function(err)
				{
					
				}
		});
		*/
		$("#submits").submit();   
	}
</script>
<script type="text/javascript">


function showSrcIPInfo(obj)  // 显示目标IP 的产品版本信息
{
	var json;//返回的信息包括版本、产品名、实例名
	//var ip = ;//获取IP
	var info={type:'all',ip:$(obj).val()};
	var ip = $(obj).val();     //如果ip 不为空才发请求
	if(ip != null)
	{
	$.ajax({
		url : '<%=path%>/getIPConfigInfo.do',
		type : 'post',
		data:info,
		dataType : 'json',
		traditional:true,
		success:function(result){
			if(result.length != 0)
			{
			   // json= jQuery.parseJSON(result['msg']);// str to json
			    //alert(json)
			    $("#srcProduct").empty();  //要先清空
			    $("#srcProduct").append('<option value="-" selected="selected">请选择</option>');
				for(var i = 0 ; i < result.length;i++)  
				{
					
					$("#srcProduct").append('<option value='+result[i]+'>'+result[i]+'</option>')
				}
			}
			else{
				//swal("", "没有查询到相关数据", "warning");
				//如果是空的话，要进行select 复位
				 $("#srcProduct").empty();  //要先清空
				//    $("#srcProduct").append('<option value="-" selected="selected">请选择</option>');
			}
		},
		failure:function(err)
		{
			
		}
	});
	}else{ 
		//如果是反选要清空
		 $("#srcProduct").empty();  //要先清空
		 $("#srcProductVer").empty();
		 $("#srcDatetime").empty();
		 $("#srcInstance").empty();
		 $("#srcDB").empty();
		
		 $("#srcProduct").append('<option value="-" selected="selected">请选择</option>');
		 $("#srcProductVer").append('<option value="-" selected="selected">请选择</option>');
		 $("#srcDatetime").append('<option value="-" selected="selected">请选择</option>');
		 $("#srcInstance").append('<option value="-" selected="selected">请选择</option>');
		 $("#srcDB").append('<option value="-" selected="selected">请选择</option>');
		 $("#srcProduct").parent().find('a').find('span').text('请选择');
		 $("#srcDatetime").parent().find('a').find('span').text('请选择');
		 $("#srcInstance").parent().find('a').find('span').text('请选择');
		 $("#srcDB").parent().find('a').find('span').text('请选择');
		 product_left();
	
	}
}







function showTargetIPInfo(obj)  // 显示目标IP 的产品版本信息
{
	var json;//返回的信息包括版本、产品名、实例名
	//var ip = ;//获取IP
	var info={type:'all',ip:$(obj).val()};
	var ip = $(obj).val();     //如果ip 不为空才发请求
	if(ip != null)
	{
	$.ajax({
		url : '<%=path%>/getIPConfigInfo.do',
		type : 'post',
		data:info,
		dataType : 'json',
		traditional:true,
		success:function(result){
			if(result.length != 0)
			{
			   // json= jQuery.parseJSON(result['msg']);// str to json
			    //alert(json)
			    // $("#targetProduct").empty();  //要先清空
			    
			    $("#targetProduct").empty();  //要先清空
			    $("#targetProduct").append('<option value="-" selected="selected">请选择</option>');
				for(var i = 0 ; i < result.length;i++)  
				{
					
					$("#targetProduct").append('<option value='+result[i]+'>'+result[i]+'</option>')
				}
			}
			else{
				//swal("", "没有查询到相关数据", "warning");
				//如果是空的话，要进行select 复位
			}
		},
		failure:function(err)
		{
			
		}
	});
	}else{ 
		//如果是反选要清空
		 $("#targetProduct").empty();  //要先清空
		 $("#targetProductVer").empty();
		 $("#targetDatetime").empty();
		 $("#targetInstance").empty();
		 $("#targetDB").empty();
		 $("#targetProduct").append('<option value="-" selected="selected">请选择</option>');
		 $("#targetProductVer").append('<option value="-" selected="selected">请选择</option>');
		 $("#targetDatetime").append('<option value="-" selected="selected">请选择</option>');
		 $("#targetInstance").append('<option value="-" selected="selected">请选择</option>');
		 $("#targetDB").append('<option value="-" selected="selected">请选择</option>');
		 $("#targetProduct").parent().find('a').find('span').text('请选择');
		 $("#targetDatetime").parent().find('a').find('span').text('请选择');
		 $("#targetInstance").parent().find('a').find('span').text('请选择');
		 product_right();
	}
}



</script>

<script type="text/javascript">
//这个方法主要用于  产品、版本、实例获取相应信息   //obj 代表哪个html 组件    srcOrTarget 代表是比对方还是被比对方 type 是这个html 组件的类型   product /version /datetime/
	function convertDatetime(datetime)
	{
		var year = datetime.substring(0,4);
		var month= datetime.substring(4,6);
		var day= datetime.substring(6,8);
		var shi=datetime.substring(8,10);
		var fen = datetime.substring(10,12);
		return year+'-'+month+'-'+day+' ' + shi+":"+fen
	}
	function callAjax(info,obj) //obj 是哪个组件
	{
		$.ajax({
			url : '<%=path%>/getIPConfigInfo.do',
			type : 'post',
			data:info,
			dataType : 'json',
			traditional:true,
			success:function(result){
				if(result.length != 0)
				{
					$("#"+obj).empty();
					 $("#"+obj).append('<option value="-" selected="selected">请选择</option>');
					var regExp=/^\d{4}(0[1-9]|1[1-2])(0[1-9]|2[0-9]|3[0-1])([0-2][0-9])([0-5][0-9])$/;
					 for(var i = 0 ; i < result.length;i++)  
					{
						if(regExp.test(result[i]))
							{
								$("#"+obj).append('<option value='+result[i]+'>'+convertDatetime(result[i])+'</option>')
							}
						else
							$("#"+obj).append('<option value='+result[i]+'>'+result[i]+'</option>')
					} 
				}
				else{
					//swal("", "没有查询到相关数据", "warning");
					//如果是空的话，要进行select 复位
				}
			},
			failure:function(err)
			{
				
			}
		});
	}
	
	//需要使用递归删除其他数据
	//nextObj 代表  srcProduct srcProductVer srcDatetime  srcInstance srcDB
	function deleteHTMLElement(srcOrTarget,nextObj)
	{
		var totalArray;
		if(srcOrTarget=='src')
		 totalArray = new Array("srcProduct","srcProductVer","srcDatetime" ,"srcInstance","srcDB");
		if(srcOrTarget =='target')
		 totalArray = new Array("targetProduct","targetProductVer","targetDatetime" ,"targetInstance","targetDB");
		var k=0;
		//先搜索到这个nextObj
		for ( var i in totalArray)
		{
			if (nextObj != totalArray[i])
				k++;
			else
				break;
		}
		for (k;k<totalArray.length;k++)
		{
			$("#"+totalArray[k]).empty();
			 $("#"+totalArray[k]).append('<option value="-" selected="selected">请选择</option>');
			 $("#"+totalArray[k]).parent().find('a').find('span').text('请选择');
		}	
	}
	
	function showSrcParam(obj,srcOrTarget,type,nextObj)
	{
		
		var obj1 = $(obj).val(); //获取下拉框选定的值
		if(obj1 == '-')  //如果选择请选择，相应的后面的都要变成请选择
		{
			 product_left();
			 deleteHTMLElement(srcOrTarget,nextObj);
			 return;
		}
		if(obj1  ==  'db2' ||  obj1 != 'db2')
		{
			product_left();
		}
		var ip = $("#srcIP").find("option:selected").text();
		var product=$("#srcProduct").find("option:selected").text();
		//ip 和product 决定版本
		var info={ip:ip,product:product,type:type};
		console.log(info);
	//	this,'target','db','targetDB'
		if(product == 'db2' && type== 'database')
		{
				callAjax(info,nextObj);    
		}
		else if(type == 'database' && product != 'db2')
		{
			//如果通过instance 发现不是DB2 并且类型是db 什么都不做
			return;
		}else if(product=='请选择')
		{
			 
		}
		else
			callAjax(info,nextObj);   
	}
	
	
	
	function showTargetParam(obj,srcOrTarget,type,nextObj)
	{
		
		var obj1 = $(obj).val(); //获取下拉框选定的值
		if(obj1 == '-')  //如果选择请选择，相应的后面的都要变成请选择
		{
			 product_right();
			 deleteHTMLElement(srcOrTarget,nextObj);
			 return;
		}
		if(obj1  ==  'db2' ||  obj1 != 'db2')
		{
			product_right();
		}
		var ip = $("#targetIP").find("option:selected").text();
		var product=$("#targetProduct").find("option:selected").text();
		//ip 和product 决定版本
		var info={ip:ip,product:product,type:type};
		console.log(info);
	//	this,'target','db','targetDB'
		if(product == 'db2' && type== 'database')
		{
			callAjax(info,nextObj);
		}
		else if(type == 'database' && product != 'db2')
		{
			//如果通过instance 发现不是DB2 并且类型是db 什么都不做
		}
		else
			callAjax(info,nextObj);   
	}


</script>


</body>
</html>