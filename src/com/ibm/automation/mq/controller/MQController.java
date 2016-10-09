package com.ibm.automation.mq.controller;

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
import com.ibm.automation.core.bean.ClusterBean;
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
public class MQController {
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(MQController.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	private List<ServersBean> addHostList;

	public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	@RequestMapping("/toMqNextPage.do")
	public String toMqNextPage(HttpServletRequest request, HttpSession session) throws Exception {
		String qmgr_method = request.getParameter("qmgr_method");// 判断是否需要创建
		String platform = request.getParameter("platform");// 判断是AIX 还是linux 平台
		String status = request.getParameter("status");// 得到哪个页面的状态
		String mqfix = request.getParameter("mqfix");// mq 版本补丁的简单形式
		String serId = request.getParameter("serId");
		request.setAttribute("serId", serId);
		String[] SserIds = serId.split(",");
		String[] all_hostnames = request.getParameterValues("all_hostnames");// 所有主机名
		String[] all_ips = request.getParameterValues("all_ips");// 所有ip
		String[] all_qmgr_names = request.getParameterValues("all_qmgr_names");// 所有qmgr名
		// String[] all_complete_saves =
		// request.getParameterValues("all_complete_saves");// 所有是否完全存储库
		List<ClusterBean> lcb = new ArrayList<ClusterBean>();
		for (int i = 0; i < all_ips.length; i++) {
			ClusterBean bean = new ClusterBean();
			bean.setIp(all_ips[i]);
			bean.setName(all_hostnames[i]);
			// bean.setCompletesave(all_complete_saves[i]);
			bean.setQmgrname(all_qmgr_names[i]);
			bean.setUuid(SserIds[i]);
			lcb.add(bean);
		}
		if (qmgr_method.equals("yes")) {
			request.setAttribute("all_hostnames", all_hostnames);
			request.setAttribute("all_ips", all_ips);
			request.setAttribute("all_qmgrnames", all_qmgr_names);
			// request.setAttribute("all_completesaves", all_complete_saves);
			request.setAttribute("mq_cluster", request.getParameter("mq_cluster"));
			request.setAttribute("mq_lstr_port", request.getParameter("mq_lstr_port"));
			request.setAttribute("mq_qmgr_plog", request.getParameter("mq_qmgr_plog"));
			request.setAttribute("mq_qmgr_slog", request.getParameter("mq_qmgr_slog"));
			request.setAttribute("mq_log_psize", request.getParameter("mq_log_psize"));
			request.setAttribute("mq_chl_max", request.getParameter("mq_chl_max"));
			request.setAttribute("mq_chl_kalive", request.getParameter("mq_chl_kalive"));
			request.setAttribute("mq_data_path", request.getParameter("mq_data_path"));
			request.setAttribute("mq_log_path", request.getParameter("mq_log_path"));
			
			
		}
		request.setAttribute("allservers", lcb);
		String totaljson = om.writeValueAsString(lcb);
		request.getSession().setAttribute("allservertotaljson", totaljson);
		// 将对象转换为String

		request.setAttribute("mqfix", mqfix);
		request.setAttribute("mq_version", request.getParameter("mqversion"));// 版本8.0
																				// mq_version//介质名
		request.setAttribute("mqtotalFileName", request.getParameter("mq_version"));// mq安装文件全路径
		request.setAttribute("mq_fp", request.getParameter("mq_fp"));
		request.setAttribute("mq_inst_path", request.getParameter("mq_inst_path"));
		request.setAttribute("mq_user", request.getParameter("mq_user"));
		request.setAttribute("qmgr_method", request.getParameter("qmgr_method"));
		if (platform.equals("aix")) {
			request.setAttribute("aix_maxuproc", request.getParameter("aix_maxuproc"));
			request.setAttribute("aix_nofiles", request.getParameter("aix_nofiles"));
			request.setAttribute("aix_data", request.getParameter("aix_data"));
			request.setAttribute("aix_stack", request.getParameter("aix_stack"));
			request.setAttribute("platform", "aix");
		} else if (platform.equals("linux")) {
			request.setAttribute("lin_semmsl", request.getParameter("lin_semmsl"));
			request.setAttribute("lin_semmns", request.getParameter("lin_semmns"));
			request.setAttribute("lin_semopm", request.getParameter("lin_semopm"));
			request.setAttribute("lin_semmni", request.getParameter("lin_semmni"));
			request.setAttribute("lin_shmax", request.getParameter("lin_shmax"));
			request.setAttribute("lin_shmni", request.getParameter("lin_shmni"));
			request.setAttribute("lin_shmall", request.getParameter("lin_shmall"));
			request.setAttribute("lin_filemax", request.getParameter("lin_filemax"));
			request.setAttribute("lin_nofile", request.getParameter("lin_nofile"));
			request.setAttribute("lin_nproc", request.getParameter("lin_nproc"));
			request.setAttribute("platform", "linux");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("调用MQController::toMqNextPage 所有的参数信息为:os = " + platform + " status=" + status);
		}

		List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);
		request.setAttribute("servers", listDetial);
		addHostList = listDetial;
		if (status.equals("confirm")) {
			return "instance_mq_comfirm";
		}
		return null;
	}

