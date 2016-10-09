package com.ibm.automation.core.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * DB2 巡检 实例信息bean
 * 
 * 一个实例对应多个数据库
 * 
 * @author Admin
 *
 */

public class DB2HealthCheckInstanceBean {
	// private Map<String,? extends List<DB2HealthCheckDatabaseBean>>
	// instanceMap;
	// private db2hcDBList;//db2健康检查所有的bean信息

	private List<DB2HealthCheckDatabaseBean> db2_instaceList = new ArrayList<DB2HealthCheckDatabaseBean>();

	public List<DB2HealthCheckDatabaseBean> getDb2_instaceList() {
		return db2_instaceList;
	}

	public void setDb2_instaceList(List<DB2HealthCheckDatabaseBean> db2_instaceList) {
		this.db2_instaceList = db2_instaceList;
	}

	private String db2_instanceName;
	private String db2_instancePath;
	private String db2_instanceLevel;

	public String getDb2_instanceName() {
		return db2_instanceName;
	}

	public void setDb2_instanceName(String db2_instanceName) {
		this.db2_instanceName = db2_instanceName;
	}

	public String getDb2_instancePath() {
		return db2_instancePath;
	}

	public void setDb2_instancePath(String db2_instancePath) {
		this.db2_instancePath = db2_instancePath;
	}

	public String getDb2_instanceLevel() {
		return db2_instanceLevel;
	}

	public void setDb2_instanceLevel(String db2_instanceLevel) {
		this.db2_instanceLevel = db2_instanceLevel;
	}

}
