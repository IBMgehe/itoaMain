package com.ibm.automation.configCompare;


/**
 * 用于提交配置比对的bean
 * 
 * @author Admin
 *
 */
public class configCompareBean {
	private String configCompareBean_uuid;//唯一ID
	public String getConfigCompareBean_uuid() {
		return configCompareBean_uuid;
	}
	public void setConfigCompareBean_uuid(String configCompareBean_uuid) {
		this.configCompareBean_uuid = configCompareBean_uuid;
	}
	private String srcIP;// 被比对的IP
	private String srcProduct;// 被比对的产品
	private String srcProductVer;// 被比对的产品的版本
	private String srcDatetime;// 被比对的日期和时间
	private String srcInstance;// 被比对的实例

	private String srcDB;//被比对的数据库 ，如果选择DB2的话
	
	public String getSrcDB() {
		return srcDB;
	}
	public void setSrcDB(String srcDB) {
		this.srcDB = srcDB;
	}
	public String getTargetDB() {
		return targetDB;
	}
	public void setTargetDB(String targetDB) {
		this.targetDB = targetDB;
	}
	private String targetIP;// 比对的IP 只能两两比较
	private String targetProduct;// 被比对的产品
	private String targetProductVer;// 被比对的产品的版本
	private String targetDatetime;// 被比对的日期和时间
	private String targetInstance;// 被比对的实例
	private String targetDB;//被比对的数据库 ，如果选择DB2的话
	public String getSrcIP() {
		return srcIP;
	}
	public void setSrcIP(String srcIP) {
		this.srcIP = srcIP;
	}
	public String getSrcProduct() {
		return srcProduct;
	}
	public void setSrcProduct(String srcProduct) {
		this.srcProduct = srcProduct;
	}
	public String getSrcProductVer() {
		return srcProductVer;
	}
	public void setSrcProductVer(String srcProductVer) {
		this.srcProductVer = srcProductVer;
	}
	public String getSrcDatetime() {
		return srcDatetime;
	}
	public void setSrcDatetime(String srcDatetime) {
		this.srcDatetime = srcDatetime;
	}
	public String getSrcInstance() {
		return srcInstance;
	}
	public void setSrcInstance(String srcInstance) {
		this.srcInstance = srcInstance;
	}
	public String getTargetIP() {
		return targetIP;
	}
	public void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}
	public String getTargetProduct() {
		return targetProduct;
	}
	public void setTargetProduct(String targetProduct) {
		this.targetProduct = targetProduct;
	}
	public String getTargetProductVer() {
		return targetProductVer;
	}
	public void setTargetProductVer(String targetProductVer) {
		this.targetProductVer = targetProductVer;
	}
	public String getTargetDatetime() {
		return targetDatetime;
	}
	public void setTargetDatetime(String targetDatetime) {
		this.targetDatetime = targetDatetime;
	}
	public String getTargetInstance() {
		return targetInstance;
	}
	public void setTargetInstance(String targetInstance) {
		this.targetInstance = targetInstance;
	}
	@Override
	public String toString() {
		return "{\"configCompareBean_uuid\":\"" + configCompareBean_uuid + "\",\"srcIP\":\"" + srcIP
				+ "\",\"srcProduct\":\"" + srcProduct + "\",\"srcProductVer\":\"" + srcProductVer
				+ "\",\"srcDatetime\":\"" + srcDatetime + "\",\"srcInstance\":\"" + srcInstance + "\",\"srcDB\":\""
				+ srcDB + "\",\"targetIP\":\"" + targetIP + "\",\"targetProduct\":\"" + targetProduct
				+ "\",\"targetProductVer\":\"" + targetProductVer + "\",\"targetDatetime\":\"" + targetDatetime
				+ "\",\"targetInstance\":\"" + targetInstance + "\",\"targetDB\":\"" + targetDB + "\"} ";
	}
	
	

}
