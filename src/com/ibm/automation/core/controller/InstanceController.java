package com.ibm.automation.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.ams.service.AmsRestService;
import com.ibm.automation.core.bean.ServersBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.FpService;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.EncodeUtil;
import com.ibm.automation.core.util.FloatToIntUtil;
import com.ibm.automation.core.util.FormatUtil;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.PropertyUtil;
import com.ibm.automation.core.util.ServerUtil;
import com.ibm.automation.core.util.UtilDateTime;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Title:InstanceController
 * @Description:实例管理控制类
 * @Auth:hujin
 * @CreateTime:2015年6月10日 下午4:22:00
 * @version V1.0
 * 
 */
@Controller
public class InstanceController {
	public static Logger logger = Logger.getLogger(InstanceController.class);
	@Autowired
	private AmsRestService amsRestService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private FpService fpService;
	ObjectMapper om = new ObjectMapper();
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");

	/**
	 * 
	 * 将percent 从float 转换为int
	 * */
	
	

	@RequestMapping("/getIBMAllInstance.do")
	public String getIBMAllInstance(HttpServletRequest request, HttpServletResponse resp, HttpSession session)
			throws Exception {
		
		String type = request.getParameter("ptype");// 当前进入的是哪个产品
		if (type == null || type.equals("null")) {
			return "redirect:/getAllServers.do";
		}

		request.setAttribute("ptype", type); // mqcluster
		List<ServersBean> lahb = ServerUtil.getList("odata/servers");
		Collections.sort(lahb);
		request.setAttribute("servers", lahb);
		request.setAttribute("total", lahb.size());

		if (type != null && type != "" && type.equals("was")) {
			return "instance_was_list";
		} else if (type != null && type != "" && type.equals("wascluster")) {
			return "instance_wascluster_list";
		} else if (type != null && type != "" && type.equals("ihs")) {
			return "instance_ihs_list";
		} else if (type != null && type != "" && type.equals("db2ha")) {
			return "instance_db2ha_list";
		} else if (type != null && type != "" && type.equals("db2")) {
			return "instance_db2_list";
		} else if (type != null && type != "" && type.equals("mq")) {
			return "instance_mq_list";
		} else if (type != null && type != "" && type.equals("mqcluster")) {
			return "instance_mqcluster_list";
		} else if (type != null && type != "" && type.equals("itmos")) {
			return "instance_itmos_list";
		} else
			return null;
	}

