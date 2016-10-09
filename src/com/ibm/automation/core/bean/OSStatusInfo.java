package com.ibm.automation.core.bean;
/**
 * 这个bean 描述了os top 10 进程的内容 适用于linux
 * @author Admin
 *
 */
public class OSStatusInfo {
	private String command;
	private String elapese;
	private String pid;
	private String cpu;
	private String user;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getElapese() {
		return elapese;
	}
	public void setElapese(String elapese) {
		this.elapese = elapese;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
}
