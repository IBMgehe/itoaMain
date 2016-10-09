package com.ibm.automation.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ibm.automation.ams.service.AmsRestService;
import com.ibm.automation.ams.service.impl.AmsRestServiceImpl;
import com.ibm.automation.core.bean.ConfigCompareJobBean;
import com.ibm.automation.core.bean.JobsBean;
import com.ibm.automation.core.bean.LoginBean;
import com.ibm.automation.core.bean.ServersBean;
import com.ibm.automation.logcatch.LogCatchCreateBean;

public class ServerUtil {
	public static AmsRestService amsRestService = new AmsRestServiceImpl();
	private static Logger logger = Logger.getLogger(ServerUtil.class);
	// 根据选中的uuid字符串aaa,bbb翻译成[{},{}]
	public static List<ServersBean> getSelectServers(String serId) {
		List<String> serIds = new ArrayList<String>();
		if (serId != null && serId != "") {
			String[] ss = serId.split(",");
			for (int i = 0; i < ss.length; i++) {
				serIds.add(ss[i]);
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
		return listDetial;
	}
	public static List<JobsBean> getJobs(String path)
	{
		try
		{
			ArrayNode jobs_arrayNode = amsRestService.getList(null, null, path);
			List<JobsBean> jobsList = new ArrayList<JobsBean>();
			for ( JsonNode jn : jobs_arrayNode)
			{
				JobsBean jb = new JobsBean();
				jb.setJob_uuid(jn.get("job_uuid") == null ? "" : jn.get("job_uuid").asText());
				jb.setJob_type(jn.get("job_type") == null ? "" : jn.get("job_type").asText());
				jb.setJob_target(jn.get("job_target") == null ? "" : jn.get("job_target").asText());
				jb.setJob_submited_by(jn.get("job_submited_by") == null ? "" : jn.get("job_submited_by").asText());
				jb.setJob_lastrun_at(jn.get("job_lastrun_at") == null ? "" : jn.get("job_lastrun_at").asText());
				jb.setJob_scheduled_at(jn.get("job_scheduled_at") == null ? "" : jn.get("job_scheduled_at").asText());
				jb.setJob_if_daily(jn.get("job_if_daily") == null ? "" : jn.get("job_if_daily").asText());
			//	jb.setJob_status(jn.get("job_status") == null ? "" : jn.get("job_status").asText());
				jb.setJob_detail(jn.get("job_detail") == null ? "" :jn.get("job_detail").asText());
				jb.setJob_groupOrIP(jn.get("job_groupOrIP") == null ? "" :jn.get("job_groupOrIP").asText());
				jobsList.add(jb);
			}
			
			return jobsList;
		}catch(NullPointerException e )
		{
			//System.out.println("获取jobs发生空指针异常，/api/v1/jobs::" + e.getMessage());
			logger.info("获取jobs发生空指针异常，/api/v1/jobs::" + e.getMessage());
		}
		return null;
	}
	public static List<LogCatchCreateBean> getLogCatch(String path)
	{
		try {
			ArrayNode jobs_arrayNode = amsRestService.getList(null, null, path);
			List<LogCatchCreateBean> jobsList = new ArrayList<LogCatchCreateBean>();
			for ( JsonNode jn : jobs_arrayNode)
			{
				LogCatchCreateBean lccb = new LogCatchCreateBean();
				lccb.setTask_timestamp(jn.get("task_timestamp") == null ? "" : jn.get("task_timestamp").asText());
				lccb.setDatabase(jn.get("database") == null ? "" : jn.get("database").asText());
				lccb.setInstance(jn.get("instance") == null ? "" : jn.get("instance").asText());
				lccb.setIp(jn.get("ip") == null ? "" : jn.get("ip").asText());
				lccb.setProduct(jn.get("product") == null ? "" : jn.get("product").asText());
				lccb.setVersion(jn.get("version") == null ? "" : jn.get("version").asText());
				lccb.setOperation(jn.get("operation") == null ? "" : jn.get("operation").asText());
				lccb.set_id(jn.get("_id") == null ? "":jn.get("_id").asText());
				lccb.setLoc(jn.get("loc") == null ? "":jn.get("loc").asText());
				lccb.setUrl(jn.get("url") == null ? "":jn.get("url").asText());
				jobsList.add(lccb);
			}
			return jobsList;
		} catch (Exception e) {
			logger.info("获取配置比对  jobs发生空指针异常，/api/v1/logcatch::" + e.getMessage());
		}
		return null;
	}
	public static List<ConfigCompareJobBean> getConfCompJobs(String path)
	{
		try
		{
			ArrayNode jobs_arrayNode = amsRestService.getList(null, null, path);
			List<ConfigCompareJobBean> jobsList = new ArrayList<ConfigCompareJobBean>();
			for ( JsonNode jn : jobs_arrayNode)
			{
				ConfigCompareJobBean jb = new ConfigCompareJobBean();
				jb.setConfComp_uuid(jn.get("confComp_uuid") == null ? "" : jn.get("confComp_uuid").asText());
				jb.setConfComp_type(jn.get("confComp_type") == null ? "" : jn.get("confComp_type").asText());
				jb.setConfComp_target(jn.get("confComp_target") == null ? "" : jn.get("confComp_target").asText());
				jb.setConfComp_submited_by(jn.get("confComp_submited_by") == null ? "" : jn.get("confComp_submited_by").asText());
				jb.setConfComp_lastrun_at(jn.get("confComp_lastrun_at") == null ? "" : jn.get("confComp_lastrun_at").asText());
				jb.setConfComp_scheduled_at(jn.get("confComp_scheduled_at") == null ? "" : jn.get("confComp_scheduled_at").asText());
				jb.setConfComp_if_daily(jn.get("confComp_if_daily") == null ? "" : jn.get("confComp_if_daily").asText());
			//	jb.setConfComp_status(jn.get("confComp_status") == null ? "" : jn.get("confComp_status").asText());
				jb.setConfComp_detail(jn.get("confComp_detail") == null ? "" :jn.get("confComp_detail").asText());
				jb.setConfComp_groupOrIP(jn.get("confComp_groupOrIP") == null ? "" :jn.get("confComp_groupOrIP").asText());
				jobsList.add(jb);
			}
			
			return jobsList;
		}catch(NullPointerException e )
		{
			//System.out.println("获取jobs发生空指针异常，/api/v1/jobs::" + e.getMessage());
			logger.info("获取配置比对  jobs发生空指针异常，/api/v1/configjobs::" + e.getMessage());
		}
		return null;
	}
	
	
	public static List<ServersBean> getList(String path) {
		List<ServersBean> lahb = new ArrayList<ServersBean>();
		try {
			ArrayNode lists = amsRestService.getList(null, null, path);
			for (JsonNode js : lists) {
				ServersBean ahb = new ServersBean();
				ahb.setIp(js.get("ip") != null ? js.get("ip").asText() : "");
				ahb.setName(js.get("name") == null ?"":js.get("name").asText());
				ahb.setOs(js.get("os") != null ? js.get("os").asText() : "");
				ahb.setPassword(js.get("password") != null ? js.get("password").asText() : "");
				ahb.setUserid(js.get("userid") != null ? js.get("userid").asText() : "");
				ahb.setStatus(js.get("status") != null ? js.get("status").asText() : "");
				ahb.setHconf(js.get("hconf") != null ? js.get("hconf").asText() : "");
				ahb.setUuid(js.get("uuid") != null ? js.get("uuid").asText() : "");
				ahb.setHvisor(js.get("hvisor") != null ? js.get("hvisor").asText() : "");
				ahb.setProduct(js.get("product") != null ? js.get("product").asText().replace(",", "  ") : "");
				lahb.add(ahb);
			}
		} catch (NullPointerException e) {
			logger.info("发生空指针异常，odata/servers::" + e.getMessage());
		}
		Collections.sort(lahb);
		return lahb;
	}
	public  static List<LoginBean> getAllUsers(String path)
	{
		try
		{
			ArrayNode jobs_arrayNode = amsRestService.getList(null, null, path);
			List<LoginBean> loginList = new ArrayList<LoginBean>();
			for ( JsonNode jn : jobs_arrayNode)
			{
				LoginBean lb = new LoginBean();
				lb.setRole(jn.get("role") == null ? 0 : jn.get("role").asInt());
				lb.setUsername(jn.get("name") == null ? "" : jn.get("name").asText());
				lb.setEmail(jn.get("email") == null ? "" : jn.get("email").asText());
				JsonNode pros = jn.get("product");
				List<String> proList = new ArrayList<String>();
				if (pros instanceof ArrayNode)
				{
					ArrayNode an = (ArrayNode)pros;
					for(JsonNode jn1:an)
					{
						proList.add(jn1.asText());
					}
				}
				lb.setProList(proList);
				loginList.add(lb);
			}
			
			return loginList;
		}catch(NullPointerException e )
		{
			logger.info("获取配置比对  jobs发生空指针异常，/api/v1/configjobs::" + e.getMessage());
		}
		return null;
	}
}
