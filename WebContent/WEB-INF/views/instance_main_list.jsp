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
<title>自动化部署平台——主机列表</title>
<style type="text/css">
.mr20{
	font-size:14px;
}
input[type="text"],input[type="password"] {
	height:28px;
}
.sweet-alert button.cancel { 
	background-color: #ec6c62; 
}
.sweet-alert button.cancel:hover { 
	background-color: #E56158; 
}
.content {
	position:relative;
	float:right;
	width:86%;
	margin:0px;
	height:calc(100vh - 70px);
	overflow-y:scroll;
}
</style>
</head>

<script>
	/* 提取sweet提示框代码，以便后面方便使用，减少代码行数 */ 
	function sweet(te,ty,conBut)
	{
		swal({ title: "", text: te,  type: ty, confirmButtonText: conBut, });
	}
</script>

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
	
	//编辑框按钮提交
	function CheckEditFormInput()
	{
		if ($("#edit_ip").val().trim() == "") 
		{
			sweet("请输入IP!","warning","确定");
			return;
		}
		if ($("#edit_userid").val().trim() == "") 
		{
			sweet("请输入用户名!","warning","确定");
			return;
		}
		if ($("#edit_password").val().trim() == "") 
		{
			sweet("请输入密码!","warning","确定");
			return;
		}
		$.ajax({
			url : "<%=path%>/modifyServer.do",
		//	data : {edit_ip:$("#edit_ip").val().trim(),edit_uuid:$("#edit_uuid").val().trim(),edit_userid:$("#edit_userid").val().trim(),edit_password:$("#edit_password").val().trim(),edit_product:$("#edit_product").val()},
			type : 'post',
			data : $('#edit_submits').serialize(),
			dataType : 'json',
			success : function(result) 
			{
				var state
				if(result.msg=='success')
				{
				   state='成功';
				   swal({
						title: "",
						text: "修改"+state, 
						type: "success",
						confirmButtonText: "确定",  
						confirmButtonColor: "rgb(174,222,244)"
					},
					function(isConfirm)
					{
						if(isConfirm)
						{
						   window.location.href = "getAllServers.do";
						}
					});
					}
					else
					{
						state='失败';
						swal({
							  title: "",
							  text: "修改"+state, 
							  type: "error",
							  confirmButtonText: "确定",  
							  confirmButtonColor: "rgb(174,222,244)"
							});
						}
			},
			failure:function(err)
			{
				
			}
	})}
	
	//编辑主机
	function editServer()
	{
		if(infoId.length == 1)
		{
			$("input[name='servers']").each(function() {
				if ($(this).attr("checked")) {
				$('#edit_uuid').val($(this).val());
				$('#edit_userid').val('');
				$('#edit_password').val('');
			    //$('#edit_name').val($(this).parents('td').next().text());
				$('#edit_ip').val($(this).parents('td').next().next().text());
			    //$('#edit_hconf').val($(this).parents('td').next().next().next().text());
			    //$('#edit_os').val($(this).parents('td').next().next().next().next().text());	
		        //$('#edit_product').val($(this).parents('td').next().next().next().next().next().text());		
			    //$("#edit_product").html($(this).parents('td').next().next().next().next().next().text());
		        //$("#edit_product option").eq(1).attr("selected",true);
			    //$("#edit_product").html("<option value='1'>aaa</option>");
				}
			})	
			$('#edit_more').modal('show');
		}else{	
			sweet("只能选择一台主机，不能不选或者多选！","warning","确定");
		}
	}
		
	function transData(infoId){
    	var data="";
		for(var i = 0 ; i < infoId.length;i++)
		{
	   	    data+=infoId[i]+',';
		}
		var length=data.lastIndexOf(',');	
		return data.substr(0,length);
	}
	
	//当按下删除按钮的时候，去判断选了一台主机，其他不行。
 	function deleteServer()
	{
		if (infoId.length>=1)
		{
			swal({
				title: "", 
				text: "是否确认删除选择主机？",
				type: "warning",
				showCancelButton: true,
				closeOnConfirm: false,
				confirmButtonText: "是",
	            cancelButtonText: "否",
			}, function() {
				$.ajax({
					url : "<%=path%>/delServer.do",
					data : { data1 : transData(infoId),ip:transData(ips)},
					type : 'post',
					dataType : 'json'
				}).done(function(result) {
					if(result.msg==1)
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
								  window.location.href = "getAllServers.do";
							  } 
						});				
					}
				}).error(function(result) {
					sweet("删除失败!","error","确定");
				});
			})			
		}
		else
		{
			sweet("请选择至少一台主机!","warning","确定");
		}	
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
 	
 	window.onload = function() {
		$.ajax({
			url : '<%=path%>/getProduct.do',
			type : 'get',
			dataType : 'json',
			success : function(result) 
			{
				var str;
				for (var i = 0; i < result.length; i++) 
				{
					str += "<option value='" + result[i] + "'>" + result[i]+ "</option>";
				}
				$("#product").append(str);
				$("#edit_product").append(str);
			},failure:function(err){
				alert(err);
			}
		})
	}
