package com.ibm.automation.core.bean;

import java.util.List;

/**
 * 发送给tornado的json参数基类
 * */
public class BaseBean {
	private String type;
	private List<String> ip_list;
	private List<String> hostname_list;
	private List<String> user_list;
	private List<String> password_list;


	public String getType() {
		return type;
	}

	public List<String> getIp_list() {
		return ip_list;
	}

	public void setIp_list(List<String> ip_list) {
		this.ip_list = ip_list;
	}

	public List<String> getHostname_list() {
		return hostname_list;
	}

	public void setHostname_list(List<String> hostname_list) {
		this.hostname_list = hostname_list;
	}

	public List<String> getUser_list() {
		return user_list;
	}

	public void setUser_list(List<String> user_list) {
		this.user_list = user_list;
	}

	public List<String> getPassword_list() {
		return password_list;
	}

	public void setPassword_list(List<String> password_list) {
		this.password_list = password_list;
	}

	public void setType(String type) {
		this.type = type;
	}
}
