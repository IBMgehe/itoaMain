package com.ibm.automation.core.service.impl;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.core.bean.LoginBean;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.service.ServerService;
import com.ibm.automation.core.util.HttpClientUtil;
import com.ibm.automation.core.util.PropertyUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 增加用户主机
 */

@Service("serverService")
public class ServerServiceImpl implements ServerService {
	Properties amsCfg = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	private static Logger logger = Logger.getLogger(ServerServiceImpl.class);
	ObjectMapper om = new ObjectMapper();
	
	public ServerServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * -1 有问题 1 已经注册 0 未注册
	 */
	// @SuppressWarnings("null")
	@Override
	public int IPCheck(String ip, String type) {
		// TODO Auto-generated method stub
		if ((ip == null || ip.equals("")) || (type == null && "".equals(type)))
			return -1;
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_servers);
		String strOrgUrl = hdiskHost + hdiskApi;
		
		ObjectNode node = om.createObjectNode();
		node.put("type", type);
		node.put("IP", ip);
		String response = "";
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, node.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!response.equals("")) {
			JSONObject jsonObj = JSONObject.fromObject(response);
			Integer retVal = (Integer) jsonObj.get("status");
			return retVal.intValue();// 1
		}

		return 0;// 0未注册
	}

	/**
	 * -1 不成功，原因未知 1成功
	 */
	@Override
	public int createOne(String on) {
		// TODO Auto-generated method stub
		if (null == on)
			return -1;
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_servers);
		String strOrgUrl = hdiskHost + hdiskApi;
		String response = "";
		try {
			try {
				response = HttpClientUtil.postMethod(strOrgUrl, on);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(response);
			if (!response.equals("")) {
				JSONObject jsonObj = JSONObject.fromObject(response);
				Integer retVal = (Integer) jsonObj.get("status");
				if (retVal == 1)
					return 1;
				// else return -1;
			}
		} catch (JSONException e1) {
			logger.error("json 格式存在异常！" + e1.getMessage());
			e1.printStackTrace();
		}
		return -1;
	}

	/**
	 * 1 成功 0 失败
	 */
	@Override
	public int modifyOne(String on) {
		// TODO Auto-generated method stub
		if (null == on)
			return 0;
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_servers);
		String strOrgUrl = hdiskHost + hdiskApi;
		String response = "";
		try {
			try {
				response = HttpClientUtil.postMethod(strOrgUrl, on.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(response);
			if (!response.equals("")) {
				JSONObject jsonObj = JSONObject.fromObject(response);
				Integer retVal = (Integer) jsonObj.get("status");
				if (retVal == 1)
					return 1;
				// else return -1;
			}
		} catch (JSONException e1) {
			logger.error("json 格式存在异常！" + e1.getMessage());
			e1.printStackTrace();
		}
		return 0;
	}

	@Override
	public int importFromExcel(String lsb) {
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_servers);
		String strOrgUrl = hdiskHost + hdiskApi;
		String response = "";
		try {
			try {
				response = HttpClientUtil.postMethod(strOrgUrl, lsb.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(response);
			if (!response.equals("")) {
				JSONObject jsonObj = JSONObject.fromObject(response);
				Integer retVal = (Integer) jsonObj.get("status");
				if (retVal == 1)
					return 1;
				// else return -1;
			}
		} catch (JSONException e1) {
			logger.error("json 格式存在异常！" + e1.getMessage());
			e1.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteServer(String servers) {
		// TODO Auto-generated method stub
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_servers);
		String strOrgUrl = hdiskHost + hdiskApi;
		String response = "";
		try {
			try {
				response = HttpClientUtil.postMethod(strOrgUrl, servers.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(response);
			if (!response.equals("")) {
				JSONObject jsonObj = JSONObject.fromObject(response);
				Integer retVal = (Integer) jsonObj.get("status");
				if (retVal == 1)
					return 1;
				// else return -1;
			}
		} catch (JSONException e1) {
			logger.error("json 格式存在异常！" + e1.getMessage());
			e1.printStackTrace();
		}
		return 0;
	}

	/**
	 * 这个方法用于组装和tornado 的交互json产生
	 */
	@Override
	public ObjectNode createSendJson(String type, Object param) {
		// TODO Auto-generated method stub
		ObjectNode on = om.createObjectNode();
		if (type != null) {
			switch (type) {
			case "login":
				if (param instanceof LoginBean) {
					LoginBean lb = (LoginBean) param;
					on.put("type", type);
					on.put("name", lb.getUsername());
					on.put("password", lb.getPassword());
					on.put("role", lb.getRole());
				}
				break;
			case "ipcheck":
				break;
			case "importExcel":
				break;
			case "createServer":
				break;
			case "deleteServer":
				break;
			case "modifyServer":
				break;
			default:
				return null;
			}
		}
		return on;
	}

	/**
	 * 产生发送到url tornado 的api地址
	 */
	@Override
	public String createSendUrl(String host, String apiPath) {
		// TODO Auto-generated method stub
		String hdiskHost = amsCfg.getProperty(host);
		String hdiskApi = amsCfg.getProperty(apiPath);
		return hdiskHost + hdiskApi;

	}

}
