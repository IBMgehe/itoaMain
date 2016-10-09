<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="header.jsp" flush="true" />
<title>自动化部署平台</title>
<style type="text/css">
.sweet-alert button.cancel { 
	background-color: #ec6c62; 
}
.sweet-alert button.cancel:hover { 
	background-color: #E56158; 
}
.tooltip {
	width:110px;
	word-wrap:break-word;
}
.content {
	position:relative;
	float:right;
	width:calc(100% - 57px);
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
} 
.mr20{
	font-size:14px;
}
label > div.radio {
    position: relative;
    top:4px;
}
.current1,.current1:hover {
    color: #444444;
}
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
			<a href="" class="current">
				<i class="icon-home"></i>实例一览
			</a>
			<a href="#" class="current1">IBM 健康检查</a>
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
							<div class="widget-content">健康检查概要信息.</div>
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
									<font color="white">创建巡检任务</font>
								</button>
								<span style="margin-right: 4px;"></span>
								<button onclick="deleteJobs();" class="btn btn-sm" data-toggle="modal" style="background-color: #448FC8;">
									<font color="white">删除任务</font>
								</button>								
							</div>

							<div style="margin-bottom: 10px;"></div>
							<table id="sel_tab" class="table table-bordered data-table  with-check table-hover no-search no-select">
								<thead>
									<tr>
										<th style="text-align: center;">序号</th>
										<th style="text-align: center;">巡检类型</th>
										<th style="text-align: center;">巡检对象</th>
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
											<td style="text-align: center;"><input type="checkbox" name="servers"
													value="${job.job_uuid }" onclick="isSelect(this);" /></td>
											<td style="text-align: center;">${job.job_type }</td>
											<!-- <td style="text-align: center;">${job.job_target}</td>  -->	
											<td style="text-align: center;">
												<c:if test="${job.job_groupOrIP eq 'ip' }">
											 	<span href="#" class="tooltipa1" data-toggle="tooltip" data-placement="right"
											 	 title="">${job.job_target }</span>
											 	 </c:if>
											 	 <c:if test="${job.job_groupOrIP ne 'ip' }">
											 	<span>${job.job_groupOrIP }</span>
											 	 </c:if>
			   								</td>										
											<td style="text-align: center;">${job.job_scheduled_at}</td>
											<td style="text-align: center;">${job.job_lastrun_at}</td>
											<c:if test="${job.job_if_daily == 0 }">
												<td style="text-align: center;">立即执行</td>
											</c:if>
											<c:if test="${job.job_if_daily == 1 }">
												<td style="text-align: center;">定时执行</td>
											</c:if>
											<c:if test="${job.job_if_daily == 2 }">
												<td style="text-align: center;">每日执行</td>
											</c:if>
											<td style="text-align: center;">${job.job_submited_by}</td>
										    <td style="text-align: center;"><a href="${job.job_detail }" style="color:#08c;">详情</a></td> 
											</tr>
										
									</c:forEach>
								</tbody>
							</table>



							<!-- 创建巡检任务 -->
							<div class="modal fade" id="detail" tabindex="-1" role="dialog"
								aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<!-- 头部 -->
										<div class="modal-header">											
											<h5 class="modal-title" id="myModalLabel">
												<img src="<%=path%>/img/plus15.png">&nbsp;&nbsp;巡检任务
											</h5>
										</div>

										<!-- 主体内容 -->
										<form action="createJob.do" method="post" id="submits">
											<div class="modal-body">
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">巡检类型：</span>
														<select id="job_type" class="w85" style="width: 220px;" name="job_type"
															onchange="change_type(this)">
															<option value="WAS">WAS 巡检</option>
															<option value="MQ">MQ 巡检</option>
															<option value="DB2">DB2 巡检</option>
															<option value="OS">OS 巡检</option>
														</select>
													</div>
												</div>
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">巡检对象：</span>
														<div class="inblock mr20"><label><input type="radio" checked value="group" name="select" onClick="sortRadio1()"><font size="2.5px;">巡检对象组</font></label></div>
														&nbsp;&nbsp;
														<div class="inblock mr20"><label><input type="radio" value="ip" name="select" onClick="sortRadio2()"><font size="2.5px;">巡检对象IP</font></label></div>
														<!-- <select id="job_target" class="w85" multiple style="width: 220px;" name="job_target">
															<option value="All" selected="selected">All</option>
														</select> -->
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
														<select id="job_target" class="w85" multiple style="width: 220px;" name="job_target">															
														</select>
													</div>
												</div>
												<div class="control-group">
													<div class="controls">
														<span class="input140 mr20">巡检时间：</span>
														<input type="checkbox" id="job_date" name="job_date" onclick="isSelectDate(this);" />
														<font size="2.5px;">设置时间</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<div style="float: right; margin-right: 150px; display: none;" id="every">
															<input type="checkbox" id="repeat" name="repeat" onclick="selectTime(this)" />
															<font size="2.5px;">每日重复</font>
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
														<input class="form-control" type="text" id="next" name="next" value=""
															readonly="readonly" style="display: none;height:28px;width: 220px;" />
													</div>
												</div>
											</div>
										</form>
									</div>

									<div class="modal-footer">
										<button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeModal();">关闭</button>
										<button type="button" class="btn" style="background-color: rgb(68, 143, 200);"
											onclick="CheckInput();">
											<font color="white">提交</font>
										</button>
									</div>
								</div>
							</div>
							<!-- 详细信息 -->

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
	//var ips=[];
	//操作
	function isSelect(s) {
		if ($(s).attr("checked")) {
			infoId.push(s.value);
		//	ips.push($(s).parents('td').next().next().text());
		} else {
			var index = 0;
			for (var i = 0; i < infoId.length; i++) {
				if (s.value == infoId[i]) {
					index = i;
				}
			}
			infoId.splice(index, 1);
			//ips.splice(index,1);
		}
		console.log(infoId);
		//console.log(ips);
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
    	$("#job_target").empty();
    	//$("#job_target").append("<option value='All' selected='selected'>All</option>");
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
				$("#job_target").append(str);	
			},
			failure:function(errmsg)
			{
				alert(errmsg)
			}
    	});
    } 
    
	function isSelectDate(s)
	{
		if ($(s).attr("checked"))
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
		}
	}
	
	function selectTime(s)
	{
		if($(s).attr("checked"))
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
				//dateFormat: "'yy-mm-dd HH:mm",
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
		}
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
		window.location.href = "healthCheck.do";
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
					data : { job_uuid :transData(infoId),operType:'deleteJob' },
					type : 'post',
					dataType : 'json'
				}).done(function(result) {
					if(result.status==1)
					{
						swal({
							title: "",
							text: "删除成功！", 
							type: "success",
							confirmButtonText: "确定",  
							confirmButtonColor: "rgb(174,222,244)"
						},
						function(isConfirm)
						{
							  if (isConfirm) 
							  {
								  window.location.href = "healthCheck.do";
							  } 
						});				
					}
				}).error(function(result) {
					swal({
						title: "",
						text: "删除失败！", 
						type: "error",
						confirmButtonText: "确定",  
						confirmButtonColor: "rgb(174,222,244)"
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
		if ( (grouporip=='ip' && job_target == null) )
		{
			sweet("巡检对象不能为空!","warning","确定");
			return;
		}
		//如果选择了组的话
		
		
 		$.ajax({
			url : '<%=path%>/createJob.do',
				data : $('#submits').serialize(),
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
								window.location.href = "healthCheck.do";
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
		var jobtype = $("#job_type").val();
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
		var jobtype = $("#job_type").val();
		$("#job_target").empty();
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
				$("#job_target").append(str);	
			},
			failure:function(errmsg)
			{
				alert(errmsg)
			}
    	});
	})
</script>
</body>
</html>