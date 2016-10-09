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
input[type="text"],input[type="password"]  {
	height:28px;
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
.current1,.current1:hover {
    color: #444444;
}
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
.comm95{ 
	width: 93px; 
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
			<a href="#" class="current1">IBM 账号管理</a>
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
							<div class="widget-content">账号管理概要信息.</div>
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
								<button class="btn btn-sm" data-toggle="modal" data-target="#create_user" style="background-color: #448FC8;">
									<font color="white">创建用户</font>
								</button>	
								<span style="margin-right: 4px;"></span>
								<button class="btn btn-sm" onclick="deleteUser();" id="delete_button" style="background-color: #448FC8;">
									<font color="white">删除用户</font>
								</button>
								<!-- <span style="margin-right: 4px;"></span>
								<button class="btn btn-sm" onclick="editUser()" data-toggle="modal" style="background-color: #448FC8;">
									<font color="white">编辑用户</font>
								</button> -->						
							</div>

							<div style="margin-bottom: 10px;"></div>
							<table id="sel_tab" class="table table-bordered data-table with-check table-hover no-search no-select">
								<thead>
									<tr>
										<th style="text-align: center;">序号</th>
										<th style="text-align: center;">用户名</th>
										<th style="text-align: center;">E-mail</th>
										<th style="text-align: center;">角色</th>
										<th style="text-align: center;">管理产品</th>
									</tr>
								</thead>

								<tbody class="searchable">
									 <c:forEach items="${userList }" var="job" varStatus="status">
										<tr>
											<td style="text-align: center;">
												<input type="checkbox" name="servers"
													 value="${job.username }" onclick="isSelect(this);" />
											</td>
											<td style="text-align: center;">${job.username }</td>											
											<td style="text-align: center;">${job.email }</td>
											<td style="text-align: center;"><c:if test="${job.role == 1}">管理员</c:if><c:if test="${job.role == 0}">操作员</c:if></td>
											 <td style="text-align: center;">${job.proList }</td>										    
											</tr>							
									</c:forEach> 
								</tbody>
							</table>

							
							
							<!-- 模态框：账号管理(创建用户) -->
							<div class="modal fade" id="create_user" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<!-- 头部 -->
										<div class="modal-header">	
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>									
											<h5 class="modal-title" id="myModalLabel">
												<img src="<%=path%>/img/plus15.png">&nbsp;&nbsp;账号管理
											</h5>
										</div>

										<!-- 主体内容 -->
										<form action="createAccount.do" method="post" id="submits_jobs">
											<div class="modal-body">												
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;margin-bottom:5px;">
														<span class="input140 mr20" style="margin-bottom:5px;"><font color="red">*</font> 用户名：</span>
														<input class="form-control" type="text" id="username" name="username">
													</div>
												</div>
												
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">E-mail：</span>
														<input class="form-control" type="text" id="email" name="email">
													</div>
												</div>	
												
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20"><font color="red">*</font> 密码：</span>
														<input class="form-control" type="password" id="passwd" name="passwd">
													</div>
												</div>	
												
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">确认密码：</span>
														<input class="form-control" type="password"  onblur="confirmPasswd()"
														       id="confirmasswd" name="confirmasswd">
													</div>
												</div>											
												
												<div class="control-group">
													<div class="controls" style="padding-top: 5px;">
														<span class="input140 mr20">角色：</span>
														<select id="role" class="w85" style="width: 210px;" name="role">
															<option value="1" selected="selected">Adminstor</option>
															<option value="0" >Operator</option>
														</select>
													</div>
												</div>
												
												<div class="control-group">
													<div class="controls" style="padding-top: 10px;">
														<span class="input140 mr20">管理产品：</span> 
														<select id="manageProduct" multiple="multiple" style="width: 206px;" name="manageProduct">
														</select>
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
							<!-- 模态框：账号管理(创建用户) -->		
							
							
										
									
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

<script>   /************************** 创建用户JS代码 ***************************/     
	//判断确认密码与密码是否一致
	function confirmPasswd()
	{
		var passwd = $("#passwd").val();
		var confirmasswd = $("#confirmasswd").val();
		if(confirmasswd != passwd)
		{
			sweet("确认密码与密码不一致，请重新输入 !","warning","确定"); 
			$("#confirmasswd").val("");
		}
	}
	
	//模态弹框，关闭按钮功能 
	function closeModal()  
	{
		window.location.href = "accountManage.do";
	}
	
	//提交检验用户名和密码
	function CheckInput()
	{
		//判断用户名不为空
		var username = $("#username").val();
		if(username == "")
		{
			sweet("用户名不能为空 !","warning","确定");
		} 
		
		//判断密码和确认密码都不为空 
		var passwd = $("#passwd").val();
		var confirmasswd = $("#confirmasswd").val();
		if(username != "" && passwd == "")
		{
			sweet("密码不能为空 !","warning","确定"); 
		}
		if(username != "" && passwd != "" && confirmasswd == "")
		{
			sweet("确认密码不能为空 !","warning","确定");  
		}
		
		//判断管理产品不为空
		var manageProduct = $("#manageProduct").val();
		if(username != "" && passwd != "" && confirmasswd != "" && manageProduct == null)
		{
			sweet("请选择管理产品 !","warning","确定");  
		}
		
		$.ajax({
			url : '<%=path%>/addUser.do',
			data : $('#submits_jobs').serialize(), 
			type : 'post',
			dataType : 'json',
			success : function(retMsg) {
				if(retMsg == 1){
					swal({
						title : "",
						text : "创建成功!",
						type : "success",
						confirmButtonText : "确定",
					},function(isConfirm){
						if (isConfirm) {
							window.location.href = "accountManage.do";
						}
					})
				}
			},
			failure : function(err) {
				alert(err)
			}
		})
	}
</script>	

<script>   /************************** 删除用户JS代码 ***************************/	 
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
	
	function transData(infoId){
    	var data="";
		for(var i = 0 ; i < infoId.length;i++)
		{
	   	    data+=infoId[i]+',';
		}
		var length=data.lastIndexOf(',');	
		return data.substr(0,length);
	}
	
	function deleteUser()
	{
		
		if (infoId.length>=1)
		{
			swal({
				title: "", 
				text: "是否确认删除用户名？",
				type: "warning",
				showCancelButton: true,
				closeOnConfirm: false,
				confirmButtonText: "是",
	            cancelButtonText: "否",
			},function(){
				
				$.ajax({
					url : '<%=path%>/delUser.do',
					data : { data1 : transData(infoId)},
					type : 'post',
					dataType : 'json'
				}).done(function(msg) {
					if(msg == 1){
					swal({
						title : "",
						text : "删除成功!",
						type : "success",
						confirmButtonText : "确定",
					},function(isConfirm){
						if (isConfirm) {
							window.location.href = "accountManage.do";
						}
					})
					}
				})
			})
			
		}
		else
		{
			sweet("请选择至少一个用户名!","warning","确定"); 
		}
	}
</script>



<script>
	/* 获取管理产品 */  
	$(document).ready(function(){
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
				$("#manageProduct").append(str);
				$("#edit_manageProduct").append(str);
			},failure:function(err){
				alert(err);
			}
		})
	})
</script>

</body>
</html>