package com.ibm.automation.configCompare;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import com.ibm.automation.core.bean.ConfigCompareJobBean;
import com.ibm.automation.core.bean.confCompResultStatusBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.ServerUtil;
import com.ibm.automation.core.util.StringUtil;
import com.ibm.automation.core.util.UtilDateTime;

import net.sf.json.JSONObject;

@Controller
public class ConfigCompareController {
	@Autowired
	private ServerService serverService;
	@Autowired
	private AmsRestService amsRestService;
	private static Logger logger = Logger.getLogger(ConfigCompareController.class);
	ObjectMapper om = new ObjectMapper();

	/**
	 * 将201608221651转换为2016-08-22 16:51
	 * 
	 * @throws ParseException
	 * 
	 */
	public static String convertDatetime(String srcdatetime) throws ParseException {
		SimpleDateFormat sdfsrc = new SimpleDateFormat("yyyyMMddHHmm");
		Date srcDatetime = sdfsrc.parse(srcdatetime);
		SimpleDateFormat sdfdest = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdfdest.format(srcDatetime);
	}

	/**
	 * 将2016-08-22 16:56转换为51201608221651
	 * 
	 * @param srcdatetime
	 * @return
	 * @throws ParseException
	 */
	public static String convertOpsiteDatetime(String srcdatetime) throws ParseException {
		SimpleDateFormat sdfsrc = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date srcDatetime = sdfsrc.parse(srcdatetime);
		SimpleDateFormat sdfdest = new SimpleDateFormat("yyyyMMddHHmm");
		return sdfdest.format(srcDatetime);
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

	/**
	 * 这是配置比对获取IP错误信息
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getconfCompIPErrMsg.do")
	@ResponseBody
	public ObjectNode getIPErrMsg(HttpServletRequest request, HttpSession session) {
		String uuid = request.getParameter("confCompJobRunResult_uuid");
		ObjectNode curPlaybook = amsRestService.getList_one(null, null,
				"/api/v1/configcompareiperrmsg?confCompJobRunResult_uuid=" + uuid);
		String retData = curPlaybook.get("errmsg").asText();
		ObjectNode retNode = om.createObjectNode();
		retNode.put("errmsg", retData);
		return retNode;
	}

	public String makeParam(HttpServletRequest request, HttpSession session) {
		String job_type = request.getParameter("confComp_type");// 巡检类型
		String datetimepic = request.getParameter("datetimepicker"); // 时间控件
		// String[] repeat = request.getParameterValues("repeat");//
		// 每日重复checkbox
		String repeat = request.getParameter("repeat");// 是每日重复还是立即执行
		ConfigCompareJobBean cfgcombean = new ConfigCompareJobBean();
		try {
			if (repeat.equals("0")) // 立即执行
			{
				BeanUtils.copyProperty(cfgcombean, "confComp_if_daily", "0");
				BeanUtils.copyProperty(cfgcombean, "confComp_scheduled_at", UtilDateTime.getFormatCurrentDate());
			} else if (repeat.equals("2")) // 每日执行
			{
				BeanUtils.copyProperty(cfgcombean, "confComp_if_daily", "2");
				BeanUtils.copyProperty(cfgcombean, "confComp_scheduled_at", datetimepic);
			}
			BeanUtils.copyProperty(cfgcombean, "confComp_uuid", UUID.randomUUID().toString());
			BeanUtils.copyProperty(cfgcombean, "confComp_type", job_type);
			// 选择组
			String select = request.getParameter("select");
			if (select.equalsIgnoreCase("group")) {
				BeanUtils.copyProperty(cfgcombean, "confComp_target", "All");
				BeanUtils.copyProperty(cfgcombean, "confComp_groupOrIP", job_type + "_Group");
			}
			if (select.equalsIgnoreCase("ip")) {
				String[] job_target = request.getParameterValues("confComp_target");// 被巡检的对象有可能是all
				BeanUtils.copyProperty(cfgcombean, "confComp_groupOrIP", "ip");
				BeanUtils.copyProperty(cfgcombean, "confComp_target", convertStringListToString(job_target));

			}
			BeanUtils.copyProperty(cfgcombean, "confComp_submited_by", request.getSession().getAttribute("userName"));
			BeanUtils.copyProperty(cfgcombean, "confComp_lastrun_at", "");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取配置比对任务参数失败：" + e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取配置比对任务参数失败：" + e.getMessage());
		}

		JSONObject runType = new JSONObject();
		runType.put("status", 0);
		JSONObject jsonObject = JSONObject.fromObject(cfgcombean);
		jsonObject.put("confComp_runType", runType);
		jsonObject.put("operType", "createCfgCompJob");// 是增加还是删除
		logger.info("创建配置比对任务，组装参数" + jsonObject.toString());
		return jsonObject.toString();
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

	@RequestMapping(value = "/configCompare_outline.do", method = RequestMethod.GET)
	public String configCompare_outline(HttpServletRequest request, HttpSession session) {
		String confCompType = request.getParameter("confComptype");// =MQ
		String confCompDetail_uuid = request.getParameter("confCompDetail_uuid");
		String confCompRunResult_datetime = request.getParameter("confCompRunResult_datetime");
		String confCompDetail_if_daily = request.getParameter("confCompDetail_if_daily");
		List<Integer> pieStatusList = new ArrayList<Integer>(); // 用于显示饼图
		if (confCompDetail_if_daily.equals("0")) { // 是立即执行
			ObjectNode retRunResult = amsRestService.getList_one(null, null,
					"/api/v1/configjobrunresult?confCompDetail_uuid=" + confCompDetail_uuid
							+ "&confCompRunResult_datetime=" + confCompRunResult_datetime + "&confCompDetail_if_daily="
							+ confCompDetail_if_daily + "&curPage=outline");
			logger.info(retRunResult);
			Set<String> ipset = new HashSet<String>();
			String retObjNode = retRunResult.get("msg").asText();
			List<confCompResultStatusBean> lccrsb = new ArrayList<confCompResultStatusBean>();
			ArrayNode an = null;
			try {
				an = (ArrayNode) om.readTree(retObjNode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (JsonNode jn : an) {
				String datetime = jn.get("confCompJobRunResult_datetime").asText();
				ipset.add(covertDateFormat(datetime));
				confCompResultStatusBean ccrsb = new confCompResultStatusBean();
				ccrsb.setConfCompJobRunResult_ip(jn.get("confCompJobRunResult_ip").asText());
				ccrsb.setConfCompJobRunResult_uuid(jn.get("confCompJobRunResult_uuid").asText());
				ccrsb.setConfCompJobRunResult_result(jn.get("confCompJobRunResult_result").asInt());
				ccrsb.setConfCompJobRunResult_detail(jn.get("confCompJobRunResult_detail") == null ? ""
						: jn.get("confCompJobRunResult_detail").asText());

				pieStatusList.add(jn.get("confCompJobRunResult_result").asInt());// 饼图加入状态0
																					// 1
																					// 2
																					// 3
				lccrsb.add(ccrsb);
			}

			request.setAttribute("confCompType", confCompType);
			ObjectNode retJobDetail = amsRestService.getList_one(null, null,
					"/api/v1/configjobdetail?confCompDetail_uuid=" + confCompDetail_uuid + "&curPage=outline");
			request.setAttribute("confCompDetail_status", retJobDetail.get("confCompDetail_status").asText());
			request.setAttribute("confCompDetail_submited_by", retJobDetail.get("confCompDetail_submited_by").asText());
			request.setAttribute("confCompDetail_if_daily", retJobDetail.get("confCompDetail_if_daily").asText());

			request.setAttribute("confCompDetail_groupOrIP", retJobDetail.get("confCompDetail_groupOrIP").asText());
			request.setAttribute("confCompDetail_target", retJobDetail.get("confCompDetail_target").asText());
			// request.setAttribute("pieMap",
			// convertipStatusToPie(pieStatusList)); // 饼图
			request.setAttribute("IPStatusList", lccrsb); // 将好坏的IP都放入一个数组进行判断
			request.setAttribute("runTimeList", ipset);

		} else if (confCompDetail_if_daily.equals("2")) {// 每日执行
			ObjectNode retRunResult = amsRestService.getList_one(null, null,
					"/api/v1/configjobrunresult?confCompDetail_uuid=" + confCompDetail_uuid
							+ "&confCompRunResult_datetime=" + confCompRunResult_datetime + "&confCompDetail_if_daily="
							+ confCompDetail_if_daily + "&curPage=outline");
			List<confCompResultStatusBean> lccrsb = new ArrayList<confCompResultStatusBean>();
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
				String str = tempJn.get("confCompJobRunResult_datetime").asText();
				datetimOrderList.add(str);

			}
			for (JsonNode jn : an) {
				if (datetimOrderList.first().equals(jn.get("confCompJobRunResult_datetime").asText())) {
					confCompResultStatusBean rrsb = new confCompResultStatusBean();
					rrsb.setConfCompJobRunResult_ip(jn.get("confCompJobRunResult_ip").asText());
					rrsb.setConfCompJobRunResult_result(jn.get("confCompJobRunResult_result").asInt());
					rrsb.setConfCompJobRunResult_uuid(jn.get("confCompJobRunResult_uuid").asText());
					pieStatusList.add(jn.get("confCompJobRunResult_result").asInt());// 饼图加入状态0
					rrsb.setConfCompJobRunResult_detail(jn.get("confCompJobRunResult_detail") == null ? " "
							: jn.get("confCompJobRunResult_detail").asText());
					lccrsb.add(rrsb);
				}
			}
			Iterator<String> iter = datetimOrderList.iterator();
			List<String> finalList = new ArrayList<String>();
			while (iter.hasNext()) {
				String curTime = (String) iter.next();
				finalList.add(covertDateFormat(curTime));
			}
			request.setAttribute("runTimeList", finalList);
			request.setAttribute("IPStatusList", lccrsb);
			request.setAttribute("confCompType", confCompType);
			ObjectNode retJobDetail = amsRestService.getList_one(null, null,
					"/api/v1/configjobdetail?confCompDetail_uuid=" + confCompDetail_uuid + "&curPage=outline");
			request.setAttribute("confCompDetail_status", retJobDetail.get("confCompDetail_status").asText());
			request.setAttribute("confCompDetail_submited_by", retJobDetail.get("confCompDetail_submited_by").asText());
			request.setAttribute("confCompDetail_if_daily", retJobDetail.get("confCompDetail_if_daily").asText());
			request.setAttribute("confCompDetail_target", retJobDetail.get("confCompDetail_target").asText());
			request.setAttribute("confCompDetail_groupOrIP", retJobDetail.get("confCompDetail_groupOrIP").asText());

		}
		request.setAttribute("pieMap", convertipStatusToPie(pieStatusList)); // 饼图
		request.setAttribute("confCompDetail_uuid", confCompDetail_uuid);
		logger.info("正在跳转到configCompare_outline.jsp...");
		return "configCompare_outline";
	}

	@RequestMapping(value = "/configSummary.do", method = RequestMethod.GET)
	public String configSummary(HttpServletRequest request, HttpSession session) {
		String jobType = request.getParameter("jobType");// mqcc wascc db2cc
		String confCompJobRunResult_uuid = request.getParameter("confCompJobRunResult_uuid");
		ObjectNode retRunResult = amsRestService.getList_one(null, null,
				"/api/v1/configjobrunresult?confCompJobRunResult_uuid=" + confCompJobRunResult_uuid
						+ "&curPage=summary");
		logger.info("跳转到summary页面:" + retRunResult);
		//
		String jsonStr = retRunResult.get("msg").get("confCompJobRunResult_retJson").asText();
		String jsonDatetime = retRunResult.get("msg").get("confCompJobRunResult_datetime").asText();// 获取时间
		JsonNode jsonData = null;
		try {

			jsonData = om.readTree(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("configCompare_summary::解析返回的json出错::" + e.getMessage());
		}

		if (jobType.equalsIgnoreCase("mqcc")) {
			JsonNode retDataArr = jsonData.get("data");
			List<MQconfigCompSummaryBean> lmqcsb = new ArrayList<MQconfigCompSummaryBean>();
			if (retDataArr.size() != 0 && retDataArr instanceof ArrayNode) {
				ArrayNode an = (ArrayNode) retDataArr;
				for (JsonNode jn : an) {
					MQconfigCompSummaryBean mqccsb = new MQconfigCompSummaryBean();
					mqccsb.setClntRcvBuffSize(jn.get("config").get("ClntRcvBuffSize").asText());
					mqccsb.setName(jn.get("name").asText());
					try {
						mqccsb.setCompDate(convertDatetime(jsonDatetime));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("configcomparecontroller 转换时间出错" + e.getMessage());
						mqccsb.setCompDate(jsonDatetime);
					}
					mqccsb.setClntSndBuffSize(jn.get("config").get("ClntSndBuffSize").asText());
					mqccsb.setKeepAlive(jn.get("config").get("KeepAlive").asText());
					mqccsb.setLogFilePages(jn.get("config").get("LogFilePages").asText());
					mqccsb.setLogSecondaryFiles(jn.get("config").get("LogSecondaryFiles").asText());
					mqccsb.setLogPrimaryFiles(jn.get("config").get("LogPrimaryFiles").asText());
					mqccsb.setLogType(jn.get("config").get("LogType").asText());
					mqccsb.setMaxActiveChannels(jn.get("config").get("MaxActiveChannels").asText());
					mqccsb.setMaxChannels(jn.get("config").get("MaxChannels").asText());
					mqccsb.setRcvBuffSize(jn.get("config").get("RcvBuffSize").asText());
					mqccsb.setRcvRcvBuffSize(jn.get("config").get("RcvRcvBuffSize").asText());
					mqccsb.setRcvSndBuffSize(jn.get("config").get("RcvSndBuffSize").asText());
					mqccsb.setSndBuffSize(jn.get("config").get("SndBuffSize").asText());
					mqccsb.setSvrRcvBuffSize(jn.get("config").get("RcvBuffSize").asText());
					mqccsb.setSvrSndBuffSize(jn.get("config").get("SvrRcvBuffSize").asText());
					lmqcsb.add(mqccsb);
				}
			}
			request.setAttribute("mqcclistbase64", StringUtil.encodeString(lmqcsb.toString()));
			return "config_MQ_Summary";
		} else if (jobType.equalsIgnoreCase("wascc"))
			return "config_WAS_Summary;";
		else if (jobType.equalsIgnoreCase("db2cc")) {
			getDB2SummaryInfo(jsonData, jsonDatetime, request);

			return "config_DB2_Summary";
		}

		return "";
	}

	public void getDB2SummaryInfo(JsonNode jsonData, String jsonDatetime, HttpServletRequest request) {
		JsonNode retDataArr = jsonData.get("data");
		List<DB2InstanceConfigCompSummaryBean> ldb2ccsb = new ArrayList<DB2InstanceConfigCompSummaryBean>();
		List<DB2DataBaseConfigCompSummaryBean> ldb2dbccsb = new ArrayList<DB2DataBaseConfigCompSummaryBean>();
		if (retDataArr.size() != 0 && retDataArr instanceof ArrayNode) {
			ArrayNode an = (ArrayNode) retDataArr;
			for (JsonNode jn : an) {
				DB2InstanceConfigCompSummaryBean db2ccsb = new DB2InstanceConfigCompSummaryBean();
				DB2DataBaseConfigCompSummaryBean db2dbccsb = new DB2DataBaseConfigCompSummaryBean();
				if (jn.get("type").asText().equalsIgnoreCase("instance"))// 实例
				{
					db2ccsb.setName(jn.get("name").asText());
					db2ccsb.setSYSMON_GROUP(jn.get("config").get("SYSMON_GROUP (memory)   ").asText());
					db2ccsb.setRESTBUFSZ(jn.get("config").get("RESTBUFSZ (4KB)").asText());
					db2ccsb.setDIAGPATH(jn.get("config").get("DIAGPATH (memory)").asText());
					db2ccsb.setASLHEAPSZ(jn.get("config").get("ASLHEAPSZ (4KB)").asText());
					db2ccsb.setMAX_CONNECTIONS(jn.get("config").get("MAX_CONNECTIONS").asText());
					db2ccsb.setBACKBUFSZ(jn.get("config").get("BACKBUFSZ (4KB)").asText());
					db2ccsb.setINTRA_PARALLEL(jn.get("config").get("INTRA_PARALLEL").asText());
					db2ccsb.setUTIL_IMPACT_LIM(jn.get("config").get("UTIL_IMPACT_LIM").asText());
					db2ccsb.setALT_DIAGPATH(jn.get("config").get("ALT_DIAGPATH (memory)").asText());
					db2ccsb.setFENCED_POOL(jn.get("config").get("FENCED_POOL").asText());
					db2ccsb.setINSTANCE_MEMORY(jn.get("config").get("INSTANCE_MEMORY (4KB)").asText());
					db2ccsb.setSYSMAINT_GROUP(jn.get("config").get("SYSMAINT_GROUP (memory) ").asText());
					db2ccsb.setMAX_QUERYDEGREE(jn.get("config").get("MAX_QUERYDEGREE").asText());
					db2ccsb.setSYSCTRL_GROUP(jn.get("config").get("SYSCTRL_GROUP (memory)").asText());
					db2ccsb.setDFT_MON_STMT(jn.get("config").get("DFT_MON_STMT").asText());
					db2ccsb.setNOTIFYLEVEL(jn.get("config").get("NOTIFYLEVEL").asText());
					db2ccsb.setDFT_MON_LOCK(jn.get("config").get("DFT_MON_LOCK").asText());
					db2ccsb.setDFT_MON_BUFPOOL(jn.get("config").get("DFT_MON_BUFPOOL").asText());
					db2ccsb.setDFT_MON_SORT(jn.get("config").get("DFT_MON_SORT").asText());
					db2ccsb.setDIAGSIZE(jn.get("config").get("DIAGSIZE (MB)").asText());
					db2ccsb.setAUTHENTICATION(jn.get("config").get("AUTHENTICATION").asText());
					db2ccsb.setSYSADM_GROUP(jn.get("config").get("SYSADM_GROUP (memory)").asText());
					db2ccsb.setDIAGLEVEL(jn.get("config").get("DIAGLEVEL").asText());
					db2ccsb.setDFT_MON_TABLE(jn.get("config").get("DFT_MON_TABLE").asText());
					db2ccsb.setDISCOVER(jn.get("config").get("DISCOVER").asText());
					db2ccsb.setJAVA_HEAP_SZ(jn.get("config").get("JAVA_HEAP_SZ (4KB)").asText());
					db2ccsb.setMAX_COORDAGENTS(jn.get("config").get("MAX_COORDAGENTS").asText());
					db2ccsb.setINDEXREC(jn.get("config").get("INDEXREC").asText());
					db2ccsb.setDFT_MON_UOW(jn.get("config").get("DFT_MON_UOW").asText());
					db2ccsb.setHEALTH_MON(jn.get("config").get("HEALTH_MON").asText());
					db2ccsb.setRESYNC_INTERVAL(jn.get("config").get("RESYNC_INTERVAL (secs)").asText());
					db2ccsb.setSHEAPTHRES(jn.get("config").get("SHEAPTHRES (4KB)").asText());
					db2ccsb.setAUDIT_BUF_SZ(jn.get("config").get("AUDIT_BUF_SZ (4KB)").asText());
					db2ccsb.setNUM_INITAGENTS(jn.get("config").get("NUM_INITAGENTS").asText());
					db2ccsb.setQUERY_HEAP_SZ(jn.get("config").get("QUERY_HEAP_SZ (4KB)").asText());
					db2ccsb.setDFT_MON_TIMESTAMP(jn.get("config").get("DFT_MON_TIMESTAMP").asText());
					db2ccsb.setRQRIOBLK(jn.get("config").get("RQRIOBLK (bytes)").asText());
					db2ccsb.setKEEPFENCED(jn.get("config").get("KEEPFENCED").asText());
					db2ccsb.setNUM_POOLAGENTS(jn.get("config").get("NUM_POOLAGENTS").asText());
					db2ccsb.setNUM_INITFENCED(jn.get("config").get("NUM_INITFENCED").asText());
					db2ccsb.setDFTDBPATH(jn.get("config").get("DFTDBPATH (memory)").asText());
					db2ccsb.setMON_HEAP_SZ(jn.get("config").get("MON_HEAP_SZ (4KB)").asText());
					db2ccsb.setAGENTPRI(jn.get("config").get("AGENTPRI").asText());
					db2ccsb.setDISCOVER_INST(jn.get("config").get("DISCOVER_INST").asText());
					db2ccsb.setSVCENAME(jn.get("config").get("SVCENAME").asText());
					try {
						db2ccsb.setCompDate(convertDatetime(jsonDatetime));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("configcomparecontroller 转换时间出错" + e.getMessage());
						db2ccsb.setCompDate(jsonDatetime);
					}
					ldb2ccsb.add(db2ccsb);
				}
				if (jn.get("type").asText().equalsIgnoreCase("database")) // db信息
				{
					db2dbccsb.setName(jn.get("name").asText());
					db2dbccsb.setParent(jn.get("parent").asText());
					db2dbccsb.setENABLE_XMLCHAR(jn.get("config").get("ENABLE_XMLCHAR").asText());
					db2dbccsb.setAUTORESTART(jn.get("config").get("AUTORESTART").asText());
					db2dbccsb.setLOCKTIMEOUT(jn.get("config").get("LOCKTIMEOUT (secs)").asText());
					db2dbccsb.setAUTO_PROF_UPD(jn.get("config").get("AUTO_PROF_UPD").asText());
					db2dbccsb.setLog_retain_for_recovery_status(
							jn.get("config").get("Log retain for recovery status").asText());
					db2dbccsb.setMINCOMMIT(jn.get("config").get("MINCOMMIT").asText());
					db2dbccsb.setREC_HIS_RETENTN(jn.get("config").get("REC_HIS_RETENTN (days)").asText());
					db2dbccsb.setUser_exit_for_logging_status(
							jn.get("config").get("User exit for logging status").asText());
					db2dbccsb.setUTIL_HEAP_SZ(jn.get("config").get("UTIL_HEAP_SZ (4KB)").asText());
					db2dbccsb.setDFT_SQLMATHWARN(jn.get("config").get("DFT_SQLMATHWARN").asText());
					db2dbccsb.setHADR_database_role(jn.get("config").get("HADR database role").asText());
					db2dbccsb.setSELF_TUNING_MEM(jn.get("config").get("SELF_TUNING_MEM").asText());
					db2dbccsb.setSEQDETECT(jn.get("config").get("SEQDETECT").asText());
					db2dbccsb.setTSM_MGMTCLASS(jn.get("config").get("TSM_MGMTCLASS").asText());
					db2dbccsb.setMAX_LOG(jn.get("config").get("MAX_LOG").asText());
					db2dbccsb.setAUTO_REVAL(jn.get("config").get("AUTO_REVAL").asText());
					db2dbccsb.setHADR_TIMEOUT(jn.get("config").get("HADR_TIMEOUT").asText());
					db2dbccsb.setAUTO_DEL_REC_OBJ(jn.get("config").get("AUTO_DEL_REC_OBJ").asText());
					db2dbccsb.setNUM_QUANTILES(jn.get("config").get("NUM_QUANTILES").asText());
					db2dbccsb.setHADR_REMOTE_SVC(jn.get("config").get("HADR_REMOTE_SVC").asText());
					db2dbccsb.setMON_PKGLIST_SZ(jn.get("config").get("MON_PKGLIST_SZ").asText());
					db2dbccsb.setNUM_DB_BACKUPS(jn.get("config").get("NUM_DB_BACKUPS").asText());
					db2dbccsb.setVARCHAR2_COMPAT(jn.get("config").get("VARCHAR2_COMPAT").asText());
					db2dbccsb.setDB_MEM_THRESH(jn.get("config").get("DB_MEM_THRESH").asText());
					db2dbccsb.setAUTO_REORG(jn.get("config").get("AUTO_REORG").asText());
					db2dbccsb.setARCHRETRYDELAY(jn.get("config").get("ARCHRETRYDELAY (secs)").asText());
					db2dbccsb.setVENDOROPT(jn.get("config").get("VENDOROPT").asText());
					db2dbccsb.setDFT_EXTENT_SZ(jn.get("config").get("DFT_EXTENT_SZ (pages)").asText());
					db2dbccsb.setNUM_FREQVALUES(jn.get("config").get("NUM_FREQVALUES").asText());
					db2dbccsb.setMAXAPPLS(jn.get("config").get("MAXAPPLS").asText());
					db2dbccsb.setDECFLT_ROUNDING(jn.get("config").get("DECFLT_ROUNDING").asText());
					db2dbccsb.setMON_OBJ_METRICS(jn.get("config").get("MON_OBJ_METRICS").asText());
					db2dbccsb.setAUTO_MAINT(jn.get("config").get("AUTO_MAINT").asText());
					db2dbccsb.setALT_COLLATE(jn.get("config").get("ALT_COLLATE").asText());
					db2dbccsb.setDBHEAP(jn.get("config").get("DBHEAP (4KB)").asText());
					db2dbccsb.setNUMBER_COMPAT(jn.get("config").get("NUMBER_COMPAT").asText());
					db2dbccsb.setDISCOVER_DB(jn.get("config").get("DISCOVER_DB").asText());
					db2dbccsb.setINDEXREC(jn.get("config").get("INDEXREC").asText());
					db2dbccsb.setMAXLOCKS(jn.get("config").get("MAXLOCKS").asText());
					db2dbccsb.setDefault_number_of_containers(
							jn.get("config").get("Default number of containers").asText());
					db2dbccsb.setBLK_LOG_DSK_FUL(jn.get("config").get("BLK_LOG_DSK_FUL").asText());
					db2dbccsb.setTSM_PASSWORD(jn.get("config").get("TSM_PASSWORD").asText());
					db2dbccsb.setLOGRETAIN(jn.get("config").get("LOGRETAIN").asText());
					db2dbccsb.setBUFFPAGE(jn.get("config").get("BUFFPAGE").asText());
					db2dbccsb.setSTMTHEAP(jn.get("config").get("STMTHEAP (4KB)").asText());
					db2dbccsb.setLOGPRIMARY(jn.get("config").get("LOGPRIMARY").asText());
					db2dbccsb.setDatabase_territory(jn.get("config").get("Database territory").asText());
					db2dbccsb.setDatabase_code_page(jn.get("config").get("Database code page").asText());
					db2dbccsb.setDFT_QUERYOPT(jn.get("config").get("DFT_QUERYOPT").asText());
					db2dbccsb.setDatabase_country_region_code(
							jn.get("config").get("Database country/region code").asText());
					db2dbccsb.setFirst_active_log_file(jn.get("config").get("First active log file").asText());
					db2dbccsb.setLOGARCHMETH1(jn.get("config").get("LOGARCHMETH1 (memory)").asText());
					db2dbccsb.setCHNGPGS_THRESH(jn.get("config").get("CHNGPGS_THRESH").asText());
					db2dbccsb.setDFT_LOADREC_SES(jn.get("config").get("DFT_LOADREC_SES").asText());
					db2dbccsb.setTSM_OWNER(jn.get("config").get("TSM_OWNER").asText());
					db2dbccsb.setAUTO_STATS_PROF(jn.get("config").get("AUTO_STATS_PROF").asText());
					db2dbccsb.setDEC_TO_CHAR_FMT(jn.get("config").get("DEC_TO_CHAR_FMT").asText());
					db2dbccsb.setCATALOGCACHE_SZ(jn.get("config").get("CATALOGCACHE_SZ (4KB)").asText());
					db2dbccsb.setPath_to_log_files(jn.get("config").get("Path to log files (memory)").asText());
					db2dbccsb.setDFT_DEGREE(jn.get("config").get("DFT_DEGREE").asText());
					db2dbccsb.setLOGFILSIZ(jn.get("config").get("LOGFILSIZ (4KB)").asText());
					db2dbccsb.setBackup_pending(jn.get("config").get("Backup pending").asText());
					db2dbccsb.setSHEAPTHRES_SHR(jn.get("config").get("SHEAPTHRES_SHR (4KB)").asText());
					db2dbccsb.setRestrict_access(jn.get("config").get("Restrict access").asText());
					db2dbccsb.setStatement_concentrator(jn.get("config").get("Statement concentrator").asText());
					db2dbccsb.setDATE_COMPAT(jn.get("config").get("DATE_COMPAT").asText());
					db2dbccsb.setHADR_PEER_WINDOW(jn.get("config").get("HADR_PEER_WINDOW (secs)").asText());
					db2dbccsb.setMAXFILOP(jn.get("config").get("MAXFILOP").asText());
					db2dbccsb.setLOGSECOND(jn.get("config").get("LOGSECOND").asText());
					db2dbccsb.setAUTO_TBL_MAINT(jn.get("config").get("AUTO_TBL_MAINT").asText());
					db2dbccsb.setHADR_REMOTE_INST(jn.get("config").get("HADR_REMOTE_INST").asText());
					db2dbccsb.setAUTO_DB_BACKUP(jn.get("config").get("AUTO_DB_BACKUP").asText());
					db2dbccsb.setDFT_PREFETCH_SZ(jn.get("config").get("DFT_PREFETCH_SZ (pages)").asText());
					db2dbccsb.setMON_LOCKWAIT(jn.get("config").get("MON_LOCKWAIT").asText());
					db2dbccsb.setNUM_IOCLEANERS(jn.get("config").get("NUM_IOCLEANERS").asText());
					db2dbccsb.setNUM_LOG_SPAN(jn.get("config").get("NUM_LOG_SPAN").asText());
					db2dbccsb.setMON_LOCKTIMEOUT(jn.get("config").get("MON_LOCKTIMEOUT").asText());
					db2dbccsb.setTRACKMOD(jn.get("config").get("TRACKMOD").asText());
					db2dbccsb.setNUM_IOSERVERS(jn.get("config").get("NUM_IOSERVERS").asText());
					db2dbccsb.setLOGINDEXBUILD(jn.get("config").get("LOGINDEXBUILD").asText());
					db2dbccsb.setMON_LCK_MSG_LVL(jn.get("config").get("MON_LCK_MSG_LVL").asText());
					db2dbccsb.setMON_REQ_METRICS(jn.get("config").get("MON_REQ_METRICS").asText());
					db2dbccsb.setLOGARCHMETH2(jn.get("config").get("LOGARCHMETH2 (memory)").asText());
					db2dbccsb.setDLCHKTIME(jn.get("config").get("DLCHKTIME (ms)").asText());
					db2dbccsb.setSOFTMAX(jn.get("config").get("SOFTMAX").asText());
					db2dbccsb.setMIRRORLOGPATH(jn.get("config").get("MIRRORLOGPATH (memory)").asText());
					db2dbccsb.setMultipage_File_alloc_enabled(
							jn.get("config").get("Multi-page File alloc enabled").asText());
					db2dbccsb.setNUMARCHRETRY(jn.get("config").get("NUMARCHRETRY").asText());
					db2dbccsb.setAUTO_STMT_STATS(jn.get("config").get("AUTO_STMT_STATS").asText());
					db2dbccsb.setFAILARCHPATH(jn.get("config").get("FAILARCHPATH (memory)").asText());
					db2dbccsb.setUSEREXIT(jn.get("config").get("USEREXIT").asText());
					db2dbccsb.setDatabase_page_size(jn.get("config").get("Database page size").asText());
					db2dbccsb.setHADR_LOCAL_HOST(jn.get("config").get("HADR_LOCAL_HOST").asText());
					db2dbccsb.setMON_ACT_METRICS(jn.get("config").get("MON_ACT_METRICS").asText());
					db2dbccsb.setAVG_APPLS(jn.get("config").get("AVG_APPLS").asText());
					db2dbccsb.setWLM_COLLECT_INT(jn.get("config").get("WLM_COLLECT_INT (mins)").asText());
					db2dbccsb.setLOGBUFSZ(jn.get("config").get("LOGBUFSZ (4KB)").asText());
					db2dbccsb.setDFT_MTTB_TYPES(jn.get("config").get("DFT_MTTB_TYPES").asText());
					db2dbccsb.setDatabase_is_consistent(jn.get("config").get("Database is consistent").asText());
					db2dbccsb.setSTAT_HEAP_SZ(jn.get("config").get("STAT_HEAP_SZ (4KB)").asText());
					db2dbccsb.setDatabase_code_set(jn.get("config").get("Database code set").asText());
					db2dbccsb.setSORTHEAP(jn.get("config").get("SORTHEAP (4KB)").asText());
					db2dbccsb.setCUR_COMMIT(jn.get("config").get("CUR_COMMIT").asText());
					db2dbccsb.setMON_LW_THRESH(jn.get("config").get("MON_LW_THRESH").asText());
					db2dbccsb.setDATABASE_MEMORY(jn.get("config").get("DATABASE_MEMORY (4KB)").asText());
					db2dbccsb.setMON_UOW_DATA(jn.get("config").get("MON_UOW_DATA").asText());
					db2dbccsb.setBLOCKNONLOGGED(jn.get("config").get("BLOCKNONLOGGED").asText());
					db2dbccsb.setTSM_NODENAME(jn.get("config").get("TSM_NODENAME").asText());
					db2dbccsb.setPCKCACHESZ(jn.get("config").get("PCKCACHESZ (4KB)").asText());
					db2dbccsb.setHADR_REMOTE_HOST(jn.get("config").get("HADR_REMOTE_HOST").asText());
					db2dbccsb.setAPPL_MEMORY(jn.get("config").get("APPL_MEMORY (4KB)").asText());
					db2dbccsb.setRestore_pending(jn.get("config").get("Restore pending").asText());
					db2dbccsb.setDFT_REFRESH_AGE(jn.get("config").get("DFT_REFRESH_AGE").asText());
					db2dbccsb.setHADR_SYNCMODE(jn.get("config").get("HADR_SYNCMODE").asText());
					db2dbccsb.setOVERFLOWLOGPATH(jn.get("config").get("OVERFLOWLOGPATH (memory)").asText());
					db2dbccsb.setMON_DEADLOCK(jn.get("config").get("MON_DEADLOCK").asText());
					db2dbccsb.setAUTO_RUNSTATS(jn.get("config").get("AUTO_RUNSTATS").asText());
					db2dbccsb.setLOCKLIST(jn.get("config").get("LOCKLIST (4KB)").asText());
					db2dbccsb.setRollforward_pending(jn.get("config").get("Rollforward pending").asText());
					db2dbccsb.setDatabase_collating_sequence(
							jn.get("config").get("Database collating sequence").asText());
					db2dbccsb.setNEWLOGPATH(jn.get("config").get("NEWLOGPATH (memory)").asText());
					db2dbccsb.setDYN_QUERY_MGMT(jn.get("config").get("DYN_QUERY_MGMT").asText());
					db2dbccsb.setINDEXSORT(jn.get("config").get("INDEXSORT").asText());
					db2dbccsb.setAPPLHEAPSZ(jn.get("config").get("APPLHEAPSZ (4KB)").asText());
					db2dbccsb.setHADR_LOCAL_SVC(jn.get("config").get("HADR_LOCAL_SVC").asText());
					try {
						db2dbccsb.setCompDate(convertDatetime(jsonDatetime));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("configcomparecontroller 转换时间出错" + e.getMessage());
						db2ccsb.setCompDate(jsonDatetime);
					}
					ldb2dbccsb.add(db2dbccsb);
				}

			}
		}
		request.setAttribute("db2cclistbase64", StringUtil.encodeString(ldb2ccsb.toString()));
		request.setAttribute("db2dbcclistbase64", StringUtil.encodeString(ldb2dbccsb.toString()));
	}

	@RequestMapping("/createConfCompJob.do")
	@ResponseBody
	public String addConfigCompareJob(HttpServletRequest request, HttpSession session) {
		String retParam = makeParam(request, session);
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_configCompare_jobs);
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, retParam.toString());
			logger.info("创建配置比对任务,收到返回数据:" + retMsg);
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
			logger.error("创建配置比对任务出现网络异常：" + e.getMessage());
		}

		return null;
	}

	/**
	 * 得到所有的巡检任务并展示出来
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/configCompare.do")
	public String getAlljobs(HttpServletRequest request, HttpSession session) {
		List<ConfigCompareJobBean> ljb = ServerUtil.getConfCompJobs("/api/v1/configJobs");
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_configCompare_getCCInfo);
		String retMsg = null;
		try {
			retMsg = HttpClientUtil.postMethod(strOrgUrl, "{}");
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取配置比对信息出错，原因为：" + e.getMessage());

		}
		JsonNode retJson = null;
		try {
			retJson = om.readTree(retMsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = retJson.get("msg").asText();
		JsonNode innerJson = null;
		try {
			innerJson = om.readTree(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return innerJson;
		List<String> list = new ArrayList<String>();
		Iterator<JsonNode> iter = innerJson.iterator();
		while (iter.hasNext()) {
			String temp = iter.next().asText();
			list.add(temp);
		}
		request.setAttribute("totalIPList", list);
		request.setAttribute("jobs", ljb);
		return "configCompare";
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
	 * 每日任务 获取不同时间点的任务数据
	 */
	@RequestMapping(value = "/getconfCompOtherDateData.do", method = RequestMethod.GET)
	@ResponseBody
	public ObjectNode getconfCompOtherDateData(HttpServletRequest request, HttpSession session) {
		String confCompDetail_uuid = request.getParameter("confCompDetail_uuid");
		String confCompRunResult_datetime = request.getParameter("confCompRunResult_datetime");
		String confCompDetail_if_daily = request.getParameter("confCompDetail_if_daily");
		ObjectNode retRunResult = amsRestService.getList_one(null, null,
				"/api/v1/configjobrunresult?confCompDetail_uuid=" + confCompDetail_uuid + "&confCompRunResult_datetime="
						+ confCompRunResult_datetime + "&curPage=outline" + "&confCompDetail_if_daily="
						+ confCompDetail_if_daily);

		List<confCompResultStatusBean> lrrsb = new ArrayList<confCompResultStatusBean>();
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
			String datetime = jn.get("confCompJobRunResult_datetime").asText();
			if (confCompRunResult_datetime.equals(datetime)) {
				confCompResultStatusBean rrsb = new confCompResultStatusBean();
				rrsb.setConfCompJobRunResult_ip(jn.get("confCompJobRunResult_ip").asText());
				rrsb.setConfCompJobRunResult_result(jn.get("confCompJobRunResult_result").asInt());
				rrsb.setConfCompJobRunResult_uuid(jn.get("confCompJobRunResult_uuid").asText());
				rrsb.setConfCompJobRunResult_detail(jn.get("confCompJobRunResult_detail") == null ? " "
						: jn.get("confCompJobRunResult_detail").asText());
				lrrsb.add(rrsb);
			}
		}
		ObjectNode retJobDetail = amsRestService.getList_one(null, null,
				"/api/v1/configjobdetail?confCompDetail_uuid=" + confCompDetail_uuid + "&curPage=outline");
		ObjectNode totalNode = om.createObjectNode();
		// ArrayNode iplist = om.createArrayNode();
		totalNode.put("confCompDetail_submited_by", retJobDetail.get("confCompDetail_submited_by").asText());
		totalNode.put("confCompDetail_status", retJobDetail.get("confCompDetail_status").asText());
		// iplist.addPOJO(lrrsb);
		totalNode.putPOJO("iplist", lrrsb);
		return totalNode;
	}

