package com.ibm.automation.core.bean;
/**
 * 这个是详细的MQ参数信息bean
 * @author Admin
 *
 */
public class MQHealthCheckSummaryBean {
	private String ip;
	private String hostname;
	private String os_version;
	private String mq_version;
	private String mq_inst_path;
	private String mq_Semns;
	private String mq_Semsl;
	private String mq_nofile;
	private String mq_shmmni;
	private String cpu;
	private String ram;
	private String disk;
	private String mq_queue_status;
	private String mq_dead_queue;
	private String mq_message_duiji;
	private String mq_channel_status;
	private String mq_list_status;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public String getMq_version() {
		return mq_version;
	}
	public void setMq_version(String mq_version) {
		this.mq_version = mq_version;
	}
	public String getMq_inst_path() {
		return mq_inst_path;
	}
	public void setMq_inst_path(String mq_inst_path) {
		this.mq_inst_path = mq_inst_path;
	}
	public String getMq_Semns() {
		return mq_Semns;
	}
	public void setMq_Semns(String mq_Semns) {
		this.mq_Semns = mq_Semns;
	}
	public String getMq_Semsl() {
		return mq_Semsl;
	}
	public void setMq_Semsl(String mq_Semsl) {
		this.mq_Semsl = mq_Semsl;
	}
	public String getMq_nofile() {
		return mq_nofile;
	}
	public void setMq_nofile(String mq_nofile) {
		this.mq_nofile = mq_nofile;
	}
	public String getMq_shmmni() {
		return mq_shmmni;
	}
	public void setMq_shmmni(String mq_shmmni) {
		this.mq_shmmni = mq_shmmni;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getDisk() {
		return disk;
	}
	public void setDisk(String disk) {
		this.disk = disk;
	}
	public String getMq_queue_status() {
		return mq_queue_status;
	}
	public void setMq_queue_status(String mq_queue_status) {
		this.mq_queue_status = mq_queue_status;
	}
	public String getMq_dead_queue() {
		return mq_dead_queue;
	}
	public void setMq_dead_queue(String mq_dead_queue) {
		this.mq_dead_queue = mq_dead_queue;
	}
	public String getMq_message_duiji() {
		return mq_message_duiji;
	}
	public void setMq_message_duiji(String mq_message_duiji) {
		this.mq_message_duiji = mq_message_duiji;
	}
	public String getMq_channel_status() {
		return mq_channel_status;
	}
	public void setMq_channel_status(String mq_channel_status) {
		this.mq_channel_status = mq_channel_status;
	}
	public String getMq_list_status() {
		return mq_list_status;
	}
	public void setMq_list_status(String mq_list_status) {
		this.mq_list_status = mq_list_status;
	}
	
	
}
