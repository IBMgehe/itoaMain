package com.ibm.automation.configCompare;

/**
 * mq 配置比对的每天获取的配置信息，区别于MQ配置比对
 * 
 * @author Admin
 *
 */
public class MQconfigCompSummaryBean {
	private String name;// 被比较的队列管理器
	private String compDate;// 被比较的内容的时间
	private String LogPrimaryFiles;
	private String LogSecondaryFiles;
	private String LogFilePages;
	private String LogType;

	private String MaxChannels;
	private String MaxActiveChannels;

	private String KeepAlive;
	private String SndBuffSize;
	private String RcvBuffSize;
	private String RcvSndBuffSize;
	private String RcvRcvBuffSize;

	private String ClntSndBuffSize;
	private String ClntRcvBuffSize;
	private String SvrSndBuffSize;
	private String SvrRcvBuffSize;
	//private String ErrorLogSize;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaxChannels() {
		return MaxChannels;
	}

	public void setMaxChannels(String maxChannels) {
		MaxChannels = maxChannels;
	}

	public String getMaxActiveChannels() {
		return MaxActiveChannels;
	}

	public void setMaxActiveChannels(String maxActiveChannels) {
		MaxActiveChannels = maxActiveChannels;
	}

	public String getSndBuffSize() {
		return SndBuffSize;
	}

	public void setSndBuffSize(String sndBuffSize) {
		SndBuffSize = sndBuffSize;
	}

	public String getRcvBuffSize() {
		return RcvBuffSize;
	}

	public void setRcvBuffSize(String rcvBuffSize) {
		RcvBuffSize = rcvBuffSize;
	}

	public String getRcvSndBuffSize() {
		return RcvSndBuffSize;
	}

	public void setRcvSndBuffSize(String rcvSndBuffSize) {
		RcvSndBuffSize = rcvSndBuffSize;
	}

	public String getRcvRcvBuffSize() {
		return RcvRcvBuffSize;
	}

	public void setRcvRcvBuffSize(String rcvRcvBuffSize) {
		RcvRcvBuffSize = rcvRcvBuffSize;
	}

	public String getClntSndBuffSize() {
		return ClntSndBuffSize;
	}

	public void setClntSndBuffSize(String clntSndBuffSize) {
		ClntSndBuffSize = clntSndBuffSize;
	}

	public String getClntRcvBuffSize() {
		return ClntRcvBuffSize;
	}

	public void setClntRcvBuffSize(String clntRcvBuffSize) {
		ClntRcvBuffSize = clntRcvBuffSize;
	}

	
	public String getCompDate() {
		return compDate;
	}

	public void setCompDate(String compDate) {
		this.compDate = compDate;
	}

	

	public String getLogSecondaryFiles() {
		return LogSecondaryFiles;
	}

	public void setLogSecondaryFiles(String logSecondaryFiles) {
		LogSecondaryFiles = logSecondaryFiles;
	}

	public String getLogFilePages() {
		return LogFilePages;
	}

	public void setLogFilePages(String logFilePages) {
		LogFilePages = logFilePages;
	}

	public String getLogType() {
		return LogType;
	}

	public void setLogType(String logType) {
		LogType = logType;
	}

	public String getKeepAlive() {
		return KeepAlive;
	}

	public void setKeepAlive(String keepAlive) {
		KeepAlive = keepAlive;
	}

	public String getSvrSndBuffSize() {
		return SvrSndBuffSize;
	}

	public void setSvrSndBuffSize(String svrSndBuffSize) {
		SvrSndBuffSize = svrSndBuffSize;
	}

	public String getSvrRcvBuffSize() {
		return SvrRcvBuffSize;
	}

	public void setSvrRcvBuffSize(String svrRcvBuffSize) {
		SvrRcvBuffSize = svrRcvBuffSize;
	}

	public String getLogPrimaryFiles() {
		return LogPrimaryFiles;
	}

	public void setLogPrimaryFiles(String logPrimaryFiles) {
		LogPrimaryFiles = logPrimaryFiles;
	}

	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"compDate\":\"" + compDate + "\",\"LogPrimaryFiles\":\"" + LogPrimaryFiles
				+ "\",\"LogSecondaryFiles\":\"" + LogSecondaryFiles + "\",\"LogFilePages\":\"" + LogFilePages
				+ "\",\"LogType\":\"" + LogType + "\",\"MaxChannels\":\"" + MaxChannels + "\",\"MaxActiveChannels\":\""
				+ MaxActiveChannels + "\",\"KeepAlive\":\"" + KeepAlive + "\",\"SndBuffSize\":\"" + SndBuffSize
				+ "\",\"RcvBuffSize\":\"" + RcvBuffSize + "\",\"RcvSndBuffSize\":\"" + RcvSndBuffSize
				+ "\",\"RcvRcvBuffSize\":\"" + RcvRcvBuffSize + "\",\"ClntSndBuffSize\":\"" + ClntSndBuffSize
				+ "\",\"ClntRcvBuffSize\":\"" + ClntRcvBuffSize + "\",\"SvrSndBuffSize\":\"" + SvrSndBuffSize
				+ "\",\"SvrRcvBuffSize\":\"" + SvrRcvBuffSize + "\"} ";
	}

	

	
	

}
