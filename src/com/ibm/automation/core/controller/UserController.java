package com.ibm.automation.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.core.bean.LoginBean;
import com.ibm.automation.core.bean.ServersBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.PropertyUtil;
import com.ibm.automation.core.util.SecurityUtil;
import com.ibm.automation.core.util.ServerUtil;

import net.sf.json.JSONObject;

@Controller
public class UserController {
	Properties amsCfg = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(UserController.class);

	@Autowired 
	private ServerService  addHostService;
	
	@RequestMapping(value="/login.do")
	public String login(HttpServletRequest request, HttpServletResponse resp, HttpSession session) {
		// 如果已经登录，访问/login.do 直接跳转到getAllServers.do
		if (request.getSession().getAttribute("userName") != null) {
			logger.info("login.do::正在调转到getAllServers.do");
			return "redirect:/getAllServers.do";
		}
		// 未登录，直接访问login.do是不行的
		if (request.getParameter("userName") == null && request.getParameter("password") == null) {
			logger.info("login.do::用户未登录，跳转到login.jsp");
			return "redirect:/login.jsp";
		}
		// 从/login.jsp登录后执行的代码
		LoginBean user = new LoginBean();
		byte[] passByte = Base64.decodeBase64(request.getParameter("password"));// base64解密
		user.setPassword(SecurityUtil.encrypt(new String(passByte), amsCfg.getProperty("key"))); // 加密字符串
		user.setUsername(request.getParameter("userName"));
		String strOrgUrl = addHostService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_service_users);
		ObjectNode on = addHostService.createSendJson("login", user);
		logger.debug("the send url is" +strOrgUrl.toString() +" and the send data is "+on.toString());
		try {
			String response = HttpClientUtil.postMethod(strOrgUrl, on.toString());
			if (!response.equals("")) {
				JsonNode jn = om.readTree(response);
			//	JSONObject jsonObj = JSONObject.fromObject(response);
			//	int retVal = (Integer) jsonObj.get("status"); // 返回状态
			//	String retMsg =jsonObj.get("message").toString(); // 返回描述符
				int retVal = jn.get("status").asInt();
				JsonNode retMsg = jn.get("message");
				if (retVal == 1) // 登录成功
				{
					List<ServersBean> lahb = ServerUtil.getList("odata/servers");
					request.setAttribute("servers", lahb);
					request.setAttribute("total", lahb.size());
					JsonNode innerNode = om.readTree(retMsg.asText());
					String[] proList = innerNode.get("product").asText().toUpperCase().split(",");
					request.getSession().setAttribute("userName", user.getUsername());
					request.getSession().setAttribute("proList", proList);
					request.getSession().setAttribute("role", innerNode.get("role").asInt());
					logger.info("登录成功，正在为您跳转！");
					return "instance_main_list";
				} else {
					request.setAttribute("errorMessageFlag", "fail");
					request.setAttribute("errorMessage", retMsg);
					return "forward:/login.jsp";
				}
			} else {
				request.setAttribute("errorMessageFlag", "networkfail");
				request.setAttribute("errorMessage", "接口未收到返回信息");
				logger.info("login.do::接口未收到返回信息");
				return "forward:/login.jsp";
			}
		} catch (NetWorkException e) {
			request.setAttribute("errorMessageFlag", "networkfail");
			request.setAttribute("errorMessage", e.getMessage());
			logger.info("login.do::"+e.getMessage());
			return "forward:/login.jsp";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			request.setAttribute("errorMessageFlag", "networkfail");
			request.setAttribute("errorMessage", e.getMessage());
			logger.info("login.do::"+e.getMessage());
			return "forward:/login.jsp";
		}
	}
	
	
	
	
	
	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse resp, HttpSession session) {
		request.getSession(true);
		request.getSession().setAttribute("username", null);
		request.getSession().invalidate();
		logger.info("用户::"+request.getSession().getAttribute("userName")+"退出系统。" );
		return "redirect:/login.jsp";
	}
	
	
	/*
	 * 账号管理主页
	 */
	
	@RequestMapping(value="/accountManage")
	public String accountManager(HttpServletRequest request)
	{
		List<LoginBean> list = ServerUtil.getAllUsers("/api/v1/users");
		request.setAttribute("userList", list);
		request.setAttribute("total", list.size());
		return "accountManage";
	}
	
	
	
	
	/*
	 * 添加账号
	 */
	@RequestMapping(value="/addUser")
	@ResponseBody
	public int userAdd(HttpServletRequest request)
	{
		String name = request.getParameter("username");
		String passwd = request.getParameter("passwd");
		String role = request.getParameter("role");
		String email = request.getParameter("email");
		String[] products= request.getParameterValues("manageProduct");
		ObjectNode on = om.createObjectNode();
		on.put("type", "addUser");
		on.put("name", name);
		
		on.put("password", SecurityUtil.encrypt(new String(Base64.decodeBase64(passwd)), amsCfg.getProperty("key")));
		on.put("email", email);
		on.put("role", Integer.valueOf(role));
		
		ArrayNode an = om.createArrayNode();
		for(String pro : products)
		{
			an.add(pro);
		}
		on.putPOJO("product", an);
		String strOrgUrl = addHostService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_service_users);
		try {
			String retMsg=HttpClientUtil.postMethod(strOrgUrl, on.toString());
			JsonNode retJson = om.readTree(retMsg);
			return retJson.get("status").asInt();
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加用户发生错误，错误原因为："+e.getMessage());
		}
		return -1;
	}
	/*
	 * 检查账号是否存在   1存在  - 0  不存在
	 */
	@RequestMapping(value="/checkUser")
	@ResponseBody
	public int checkUser(HttpServletRequest request)
	{
		String name = request.getParameter("username");//用户名校验
		ObjectNode on = om.createObjectNode();
		on.put("type", "checkUser");
		on.put("name", name);
		String strOrgUrl = addHostService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_service_users);
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, on.toString());
			JsonNode retJson = om.readTree(retMsg);
			int retStatus = retJson.get("msg").asInt();
			return retStatus; //
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加检查用户账号发生错误，错误原因为："+e.getMessage());
			
		}
		return -1; 
	}
	/**
	 * 删除账号
	 */
	@RequestMapping(value="/delUser")
	@ResponseBody
	public int delUser(HttpServletRequest request)
	{
		String data1 = request.getParameter("data1");//用户名校验
		
		ObjectNode on = om.createObjectNode();
		ArrayNode an = om.createArrayNode();
		String[] names = data1.split(",");
		for(String name : names)
		{
			an.add(name);
		}
		on.put("type", "delUser");
		on.putPOJO("name", an);
		String strOrgUrl = addHostService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_service_users);
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, on.toString());
			JsonNode retJson = om.readTree(retMsg);
			int retStatus = retJson.get("status").asInt();
			return retStatus; //
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除用户发生错误，错误原因为："+e.getMessage());
			
		}
		return -1; 
	}
	public static void main(String[] args) {

		
	}
	
	
	/*
	 * 账号管理主页
	 */
	
	@RequestMapping(value="/cenManage")
	public String cenManage(HttpServletRequest request)
	{
		return "cenManage";
	}
}
