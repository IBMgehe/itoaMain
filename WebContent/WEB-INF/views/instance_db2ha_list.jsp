<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="header.jsp" flush="true" />
<title>自动化部署平台</title>
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

<script type="text/javascript">
	var infoId = [];
	var ips=[];
	//操作
	function isSelect(s) {
		if ($(s).attr("checked")) {
			//这里要加上判断，剔除windows
			/* if ($(s).parent().parent().parent().parent().parent().next().next()
					.next().next().next().find('b').text() == 'Error') 
			{
				$(s).prop("checked", false);		
				swal({
					title: "",
					text: "当前目标系统存在问题，无法选择",
					type: "warning",
					confirmButtonText: "确定",  
					confirmButtonColor: "rgb(174,222,244)"
				});				
				return;
			} */
			infoId.push(s.value);
			ips.push($(s).parents('td').next().next().text());
		} else {
			var index = 0;
			for (var i = 0; i < infoId.length; i++) {
				if (s.value == infoId[i]) {
					index = i;
				}
			}
			infoId.splice(index, 1);
			ips.splice(index,1);
		}
		console.log(infoId);
		console.log(ips);
	}

	function judge(arr)
	{
	    for(var i = 1 ; i < arr.length;i++)
		{
		     if (arr[i].toLowerCase() == arr[i-1].toLowerCase())
			    continue;
			 else return false;
		}			
		return true;
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
	
	function checkServer()
	{
		if(infoId.length != 1 )
		{
			sweet("请选择一台主机!","warning","确定");
		}
		else
		{
			swal({
				title: "", 
				text: "检测中...",
				type: "warning",
				timer: 1000,
				showConfirmButton: false
			}, function() {
				$.ajax({
					url : "<%=path%>/checkServerStatus.do",
					data : { selectedServers : transData(ips)},
					type : 'post',
					dataType : 'json',
					timeout : 10000, //超时时间设置，单位毫秒
				}).done(function(retdata) {
					if(retdata['status']==1)
					{
						var  out=[];
					  	for( key in retdata['msg'])
					  	{
					  		if(retdata['msg'][key] != 1)
					  		{
					  			out.push(key);
					  		}
					  	}
					  	var finalStringOut;
					  	var type;
					  	if (out.length == 0){
					  		finalStringOut="检查完成";
					  		type="success";
					  	}else{
					  		finalStringOut="检查完成,该主机网络不可达";
					  		type="error";
					  	}
					  	swal({title: "",text:finalStringOut ,  type: type,confirmButtonText: "返回",confirmButtonColor: "rgb(174,222,244)"},
								function(isConfirm)
								{
									  if (isConfirm) 
									  {
										  window.location.href = "getAllServers.do";
									  } 
								})
					}
					
				});
			})	
		}
	}
	function checkDB2HASelect() {
		var infoId = [];
		var osId=[];
		$("input[name='servers']").each(function() {
			if ($(this).attr("checked")) 
			{
				infoId.push($(this).val());
				osId.push($(this).parent().parent().parent().parent().parent().next().next().next().next().text());
			}
		});
		 if (infoId.length != 2 ) 
		 {
			swal({
				title: "",
				text: "请选择两条实例", 
				type: "warning",
				confirmButtonText: "确定",  
				confirmButtonColor: "rgb(174,222,244)"
			});
			return;
		 }
		 else{
			 location.href = "getInstanceDetial.do?serId=" + infoId + "&ptype=db2ha&platform=aix"; 
		 } 
	}
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
			<a href="getAllServers.do" class="current">
				<i class="icon-home"></i>实例一览
			</a>
			<a href="#" title="IBM DB2HA" class="tip-bottom">IBM DB2HA</a>
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
							<div class="widget-content">所有DB2HA实例信息.</div>
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
								<input id="filter" type="text" class="form-control" placeholder="请输入过滤项">
								<span style="margin-right: 5px;">&nbsp;&nbsp;&nbsp;</span>
									<button onclick="checkServer();" id="check_button" class="btn btn-sm"
										style="background-color: rgb(68, 143, 200);">
										<font color="white">检查主机状态</font>
									</button>
								<div id="showcomputer" name="showcomputer" style="float:right;"><span style="position:relative;top:10px;">共有<font size="3">${total }</font>台主机</span></div>

							</div>
							<div style="margin-bottom: 5px"></div>
							<table class="table table-bordered data-table  with-check table-hover no-search no-select">
								<thead>
									<tr>
										<th style="text-align: center;">选择</th>
										<th style="text-align: center;">主机名</th>
										<th style="text-align: center;">IP地址</th>
										<th style="text-align: center;">主机配置</th>
										<th style="text-align: center;">操作系统</th>										
																				<th style="text-align: center;">所属产品组</th>
										<th style="text-align: center;">健康状态</th>
									</tr>
								</thead>
								<tbody class="searchable">
									<c:forEach items="${servers }" var="ser">
									    <c:if test="${ser.os eq 'aix' }">
										<tr>										
											<td style="text-align: center;"><input type="checkbox" name="servers"
													value="${ser.uuid }" onclick="isSelect(this);" /></td>
											<td style="text-align: center;">${ser.name }</td>
											<td style="text-align: center;">${ser.ip }</td>
											<td style="text-align: center;">${ser.hconf}</td>
											<td style="text-align: center;">${ser.os}</td>
																				<td style="text-align: center;">${fn:replace(ser.product,' ','&nbsp;&nbsp;')} </td>
											<td style="text-align: center;" name="state">
												<c:if test="${ser.status eq 'Active' }">
													&nbsp;&nbsp;<img src="img/icon_success.png"></img>
													<b>${ser.status}</b>
												</c:if> 
												<c:if test="${ser.status eq 'Error' }">
													<img src="img/icon_error.png"></img>
													<b>${ser.status}</b>
												</c:if>
											</td>										
										</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<div style="margin-bottom: 55px"></div>

					<div class="columnfoot">
						<a class="btn btn-info fr btn-next" onclick="checkDB2HASelect();">
							<span>下一页</span> <i class="icon-btn-next"></i>
						</a>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>
</html>