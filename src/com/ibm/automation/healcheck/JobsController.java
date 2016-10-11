package com.ibm.automation.healcheck;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.ams.service.AmsRestService;
import com.ibm.automation.core.bean.DB2HealthCheckDatabaseBean;
import com.ibm.automation.core.bean.DB2HealthCheckInstanceBean;
import com.ibm.automation.core.bean.JobsBean;
import com.ibm.automation.core.bean.MQQMGRBean;
import com.ibm.automation.core.bean.OSStatusInfo;
import com.ibm.automation.core.bean.OSSystemInfoBean;
import com.ibm.automation.core.bean.RunResultStatusBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.ServerUtil;
import com.ibm.automation.core.util.UtilDateTime;

import net.sf.json.JSONObject;

@Controller
public class JobsController {

	@Autowired
	private ServerService serverService;
	@Autowired
	private AmsRestService amsRestService;
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(JobsController.class);

	/**
	 * 得到所有的巡检任务并展示出来
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/healthCheck.do")
	public String getAlljobs(HttpServletRequest request, HttpSession session) {
		List<JobsBean> ljb = ServerUtil.getJobs("/api/v1/jobs");
		request.setAttribute("jobs", ljb);
		request.setAttribute("proList",request.getSession().getAttribute("proList"));
		request.setAttribute("role", request.getSession().getAttribute("role"));
		return "healthCheck";
	}

	/**
	 * 删除巡检任务，主要用于定时任务、每日循环任务的处理
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/deleteJob.do")
	@ResponseBody
	public String deleteJob(HttpServletRequest request, HttpSession session) {
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_healthCheck_jobs);
		String job_uuid = request.getParameter("job_uuid");// 巡检任务类型
		ObjectNode delete_param = om.createObjectNode();
		delete_param.put("job_uuid", job_uuid);
		delete_param.put("operType", "deleteJob");
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, delete_param.toString());
			logger.info("deletejob::收到数据" + retMsg);
			return retMsg;
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("deletejob::发生异常，异常为：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 创建巡检任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/createJob.do")
	@ResponseBody
	public String createJob(HttpServletRequest request, HttpSession session) {
		String retParam = makeParam(request, session);// 组成java bean
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_healthCheck_jobs);
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, retParam.toString());
			logger.info("创建巡检任务,收到返回数据:" + retMsg);
			ObjectNode ons = (ObjectNode) om.readTree(retMsg);
			if (ons.get("status").asText().equalsIgnoreCase("1")) {
				JSONObject json = new JSONObject();
				json.put("msg", "success");
				return json.toString();
			} else {
				JSONObject json = new JSONObject();
				json.put("msg", "failure");
				return json.toString();
			}
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("创建巡检任务出现网络异常：" + e.getMessage());
		}

		return null;
	}

	/**
	 * 将被巡检的多个IP 单个IP 或者All 封装成一个,分割的字符串 ["MQ_Group"] ["1","2"]
	 *
	 * @param request
	 * @param session
	 * @return
	 */
	public String convertStringListToString(String[] ipList) {
		StringBuffer finalStringOut = new StringBuffer("");
		if (ipList.length == 1) // 表示选了组或者单个IP
		{
			if (!isIP(ipList[0])) // 用正则表达式判断是否为IP ，不是IP就是全部
				return "All";// 返回all的原因是ansible -i all ...
		}
		for (String ip : ipList) {
			finalStringOut.append(ip).append(",");
		}
		return finalStringOut.toString();
	}