	@RequestMapping("/installMqInfo.do")
	public String installMqInfo(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		String qmgr_method = request.getParameter("qmgr_method");
		ObjectNode outNode = om.createObjectNode();
		ArrayNode iplistArrNode = om.createArrayNode();
		ArrayNode hostnamelistArrNode = om.createArrayNode();
		ArrayNode mq_qmgr_listArrNode = om.createArrayNode();
		ArrayNode user_listArrNode = om.createArrayNode();
		ArrayNode password_listArrNode = om.createArrayNode();
		// ArrayNode all_complete_savesArrNode = om.createArrayNode();
		// ObjectNode all_complete_savesArrNode = om.createObjectNode();
		ArrayNode final_an = om.createArrayNode();
		JavaType javaType = getCollectionType(ArrayList.class, ClusterBean.class);
		String allsrvs = (String) request.getSession().getAttribute("allservertotaljson");
		List<ClusterBean> lcb = null;
		try {
			lcb = (ArrayList<ClusterBean>) om.readValue(allsrvs, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.info("installMqInfo::" + e.getMessage());
			return "redirect:/500.jsp";
		}
		Iterator<ClusterBean> iter = lcb.iterator();
		Iterator<ServersBean> ahbiter = addHostList.iterator();
		while (iter.hasNext()) {
			ObjectNode final_o = om.createObjectNode();
			ClusterBean temp = iter.next();
			ServersBean ahb = ahbiter.next();
			iplistArrNode.add(temp.getIp());
			hostnamelistArrNode.add(temp.getName());
			mq_qmgr_listArrNode.add(temp.getQmgrname());
			user_listArrNode.add(ahb.getUserid());
			password_listArrNode.add(ahb.getPassword());
			outNode.putPOJO("ip_list", iplistArrNode);
			outNode.putPOJO("hostname_list", hostnamelistArrNode);
			if (qmgr_method.equals("yes")) {
				outNode.putPOJO("mq_qmgr_list", mq_qmgr_listArrNode);
			}else{
				ArrayNode an = om.createArrayNode();
				an.add("-");
				outNode.putPOJO("mq_qmgr_list",an);
			}
			
			final_o.put("role", 1);
			final_o.put("uuid", temp.getUuid());
			final_o.put("name", temp.getName());
			final_o.put("address", temp.getIp());
			final_an.add(final_o);

		}
		outNode.put("platform", platform);
		outNode.put("qmgr_method", qmgr_method);
		if (qmgr_method.equals("yes")) {
			// outNode.put("mq_cluster", request.getParameter("mq_cluster"));
			// //单节点不需要
			outNode.put("mq_log_psize", Integer.parseInt(request.getParameter("mq_log_psize")));
			outNode.put("mq_qmgr_plog", Integer.parseInt(request.getParameter("mq_qmgr_plog")));
			outNode.put("mq_qmgr_slog", Integer.parseInt(request.getParameter("mq_qmgr_slog")));
			outNode.put("mq_lstr_port", Integer.parseInt(request.getParameter("mq_lstr_port")));
			outNode.put("mq_chl_max", Integer.parseInt(request.getParameter("mq_chl_max")));
			outNode.put("mq_chl_kalive", request.getParameter("mq_chl_kalive"));
			// outNode.putPOJO("mq_fullRep", all_complete_savesArrNode);
			// //单节点不需要
			outNode.put("mq_data_path", request.getParameter("mq_data_path"));
			outNode.put("mq_log_path", request.getParameter("mq_log_path"));
		}
		if (platform.equals("linux")) {
			ObjectNode lin_sys_param = om.createObjectNode();
			lin_sys_param.put("kernel.sem",
					request.getParameter("lin_semmsl") + " " + request.getParameter("lin_semmns") + " "
							+ request.getParameter("lin_semopm") + " " + request.getParameter("lin_semmni"));
			lin_sys_param.put("kernel.shmmax", request.getParameter("lin_shmax"));
			lin_sys_param.put("kernel.shmall", request.getParameter("lin_shmall"));
			lin_sys_param.put("kernel.shmmni", request.getParameter("lin_shmni"));
			lin_sys_param.put("fs.file-max", request.getParameter("lin_filemax"));
			outNode.putPOJO("os_para_sysctl", lin_sys_param);
			ObjectNode nproc = om.createObjectNode();
			nproc.put("nofile", request.getParameter("lin_nofile"));
			nproc.put("nproc", request.getParameter("lin_nproc"));
			outNode.putPOJO("os_para_ulimit", nproc);
		}
		if (platform.equals("aix")) {
			ObjectNode aix_sys_param = om.createObjectNode();
			aix_sys_param.put("maxuproc", request.getParameter("aix_maxuproc"));
			ObjectNode nproc = om.createObjectNode();
			nproc.put("nofiles", request.getParameter("aix_nofiles"));
			nproc.put("data", request.getParameter("aix_data"));
			nproc.put("stack", request.getParameter("aix_stack"));
			outNode.putPOJO("os_para_sysctl", aix_sys_param);
			outNode.putPOJO("os_para_ulimit", nproc);
		}

		outNode.put("mq_version", request.getParameter("mq_version"));
		// outNode.put("mq_binary", "WS_MQ*.tar*");

		outNode.put("mq_binary", request.getParameter("mqtotalFileName"));
		outNode.put("mq_fix", request.getParameter("mqfix"));
		outNode.put("mq_fp", request.getParameter("mq_fp"));

		outNode.put("downloadpath", (String) amsprop.get("downloadpath") + "/mq");
		outNode.put("ftp_user", (String) amsprop.getProperty("ftp_user"));
		outNode.put("ftp_password", (String) amsprop.getProperty("ftp_password"));
		outNode.put("ftp_server", (String) amsprop.getProperty("ftp_server"));

		// outNode.put("downloadpath", "/tmp/mq");
		outNode.put("mq_rpm", "MQSeries*.rpm");
		outNode.put("mq_user", request.getParameter("mq_user"));
		outNode.put("mq_inst_path", request.getParameter("mq_inst_path"));
		outNode.put("softpath", amsprop.getProperty("softpath") + "mq");

		logger.info("installMqInfo.do::" + outNode.toString());
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_service_run);
		ObjectNode final_on = om.createObjectNode();
		final_on.put("playbook-uuid", UUID.randomUUID().toString());// uuid //
		outNode.put("playbook-uuid", final_on.get("playbook-uuid").asText()); // 每个task
		// 唯一索引
		final_on.put("playbook-name", "mq");// task-name task 名
		final_on.put("product-name", "mq");// product-name 产品名
		final_on.put("param-content", EncodeUtil.encode(outNode.toString()));
		final_on.putPOJO("nodes", final_an);
		logger.info("installMqInfo::" + final_on.toString());
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, final_on.toString());
			ObjectNode ons = (ObjectNode) om.readTree(response);
			if (ons.get("status").asText().equalsIgnoreCase("ok")) {
				logger.info("installMqInfo.do::正在跳转到getLogInfo.do");
				return "redirect:/getLogInfo.do";
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("installMqInfo.do::" + e.getMessage());
			request.setAttribute("errmsg", e.getMessage());

		}
		return "redirect:/500.jsp";

	}

	@RequestMapping("/getmqLogInfoDetail.do")
	public String getmqLogInfoDetail(HttpServletRequest request, HttpSession session) {
		String uuid = request.getParameter("uuid");
		request.setAttribute("uuid", uuid);
		request.setAttribute("type", "mq");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=mq");
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) {
			trans = (ObjectNode) jn;
		}
		if (curPlaybook != null && curPlaybook.get("options") != null) {
			try {
				JsonNode options = om.readTree(curPlaybook.get("options").asText());
				request.setAttribute("mq_version", options.get("mq_version").asText());
				request.setAttribute("mq_fix", options.get("mq_fix").asText());
				request.setAttribute("mq_inst_path", options.get("mq_inst_path").asText());
				request.setAttribute("mq_user", options.get("mq_user").asText());

				request.setAttribute("qmgr_method", options.get("qmgr_method").asText());
				if (options.get("qmgr_method").asText().equalsIgnoreCase("yes")) {
					request.setAttribute("mq_mon_port", options.get("mq_lstr_port").asText());
					request.setAttribute("mq_data_path", options.get("mq_data_path").asText());
					request.setAttribute("mq_log_path", options.get("mq_log_path").asText());
					request.setAttribute("mq_qmgr_plog", options.get("mq_qmgr_plog").asText());
					request.setAttribute("mq_qmgr_slog", options.get("mq_qmgr_slog").asText());
					request.setAttribute("mq_log_psize", options.get("mq_log_psize").asText());
					request.setAttribute("mq_chl_max", options.get("mq_chl_max").asText());
					request.setAttribute("mq_chl_kalive", options.get("mq_chl_kalive").asText());
					JsonNode mqqmgrlist = options.get("mq_qmgr_list");
					if (mqqmgrlist instanceof ArrayNode) {

						ArrayNode an = (ArrayNode) mqqmgrlist;
						request.setAttribute("qmgr_name", an.get(0).asText());
					}
				}
				String type = options.get("platform").asText();
				request.setAttribute("platform", type);
				if (type.equalsIgnoreCase("linux")) {
					String[] sys_param = options.get("os_para_sysctl").get("kernel.sem").asText().split(" ");
					request.setAttribute("lin_semmsl", sys_param[0]);
					request.setAttribute("lin_semmns", sys_param[1]);
					request.setAttribute("lin_semopm", sys_param[2]);
					request.setAttribute("lin_semmni", sys_param[3]);
					request.setAttribute("lin_shmax", options.get("os_para_sysctl").get("kernel.shmmax").asText());
					request.setAttribute("lin_shmni", options.get("os_para_sysctl").get("kernel.shmmni").asText());
					request.setAttribute("lin_shmall", options.get("os_para_sysctl").get("kernel.shmall").asText());
					request.setAttribute("lin_filemax", options.get("os_para_sysctl").get("fs.file-max").asText());
					request.setAttribute("lin_nofile", options.get("os_para_ulimit").get("nofile").asText());
					request.setAttribute("lin_nproc", options.get("os_para_ulimit").get("nproc").asText());
				}
				if (type.equalsIgnoreCase("aix")) {
					request.setAttribute("aix_maxuproc", options.get("os_para_sysctl").get("maxuproc").asText());
					request.setAttribute("aix_nofiles", options.get("os_para_ulimit").get("nofiles").asText());
					request.setAttribute("aix_data", options.get("os_para_ulimit").get("data").asText());
					request.setAttribute("aix_stack", options.get("os_para_ulimit").get("stack").asText());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("getmqLogInfoDetail::" + e.getMessage());
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
		String name=null; //英文步骤名
		try {
			ArrayNode tasks = amsRestService.getList(null, null, url);
			List<FinalStatusBean> list = new ArrayList<FinalStatusBean>();
			
			for (JsonNode jn : tasks) {
				FinalStatusBean mqFinalStatus = new FinalStatusBean();
				 name = jn.get("name").asText();
				ObjectNode tempNode = (ObjectNode) trans.get("dict");
				String chinese = ((JsonNode) tempNode.get(name)).asText();
				mqFinalStatus.setHost(jn.get("host").asText());
				mqFinalStatus.setName(chinese);// 变成中文
				mqFinalStatus.setUuid(jn.get("uuid").asText()); // task 唯一id
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
			logger.info("getmqLogInfo.do::" + list);
		} catch (NullPointerException e) {
			session.setAttribute("errMsg", "请检查"+name+"是否在Mongodb dict 表中定义");
			return "redirect:/500.jsp";
		}
		return "instance_mq_log_details";
	}

}
