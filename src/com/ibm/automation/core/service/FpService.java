package com.ibm.automation.core.service;



public interface FpService {
/**
 * this class is used for mq\was\dbs\ fp install
 * @author hujin
 * @see 
 * */
	public String getVersion(String pro,String platform);
	public String getFix(String platform,String pro , String version);
	public String getIm(String platform,String pro,String version);
}
