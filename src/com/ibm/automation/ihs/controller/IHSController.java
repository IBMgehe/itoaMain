package com.ibm.automation.ihs.controller;

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
import com.ibm.automation.core.bean.IhsBean;
import com.ibm.automation.core.bean.ServersBean;
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
public class IHSController {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private FpService fpService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(IHSController.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	private List<ServersBean> addHostList;
	
	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	
	@RequestMapping("/toIhsNextPage.do")
	public String toIhsNextPage(HttpServletRequest request, HttpSession session) throws Exception{
		String platform = request.getParameter("platform");// 判断是AIX 还是linux 平台
		String status = request.getParameter("status");// 得到哪个页面的状态
		String ihsfix = request.getParameter("ihsfix");// ihs 版本补丁的简单形式
		String serId = request.getParameter("serId");
		
		request.setAttribute("serId", serId);
		request.setAttribute("platform", platform);
		
		String[] SserIds = serId.split(",");
		String[] all_hostnames = request.getParameterValues("all_hostnames");// 主机名
		String[] all_ips = request.getParameterValues("all_ips");// IP地址
		
		List<IhsBean> ihsb = new ArrayList<IhsBean>();
		for (int i = 0; i < all_ips.length; i++) {
			IhsBean bean = new IhsBean();
			bean.setIp(all_ips[i]);
			bean.setName(all_hostnames[i]);
			bean.setUuid(SserIds[i]);
			ihsb.add(bean);
		}
		
		request.setAttribute("ihsfix", ihsfix);
		request.setAttribute("ihs_binary", request.getParameter("ihs_version"));
		request.setAttribute("all_hostnames", all_hostnames);
		request.setAttribute("all_ips", all_ips);
		request.setAttribute("ihs_user", request.getParameter("ihs_user"));
		request.setAttribute("ihs_fp", request.getParameter("ihs_fp"));
		request.setAttribute("ihs_version", request.getParameter("ihsversion"));
		request.setAttribute("ihs_inst_path", request.getParameter("ihs_inst_path"));
		request.setAttribute("ihs_im_path", request.getParameter("ihs_im_path"));
		request.setAttribute("ihs_plu_path", request.getParameter("ihs_plu_path"));
		
		request.setAttribute("ihs_nofile", request.getParameter("ihs_nofile"));
		request.setAttribute("ihs_fsize", request.getParameter("ihs_fsize"));
		request.setAttribute("ihs_core", request.getParameter("ihs_core"));
		request.setAttribute("ihs_nproc", request.getParameter("ihs_nproc"));
		
		// 获取ihs im 版本
		String ihsimversion = request.getParameter("ihsversion");// 为了获取ihs im版本
		String imout = fpService.getIm(platform, "ihs", ihsimversion);
		logger.info("ihsController::获取ihs im 版本" + imout);
		JSONObject obj = JSONObject.fromObject(imout);
		String ihsim = (String) obj.get(ihsimversion);
		request.setAttribute("ihs_im", ihsim);//{"8.5":"agent"}
				
		request.setAttribute("allservers", ihsb);
		String totaljson = om.writeValueAsString(ihsb);
		request.getSession().setAttribute("allservertotaljson", totaljson);
		
		if (logger.isDebugEnabled()) {
			logger.debug("调用IHSController::toIhsNextPage 所有的参数信息为:os = " + platform + " status=" + status);
		}
		
		List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial);
		addHostList = listDetial;
		if (status.equals("confirm")) {
			return "instance_ihs_comfirm";
		}
		return null;
	}
	
	
	@RequestMapping("/installIhs.do")
	public String installIhs(HttpServletRequest request, HttpSession session){
		String platform = request.getParameter("platform");
		ObjectNode outNode = om.createObjectNode();
		ArrayNode iplistArrNode = om.createArrayNode();
		ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode user_listArrNode = om.createArrayNode();
		ArrayNode password_listArrNode = om.createArrayNode();
		ArrayNode final_an = om.createArrayNode();
		JavaType javaType = getCollectionType(ArrayList.class, IhsBean.class);
		String allsrvs = (String) request.getSession().getAttribute("allservertotaljson");
		
		List<IhsBean> ihsb = null;
		try {
			ihsb = (ArrayList<IhsBean>) om.readValue(allsrvs, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.debug("installIhsInfo::" + e.getMessage());
		}
		
		Iterator<IhsBean> iter = ihsb.iterator();
		Iterator<ServersBean> ahbiter = addHostList.iterator();
		while (iter.hasNext()) {
			ObjectNode final_o = om.createObjectNode();
			IhsBean temp = iter.next();
			ServersBean ahb = ahbiter.next();
			iplistArrNode.add(temp.getIp());
			hostnamelistArrNode.add(temp.getName());
			user_listArrNode.add(ahb.getUserid());
			password_listArrNode.add(ahb.getPassword());
			outNode.putPOJO("ip_list", iplistArrNode);
			outNode.putPOJO("hostname_list", hostnamelistArrNode);
			final_o.put("role", 1);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);
		}
		
		outNode.put("softpath", amsprop.getProperty("softpath") + "was");
		outNode.put("downloadpath", (String) amsprop.get("downloadpath") + "/ihs");
		outNode.put("ftp_user", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));
		
