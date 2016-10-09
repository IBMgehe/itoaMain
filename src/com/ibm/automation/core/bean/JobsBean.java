package com.ibm.automation.core.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 健康检查 jobs 信息
 * @author Admin
 *
 */
public class JobsBean {
	private  String job_uuid;
	private  String job_type;    
	private  String job_target;
	private  String job_groupOrIP;//是选择了组还是部分ip
	public String getJob_groupOrIP() {
		return job_groupOrIP;
	}
	public void setJob_groupOrIP(String job_groupOrIP) {
		this.job_groupOrIP = job_groupOrIP;
	}
	private ObjectNode job_runType;
	private String job_detail;
	public String getJob_detail() {
		return job_detail;
	}
	public void setJob_detail(String job_detail) {
		this.job_detail = job_detail;
	}
	public ObjectNode getJob_runType() {
		return job_runType;
	}
	public void setJob_runType(ObjectNode job_runType) {
		this.job_runType = job_runType;
	}
	private String job_submited_by;
	private String job_lastrun_at;
	private String job_scheduled_at;
	private String job_if_daily;
	public String getJob_if_daily() {
		return job_if_daily;
	}
	public void setJob_if_daily(String job_if_daily) {
		this.job_if_daily = job_if_daily;
	}
//	private String job_status;
/*	public String getJob_status() {
		return job_status;
	}
	public void setJob_status(String job_status) {
		this.job_status = job_status;
	}
	*/
	public String getJob_lastrun_at() {
		return job_lastrun_at;
	}
	public void setJob_lastrun_at(String job_lastrun_at) {
		this.job_lastrun_at = job_lastrun_at;
	}
	public String getJob_scheduled_at() {
		return job_scheduled_at;
	}
	public void setJob_scheduled_at(String job_scheduled_at) {
		this.job_scheduled_at = job_scheduled_at;
	}
	public String getJob_uuid() {
		return job_uuid;
	}
	public void setJob_uuid(String job_uuid) {
		this.job_uuid = job_uuid;
	}
	public String getJob_type() {
		return job_type;
	}
	public void setJob_type(String job_type) {
		this.job_type = job_type;
	}
	public String getJob_target() {
		return job_target;
	}
	public void setJob_target(String job_target) {
		this.job_target = job_target;
	}

	public String getJob_submited_by() {
		return job_submited_by;
	}
	public void setJob_submited_by(String job_submited_by) {
		this.job_submited_by = job_submited_by;
	}
	
	
	
}
