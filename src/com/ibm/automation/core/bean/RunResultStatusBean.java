package com.ibm.automation.core.bean;
/**
 * 主要用于 ip 0 means good, 1 means warning, 2 means critical
 * @author Admin
 *
 */
public class RunResultStatusBean {
	private String healthJobsRunResult_uuid;
	public String getHealthJobsRunResult_uuid() {
		return healthJobsRunResult_uuid;
	}
	public void setHealthJobsRunResult_uuid(String healthJobsRunResult_uuid) {
		this.healthJobsRunResult_uuid = healthJobsRunResult_uuid;
	}
	private String healthJobsRunResult_detail;
	public String getHealthJobsRunResult_detail() {
		return healthJobsRunResult_detail;
	}
	public void setHealthJobsRunResult_detail(String healthJobsRunResult_detail) {
		this.healthJobsRunResult_detail = healthJobsRunResult_detail;
	}
	private int healthJobsRunResult_result;
	public int getHealthJobsRunResult_result() {
		return healthJobsRunResult_result;
	}
	public void setHealthJobsRunResult_result(int healthJobsRunResult_result) {
		this.healthJobsRunResult_result = healthJobsRunResult_result;
	}
	public String getHealthJobsRunResult_ip() {
		return healthJobsRunResult_ip;
	}
	public void setHealthJobsRunResult_ip(String healthJobsRunResult_ip) {
		this.healthJobsRunResult_ip = healthJobsRunResult_ip;
	}
	private String healthJobsRunResult_ip;
}
