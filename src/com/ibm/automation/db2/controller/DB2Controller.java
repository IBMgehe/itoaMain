package com.ibm.automation.db2.controller;

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
import com.ibm.automation.core.bean.DB2Bean;
import com.ibm.automation.core.bean.FinalStatusBean;
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
public class DB2Controller {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(DB2Controller.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	//private List<ServersBean> addHostList;

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	@RequestMapping("/toDb2NextPage.do")
	public String toDb2NextPage(HttpServletRequest request, HttpSession session) throws Exception {
		String platform = request.getParameter("platform");// 判断是AIX 还是linux 平台a
		String status = request.getParameter("status");// 得到哪个页面的状态
	
		String serId = request.getParameter("serId");
		String ptype = request.getParameter("ptype");
		
		request.setAttribute("serId", serId);		
		request.setAttribute("ptype", ptype);
		request.setAttribute("hostId", serId);
		
		request.setAttribute("platform", platform);
		
		if (ptype != null && ptype != "" && ptype.equals("db2") && status.equals("config")) {
			List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			String hostname="";
			if (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
				 hostname = bean.getName();
			}			
			request.setAttribute("servers", listDetial);	
			request.setAttribute("hostname", hostname);	
			
			
			
			
			// PV
			String vgdb2home=request.getParameter("db2homepv");//暂时不用
			String vgdb2log = request.getParameter("db2logpv");
			String vgdb2archlog=request.getParameter("db2archlogpv");
			String vgdataspace=request.getParameter("dataspacepv");
			
			
			request.setAttribute("vgdb2home", request.getParameter("vgdb2home"));
			request.setAttribute("vgdb2log", request.getParameter("vgdb2log"));
			request.setAttribute("vgdb2archlog", request.getParameter("vgdb2archlog"));
			request.setAttribute("vgdataspace", request.getParameter("vgdataspace"));
			
			// VG创建方式
			request.setAttribute("db2homemode", request.getParameter("db2homemode"));
			request.setAttribute("db2logmode", request.getParameter("db2logmode"));
			request.setAttribute("db2archlogmode", request.getParameter("db2archlogmode"));
			request.setAttribute("dataspacemode", request.getParameter("dataspacemode"));
			
			
			request.setAttribute("data_pv", vgdataspace);
			request.setAttribute("log_pv", vgdb2log);
			request.setAttribute("archlog_pv", vgdb2archlog);
			request.setAttribute("db2home_pv", vgdb2home);
			
			return "instance_db2_config";
		}
		String hostname = request.getParameter("hostname");//主机名
		String db2fix = request.getParameter("db2fix");// was 版本补丁的简单形式
		request.setAttribute("hostname", hostname);
		String[] SserIds = serId.split(",");
		String[] all_hostnames = request.getParameterValues("all_hostnames");// 主机名
		String[] all_ips = request.getParameterValues("all_ips");// IP地址
		
		List<DB2Bean> db2b = new ArrayList<DB2Bean>();
		for (int i = 0; i < all_ips.length; i++) {
			DB2Bean bean = new DB2Bean();
			bean.setIp(all_ips[i]);
			bean.setName(all_hostnames[i]);
			bean.setUuid(SserIds[i]);
			db2b.add(bean);
		}
		
		// 将对象转换为String
		// 基本信息
		request.setAttribute("db2fix",db2fix);
		request.setAttribute("db2_version", request.getParameter("db2_version"));
		request.setAttribute("db2_binary", request.getParameter("db2_fixpack"));
		request.setAttribute("db2_db2base", request.getParameter("db2_db2base"));
		
		request.setAttribute("db2_dbpath", request.getParameter("db2_dbpath"));
		request.setAttribute("db2_db2insusr", request.getParameter("db2_db2insusr"));
		request.setAttribute("db2_svcename", request.getParameter("db2_svcename"));
		request.setAttribute("db2_dbname", request.getParameter("db2_dbname"));
		request.setAttribute("db2_codeset", request.getParameter("db2_codeset"));
		request.setAttribute("db2_dbdatapath", request.getParameter("db2_dbdatapath"));

		request.setAttribute("db2_db2insusr1", request.getParameter("db2_db2insusr1"));
		request.setAttribute("db2_db2insgrp", request.getParameter("db2_db2insgrp"));
		request.setAttribute("db2_db2fenusr", request.getParameter("db2_db2fenusr"));
		request.setAttribute("db2_db2fengrp", request.getParameter("db2_db2fengrp"));
		request.setAttribute("db2_db2comm", request.getParameter("db2_db2comm"));
		request.setAttribute("db2_db2codepage", request.getParameter("db2_db2codepage"));
		//request.setAttribute("db2_initagents", request.getParameter("db2_initagents"));
		//request.setAttribute("db2_poolagents", request.getParameter("db2_poolagents"));
		//request.setAttribute("db2_max_coordagents", request.getParameter("db2_max_coordagents"));
		//request.setAttribute("db2_max_connectings", request.getParameter("db2_max_connectings"));
		//request.setAttribute("db2_diagsize", request.getParameter("db2_diagsize"));
		//request.setAttribute("db2_mon_buf", request.getParameter("db2_mon_buf"));
		//request.setAttribute("db2_mon_lock", request.getParameter("db2_mon_lock"));
		//request.setAttribute("db2_mon_sort", request.getParameter("db2_mon_sort"));
		//request.setAttribute("db2_mon_stmt", request.getParameter("db2_mon_stmt"));
		//request.setAttribute("db2_mon_table", request.getParameter("db2_mon_table"));
		//request.setAttribute("db2_mon_uow", request.getParameter("db2_mon_uow"));
		//request.setAttribute("db2_health_mon", request.getParameter("db2_health_mon"));
		//request.setAttribute("db2_mon_heap", request.getParameter("db2_mon_heap"));

		request.setAttribute("db2_db2log", request.getParameter("db2_db2log"));
		request.setAttribute("db2_logarchpath", request.getParameter("db2_logarchpath"));
		request.setAttribute("db2_locktimeout", request.getParameter("db2_locktimeout"));
		request.setAttribute("db2_logfilesize", request.getParameter("db2_logfilesize"));
		request.setAttribute("db2_logprimary", request.getParameter("db2_logprimary"));
		request.setAttribute("db2_logsecond", request.getParameter("db2_logsecond"));
		request.setAttribute("db2_softmax", request.getParameter("db2_softmax"));
		request.setAttribute("db2_trackmod", request.getParameter("db2_trackmod"));
		request.setAttribute("db2_pagesize", request.getParameter("db2_pagesize"));
		request.setAttribute("db2_ppsize", request.getParameter("db2_ppsize"));
		//request.setAttribute("db2_backuppath", request.getParameter("db2_backuppath"));
		//request.setAttribute("db2_stmm", request.getParameter("db2_stmm"));
		//request.setAttribute("db2_locklist", request.getParameter("db2_locklist"));
		//request.setAttribute("db2_maxlocks", request.getParameter("db2_maxlocks"));		
		//request.setAttribute("db2_sortheap", request.getParameter("db2_sortheap"));		
		//request.setAttribute("db2_logbuff", request.getParameter("db2_logbuff"));		

		/*
		request.setAttribute("vgdb2home", request.getParameter("vgdb2home"));
		request.setAttribute("vgdb2log", request.getParameter("vgdb2log"));
		request.setAttribute("vgdb2archlog", request.getParameter("vgdb2archlog"));
		request.setAttribute("vgdataspace", request.getParameter("vgdataspace"));
		
		// VG创建方式
		request.setAttribute("db2homemode", request.getParameter("db2homemode"));
		request.setAttribute("db2logmode", request.getParameter("db2logmode"));
		request.setAttribute("db2archlogmode", request.getParameter("db2archlogmode"));
		request.setAttribute("dataspacemode", request.getParameter("dataspacemode"));
		
		
		
		request.setAttribute("data_pv", request.getParameter("data_pv"));
		request.setAttribute("log_pv", request.getParameter("log_pv"));
		request.setAttribute("archlog_pv", request.getParameter("archlog_pv"));
		request.setAttribute("db2home_pv", request.getParameter("db2home_pv"));
	*/	
		request.setAttribute("allservers", db2b);
		String totaljson = om.writeValueAsString(db2b);
		logger.info("DB2 StandAlone 所有参数为:"+totaljson);
		request.setAttribute("allservertotaljson", EncodeUtil.encode(totaljson));
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("调用DB2Controller::toDB2NextPage 所有的参数信息为:os = " + platform + " status=" + status);
		}

		List<ServersBean> listDetial_1 = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial_1);
		if (status.equals("confirm")) {
			return "instance_db2_comfirm";
		}
		return null;
	}

	@RequestMapping("/installDb2StandAloneInfo.do")
	public String installDb2StandAloneInfo(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		ObjectNode outNode = om.createObjectNode();
		ArrayNode iplistArrNode = om.createArrayNode();
		ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode final_an = om.createArrayNode();
		JavaType javaType = getCollectionType(ArrayList.class, DB2Bean.class);
		String allsrvs=EncodeUtil.decode(request.getParameter("allservertotaljson"));
	//	String allsrvs = (String) request.getSession().getAttribute("allservertotaljson");
		String hostname= request.getParameter("hostname");//被更改的主机名
		//request.getParameter("serId");// 获取server 信息
		
		List<DB2Bean> db2b = null;
		try {
			db2b = (ArrayList<DB2Bean>) om.readValue(allsrvs, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("installDb2Info::" + e.getMessage());
			return "redirect:/500.jsp";
		}
		Iterator<DB2Bean> iter = db2b.iterator();
		while (iter.hasNext()) {
			ObjectNode final_o = om.createObjectNode();
			DB2Bean temp = iter.next();
			iplistArrNode.add(temp.getIp());
			hostnamelistArrNode.add(hostname);
			outNode.putPOJO("ip_list", iplistArrNode);
			outNode.putPOJO("hostname_list", hostnamelistArrNode);
			final_o.put("role", 1);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);
		}
		if(platform.equalsIgnoreCase("aix"))
		{
	/*		outNode.put("vgdb2home", request.getParameter("vgdb2home"));
			outNode.put("vgdb2log", request.getParameter("vgdb2log"));
			outNode.put("vgdb2archlog", request.getParameter("vgdb2archlog"));
			outNode.put("vgdataspace", request.getParameter("vgdataspace"));
			// VG创建方式
			outNode.put("db2homemode", request.getParameter("db2homemode"));
			outNode.put("db2logmode", request.getParameter("db2logmode"));
			outNode.put("db2archlogmode", request.getParameter("db2archlogmode"));
			outNode.put("dataspacemode", request.getParameter("dataspacemode"));
				
			outNode.put("db2_data_pv",request.getParameter("data_pv") );
			outNode.put("db2_log_pv",request.getParameter("log_pv") );
			outNode.put("db2_archlog_pv", request.getParameter("archlog_pv"));
			outNode.put("db2home_pv", request.getParameter("db2home_pv"));
	*/
		}
		outNode.put("downloadpath", (String) amsprop.get("downloadpath") + "/db2");
		outNode.put("ftp_user", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));
		
		outNode.put("db2_version", request.getParameter("db2_version"));
		outNode.put("db2_binary", request.getParameter("db2_binary"));
		outNode.put("db2fix", request.getParameter("db2fix"));//9.7.0.3
		//outNode.put("db2_fixpack", request.getParameter("db2_fixpack"));
		outNode.put("db2_db2base", request.getParameter("db2_db2base"));
		outNode.put("db2_dbpath", request.getParameter("db2_dbpath"));
		outNode.put("db2_dbdatapath", request.getParameter("db2_dbdatapath"));
		outNode.put("db2_db2log", request.getParameter("db2_db2log"));
		outNode.put("db2_logarchpath", request.getParameter("db2_logarchpath"));
		outNode.put("db2_svcename", request.getParameter("db2_svcename"));
		
		outNode.put("db2_db2insusr", request.getParameter("db2_db2insusr"));
		outNode.put("db2_db2insgrp", request.getParameter("db2_db2insgrp"));
		outNode.put("db2_db2fenusr", request.getParameter("db2_db2fenusr"));
		outNode.put("db2_db2fengrp", request.getParameter("db2_db2fengrp"));
		outNode.put("db2_db2comm", request.getParameter("db2_db2comm"));
		outNode.put("db2_db2codepage", request.getParameter("db2_db2codepage"));
		outNode.put("db2_dbname", request.getParameter("db2_dbname"));
		outNode.put("db2_codeset", request.getParameter("db2_codeset"));
		outNode.put("db2_locktimeout", request.getParameter("db2_locktimeout"));
		outNode.put("db2_logfilesize", request.getParameter("db2_logfilesize"));
		outNode.put("db2_logprimary", request.getParameter("db2_logprimary"));
		outNode.put("db2_logsecond", request.getParameter("db2_logsecond"));
		outNode.put("db2_softmax", request.getParameter("db2_softmax"));
		outNode.put("db2_trackmod", request.getParameter("db2_trackmod"));
		outNode.put("db2_pagesize", request.getParameter("db2_pagesize"));		
		outNode.put("db2_ppsize", request.getParameter("db2_ppsize"));
		//outNode.put("db2_db2insusr1", request.getParameter("db2_db2insusr1"));		
		//outNode.put("db2_initagents", request.getParameter("db2_initagents"));
		//outNode.put("db2_poolagents", request.getParameter("db2_poolagents"));
		//outNode.put("db2_max_coordagents", request.getParameter("db2_max_coordagents"));
		//outNode.put("db2_max_connectings", request.getParameter("db2_max_connectings"));
		//outNode.put("db2_diagsize", request.getParameter("db2_diagsize"));
		//outNode.put("db2_mon_buf", request.getParameter("db2_mon_buf"));
		//outNode.put("db2_mon_lock", request.getParameter("db2_mon_lock"));
		//outNode.put("db2_mon_sort", request.getParameter("db2_mon_sort"));
		//outNode.put("db2_mon_stmt", request.getParameter("db2_mon_stmt"));
		//outNode.put("db2_mon_table", request.getParameter("db2_mon_table"));
		//outNode.put("db2_mon_uow", request.getParameter("db2_mon_uow"));
		//outNode.put("db2_health_mon", request.getParameter("db2_health_mon"));
		//outNode.put("db2_mon_heap", request.getParameter("db2_mon_heap"));		
		//outNode.put("db2_backuppath", request.getParameter("db2_backuppath"));
		//outNode.put("db2_stmm", request.getParameter("db2_stmm"));
		//outNode.put("db2_locklist", request.getParameter("db2_locklist"));
		//outNode.put("db2_maxlocks", request.getParameter("db2_maxlocks"));
		//outNode.put("db2_sortheap", request.getParameter("db2_sortheap"));		
		//outNode.put("db2_logbuff", request.getParameter("db2_logbuff"));
		
		logger.info("installDB2StandAloneInfo.do::" + outNode.toString());
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText());
		final_on.put("playbook-name", "db2");// task-name task 名
		final_on.put("product-name", "db2");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
		final_on.putPOJO("nodes", final_an);
		logger.info("installDB2StandAloneInfo::" + final_on.toString());
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installDB2StandAloneInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("installDB2StandAloneInfo.do::" + e.getMessage());
			request.setAttribute("errmsg", e.getMessage());
		}
		return "redirect:/500.jsp";
	}
	
	
	@RequestMapping("/getdb2LogInfoDetail.do")
	public String getdb2LogInfoDetail(HttpServletRequest request, HttpSession session){
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "db2");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=db2");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) 
		{
			trans = (ObjectNode) jn;
		}
		
		if (curPlaybook != null && curPlaybook.get("options") != null) 
		{
			try {
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				request.setAttribute("db2_version", options.get("db2_version").asText());
				//request.setAttribute("db2_fix", options.get("db2fix").asText());
				request.setAttribute("db2_db2base", options.get("db2_db2base").asText());
				request.setAttribute("db2_dbpath", options.get("db2_dbpath").asText());
				request.setAttribute("db2_db2insusr", options.get("db2_db2insusr").asText());	
				request.setAttribute("db2_svcename", options.get("db2_svcename").asText());
				request.setAttribute("db2_dbname", options.get("db2_dbname").asText());
				request.setAttribute("db2_codeset", options.get("db2_codeset").asText());
				request.setAttribute("db2_dbdatapath", options.get("db2_dbdatapath").asText());
				
				request.setAttribute("db2_db2insusr", options.get("db2_db2insusr").asText());
				request.setAttribute("db2_db2insgrp", options.get("db2_db2insgrp").asText());
				request.setAttribute("db2_db2fenusr", options.get("db2_db2fenusr").asText());
				request.setAttribute("db2_db2fengrp", options.get("db2_db2fengrp").asText());
				request.setAttribute("db2_db2comm", options.get("db2_db2comm").asText());
				request.setAttribute("db2_db2codepage", options.get("db2_db2codepage").asText());
				
				request.setAttribute("db2_db2log", options.get("db2_db2log").asText());
				request.setAttribute("db2_logarchpath", options.get("db2_logarchpath").asText());
				request.setAttribute("db2_locktimeout", options.get("db2_locktimeout").asText());
				request.setAttribute("db2_logfilesize", options.get("db2_logfilesize").asText());
				request.setAttribute("db2_logprimary", options.get("db2_logprimary").asText());
				request.setAttribute("db2_logsecond", options.get("db2_logsecond").asText());
				request.setAttribute("db2_trackmod", options.get("db2_trackmod").asText());
				request.setAttribute("db2_pagesize", options.get("db2_pagesize").asText());
				request.setAttribute("db2_softmax", options.get("db2_softmax").asText());
				request.setAttribute("db2fix", options.get("db2fix").asText());
				
				String platform="";
				if(platform.equalsIgnoreCase("aix"))
				{
			/*		request.setAttribute("vgdb2home", options.get("vgdb2home").asText());
					request.setAttribute("vgdb2log", options.get("vgdb2log").asText());
					request.setAttribute("vgdb2archlog", options.get("vgdb2archlog").asText());
					request.setAttribute("vgdataspace", options.get("vgdataspace").asText());
					// VG创建方式
					request.setAttribute("db2homemode", options.get("db2homemode").asText());
					request.setAttribute("db2logmode", options.get("db2logmode").asText());
					request.setAttribute("db2archlogmode", options.get("db2archlogmode").asText());
					request.setAttribute("dataspacemode", options.get("dataspacemode").asText());
						
					request.setAttribute("db2_data_pv",options.get("data_pv").asText() );
					request.setAttribute("db2_log_pv",options.get("log_pv").asText() );
					request.setAttribute("db2_archlog_pv", options.get("archlog_pv").asText());
					request.setAttribute("db2home_pv", options.get("db2home_pv").asText());
			*/
				}
				request.setAttribute("platform", platform);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getdb2LogInfoDetail 获取参数错误::"+e.getMessage());
			}
		}
		List<String> serIds = new ArrayList<String>();
		if (curPlaybook != null && curPlaybook.get("nodes") != null) 
		{
			ArrayNode jn = (ArrayNode) curPlaybook.get("nodes");
			for (JsonNode js : jn) 
			{
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
		String name=null;
		try{
		for (JsonNode jn : tasks) {
			FinalStatusBean mqFinalStatus = new FinalStatusBean();
			 name = jn.get("name").asText();	
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			String chinese = ((JsonNode) tempNode.get(name)).asText();
			mqFinalStatus.setHost(jn.get("host").asText());
			mqFinalStatus.setName(chinese);// 变成中文
			mqFinalStatus.setUuid(jn.get("uuid").asText()); //task 唯一id
			switch (Integer.valueOf(jn.get("status").asText())) {
			case 0:
				mqFinalStatus.setStatus("未开始");
				break;
			case 1:
				mqFinalStatus.setStatus("运行中");
				break;
			case 2:
				mqFinalStatus.setStatus("成功");
				break;
			case 3:
				mqFinalStatus.setStatus("失败");
				break;
			case 4:
				mqFinalStatus.setStatus("跳过");
			}
			list.add(mqFinalStatus);
		}
				
		request.setAttribute("allServerStatus", list);
		logger.info("getdb2LogInfo.do::" + list);
		}catch(NullPointerException e )
		{
			session.setAttribute("errMsg", "请检查"+name+"是否在Mongodb dict 表中定义");
			return "redirect:/500.jsp";
		}
		return "instance_db2_log_details";
	}
	
	

}
