package com.ibm.automation.core.bean;
/**
 * 每个任务的执行情况
 * @author Admin
 *
 */
public class JobDetailBean {
	private String id;
	private String jobDetail_submited_by;//提交人
	private String jobDetail_status;//执行结果  success 1  failure 0 
	private String jobDetail_scheduled_at;//执行时间
	private String jobDetail_url;//详情跳转页
	public String getJobDetail_url() {
		return jobDetail_url;
	}
	public void setJobDetail_url(String jobDetail_url) {
		this.jobDetail_url = jobDetail_url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobDetail_submited_by() {
		return jobDetail_submited_by;
	}
	public void setJobDetail_submited_by(String jobDetail_submited_by) {
		this.jobDetail_submited_by = jobDetail_submited_by;
	}
	public String getJobDetail_status() {
		return jobDetail_status;
	}
	public void setJobDetail_status(String jobDetail_status) {
		this.jobDetail_status = jobDetail_status;
	}
	public String getJobDetail_scheduled_at() {
		return jobDetail_scheduled_at;
	}
	public void setJobDetail_scheduled_at(String jobDetail_scheduled_at) {
		this.jobDetail_scheduled_at = jobDetail_scheduled_at;
	}
	
	
}
