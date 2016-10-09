package com.ibm.automation.was.controller;

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
public class WASController {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private FpService fpService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(WASController.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	private List<ServersBean> addHostList;

	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	@RequestMapping("/toWasNextPage.do")
	public String toWasNextPage(HttpServletRequest request, HttpSession session) throws Exception {
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
			// bean.setProfiletype(all_profile_types[i]);
			// bean.setProfilename(all_profile_names[i]);
			// JSONArray wasnamelist=JSONArray.fromObject(all_profile_names);
			// JSONArray wastypelist=JSONArray.fromObject(all_profile_types);
			bean.setProfilename(all_profile_names[i]);
			bean.setProfiletype(all_profile_types[i]);
			// request.setAttribute("was_profilename_list", wasnamelist);
			// request.setAttribute("was_profiletype_list", wastypelist);
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

		request.setAttribute("was_profile_path", request.getParameter("was_profile_path"));
		request.setAttribute("was_userid", request.getParameter("was_userid"));
		request.setAttribute("was_password", request.getParameter("was_password"));

		/*
		 * request.setAttribute("was_nofile_soft",
		 * request.getParameter("was_nofile_soft"));
		 * request.setAttribute("was_nofile_hard",
		 * request.getParameter("was_nofile_hard"));
		 * request.setAttribute("was_fsize_soft",
		 * request.getParameter("was_fsize_soft"));
		 * request.setAttribute("was_fsize_hard",
		 * request.getParameter("was_fsize_hard"));
		 * request.setAttribute("was_core_soft",
		 * request.getParameter("was_core_soft"));
		 * request.setAttribute("was_core_hard",
		 * request.getParameter("was_core_hard"));
		 */

		request.setAttribute("was_nofile", request.getParameter("was_nofile"));
		request.setAttribute("was_fsize", request.getParameter("was_fsize"));
		request.setAttribute("was_core", request.getParameter("was_core"));
		request.setAttribute("was_nproc", request.getParameter("was_nproc"));

		/*
		 * String wasfsize=request.getParameter("was_fsize"); String wascore =
		 * request.getParameter("was_core"); if (wasfsize.equals("unlimited")) {
		 * request.setAttribute("was_fsize", "-1"); } else
		 * request.setAttribute("was_fsize", request.getAttribute("was_fsize"));
		 * if (wascore.equals("unlimited")) request.setAttribute("was_core",
		 * "-1"); else
		 */

		// 获取was im 版本
		String wasimversion = request.getParameter("wasversion");// 为了获取was im
																	// 版本
		String imout = fpService.getIm(platform, "was", wasimversion);
		logger.info("wasController::获取was im 版本" + imout);
		JSONObject obj = JSONObject.fromObject(imout);
		String wasim =(String) obj.get(wasimversion);
		request.setAttribute("was_im", wasim);

		request.setAttribute("allservers", wcb);
		String totaljson = om.writeValueAsString(wcb);
		request.getSession().setAttribute("allservertotaljson", totaljson);

		if (logger.isDebugEnabled()) {
			logger.debug("调用WASController::toWasNextPage 所有的参数信息为:os = " + platform + " status=" + status);
		}

		List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial);
		addHostList = listDetial;
		if (status.equals("confirm")) {
			return "instance_was_comfirm";
		}
		return null;
	}

