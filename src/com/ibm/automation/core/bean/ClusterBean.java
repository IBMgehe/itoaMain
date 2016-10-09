package com.ibm.automation.core.bean;

/**
 * @author hujin
 * @see 为了表示mq集群而存在的类
 * */
public class ClusterBean {
	private String qmgrname;
	private String completesave;
	private String ip;
	private String name;
	private String uuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getQmgrname() {
		return qmgrname;
	}
	public void setQmgrname(String qmgrname) {
		this.qmgrname = qmgrname;
	}
	public String getCompletesave() {
		return completesave;
	}
	public void setCompletesave(String completesave) {
		this.completesave = completesave;
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
}