		ObjectNode sysParam = om.createObjectNode();
		String fsize = request.getParameter("ihs_fsize");
		String core = request.getParameter("ihs_core");
		sysParam.put("nofile", (String) request.getParameter("ihs_nofile"));
		sysParam.put("nproc", (String) request.getParameter("ihs_nproc"));
		if (fsize.equals("unlimited"))
			sysParam.put("fsize", "-1");
		else
			sysParam.put("fsize", (String) request.getParameter("ihs_fsize"));
		if (core.equals("unlimited"))
			sysParam.put("core", "-1");
		else
			sysParam.put("core", (String) request.getParameter("ihs_core"));
		outNode.putPOJO("os_para_ulimit", sysParam);
		
		outNode.put("ihs_version", request.getParameter("ihs_version"));
		outNode.put("ihsfix", request.getParameter("ihsfix"));  //用于历史页面显示简单补丁格式
	//	outNode.put("ihs_version", "8.5");
		outNode.put("ihs_user", request.getParameter("ihs_user"));
		outNode.put("ihs_im", request.getParameter("ihs_im"));
		//outNode.put("ihs_im", "agent.installer.linux.gtk.x86_64_1.8.3000.20150606_0047.zip");
		outNode.put("ihs_binary", request.getParameter("ihs_binary"));
		//outNode.put("ihs_binary", "WAS_V8.5.5_SUPPL*zip");
		outNode.put("ihs_fp", request.getParameter("ihs_fp"));
		//outNode.put("ihs_fp", "8.5.5-WS-WASSupplements-FP0000005*zip");
		outNode.put("ihs_im_path", request.getParameter("ihs_im_path"));
		outNode.put("ihs_inst_path", request.getParameter("ihs_inst_path"));
		outNode.put("ihs_plg_path", request.getParameter("ihs_plu_path"));
		
		logger.info("installIhsInfo.do::" + outNode.toString());
		
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText());
		System.out.println(outNode);
		
		// 唯一索引
		final_on.put("playbook-name", "ihs");// task-name task 名
		final_on.put("product-name", "ihs");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
		final_on.putPOJO("nodes", final_an);
		logger.info("installIhsInfo::" + final_on.toString());
		String response = "";  
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installIhsInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("installIhsInfo.do::" + e.getMessage());
		}
		return "redirect:/500.jsp";
	}
	
	
	@RequestMapping("/getihsLogInfoDetail.do")
	public String getihsLogInfoDetail(HttpServletRequest request, HttpSession session){
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "ihs");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=ihs");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) {
			trans = (ObjectNode) jn;
		}
		
		if (curPlaybook != null && curPlaybook.get("options") != null){
			try{
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				request.setAttribute("ihs_fix", options.get("ihsfix").asText());
				request.setAttribute("ihs_version", options.get("ihs_version").asText());
				
				request.setAttribute("ihs_user", options.get("ihs_user").asText());
				request.setAttribute("ihs_im_path", options.get("ihs_im_path").asText());
				request.setAttribute("ihs_inst_path", options.get("ihs_inst_path").asText());
				request.setAttribute("ihs_plg_path", options.get("ihs_plg_path").asText());
				
				//iplist
				JsonNode iplist = options.get("ip_list");
				request.setAttribute("was_ip", iplist.get(0).asText());
				
				JsonNode sys_param = options.get("os_para_ulimit");
				request.setAttribute("ihs_nproc", sys_param.get("nproc").asText());
				request.setAttribute("ihs_nofile", sys_param.get("nofile").asText());
				request.setAttribute("ihs_fsize", sys_param.get("fsize").asText());
				request.setAttribute("ihs_core", sys_param.get("core").asText());
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
		String name = null;
		try{
		for(JsonNode jn : tasks){
			FinalStatusBean ihsFinalStatus = new FinalStatusBean();
			 name = jn.get("name").asText();
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			String chinese = ((JsonNode) tempNode.get(name)).asText();
			ihsFinalStatus.setHost(jn.get("host").asText());
			ihsFinalStatus.setName(chinese);// 变成中文
			ihsFinalStatus.setUuid(jn.get("uuid").asText()); //task 唯一id
			switch (Integer.valueOf(jn.get("status").asText())){
			case 0:
				ihsFinalStatus.setStatus("未开始");
				break;
			case 1:
				ihsFinalStatus.setStatus("运行中");
				break;
			case 2:
				ihsFinalStatus.setStatus("成功");
				break;
			case 3:
				ihsFinalStatus.setStatus("失败");
				break;
			case 4:
				ihsFinalStatus.setStatus("跳过");	
			}
			list.add(ihsFinalStatus);
		}
		
		request.setAttribute("allServerStatus", list);
		logger.info("getihsLogInfo.do::" + list);
		}catch(NullPointerException e)
		{
			session.setAttribute("errMsg", "请检查"+name+"是否在Mongodb dict 表中定义");
			return "redirect:/500.jsp";
		}
		return "instance_ihs_log_details";
	}
	
	
}
