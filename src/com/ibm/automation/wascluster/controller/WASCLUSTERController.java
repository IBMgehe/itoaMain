package com.ibm.automation.wascluster.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.ams.service.AmsRestService;
import com.ibm.automation.ams.util.AmsClient;
import com.ibm.automation.ams.util.AmsV2ClientHttpClient4Impl;
import com.ibm.automation.core.bean.FinalStatusBean;
import com.ibm.automation.core.bean.ServersBean;
import com.ibm.automation.core.bean.WasClusterBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.FpService;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.EncodeUtil;
import com.ibm.automation.core.util.FormatUtil;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.PropertyUtil;
import com.ibm.automation.core.util.ServerUtil;

import net.sf.json.JSONObject;

@Controller
public class WASCLUSTERController {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private FpService fpService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(WASCLUSTERController.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	private List<ServersBean> addHostList;
	
	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	
	
	@RequestMapping("/toWasClusterNextPage.do")
	public String toWasClusterNextPage(HttpServletRequest request, HttpSession session) throws Exception{
		String platform = request.getParameter("platform");// 判断是AIX 还是linux 平台
		String status = request.getParameter("status");// 得到哪个页面的状态
		String wasfix = request.getParameter("wasfix");// was 版本补丁的简单形式
		String serId = request.getParameter("serId");
		request.setAttribute("serId", serId);
		request.setAttribute("platform", platform);

		String[] SserIds = serId.split(",");
		String[] all_hostnames = request.getParameterValues("all_hostnames");// 主机名
		String[] all_ips = request.getParameterValues("all_ips");// IP地址
		String[] all_profile_names = request.getParameterValues("all_profile_names");// profile名称
		String[] all_profile_types = request.getParameterValues("all_profile_types");// profile类型
		
		List<WasClusterBean> wcb = new ArrayList<WasClusterBean>();
		for (int i = 0; i < all_ips.length; i++) {
			WasClusterBean bean = new WasClusterBean();
			bean.setIp(all_ips[i]);
			bean.setName(all_hostnames[i]);
			bean.setProfiletype(all_profile_types[i]);
			if(all_profile_types[i].equalsIgnoreCase("cell")) //做dmgr IP判断
			{
				request.setAttribute("was_dmgr_ip",all_ips[i] );
			}
			bean.setProfilename(all_profile_names[i]);
			bean.setUuid(SserIds[i]);
			wcb.add(bean);
		}
		
		request.setAttribute("wasfix", wasfix);
		request.setAttribute("was_user", request.getParameter("was_user"));
		request.setAttribute("was_fp", request.getParameter("was_fp"));
		request.setAttribute("was_version", request.getParameter("wasversion"));
		request.setAttribute("was_inst_path", request.getParameter("was_inst_path"));
		request.setAttribute("was_im_path", request.getParameter("was_im_path"));
		request.setAttribute("was_binary", request.getParameter("was_version"));
		request.setAttribute("all_hostnames", all_hostnames);
		request.setAttribute("all_ips", all_ips);
		request.setAttribute("all_profilenames", all_profile_names);
		request.setAttribute("all_profiletypes", all_profile_types);
		request.setAttribute("was_profile_path", request.getParameter("was_profile_path"));
		request.setAttribute("was_userid", request.getParameter("was_userid"));
		request.setAttribute("was_password", request.getParameter("was_password"));
		
		request.setAttribute("was_nofile", request.getParameter("was_nofile"));
		request.setAttribute("was_fsize", request.getParameter("was_fsize"));
		request.setAttribute("was_core", request.getParameter("was_core"));
		request.setAttribute("was_nproc", request.getParameter("was_nproc"));
		
		//获取was im 版本
		String wasimversion=request.getParameter("wasversion");//为了获取was im 版本
		String imout=fpService.getIm(platform, "was", wasimversion);
		logger.info("wasclusterController::获取was im 版本"+ imout);
		JSONObject obj = JSONObject.fromObject(imout);
		String wasim=(String)obj.get(wasimversion);
		request.setAttribute("was_im", wasim);
		
		request.setAttribute("allservers", wcb);
		String totaljson = om.writeValueAsString(wcb);
		request.getSession().setAttribute("allservertotaljson", totaljson);
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("调用WASCLUSTERController::toWasClusterNextPage 所有的参数信息为:os = " + platform + " status=" + status);
		}
		
		List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial);
		addHostList = listDetial;
		if (status.equals("confirm")) {
			return "instance_wascluster_comfirm";
		}
		return null;
	}
	
	
	@RequestMapping("/installWasClusterInfo.do")
	public String installWasClusterInfo(HttpServletRequest request, HttpSession session){
		//String platform = request.getParameter("platform");
		ObjectNode outNode = om.createObjectNode();
		ArrayNode iplistArrNode = om.createArrayNode();
		ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode was_profile_listArrNode = om.createArrayNode();
		ArrayNode all_profile_typesArrNode = om.createArrayNode();
		//ObjectNode all_profile_typesArrNode = om.createObjectNode();
		ArrayNode user_listArrNode = om.createArrayNode();
		ArrayNode password_listArrNode = om.createArrayNode();
		ArrayNode final_an = om.createArrayNode();
		JavaType javaType = getCollectionType(ArrayList.class, WasClusterBean.class);
		String allsrvs = (String) request.getSession().getAttribute("allservertotaljson");
		
		List<WasClusterBean> wcb = null;		
		try {
			wcb = (ArrayList<WasClusterBean>) om.readValue(allsrvs, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.debug("installWasClusterInfo::" + e.getMessage());
			return "redirect:/500.jsp";
		}
		Iterator<WasClusterBean> iter = wcb.iterator();
		Iterator<ServersBean> ahbiter = addHostList.iterator();
		while (iter.hasNext()) {
			ObjectNode final_o = om.createObjectNode();
			WasClusterBean temp = iter.next();
			ServersBean ahb = ahbiter.next();
			iplistArrNode.add(temp.getIp());
			hostnamelistArrNode.add(temp.getName());
			was_profile_listArrNode.add(temp.getProfilename());
			all_profile_typesArrNode.add(temp.getProfiletype());
			user_listArrNode.add(ahb.getUserid());
			password_listArrNode.add(ahb.getPassword());
			outNode.putPOJO("ip_list", iplistArrNode);
			outNode.putPOJO("hostname_list", hostnamelistArrNode);
			outNode.putPOJO("was_profiletype_list" , all_profile_typesArrNode);
			outNode.putPOJO("was_profilename_list" , was_profile_listArrNode);
			//outNode.putPOJO("user_list", user_listArrNode);
			//outNode.putPOJO("password_list", password_listArrNode);
			if(temp.getProfiletype().equalsIgnoreCase("cell"))
			{
				final_o.put("role", 1);
			}else 
				final_o.put("role", 0);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);
		}
		
		outNode.put("softpath", amsprop.getProperty("softpath") + "was");
		outNode.put("downloadpath", "/tmp/was");
		outNode.put("ftp_user", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));
		
		ObjectNode sysParam = om.createObjectNode();
		String fsize = request.getParameter("was_fsize");
		String core = request.getParameter("was_core");
		sysParam.put("nofile", (String) request.getParameter("was_nofile"));
		sysParam.put("nproc", request.getParameter("was_nproc"));
		if (fsize.equals("unlimited"))
			sysParam.put("fsize", "-1");
		else
			sysParam.put("fsize", (String) request.getParameter("was_fsize"));
		if (core.equals("unlimited"))
			sysParam.put("core", "-1");
		else
			sysParam.put("core", (String) request.getParameter("was_core"));
		outNode.putPOJO("os_para_ulimit", sysParam);
		outNode.put("wasfix", request.getParameter("wasfix"));  //用于历史页面显示简单补丁格式
		outNode.put("was_user", request.getParameter("was_user"));
		outNode.put("was_version", request.getParameter("was_version"));
		outNode.put("was_im_path", request.getParameter("was_im_path"));
		outNode.put("was_im", request.getParameter("was_im"));
		outNode.put("was_inst_path", request.getParameter("was_inst_path"));
		outNode.put("was_im", request.getParameter("was_im"));
		outNode.put("was_dmgr_ip", request.getParameter("was_dmgr_ip"));
		outNode.put("was_binary", request.getParameter("was_binary"));
		outNode.put("was_fp", request.getParameter("was_fp"));		
		outNode.put("was_profile_path", request.getParameter("was_profile_path"));
		outNode.put("was_userid", request.getParameter("was_userid"));
		outNode.put("was_password", request.getParameter("was_password"));		
		logger.info("installWasClusterInfo.do::" + outNode.toString());
		
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid 
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText());
		// 唯一索引
		final_on.put("playbook-name", "wascluster");// task-name task 名
		final_on.put("product-name", "was");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
		final_on.putPOJO("nodes", final_an);
		logger.info("installWasClusterInfo::" + final_on.toString());
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installWasClusterInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("installWasClusterInfo.do::" + e.getMessage());
		}
		return "redirect:/500.jsp";
	}

	
	@RequestMapping("/getwasclusterLogInfoDetail.do")
	public String getwasclusterLogInfoDetail(HttpServletRequest request, HttpSession session){
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "wascluster");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=wascluster");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) {
			trans = (ObjectNode) jn;
		}
		
		if (curPlaybook != null && curPlaybook.get("options") != null)
		{
			try{
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				request.setAttribute("was_user", options.get("was_user").asText());				
				request.setAttribute("was_version", options.get("was_version").asText());
				request.setAttribute("was_inst_path", options.get("was_inst_path").asText());
				request.setAttribute("was_fix", options.get("wasfix").asText());
				request.setAttribute("was_im_path", options.get("was_im_path").asText());
				request.setAttribute("was_dmgr_ip", options.get("was_dmgr_ip").asText());
				
				request.setAttribute("was_profile_path", options.get("was_profile_path").asText());
				request.setAttribute("was_userid", options.get("was_userid").asText());
				request.setAttribute("was_password", options.get("was_password").asText());
				
				JsonNode sys_param = options.get("os_para_ulimit");
				request.setAttribute("was_nproc", sys_param.get("nproc").asText());
				request.setAttribute("was_nofile", sys_param.get("nofile").asText());
				request.setAttribute("was_fsize", sys_param.get("fsize").asText());
				request.setAttribute("was_core", sys_param.get("core").asText());
			}catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getwasclusterLogInfoDetail::"+e.getMessage());
			}
		}
		
		List<String> serIds = new ArrayList<String>();
		if (curPlaybook != null && curPlaybook.get("nodes") != null) 
		{
			ArrayNode jn = (ArrayNode) curPlaybook.get("nodes");
			for (JsonNode js : jn) {
				serIds.add(js.get("uuid") == null ? "" : js.get("uuid").asText());
			}
		}
		List<ServersBean> lahb = ServerUtil.getList("odata/servers");
		Collections.sort(lahb);
		
		List<ServersBean> listDetial = new ArrayList<ServersBean>();
		for (int i = 0; i < lahb.size(); i++) {
			String serverId = lahb.get(i).getUuid();
			for (int j = 0; j < serIds.size(); j++) {
				if (serverId.equals(serIds.get(j))) {
					listDetial.add(lahb.get(i));
				}
			}
		}
		request.setAttribute("servers", listDetial);
		String completed = curPlaybook.get("completed").asText();
		String total = curPlaybook.get("total").asText();
		String status = curPlaybook.get("status").asText();
		Map<String, String> map = new TreeMap<String, String>();
		String progress = completed + " / " + total;
		map.put("progress", progress);
		request.setAttribute("progress", progress);
		String percent = FormatUtil.getPercent(Integer.parseInt(completed), Integer.parseInt(total));
		map.put("percent", percent);
		request.setAttribute("percent", percent);
		map.put("status", status);
		request.setAttribute("status", status);
		String table = "odata/tasks?playbook_uuid=";
		String url = table + uuid;
		ArrayNode tasks = amsRestService.getList(null, null, url);
		
		List<FinalStatusBean> list = new ArrayList<FinalStatusBean>();
		for (JsonNode jn : tasks) 
		{
			FinalStatusBean wasFinalStatus = new FinalStatusBean();
			String name = jn.get("name").asText();
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			String ss = ((JsonNode) tempNode.get(name)).asText();
			wasFinalStatus.setHost(jn.get("host").asText());
			wasFinalStatus.setName(ss);// 变成中文
			wasFinalStatus.setUuid(jn.get("uuid").asText()); //task 唯一id
			switch (Integer.valueOf(jn.get("status").asText())) {
			case 0:
				wasFinalStatus.setStatus("未开始");
				break;
			case 1:
				wasFinalStatus.setStatus("运行中");
				break;
			case 2:
				wasFinalStatus.setStatus("成功");
				break;
			case 3:
				wasFinalStatus.setStatus("失败");
				break;
			}
			list.add(wasFinalStatus);
		}
		
		request.setAttribute("allServerStatus", list);
		logger.info("getwasclusterLogInfo.do::" + list);
		return "instance_wascluster_log_details";
	}
	
}