</script>

<body>
	<!--header start-->
	<div class="header">
	 <jsp:include page="topleft_open.jsp" flush="true" />
	</div>
	<!--header end-->

	<!--content start-->
	<div class="content">
		<div class="breadcrumb">
			<a href="getAllServers.do" class="current">
				<i class="icon-home"></i>实例一览
			</a>
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
							<div class="widget-content">所有实例信息.</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="columnauto">
						<div class="widget-box nostyle ">
							<div class="col-sm-6 form-inline">
								
								<div class="dt-buttons btn-group">
									<input id="filter" type="text" class="form-control" placeholder="请输入过滤项" style="height:28px;">
									<span style="margin-right: 5px;">&nbsp;&nbsp;&nbsp;</span>
									<button class="btn btn-sm" data-toggle="modal" style="background-color: #448FC8;"
										data-target="#add_single">
										<font color="white">添加主机</font>
									</button>
									<!--  
									<span style="margin-right: 5px;">&nbsp;&nbsp;&nbsp;</span>
									<button class="btn btn-sm" data-toggle="modal" style="background-color: #448FC8;"
										data-target="#add_more">
										<font color="white">批量导入</font>
									</button>
									-->
									<span style="margin-right: 5px;">&nbsp;&nbsp;&nbsp;</span>
									<button onclick="editServer()" class="btn btn-sm" data-toggle="modal"
										style="background-color: #448FC8;">
										<font color="white">编辑主机</font>
									</button>
									<span style="margin-right: 5px;">&nbsp;&nbsp;&nbsp;</span>
									<button onclick="deleteServer();" id="delete_button" class="btn btn-sm"
										style="background-color: #448FC8;">
										<font color="white">删除主机</font>
									</button>					
									<span style="margin-right: 5px;">&nbsp;&nbsp;&nbsp;</span>
									<button onclick="checkServer();" id="check_button" class="btn btn-sm"
										style="background-color: #448FC8;">
										<font color="white">检查主机状态</font>
									</button>
								</div>
								<span style="float:right;position:relative;top:10px;">共有<font size="3">${total }</font>台主机</span>
							</div>
							<div style="margin-bottom: 5px"></div>
							
							<!-- data-table -->
							<table id="mytable" name="data-table"
								class="table table-bordered data-table  with-check table-hover no-search no-select">
								<thead>
									<tr>
										<th style="text-align: center;">序号</th>
										<th style="text-align: center;">主机名</th>
										<th style="text-align: center;">IP地址</th>
										<th style="text-align: center;">主机配置</th>
										<th style="text-align: center;">操作系统</th>
										<th style="text-align: center;">所属产品组</th>
										<th style="text-align: center;">健康状态</th>
									</tr>
								</thead>
								<%-- <%
									int i = 1;
								%> --%>
								<tbody class="searchable">
									<c:forEach items="${servers }" var="ser">
										<tr>				
											<td style="text-align: center;"><input type="checkbox" name="servers"
													value="${ser.uuid }" onclick="isSelect(this);" /></td>
											<td style="text-align: center;">${ser.name }</td>
											<td style="text-align: center;">${ser.ip }</td>
											<td style="text-align: center;">${ser.hconf}</td>
											<td style="text-align: center;">${ser.os}</td> <!-- ${ser.product} -->
											<td style="text-align: center;">${fn:replace(ser.product,' ','&nbsp;&nbsp;')} </td>
											<td style="text-align: center;" name="state">
												<c:if test="${ser.status eq 'Active' }">
													&nbsp;&nbsp;<img src="img/icon_success.png"></img>&nbsp;
													<b>${ser.status}</b>
												</c:if> 
												<c:if test="${ser.status eq 'Error' }">
													<img src="img/icon_error.png"></img>
													&nbsp;<b>${ser.status}</b>
												</c:if>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>

					<div class="columnfoot"></div>



					<div class="modal fade" id="add_single" tabindex="-1" role="dialog"
						aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
									<h5 class="modal-title" id="myModalLabel">
										<img src="<%=path%>/img/plus15.png">&nbsp;&nbsp;添加信息
									</h5>
								</div>

								<form action="addHost.do" method="post" id="submits">
									<div class="modal-body">
										<div class="control-group">
											<div class="controls">
												<!-- <span class="input140 mr20">名称：</span> -->
											</div>
										</div>

										<div class="control-group">
											<div class="controls">
												<span class="input140 mr20">IP：</span>				
												<input class="form-control" type="text" id="ip" name="ip">
												
												<div style="margin-top: 3px; float: right; margin-right: 20px;">
													<span hidden="" id="ipshow" name="ipshow"> 
														<img alt="" src="img/icon_error.png">&nbsp;
														<span style="font-size: 13px">此IP地址已注册</span>
													</span>
												</div>
												<div style="float: right; margin-top: 4px;">
													<span hidden="" id="ipshow1" name="ipshow1"> 
														<img align="top" alt="" src="img/icon_success.png">&nbsp; 
														<span style="font-size: 13px">此IP地址未注册</span>
													</span>
												</div>
												<div style="float: right; margin-top: 4px;">
													<span hidden="" id="ipshow2" name="ipshow2"> 
														<img align="top" alt="" src="img/icon_error.png">&nbsp; 
														<span style="font-size: 13px">格式或数值有误</span>
													</span>
												</div>
												<input type="hidden" id="msgflag" name="msgflag" value="">
											</div>
										</div>

										<div class="control-group">
											<div class="controls">
												<span class="input140 mr20">用户名：</span>
												<input class="form-control" type="text" id="userid" name="userid">
												&nbsp;&nbsp;&nbsp;
												<span style="font-size: 13px; color: red;">*&nbsp;&nbsp;需管理员用户</span>
											</div>
										</div>

										<div class="control-group">
											<div class="controls">
												<span class="input140 mr20">密码：</span>
												<input class="form-control" type="password" id="password" name="password">
											</div>
										</div>
							
										<div class="control-group">
											<div class="controls">
												<span class="input140 mr20">产品分类：</span> 
												<select id="product" class="add_select" multiple="multiple" style="width: 206px;" name="product">
												<!-- 	<option selected="selected" value="-1">请选择</option> -->
													<!-- <option value="was">was</option>
													<option value="db2">db2</option>
													<option value="mq">mq</option>
													<option value="itm-os">itm-os</option> -->
												</select>
											</div>
										</div> 

									</div>
									<!--  
									<div class="control-group">
										<div class="controls">
											<span class="input140 mr20" style="padding-left: 16px;">Hypervisor：</span>
											<select id="HVisor" class="w85" style="width: 220px;"
												name="HVisor">
												<option>PowerVM</option>
												<option>Vmware</option>
												<option>KVM</option>
												<option>None</option>
											</select>
										</div>
									</div>-->
							<!-- 		<input type="hidden" name="os" id="os" value="DefaultOS"> -->
							
									<input type="hidden" name="os" id="os" value="DefaultOS">
									<input type="hidden" name="hvisor" id="hvisor" value="DefaultHvisor">
									<input type="hidden" name="hconf" id="hconf" value="DefaultHconf">
									<input type="hidden" name="name" id="name" value="DefaultName">
									<input type="hidden" name="status" id="status" value="Error">
									<input type="hidden" name="type" id="type" value="createServer">
								</form>
							</div>

							<div class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeModal();">关闭</button>
								<button type="button" class="btn" style="background-color: rgb(68, 143, 200);"
									onclick="CheckInput();">
									<font color="white">保存</font>
								</button>

							</div>
						</div>
					</div>
				</div>

				<div class="modal fade" id="add_more" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
					aria-hidden="true" style="width: 400px; height: 183px;">
					<div class="modal-dialog">
						<div class="modal-content">
							<form action="importExcel.do" method="post" id="fileUpload" name="fileUpload"
								enctype="multipart/form-data">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
									<h5 class="modal-title" id="myModalLabel">
										<img src="<%=path%>/img/plus15.png">&nbsp;&nbsp;批量导入
									</h5>
								</div>
								<div class="modal-body" align="center">
									<div class="modal-body">
										<div>
											<input type="file" name="file" id="file">
											<span id="wait_tip" style="display: none;"><img src="img/spinner.gif"
												id="loading_img" />请等待...</span> <span id="wait_success" style="display: none;"><img
												src="img/check_a.png" id="loading_img1" /> 导入成功</span> <span id="wait_failure"
												style="display: none;"><img src="img/noadd.png" id="loading_img2" /> 导入失败</span>
										</div>

										<div style="margin-top: 8px;">
											<a href="xls/machine.rar" target="_self">示例文件</a>
										</div>

									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default" data-dismiss="modal" onclick="hidespinner();">关闭</button>
									<input type="submit" class="btn" value="保存"
										style="background-color: rgb(68, 143, 200); color: white;">
								</div>

							</form>
						</div>
					</div>
				</div>

			</div>
			<div class="modal fade" id="edit_more" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
				aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h5 class="modal-title" id="myModalLabel">
								<img src="<%=path%>/img/edit16.png">&nbsp;&nbsp;编辑信息
							</h5>
						</div>

						<form action="postEditForm.do" method="post" id="edit_submits">
							<input type="hidden" id="edit_uuid" name="edit_uuid"></input>
							<div class="modal-body">
								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">用户名：</span>
										<input class="form-control" type="text" id="edit_userid" name="edit_userid">
										<span style="font-size: 13px; color: red;">*&nbsp;需管理员用户</span>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">密码：</span>
										<input class="form-control" type="password" id="edit_password" name="edit_password">
									</div>
								</div>

                                <!--  隐藏
								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">主机名：</span>
										<input class="form-control" type="text" readonly="readonly" id="edit_name"
											name="edit_name" value="">
									</div>
								</div>
								-->

								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">IP：</span>
										<input class="form-control" type="text"  id="edit_ip" name="edit_ip" 
											value="">
										<div style="margin-top: 3px; float: right; margin-right: 20px;">
											<span hidden="" id="ipshow"> <img alt="" src="img/icon_error.png">&nbsp; <span
												style="font-size: 13px">此IP地址已注册</span>
											</span>
										</div>
										<div style="float: right; margin-top: 4px;">
											<span hidden="" id="ipshow1" name="ipshow1"> <img align="top" alt=""
												src="img/icon_success.png">&nbsp; <span style="font-size: 13px">此IP地址未注册</span>
											</span>
										</div>
										<input type="hidden" id="msgflag" name="msgflag" value="">
									</div>
								</div>
								
								<!--  
								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">主机配置：</span>
										<input class="form-control" type="text" readonly="readonly" value="" id="edit_hconf"
											name="edit_hconf">
									</div>
								</div>
								-->
								
								<!--  
								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">操作系统：</span> 
										<select id="edit_os" class="w85" style="width: 220px;" name="edit_os">
											<option>aix</option>
											<option>linux</option>
											<option>windows</option>
											
										</select>
									</div>
								</div> 
								-->
										
								<div class="control-group">
									<div class="controls">
										<span class="input140 mr20">所属产品组：</span> 
										<select id="edit_product" multiple="multiple" style="width: 206px;" name="edit_product">
												
										</select>
									</div>
								</div> 
							</div>

						</form>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeModal();">关闭</button>
						<button type="button" class="btn" style="background-color: rgb(68, 143, 200);"
							onclick="CheckEditFormInput();">
							<font color="white">保存</font>
						</button>

					</div>
				</div>
			</div>

		</div>
	</div>