	@RequestMapping("/installWasStandAloneInfo.do")
	public String installWasStandAloneInfo(HttpServletRequest request, HttpSession session) {
	//	String platform = request.getParameter("platform");
		ObjectNode outNode = om.createObjectNode();
		ArrayNode iplistArrNode = om.createArrayNode();
		ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode was_profile_listArrNode = om.createArrayNode();
		ArrayNode all_profile_typesArrNode = om.createArrayNode();
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
			logger.debug("installWasInfo::" + e.getMessage());
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
			outNode.putPOJO("was_profiletype_list", all_profile_typesArrNode);
			outNode.putPOJO("was_profilename_list", was_profile_listArrNode);
			// outNode.putPOJO("was_profile_list" , was_profile_listArrNode);
			// outNode.putPOJO("all_profile_types" , all_profile_typesArrNode);
			// outNode.putPOJO("user_list", user_listArrNode);
			// outNode.putPOJO("password_list", password_listArrNode);
			final_o.put("role", 1);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);
		}

		outNode.put("was_binary", request.getParameter("was_binary"));
		outNode.put("was_fp", request.getParameter("was_fp"));

		outNode.put("downloadpath", (String) amsprop.get("downloadpath") + "/was");
		outNode.put("ftp_user", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));

		// outNode.put("was_rpm", "WASSeries*.rpm");
		outNode.put("wasfix", request.getParameter("wasfix"));  //用于历史页面显示简单补丁格式
		outNode.put("was_user", request.getParameter("was_user"));
		outNode.put("was_version", request.getParameter("was_version"));
		outNode.put("was_inst_path", request.getParameter("was_inst_path"));
		outNode.put("was_im_path", request.getParameter("was_im_path"));
		// outNode.put("was_jdk7", "yes");
		outNode.put("was_im", request.getParameter("was_im"));

		/*
		 * String[] was_profiletype_list=
		 * request.getParameterValues("was_profiletype_list");
		 * outNode.putPOJO("was_profiletype_list",was_profiletype_list);
		 * String[] was_profilename_list =
		 * request.getParameterValues("was_profilename_list");
		 * outNode.putPOJO("was_profilename_list",was_profilename_list);
		 */
		outNode.put("was_profile_path", request.getParameter("was_profile_path"));
		outNode.put("was_userid", request.getParameter("was_userid"));
		outNode.put("was_password", request.getParameter("was_password"));

		/*
		 * outNode.put("was_nofile_soft",
		 * Integer.parseInt(request.getParameter("was_nofile_soft")));
		 * outNode.put("was_nofile_hard",
		 * Integer.parseInt(request.getParameter("was_nofile_hard")));
		 * outNode.put("was_fsize_soft",
		 * request.getParameter("was_fsize_soft"));
		 * outNode.put("was_fsize_hard",
		 * request.getParameter("was_fsize_hard")); outNode.put("was_core_soft",
		 * request.getParameter("was_core_soft")); outNode.put("was_core_hard",
		 * request.getParameter("was_core_hard"));
		 */
		ObjectNode sysParam = om.createObjectNode();

		String fsize = request.getParameter("was_fsize");
		String core = request.getParameter("was_core");
		sysParam.put("nproc", request.getParameter("was_nproc"));
		sysParam.put("nofile", (String) request.getParameter("was_nofile"));

		if (fsize.equals("unlimited"))
			sysParam.put("fsize", "-1");
		else
			sysParam.put("fsize", (String) request.getParameter("was_fsize"));
		if (core.equals("unlimited"))
			sysParam.put("core", "-1");
		else
			sysParam.put("core", (String) request.getParameter("was_core"));

		outNode.putPOJO("os_para_ulimit", sysParam);
		outNode.put("softpath", amsprop.getProperty("softpath") + "was");
		System.out.println(outNode);
		logger.info("installWasInfo.do::" + outNode.toString());

		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText());
		// 唯一索引
		final_on.put("playbook-name", "was");// task-name task 名
		final_on.put("product-name", "was");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
		final_on.putPOJO("nodes", final_an);
		logger.info("installWasInfo::" + final_on.toString());
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installWasInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("installWasInfo.do::" + e.getMessage());
		}
		return "redirect:/500.jsp";
	}

	@RequestMapping("/getwasLogInfoDetail.do")
	public String getwasLogInfoDetail(HttpServletRequest request, HttpSession session) {
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "was");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=was");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) {
			trans = (ObjectNode) jn;
		}
		if (curPlaybook != null && curPlaybook.get("options") != null) {
			try {
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				request.setAttribute("was_user", options.get("was_user").asText());
				request.setAttribute("was_fix", options.get("wasfix").asText());//8.5.5.5 
				request.setAttribute("was_version", options.get("was_version").asText());
				request.setAttribute("was_inst_path", options.get("was_inst_path").asText());

				request.setAttribute("was_im_path", options.get("was_im_path").asText());
				request.setAttribute("was_userid", options.get("was_userid").asText());
				request.setAttribute("was_password", options.get("was_password").asText());
				request.setAttribute("was_inst_path", options.get("was_inst_path").asText());
				request.setAttribute("was_profile_path", options.get("was_profile_path").asText());
				
				
				JsonNode profiletype = options.get("was_profiletype_list");
				JsonNode profilename = options.get("was_profilename_list");
				JsonNode iplist = options.get("ip_list");
		//		iplist.
				request.setAttribute("was_ip", iplist.get(0).asText());
				request.setAttribute("was_profile_type", profiletype.get(0).asText());
				request.setAttribute("was_profile_name",profilename.get(0).asText());
				JsonNode sys_param = options.get("os_para_ulimit");
				request.setAttribute("was_nproc", sys_param.get("nproc").asText());
				request.setAttribute("was_nofile", sys_param.get("nofile").asText());
				request.setAttribute("was_fsize", sys_param.get("fsize").asText());
				request.setAttribute("was_core", sys_param.get("core").asText());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getwasLogInfoDetail::"+e.getMessage());
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

		String name =null;
		List<FinalStatusBean> list = new ArrayList<FinalStatusBean>();
		try{
		for (JsonNode jn : tasks) {
			FinalStatusBean mqFinalStatus = new FinalStatusBean();
			 name= jn.get("name").asText();
			
		
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			String chinese = ((JsonNode) tempNode.get(name)).asText();
			mqFinalStatus.setHost(jn.get("host").asText());
			mqFinalStatus.setName(chinese);// 变成中文
			mqFinalStatus.setUuid(jn.get("uuid").asText()); //task 唯一id
	//		mqFinalStatus.setName(name);
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
		logger.info("getwasLogInfo.do::" + list);
		}catch(NullPointerException e )
		{
			session.setAttribute("errMsg", "请检查"+name+"是否在Mongodb dict 表中定义");
			return "redirect:/500.jsp";
		}
		return "instance_was_log_details";
	}
	
}
