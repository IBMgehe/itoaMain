package com.ibm.automation.core.bean;

/**
 * 这个是配置比对的创建任务bean
 * 
 * @author Admin
 *
 */
public class ConfigCompareJobBean {
	private String confComp_uuid;
	private String confComp_type; // 跟踪类型
	private String confComp_target; // 跟踪对象
	private String confComp_groupOrIP;// 是选择了组还是部分ip
	private String confComp_submited_by; // 提交人
	private String confComp_lastrun_at; // 最近运行时间
	private String confComp_scheduled_at; // 提交时间
	private String confComp_if_daily; // 是立即执行还是每日轮询
	private String confComp_detail;// outline 连接

	public String getConfComp_detail() {
		return confComp_detail;
	}

	public void setConfComp_detail(String confComp_detail) {
		this.confComp_detail = confComp_detail;
	}

	public String getConfComp_uuid() {
		return confComp_uuid;
	}

	public void setConfComp_uuid(String confComp_uuid) {
		this.confComp_uuid = confComp_uuid;
	}

	public String getConfComp_type() {
		return confComp_type;
	}

	public void setConfComp_type(String confComp_type) {
		this.confComp_type = confComp_type;
	}

	public String getConfComp_target() {
		return confComp_target;
	}

	public void setConfComp_target(String confComp_target) {
		this.confComp_target = confComp_target;
	}

	public String getConfComp_groupOrIP() {
		return confComp_groupOrIP;
	}

	public void setConfComp_groupOrIP(String confComp_groupOrIP) {
		this.confComp_groupOrIP = confComp_groupOrIP;
	}

	public String getConfComp_submited_by() {
		return confComp_submited_by;
	}

	public void setConfComp_submited_by(String confComp_submited_by) {
		this.confComp_submited_by = confComp_submited_by;
	}

	public String getConfComp_lastrun_at() {
		return confComp_lastrun_at;
	}

	public void setConfComp_lastrun_at(String confComp_lastrun_at) {
		this.confComp_lastrun_at = confComp_lastrun_at;
	}

	public String getConfComp_scheduled_at() {
		return confComp_scheduled_at;
	}

	public void setConfComp_scheduled_at(String confComp_scheduled_at) {
		this.confComp_scheduled_at = confComp_scheduled_at;
	}

	public String getConfComp_if_daily() {
		return confComp_if_daily;
	}

	public void setConfComp_if_daily(String confComp_if_daily) {
		this.confComp_if_daily = confComp_if_daily;
	}

	@Override
	public String toString() {
		return "{\"confComp_uuid\":\"" + confComp_uuid + "\",\"confComp_type\":\"" + confComp_type
				+ "\",\"confComp_target\":\"" + confComp_target + "\",\"confComp_groupOrIP\":\"" + confComp_groupOrIP
				+ "\",\"confComp_submited_by\":\"" + confComp_submited_by + "\",\"confComp_lastrun_at\":\""
				+ confComp_lastrun_at + "\",\"confComp_scheduled_at\":\"" + confComp_scheduled_at
				+ "\",\"confComp_if_daily\":\"" + confComp_if_daily + "\",\"confComp_detail\":\"" + confComp_detail
				+ "\"} ";
	}

}
