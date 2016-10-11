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
					
					<div style="float:left;height:30px;width:60px;border:1px solid #CCCCCC;" onclick="">
						<img src="img/icons/iconfont/bigGlass.png" />
					</div>
					
				</form>
			</div>
		</div>
		
		<!-- 图表 -->
		<div style="text-align:center;">
			<div id="main" style="height:300px;width:800px;margin:0 auto;"></div>
		</div>
</div>

<script>
	function try11()
	{
			
	}
</script>

<script>
var myChart = echarts.init(document.getElementById('main'));

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
	            label: {
	                normal: {
	                    show: true
	                }
	            },
	            edgeSymbol: ['circle', 'arrow'],
	            edgeSymbolSize: [4, 10],  /* 关系线两端的箭头大小 */ 
	            edgeLabel: {
	                normal: {
	                    textStyle: {
	                        fontSize: 20
	                    }
	                }
	            },
	            data: [{ name: '节点1', x: 300, y: 300 ,
	            			itemStyle:{
	            				normal:{ color:'green' }
	            			}
	                   }, 
	                   { name: '节点2', x: 400, y: 300 }, 
	                   { name: '父节点', x: 500, y: 100 },  
	                   { name: '节点3', x: 500, y: 300 }, 
	                   { name: '节点4', x: 600, y: 300 },
	                   { name: '节点5', x: 700, y: 300 },
	                   { name: '节点0', x: 200, y: 300 },
	                   { name: '节点6', x: 800, y: 300 }],
	            links: [
	            /* {   子节点之间的关系 
	                source: 0,
	                target: 1,
	                symbolSize: [5, 20],
	                label: {
	                    normal: {
	                        show: true
	                    }
	                },
	                lineStyle: {
	                    normal: {
	                        width: 5,
	                        curveness: 0.2
	                    }
	                }
	            },  */
	            /* {
	                source: '节点2',
	                target: '节点1',
	                label: {
	                    normal: {
	                        show: true
	                    }
	                },
	                lineStyle: {
	                    normal: { curveness: 0.2 }
	                }
	            },  */
	            {
	                source: '父节点',
	                target: '节点1'	 
	            }, 
	            {
	                source: '父节点',
	                target: '节点2'
	            },
	            {
	                source: '父节点',
	                target: '节点4'
	            },
	            {
	                source: '父节点',
	                target: '节点3'
	            },
	            {
	                source: '父节点',
	                target: '节点5'
	            },
	            {
	                source: '父节点',
	                target: '节点0'
	            },
	            {
	                source: '父节点',
	                target: '节点6'
	            }
	            ],
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
</script>

</body>
</html>