	@RequestMapping("/getIPConfigInfo")
	@ResponseBody
	public Set<String> getIPConfigInfo(HttpServletRequest request, HttpSession session) throws ParseException {
		String type = request.getParameter("type");
		String product = request.getParameter("product");// 属于db2 mq was ?
		if (type != null && type.equals("all"))// 获取IP的所有信息
		{
			String ip = request.getParameter("ip");
			if (ip == null || ip.equals("null")) {
				
				return new HashSet<String>();
			} else {
				String retMsg = amsRestService.getList_get(null, null, "/api/v1/getconfigInfo?ip=" + ip);
				JsonNode retJson = null;
				try {
					retJson = om.readTree(retMsg.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info(retJson.toString());
				String msgNodestr = retJson.get("msg").asText();

				Set<String> proSet = new HashSet<String>();// 专门放唯一产品的treeset
				try {
					JsonNode msgNode = om.readTree(msgNodestr);
					if (msgNode instanceof ArrayNode) {
						ArrayNode msgNodeArr = (ArrayNode) msgNode;
						request.getSession().setAttribute("msgNodeArr", msgNodeArr);// 将完整对象放入session
						for (JsonNode jn : msgNodeArr) // 取出product 信息放入proSet
						{
							String retJsonStr = jn.get("confCompJobRunResult_retJson").asText();
							JsonNode confCompJobRunResult_retJson = om.readTree(retJsonStr);

							proSet.add(confCompJobRunResult_retJson.get("product").asText());
						}
					}
					logger.info(proSet.toString());
					return proSet;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			// 这么多else if 是便于了解整个模块的流程 ，完全可以一个方法搞定
		} else if (type.equals("version"))// 产品、IP、 来获取version
		{
			return convertJsonToSet(request, type, product);
			// return null;
		} else if (type.equals("datetime")) // 产品 ip 版本来获取datetime
		{
			Set<String> datetimeset = new HashSet<String>();// 专门放唯一产品的treeset
			ArrayNode msgNodeArr = (ArrayNode) request.getSession().getAttribute("msgNodeArr");
			for (JsonNode jn : msgNodeArr) {
				try {
					String retJsonStr = jn.get("confCompJobRunResult_retJson").asText();
					JsonNode retJson = om.readTree(retJsonStr);
					String pro = retJson.get("product").asText();
					if (pro.equalsIgnoreCase(product))
						datetimeset.add(jn.get("confCompJobRunResult_datetime").asText());
				} catch (Exception e) {
					logger.error("getIPConfigInfo::获取datetime出错，出错原因为:" + e.getMessage());
				}
			}
			return datetimeset;
			// return convertJsonToSet(request,type);
		} else if (type.equals("instance"))// 获取实例
		{
			return convertJsonToSet(request, type, product);
		} else if (type.equals("database")) // 如果是db2 还要获取db 信息
		{
			return convertJsonToSet(request, type, product);
		} else {
			return null;
		}
	}

	public Set<String> convertJsonToSet(HttpServletRequest request, String type, String product) {
		Set<String> verSet = new HashSet<String>();// 专门放唯一的hashset
		ArrayNode msgNodeArr = (ArrayNode) request.getSession().getAttribute("msgNodeArr");
		for (JsonNode jn : msgNodeArr) {
			try {
				String retJsonStr = jn.get("confCompJobRunResult_retJson").asText();
				JsonNode confCompJobRunResult_retJson = om.readTree(retJsonStr);
				String pro = confCompJobRunResult_retJson.get("product").asText();// 是哪个产品
				JsonNode verNodes = confCompJobRunResult_retJson.get("data");
				if (pro.equalsIgnoreCase(product)) {
					if (verNodes instanceof ArrayNode) {
						ArrayNode verArrNodes = (ArrayNode) verNodes;
						for (JsonNode ver : verArrNodes) {
							// verSet.add(ver.get("version").asText());
							if (type.equals("instance")) {
								if (ver.get("type").asText().equalsIgnoreCase("qmgr"))
									verSet.add(ver.get("name").asText());
								if (ver.get("type").asText().equalsIgnoreCase("instance"))
									verSet.add(ver.get("name").asText());
							} else if (type.equals("database")) {
								if (ver.get("type").asText().equalsIgnoreCase("database"))
									verSet.add(ver.get("name").asText());
							} else
								verSet.add(ver.get(type).asText());

						}
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return verSet;
	}

	/*
	 * 这个是两个选择比对信息后的比较方法
	 */
	@RequestMapping("/compareConf.do")
	public String compareConf(HttpServletRequest request) throws ParseException {
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_compareConf);
		String srcDB = request.getParameter("srcDB");
		request.setAttribute("srcDB", srcDB);//为了不显示DBinfo用
		configCompareBean ccb = new configCompareBean();
		try {
			try {
				if (request.getSession().getAttribute("param") == null ||  request.getMethod().equalsIgnoreCase("post")) {
					BeanUtils.populate(ccb, request.getParameterMap());
					BeanUtils.copyProperty(ccb, "configCompareBean_uuid", UUID.randomUUID().toString());
					request.getSession().setAttribute("param", ccb); // 主要用于重复提交刷新页面
				} 
				if (request.getMethod().equalsIgnoreCase("get")) {
					ccb = (configCompareBean) request.getSession().getAttribute("param");
				}
			} catch (IllegalAccessException | InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, ccb.toString());
			JsonNode retNode = om.readTree(retMsg);
			JsonNode msgNodeArr = om.readTree(retNode.get("msg").asText());
			if (ccb.getSrcProduct().equalsIgnoreCase("mq"))
				return createMQInfo(ccb, msgNodeArr, request);
			else if (ccb.getSrcProduct().equalsIgnoreCase("db2")) {
				return createDB2Info(ccb, msgNodeArr, request);
			} else if (ccb.getSrcProduct().equalsIgnoreCase("was")) {
				return "config_WAS_Compare_Summary";
			}

		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 跳转到mq的配置比较页面，参数太多封装到一个方法中
	 * 
	 * @param ccb
	 * @param msgNodeArr
	 * @param request
	 * @return
	 */
	public String createMQInfo(configCompareBean ccb, JsonNode msgNodeArr, HttpServletRequest request) // pro
																										// mq
																										// db2
																										// was
	{
		List<MQconfigCompSummaryBean> lmqcsb = new ArrayList<MQconfigCompSummaryBean>();
		if (msgNodeArr instanceof ArrayNode) {
			ArrayNode an = (ArrayNode) msgNodeArr;
			for (JsonNode jn : an) {
				MQconfigCompSummaryBean mqccsb = new MQconfigCompSummaryBean();
				
				mqccsb.setClntRcvBuffSize(
						jn.get("confCompJobRunResult_retJson").get("config").get("ClntRcvBuffSize").asText());
				mqccsb.setName(jn.get("confCompJobRunResult_retJson").get("name").asText());
				try {
					mqccsb.setCompDate(convertDatetime(jn.get("confCompJobRunResult_datetime").asText()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("compareConf 转换时间出错" + e.getMessage());
					mqccsb.setCompDate(jn.get("confCompJobRunResult_datetime").asText());
				}
				mqccsb.setClntSndBuffSize(
						jn.get("confCompJobRunResult_retJson").get("config").get("ClntSndBuffSize").asText());
				mqccsb.setKeepAlive(jn.get("confCompJobRunResult_retJson").get("config").get("KeepAlive").asText());
				mqccsb.setLogFilePages(
						jn.get("confCompJobRunResult_retJson").get("config").get("LogFilePages").asText());
				mqccsb.setLogSecondaryFiles(
						jn.get("confCompJobRunResult_retJson").get("config").get("LogSecondaryFiles").asText());
				mqccsb.setLogPrimaryFiles(
						jn.get("confCompJobRunResult_retJson").get("config").get("LogPrimaryFiles").asText());
				mqccsb.setLogType(jn.get("confCompJobRunResult_retJson").get("config").get("LogType").asText());
				mqccsb.setMaxActiveChannels(
						jn.get("confCompJobRunResult_retJson").get("config").get("MaxActiveChannels").asText());
				mqccsb.setMaxChannels(jn.get("confCompJobRunResult_retJson").get("config").get("MaxChannels").asText());
				mqccsb.setRcvBuffSize(jn.get("confCompJobRunResult_retJson").get("config").get("RcvBuffSize").asText());
				mqccsb.setRcvRcvBuffSize(
						jn.get("confCompJobRunResult_retJson").get("config").get("RcvRcvBuffSize").asText());
				mqccsb.setRcvSndBuffSize(
						jn.get("confCompJobRunResult_retJson").get("config").get("RcvSndBuffSize").asText());
				mqccsb.setSndBuffSize(jn.get("confCompJobRunResult_retJson").get("config").get("SndBuffSize").asText());
				mqccsb.setSvrRcvBuffSize(
						jn.get("confCompJobRunResult_retJson").get("config").get("RcvBuffSize").asText());
				mqccsb.setSvrSndBuffSize(
						jn.get("confCompJobRunResult_retJson").get("config").get("SvrRcvBuffSize").asText());
				lmqcsb.add(mqccsb);
			}
		}
		request.setAttribute("mqcclistbase64", StringUtil.encodeString((lmqcsb.toString())));

		return "config_MQ_Compare_Summary";

	}

	/**
	 * 跳转到DB2的配置比较页面，参数太多封装到一个方法中
	 * 
	 * @param ccb
	 * @param msgNodeArr
	 * @param request
	 * @return
	 */
	public String createDB2Info(configCompareBean ccb, JsonNode msgNodeArr, HttpServletRequest request) // pro
																										// mq
																										// db2
																										// )
	{
		List<DB2InstanceConfigCompSummaryBean> ldb2ccsb = new ArrayList<DB2InstanceConfigCompSummaryBean>();
		List<DB2DataBaseConfigCompSummaryBean> ldb2dbccsb = new ArrayList<DB2DataBaseConfigCompSummaryBean>();
		if (ccb.getSrcDB().equals("-") && ccb.getTargetDB().equals("-")) // 先判断是选择到了instance
																			// 还是连同db
																			// 一起选了。-表示没有选择DB
		{// 只处理到instance
			if (msgNodeArr instanceof ArrayNode) {
				ArrayNode an = (ArrayNode) msgNodeArr;
				for (JsonNode jn1 : an) {
					DB2InstanceConfigCompSummaryBean db2ccsb = new DB2InstanceConfigCompSummaryBean();
					if (jn1.get("confCompJobRunResult_retJson").get("type").asText().equalsIgnoreCase("instance"))// 实例
					{
						JsonNode jn = jn1.get("confCompJobRunResult_retJson");
						db2ccsb.setName(jn.get("name").asText());
						try {
							db2ccsb.setCompDate(convertDatetime(jn1.get("confCompJobRunResult_datetime").asText()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error("compareConf 转换时间出错" + e.getMessage());
							db2ccsb.setCompDate(jn.get("confCompJobRunResult_datetime").asText());
						}
						db2ccsb.setSYSMON_GROUP(jn.get("config").get("SYSMON_GROUP (memory)   ").asText());
						db2ccsb.setRESTBUFSZ(jn.get("config").get("RESTBUFSZ (4KB)").asText());
						db2ccsb.setDIAGPATH(jn.get("config").get("DIAGPATH (memory)").asText());
						db2ccsb.setASLHEAPSZ(jn.get("config").get("ASLHEAPSZ (4KB)").asText());
						db2ccsb.setMAX_CONNECTIONS(jn.get("config").get("MAX_CONNECTIONS").asText());
						db2ccsb.setBACKBUFSZ(jn.get("config").get("BACKBUFSZ (4KB)").asText());
						db2ccsb.setINTRA_PARALLEL(jn.get("config").get("INTRA_PARALLEL").asText());
						db2ccsb.setUTIL_IMPACT_LIM(jn.get("config").get("UTIL_IMPACT_LIM").asText());
						db2ccsb.setALT_DIAGPATH(jn.get("config").get("ALT_DIAGPATH (memory)").asText());
						db2ccsb.setFENCED_POOL(jn.get("config").get("FENCED_POOL").asText());
						db2ccsb.setINSTANCE_MEMORY(jn.get("config").get("INSTANCE_MEMORY (4KB)").asText());
						db2ccsb.setSYSMAINT_GROUP(jn.get("config").get("SYSMAINT_GROUP (memory) ").asText());
						db2ccsb.setMAX_QUERYDEGREE(jn.get("config").get("MAX_QUERYDEGREE").asText());
						db2ccsb.setSYSCTRL_GROUP(jn.get("config").get("SYSCTRL_GROUP (memory)").asText());
						db2ccsb.setDFT_MON_STMT(jn.get("config").get("DFT_MON_STMT").asText());
						db2ccsb.setNOTIFYLEVEL(jn.get("config").get("NOTIFYLEVEL").asText());
						db2ccsb.setDFT_MON_LOCK(jn.get("config").get("DFT_MON_LOCK").asText());
						db2ccsb.setDFT_MON_BUFPOOL(jn.get("config").get("DFT_MON_BUFPOOL").asText());
						db2ccsb.setDFT_MON_SORT(jn.get("config").get("DFT_MON_SORT").asText());
						db2ccsb.setDIAGSIZE(jn.get("config").get("DIAGSIZE (MB)").asText());
						db2ccsb.setAUTHENTICATION(jn.get("config").get("AUTHENTICATION").asText());
						db2ccsb.setSYSADM_GROUP(jn.get("config").get("SYSADM_GROUP (memory)").asText());
						db2ccsb.setDIAGLEVEL(jn.get("config").get("DIAGLEVEL").asText());
						db2ccsb.setDFT_MON_TABLE(jn.get("config").get("DFT_MON_TABLE").asText());
						db2ccsb.setDISCOVER(jn.get("config").get("DISCOVER").asText());
						db2ccsb.setJAVA_HEAP_SZ(jn.get("config").get("JAVA_HEAP_SZ (4KB)").asText());
						db2ccsb.setMAX_COORDAGENTS(jn.get("config").get("MAX_COORDAGENTS").asText());
						db2ccsb.setINDEXREC(jn.get("config").get("INDEXREC").asText());
						db2ccsb.setDFT_MON_UOW(jn.get("config").get("DFT_MON_UOW").asText());
						db2ccsb.setHEALTH_MON(jn.get("config").get("HEALTH_MON").asText());
						db2ccsb.setRESYNC_INTERVAL(jn.get("config").get("RESYNC_INTERVAL (secs)").asText());
						db2ccsb.setSHEAPTHRES(jn.get("config").get("SHEAPTHRES (4KB)").asText());
						db2ccsb.setAUDIT_BUF_SZ(jn.get("config").get("AUDIT_BUF_SZ (4KB)").asText());
						db2ccsb.setNUM_INITAGENTS(jn.get("config").get("NUM_INITAGENTS").asText());
						db2ccsb.setQUERY_HEAP_SZ(jn.get("config").get("QUERY_HEAP_SZ (4KB)").asText());
						db2ccsb.setDFT_MON_TIMESTAMP(jn.get("config").get("DFT_MON_TIMESTAMP").asText());
						db2ccsb.setRQRIOBLK(jn.get("config").get("RQRIOBLK (bytes)").asText());
						db2ccsb.setKEEPFENCED(jn.get("config").get("KEEPFENCED").asText());
						db2ccsb.setNUM_POOLAGENTS(jn.get("config").get("NUM_POOLAGENTS").asText());
						db2ccsb.setNUM_INITFENCED(jn.get("config").get("NUM_INITFENCED").asText());
						db2ccsb.setDFTDBPATH(jn.get("config").get("DFTDBPATH (memory)").asText());
						db2ccsb.setMON_HEAP_SZ(jn.get("config").get("MON_HEAP_SZ (4KB)").asText());
						db2ccsb.setAGENTPRI(jn.get("config").get("AGENTPRI").asText());
						db2ccsb.setDISCOVER_INST(jn.get("config").get("DISCOVER_INST").asText());
						db2ccsb.setSVCENAME(jn.get("config").get("SVCENAME").asText());

						ldb2ccsb.add(db2ccsb);
					}
					

				}
			}
			
		} else {  //处理到instance 和db 信息

			if (msgNodeArr instanceof ArrayNode) {
				ArrayNode an = (ArrayNode) msgNodeArr;
				for (JsonNode jn1 : an) {
					DB2InstanceConfigCompSummaryBean db2ccsb = new DB2InstanceConfigCompSummaryBean();
					DB2DataBaseConfigCompSummaryBean db2dbccsb = new DB2DataBaseConfigCompSummaryBean();
					if (jn1.get("confCompJobRunResult_retJson").get("type").asText().equalsIgnoreCase("instance"))// 实例
					{
						JsonNode jn = jn1.get("confCompJobRunResult_retJson");
						db2ccsb.setName(jn.get("name").asText());
						try {
							db2ccsb.setCompDate(convertDatetime(jn1.get("confCompJobRunResult_datetime").asText()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error("compareConf 转换时间出错" + e.getMessage());
							db2ccsb.setCompDate(jn.get("confCompJobRunResult_datetime").asText());
						}
						db2ccsb.setSYSMON_GROUP(jn.get("config").get("SYSMON_GROUP (memory)   ").asText());
						db2ccsb.setRESTBUFSZ(jn.get("config").get("RESTBUFSZ (4KB)").asText());
						db2ccsb.setDIAGPATH(jn.get("config").get("DIAGPATH (memory)").asText());
						db2ccsb.setASLHEAPSZ(jn.get("config").get("ASLHEAPSZ (4KB)").asText());
						db2ccsb.setMAX_CONNECTIONS(jn.get("config").get("MAX_CONNECTIONS").asText());
						db2ccsb.setBACKBUFSZ(jn.get("config").get("BACKBUFSZ (4KB)").asText());
						db2ccsb.setINTRA_PARALLEL(jn.get("config").get("INTRA_PARALLEL").asText());
						db2ccsb.setUTIL_IMPACT_LIM(jn.get("config").get("UTIL_IMPACT_LIM").asText());
						db2ccsb.setALT_DIAGPATH(jn.get("config").get("ALT_DIAGPATH (memory)").asText());
						db2ccsb.setFENCED_POOL(jn.get("config").get("FENCED_POOL").asText());
						db2ccsb.setINSTANCE_MEMORY(jn.get("config").get("INSTANCE_MEMORY (4KB)").asText());
						db2ccsb.setSYSMAINT_GROUP(jn.get("config").get("SYSMAINT_GROUP (memory) ").asText());
						db2ccsb.setMAX_QUERYDEGREE(jn.get("config").get("MAX_QUERYDEGREE").asText());
						db2ccsb.setSYSCTRL_GROUP(jn.get("config").get("SYSCTRL_GROUP (memory)").asText());
						db2ccsb.setDFT_MON_STMT(jn.get("config").get("DFT_MON_STMT").asText());
						db2ccsb.setNOTIFYLEVEL(jn.get("config").get("NOTIFYLEVEL").asText());
						db2ccsb.setDFT_MON_LOCK(jn.get("config").get("DFT_MON_LOCK").asText());
						db2ccsb.setDFT_MON_BUFPOOL(jn.get("config").get("DFT_MON_BUFPOOL").asText());
						db2ccsb.setDFT_MON_SORT(jn.get("config").get("DFT_MON_SORT").asText());
						db2ccsb.setDIAGSIZE(jn.get("config").get("DIAGSIZE (MB)").asText());
						db2ccsb.setAUTHENTICATION(jn.get("config").get("AUTHENTICATION").asText());
						db2ccsb.setSYSADM_GROUP(jn.get("config").get("SYSADM_GROUP (memory)").asText());
						db2ccsb.setDIAGLEVEL(jn.get("config").get("DIAGLEVEL").asText());
						db2ccsb.setDFT_MON_TABLE(jn.get("config").get("DFT_MON_TABLE").asText());
						db2ccsb.setDISCOVER(jn.get("config").get("DISCOVER").asText());
						db2ccsb.setJAVA_HEAP_SZ(jn.get("config").get("JAVA_HEAP_SZ (4KB)").asText());
						db2ccsb.setMAX_COORDAGENTS(jn.get("config").get("MAX_COORDAGENTS").asText());
						db2ccsb.setINDEXREC(jn.get("config").get("INDEXREC").asText());
						db2ccsb.setDFT_MON_UOW(jn.get("config").get("DFT_MON_UOW").asText());
						db2ccsb.setHEALTH_MON(jn.get("config").get("HEALTH_MON").asText());
						db2ccsb.setRESYNC_INTERVAL(jn.get("config").get("RESYNC_INTERVAL (secs)").asText());
						db2ccsb.setSHEAPTHRES(jn.get("config").get("SHEAPTHRES (4KB)").asText());
						db2ccsb.setAUDIT_BUF_SZ(jn.get("config").get("AUDIT_BUF_SZ (4KB)").asText());
						db2ccsb.setNUM_INITAGENTS(jn.get("config").get("NUM_INITAGENTS").asText());
						db2ccsb.setQUERY_HEAP_SZ(jn.get("config").get("QUERY_HEAP_SZ (4KB)").asText());
						db2ccsb.setDFT_MON_TIMESTAMP(jn.get("config").get("DFT_MON_TIMESTAMP").asText());
						db2ccsb.setRQRIOBLK(jn.get("config").get("RQRIOBLK (bytes)").asText());
						db2ccsb.setKEEPFENCED(jn.get("config").get("KEEPFENCED").asText());
						db2ccsb.setNUM_POOLAGENTS(jn.get("config").get("NUM_POOLAGENTS").asText());
						db2ccsb.setNUM_INITFENCED(jn.get("config").get("NUM_INITFENCED").asText());
						db2ccsb.setDFTDBPATH(jn.get("config").get("DFTDBPATH (memory)").asText());
						db2ccsb.setMON_HEAP_SZ(jn.get("config").get("MON_HEAP_SZ (4KB)").asText());
						db2ccsb.setAGENTPRI(jn.get("config").get("AGENTPRI").asText());
						db2ccsb.setDISCOVER_INST(jn.get("config").get("DISCOVER_INST").asText());
						db2ccsb.setSVCENAME(jn.get("config").get("SVCENAME").asText());

						ldb2ccsb.add(db2ccsb);
					}
					if (jn1.get("confCompJobRunResult_retJson").get("type").asText().equalsIgnoreCase("database")) // db信息
					{
						JsonNode jn = jn1.get("confCompJobRunResult_retJson");
						db2dbccsb.setName(jn.get("name").asText());
						try {
							db2dbccsb.setCompDate(convertDatetime(jn1.get("confCompJobRunResult_datetime").asText()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error("compareConf 转换时间出错" + e.getMessage());
							db2dbccsb.setCompDate(jn.get("confCompJobRunResult_datetime").asText());
						}
						db2dbccsb.setParent(jn.get("parent").asText());
						db2dbccsb.setENABLE_XMLCHAR(jn.get("config").get("ENABLE_XMLCHAR").asText());
						db2dbccsb.setAUTORESTART(jn.get("config").get("AUTORESTART").asText());
						db2dbccsb.setLOCKTIMEOUT(jn.get("config").get("LOCKTIMEOUT (secs)").asText());
						db2dbccsb.setAUTO_PROF_UPD(jn.get("config").get("AUTO_PROF_UPD").asText());
						db2dbccsb.setLog_retain_for_recovery_status(
								jn.get("config").get("Log retain for recovery status").asText());
						db2dbccsb.setMINCOMMIT(jn.get("config").get("MINCOMMIT").asText());
						db2dbccsb.setREC_HIS_RETENTN(jn.get("config").get("REC_HIS_RETENTN (days)").asText());
						db2dbccsb.setUser_exit_for_logging_status(
								jn.get("config").get("User exit for logging status").asText());
						db2dbccsb.setUTIL_HEAP_SZ(jn.get("config").get("UTIL_HEAP_SZ (4KB)").asText());
						db2dbccsb.setDFT_SQLMATHWARN(jn.get("config").get("DFT_SQLMATHWARN").asText());
						db2dbccsb.setHADR_database_role(jn.get("config").get("HADR database role").asText());
						db2dbccsb.setSELF_TUNING_MEM(jn.get("config").get("SELF_TUNING_MEM").asText());
						db2dbccsb.setSEQDETECT(jn.get("config").get("SEQDETECT").asText());
						db2dbccsb.setTSM_MGMTCLASS(jn.get("config").get("TSM_MGMTCLASS").asText());
						db2dbccsb.setMAX_LOG(jn.get("config").get("MAX_LOG").asText());
						db2dbccsb.setAUTO_REVAL(jn.get("config").get("AUTO_REVAL").asText());
						db2dbccsb.setHADR_TIMEOUT(jn.get("config").get("HADR_TIMEOUT").asText());
						db2dbccsb.setAUTO_DEL_REC_OBJ(jn.get("config").get("AUTO_DEL_REC_OBJ").asText());
						db2dbccsb.setNUM_QUANTILES(jn.get("config").get("NUM_QUANTILES").asText());
						db2dbccsb.setHADR_REMOTE_SVC(jn.get("config").get("HADR_REMOTE_SVC").asText());
						db2dbccsb.setMON_PKGLIST_SZ(jn.get("config").get("MON_PKGLIST_SZ").asText());
						db2dbccsb.setNUM_DB_BACKUPS(jn.get("config").get("NUM_DB_BACKUPS").asText());
						db2dbccsb.setVARCHAR2_COMPAT(jn.get("config").get("VARCHAR2_COMPAT").asText());
						db2dbccsb.setDB_MEM_THRESH(jn.get("config").get("DB_MEM_THRESH").asText());
						db2dbccsb.setAUTO_REORG(jn.get("config").get("AUTO_REORG").asText());
						db2dbccsb.setARCHRETRYDELAY(jn.get("config").get("ARCHRETRYDELAY (secs)").asText());
						db2dbccsb.setVENDOROPT(jn.get("config").get("VENDOROPT").asText());
						db2dbccsb.setDFT_EXTENT_SZ(jn.get("config").get("DFT_EXTENT_SZ (pages)").asText());
						db2dbccsb.setNUM_FREQVALUES(jn.get("config").get("NUM_FREQVALUES").asText());
						db2dbccsb.setMAXAPPLS(jn.get("config").get("MAXAPPLS").asText());
						db2dbccsb.setDECFLT_ROUNDING(jn.get("config").get("DECFLT_ROUNDING").asText());
						db2dbccsb.setMON_OBJ_METRICS(jn.get("config").get("MON_OBJ_METRICS").asText());
						db2dbccsb.setAUTO_MAINT(jn.get("config").get("AUTO_MAINT").asText());
						db2dbccsb.setALT_COLLATE(jn.get("config").get("ALT_COLLATE").asText());
						db2dbccsb.setDBHEAP(jn.get("config").get("DBHEAP (4KB)").asText());
						db2dbccsb.setNUMBER_COMPAT(jn.get("config").get("NUMBER_COMPAT").asText());
						db2dbccsb.setDISCOVER_DB(jn.get("config").get("DISCOVER_DB").asText());
						db2dbccsb.setINDEXREC(jn.get("config").get("INDEXREC").asText());
						db2dbccsb.setMAXLOCKS(jn.get("config").get("MAXLOCKS").asText());
						db2dbccsb.setDefault_number_of_containers(
								jn.get("config").get("Default number of containers").asText());
						db2dbccsb.setBLK_LOG_DSK_FUL(jn.get("config").get("BLK_LOG_DSK_FUL").asText());
						db2dbccsb.setTSM_PASSWORD(jn.get("config").get("TSM_PASSWORD").asText());
						db2dbccsb.setLOGRETAIN(jn.get("config").get("LOGRETAIN").asText());
						db2dbccsb.setBUFFPAGE(jn.get("config").get("BUFFPAGE").asText());
						db2dbccsb.setSTMTHEAP(jn.get("config").get("STMTHEAP (4KB)").asText());
						db2dbccsb.setLOGPRIMARY(jn.get("config").get("LOGPRIMARY").asText());
						db2dbccsb.setDatabase_territory(jn.get("config").get("Database territory").asText());
						db2dbccsb.setDatabase_code_page(jn.get("config").get("Database code page").asText());
						db2dbccsb.setDFT_QUERYOPT(jn.get("config").get("DFT_QUERYOPT").asText());
						db2dbccsb.setDatabase_country_region_code(
								jn.get("config").get("Database country/region code").asText());
						db2dbccsb.setFirst_active_log_file(jn.get("config").get("First active log file").asText());
						db2dbccsb.setLOGARCHMETH1(jn.get("config").get("LOGARCHMETH1 (memory)").asText());
						db2dbccsb.setCHNGPGS_THRESH(jn.get("config").get("CHNGPGS_THRESH").asText());
						db2dbccsb.setDFT_LOADREC_SES(jn.get("config").get("DFT_LOADREC_SES").asText());
						db2dbccsb.setTSM_OWNER(jn.get("config").get("TSM_OWNER").asText());
						db2dbccsb.setAUTO_STATS_PROF(jn.get("config").get("AUTO_STATS_PROF").asText());
						db2dbccsb.setDEC_TO_CHAR_FMT(jn.get("config").get("DEC_TO_CHAR_FMT").asText());
						db2dbccsb.setCATALOGCACHE_SZ(jn.get("config").get("CATALOGCACHE_SZ (4KB)").asText());
						db2dbccsb.setPath_to_log_files(jn.get("config").get("Path to log files (memory)").asText());
						db2dbccsb.setDFT_DEGREE(jn.get("config").get("DFT_DEGREE").asText());
						db2dbccsb.setLOGFILSIZ(jn.get("config").get("LOGFILSIZ (4KB)").asText());
						db2dbccsb.setBackup_pending(jn.get("config").get("Backup pending").asText());
						db2dbccsb.setSHEAPTHRES_SHR(jn.get("config").get("SHEAPTHRES_SHR (4KB)").asText());
						db2dbccsb.setRestrict_access(jn.get("config").get("Restrict access").asText());
						db2dbccsb.setStatement_concentrator(jn.get("config").get("Statement concentrator").asText());
						db2dbccsb.setDATE_COMPAT(jn.get("config").get("DATE_COMPAT").asText());
						db2dbccsb.setHADR_PEER_WINDOW(jn.get("config").get("HADR_PEER_WINDOW (secs)").asText());
						db2dbccsb.setMAXFILOP(jn.get("config").get("MAXFILOP").asText());
						db2dbccsb.setLOGSECOND(jn.get("config").get("LOGSECOND").asText());
						db2dbccsb.setAUTO_TBL_MAINT(jn.get("config").get("AUTO_TBL_MAINT").asText());
						db2dbccsb.setHADR_REMOTE_INST(jn.get("config").get("HADR_REMOTE_INST").asText());
						db2dbccsb.setAUTO_DB_BACKUP(jn.get("config").get("AUTO_DB_BACKUP").asText());
						db2dbccsb.setDFT_PREFETCH_SZ(jn.get("config").get("DFT_PREFETCH_SZ (pages)").asText());
						db2dbccsb.setMON_LOCKWAIT(jn.get("config").get("MON_LOCKWAIT").asText());
						db2dbccsb.setNUM_IOCLEANERS(jn.get("config").get("NUM_IOCLEANERS").asText());
						db2dbccsb.setNUM_LOG_SPAN(jn.get("config").get("NUM_LOG_SPAN").asText());
						db2dbccsb.setMON_LOCKTIMEOUT(jn.get("config").get("MON_LOCKTIMEOUT").asText());
						db2dbccsb.setTRACKMOD(jn.get("config").get("TRACKMOD").asText());
						db2dbccsb.setNUM_IOSERVERS(jn.get("config").get("NUM_IOSERVERS").asText());
						db2dbccsb.setLOGINDEXBUILD(jn.get("config").get("LOGINDEXBUILD").asText());
						db2dbccsb.setMON_LCK_MSG_LVL(jn.get("config").get("MON_LCK_MSG_LVL").asText());
						db2dbccsb.setMON_REQ_METRICS(jn.get("config").get("MON_REQ_METRICS").asText());
						db2dbccsb.setLOGARCHMETH2(jn.get("config").get("LOGARCHMETH2 (memory)").asText());
						db2dbccsb.setDLCHKTIME(jn.get("config").get("DLCHKTIME (ms)").asText());
						db2dbccsb.setSOFTMAX(jn.get("config").get("SOFTMAX").asText());
						db2dbccsb.setMIRRORLOGPATH(jn.get("config").get("MIRRORLOGPATH (memory)").asText());
						db2dbccsb.setMultipage_File_alloc_enabled(
								jn.get("config").get("Multi-page File alloc enabled").asText());
						db2dbccsb.setNUMARCHRETRY(jn.get("config").get("NUMARCHRETRY").asText());
						db2dbccsb.setAUTO_STMT_STATS(jn.get("config").get("AUTO_STMT_STATS").asText());
						db2dbccsb.setFAILARCHPATH(jn.get("config").get("FAILARCHPATH (memory)").asText());
						db2dbccsb.setUSEREXIT(jn.get("config").get("USEREXIT").asText());
						db2dbccsb.setDatabase_page_size(jn.get("config").get("Database page size").asText());
						db2dbccsb.setHADR_LOCAL_HOST(jn.get("config").get("HADR_LOCAL_HOST").asText());
						db2dbccsb.setMON_ACT_METRICS(jn.get("config").get("MON_ACT_METRICS").asText());
						db2dbccsb.setAVG_APPLS(jn.get("config").get("AVG_APPLS").asText());
						db2dbccsb.setWLM_COLLECT_INT(jn.get("config").get("WLM_COLLECT_INT (mins)").asText());
						db2dbccsb.setLOGBUFSZ(jn.get("config").get("LOGBUFSZ (4KB)").asText());
						db2dbccsb.setDFT_MTTB_TYPES(jn.get("config").get("DFT_MTTB_TYPES").asText());
						db2dbccsb.setDatabase_is_consistent(jn.get("config").get("Database is consistent").asText());
						db2dbccsb.setSTAT_HEAP_SZ(jn.get("config").get("STAT_HEAP_SZ (4KB)").asText());
						db2dbccsb.setDatabase_code_set(jn.get("config").get("Database code set").asText());
						db2dbccsb.setSORTHEAP(jn.get("config").get("SORTHEAP (4KB)").asText());
						db2dbccsb.setCUR_COMMIT(jn.get("config").get("CUR_COMMIT").asText());
						db2dbccsb.setMON_LW_THRESH(jn.get("config").get("MON_LW_THRESH").asText());
						db2dbccsb.setDATABASE_MEMORY(jn.get("config").get("DATABASE_MEMORY (4KB)").asText());
						db2dbccsb.setMON_UOW_DATA(jn.get("config").get("MON_UOW_DATA").asText());
						db2dbccsb.setBLOCKNONLOGGED(jn.get("config").get("BLOCKNONLOGGED").asText());
						db2dbccsb.setTSM_NODENAME(jn.get("config").get("TSM_NODENAME").asText());
						db2dbccsb.setPCKCACHESZ(jn.get("config").get("PCKCACHESZ (4KB)").asText());
						db2dbccsb.setHADR_REMOTE_HOST(jn.get("config").get("HADR_REMOTE_HOST").asText());
						db2dbccsb.setAPPL_MEMORY(jn.get("config").get("APPL_MEMORY (4KB)").asText());
						db2dbccsb.setRestore_pending(jn.get("config").get("Restore pending").asText());
						db2dbccsb.setDFT_REFRESH_AGE(jn.get("config").get("DFT_REFRESH_AGE").asText());
						db2dbccsb.setHADR_SYNCMODE(jn.get("config").get("HADR_SYNCMODE").asText());
						db2dbccsb.setOVERFLOWLOGPATH(jn.get("config").get("OVERFLOWLOGPATH (memory)").asText());
						db2dbccsb.setMON_DEADLOCK(jn.get("config").get("MON_DEADLOCK").asText());
						db2dbccsb.setAUTO_RUNSTATS(jn.get("config").get("AUTO_RUNSTATS").asText());
						db2dbccsb.setLOCKLIST(jn.get("config").get("LOCKLIST (4KB)").asText());
						db2dbccsb.setRollforward_pending(jn.get("config").get("Rollforward pending").asText());
						db2dbccsb.setDatabase_collating_sequence(
								jn.get("config").get("Database collating sequence").asText());
						db2dbccsb.setNEWLOGPATH(jn.get("config").get("NEWLOGPATH (memory)").asText());
						db2dbccsb.setDYN_QUERY_MGMT(jn.get("config").get("DYN_QUERY_MGMT").asText());
						db2dbccsb.setINDEXSORT(jn.get("config").get("INDEXSORT").asText());
						db2dbccsb.setAPPLHEAPSZ(jn.get("config").get("APPLHEAPSZ (4KB)").asText());
						db2dbccsb.setHADR_LOCAL_SVC(jn.get("config").get("HADR_LOCAL_SVC").asText());

						ldb2dbccsb.add(db2dbccsb);
					}

				}
			}
		}
		request.setAttribute("db2cclistbase64", StringUtil.encodeString(ldb2ccsb.toString()));
		if(ldb2ccsb.size() == 0 )//表示没有选择db信息
			request.setAttribute("db2dbcclistbase64", new ArrayList<>());
		else
		request.setAttribute("db2dbcclistbase64", StringUtil.encodeString(ldb2dbccsb.toString()));
		return "config_DB2_Compare_Summary";
	}

	@RequestMapping("/deleteConfCompJob.do")
	@ResponseBody
	public String deleteConfCompJob(HttpServletRequest request, HttpSession session) {
		String strOrgUrl = serverService.createSendUrl(PropertyKeyConst.AMS2_HOST,
				PropertyKeyConst.POST_ams2_configCompare_jobs);
		String job_uuid = request.getParameter("job_uuid");// 巡检任务类型
		ObjectNode delete_param = om.createObjectNode();
		delete_param.put("job_uuid", job_uuid);
		delete_param.put("operType", "deleteCfgCompJob");
		try {
			String retMsg = HttpClientUtil.postMethod(strOrgUrl, delete_param.toString());
			logger.info("deleteConfCompJob::收到数据" + retMsg);
			return retMsg;
		} catch (NetWorkException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("deleteConfCompJob::发生异常，异常为：" + e.getMessage());
		}
		return null;
	}

}

class StringComparator implements Comparator<String> {

	@Override
	public int compare(String str1, String str2) {

		return str2.compareTo(str1);// 降序;
	}

}
