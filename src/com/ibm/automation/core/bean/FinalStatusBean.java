package com.ibm.automation.core.bean;
/**
 * 主要用于产品更新状态的时候的数据回显
 * 
 * */
public class FinalStatusBean {
	private String uuid;//task 唯一ID 
	private String name;//中文名
	private String alisname;//英文名
	private String host;//IP地址
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	private String status;//状态  成功、运行中、失败、跳过
	private String play;//属于哪一个play
	public String getPlay() {
		return play;
	}
	public void setPlay(String play) {
		this.play = play;
	}
	public String getAlisname() {
		return alisname;
	}
	public void setAlisname(String alisname) {
		this.alisname = alisname;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
}
