package com.ibm.automation.core.bean;

/**
 * @author hujin
 * @see 为了表示was集群而存在的类
 * */
public class WasClusterBean {
	private String profilename;
	private String profiletype;
	private String ip;
	private String name;
	private String uuid;
	public String getProfilename() {
		return profilename;
	}
	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}
	public String getProfiletype() {
		return profiletype;
	}
	public void setProfiletype(String profiletype) {
		this.profiletype = profiletype;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	
}