	public String makeParam(HttpServletRequest request, HttpSession session) {
		String job_type = request.getParameter("job_type");// 巡检类型
		String[] jobDate = request.getParameterValues("job_date");// 设置时间
		String datetimepic = request.getParameter("datetimepicker"); // 时间控件
		String[] repeat = request.getParameterValues("repeat");// 每日重复checkbox
		JobsBean job = new JobsBean();
		if (jobDate == null && repeat == null) // 立即执行
		{
			job.setJob_if_daily("0"); // 0 立即执行 1 设定时间执行 2 每日执行
			job.setJob_scheduled_at(UtilDateTime.getFormatCurrentDate()); // 立即执行时间
		} else if (jobDate != null && repeat == null && !datetimepic.equals("")) // 定时触发
		{
			job.setJob_if_daily("1"); // 0 立即执行 1 设定时间执行 2 每日执行
			job.setJob_scheduled_at(convertDate(datetimepic));
		} else if (jobDate != null && repeat != null && !datetimepic.equals("")) // 每日执行
		{
			job.setJob_if_daily("2"); // 0 立即执行 1 设定时间执行 2 每日执行
			job.setJob_scheduled_at(datetimepic);
		}

		job.setJob_uuid(UUID.randomUUID().toString());
		job.setJob_type(job_type);
		// 选择组
		String select = request.getParameter("select");
		if (select.equalsIgnoreCase("group"))
			job.setJob_target("All");
		job.setJob_groupOrIP(job_type + "_Group"); // 是组的话就是组名
		if (select.equalsIgnoreCase("ip")) {
			String[] job_target = request.getParameterValues("job_target");// 被巡检的对象有可能是all
			job.setJob_target(convertStringListToString(job_target));
			job.setJob_groupOrIP("ip");// 不是组的话是空
		}

		job.setJob_lastrun_at("");// 上一次执行时间为空
		job.setJob_submited_by((String) request.getSession().getAttribute("userName"));// 从session中获取当前登陆用户
		JSONObject runType = new JSONObject();
		runType.put("status", 0);
		JSONObject jsonObject = JSONObject.fromObject(job);
		jsonObject.put("job_runType", runType);
		jsonObject.put("operType", "createJob");// 是增加还是删除
		logger.info("创建巡检任务，组装参数" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 将16/6/28 7:49 转换为2016/6/28 7:49
	 * 
	 * @param srcDate
	 * @return
	 */
	public String convertDate(String srcDate) {
		if (srcDate != null && srcDate.length() > 5) // 11:10
		{
			SimpleDateFormat srcDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
			SimpleDateFormat destDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				return destDateFormat.format(srcDateFormat.parse(srcDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("jobsController::convertDate-->" + e.getMessage());
			}
			return null;
		} else if (srcDate != null && srcDate.length() < 7) {
			return srcDate;// 直接返回时间
		} else
			return null;
	}

	/**
	 * 处理多个任务的详细信息
	 */
	@RequestMapping("/getJobDetail.do")
	@ResponseBody
	public ArrayNode getJobDetailInfo(HttpServletRequest request, HttpSession session) {
		String uuid = request.getParameter("jobUUID");// 获取选中的复选框的UUID
		return getJobDetailList(uuid);
	}

	public ArrayNode getJobDetailList(String uuid) {
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_healthCheck_jobDetail);
		ObjectNode tempNode = om.createObjectNode();
		tempNode.put("job_uuid", uuid);
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, tempNode.toString());
			logger.info("获取巡检任务列表,返回数据:" + retMsg);
			JsonNode retNode = om.readTree(retMsg);
			if (retNode.get("status").asInt() == 1) {
				return (ArrayNode) retNode.get("msg");
			} else
				return om.createArrayNode();
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取巡检任务列表出现网络异常：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 获取健康检查汇总信息
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/healthCheck_outline.do", method = RequestMethod.GET)
	public String healthCheck_outline(HttpServletRequest request, HttpSession session) {
		String jobType = request.getParameter("jobtype");// 是db2 mq 还是was
		String jobDetail_uuid = request.getParameter("jobDetail_uuid");
		String healthJobsRunResult_datetime = request.getParameter("healthJobsRunResult_datetime");

		List<Integer> pieStatusList = new ArrayList<Integer>();

		if (healthJobsRunResult_datetime.length() > 5) { // 是立即执行和定时执行
			ObjectNode retRunResult = amsRestService.getList_one(null, null,
					"/api/v1/jobrunresult?jobDetail_uuid=" + jobDetail_uuid + "&healthJobsRunResult_datetime="
							+ healthJobsRunResult_datetime + "&curPage=outline");
			Set<String> ipset = new HashSet<String>();

			List<RunResultStatusBean> lrrlsb = new ArrayList<RunResultStatusBean>();
			String retObjNode = retRunResult.get("msg").asText();
			ArrayNode an = null;
			try {
				an = (ArrayNode) om.readTree(retObjNode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (JsonNode jn : an) {
				String datetime = jn.get("healthJobsRunResult_datetime").asText();
				ipset.add(covertDateFormat(datetime));
				RunResultStatusBean rrsb = new RunResultStatusBean();
				rrsb.setHealthJobsRunResult_ip(jn.get("healthJobsRunResult_ip").asText());
				rrsb.setHealthJobsRunResult_uuid(jn.get("healthJobsRunResult_uuid").asText());
				rrsb.setHealthJobsRunResult_result(jn.get("healthJobsRunResult_result").asInt());

				pieStatusList.add(jn.get("healthJobsRunResult_result").asInt());// 饼图加入状态0
																				// 1
																				// 2
																				// 3
				rrsb.setHealthJobsRunResult_detail(jn.get("healthJobsRunResult_detail") == null ? " "
						: jn.get("healthJobsRunResult_detail").asText());
				lrrlsb.add(rrsb);
			}
			request.setAttribute("jobtype", jobType);
			ObjectNode retJobDetail = amsRestService.getList_one(null, null,
					"/api/v1/jobdetail?jobDetail_uuid=" + jobDetail_uuid + "&curPage=outline");
			request.setAttribute("jobDetail_status", retJobDetail.get("jobDetail_status").asText());
			request.setAttribute("jobDetail_submited_by", retJobDetail.get("jobDetail_submited_by").asText());
			request.setAttribute("jobDetail_if_daily", retJobDetail.get("jobDetail_if_daily").asText());

			request.setAttribute("jobDetail_groupOrIP", retJobDetail.get("jobDetail_groupOrIP").asText());
			request.setAttribute("jobDetail_target", retJobDetail.get("jobDetail_target").asText());
			request.setAttribute("IPStatusList", lrrlsb); // 将好坏的IP都放入一个数组进行判断
			request.setAttribute("runTimeList", ipset);
		} else { // 每日执行
			// 1 获取最新时间的任务放在页面
			// 2点击下拉列表获取指定时间，更新table
			ObjectNode retRunResult = amsRestService.getList_one(null, null,
					"/api/v1/jobrunresult?jobDetail_uuid=" + jobDetail_uuid + "&healthJobsRunResult_datetime="
							+ healthJobsRunResult_datetime + "&curPage=outline");
			List<RunResultStatusBean> lrrsb = new ArrayList<RunResultStatusBean>();
			String retObjNode = retRunResult.get("msg").asText();
			ArrayNode an = null;
			try {
				an = (ArrayNode) om.readTree(retObjNode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final TreeSet<String> datetimOrderList = new TreeSet<String>(new StringComparator());
			for (JsonNode tempJn : an) {
				String str = tempJn.get("healthJobsRunResult_datetime").asText();
				datetimOrderList.add(str);

			}

			for (JsonNode jn : an) {
				if (datetimOrderList.first().equals(jn.get("healthJobsRunResult_datetime").asText())) {
					RunResultStatusBean rrsb = new RunResultStatusBean();
					rrsb.setHealthJobsRunResult_ip(jn.get("healthJobsRunResult_ip").asText());
					rrsb.setHealthJobsRunResult_result(jn.get("healthJobsRunResult_result").asInt());
					rrsb.setHealthJobsRunResult_uuid(jn.get("healthJobsRunResult_uuid").asText());
					pieStatusList.add(jn.get("healthJobsRunResult_result").asInt());// 饼图加入状态0
					rrsb.setHealthJobsRunResult_detail(jn.get("healthJobsRunResult_detail") == null ? " "
							: jn.get("healthJobsRunResult_detail").asText());
					lrrsb.add(rrsb);
				}
			}
			Iterator<String> iter = datetimOrderList.iterator();
			List<String> finalList = new ArrayList<String>();
			while (iter.hasNext()) {
				String curTime = (String) iter.next();
				finalList.add(covertDateFormat(curTime));
			}
			request.setAttribute("runTimeList", finalList);
			request.setAttribute("IPStatusList", lrrsb);
			request.setAttribute("jobtype", jobType);
			ObjectNode retJobDetail = amsRestService.getList_one(null, null,
					"/api/v1/jobdetail?jobDetail_uuid=" + jobDetail_uuid + "&curPage=outline");
			request.setAttribute("jobDetail_status", retJobDetail.get("jobDetail_status").asText());
			request.setAttribute("jobDetail_submited_by", retJobDetail.get("jobDetail_submited_by").asText());
			request.setAttribute("jobDetail_if_daily", retJobDetail.get("jobDetail_if_daily").asText());
			request.setAttribute("jobDetail_target", retJobDetail.get("jobDetail_target").asText());
			request.setAttribute("jobDetail_groupOrIP", retJobDetail.get("jobDetail_groupOrIP").asText());
		}
		request.setAttribute("jobDetail_uuid", jobDetail_uuid);
		request.setAttribute("pieMap", convertipStatusToPie(pieStatusList));
		logger.info("正在跳转到healthCheck_outline.jsp...");
		return "healthCheck_outline";
	}

	/**
	 * 从yyyyMMddHHmm 变成yyyy-MM-dd HH:mm
	 * 
	 * @param src
	 * @return
	 */
	public static String covertDateFormat(String src) {
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyyMMddHHmm");
			Date d = sdfSource.parse(src);
			SimpleDateFormat sdfDest = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdfDest.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从 yyyy-MM-dd HH:mm 变成 yyyyMMddHHmm
	 * 
	 * @param src
	 * @return
	 */
	public static String covertToDateFormat(String src) {
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d = sdfSource.parse(src);
			SimpleDateFormat sdfDest = new SimpleDateFormat("yyyyMMddHHmm");
			return sdfDest.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取健康检查详细信息
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/healthCheck_summary.do", method = RequestMethod.GET)
	public String healthCheck_summary(HttpServletRequest request, HttpSession session) {
		String healthJobsRunResult_uuid = request.getParameter("healthJobsRunResult_uuid");
		String jobType = request.getParameter("jobType");// 是哪个任务mq\db2\was\ihs
		ObjectNode retRunResult = amsRestService.getList_one(null, null,
				"/api/v1/jobrunresult?healthJobsRunResult_uuid=" + healthJobsRunResult_uuid + "&curPage=summary");
		logger.info("跳转到summary页面:" + retRunResult);

		String json_os_data = retRunResult.get("msg").get("healthJobsRunResult_retJson_os").asText();

		JsonNode jsonOSData = null;
		try {

			jsonOSData = om.readTree(json_os_data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("healthCheck_summary::解析返回的OS json出错::" + e.getMessage());
		}
		// os 公共信息
		Map<String, String> OSMap = transOSRetJsontoMap(jsonOSData);
		request.setAttribute("healthJobsRunResult_uuid", healthJobsRunResult_uuid);
		request.setAttribute("OSMap", OSMap);
		request.setAttribute("OSENVList", translateOSJsonToList(OSMap));
		request.setAttribute("OSTop10List", translateOSTop10ToList(jsonOSData));
		request.setAttribute("OSSystemInfoList", translateOSSystemInfoToList(jsonOSData));
		request.setAttribute("OSCronjobs", translateOSCronJobsToList(jsonOSData));
		if (jobType.equalsIgnoreCase("oshc")) {
			return "healthCheck_OS_Summary";
		}
		String json_data = null;
		try {
			json_data = retRunResult.get("msg").get("healthJobsRunResult_retJson").asText();
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.error("获取产品数据JSON信息出错：" + e.getMessage());
		}
		JsonNode jsonData = null;
		try {
			jsonData = om.readTree(json_data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("healthCheck_summary::解析返回的产品 json出错::" + e.getMessage());
		}

		if (jobType.equalsIgnoreCase("mqhc")) {
			Map<String, String> MQMap = transMQRetJsontoMap(jsonData);
			request.setAttribute("MQMap", MQMap);
			request.setAttribute("MQQMGRList", translateMQQmgrToList(jsonData));
			return "healthCheck_MQ_Summary";
		}
		if (jobType.equalsIgnoreCase("db2hc")) {
			request.setAttribute("instanceList", transDB2retJsonToList(json_data)); // 实例和数据库信息汇总
			request.setAttribute("DB2Map", transDB2RetJsontoMap(jsonData));
			// db2 系统参数汇总
			return "healthCheck_DB2_Summary";
		}
		if (jobType.equalsIgnoreCase("washc")) {
			return "healthCheck_WAS_Summary";
		}
		return null;
	}

	// 将mq qmgr 转换为ArrayList
	public List<MQQMGRBean> translateMQQmgrToList(JsonNode jsonMQData) {
		JsonNode qmgrsNode = jsonMQData.get("mq").get("QMGRs");
		List<MQQMGRBean> qmgrlist = new ArrayList<MQQMGRBean>();
		if (qmgrsNode instanceof ArrayNode) {
			ArrayNode an = (ArrayNode) qmgrsNode;
			Iterator<JsonNode> json = an.iterator();
			while (json.hasNext()) {
				MQQMGRBean qmgrbean = new MQQMGRBean();
				JsonNode jn = json.next();
				qmgrbean.setQmgrName(jn.get("name").asText() == null ? "" : jn.get("name").asText());
				qmgrbean.setQmgr(jn.get("qmgr").asText() == null ? "" : jn.get("qmgr").asText());
				qmgrbean.setDlq(jn.get("dlq").asText() == null ? "" : jn.get("dlq").asText());
				qmgrbean.setQue(jn.get("que").asText() == null ? "" : jn.get("que").asText());
				qmgrbean.setChl(jn.get("chl").asText() == null ? "" : jn.get("chl").asText());
				qmgrbean.setLstr(jn.get("lstr").asText() == null ? "" : jn.get("lstr").asText());
				qmgrlist.add(qmgrbean);
			}
		}
		return qmgrlist;
	}

	// 将os cpu ram 使用率转换为list
	public List<OSSystemInfoBean> translateOSSystemInfoToList(JsonNode jsonOSData) {
		String[] osArray = { "CPU", "RAM", "Swap", "Disk" };
		List<OSSystemInfoBean> listOSSystemInfoBean = new ArrayList<OSSystemInfoBean>();
		JsonNode runtime = jsonOSData.get("os").get("runtime");
		for (String osInfo : osArray) {
			OSSystemInfoBean osib = new OSSystemInfoBean();
			String line = ((ArrayNode) runtime.get(osInfo)).get(0).asText();
			String status = ((ArrayNode) runtime.get(osInfo)).get(2).asText();
			String value = ((ArrayNode) runtime.get(osInfo)).get(1).asText();
			osib.setHealthInfo(osInfo);
			osib.setHealthLine(line);
			osib.setHealthStatus(status);
			osib.setHealthValue(value);
			listOSSystemInfoBean.add(osib);
		}
		return listOSSystemInfoBean;
	}

	// 将map 中top 10 进程转成list
	public List<OSStatusInfo> translateOSTop10ToList(JsonNode jsonOSData) {
		List<OSStatusInfo> listOSTop10Bean = new ArrayList<OSStatusInfo>();
		JsonNode retOSTop10Json = jsonOSData.get("os").get("top10");
		Iterator<JsonNode> iter = retOSTop10Json.iterator();
		while (iter.hasNext()) {
			ObjectNode aNode = (ObjectNode) iter.next();
			OSStatusInfo osi = new OSStatusInfo();
			osi.setCommand(aNode.get("COMMAND").asText() == null ? "" : aNode.get("COMMAND").asText());
			osi.setElapese(aNode.get("ELAPSED").asText() == null ? "" : aNode.get("ELAPSED").asText());
			osi.setPid(aNode.get("PID").asText() == null ? "" : aNode.get("PID").asText());
			osi.setCpu(aNode.get("CPU").asText() == null ? "" : aNode.get("CPU").asText());
			osi.setUser(aNode.get("USER").asText() == null ? "" : aNode.get("USER").asText());
			listOSTop10Bean.add(osi);
		}
		return listOSTop10Bean;
	}

	// 将map 中top 10 进程转成list
	public Map<String, String> translateOSCronJobsToList(JsonNode jsonOSData) {
		JsonNode cronjobs = jsonOSData.get("os").get("cronjobs");
		Iterator<String> nameIter = cronjobs.fieldNames();
		Map<String, String> mapTemp = new HashMap<String, String>();
		while (nameIter.hasNext()) {

			String s = nameIter.next();

			mapTemp.put(s, cronjobs.get(s).toString());

		}

		return mapTemp;
	}

	// 将map 中的环境变量转换成list
	public List<String> translateOSJsonToList(Map<String, String> OSMap) {
		String[] osENV = OSMap.get("OSENV").toString().split("\n");
		List<String> osENVList = new ArrayList<String>();
		for (String str : osENV) {
			osENVList.add(str);
		}
		return osENVList;
	}

	/**
	 * 将db2 retjson中的系统参数做成map映射
	 * 
	 * @param json
	 * @return map
	 */
	public Map<String, String> transDB2RetJsontoMap(JsonNode json) {
		Map<String, String> map = new HashMap<String, String>();
		JsonNode db2Info = json.get("db2");
		map.put("db2_msgmni_line", db2Info.get("System Paramters").get("msgmni").get(1).asText()); // 数组下标1是基线值
		map.put("db2_msgmax_line", db2Info.get("System Paramters").get("msgmax").get(1).asText());
		map.put("db2_msgmnb_line", db2Info.get("System Paramters").get("msgmnb").get(1).asText());
		map.put("db2_shmmni_line", db2Info.get("System Paramters").get("shmmni").get(1).asText());
		map.put("db2_shmmax_line", db2Info.get("System Paramters").get("shmmax").get(1).asText());
		map.put("db2_sem_line", db2Info.get("System Paramters").get("sem").get(1).asText());
		map.put("db2_shmall_line", db2Info.get("System Paramters").get("shmall").get(1).asText());

		map.put("db2_msgmni", db2Info.get("System Paramters").get("msgmni").get(0).asText()); // 数组下标0是实际值
		map.put("db2_msgmax", db2Info.get("System Paramters").get("msgmax").get(0).asText());
		map.put("db2_msgmnb", db2Info.get("System Paramters").get("msgmnb").get(0).asText());
		map.put("db2_shmmni", db2Info.get("System Paramters").get("shmmni").get(0).asText());
		map.put("db2_shmmax", db2Info.get("System Paramters").get("shmmax").get(0).asText());
		map.put("db2_sem", db2Info.get("System Paramters").get("sem").get(0).asText());
		map.put("db2_shmall", db2Info.get("System Paramters").get("shmall").get(0).asText());
		return map;
	}

	// 将MQ RETJSON处理成Map映射
	public Map<String, String> transMQRetJsontoMap(JsonNode json) {
		Map<String, String> map = new HashMap<String, String>();

		// mq安装信息
		JsonNode mqInfo = json.get("mq");
		map.put("MQVersion", mqInfo.get("Version").asText());
		map.put("MQInstallPath", mqInfo.get("InstallPath").asText());
		// mq系统参数信息
		map.put("MQsemmns", mqInfo.get("System Paramters").get("semmns").get(1).asText());
		map.put("MQsemmsl", mqInfo.get("System Paramters").get("semmsl").get(1).asText());
		map.put("MQnofile", mqInfo.get("System Paramters").get("nofile").get(1).asText());
		map.put("MQshmmni", mqInfo.get("System Paramters").get("shmmni").get(1).asText());
		map.put("MQfile_max", mqInfo.get("System Paramters").get("file-max").get(1).asText());
		map.put("MQshmmax", mqInfo.get("System Paramters").get("shmmax").get(1).asText());
		// map.put("MQsh", mqInfo.get("System Paramters").get("sh").asText());
		map.put("MQnproc", mqInfo.get("System Paramters").get("nproc").get(1).asText());
		map.put("MQsemmni", mqInfo.get("System Paramters").get("semmni").get(1).asText());
		map.put("MQshmall", mqInfo.get("System Paramters").get("shmall").get(1).asText());
		map.put("MQsemopm", mqInfo.get("System Paramters").get("semopm").get(1).asText());

		map.put("MQsemmns_line", mqInfo.get("System Paramters").get("semmns").get(0).asText());
		map.put("MQsemmsl_line", mqInfo.get("System Paramters").get("semmsl").get(0).asText());
		map.put("MQnofile_line", mqInfo.get("System Paramters").get("nofile").get(0).asText());
		map.put("MQshmmni_line", mqInfo.get("System Paramters").get("shmmni").get(0).asText());
		map.put("MQfile_max_line", mqInfo.get("System Paramters").get("file-max").get(0).asText());
		map.put("MQshmmax_line", mqInfo.get("System Paramters").get("shmmax").get(0).asText());
		// map.put("MQsh", mqInfo.get("System Paramters").get("sh").asText());
		map.put("MQnproc_line", mqInfo.get("System Paramters").get("nproc").get(0).asText());
		map.put("MQsemmni_line", mqInfo.get("System Paramters").get("semmni").get(0).asText());
		map.put("MQshmall_line", mqInfo.get("System Paramters").get("shmall").get(0).asText());
		map.put("MQsemopm_line", mqInfo.get("System Paramters").get("semopm").get(0).asText());
		return map;

	}

	// 将os 返回json 串的基本信息和环境变量放入map
	public Map<String, String> transOSRetJsontoMap(JsonNode json) {
		Map<String, String> map = new HashMap<String, String>();
		// ip hostname version start
		map.put("OSIP", json.get("os").get("basic").get("IP").asText());
		map.put("OSHostname", json.get("os").get("basic").get("Hostname").asText());
		map.put("OSVersion", json.get("os").get("basic").get("Version").asText());
		map.put("OSStart", json.get("os").get("basic").get("Start").asText());
		// 环境变量
		map.put("OSENV", json.get("os").get("env").asText());
		return map;

	}

	@RequestMapping(value = "/healthCheck_download_summary_info.do", method = RequestMethod.POST)
	public void healthCheck_download_summary_info(HttpServletRequest request, HttpServletResponse response) {
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_healthCheck_jobRunResult);
		String uuid = request.getParameter("healthJobsRunResult_uuid");
		String ip = request.getParameter("healthJobsRunResult_ip");
		String product = request.getParameter("product");// 判断是os 还是产品
		ObjectNode sendJson = om.createObjectNode();
		sendJson.put("healthJobsRunResult_uuid", uuid);
		sendJson.put("product", product);// 判断是取os 还是产品

		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, sendJson.toString());
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + (product + " - " + ip));
			PrintWriter pw = response.getWriter();
			pw.write(retMsg);
			pw.flush();
			pw.close();

		} catch (NetWorkException | IOException e) {
			e.printStackTrace();
			logger.error("JobsController -- > 下载文件中，出现错误：" + e.getMessage());
		}
	}

	/**
	 * 把/scirpts/xx/mqhc-192.158.123.12 转换为mqhc-192.158.80.142-201603023
	 * 
	 * @param filePathAndName
	 * @return
	 */
	public String convertFileName(String filePathAndName) {
		String[] arr = filePathAndName.split("/");
		StringBuffer sb = new StringBuffer();
		sb.append(arr[arr.length - 1]);
		sb.append("-");
		sb.append(arr[arr.length - 2]);
		return sb.toString();
	}

	/**
	 * 每日任务 获取不同时间点的任务数据
	 */
	@RequestMapping(value = "/getOtherDateData.do", method = RequestMethod.GET)
	@ResponseBody
	public ObjectNode getOtherDateData(HttpServletRequest request, HttpSession session) {
		String jobDetail_uuid = request.getParameter("jobDetail_uuid");
		String healthJobsRunResult_datetime = request.getParameter("healthJobsRunResult_datetime");
		ObjectNode retRunResult = amsRestService.getList_one(null, null,
				"/api/v1/jobrunresult?jobDetail_uuid=" + jobDetail_uuid + "&healthJobsRunResult_datetime="
						+ healthJobsRunResult_datetime + "&curPage=outline");

		List<RunResultStatusBean> lrrsb = new ArrayList<RunResultStatusBean>();
		String retObjNode = retRunResult.get("msg").asText();
		ArrayNode an = null;
		try {
			an = (ArrayNode) om.readTree(retObjNode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String
		// compareDatetime=covertToDateFormat(healthJobsRunResult_datetime);//转换为201607150936

		for (JsonNode jn : an) {
			String datetime = jn.get("healthJobsRunResult_datetime").asText();
			if (healthJobsRunResult_datetime.equals(datetime)) {
				RunResultStatusBean rrsb = new RunResultStatusBean();
				rrsb.setHealthJobsRunResult_ip(jn.get("healthJobsRunResult_ip").asText());
				rrsb.setHealthJobsRunResult_uuid(jn.get("healthJobsRunResult_uuid").asText());
				rrsb.setHealthJobsRunResult_result(jn.get("healthJobsRunResult_result").asInt());
				rrsb.setHealthJobsRunResult_detail(jn.get("healthJobsRunResult_detail") == null ? " "
						: jn.get("healthJobsRunResult_detail").asText());
				lrrsb.add(rrsb);
			}
		}
		ObjectNode retJobDetail = amsRestService.getList_one(null, null,
				"/api/v1/jobdetail?jobDetail_uuid=" + jobDetail_uuid + "&curPage=outline");
		ObjectNode totalNode = om.createObjectNode();
		// ArrayNode iplist = om.createArrayNode();
		totalNode.put("jobDetail_submited_by", retJobDetail.get("jobDetail_submited_by").asText());
		totalNode.put("jobDetail_status", retJobDetail.get("jobDetail_status").asText());
		// iplist.addPOJO(lrrsb);
		totalNode.putPOJO("iplist", lrrsb);
		return totalNode;
	}

	/**
	 * // 将DB2 实例信息，数据库信息汇总成List
	 * 
	 * @param db2_retJson
	 * @return
	 */
	public List<DB2HealthCheckInstanceBean> transDB2retJsonToList(String db2_retJson) {
		JsonNode total = null;
		try {
			total = om.readTree(db2_retJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("transDB2retJsonToList::将db2 返回的json数据解开出错，出错信息为：" + e.getMessage());
			return null;
		}
		JsonNode db2 = total.get("db2");
		JsonNode instance = db2.get("instance");
		List<DB2HealthCheckInstanceBean> totalList = new ArrayList<DB2HealthCheckInstanceBean>();
		for (JsonNode jn : instance) {
			DB2HealthCheckInstanceBean dbhcib = new DB2HealthCheckInstanceBean();// 一个实例对应多个数据库
			Iterator<Entry<String, JsonNode>> iterNode = jn.fields();
			while (iterNode.hasNext()) {

				Entry<String, JsonNode> entry = iterNode.next();
				dbhcib.setDb2_instanceName(entry.getKey());// 获取实例的名
				Iterator<JsonNode> iter = entry.getValue().iterator();
				while (iter.hasNext()) {
					DB2HealthCheckDatabaseBean dbcdb = new DB2HealthCheckDatabaseBean();
					JsonNode dbNode = iter.next();
					// 实例信息设置
					dbhcib.setDb2_instancePath(dbNode.get("path").asText());
					dbhcib.setDb2_instanceLevel(dbNode.get("level").asText());
					// 数据库设置
					dbcdb.setDb2_dbbufferpool_hitratio(dbNode.get("db").get("bufferpool").get("hitratio").asText());
					dbcdb.setDb2_dbconnections(dbNode.get("db").get("connections").asText());
					dbcdb.setDb2_dbdeadlock(dbNode.get("db").get("deadlock").asText());
					dbcdb.setDb2_dblastbackup(dbNode.get("db").get("lastbackup").asText());
					dbcdb.setDb2_dblockeasl(dbNode.get("db").get("lockeasl").asText());
					dbcdb.setDb2_dblocktimeout(dbNode.get("db").get("locktimeout").asText());
					dbcdb.setDb2_dbLOGMETHOD(dbNode.get("db").get("LOGMETHOD").asText());
					dbcdb.setDb2_dbName(dbNode.get("db").get("dbName").asText());
					dbcdb.setDb2_dbsortoverflow(dbNode.get("db").get("sortoverflow").asText());
					dbcdb.setDb2_dbStatus(dbNode.get("db").get("status").asText());
					dbcdb.setDb2_dbTablespacePercent(dbNode.get("db").get("tablespace").get("percent").asText());
					dbcdb.setDb2_dbTablespaceStatus(dbNode.get("db").get("tablespace").get("status").asText());
					// dbcdb.set
					dbhcib.getDb2_instaceList().add(dbcdb);
				}
				totalList.add(dbhcib);
			}
		}
		return totalList;
	}

	/**
	 * 饼图获取数值函数
	 * 
	 * @param iplist
	 *            包含IP 状态的数组
	 * @return 0-10,1-20,2-10,3-5
	 */
	public Map<String, Integer> convertipStatusToPie(List<Integer> iplist) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int goodNum = 0, errorNum = 0, warnNum = 0, unreachableNum = 0;

		for (int status : iplist) {
			if (status == 0)
				goodNum++;
			else if (status == 2) {
				errorNum++;
			} else if (status == 1) {
				warnNum++;
			} else if (status == 3) {
				unreachableNum++;
			}
		}
		map.put("goodNum", goodNum);
		map.put("warnNum", warnNum);
		map.put("errorNum", errorNum);
		map.put("unreachableNum", unreachableNum);

		return map;
	}

	public static boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(addr);

		boolean ipAddress = mat.find();

		return ipAddress;
	}

	/**
	 * 这是健康检查获取IP错误信息
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getIPErrMsg.do")
	@ResponseBody
	public ObjectNode getIPErrMsg(HttpServletRequest request, HttpSession session) {
		String uuid = request.getParameter("healthJobsRunResult_uuid");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null,
				"odata/healthcheckiperrmsg?healthJobsRunResult_uuid=" + uuid);
		String retData = curPlaybook.get("errmsg").asText();
		ObjectNode retNode = om.createObjectNode();
		retNode.put("errmsg", retData);
		return retNode;
	}

}

class StringComparator implements Comparator<String> {

	@Override
	public int compare(String str1, String str2) {

		return str2.compareTo(str1);// 降序;
	}

}
