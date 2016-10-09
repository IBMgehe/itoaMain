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
</head>
<style type="text/css">
.one {
	display: none;
}
body{ 
	overflow:hidden; 
}
label,.input140{
	font-size:13px;
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
label > div.radio {
    position: relative;
    top:4px;
}
.current1,.current1:hover {
    color: #444444;
}
</style>

<script>
	/* 根据机器类型，如果是AIX，安装路径就是/usr/mqm，其他情况就是/opt/mqm */ 
	$(document).ready(function(){
		var opos = $("#opos").text().toLowerCase();//aix 7.1
		var patt = new RegExp("aix");
		var result = patt.test(opos);
		if(result == true)
		{
			$("#mq_inst_path").val("/usr/mqm");
		}
		else
			$("#mq_inst_path").val("/opt/mqm");
	})
</script>

<body>
	<!--header start-->
	<div class="header">
		<jsp:include page="topleft_close.jsp" flush="true" />
	</div>
	<!--header end-->

	<!--content start-->
	<div class="content">
		<div class="breadcrumb">
			<a href="getAllServers.do" class="current1">
				<i class="icon-home"></i>IBM MQ
			</a>
			<a class="current">实例配置详细</a>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="columnauto">

						<div class="mainmodule">
							<!--**************** 拓扑结构 ****************-->
							<h5 class="swapcon">
								<i class="icon-chevron-down icon-chevron-right"></i>拓扑结构
							</h5>
							<div class="form-horizontal" style="display:none;">
								<c:forEach items="${servers }" var="ser" varStatus="num">
									<p class="twotit" style="padding-left: 15px;">
										<em class="majornode">单</em>节点
										<c:out value="${num.count }" />
									</p>
									<div class="column" style="margin-left:15px;">
										<div class="span12">
											<p>
												<b>主机名:</b><span class="column_txt"> ${ser.name } </span>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
												<b>IP地址:</b><span class="column_txt">${ser.ip}</span>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
												<b>操作系统:</b><span class="column_txt" id="opos"><em>${ser.os}</em></span>
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

						<form action="toMqNextPage.do?serId=${serId}&platform=${platform}&type=${ptype}&status=confirm"
							  method="post" id="submits" name="submits" class="form-horizontal">
							<input type="hidden" id="serId" name="serId" value="${serId}">
							<input type="hidden" id="ptype" name="ptype" value="${ptype}">
							<input type="hidden" id="hostId" name="hostId" value="${hostId}">
							 <input type="hidden" id="platform" name="platform" value="${platform}"> 
							<input type="hidden" id="mqversion" name="mqversion">
							<input type="hidden" id="mqtotalname" name="mqtotalname">
							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>基本属性
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">MQ安装版本</label>
										<div class="controls">
											<div class="inputb2l">
												<select style="width: 47.5%" id="mq_version" class="w48" name="mq_version"
													onchange="showfix(this);">
													<option value="-1" selected="selected">请选择...</option>										
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">MQ安装补丁</span>
												<select style="width: 47.5%" id="mq_fp" class="w48" name="mq_fp" onchange="getVer(this)">
													<option value="-">请选择...</option>
												</select>
												<input type="hidden" id="mqfix" name="mqfix" value="-"> 
											</div>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">MQ安装路径</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="mq_inst_path" name="mq_inst_path" value="/opt/mqm" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">MQ管理用户</span>
												<input type="text" class="w45" id="mq_user" name="mq_user" value="mqm" />
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="mainmodule">
								<h5 class="swapcon">
									<i class="icon-chevron-down"></i>队列管理器属性
								</h5>
								<div class="form-horizontal">
									<div class="control-group">
										<label class="control-label">队列管理器创建方式</label>
										<div class="controls">
											<div class="inputb2l">
												<div class="inblock mr20">
													<label>
														<input type="radio" name="qmgr_method" value="yes" onClick="sortRadio1()" checked />
														默认命令方式
													</label>
												</div>
												<div class="inblock mr20">
													<label>
														<input type="radio" name="qmgr_method" value="no" onClick="sortRadio2()" />
														不创建
													</label>
												</div>
											</div>
											
										</div>
									</div>
									
									<c:forEach items="${servers }" var="ser">
										<div class="control-group" id="hideserver">
										    <label class="control-label">主机名</label>
										    <div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="all_hostnames" value="${ser.name }" name="all_hostnames" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">IP地址</span>
													<input type="text" class="w45" id="all_ips" name="all_ips" value="${ser.ip }" readonly="readonly"/>
												</div>
											</div>	
										</div>
									</c:forEach>
								
									<div class="control-group" id="hide0">
										<label class="control-label">QMGR名称</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="all_qmgr_names" value="QMGR01" name="all_qmgr_names" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">主日志文件数</span>
												<input type="text" class="w45" id="mq_qmgr_plog" name="mq_qmgr_plog" value="10" />
											</div>
										</div>
									</div>

									<div class="control-group" id="hide1">
										<label class="control-label">QMGR数据路径</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="mq_data_path" name="mq_data_path"
													value="/var/mqm/qmgrs" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">次日志文件数</span>
												<input type="text" class="w45" id="mq_qmgr_slog" name="mq_qmgr_slog" value="10" />
											</div>
										</div>
									</div>

									<div class="control-group" id="hide2">
										<label class="control-label">QMGR日志路径</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="mq_log_path" name="mq_log_path" value="/var/mqm/log" />
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">日志文件页数(4KB)</span>
												<input type="text" class="w45" id="mq_log_psize" name="mq_log_psize" value="4096" />
											</div>
										</div>
									</div>

									<div class="control-group" id="hide3">
										<label class="control-label">通道连接是否保持</label>
										<div class="controls">
											<div class="inputb2l">
												<select style="width: 47.5%" id="mq_chl_kalive" class="w48" name="mq_chl_kalive">
													<option value="yes" selected="selected">Yes</option>
													<option value="no">No</option>
												</select>
											</div>
											<div class="inputb2l">
												<span class="input140 mr20">最大通道数</span>
												<input type="text" class="w45" id="mq_chl_max" name="mq_chl_max" value="1000" />
											</div>
										</div>
									</div>
									<div class="control-group" id="hide2">
										<label class="control-label">监听端口</label>
										<div class="controls">
											<div class="inputb2l">
												<input type="text" class="w45" id="mq_lstr_port" name="mq_lstr_port" value="1414" />
											</div>									
										</div>
									</div>
								</div>
							</div>

							<c:if test="${platform =='linux' }">
								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>系统属性
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">semmsl</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="lin_semmsl" name="lin_semmsl" value="500" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">semmns</span>
													<input type="text" class="w45" id="lin_semmns" name="lin_semmns" value="256000" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">semopm</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="lin_semopm" name="lin_semopm" value="250" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">semmni</span>
													<input type="text" class="w45" id="lin_semmni" name="lin_semmni" value="1024" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">shmmax</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="lin_shmax" name="lin_shmax" value="268435456" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">shmmni</span>
													<input type="text" class="w45" id="lin_shmni" name="lin_shmni" value="4096" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">shmall</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="lin_shmall" name="lin_shmall" value="2097152" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">file-max</span>
													<input type="text" class="w45" id="lin_filemax" name="lin_filemax" value="524288" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">nofile</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="lin_nofile" name="lin_nofile" value="10240" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">nproc</span>
													<input type="text" class="w45" id="lin_nproc" name="lin_nproc" value="4096" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:if>
							
							<c:if test="${platform =='aix'}">
								<div class="mainmodule">
									<h5 class="swapcon">
										<i class="icon-chevron-down"></i>系统属性
									</h5>
									<div class="form-horizontal">
										<div class="control-group">
											<label class="control-label">maxuproc</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="aix_maxuproc" name="aix_maxuproc" value="4096" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">nofiles</span>
													<input type="text" class="w45" id="aix_nofiles" name="aix_nofiles" value="10240" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">data</label>
											<div class="controls">
												<div class="inputb2l">
													<input type="text" class="w45" id="aix_data" name="aix_data" value="-1" />
												</div>
												<div class="inputb2l">
													<span class="input140 mr20">stack</span>
													<input type="text" class="w45" id="aix_stack" name="aix_stack" value="-1" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:if>
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
		</a>
		<a class="btn btn-info fr btn-down" onclick="CheckInput();">
			<span>下一页</span> <i class="icon-btn-next"></i>
		</a>
	</div>
	<!--footer end-->

	<script type="text/javascript">
    function change_hostname(obj){          //*********************change_hostname()改变主机名，实现联动变化 
    	var num = $(obj).val();  //获取改变后的主机名  
        var num_next = $(obj).parent().parent().parent()
                             .next("div").children("div").children("div")
                             .children("input[name='all_ips']").val();  //获取改变了主机名的那行ip值
        
        var index = $(obj).parent().parent().parent().parent().index();//获取当前行的索引 
        
        //定位到当前行的添加按钮  
        var curr_btn = $(obj).parent().parent().parent().next("div").next("div").next("div").next("div").children(".add_btn");

        //定位到下一个添加按钮 
        var next_btn = $(obj).parent().parent().parent().parent().nextAll().children("div").children(".add_btn:first");
        
        if($(next_btn).length > 0){ //如果存在 
        	var next_index = $(next_btn).parent().parent().index();//获取下一个添加按钮所在行的索引 
            for(var i=index;i<next_index-1;i++)
            {
            	$(".add_info").eq(i).children("div:eq(0)").children("div").children("div").children("input[name='all_hostnames']").val(num);
            }
        }
        else  //如果不存在 
        {
        	var total_line = $(".add_info").length;
        	for(var i=index;i<total_line;i++)
            {
            	$(".add_info").eq(i).children("div:eq(0)").children("div").children("div").children("input[name='all_hostnames']").val(num);
            }
        }
    }
	</script>

	<script type="text/javascript">    //*********************add_line()函数：按序添加                   delete_line()函数：删除当前行 	    
	    function add_line(obj){
		    var a = $(obj).parent().parent().prop("outerHTML");//点击按钮获得当前行
		    var b = $(a).append("<div style='float:left;margin-left:-100px;'><button class='btn del_btn' onclick='delete_line(this)'>删除</botton></div>").prop("outerHTML");//添加删除按钮 
		    var c = $(b).children("div:eq(4)").children("button").removeClass("add_btn")
		    		    .parent().parent().prop("outerHTML");//移除add_btn
		    var d = $(c).children("div").children("div").children("div").children("input[name='all_hostnames']").attr("readonly","readonly")
		                .parent().parent().parent().parent().prop("outerHTML"); 
		    var next_add_btn = $(obj).parent().parent().nextAll().children("div").children(".add_btn:first");//查找下一个添加按钮 
		    if($(next_add_btn))//如果下一个添加按钮存在，就在那行之前插入 
		    {
		    	$($(next_add_btn).parent().parent()).before(d);
		    }
		    if(obj == $(".add_btn:last").get(0))
		    {
		    	$(".add_info:last").after(d);
		    } 
	    }
	    
	    function delete_line(obj){
	    	 $(obj).parent().parent().remove();   
	    }
	</script>
	
	<script>
		/* 提取sweet提示框代码，以便后面方便使用，减少代码行数 */ 
		function sweet(te,ty,conBut)
		{
			swal({ title: "", text: te,  type: ty, confirmButtonText: conBut, });
		}
	</script>

	<script type="text/javascript">    //*********************CheckInput()函数：判断MQ安装版本不能为空，判断QMGR名称不能相同   
	function CheckInput()
	{  
		if($("#mq_version").val()== -1)
		{
			sweet("请选择MQ安装版本!","warning","确定");
			return;
		}

		var cre_method = $("input:radio:checked").val(); //获取队列管理器创建方式 
		if(cre_method == "yes")  //如果是"默认命令方式",则需要判断qmgr的名称不能相同，否则不用判断 
		{
	        var numArr = []; // 定义一个空数组
	        var txt = $(".all_qmgr_names"); //获取所有的qmgr名称文本框 
	        for (var i = 0; i < txt.length; i++) {
	            numArr.push(txt.eq(i).val()); // 将文本框的值添加到数组中
	        }
			for(var j=0;j<numArr.length;j++)   //逐层进行判断 
			{
				for(var k=j+1;k<numArr.length;k++)   
				{
					if(numArr[j] == numArr[k])
					{
						sweet("QMGR名称不能相同!","warning","确定");
						return ;
					}
				}
			}
		}
 		
		$("#submits").submit();
	}
	</script>

	<script type="text/javascript">    //*********************队列管理器创建方式	    
	    var total = $(".add_info").length;//总共多少条记录 
	            
		function sortRadio2() {   //不创建 
			$("#hide0").hide();
			$("#hide1").hide();
			$("#hide2").hide();
			$("#hide3").hide();	
			$('#hideserver').hide();
			$('#clusterInfo').hide();
	        $(".all_qmgr_names").each(function(){  //把所有的qmgr名称设置为"null" 
	        //	$(".all_qmgr_names").val("null");
	        	$(".all_qmgr_names").attr("readonly","readonly");
	        })
	        
	        $(".del_btn").parent().parent().remove();  //把添加的信息移除掉 
	        
	        $(".add_btn").css("display","none");  //把所有添加按钮移除掉 	        
	        	        
 	        for(var i=0;i<total;i++)
	        {
	        	$(".add_info").eq(i).children("div:eq(3)").children("div").children("div").children("div")
                              .addClass("select2-container-disabled");  //将下拉框禁用                             
	        }             
		}
		
		function sortRadio1() {   //默认方式 
			$("#hide0").show();
			$("#hide1").show();
			$("#hide2").show();
			$("#hide3").show();	
			$('#clusterInfo').show();
			$('#hideserver').show();
			$("#mon_port").val("1414");
			$("#mq_qmgr_plog").val("10");			
 			$("#mq_data_path").val("/var/mqm/qmgrs");
 			$("#mq_qmgr_slog").val("10");			
			$("#mq_log_path").val("/var/mqm/logs");
			$("#mq_log_psize").val("4096");
			$("#mq_chl_kalive").removeAttr("readonly");
			$("#mq_chl_kalive").attr("disabled", false);
			$("#mq_chl_kalive").prop('disabled', false);
			$("#mq_chl_max").val("1000");	
			
			$(".all_qmgr_names").each(function(){
	        	$(".all_qmgr_names").removeAttr("readonly");//解禁：qmgr名称
	        })
	        
	        $(".add_btn").css("display","block");  //恢复添加按钮 
	        
 	        for(var i=0;i<total;i++)
	        {
	        	$(".add_info").eq(i).children("div:eq(3)").children("div").children("div").children("div")
                              .removeClass("select2-container-disabled");  //恢复下拉框 
	        } 
		}
	</script>

	<script type="text/javascript">
		//自动加载
		function showfix(obj) 
		{
			var ver = obj.value;//获取版本
			var checkText=$(obj).find("option:selected").text();
			var platform1=$('#platform').val()
			$.ajax({
						url : "<%=path%>/getMqFix.do",
						data : { version : checkText,platform : platform1, pName:'mq'},
						type : 'post',
						dataType : 'json',
						success : function(result) 
						{		
							$("#mq_fp").empty();
							$("#mqfix").val('-');
							if(result.length==0)
							{
								var str="<option value='-'>"+ "请选择..." + "</option>";
								$("#mq_fp").append(str);
								$("#mqversion").val(checkText=$(obj).find("option:selected").text());
								return;
							}
							
							var str="<option value='-'>"+ "请选择..." + "</option>";
							for (var i = 0; i < result.length; i++) 
							{										
								for ( var key in result[i])
								{
								str += "<option value='" + result[i][key] + "'>" + key+ "</option>";}
																
							}
							$("#mq_fp").append(str);
							$("#mqversion").val(checkText=$(obj).find("option:selected").text());
						}
					});
		}
		
		function getVer(obj) 
		{
			if($("#mq_fp").find("option:selected").text()=='请选择...')
			{
	 			$("#mqfix").val('-'); 
				$("mq_fp").val('-');
			}
			else{
			 $("#mqfix").val($("#mq_fp").find("option:selected").text()); //8.0.0.4
		}}
		
		window.onload = function() 
		{
			var platform1=$('#platform').val()
			$.ajax({
				url : '<%=path%>/getMqVerion1.do',
				type : 'post',
				data : {platform : platform1,pName : 'mq'},
				dataType : 'json',
				success : function(result) {
				
					var str;		
					for (var i = 0; i < result.length; i++) {
						for ( var key in result[i])
							{
							str += "<option value='" + result[i][key] + "'>" + key+ "</option>";}
							}
		
						$("#mq_version").append(str);
						
						
			},failure:function(err)
			{
				alert(err)
			}
				}
			);
		}
	</script>
</body>
</html>
