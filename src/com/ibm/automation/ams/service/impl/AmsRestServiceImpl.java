package com.ibm.automation.ams.service.impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.ams.service.AmsRestService;
import com.ibm.automation.ams.util.AmsClient;
import com.ibm.automation.ams.util.AmsV2ClientHttpClient4Impl;
import com.ibm.automation.core.constants.PropertyKeyConst;
import com.ibm.automation.core.exception.NetWorkException;
import com.ibm.automation.core.exception.ParamErrorException;
import com.ibm.automation.core.util.HttpClientUtil;

@Service("amsRestServiceImpl")
public class AmsRestServiceImpl implements AmsRestService {
	public static Logger logger = Logger.getLogger(AmsRestServiceImpl.class);
	AmsClient amsClient = new AmsV2ClientHttpClient4Impl();
	ObjectMapper om = new ObjectMapper();
	
	public ArrayNode getList(ArrayNode sort, JsonNode query, String url) {
		try {
			return amsClient.list(sort, query, url);
		}catch(Exception e ) {
			e.printStackTrace();
			logger.error("调用AmsRestServiceImpl::getList出错，出错信息为" + e.getMessage());
		}
		return null;
	}
	
	//get 方法
	public String getList_get(ArrayNode sort, JsonNode query, String url) {
		try {
			return amsClient.list_get(sort, query, url);
		}catch(Exception e ) {
			e.printStackTrace();
			logger.error("调用AmsRestServiceImpl::getList出错，出错信息为" + e.getMessage());
		}
		return null;
	}
	
	public ObjectNode getList_one(ArrayNode sort, JsonNode query, String url) {
		try {
			return amsClient.list_one(sort, query, url);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用AmsRestServiceImpl::getList出错，出错信息为" + e.getMessage());
		}
		return null;
	}
	

	/**
	 * @author warren(hj)
	 * @param version
	 *            the version of ibm product.such as DB2 WAS MQ ORACLE
	 * @param os
	 *            the operation system such as AIX LINUX
	 * @return ObjectNode 返回一个JSON对象，包含所有的版本、补丁数据
	 * @throws ParamErrorException
	 * 
	 */
	@Override
	public ObjectNode getVersion(String platform, String pName) {
		// TODO Auto-generated method stub

	
		
		String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
		String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_service_fp);
		// 拼接URL
		String strOrgUrl = hdiskHost + hdiskApi;

		String response = "";
		ObjectNode on = om.createObjectNode();
		on.put("pName", pName.toLowerCase());
		on.put("platform", platform.toLowerCase());
		try {
			response = HttpClientUtil.postMethod(strOrgUrl, on.toString());
			try {
				return (ObjectNode) om.readTree(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	

//通过接口更新主机active/error
@Override
public String checkServerStatus(String uuids) {
	// TODO Auto-generated method stub
	String hdiskHost = amsCfg.getProperty(PropertyKeyConst.AMS2_HOST);
	String hdiskApi = amsCfg.getProperty(PropertyKeyConst.POST_ams2_server_checkserverstatus);
	// 拼接URL
	String strOrgUrl = hdiskHost + hdiskApi;
	ObjectNode on = om.createObjectNode();
	on.put("servers", uuids);
	String response="";
	try {
		response= HttpClientUtil.postMethod(strOrgUrl, on.toString());
	} catch (NetWorkException | IOException e) {	
		logger.info("AmsRestService::checkserverstatus error "+e.getMessage());
		return "";
	}
	
	return response;
}

public static void main(String[] args) {
	AmsRestService ars = new AmsRestServiceImpl();
	ArrayNode an = ars.getList(null, null, "/api/v1/users");
	System.out.println(an);
}
	

}