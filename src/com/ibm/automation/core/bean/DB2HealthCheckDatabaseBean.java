package com.ibm.automation.core.bean;


/**
 * DB2 巡检数据库 信息bean
 * @author Admin
 *
 */
public class DB2HealthCheckDatabaseBean {
	private  String db2_dbName; //数据库名
	private String db2_dbStatus;//数据库状态
	private String db2_dbTablespaceStatus;//表空间状态
	private String db2_dbTablespacePercent;//表空间使用率 
	private String db2_dbbufferpool_hitratio;//缓冲池信息
	private String db2_dbconnections;//连接数
	private String db2_dbdeadlock;//死锁
	private String db2_dblockeasl;//锁
	private String db2_dblocktimeout;//超时
	private String db2_dbsortoverflow;
	private String db2_dblastbackup;
	private String db2_dbLOGMETHOD;//日志
	public String getDb2_dbName() {
		return db2_dbName;
	}
	public void setDb2_dbName(String db2_dbName) {
		this.db2_dbName = db2_dbName;
	}
	public String getDb2_dbStatus() {
		return db2_dbStatus;
	}
	public void setDb2_dbStatus(String db2_dbStatus) {
		this.db2_dbStatus = db2_dbStatus;
	}
	public String getDb2_dbTablespaceStatus() {
		return db2_dbTablespaceStatus;
	}
	public void setDb2_dbTablespaceStatus(String db2_dbTablespaceStatus) {
		this.db2_dbTablespaceStatus = db2_dbTablespaceStatus;
	}
	public String getDb2_dbTablespacePercent() {
		return db2_dbTablespacePercent;
	}
	public void setDb2_dbTablespacePercent(String db2_dbTablespacePercent) {
		this.db2_dbTablespacePercent = db2_dbTablespacePercent;
	}
	public String getDb2_dbbufferpool_hitratio() {
		return db2_dbbufferpool_hitratio;
	}
	public void setDb2_dbbufferpool_hitratio(String db2_dbbufferpool_hitratio) {
		this.db2_dbbufferpool_hitratio = db2_dbbufferpool_hitratio;
	}
	public String getDb2_dbconnections() {
		return db2_dbconnections;
	}
	public void setDb2_dbconnections(String db2_dbconnections) {
		this.db2_dbconnections = db2_dbconnections;
	}
	public String getDb2_dbdeadlock() {
		return db2_dbdeadlock;
	}
	public void setDb2_dbdeadlock(String db2_dbdeadlock) {
		this.db2_dbdeadlock = db2_dbdeadlock;
	}
	public String getDb2_dblockeasl() {
		return db2_dblockeasl;
	}
	public void setDb2_dblockeasl(String db2_dblockeasl) {
		this.db2_dblockeasl = db2_dblockeasl;
	}
	public String getDb2_dblocktimeout() {
		return db2_dblocktimeout;
	}
	public void setDb2_dblocktimeout(String db2_dblocktimeout) {
		this.db2_dblocktimeout = db2_dblocktimeout;
	}
	public String getDb2_dbsortoverflow() {
		return db2_dbsortoverflow;
	}
	public void setDb2_dbsortoverflow(String db2_dbsortoverflow) {
		this.db2_dbsortoverflow = db2_dbsortoverflow;
	}
	public String getDb2_dblastbackup() {
		return db2_dblastbackup;
	}
	public void setDb2_dblastbackup(String db2_dblastbackup) {
		this.db2_dblastbackup = db2_dblastbackup;
	}
	public String getDb2_dbLOGMETHOD() {
		return db2_dbLOGMETHOD;
	}
	public void setDb2_dbLOGMETHOD(String db2_dbLOGMETHOD) {
		this.db2_dbLOGMETHOD = db2_dbLOGMETHOD;
	}
	
	
}
