package com.ibm.automation.db2ha.controller;

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
import com.ibm.automation.core.bean.DB2HABean;
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
public class DB2HAController {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(DB2HAController.class);
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
	
	@RequestMapping("/toDb2haNextPage.do")
	public String toDb2haNextPage(HttpServletRequest request, HttpSession session) throws Exception{
		ObjectMapper om = new ObjectMapper();
		String platform = request.getParameter("platform");// aix
		String status = request.getParameter("status");// 得到哪个页面的状态
		String serId = request.getParameter("serId");
		String ptype = request.getParameter("ptype");
		
		request.setAttribute("serId", serId);
		request.setAttribute("platform", platform);	
		request.setAttribute("status", status);
		request.setAttribute("ptype", ptype);
		
		if (ptype != null && ptype != "" && ptype.equals("db2ha") && status.equals("makevg")) {
			List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			
			/*ArrayNode allHdisk = om.createArrayNode();
			ArrayNode allHdisk1 = om.createArrayNode();
			for (int i = 0; i < listDetial.size(); i++) {
				String instaddr = listDetial.get(i).getIp();
				ObjectNode hdisks = amsRestService.getList_one(null, null, "odata/hdisks?ip="+instaddr);
				String retData=hdisks.get("hdisks").asText();
				String[] ele=retData.split("\\r\\n");
				for (String info : ele)
				{
					ObjectNode os = om.createObjectNode();
					os.put("hdiskname", info);
					allHdisk1.add(os);
				}				
			}
			request.setAttribute("allHdisk", allHdisk.add(allHdisk1));*/
			
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			
			/*String haname = request.getParameter("haname");
			String ha_primaryNode = request.getParameter("ha_primaryNode");
			String ha_ip1 = request.getParameter("ha_ip1");
			String ha_hostname1 = request.getParameter("ha_hostname1");
			String ha_bootip1 = request.getParameter("ha_bootip1");
			String ha_bootalias1 = request.getParameter("ha_bootalias1");
			String ha_ip2 = request.getParameter("ha_ip2");
			String ha_hostname2 = request.getParameter("ha_hostname2");
			String ha_bootip2 = request.getParameter("ha_bootip2");
			String ha_bootalias2 = request.getParameter("ha_bootalias2");
			String ha_svcip = request.getParameter("ha_svcip");
			String ha_svcalias = request.getParameter("ha_svcalias");
			String ha_netmask = request.getParameter("ha_netmask");*/
			
			request.setAttribute("haname", request.getParameter("haname"));
			request.setAttribute("ha_primaryNode", request.getParameter("ha_primaryNode"));
			request.setAttribute("ha_ip1", request.getParameter("ha_ip1"));
			request.setAttribute("ha_hostname1", request.getParameter("ha_hostname1")); 
			request.setAttribute("ha_bootip1", request.getParameter("ha_bootip1"));
			request.setAttribute("ha_bootalias1", request.getParameter("ha_bootalias1"));
			request.setAttribute("ha_ip2", request.getParameter("ha_ip2"));
			request.setAttribute("ha_hostname2", request.getParameter("ha_hostname2"));
			request.setAttribute("ha_bootip2", request.getParameter("ha_bootip2"));
			request.setAttribute("ha_bootalias2", request.getParameter("ha_bootalias2"));
			request.setAttribute("ha_svcip", request.getParameter("ha_svcip"));
			request.setAttribute("ha_svcalias", request.getParameter("ha_svcalias"));
			request.setAttribute("ha_netmask", request.getParameter("ha_netmask"));
			
			return "instance_db2ha_makevg";
		}
		
		
		if (ptype != null && ptype != "" && ptype.equals("db2ha") && status.equals("config"))
		{
			List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			//String hostname="";
			if (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
				//hostname = bean.getName();
			}
			request.setAttribute("servers", listDetial);	
			//request.setAttribute("hostname", hostname);				
			
			//Host页参数
			request.setAttribute("haname", request.getParameter("haname"));
			request.setAttribute("ha_primaryNode", request.getParameter("ha_primaryNode"));
			request.setAttribute("ha_ip1", request.getParameter("ha_ip1"));
			request.setAttribute("ha_hostname1", request.getParameter("ha_hostname1")); 
			request.setAttribute("ha_bootip1", request.getParameter("ha_bootip1"));
			request.setAttribute("ha_bootalias1", request.getParameter("ha_bootalias1"));
			request.setAttribute("ha_ip2", request.getParameter("ha_ip2"));
			request.setAttribute("ha_hostname2", request.getParameter("ha_hostname2"));
			request.setAttribute("ha_bootip2", request.getParameter("ha_bootip2"));
			request.setAttribute("ha_bootalias2", request.getParameter("ha_bootalias2"));
			request.setAttribute("ha_svcip", request.getParameter("ha_svcip"));
			request.setAttribute("ha_svcalias", request.getParameter("ha_svcalias"));
			request.setAttribute("ha_netmask", request.getParameter("ha_netmask"));
			
			//makevg页参数
			request.setAttribute("ha_RGName", request.getParameter("ha_RGName"));
			request.setAttribute("ha_ASName", request.getParameter("ha_ASName"));
			request.setAttribute("ha_vgdb2inst", request.getParameter("ha_vgdb2inst"));
			request.setAttribute("ha_vgdb2data", request.getParameter("ha_vgdb2data"));
			request.setAttribute("ha_vgdb2log", request.getParameter("ha_vgdb2log"));
			request.setAttribute("ha_vgdb2archlog", request.getParameter("ha_vgdb2archlog"));
			request.setAttribute("db2_inst_pv", request.getParameter("db2_inst_pv"));
			request.setAttribute("db2_data_pv", request.getParameter("db2_data_pv"));
			request.setAttribute("db2_log_pv", request.getParameter("db2_log_pv"));
			request.setAttribute("db2_archlog_pv", request.getParameter("db2_archlog_pv"));
			request.setAttribute("db2_insthome", request.getParameter("db2_insthome"));
			request.setAttribute("ha_caappv", request.getParameter("ha_caappv"));
			request.setAttribute("ha_startpolicy", request.getParameter("ha_startpolicy"));
			request.setAttribute("ha_fopolicy", request.getParameter("ha_fopolicy"));
			request.setAttribute("ha_fbpolicy", request.getParameter("ha_fbpolicy"));
			request.setAttribute("ha_nfsON", request.getParameter("ha_nfsON"));
			
			return "instance_db2ha_config";	
		}
		
		
		
		//String hostname = request.getParameter("hostname");//主机名
		//request.setAttribute("hostname", hostname);
		String[] SserIds = serId.split(",");
		String[] all_hostnames = request.getParameterValues("all_hostnames");// 主机名
		String[] all_ips = request.getParameterValues("all_ips");// IP地址
		
		List<DB2HABean> db2b = new ArrayList<DB2HABean>();
		for (int i = 0; i < all_ips.length; i++) {
			DB2HABean bean = new DB2HABean();
			bean.setIp(all_ips[i]);
			bean.setName(all_hostnames[i]);
			bean.setUuid(SserIds[i]);
			db2b.add(bean);
		}
		
		//Host
		request.setAttribute("haname", request.getParameter("haname"));
		request.setAttribute("ha_primaryNode", request.getParameter("ha_primaryNode"));
		request.setAttribute("ha_ip1", request.getParameter("ha_ip1"));
		request.setAttribute("ha_ip2", request.getParameter("ha_ip2"));
		request.setAttribute("ha_hostname1", request.getParameter("ha_hostname1"));
		request.setAttribute("ha_hostname2", request.getParameter("ha_hostname2"));
		request.setAttribute("ha_bootip1", request.getParameter("ha_bootip1"));
		request.setAttribute("ha_bootip2", request.getParameter("ha_bootip2"));
		request.setAttribute("ha_bootalias1", request.getParameter("ha_bootalias1"));
		request.setAttribute("ha_bootalias2", request.getParameter("ha_bootalias2"));		
		request.setAttribute("ha_svcip", request.getParameter("ha_svcip"));
		request.setAttribute("ha_svcalias", request.getParameter("ha_svcalias"));
		request.setAttribute("ha_netmask", request.getParameter("ha_netmask"));
		
		//makevg
		request.setAttribute("ha_RGName", request.getParameter("ha_RGName"));
		request.setAttribute("ha_ASName", request.getParameter("ha_ASName"));
		request.setAttribute("ha_vgdb2inst", request.getParameter("ha_vgdb2inst"));
		request.setAttribute("ha_vgdb2data", request.getParameter("ha_vgdb2data"));
		request.setAttribute("ha_vgdb2log", request.getParameter("ha_vgdb2log"));
		request.setAttribute("ha_vgdb2archlog", request.getParameter("ha_vgdb2archlog"));
		request.setAttribute("db2_inst_pv", request.getParameter("db2_inst_pv"));
		request.setAttribute("db2_data_pv", request.getParameter("db2_data_pv"));
		request.setAttribute("db2_log_pv", request.getParameter("db2_log_pv"));
		request.setAttribute("db2_archlog_pv", request.getParameter("db2_archlog_pv"));
		request.setAttribute("db2_insthome", request.getParameter("db2_insthome"));
		request.setAttribute("ha_caappv", request.getParameter("ha_caappv"));
		request.setAttribute("ha_startpolicy", request.getParameter("ha_startpolicy"));
		request.setAttribute("ha_fopolicy", request.getParameter("ha_fopolicy"));
		request.setAttribute("ha_fbpolicy", request.getParameter("ha_fbpolicy"));
		request.setAttribute("ha_nfsON", request.getParameter("ha_nfsON"));
				
		// 基本信息
		request.setAttribute("db2fix",request.getParameter("db2fix"));//db2版本补丁的简单形式
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
		
		request.setAttribute("allservers", db2b);
		String totaljson = om.writeValueAsString(db2b);
		request.setAttribute("allservertotaljson", EncodeUtil.encode(totaljson));
		
		if (logger.isDebugEnabled()) {
			logger.debug("调用DB2HAController::toDB2HANextPage 所有的参数信息为:os = " + platform + " status=" + status);
		}
		
		List<ServersBean> listDetial_1 = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial_1);
		if (status.equals("confirm")) {
			return "instance_db2ha_comfirm";
		}
		
