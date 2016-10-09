<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<link rel="stylesheet" href="css/reset.css">
<!-- CSS reset -->
<link rel="stylesheet" href="css/style.css">
<!-- Resource style -->
<link href="css/jquery-accordion-menu.css" rel="stylesheet" type="text/css" />
<link href="css/font-awesome.css" rel="stylesheet" type="text/css" />

<style type="text/css">
   .nano {
		
	}
	.nano > .nano-content { }
   .nano .pane   { background: #888; }
   .nano .slider {background: #111;}//兼容IE8
.top5 {
	position: relative;
	padding-left:5px; 
	color:red;
}
.img_icon {
	position:relative;
	right:3px;
	top:-2px;
	width:18px;
	height:18px;
	max-width:18px;
}
.tooltip-inner {
	color:white;
	background-color:#413A3A;
}
.tooltip-arrow{
	border-right-color:black;
}
.tooltip {
	font-size:15px;
	line-height:25px;
	height:40px;
	width:125px;
	position:fixed;
}
.notvisible{
	display:none;
}
.isvisible{
	display:block;
}
.mark1 {
	display:none;
}
</style>

<script src="js/jquery-accordion-menu.js" type="text/javascript"></script>
<script type="text/javascript">
	 $(function(){
		 $("[data-toggle='tooltip']").tooltip();
	     $(".nano").nanoScroller();
	     });
	 
	 
	 //show last menu
	 jQuery(document).ready(function () {
		 $(".has-children").bind("hover",function(){
			 var offset1 = $(this).offset();
			 var topof = offset1.top;
			 
			 $(this).children("ul").css("top",topof);
			 $(this).children("ul").css("left","14%");
			 $(this).children("ul").css("width","160px");
		 });
	 });
	
	 
	 
	 
	 
	var button1 = "<a href='#' id='drawback1' style='padding:0px 80px 8px 90px;'><img src='img/icons/iconfont/threev.png' style='margin-top:8px;'></img></a>";
	var button2 = "<a href='#' id='drawback2' style='padding:0px 10px 8px 10px;'><img src='img/icons/iconfont/threeh.png' style='margin-top:8px;'></img></a>";
	jQuery(document).ready(function () {
		jQuery("#jquery-accordion-menu").jqueryAccordionMenu();
		
		
		//点击缩起来的图标展示相关子菜单
		$("#jquery-accordion-menu").children("div").children("ul").children("li").children(".showsubmenu").bind("click", function(e){
        		  			 
	   			 $(".content").animate({width:"86%"},1,function(){
	   			 $("#jquery-accordion-menu").animate({width:"14%"},1,function(){
	   				 $(".tooltipa1").addClass("notvisible");
	   				 $(".tooltipa2").removeClass("notvisible");
	   				  $('.nosubmenu').find('.has-children.selected').removeClass('selected');
	   				  $("#drawback2").replaceWith(button1);
	   				  $(".mark1").removeClass("mark1"); 	
	   				  $(".columnfoot").css("width","82.7%");
	   				  $(".columnfoot").css("left","15%");
	   				 });
	   			 });
   		
		   		 $("#menu11").show();
		   		 $("#menu16").show();
		   		 $("#menu12").show();
		   		 $("#menu13").show();
		   		 $("#menu14").show();
		   		 $("#menu15").show();
		   		 $("#menu17").show();	
		   		 
		   		 $("#showonce").delay(0).slideDown(300);
		   		$("#forremoveminux").addClass("submenu-indicator-minus");
		   	
        	});
		
		//左侧菜单栏的隐藏和显示 开始
		$("body").on("click","#drawback1",function(){
		 	$(".tooltipa1").removeClass("notvisible");
			$(".tooltipa2").addClass("notvisible");
		 	$("#menu11").hide();
		 	$("#menu12").hide();
		 	$("#menu13").hide();
			$("#menu14").hide();
			$("#menu15").hide();
			$("#menu16").hide();
			$("#menu17").hide();
		 	$("#jquery-accordion-menu").animate({width:"56px"},1,function(){
			 	$(".nosubmenu").css("display","none");			//收缩后将三级菜单收起
			 	$('.nosubmenu').find('.has-children.selected').removeClass('selected');	//将三级菜单还原到默认情况
			 	$("#drawback1").replaceWith(button2);			//将收缩绑定的图标替换
			 	$(".submenu-indicator").addClass("mark1");		//去除右侧的加号
			 	$(".content").css("width","calc(100% - 57px)");	//将侧边栏右侧的主页面部分拓宽
		 	 	$(".columnfoot").css("width","92.6%");		//下一页，上一页的拓宽
			 	$(".columnfoot").css("left","5.1%");			//下一页，上一页往左移
		 	});		
		});
	
		$("body").on("click","#drawback2",function(){
			 
			 
			 $(".content").animate({width:"86%"},1,function(){
			 $("#jquery-accordion-menu").animate({width:"14%"},1,function(){
				 $(".tooltipa1").addClass("notvisible");
				 $(".tooltipa2").removeClass("notvisible");
				  $('.nosubmenu').find('.has-children.selected').removeClass('selected');
				  $("#drawback2").replaceWith(button1);
				  $(".mark1").removeClass("mark1"); 
				  $('#forremoveminux').removeClass('submenu-indicator-minus');	
				  $(".columnfoot").css("width","82.7%");
				  $(".columnfoot").css("left","15%");
				 });
		 });
		
		 $("#menu11").show();
		 $("#menu16").show();
		 $("#menu12").show();
		 $("#menu13").show();
		 $("#menu14").show();
		 $("#menu15").show();
		 $("#menu17").show();
	});
	//左侧菜单栏的隐藏和显示 结束
});
</script>

<script>
	var i = 1;
	var j = 1;
 	$("body").on("click","#proAndser,#proAndser2",function(){
		$("#demo-list").toggle();
		if(i%2 == 1){
			var state1 = "<img class='forrotate' src='img/icons/iconfont/arrowright.png' style='position:relative;top:16px;'></img>";
			$(".forrotate").replaceWith(state1); 
		}else if(i%2 == 0) {
			var state1 = "<img class='forrotate' src='img/icons/iconfont/arrowdown.png' style='position:relative;top:16px;'></img>";
			$(".forrotate").replaceWith(state1); 
		}
		i++;
		
		
	}) 
	$("body").on("click","#usercenter,#usercenter2",function(){
		$("#demo-list1").toggle();
		if(j%2 == 1){
			var state1 = "<img class='forrotate2' src='img/icons/iconfont/arrowright.png' style='position:relative;top:16px;'></img>";
			$(".forrotate2").replaceWith(state1); 
		}else if(j%2 == 0) {
			var state1 = "<img class='forrotate2' src='img/icons/iconfont/arrowdown.png' style='position:relative;top:16px;'></img>";
			$(".forrotate2").replaceWith(state1); 
		}
		j++;
	})
</script>

	<header class="cd-main-header">
		<nav style="display: block;float: left;height: 100%;">
			<ul style="height:100%;list-style:none;margin:0px">
				<li style="height:100%;display:inline-block;width:56px;padding-top:14px;padding-left:10px;background:#465A96;position:relative;">
					<img src="img/logo_new.png" style="position: relative; bottom: 5px;"></img>
				</li>
				<li style="height:100%;display:inline-block;position:relative;bottom:30px;left:10px;">
					<a href="#" style="position: relative; top: 25px;">自动化运维平台</a>
				</li>
			</ul>
		</nav>  

		<nav class="cd-nav">
			<ul class="cd-top-nav">
				<li>
					<a href="getAllServers.do" style="color: white;">
						<i class="icon-home icon-white" style="margin: 3px 5px;"></i>主页    
					</a>
				</li>
				<li>
					<a href="#0" style="color: white;"> 
						<i class="icon-user icon-white" style="margin: 5px;"></i> <span>${userName }</span>
					</a>
				</li>
				<li>
					<a href="logout.do" style="color: white;"> 
						<i class="icon-off icon-white" style="margin: 5px;"></i> <span>注销</span>&nbsp;&nbsp;
					</a>
				</li>
			</ul>
		</nav>
	</header>
	<!-- .cd-main-header -->


	<div id="jquery-accordion-menu" class="jquery-accordion-menu red downleft nano" style="z-index: 5;width:56px;">
	<div class="nano-content">
		<div style="text-align: center; height:30px;">
			<a href="#" id="drawback2">
				<img src="img/icons/iconfont/threeh.png" style="margin-top:8px;padding:0px 10px 8px 10px;"></img>
			</a>
		</div>

		<div class="jquery-accordion-menu-footer tooltipa1" id="proAndser" style="cursor:pointer;" data-toggle="tooltip" data-placement="right" title="产品与服务">
			<img class="forrotate" src="img/icons/iconfont/arrowdown.png" style="position:relative;top:16px;"></img>&nbsp;&nbsp;
			
		</div>
		
		<div class="jquery-accordion-menu-footer tooltipa2 notvisible" id="proAndser2" style="cursor:pointer;">
			<img class="forrotate" src="img/icons/iconfont/arrowdown.png" style="position:relative;top:16px;"></img>&nbsp;&nbsp;
			<span id="menu11" style="position:relative;top:15px;font-size:13px;" >产品与服务</span>
		</div>
		
		<ul id="demo-list">
			<li >
				<a href="#" class="tooltipa1 showsubmenu" data-toggle="tooltip" data-placement="right" title="自动化部署">
					<img class="img_icon" src="img/icons/iconfont/deploy.png"></img>
				</a>
				<a href="#" class="notvisible tooltipa2" id="forremoveminux">
					<img class="img_icon" src="img/icons/iconfont/deploy.png"></img>&nbsp;&nbsp;&nbsp;
					<span id="menu12" class="top5">自动化部署</span> 
				</a>
				<ul class="nosubmenu submenu" id="showonce">
					<li class="has-children">
						<a href="getLogInfo.do"><span>部署历史</span></a>
					</li>
					<li class="has-children"><a href="#">IBM IHS </a>
						<ul style="width:100px;">
							<li><a href="getIBMAllInstance.do?ptype=ihs">IHS 单节点</a></li>
						</ul>
					</li>
					<li class="has-children"><a href="#">IBM WAS </a>
						<ul>
							<li><a href="getIBMAllInstance.do?ptype=was">WAS 单节点</a></li>
							<li><a href="getIBMAllInstance.do?ptype=wascluster">WAS 集群</a></li>
						</ul>
					</li>
					<li class="has-children"><a href="#">IBM MQ </a>
						<ul>
							<li><a href="getIBMAllInstance.do?ptype=mq">MQ 单节点</a></li>
							<li><a href="getIBMAllInstance.do?ptype=mqcluster">MQ 集群</a></li>
						</ul>
					</li>
					<li class="has-children"><a href="#">IBM DB2 </a>
						<ul>
							<li><a href="getIBMAllInstance.do?ptype=db2">DB2 单节点</a></li>
							<li><a href="getIBMAllInstance.do?ptype=db2ha&platform=aix">DB2 HA</a></li>
						</ul>
					</li>
					<li class="has-children"><a href="#">IBM Oracle </a>
						<ul>
							<li><a href="getIBMAllInstance.do?ptype=oracle">Oracle 单节点</a></li>
						</ul>
					</li>
					<li class="has-children"><a href="#">IBM ITM </a>
						<ul>
							<li><a href="getIBMAllInstance.do?ptype=itmos">OS Agent</a></li>
							<li><a href="getIBMAllInstance.do?ptype=itmwas">WAS Agent</a></li>
							<li><a href="getIBMAllInstance.do?ptype=itmmq">MQ Agent</a></li>
							<li><a href="getIBMAllInstance.do?ptype=itmdb2">DB2 Agent</a></li>
						</ul>
					</li>
				</ul>
			</li>
			
			<li>
				<a href="healthCheck.do" class="tooltipa1" data-toggle="tooltip" data-placement="right" title="自动化巡检">
					<img class="img_icon" src="img/icons/iconfont/patrol.png" id="icon12"></img>
					
				</a>
				<a href="healthCheck.do" class="notvisible tooltipa2">
					<img class="img_icon" src="img/icons/iconfont/patrol.png" id="icon12"></img>&nbsp;&nbsp;&nbsp;
					<span id="menu13" class="top5">自动化巡检</span> 
			    </a>
			</li>
			
			<li>
				<a href="configCompare.do" class="tooltipa1" data-toggle="tooltip" data-placement="right" title="配置跟踪比对">
					<img class="img_icon" src="img/icons/iconfont/trace.png" id="icon13"></img>
				
				</a>
				<a href="configCompare.do" class="notvisible tooltipa2">
					<img class="img_icon" src="img/icons/iconfont/trace.png" id="icon13"></img>&nbsp;&nbsp;&nbsp;
					<span id="menu14" class="top5">配置跟踪比对</span> 
				</a>
			</li>
			
			<li>
				<a href="logCatch.do" class="tooltipa1" data-toggle="tooltip" data-placement="right" title="日志抓取">
					<img class="img_icon" src="img/icons/iconfont/logcatch17.png" id="icon14"></img>
					
				</a>
				<a href="logCatch.do" class="notvisible tooltipa2">
					<img class="img_icon" src="img/icons/iconfont/logcatch17.png" id="icon14"></img>&nbsp;&nbsp;&nbsp;
					<span id="menu15" class="top5">日志抓取</span> 
				</a>
			</li>		
		</ul>
		
		
		<div class="jquery-accordion-menu-footer tooltipa1" id="usercenter" style="cursor:pointer;" data-toggle="tooltip" data-placement="right" title="用户中心">
			<img class="forrotate2" src="img/icons/iconfont/arrowdown.png" style="position:relative;top:16px;"></img>&nbsp;&nbsp;
			
		</div>
		<div class="jquery-accordion-menu-footer tooltipa2 notvisible" id="usercenter2" style="cursor:pointer;">
			<img class="forrotate2" src="img/icons/iconfont/arrowdown.png" style="position:relative;top:16px;"></img>&nbsp;&nbsp;
			<span id="menu16" style="position:relative;top:15px;font-size:13px;">用户中心</span>
		</div>
		
		
		<ul id="demo-list1">
			<li>
				<a href="accountManage.do" class="tooltipa1" data-toggle="tooltip" data-placement="right" title="账号管理">
					<img class="img_icon" src="img/icons/iconfont/account.png" id="icon15"></img>
					
				</a>
				<a href="accountManage.do" class="notvisible tooltipa2">
					<img class="img_icon" src="img/icons/iconfont/account.png" id="icon15"></img>&nbsp;&nbsp;&nbsp;
					<span id="menu17" class="top5">账号管理</span> 
			    </a>
			</li>
		</ul>
		</div>
	</div>


	<script src="js/main.js"></script>

	<!-- Resource jQuery -->
</html>