<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>服务器日志出错问题</title>
</head>
<body>
<p>与服务器<%request.getRemoteAddr() ;%>的连接中断了</p>
<p>出错原因为：${errMsg }</p>

<p>更多详细内容：</p>
<p>请检查Tomcat服务器端日志/var/log/itoa/itoa_log</p>
<p>请检查tornado服务器端日志/var/log/itoa/tornado_rotatefile.log</p>

<a href="getAllServers.do">回到主页</a>
<script type="text/javascript">

</script>
</body>
</html>