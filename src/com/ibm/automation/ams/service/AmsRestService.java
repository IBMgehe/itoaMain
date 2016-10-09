package com.ibm.automation.ams.service;

import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.core.util.PropertyUtil;



public interface AmsRestService {
	
	Properties amsCfg = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	public  ArrayNode getList(ArrayNode sort, JsonNode query, String url);
	public  ObjectNode getList_one(ArrayNode sort, JsonNode query, String url);//单一playbook查找
	public ObjectNode getVersion(String version, String os);
	public String getList_get(ArrayNode sort, JsonNode query, String url) ;
	public String checkServerStatus(String uuids);
}