	/*
	 * /**
	 * 
	 * @Title: getInstanceDetial
	 * 
	 * @Description:查询虚机详细信息
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @param @param session
	 * 
	 * @param @return
	 * 
	 * @param @throws Exception
	 * 
	 * @author LiangRui
	 * 
	 * @throws @Time 2015年6月16日 上午11:08:32
	 */
	// @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getInstanceDetial.do")
	public String getInstanceDetial(HttpServletRequest request, HttpServletResponse resp, HttpSession session)
			throws Exception {
		ObjectMapper om = new ObjectMapper();
		String platform = request.getParameter("platform");
		request.setAttribute("platform", platform);
		String serId = request.getParameter("serId");
		String ptype = request.getParameter("ptype");// db2 -db2ha -was - mq
		logger.info("getInstanceDetial::获取serId 和ptype " + serId + ";" + ptype);
		List<ServersBean> listDetial = ServerUtil.getSelectServers(serId);

		if (ptype != null && ptype != "" && ptype.equals("was")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_was_config";
		} else if (ptype != null && ptype != "" && ptype.equals("wascluster")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_wascluster_config";
		} else if (ptype != null && ptype != "" && ptype.equals("ihs")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_ihs_config";
		} else if (ptype != null && ptype != "" && ptype.equals("mqcluster")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_mqcluster_config";
		} else if (ptype != null && ptype != "" && ptype.equals("mq")) {

			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_mq_config";
		}else if (ptype != null && ptype != "" && ptype.equals("itmos")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_itmos_config";
		} else if (ptype != null && ptype != "" && ptype.equals("osagent")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_os_agent_config";
		} /*else if (ptype != null && ptype != "" && ptype.equals("db2") && platform.equals("aix")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			
			if (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			ArrayNode allHdisk = om.createArrayNode();
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
			request.setAttribute("allHdisk", allHdisk.add(allHdisk1));
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_db2_makevg";
		}*/
		// else if (ptype != null && ptype != "" && ptype.equals("db2") && platform.equals("linux")) {
			else if (ptype != null && ptype != "" && ptype.equals("db2")) {
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			String hostname="";
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
				hostname=bean.getName();
			}
			request.setAttribute("hostname", hostname);
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_db2_config";
		}else if (ptype != null && ptype != "" && ptype.equals("db2ha") && platform.equals("aix")) { 
			Iterator<ServersBean> curIter = listDetial.iterator();
			List<String> curOS = new ArrayList<String>();
			while (curIter.hasNext()) {
				ServersBean bean = (ServersBean) curIter.next();
				curOS.add(bean.getOs());
			}
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			request.setAttribute("hostId", serId);
			return "instance_db2ha_host";
		} else {
			Iterator<ServersBean> iter = listDetial.iterator();
			ServersBean sb = null;
			while (iter.hasNext()) {
				sb = (ServersBean) iter.next();
			}
			Collections.sort(listDetial);
			// request.setAttribute("total", lahb.size());
			request.setAttribute("serId", serId);
			request.setAttribute("servers", listDetial);
			request.setAttribute("ptype", ptype);
			// request.setAttribute("hostId", sb.get_id());
			request.setAttribute("was_ip", sb.getIp());
			String sername = amsprop.getProperty("server_name");
			String serip = amsprop.getProperty("server_ip");
			String pername = amsprop.getProperty("permanent_name");
			String perip = amsprop.getProperty("permanent_ip");

			ArrayNode objnode = om.createArrayNode();
			for (ServersBean ser : listDetial) {
				String addr = ser.getIp() != null ? ser.getIp() : "";
				String addsub = addr.substring(addr.indexOf(".", addr.indexOf(".", addr.indexOf(".", 0) + 1) + 1) + 1,
						addr.length());
				ObjectNode node = om.createObjectNode();
				node.put("serip", serip.replace("#", addsub));
				node.put("sername", sername.replace("#", ser.getName()));
				node.put("perip", perip.replace("#", addsub));
				node.put("pername", pername.replace("#", ser.getName()));
				node.put("hostname", ser.getName());
				node.put("addr", addr);
				// node.put("hostId", ser.get_id());
				objnode.add(node);
			}
			request.setAttribute("hosts", objnode);
			request.setAttribute("ha_primaryNode", request.getParameter("ha_primaryNode"));
			return "instance_aix_db2ha_host";
		}
	}

	public static String[] convertStrToArray(String str) {
		String[] strArray = null;
		strArray = str.split(",");
		return strArray;
	}

	@RequestMapping("/getLogInfo.do")
	public String getLogInfo(HttpServletRequest request, HttpServletResponse resp, HttpSession session) {

		logger.info("getLogInfo.do..................");

		ArrayNode list = amsRestService.getList(null, null, "odata/playbooks");
		ArrayNode logAryNode = om.createArrayNode();
		for (JsonNode jn : list) {
			ObjectNode logObjNode = om.createObjectNode();
			ObjectNode nodeJN = (ObjectNode) jn;
			logObjNode.put("uuid", nodeJN.get("uuid").asText());
			logObjNode.put("type", nodeJN.get("name").asText()); // 什么任务
			logObjNode.put("created_at",
					UtilDateTime.getDateFromMilles(Double.valueOf(nodeJN.get("created_at").asText())));
			ArrayNode Nodes = (ArrayNode) nodeJN.get("nodes");
			ArrayNode judgeArrMainNodes = om.createArrayNode();
			ArrayNode judgeArrSubNodes = om.createArrayNode();
			for (JsonNode node : Nodes) {
				ObjectNode mNode = (ObjectNode) node;
				if (1 == Integer.valueOf(mNode.get("role").asText())) // 1代表主节点
																		// 0
																		// 副节点
				{
					judgeArrMainNodes.add(mNode.get("address").asText());
				} else {
					judgeArrSubNodes.add(mNode.get("address").asText());
				}

			}
			logObjNode.putPOJO("mainNodes", judgeArrMainNodes);
			logObjNode.putPOJO("subNodes", judgeArrSubNodes);
			logObjNode.put("status", nodeJN.get("status").asText());
			logAryNode.add(logObjNode);
		}
		request.setAttribute("items", logAryNode);
		return "instance_log";

	}

	@RequestMapping("/getLogInfoDetail.do")
	public String getLogInfoDetail(HttpServletRequest request, HttpSession session) throws Exception {

		String type = request.getParameter("type");
		if (type.equalsIgnoreCase("db2")) // db2 1表示单节点
		{
			return "forward:" + "/getdb2LogInfoDetail.do";
		} else if (type.equalsIgnoreCase("was")) // was 单节点
		{
			return "forward:" + "/getwasLogInfoDetail.do";
			// return getWasStandAloneInfoDetail(request, session);
		} else if (type.equalsIgnoreCase("wascluster")) {
			// return getWasClusterInfoDetail(request, session);// was cluster
			return "forward:" + "/getwasclusterLogInfoDetail.do";
		} else if (type.equalsIgnoreCase("db2ha")) // 2表示DB2 HA
		{
			return "forward:" + "/getdb2haLogInfoDetail.do";
		} else if (type.equalsIgnoreCase("mqcluster")) {
			return "forward:" + "/getmqclusterLogInfoDetail.do";
		} else if (type.equalsIgnoreCase("mq")) {
			return "forward:" + "/getmqLogInfoDetail.do";
		} else if (type.equalsIgnoreCase("ihs")) {
			return "forward:" + "/getihsLogInfoDetail.do";
		} else if (type.equalsIgnoreCase("itm-os")){
			return "forward:" + "/getitmosLogInfoDetail.do";
		} else
			return "";
	}

	@RequestMapping("nodeInstall.do")
	@ResponseBody
	public void nodeInstall(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String uuid = request.getParameter("uuid");
		String type = request.getParameter("type");// 是哪个安装 类型
		// String type = "linux";
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/playbooks?uuid=" + uuid);
		ArrayNode engToChinse = amsRestService.getList(null, null, "odata/dict?type=" + type);
		ObjectNode trans = om.createObjectNode();
		for (JsonNode jn : engToChinse) {
			trans = (ObjectNode) jn;
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
		ObjectNode respJson = om.createObjectNode();
		String completed = curPlaybook.get("completed").asText();
		String total = curPlaybook.get("total").asText();
		String status = curPlaybook.get("status").asText();
		Map<String, String> map = new TreeMap<String, String>();
		String progress = completed + " / " + total;
		map.put("progress", progress);
		respJson.put("progress", progress);
		String percent = FormatUtil.getPercent(Integer.parseInt(completed), Integer.parseInt(total));
		map.put("percent", percent);
		
		respJson.put("percent", FloatToIntUtil.floatToInt(percent));

		map.put("status", status);
		respJson.put("status", status);
		String table = "odata/tasks?playbook_uuid=";
		String url = table + uuid;
		ArrayNode tasks = amsRestService.getList(null, null, url);

		//List<FinalStatusBean> list = new ArrayList<FinalStatusBean>();
		for (JsonNode jn : tasks) {
			//FinalStatusBean mqFinalStatus = new FinalStatusBean();
			String name = jn.get("name").asText();
			ObjectNode tempNode = (ObjectNode) trans.get("dict");
			String ss = ((JsonNode) tempNode.get(name)).asText();
		//	mqFinalStatus.setHost(jn.get("host").asText());
		//	mqFinalStatus.setName(ss);// 变成中文
			switch (Integer.valueOf(jn.get("status").asText())) {
			case 0:
				// mqFinalStatus.setStatus("未开始");
				respJson.put(jn.get("uuid").asText(), "未开始");
				//respJson.put(jn.get("host").asText() + name+"("+ss+")", "未开始");
				break;
			case 1:
				// mqFinalStatus.setStatus("运行中");
				respJson.put(jn.get("uuid").asText(), "运行中");
				//respJson.put(jn.get("host").asText() + name+"("+ss+")", "运行中");
				break;
			case 2:
				// mqFinalStatus.setStatus("成功");
				respJson.put(jn.get("uuid").asText(), "成功");
				//respJson.put(jn.get("host").asText() + name+"("+ss+")", "成功");
				break;
			case 3:
				// mqFinalStatus.setStatus("失败");
				respJson.put(jn.get("uuid").asText(), "失败");
				//respJson.put(jn.get("host").asText() + name+"("+ss+")", "失败");
				break;
			case 4:
				respJson.put(jn.get("uuid").asText(), "跳过");
				//respJson.put(jn.get("host").asText() + name+"("+ss+")", "跳过"); 
				break;
			}
			//list.add(mqFinalStatus);
		}

		// logger.info("map:" + map);
		// System.out.println("resp::" + respJson);
		logger.info("resp" + respJson);
		out.print(respJson);
		out.close();

	}

	@RequestMapping(value = "/getMqVerion1.do")
	@ResponseBody
	public JSONArray getVersion(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		String pName = request.getParameter("pName");
		// String mqPath = request.getParameter("mqPath");
		String resp = fpService.getVersion(pName, platform);
		JSONArray jsonArr = JSONArray.fromObject(resp);
		logger.info(jsonArr);
		return jsonArr;
	}

	@RequestMapping(value = "/getMqFix.do")
	@ResponseBody
	public JSONArray getMqFix(HttpServletRequest request, HttpSession session) {

		String pVer = request.getParameter("version");// 8.0
		String pName = request.getParameter("pName");// mq
		String platform = request.getParameter("platform");// linux aix
		String resp = fpService.getFix(platform, pName, pVer);
		JSONObject obj = JSONObject.fromObject(resp);
		;
		if (obj.size() == 0) // 返回的数组为空，表示没有查找到
		{
			return new JSONArray();
		}
		JSONArray jsonArr = obj.getJSONArray(pVer);
		logger.info(jsonArr);
		return jsonArr;
	}

	@RequestMapping(value = "/getWasVerion1.do")
	@ResponseBody
	public JSONArray getWasVerion(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		String pName = request.getParameter("pName");
		// String mqPath = request.getParameter("mqPath");
		String resp = fpService.getVersion(pName, platform);
		if (pName.equals("was")) {
			JSONObject obj = JSONObject.fromObject(resp);
			logger.info(obj);
			JSONArray arr = new JSONArray();
			arr.add(obj);
			return arr;
		} else {
			JSONArray jsonArr = JSONArray.fromObject(resp);
			logger.info(jsonArr);
			return jsonArr;
		}
	}

	@RequestMapping(value = "/getWasFix.do")
	@ResponseBody
	public JSONArray getWasFix(HttpServletRequest request, HttpSession session) {

		String pVer = request.getParameter("version");// 8.0
		String pName = request.getParameter("pName");//
		String platform = request.getParameter("platform");// linux aix
		String resp = fpService.getFix(platform, pName, pVer);
		JSONObject obj = JSONObject.fromObject(resp);

		if (obj.size() == 0) // 返回的数组为空，表示没有查找到
		{
			return new JSONArray();
		}
		if (pName.equals("was")) {
			JSONArray jsonArr = new JSONArray();
			jsonArr.add(obj);
			return jsonArr;
		} else {
			JSONArray jsonArr = obj.getJSONArray(pVer);
			logger.info(jsonArr);
			return jsonArr;
		}
	}

	@RequestMapping(value = "/getWasIM.do")
	@ResponseBody
	public JSONArray getWasIM(HttpServletRequest request, HttpSession session) {

		String pVer = request.getParameter("version");// 8.0
		String pName = request.getParameter("pName");//
		String platform = request.getParameter("platform");// linux aix
		String resp = fpService.getIm(platform, pName, pVer);
		JSONObject obj = JSONObject.fromObject(resp);

		if (obj.size() == 0) // 返回的数组为空，表示没有查找到
		{
			return new JSONArray();
		}
		if (pName.equals("was")) {
			JSONArray jsonArr = new JSONArray();
			jsonArr.add(obj);
			return jsonArr;
		} else {
			JSONArray jsonArr = obj.getJSONArray(pVer);
			logger.info(jsonArr);
			return jsonArr;
		}
	}

	@RequestMapping(value = "/getIhsVerion1.do")
	@ResponseBody
	public JSONArray getIhsVerion(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		String pName = request.getParameter("pName");
		String resp = fpService.getVersion(pName, platform);
		if (pName.equals("ihs")) {
			JSONObject obj = JSONObject.fromObject(resp);
			logger.info(obj);
			JSONArray arr = new JSONArray();
			arr.add(obj);
			return arr;
		} else {
			JSONArray jsonArr = JSONArray.fromObject(resp);
			logger.info(jsonArr);
			return jsonArr;
		}
	}

	@RequestMapping(value = "/getIhsFix.do")
	@ResponseBody
	public JSONArray getIhsFix(HttpServletRequest request, HttpSession session) {

		String pVer = request.getParameter("version");// 8.0
		String pName = request.getParameter("pName");//
		String platform = request.getParameter("platform");// linux aix
		String resp = fpService.getFix(platform, pName, pVer);
		JSONObject obj = JSONObject.fromObject(resp);

		if (obj.size() == 0) // 返回的数组为空，表示没有查找到
		{
			return new JSONArray();
		}
		if (pName.equals("ihs")) {
			JSONArray jsonArr = new JSONArray();
			jsonArr.add(obj);
			return jsonArr;
		} else {
			JSONArray jsonArr = obj.getJSONArray(pVer);
			logger.info(jsonArr);
			return jsonArr;
		}
	}
	
	@RequestMapping(value = "/getItmOsVerion1.do")
	@ResponseBody
	public JSONArray getitmosVerion(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		String pName = request.getParameter("pName");
		String resp = fpService.getVersion(pName, platform);
		logger.info(resp);
		System.out.println(resp);
		if (pName.equals("itmos")) {
			JSONObject obj = JSONObject.fromObject(resp);
			logger.info(obj);
			System.out.println(obj);
			JSONArray arr = new JSONArray();
			arr.add(obj);
			return arr;
		} else {
			JSONArray jsonArr = JSONArray.fromObject(resp);
			logger.info(jsonArr);
			System.out.println(jsonArr);
			return jsonArr;
		}
	}

	@RequestMapping(value = "/getItmOSFix.do")
	@ResponseBody
	public JSONArray getitmosfix(HttpServletRequest request, HttpSession session) {

		String pVer = request.getParameter("version");// 8.0
		String pName = request.getParameter("pName");//
		String platform = request.getParameter("platform");// linux aix
		String resp = fpService.getFix(platform, pName, pVer);
		JSONObject obj = JSONObject.fromObject(resp);

		if (obj.size() == 0) // 返回的数组为空，表示没有查找到
		{
			return new JSONArray();
		}
		if (pName.equals("itmos")) {
			JSONArray jsonArr = new JSONArray();
			jsonArr.add(obj);
			return jsonArr;
		} else {
			JSONArray jsonArr = obj.getJSONArray(pVer);
			logger.info(jsonArr);
			return jsonArr;
		}
	}
	
	
	

	@RequestMapping(value = "/getDb2Verion1.do")
	@ResponseBody
	public JSONArray getDB2Verion(HttpServletRequest request, HttpSession session) {
		String platform = request.getParameter("platform");
		String pName = request.getParameter("pName");
		String resp = fpService.getVersion(pName, platform);
		if (pName.equals("db2")) {
			JSONObject obj = JSONObject.fromObject(resp);
			logger.info(obj);
			JSONArray arr = new JSONArray();
			arr.add(obj);
			return arr;
		} else {
			JSONArray jsonArr = JSONArray.fromObject(resp);
			logger.info(jsonArr);
			return jsonArr;
		}
	}

	@RequestMapping(value = "/getDb2Fix.do")
	@ResponseBody
	public JSONArray getDB2Fix(HttpServletRequest request, HttpSession session) {

		String pVer = request.getParameter("version");// 8.0
		String pName = request.getParameter("pName");//
		String platform = request.getParameter("platform");// linux aix
		String resp = fpService.getFix(platform, pName, pVer);
		JSONObject obj = JSONObject.fromObject(resp);

		if (obj.size() == 0) // 返回的数组为空，表示没有查找到
		{
			return new JSONArray();
		}
		if (pName.equals("db2")) {
			JSONArray jsonArr = new JSONArray();
			jsonArr.add(obj);
			return jsonArr;
		} else {
			JSONArray jsonArr = obj.getJSONArray(pVer);
			logger.info(jsonArr);
			return jsonArr;
		}
	}
/**
 * 这是部署的获取errmsg 信息
 * @param request
 * @param session
 * @return
 */
	@RequestMapping(value = "/getErrMsg.do")
	@ResponseBody
	public ObjectNode getErrMsg(HttpServletRequest request, HttpSession session) {

		String uuid = request.getParameter("playbook_uuid");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null, "odata/errmsg?playbook_uuid=" + uuid);
		String retData = curPlaybook.get("errmsg").asText();
		ObjectNode retNode = om.createObjectNode();
		retNode.put("errmsg", retData);
		return retNode;
	}
	


	@RequestMapping(value = "/getPlaybookStatus.do")
	@ResponseBody
	public JsonNode getPlaybookStatus(HttpServletRequest request, HttpSession session) {

		String[] playbookuuids = request.getParameterValues("playbookuuids");
		ArrayNode an = om.createArrayNode();
		ObjectNode singleObj = om.createObjectNode();
		for(String uuid:playbookuuids)
		{
			singleObj.put("playbookuuid", uuid);
			an.addPOJO(singleObj);
		}
		
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,PropertyKeyConst.POST_ams2_server_checkplaybookstatus);
		JsonNode  retJson = null ;
		try {
			String str = EncodeUtil.encode(an.toString());
			String retData = HttpClientUtil.postMethod(strOrgUrl, str);
			if(retData != null)
			{
				 retJson = om.readTree(retData);
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("InstanceController::获取playbook运行状态错误，异常为:"+e.getMessage());
		}
		return retJson;
	}
	
	
	
	public static void main(String[] args) {
		ObjectMapper om = new ObjectMapper();
		ArrayNode an = om.createArrayNode();
	    ObjectNode on = om.createObjectNode();
	    on.put("aaa", "bbbb");
	    an.add(on);
	    System.out.println(EncodeUtil.encode(an.toString()));
	}
}