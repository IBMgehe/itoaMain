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
<script type="text/javascript" src="js/echarts.js"></script> 
<title>云计算基础架构平台</title>
<style type="text/css">
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

.select2-container .select2-choice{   /* 设置下拉框的四个角的弧度 */ 
   /*  border-top-left-radius：4px;
　　border-top-right-radius:0px;　　
	border-bottom-right-radius:0px;
	border-bottom-left-radius:4px;  */  
	border-radius: 0px;
} 
input[type="text"]{   /* 设置文本框的四个角的弧度 */  
    border-radius：0px;
    width:300px;
}
.select2-container .select2-choice{    /* 设置下拉框的高度 */ 
	height:30px;
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
			<a href="" class="current"><i class="icon-home"></i>实例一览</a>
			<a href="#" class="current1">IBM 集中管理</a>
		</div>	
		
		<div style="text-align: center;margin-top:50px;">
			<div style="display:inline-block;width:510px;margin:0px auto;">
				<form action="">
					<div style="float:left;text-align:left;">
						<select style="width:150px;">
				           <option value="-1" selected="selected">请选择...</option>
				           <option value="was">WAS</option>
				           <option value="mq">MQ</option>
				           <option value="db2">DB2</option>
				        </select>
					</div>
					
					<div style="margin:0;padding:0;float:left;width:300px;">
						 <input id="enterIP" type="text" placeholder="请输入IP地址" 
						      style="height:30px;border-radius:0px;border-left:0px;border-right:0px;">
					</div>
					
					<div style="float:left;height:30px;width:60px;border:1px solid #CCCCCC;" onclick="showInfo()">
						<img src="img/icons/iconfont/bigGlass.png" />
					</div>
					
				</form>
			</div>
		</div>
		
		<div id="picStatus" style="display:none;">
			<div style="height:20px;width:50px;background-color:#C23531;float:left;margin-right:10px;margin-left:10%;"></div>
			<font family="Microsoft YaHei" size="2px;">本地节点</font>
			<div style="margin-top:5px;"></div>
			<div style="height:20px;width:50px;background-color:#008000;float:left;margin-right:10px;margin-left:10%;"></div>
			<font family="Microsoft YaHei" size="2px;">远程节点</font>	
			</div>	
		<div style="height:10px;"></div>
	</div>
	<!--content end-->



<script>    /* 功能：根据管理节点个数动态生成图表个数 */
	function showInfo()
	{
		$("#picStatus").show();
		
		var node_manage = 2;  //获取“管理节点”的个数
		var line_count = Math.ceil(node_manage / 2); //计算需要的行数，若为奇数，则向上取整 
		var node_manage_name = ["dmgr1" ,"dmgr2"/*,"dmgr3","dmgr4","dmgr5" */];//“管理节点”的名称,存在一个数组中 
		//定义一个二维数组，存放每个 “管理节点”的子节点（按顺序）
		var sub_node = [["app11","app12","app13","app14","app15"] ,
		                ["app21","app22","app23"]/*,
		                ["app31","app32","app33","app34"],
		                ["app41","app42"],
		                ["app51","app52","app53","app54","app55"] */];		

		     
		//根据主节点个数，在页面加载的时候动态生成n个div区域，每行2个
		if(node_manage == 1)
		{
			$(".content").append("<div name='pic' id='left"+i+"' style='width:100%;height:300px;float:left;'></div>");
		}
		else
		{
			for(var i=1;i<=line_count;i++)
			{
				$(".content").append("<div name='pic' id='left"+i+"' style='width:50%;height:300px;float:left;'></div><div name='pic' id='right"+i+"' style='width:50%;height:300px;float:right;'></div>");
			}
		}
			
		
		//获取所有name为pic的id
		var obj = document.getElementsByName("pic");
		var arr = [];
		//判断管理节点是奇数还是偶数	
		if(node_manage % 2 == 0)
		{
			for(var i =0;i<obj.length;i++)
			{
				arr.push(obj[i].id);   //偶数情况：arr数组存放所有pic的id 
			}
		}
		else
		{
			for(var i =0;i<obj.length-1;i++)
			{
				arr.push(obj[i].id);   //奇数情况：arr数组存放所有pic的id,去掉最后一个
			}
		}
		
		
		//开始设置关系图
		for(var i=0;i<sub_node.length;i++)
		{
			var myChart = echarts.init(document.getElementById(arr[i]));
			var dmgr = node_manage_name[i];
			var hen = 0;//初始化一个横坐标 
			var datas = [];
			var linkss = [];
			for(var j=0;j<sub_node[i].length;j++)
			{	
				datas.push({"name": sub_node[i][j], "x": hen, "y": 300});
				hen+=100;
			}
			datas.push({"name": dmgr, "x": hen/2-50, "y": 100});
			for(var j=0;j<sub_node[i].length;j++)
			{
				linkss.push({"source":dmgr,"target":sub_node[i][j]});
			}
			
			//
			
			var option = {
				    title: { text: '' },
				    tooltip: {},
				    animationDurationUpdate: 1500,
				    animationEasingUpdate: 'quinticInOut',
				    series : [
				        {
				            type: 'graph',
				            layout: 'none',
				            symbolSize: 50,  /* 圆的大小 */ 
				            roam: true,
				            label: { normal: { show: true } },
				            edgeSymbol: ['circle', 'arrow'],
				            edgeSymbolSize: [4, 10],  /* 关系线两端的箭头大小 */ 
				            edgeLabel: { normal: { textStyle: { fontSize: 20 } } },
				            data: datas,
				            links:linkss,
				            lineStyle: {
				                normal: {
				                    opacity: 0.9, /* 线条的透明度 */ 
				                    width: 2,  /* 线条的宽度 */ 
				                    curveness: 0  /* 直线，1，2，3....表示线的弯曲程度 */ 
				                }
				            }
				        }
				    ]
				}; 
				
			myChart.setOption(option);
		}
	}
</script>

</body>
</html>