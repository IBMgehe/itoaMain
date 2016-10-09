package com.ibm.automation.core.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ServerService {
	public int createOne(String on);//增加数组单个节点
	public int modifyOne(String on);//增加修改单个节点
	public int IPCheck(String ip , String type );//判断IP是否已经添加
	public int importFromExcel(String lsb);//导入excel
	public int deleteServer(String servers);//删除主机IP信息
	
	public ObjectNode createSendJson(String type,Object param);//发送的json串
	public String createSendUrl(String host,String apiPath);//发送到后台的地址

	
}
