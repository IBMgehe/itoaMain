package com.ibm.automation.itmos.controller;

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
import com.ibm.automation.core.bean.ItmOSBean;
import com.ibm.automation.core.bean.ServersBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.EncodeUtil;
import com.ibm.automation.core.util.FormatUtil;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.PropertyUtil;
import com.ibm.automation.core.util.ServerUtil;

@Controller
public class ITMOSController {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(ITMOSController.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
//	private List<ServersBean> addHostList;
	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	@RequestMapping("/toItmOsNextPage.do")
	public String toItmOsNextPage(HttpServletRequest request, HttpSession session) throws Exception{
		String ha_tems = request.getParameter("itm_ha_tems");
		String platform = request.getParameter("platform");// 判断是AIX 还是linux 平台
		String status = request.getParameter("status");// 得到哪个页面的状态
		String itmosfix = request.getParameter("itmosfix");// was 版本补丁的简单形式
		String serId = request.getParameter("serId");
		
		request.setAttribute("serId", serId);
		request.setAttribute("platform", platform);
		
		String[] SserIds = serId.split(",");
		String[] all_hostnames = request.getParameterValues("all_hostnames");// 主机名
		String[] all_ips = request.getParameterValues("all_ips");// IP地址
		String[] all_teps_names = request.getParameterValues("all_teps_names");// profile名称
		
		List<ItmOSBean> osb = new ArrayList<ItmOSBean>();
		for (int i = 0; i < all_ips.length; i++) {
			ItmOSBean bean = new ItmOSBean();
			bean.setIp(all_ips[i]);
			bean.setName(all_hostnames[i]);
			bean.setTepsname(all_teps_names[i]);
			bean.setUuid(SserIds[i]);
			osb.add(bean);
		}
		request.setAttribute("itm_fix", itmosfix);
		request.setAttribute("itm_type", request.getParameter("itm_type"));
		request.setAttribute("itm_fp", request.getParameter("itm_fp"));
		request.setAttribute("itm_version", request.getParameter("itmosversion"));
		request.setAttribute("itm_binary", request.getParameter("itm_version"));
		request.setAttribute("itm_inst_path", request.getParameter("itm_inst_path"));
		request.setAttribute("itm_code", request.getParameter("itm_code"));
		request.setAttribute("itm_manager_user", request.getParameter("itm_manager_user"));
		request.setAttribute("itm_manager_group", request.getParameter("itm_manager_group"));
		request.setAttribute("itm_ha_tems", request.getParameter("itm_ha_tems"));
		request.setAttribute("pri_tems", request.getParameter("pri_tems"));
		request.setAttribute("pri_protocol", request.getParameter("pri_protocol"));
		request.setAttribute("pri_port", request.getParameter("pri_port"));
		if (ha_tems.equals("yes")){
			request.setAttribute("sec_tems", request.getParameter("sec_tems"));
			request.setAttribute("sec_protocol", request.getParameter("sec_protocol"));
			request.setAttribute("sec_port", request.getParameter("sec_port"));
		}else if (ha_tems.equals("no"))
		{
			request.setAttribute("sec_tems", "");
			request.setAttribute("sec_protocol", "");
			request.setAttribute("sec_port", "");
		}
		request.setAttribute("all_hostnames", all_hostnames);
		request.setAttribute("all_ips", all_ips);
		request.setAttribute("all_tepsnames", all_teps_names);
		String totaljson = om.writeValueAsString(osb);
		request.setAttribute("allservertotaljson", EncodeUtil.encode(totaljson));
		List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial);
	//	addHostList = listDetial;
		if (status.equals("confirm")) {
			return "instance_itmos_comfirm";
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/installItmOsInfo.do")
	public String installItmOsInfo(HttpServletRequest request, HttpSession session){
		String platform = request.getParameter("platform");
		String ha_tems = request.getParameter("itm_ha_tems");
		ObjectNode outNode = om.createObjectNode();
		ArrayNode iplistArrNode = om.createArrayNode();
		ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode all_tepsname_listArrNode = om.createArrayNode();
		ArrayNode final_an = om.createArrayNode();
		JavaType javaType = getCollectionType(ArrayList.class, ItmOSBean.class);
		//String allsrvs = (String) request.getSession().getAttribute("allservertotaljson");
		String allsrvs=EncodeUtil.decode(request.getParameter("allservertotaljson"));
		List<ItmOSBean> osb = null;		
		try {
			osb = (ArrayList<ItmOSBean>) om.readValue(allsrvs, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("installItmOsInfo::" + e.getMessage());
		}
		Iterator<ItmOSBean> iter = osb.iterator();
		while (iter.hasNext()) {
			ObjectNode final_o = om.createObjectNode();
			ItmOSBean temp = iter.next();
			iplistArrNode.add(temp.getIp());
			hostnamelistArrNode.add(temp.getName());
			all_tepsname_listArrNode.add(temp.getTepsname());
			outNode.putPOJO("hostname_list", hostnamelistArrNode);
			outNode.putPOJO("ip_list", iplistArrNode);
			final_o.put("role", 1);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);
		}
		outNode.put("ftp_user", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));
		outNode.put("itm_type", request.getParameter("itm_type"));
		outNode.put("itm_code", request.getParameter("itm_code"));
		outNode.put("itm_version", request.getParameter("itm_version"));//版本
		outNode.put("itm_fp", request.getParameter("itm_fp"));//补丁
		outNode.put("downloadpath", (String) amsprop.get("downloadpath") + "/itmos");
	//	outNode.put("softpath", "/opt/playbook/media");                  //source_path
		outNode.put("itm_binary", request.getParameter("itm_binary"));  //source_file
		outNode.put("itm_fix", request.getParameter("itm_fix"));                                      //source_fix
	//	outNode.put("itm_silent_install", "silent_install.txt");                //silent_install
	//	outNode.put("itm_silent_config", "silent_config.txt");                  //silent_config
		                             
		outNode.put("itm_inst_path", request.getParameter("itm_inst_path"));
		outNode.put("softpath", amsprop.getProperty("softpath") + "itm/os/");
		outNode.put("itm_manager_user", request.getParameter("itm_manager_user"));
		outNode.put("itm_manager_group", request.getParameter("itm_manager_group"));  
		
		outNode.put("itm_ha_tems", ha_tems);
		outNode.put("itm_primary_hostname", request.getParameter("pri_tems"));
		outNode.put("itm_primary_protocol", request.getParameter("pri_protocol"));
		outNode.put("itm_primary_port", request.getParameter("pri_port"));
		outNode.put("itm_secondary_hostname", request.getParameter("sec_tems"));
		outNode.put("itm_secondary_protocol", request.getParameter("sec_protocol"));
		outNode.put("itm_secondary_port", request.getParameter("sec_port"));
		outNode.put("allservertotaljson",request.getParameter("allservertotaljson"));
		System.out.println(outNode);
		logger.info("installItmOsInfo.do::" + outNode.toString());
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid 
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText());
		
		// 唯一索引
		final_on.put("playbook-name", "itm-os");// task-name task 名
		final_on.put("product-name", "itm-os");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
	
		final_on.putPOJO("nodes", final_an);
		logger.info("installItmOsInfo::" + final_on.toString());
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installItmOsInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("installItmOsInfo.do::" + e.getMessage());
			request.setAttribute("errmsg", e.getMessage());
		}		
		return "redirect:/500.jsp";
	}
	
	
	@RequestMapping("/getitmosLogInfoDetail.do")
	public String getitmosLogInfoDetail(HttpServletRequest request, HttpSession session){
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "itm-os");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=itm-os");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) {
			trans = (ObjectNode) jn;
		}
		
		if (curPlaybook != null && curPlaybook.get("options") != null)
		{
			try{
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				request.setAttribute("itm_type", options.get("itm_type").asText());
				request.setAttribute("itm_version", options.get("itm_version").asText());
				request.setAttribute("itm_code", options.get("itm_code").asText());
				request.setAttribute("itm_fp", options.get("itm_fp").asText());
				request.setAttribute("itm_fix", options.get("itm_fix").asText());
				request.setAttribute("itm_inst_path", options.get("itm_inst_path").asText());
				request.setAttribute("itm_manager_user", options.get("itm_manager_user").asText());
				request.setAttribute("itm_manager_group", options.get("itm_manager_group").asText());
				
				request.setAttribute("itm_ha_tems", options.get("itm_ha_tems").asText());
				request.setAttribute("itm_primary_hostname", options.get("itm_primary_hostname").asText());
				request.setAttribute("itm_primary_protocol", options.get("itm_primary_protocol").asText());
				request.setAttribute("itm_primary_port", options.get("itm_primary_port").asText());
				
				
				
				if (options.get("itm_ha_tems").asText().equalsIgnoreCase("yes"))
				{
					request.setAttribute("itm_secondary_hostname", options.get("itm_secondary_hostname").asText());
					request.setAttribute("itm_secondary_protocol", options.get("itm_secondary_protocol").asText());
					request.setAttribute("itm_secondary_port", options.get("itm_secondary_port").asText());
				}
				JavaType javaType = getCollectionType(ArrayList.class, ItmOSBean.class);
				String allservertotaljson = EncodeUtil.decode(options.get("allservertotaljson").asText());//取出encode的server信息
				List<ItmOSBean> osb = null;		
				try {
					osb = (ArrayList<ItmOSBean>) om.readValue(allservertotaljson, javaType);
					request.setAttribute("allosbservers", osb);
				}catch(NullPointerException e1)
				{
					
				}catch (IOException e) {
					logger.debug("installItmOsInfo::" + e.getMessage());
				}
			} catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		List<String> serIds = new ArrayList<String>();
		if (curPlaybook != null && curPlaybook.get("nodes") != null) {
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
		for (JsonNode jn : tasks) {
			FinalStatusBean osFinalStatus = new FinalStatusBean();
			String name = jn.get("name").asText();		
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			
			String chinese = ((JsonNode) tempNode.get(name)).asText();
			osFinalStatus.setHost(jn.get("host").asText());
			osFinalStatus.setName(chinese);// 变成中文
			osFinalStatus.setUuid(jn.get("uuid").asText()); //task 唯一id
			switch (Integer.valueOf(jn.get("status").asText())) {
			case 0:
				osFinalStatus.setStatus("未开始");
				break;
			case 1:
				osFinalStatus.setStatus("运行中");
				break;
			case 2:
				osFinalStatus.setStatus("成功");
				break;
			case 3:
				osFinalStatus.setStatus("失败");
				break;
			case 4:
				osFinalStatus.setStatus("跳过");
			}
			list.add(osFinalStatus);
		}
		
		request.setAttribute("allServerStatus", list);
		logger.info("getitmosLogInfo.do::" + list);
		return "instance_itmos_log_details";
	}
	
	
	
	
	
}
