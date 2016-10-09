package com.ibm.automation.logcatch;

/**
 * 这个bean 主要用于提交参数的封装
 * 
 * @author Admin
 *
 */
public class LogCatchCreateBean {
	private String _id;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	private String url;
	private String ip;
	private String version;
	private String product;
	private String instance;
	private String database;
	private String task_timestamp;
	private String operation;
	private String loc;//日志存放点
	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTask_timestamp() {
		return task_timestamp;
	}

	public void setTask_timestamp(String task_timestamp) {
		this.task_timestamp = task_timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "{\"_id\":\"" + _id + "\",\"url\":\"" + url + "\",\"ip\":\"" + ip + "\",\"version\":\"" + version
				+ "\",\"product\":\"" + product + "\",\"instance\":\"" + instance + "\",\"database\":\"" + database
				+ "\",\"task_timestamp\":\"" + task_timestamp + "\",\"operation\":\"" + operation + "\",\"loc\":\""
				+ loc + "\"} ";
	}

	

}