</body>

<script type="text/javascript">
	function checkFile() 
	{
		var excelFile = $("#file").val();
		if (excelFile == '') 
		{
			getId("wait_tip").style.display = "none";
			sweet("请选择需上传的文件!","warning","确定");
			return false;
		}
		if (excelFile.indexOf('.xls') == -1) 
		{
			getId("wait_tip").style.display = "none";
			sweet("文件格式不正确，请选择正确的Excel文件(后缀名.xls)!","warning","确定");
			return false;
		}
	}
	
	$("#ip").blur(function() { //发送请求看是否存在IP
		var re = /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
		if (!re.test($("#ip").val().trim())) 
		{
			$("#ipshow").hide();
			$("#ipshow1").hide();
			$("#ipshow2").show();							
			return;
		}

		var txt = $("#ip").val();
		$.ajax({
			url : "<%=path%>/IPCheck.do",
			data : { IP : txt },
			type : 'post',
			dataType : 'json',
			success : function(result) 
			{
				if (result.msg == 1) {
					$("#msgflag").val(1);
					$("#ipshow1").hide();
					$("#ipshow2").hide();
					$("#ipshow").show()
				}
				if (result.msg == 0) {
					$("#msgflag").val(0);
					$("#ipshow").hide();
					$("#ipshow2").hide();
					$("#ipshow1").show();
				    //OK =  true;//表明IP是未添加过的。数值是合法的。不为空
				}
			}
		});
	});
	
	function CheckInput() 
	{
		if ($("#ip").val().trim() == "") 
		{
			sweet("请输入IP!","warning","确定");
			return;
		}
		var re = /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
		if (!re.test($("#ip").val().trim())) 
		{
			$("#ipshow").hide();
			$("#ipshow1").hide();
			$("#ipshow2").show();	
			sweet("请修正IP值!","warning","确定");
			return;
		}
		if ($("#userid").val().trim() == "") 
		{
			sweet("请输入用户名!","warning","确定");
			return;
		}
		if ($("#password").val().trim() == "") 
		{
			sweet("请输入密码!","warning","确定");
			return;
		}
		if ($("#msgflag").val() == 1) 
		{
			sweet("IP已经存在，请更换IP!","warning","确定");
			return;
		}
		var value = $("#product option:selected").attr("value");
		if ($("#product").val() == -1 || value ==  undefined) //value 判断有没有选择
		{
			sweet("请选择该IP拥有的产品!","warning","确定");
			return;
		}
		$.ajax({
			url : '<%=path%>/addHost.do',
			data : $('#submits').serialize(),
			type : 'post',
			dataType : 'json',
			success : function(result) 
			{
				if (result.msg == 'failure') 
				{
					swal({
			            title: "",
			            text: "是->重新添加，否->返回主机列表",
			            type: "warning",
			            showCancelButton: true,
			            confirmButtonText: "是",
			            cancelButtonText: "否", 
			            //confirmButtonColor: "#ec6c62"
			        }, 
			        function(isConfirm)
			        {
			        	  if (isConfirm) 
			        	  {
			        		    $("#name").val('DefaultName');
								$("#ip").val("");
								$("#ipshow").hide();
								$("#ipshow1").hide();
								$("#userid").val("");
								$("#password").val("");
								$('#product').val(-1);  
			        	  } 
			        	  else 
			        	  {
			        		  window.location.href = "getAllServers.do";
			        	  }
			        });	
				}
				if (result.msg == 'success') 
				{
					/* ymPrompt.win({
						message : '创建成功，是否继续添加？',
						handler : function(tp) {
							if (tp == "no")//返回主页
							{
								window.location.href = "getAllServers.do";
							} else {
								$("#name").val("DefaultName");
								$("#ip").val("");
								$("#ipshow").hide();
								$("#ipshow1").hide();
								$("#userid").val("");
								$("#password").val("");
							}
						},
						btn : [ [ '是', 'yes' ], [ '否', 'no' ] ]
					}); */
					swal({
			            title: "",
			            text: "创建成功，是否继续添加？",
			            type: "warning",
			            showCancelButton: true,
			            confirmButtonText: "是",
			            cancelButtonText: "否",  
			            //confirmButtonColor: "#ec6c62"
			        }, 
			        function(isConfirm)
			        {
			        	  if (isConfirm) 
			        	  {
			        		    $("#name").val("DefaultName");
								$("#ip").val("");
								$("#ipshow").hide();
								$("#ipshow1").hide();
								$("#ipshow2").hide();
								$("#userid").val("");
								$("#password").val("");
							    //$("#product").empty();							
							    $("#product option[text='请选择']").attr("selected", true); 
							    //var pro_str="<option selected=\"selected\" ></option>"
								//$("#product option:selected").val(-1);
								//$("#product option:selected").text("请选择");
			        	  } 
			        	  else 
			        	  {
			        		  window.location.href = "getAllServers.do";
			        	  }
			        });
				}
			}
		});
	}

	function getId(id) 
	{
		return document.getElementById(id);
	}
	
	function validation() 
	{
		getId("submits").style.display = "none";
		getId("wait_tip").style.display = "";
		return true;
	}

	function closeModal() 
	{
		window.location.href = "getAllServers.do";
	}
	
	function hidespinner() 
	{
		getId("wait_tip").style.display = "none";
		window.location.href = "getAllServers.do";
	}
