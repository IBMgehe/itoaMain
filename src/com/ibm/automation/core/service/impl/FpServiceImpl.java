package com.ibm.automation.core.service.impl;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.service.FpService;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.PropertyUtil;

@Service("fpService")
public class FpServiceImpl implements FpService {

	/**
	 * 
	 * */
	ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(FpServiceImpl.class);
	Properties amsCfg = PropertyUtil.getResourceFile("config/properties/ams2.properties");

	@Override
	public String getVersion(String pro, String platform) {
		// TODO Auto-generated method stub
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_fp);
		String strOrgUrl = hdiskHost + hdiskApi;
		ObjectNode outerNode = om.createObjectNode();
		outerNode.put("type", "version");
		outerNode.put("platform", platform);
		outerNode.put("pName", pro);
		if (pro.equals("mq"))
			outerNode.put("mqPath", amsCfg.getProperty("softpath") + pro);
		else if (pro.equals("was"))
			outerNode.put("wasPath", amsCfg.getProperty("softpath") + pro);
		else if (pro.equals("db2"))
			outerNode.put("db2Path", amsCfg.getProperty("softpath") + pro);
		else if (pro.equals("ihs"))
			outerNode.put("ihsPath", amsCfg.getProperty("softpath") + "was");
		else if (pro.equals("itmos"))
		{
			outerNode.put("itmosPath",amsCfg.getProperty("softpath")+"itm/os");
		}
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, outerNode.toString());
			logger.info("fpImpl收到数据" + response);
		} catch (NetWorkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("fp接口网络存在问题！" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("和fp接口网络存在问题！" + e.getMessage());
		}
		if (!response.equals("")) {

			return response;
		}
		return null;
	}

	@Override
	public String getFix(String platform, String pro, String version) {
		// TODO Auto-generated method stub
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_fp);
		String strOrgUrl = hdiskHost + hdiskApi;
		ObjectNode outerNode = om.createObjectNode();
		outerNode.put("type", "fix");
		outerNode.put("version", version);
		outerNode.put("platform", platform);
		outerNode.put("pName", pro);
		if (pro.equals("mq"))
			outerNode.put("mqPath", amsCfg.getProperty("softpath") + pro);
		else if (pro.equals("was"))
			outerNode.put("wasPath", amsCfg.getProperty("softpath") + pro);
		else if (pro.equals("db2"))
			outerNode.put("db2Path", amsCfg.getProperty("softpath") + pro);
		else if (pro.equals("ihs"))
			outerNode.put("ihsPath", amsCfg.getProperty("softpath") + "was");
		else if (pro.equals("itmos"))
		{
			outerNode.put("itmosPath", amsCfg.getProperty("softpath")+"itm/os");
		}
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, outerNode.toString());
			logger.info("fixImpl收到数据" + response);
		} catch (NetWorkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("fp接口网络存在问题！" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("和fp接口网络存在问题！" + e.getMessage());
		}
		if (!response.equals("")) {

			return response;
		}
		return null;
	}

	@Override
	public String getIm(String platform, String pro, String version) {
		// TODO Auto-generated method stub
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_fp);
		String strOrgUrl = hdiskHost + hdiskApi;
		ObjectNode outerNode = om.createObjectNode();
		outerNode.put("type", "im");
		outerNode.put("version", version);
		outerNode.put("platform", platform);
		outerNode.put("pName", pro);
		if(pro.equalsIgnoreCase("ihs")) //ihs 的im 
		{
			outerNode.put("ihsPath", amsCfg.getProperty("softpath") + "was");
		}else
		outerNode.put("wasPath", amsCfg.getProperty("softpath") + pro);
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, outerNode.toString());
			logger.info("fixImpl收到数据" + response);
		} catch (NetWorkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("fp接口网络存在问题！" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("和fp接口网络存在问题！" + e.getMessage());
		}
		if (!response.equals("")) {

			return response;
		}
		return null;
	}

}