		return null;
	}
	
	
	@RequestMapping("/installDb2haInfo.do")
	public String installDb2haInfo(HttpServletRequest request, HttpSession session){
		String platform = request.getParameter("platform");
		ObjectNode outNode = om.createObjectNode();
		//ArrayNode iplistArrNode = om.createArrayNode();
		//ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode final_an = om.createArrayNode();
		JavaType javaType = getCollectionType(ArrayList.class, DB2HABean.class);
		String allsrvs=EncodeUtil.decode(request.getParameter("allservertotaljson"));
		//String hostname= request.getParameter("hostname");//被更改的主机名
		request.getParameter("serId");// 获取server 信息
		
		List<DB2HABean> db2b = null;
		try {
			db2b = (ArrayList<DB2HABean>) om.readValue(allsrvs, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.debug("installDb2haInfo::" + e.getMessage());
		}
		Iterator<DB2HABean> iter = db2b.iterator();
		while (iter.hasNext()) {
			ObjectNode final_o = om.createObjectNode();
			DB2HABean temp = iter.next();
			//iplistArrNode.add(temp.getIp());
			//hostnamelistArrNode.add(temp.getName());
			//outNode.putPOJO("ip_list", iplistArrNode);
			//outNode.putPOJO("hostname_list", hostnamelistArrNode);
			final_o.put("role", 1);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);
		}
		
		//拼装ip_list
		ArrayNode ip_listArrNode = om.createArrayNode();
		ip_listArrNode.add(request.getParameter("ha_ip1"));
		ip_listArrNode.add(request.getParameter("ha_ip2"));
		outNode.putPOJO("ip_list", ip_listArrNode);
		
		//拼装hostname_list
		ArrayNode hostname_listArrNode = om.createArrayNode();
		hostname_listArrNode.add(request.getParameter("ha_hostname1"));
		hostname_listArrNode.add(request.getParameter("ha_hostname2"));
		outNode.putPOJO("hostname_list", hostname_listArrNode);
		
		outNode.put("downloadpath", (String) amsprop.get("downloadpath") + "/db2ha");
		outNode.put("ftp_username", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));		
		
		outNode.put("ha_svcalias", request.getParameter("ha_svcalias"));
		outNode.put("ha_svceip", request.getParameter("ha_svcip"));
		outNode.put("ha_netmask", request.getParameter("ha_netmask"));
		outNode.put("ha_caappv", request.getParameter("ha_caappv"));
		outNode.put("ha_clustername", request.getParameter("haname"));
		outNode.put("ha_startpolicy", request.getParameter("ha_startpolicy"));
		outNode.put("ha_fopolicy", request.getParameter("ha_fopolicy"));
		outNode.put("ha_fbpolicy", request.getParameter("ha_fbpolicy"));
		
		outNode.put("db2_version", request.getParameter("db2_version"));
		outNode.put("db2_binary", request.getParameter("db2_binary"));
		outNode.put("db2_insthome",request.getParameter("db2_insthome"));
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
		
		outNode.put("db2_inst_pv", request.getParameter("db2_inst_pv"));
		outNode.put("db2_data_pv",request.getParameter("db2_data_pv") );
		outNode.put("db2_log_pv",request.getParameter("db2_log_pv") );
		outNode.put("db2_archlog_pv", request.getParameter("db2_archlog_pv"));
		outNode.put("db2_ppsize", request.getParameter("db2_ppsize"));
		
		//json串未用到参数，仅做日志页面显示用
		outNode.put("ha_primaryNode", request.getParameter("ha_primaryNode"));
		outNode.put("ha_ip1", request.getParameter("ha_ip1"));
		outNode.put("ha_hostname1", request.getParameter("ha_hostname1"));
		outNode.put("ha_bootip1", request.getParameter("ha_bootip1"));
		outNode.put("ha_bootalias1", request.getParameter("ha_bootalias1"));
		outNode.put("ha_ip2", request.getParameter("ha_ip2"));
		outNode.put("ha_hostname2", request.getParameter("ha_hostname2"));
		outNode.put("ha_bootip2", request.getParameter("ha_bootip2"));
		outNode.put("ha_bootalias2", request.getParameter("ha_bootalias2"));
		outNode.put("ha_vgdb2inst", request.getParameter("ha_vgdb2inst"));
		outNode.put("ha_vgdb2data", request.getParameter("ha_vgdb2data"));
		outNode.put("ha_vgdb2log", request.getParameter("ha_vgdb2log"));
		outNode.put("ha_vgdb2archlog", request.getParameter("ha_vgdb2archlog"));
		outNode.put("ha_nfsON", request.getParameter("ha_nfsON"));
				
		logger.info("installDB2HAInfo.do::" + outNode.toString());
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText());
		final_on.put("playbook-name", "db2ha");// task-name task 名
		final_on.put("product-name", "db2");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
		final_on.putPOJO("nodes", final_an);
		logger.info("installDB2HAInfo::" + final_on.toString());
		System.out.println(outNode);
		
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installDB2HAInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("installDB2HAInfo.do::" + e.getMessage());
			request.setAttribute("errmsg", e.getMessage());
		}
		return "redirect:/500.jsp";
	}
	
	
	@RequestMapping("/getdb2haLogInfoDetail.do")
	public String getdb2haLogInfoDetail(HttpServletRequest request, HttpSession session){
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "db2ha");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=db2ha");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) 
		{
			trans = (ObjectNode) jn;
		}
		
		if (curPlaybook != null && curPlaybook.get("options") != null) 
		{
			try {
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				//Host信息
				request.setAttribute("haname", options.get("ha_clustername").asText());
				request.setAttribute("ha_primaryNode", options.get("ha_primaryNode").asText());
				request.setAttribute("ha_ip1", options.get("ha_ip1").asText());
				request.setAttribute("ha_hostname1", options.get("ha_hostname1").asText());
				request.setAttribute("ha_bootip1", options.get("ha_bootip1").asText());
				request.setAttribute("ha_bootalias1", options.get("ha_bootalias1").asText());
				request.setAttribute("ha_ip2", options.get("ha_ip2").asText());
				request.setAttribute("ha_hostname2", options.get("ha_hostname2").asText());
				request.setAttribute("ha_bootip2", options.get("ha_bootip2").asText());
				request.setAttribute("ha_bootalias2", options.get("ha_bootalias2").asText());			
				request.setAttribute("ha_svcalias", options.get("ha_svcalias").asText());
				request.setAttribute("ha_svcip", options.get("ha_svceip").asText());
				request.setAttribute("ha_netmask", options.get("ha_netmask").asText());
				//VG信息
				request.setAttribute("ha_vgdb2inst", options.get("ha_vgdb2inst").asText());
				request.setAttribute("ha_vgdb2data", options.get("ha_vgdb2data").asText());
				request.setAttribute("ha_vgdb2log", options.get("ha_vgdb2log").asText());
				request.setAttribute("ha_vgdb2archlog", options.get("ha_vgdb2archlog").asText());
				request.setAttribute("db2_inst_pv", options.get("db2_inst_pv").asText());
				request.setAttribute("db2_data_pv", options.get("db2_data_pv").asText());				
				request.setAttribute("db2_log_pv", options.get("db2_log_pv").asText());
				request.setAttribute("db2_archlog_pv", options.get("db2_archlog_pv").asText());
				request.setAttribute("db2_insthome", options.get("db2_insthome").asText());
				request.setAttribute("ha_nfsON", options.get("ha_nfsON").asText());
				//基本信息
				request.setAttribute("db2_version", options.get("db2_version").asText());
				request.setAttribute("db2_binary", options.get("db2_binary").asText());
				request.setAttribute("db2_db2base", options.get("db2_db2base").asText());
				request.setAttribute("db2_dbpath", options.get("db2_dbpath").asText());
				request.setAttribute("db2_db2insusr", options.get("db2_db2insusr").asText());	
				request.setAttribute("db2_svcename", options.get("db2_svcename").asText());
				request.setAttribute("db2_dbname", options.get("db2_dbname").asText());
				request.setAttribute("db2_codeset", options.get("db2_codeset").asText());
				request.setAttribute("db2_dbdatapath", options.get("db2_dbdatapath").asText());
				//实例高级属性
				request.setAttribute("db2_db2insusr", options.get("db2_db2insusr").asText());
				request.setAttribute("db2_db2insgrp", options.get("db2_db2insgrp").asText());
				request.setAttribute("db2_db2fenusr", options.get("db2_db2fenusr").asText());
				request.setAttribute("db2_db2fengrp", options.get("db2_db2fengrp").asText());
				request.setAttribute("db2_db2comm", options.get("db2_db2comm").asText());
				request.setAttribute("db2_db2codepage", options.get("db2_db2codepage").asText());
				//数据库高级属性
				request.setAttribute("db2_db2log", options.get("db2_db2log").asText());
				request.setAttribute("db2_logarchpath", options.get("db2_logarchpath").asText());
				request.setAttribute("db2_locktimeout", options.get("db2_locktimeout").asText());
				request.setAttribute("db2_logfilesize", options.get("db2_logfilesize").asText());
				request.setAttribute("db2_logprimary", options.get("db2_logprimary").asText());
				request.setAttribute("db2_logsecond", options.get("db2_logsecond").asText());
				request.setAttribute("db2_trackmod", options.get("db2_trackmod").asText());
				request.setAttribute("db2_pagesize", options.get("db2_pagesize").asText());
				request.setAttribute("db2_softmax", options.get("db2_softmax").asText());
				
				String platform="aix";
				request.setAttribute("platform", platform);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		for (JsonNode jn : tasks) {
			FinalStatusBean db2FinalStatus = new FinalStatusBean();
			String name = jn.get("name").asText();	
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			//String chinese = ((JsonNode) tempNode.get(name)).asText();
			db2FinalStatus.setHost(jn.get("host").asText());
			//db2FinalStatus.setName(chinese);// 变成中文
			db2FinalStatus.setUuid(jn.get("uuid").asText()); //task 唯一id
			switch (Integer.valueOf(jn.get("status").asText())) {
			case 0:
				db2FinalStatus.setStatus("未开始");
				break;
			case 1:
				db2FinalStatus.setStatus("运行中");
				break;
			case 2:
				db2FinalStatus.setStatus("成功");
				break;
			case 3:
				db2FinalStatus.setStatus("失败");
				break;
			case 4:
				db2FinalStatus.setStatus("跳过");
			}
			list.add(db2FinalStatus);
		}
		
		request.setAttribute("allServerStatus", list);
		logger.info("getdb2haLogInfo.do::" + list);
		return "instance_db2ha_log_details";
	}
	
}
