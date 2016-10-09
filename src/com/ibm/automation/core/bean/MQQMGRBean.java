package com.ibm.automation.core.bean;
/**
 * 描述了mq qmgr 的状态
 * @author Admin
 *
 */
public class MQQMGRBean {
	private String qmgrName;
	private String qmgr;
	private String dlq;
	private String que;
	private String chl;
	private String lstr;
	public String getQmgrName() {
		return qmgrName;
	}
	public void setQmgrName(String qmgrName) {
		this.qmgrName = qmgrName;
	}
	public String getQmgr() {
		return qmgr;
	}
	public void setQmgr(String qmgr) {
		this.qmgr = qmgr;
	}
	public String getDlq() {
		return dlq;
	}
	public void setDlq(String dlq) {
		this.dlq = dlq;
	}
	public String getQue() {
		return que;
	}
	public void setQue(String que) {
		this.que = que;
	}
	public String getChl() {
		return chl;
	}
	public void setChl(String chl) {
		this.chl = chl;
	}
	public String getLstr() {
		return lstr;
	}
	public void setLstr(String lstr) {
		this.lstr = lstr;
	}
}
