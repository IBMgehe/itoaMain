package com.ibm.automation.core.bean;

//适用于linux
public class OSSystemInfoBean {
	private String healthLine;    // <50   70 90 
	private String healthStatus;// warn  bad good
	private String healthValue;//巡检数值
	private String healthInfo;//属于ram cpu disk swap?
	public String getHealthLine() {
		return healthLine;
	}
	public void setHealthLine(String healthLine) {
		this.healthLine = healthLine;
	}
	public String getHealthStatus() {
		return healthStatus;
	}
	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}
	public String getHealthValue() {
		return healthValue;
	}
	public void setHealthValue(String healthValue) {
		this.healthValue = healthValue;
	}
	public String getHealthInfo() {
		return healthInfo;
	}
	public void setHealthInfo(String healthInfo) {
		this.healthInfo = healthInfo;
	}
	
}