</script>

<script type="text/javascript">
	(function() {
		$('#fileUpload').ajaxForm({
			dataType : 'json',
			beforeSubmit : function() {
				var excelFile = $("#file").val();
				if (excelFile == '') 
				{
					getId("wait_tip").style.display = "none";
					sweet("请选择需上传的文件!","warning","确定");
					return false;
				}
				if (excelFile.indexOf('.xls') == -1) 
				{
					getId("wait_tip").style.display = "none";
					sweet("文件格式不正确，请选择正确的Excel文件(后缀名.xls)!","warning","确定");
					return false;
				}
			},
			beforeSend : function() {
				getId("wait_success").style.display = "none";
				getId("wait_failure").style.display = "none";
			},
			uploadProgress : function(event, position, total, percentComplete) {
			
			},

			success : function(result) 
			{
				if (result.msg == 'success') 
				{
					getId("wait_success").style.display = "";
					getId("submits").style.display = "";
				}
				if (result.msg == 'failure') 
				{
					getId("wait_failure").style.display = "";
					getId("wait_tip").style.display = "none";
					return true;
				}
			},
			complete : function(xhr) {
			}
		});
	})();
</script>

<!-- websocket 动态更新主机信息 -->
<script type="text/javascript">
    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://192.168.80.5/itoa/updateServerStatus"); 
    }
    else {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
    //    setMessageInnerHTML("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
     //   setMessageInnerHTML("WebSocket连接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
     //   setMessageInnerHTML("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

  //将消息显示在网页上
    function setMessageInnerHTML(retData) {
    	var obj = eval('(' + retData + ')'); //转为对象
    	if(obj['type'] == 1) //密码不对。网络正确
    	{
    		var ip=obj['msg'];
    		alert(ip+' 账号或密码不正确！');
    	}else if(obj['type']==2){//网络不通
    		alert('网络不通！');
    	}else if(obj['type']==0){   //账号密码、网络正确、获取到数据
    	var dataList = obj['msg'].split(':');
    	$("#mytable tbody").find('tr').each(function(){
    		 var tdArr = $(this).children();
    		 var ip = tdArr.eq(2).text().trim();//取出ip
    		 if(ip == dataList[1])
    		 {
    			tdArr.eq(1).html(dataList[0]); 
    			tdArr.eq(3).html(dataList[2]);
    			tdArr.eq(4).html(dataList[3]);
    			tdArr.eq(6).html("&nbsp;&nbsp;<img src='img/icon_success.png'></img>&nbsp;<b>"+dataList[5]+"</b>");
    		 }
    	})
    	}
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }
</script>
</html>
