package com.ibm.automation.core.bean;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ServersBean implements Serializable, Comparable<ServersBean> {
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger(ServersBean.class);
	private static final long serialVersionUID = -6626380570577382415L;
	private String ip;// ip
	private String userid;// userId
	private String password;// password
	private String status;
	private String hconf;
	private String os;// os
	private String name;// 名称
	private String uuid;
	private String hvisor;
	private String product;//这个IP是装mq was db2 的。


	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ServersBean.logger = logger;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHconf() {
		return hconf;
	}

	public void setHconf(String hconf) {
		this.hconf = hconf;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getHvisor() {
		return hvisor;
	}

	public void setHvisor(String hvisor) {
		this.hvisor = hvisor;
	}

	public ObjectMapper getOm() {
		return om;
	}

	public void setOm(ObjectMapper om) {
		this.om = om;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private ObjectMapper om = new ObjectMapper();

	@Override
	public int compareTo(ServersBean s) {
		// TODO Auto-generated method stub
		return s.getUuid().compareTo(this.getUuid()) == 0 ? this.getName().compareTo(s.getName())
				: s.getUuid().compareTo(this.getUuid());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		ObjectNode oServer = om.createObjectNode();
		oServer.put("name", this.getName());
		oServer.put("ip", this.getIp());
		oServer.put("os", this.getOs());
		oServer.put("hconf", this.getHconf());
		oServer.put("hvisor", this.getHvisor());
		oServer.put("status", this.getStatus());
		oServer.put("userid", this.getUserid());
		oServer.put("password", this.getPassword());
		oServer.put("uuid", this.getUuid());
		oServer.put("product", this.getProduct());
		String retVal = null;
		try {
			retVal = om.writeValueAsString(oServer);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("ServerBean::" + e.getMessage());
			return null;
		}
		return retVal;
	}

}
