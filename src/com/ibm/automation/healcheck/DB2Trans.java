package com.ibm.automation.healcheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.automation.core.bean.DB2HealthCheckDatabaseBean;
import com.ibm.automation.core.bean.DB2HealthCheckInstanceBean;

public class DB2Trans {
	public  List<DB2HealthCheckInstanceBean> transDB2retJsonToList(String db2_retJson)
	{
		ObjectMapper om = new ObjectMapper();
		JsonNode total =null;
		try {
			total = om.readTree(db2_retJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//logger.info("transDB2retJsonToList::将db2 返回的json数据解开出错，出错信息为："+e.getMessage());
		}
		JsonNode db2 = total.get("db2");
		// System.out.println(db2);
		JsonNode instance = db2.get("instance");
		//System.out.println(instance);
		List<DB2HealthCheckInstanceBean> totalList = new ArrayList<DB2HealthCheckInstanceBean>();
		for (JsonNode jn : instance) {
			DB2HealthCheckInstanceBean dbhcib = new DB2HealthCheckInstanceBean();//一个实例对应多个数据库
			
			Iterator<Entry<String, JsonNode>> iterNode = jn.fields();
			while (iterNode.hasNext()) {
				DB2HealthCheckDatabaseBean dbcdb = new DB2HealthCheckDatabaseBean();
				Entry<String, JsonNode> entry = iterNode.next();
				//String key = ;
				
				dbhcib.setDb2_instanceName(entry.getKey());//获取实例的名
				
				Iterator<JsonNode> iter = entry.getValue().iterator();
				while(iter.hasNext())
				{
					JsonNode dbNode = iter.next();
					//实例信息设置
					dbhcib.setDb2_instancePath(dbNode.get("path").asText());
					dbhcib.setDb2_instanceLevel(dbNode.get("level").asText());
					
					
					//数据库设置
					dbcdb.setDb2_dbbufferpool_hitratio(dbNode.get("db").get("bufferpool").get("hitratio").asText());
					dbcdb.setDb2_dbconnections(dbNode.get("db").get("connections").asText());
					dbcdb.setDb2_dbdeadlock(dbNode.get("db").get("deadlock").asText());
					dbcdb.setDb2_dblastbackup(dbNode.get("db").get("lastbackup").asText());
					dbcdb.setDb2_dblockeasl(dbNode.get("db").get("lockeasl").asText());
					dbcdb.setDb2_dblocktimeout(dbNode.get("db").get("locktimeout").asText());
					dbcdb.setDb2_dbLOGMETHOD(dbNode.get("db").get("LOGMETHOD").asText());
					dbcdb.setDb2_dbName(dbNode.get("db").get("dbName").asText());
					dbcdb.setDb2_dbsortoverflow(dbNode.get("db").get("sortoverflow").asText());
					dbcdb.setDb2_dbStatus(dbNode.get("db").get("status").asText());
					dbcdb.setDb2_dbTablespacePercent(dbNode.get("db").get("tablespace").get("percent").asText());
					dbcdb.setDb2_dbTablespaceStatus(dbNode.get("db").get("tablespace").get("status").asText());
					//dbcdb.set
					dbhcib.getDb2_instaceList().add(dbcdb);
					totalList.add(dbhcib);
				}
			}
		}
		return totalList;
	}
}
