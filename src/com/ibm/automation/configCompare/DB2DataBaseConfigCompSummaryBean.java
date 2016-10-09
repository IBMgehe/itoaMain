package com.ibm.automation.configCompare;
public class DB2DataBaseConfigCompSummaryBean {
	private String name;
	private String parent;//属于哪个instance
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}

	private String compDate;
	private String ENABLE_XMLCHAR;
	private String AUTORESTART;
	private String LOCKTIMEOUT;
	private String AUTO_PROF_UPD;
	private String Log_retain_for_recovery_status;
	private String MINCOMMIT;
	private String REC_HIS_RETENTN;
	private String User_exit_for_logging_status;
	private String UTIL_HEAP_SZ;
	private String DFT_SQLMATHWARN;
	private String HADR_database_role;
	private String SELF_TUNING_MEM;
	private String SEQDETECT;
	private String TSM_MGMTCLASS;
	private String MAX_LOG;
	private String AUTO_REVAL;
	private String HADR_TIMEOUT;
	private String AUTO_DEL_REC_OBJ;
	private String NUM_QUANTILES;
	private String HADR_REMOTE_SVC;
	private String MON_PKGLIST_SZ;
	private String NUM_DB_BACKUPS;
	private String VARCHAR2_COMPAT;
	private String DB_MEM_THRESH;
	private String AUTO_REORG;
	private String ARCHRETRYDELAY;
	private String VENDOROPT;
	private String DFT_EXTENT_SZ;
	private String NUM_FREQVALUES;
	private String MAXAPPLS;
	private String DECFLT_ROUNDING;
	private String MON_OBJ_METRICS;
	private String AUTO_MAINT;
	private String ALT_COLLATE;
	private String DBHEAP;
	private String NUMBER_COMPAT;
	private String DISCOVER_DB;
	private String INDEXREC;
	private String MAXLOCKS;
	private String Default_number_of_containers;
	private String BLK_LOG_DSK_FUL;
	private String TSM_PASSWORD;
	private String LOGRETAIN;
	private String BUFFPAGE;
	private String STMTHEAP;
	private String LOGPRIMARY;
	private String Database_territory;
	private String Database_code_page;
	private String DFT_QUERYOPT;
	private String Database_country_region_code;
	private String First_active_log_file;
	private String LOGARCHMETH1;
	private String CHNGPGS_THRESH;
	private String DFT_LOADREC_SES;
	private String TSM_OWNER;
	private String AUTO_STATS_PROF;
	private String DEC_TO_CHAR_FMT;
	private String CATALOGCACHE_SZ;
	private String Path_to_log_files;
	private String DFT_DEGREE;
	private String LOGFILSIZ;
	private String Backup_pending;
	private String SHEAPTHRES_SHR;
	private String Restrict_access;
	private String Statement_concentrator;
	private String DATE_COMPAT;
	private String HADR_PEER_WINDOW;
	private String MAXFILOP;
	private String LOGSECOND;
	private String AUTO_TBL_MAINT;
	private String HADR_REMOTE_INST;
	private String AUTO_DB_BACKUP;
	private String DFT_PREFETCH_SZ;
	private String MON_LOCKWAIT;
	private String NUM_IOCLEANERS;
	private String NUM_LOG_SPAN;
	private String MON_LOCKTIMEOUT;
	private String TRACKMOD;
	private String NUM_IOSERVERS;
	private String LOGINDEXBUILD;
	private String MON_LCK_MSG_LVL;
	private String MON_REQ_METRICS;
	private String LOGARCHMETH2;
	private String DLCHKTIME;
	private String SOFTMAX;
	private String MIRRORLOGPATH;
	private String Multipage_File_alloc_enabled;
	private String NUMARCHRETRY;
	private String AUTO_STMT_STATS;
	private String FAILARCHPATH;
	private String USEREXIT;
	private String Database_page_size;
	private String HADR_LOCAL_HOST;
	private String MON_ACT_METRICS;
	private String AVG_APPLS;
	private String WLM_COLLECT_INT;
	private String LOGBUFSZ;
	private String DFT_MTTB_TYPES;
	private String Database_is_consistent;
	private String STAT_HEAP_SZ;
	private String Database_code_set;
	private String SORTHEAP;
	private String CUR_COMMIT;
	private String MON_LW_THRESH;
	private String DATABASE_MEMORY;
	private String MON_UOW_DATA;
	private String BLOCKNONLOGGED;
	private String TSM_NODENAME;
	private String PCKCACHESZ;
	private String HADR_REMOTE_HOST;
	private String APPL_MEMORY;
	private String Restore_pending;
	private String DFT_REFRESH_AGE;
	private String HADR_SYNCMODE;
	private String OVERFLOWLOGPATH;
	private String MON_DEADLOCK;
	private String AUTO_RUNSTATS;
	private String LOCKLIST;
	private String Rollforward_pending;
	private String Database_collating_sequence;
	private String NEWLOGPATH;
	private String DYN_QUERY_MGMT;
	private String INDEXSORT;
	private String APPLHEAPSZ;
	private String HADR_LOCAL_SVC;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompDate() {
		return compDate;
	}
	public void setCompDate(String compDate) {
		this.compDate = compDate;
	}
	public String getENABLE_XMLCHAR() {
		return ENABLE_XMLCHAR;
	}
	public void setENABLE_XMLCHAR(String eNABLE_XMLCHAR) {
		ENABLE_XMLCHAR = eNABLE_XMLCHAR;
	}
	public String getAUTORESTART() {
		return AUTORESTART;
	}
	public void setAUTORESTART(String aUTORESTART) {
		AUTORESTART = aUTORESTART;
	}
	public String getLOCKTIMEOUT() {
		return LOCKTIMEOUT;
	}
	public void setLOCKTIMEOUT(String lOCKTIMEOUT) {
		LOCKTIMEOUT = lOCKTIMEOUT;
	}
	public String getAUTO_PROF_UPD() {
		return AUTO_PROF_UPD;
	}
	public void setAUTO_PROF_UPD(String aUTO_PROF_UPD) {
		AUTO_PROF_UPD = aUTO_PROF_UPD;
	}
	public String getLog_retain_for_recovery_status() {
		return Log_retain_for_recovery_status;
	}
	public void setLog_retain_for_recovery_status(String log_retain_for_recovery_status) {
		Log_retain_for_recovery_status = log_retain_for_recovery_status;
	}
	public String getMINCOMMIT() {
		return MINCOMMIT;
	}
	public void setMINCOMMIT(String mINCOMMIT) {
		MINCOMMIT = mINCOMMIT;
	}
	public String getREC_HIS_RETENTN() {
		return REC_HIS_RETENTN;
	}
	public void setREC_HIS_RETENTN(String rEC_HIS_RETENTN) {
		REC_HIS_RETENTN = rEC_HIS_RETENTN;
	}
	public String getUser_exit_for_logging_status() {
		return User_exit_for_logging_status;
	}
	public void setUser_exit_for_logging_status(String user_exit_for_logging_status) {
		User_exit_for_logging_status = user_exit_for_logging_status;
	}
	public String getUTIL_HEAP_SZ() {
		return UTIL_HEAP_SZ;
	}
	public void setUTIL_HEAP_SZ(String uTIL_HEAP_SZ) {
		UTIL_HEAP_SZ = uTIL_HEAP_SZ;
	}
	public String getDFT_SQLMATHWARN() {
		return DFT_SQLMATHWARN;
	}
	public void setDFT_SQLMATHWARN(String dFT_SQLMATHWARN) {
		DFT_SQLMATHWARN = dFT_SQLMATHWARN;
	}
	public String getHADR_database_role() {
		return HADR_database_role;
	}
	public void setHADR_database_role(String hADR_database_role) {
		HADR_database_role = hADR_database_role;
	}
	public String getSELF_TUNING_MEM() {
		return SELF_TUNING_MEM;
	}
	public void setSELF_TUNING_MEM(String sELF_TUNING_MEM) {
		SELF_TUNING_MEM = sELF_TUNING_MEM;
	}
	public String getSEQDETECT() {
		return SEQDETECT;
	}
	public void setSEQDETECT(String sEQDETECT) {
		SEQDETECT = sEQDETECT;
	}
	public String getTSM_MGMTCLASS() {
		return TSM_MGMTCLASS;
	}
	public void setTSM_MGMTCLASS(String tSM_MGMTCLASS) {
		TSM_MGMTCLASS = tSM_MGMTCLASS;
	}
	public String getMAX_LOG() {
		return MAX_LOG;
	}
	public void setMAX_LOG(String mAX_LOG) {
		MAX_LOG = mAX_LOG;
	}
	public String getAUTO_REVAL() {
		return AUTO_REVAL;
	}
	public void setAUTO_REVAL(String aUTO_REVAL) {
		AUTO_REVAL = aUTO_REVAL;
	}
	public String getHADR_TIMEOUT() {
		return HADR_TIMEOUT;
	}
	public void setHADR_TIMEOUT(String hADR_TIMEOUT) {
		HADR_TIMEOUT = hADR_TIMEOUT;
	}
	public String getAUTO_DEL_REC_OBJ() {
		return AUTO_DEL_REC_OBJ;
	}
	public void setAUTO_DEL_REC_OBJ(String aUTO_DEL_REC_OBJ) {
		AUTO_DEL_REC_OBJ = aUTO_DEL_REC_OBJ;
	}
	public String getNUM_QUANTILES() {
		return NUM_QUANTILES;
	}
	public void setNUM_QUANTILES(String nUM_QUANTILES) {
		NUM_QUANTILES = nUM_QUANTILES;
	}
	public String getHADR_REMOTE_SVC() {
		return HADR_REMOTE_SVC;
	}
	public void setHADR_REMOTE_SVC(String hADR_REMOTE_SVC) {
		HADR_REMOTE_SVC = hADR_REMOTE_SVC;
	}
	public String getMON_PKGLIST_SZ() {
		return MON_PKGLIST_SZ;
	}
	public void setMON_PKGLIST_SZ(String mON_PKGLIST_SZ) {
		MON_PKGLIST_SZ = mON_PKGLIST_SZ;
	}
	public String getNUM_DB_BACKUPS() {
		return NUM_DB_BACKUPS;
	}
	public void setNUM_DB_BACKUPS(String nUM_DB_BACKUPS) {
		NUM_DB_BACKUPS = nUM_DB_BACKUPS;
	}
	public String getVARCHAR2_COMPAT() {
		return VARCHAR2_COMPAT;
	}
	public void setVARCHAR2_COMPAT(String vARCHAR2_COMPAT) {
		VARCHAR2_COMPAT = vARCHAR2_COMPAT;
	}
	public String getDB_MEM_THRESH() {
		return DB_MEM_THRESH;
	}
	public void setDB_MEM_THRESH(String dB_MEM_THRESH) {
		DB_MEM_THRESH = dB_MEM_THRESH;
	}
	public String getAUTO_REORG() {
		return AUTO_REORG;
	}
	public void setAUTO_REORG(String aUTO_REORG) {
		AUTO_REORG = aUTO_REORG;
	}
	public String getARCHRETRYDELAY() {
		return ARCHRETRYDELAY;
	}
	public void setARCHRETRYDELAY(String aRCHRETRYDELAY) {
		ARCHRETRYDELAY = aRCHRETRYDELAY;
	}
	public String getVENDOROPT() {
		return VENDOROPT;
	}
	public void setVENDOROPT(String vENDOROPT) {
		VENDOROPT = vENDOROPT;
	}
	public String getDFT_EXTENT_SZ() {
		return DFT_EXTENT_SZ;
	}
	public void setDFT_EXTENT_SZ(String dFT_EXTENT_SZ) {
		DFT_EXTENT_SZ = dFT_EXTENT_SZ;
	}
	public String getNUM_FREQVALUES() {
		return NUM_FREQVALUES;
	}
	public void setNUM_FREQVALUES(String nUM_FREQVALUES) {
		NUM_FREQVALUES = nUM_FREQVALUES;
	}
	public String getMAXAPPLS() {
		return MAXAPPLS;
	}
	public void setMAXAPPLS(String mAXAPPLS) {
		MAXAPPLS = mAXAPPLS;
	}
	public String getDECFLT_ROUNDING() {
		return DECFLT_ROUNDING;
	}
	public void setDECFLT_ROUNDING(String dECFLT_ROUNDING) {
		DECFLT_ROUNDING = dECFLT_ROUNDING;
	}
	public String getMON_OBJ_METRICS() {
		return MON_OBJ_METRICS;
	}
	public void setMON_OBJ_METRICS(String mON_OBJ_METRICS) {
		MON_OBJ_METRICS = mON_OBJ_METRICS;
	}
	public String getAUTO_MAINT() {
		return AUTO_MAINT;
	}
	public void setAUTO_MAINT(String aUTO_MAINT) {
		AUTO_MAINT = aUTO_MAINT;
	}
	public String getALT_COLLATE() {
		return ALT_COLLATE;
	}
	public void setALT_COLLATE(String aLT_COLLATE) {
		ALT_COLLATE = aLT_COLLATE;
	}
	public String getDBHEAP() {
		return DBHEAP;
	}
	public void setDBHEAP(String dBHEAP) {
		DBHEAP = dBHEAP;
	}
	public String getNUMBER_COMPAT() {
		return NUMBER_COMPAT;
	}
	public void setNUMBER_COMPAT(String nUMBER_COMPAT) {
		NUMBER_COMPAT = nUMBER_COMPAT;
	}
	public String getDISCOVER_DB() {
		return DISCOVER_DB;
	}
	public void setDISCOVER_DB(String dISCOVER_DB) {
		DISCOVER_DB = dISCOVER_DB;
	}
	public String getINDEXREC() {
		return INDEXREC;
	}
	public void setINDEXREC(String iNDEXREC) {
		INDEXREC = iNDEXREC;
	}
	public String getMAXLOCKS() {
		return MAXLOCKS;
	}
	public void setMAXLOCKS(String mAXLOCKS) {
		MAXLOCKS = mAXLOCKS;
	}
	public String getDefault_number_of_containers() {
		return Default_number_of_containers;
	}
	public void setDefault_number_of_containers(String default_number_of_containers) {
		Default_number_of_containers = default_number_of_containers;
	}
	public String getBLK_LOG_DSK_FUL() {
		return BLK_LOG_DSK_FUL;
	}
	public void setBLK_LOG_DSK_FUL(String bLK_LOG_DSK_FUL) {
		BLK_LOG_DSK_FUL = bLK_LOG_DSK_FUL;
	}
	public String getTSM_PASSWORD() {
		return TSM_PASSWORD;
	}
	public void setTSM_PASSWORD(String tSM_PASSWORD) {
		TSM_PASSWORD = tSM_PASSWORD;
	}
	public String getLOGRETAIN() {
		return LOGRETAIN;
	}
	public void setLOGRETAIN(String lOGRETAIN) {
		LOGRETAIN = lOGRETAIN;
	}
	public String getBUFFPAGE() {
		return BUFFPAGE;
	}
	public void setBUFFPAGE(String bUFFPAGE) {
		BUFFPAGE = bUFFPAGE;
	}
	public String getSTMTHEAP() {
		return STMTHEAP;
	}
	public void setSTMTHEAP(String sTMTHEAP) {
		STMTHEAP = sTMTHEAP;
	}
	public String getLOGPRIMARY() {
		return LOGPRIMARY;
	}
	public void setLOGPRIMARY(String lOGPRIMARY) {
		LOGPRIMARY = lOGPRIMARY;
	}
	public String getDatabase_territory() {
		return Database_territory;
	}
	public void setDatabase_territory(String database_territory) {
		Database_territory = database_territory;
	}
	public String getDatabase_code_page() {
		return Database_code_page;
	}
	public void setDatabase_code_page(String database_code_page) {
		Database_code_page = database_code_page;
	}
	public String getDFT_QUERYOPT() {
		return DFT_QUERYOPT;
	}
	public void setDFT_QUERYOPT(String dFT_QUERYOPT) {
		DFT_QUERYOPT = dFT_QUERYOPT;
	}
	public String getDatabase_country_region_code() {
		return Database_country_region_code;
	}
	public void setDatabase_country_region_code(String database_country_region_code) {
		Database_country_region_code = database_country_region_code;
	}
	public String getFirst_active_log_file() {
		return First_active_log_file;
	}
	public void setFirst_active_log_file(String first_active_log_file) {
		First_active_log_file = first_active_log_file;
	}
	public String getLOGARCHMETH1() {
		return LOGARCHMETH1;
	}
	public void setLOGARCHMETH1(String lOGARCHMETH1) {
		LOGARCHMETH1 = lOGARCHMETH1;
	}
	public String getCHNGPGS_THRESH() {
		return CHNGPGS_THRESH;
	}
	public void setCHNGPGS_THRESH(String cHNGPGS_THRESH) {
		CHNGPGS_THRESH = cHNGPGS_THRESH;
	}
	public String getDFT_LOADREC_SES() {
		return DFT_LOADREC_SES;
	}
	public void setDFT_LOADREC_SES(String dFT_LOADREC_SES) {
		DFT_LOADREC_SES = dFT_LOADREC_SES;
	}
	public String getTSM_OWNER() {
		return TSM_OWNER;
	}
	public void setTSM_OWNER(String tSM_OWNER) {
		TSM_OWNER = tSM_OWNER;
	}
	public String getAUTO_STATS_PROF() {
		return AUTO_STATS_PROF;
	}
	public void setAUTO_STATS_PROF(String aUTO_STATS_PROF) {
		AUTO_STATS_PROF = aUTO_STATS_PROF;
	}
	public String getDEC_TO_CHAR_FMT() {
		return DEC_TO_CHAR_FMT;
	}
	public void setDEC_TO_CHAR_FMT(String dEC_TO_CHAR_FMT) {
		DEC_TO_CHAR_FMT = dEC_TO_CHAR_FMT;
	}
	public String getCATALOGCACHE_SZ() {
		return CATALOGCACHE_SZ;
	}
	public void setCATALOGCACHE_SZ(String cATALOGCACHE_SZ) {
		CATALOGCACHE_SZ = cATALOGCACHE_SZ;
	}
	public String getPath_to_log_files() {
		return Path_to_log_files;
	}
	public void setPath_to_log_files(String path_to_log_files) {
		Path_to_log_files = path_to_log_files;
	}
	public String getDFT_DEGREE() {
		return DFT_DEGREE;
	}
	public void setDFT_DEGREE(String dFT_DEGREE) {
		DFT_DEGREE = dFT_DEGREE;
	}
	public String getLOGFILSIZ() {
		return LOGFILSIZ;
	}
	public void setLOGFILSIZ(String lOGFILSIZ) {
		LOGFILSIZ = lOGFILSIZ;
	}
	public String getBackup_pending() {
		return Backup_pending;
	}
	public void setBackup_pending(String backup_pending) {
		Backup_pending = backup_pending;
	}
	public String getSHEAPTHRES_SHR() {
		return SHEAPTHRES_SHR;
	}
	public void setSHEAPTHRES_SHR(String sHEAPTHRES_SHR) {
		SHEAPTHRES_SHR = sHEAPTHRES_SHR;
	}
	public String getRestrict_access() {
		return Restrict_access;
	}
	public void setRestrict_access(String restrict_access) {
		Restrict_access = restrict_access;
	}
	public String getStatement_concentrator() {
		return Statement_concentrator;
	}
	public void setStatement_concentrator(String statement_concentrator) {
		Statement_concentrator = statement_concentrator;
	}
	public String getDATE_COMPAT() {
		return DATE_COMPAT;
	}
	public void setDATE_COMPAT(String dATE_COMPAT) {
		DATE_COMPAT = dATE_COMPAT;
	}
	public String getHADR_PEER_WINDOW() {
		return HADR_PEER_WINDOW;
	}
	public void setHADR_PEER_WINDOW(String hADR_PEER_WINDOW) {
		HADR_PEER_WINDOW = hADR_PEER_WINDOW;
	}
	public String getMAXFILOP() {
		return MAXFILOP;
	}
	public void setMAXFILOP(String mAXFILOP) {
		MAXFILOP = mAXFILOP;
	}
	public String getLOGSECOND() {
		return LOGSECOND;
	}
	public void setLOGSECOND(String lOGSECOND) {
		LOGSECOND = lOGSECOND;
	}
	public String getAUTO_TBL_MAINT() {
		return AUTO_TBL_MAINT;
	}
	public void setAUTO_TBL_MAINT(String aUTO_TBL_MAINT) {
		AUTO_TBL_MAINT = aUTO_TBL_MAINT;
	}
	public String getHADR_REMOTE_INST() {
		return HADR_REMOTE_INST;
	}
	public void setHADR_REMOTE_INST(String hADR_REMOTE_INST) {
		HADR_REMOTE_INST = hADR_REMOTE_INST;
	}
	public String getAUTO_DB_BACKUP() {
		return AUTO_DB_BACKUP;
	}
	public void setAUTO_DB_BACKUP(String aUTO_DB_BACKUP) {
		AUTO_DB_BACKUP = aUTO_DB_BACKUP;
	}
	public String getDFT_PREFETCH_SZ() {
		return DFT_PREFETCH_SZ;
	}
	public void setDFT_PREFETCH_SZ(String dFT_PREFETCH_SZ) {
		DFT_PREFETCH_SZ = dFT_PREFETCH_SZ;
	}
	public String getMON_LOCKWAIT() {
		return MON_LOCKWAIT;
	}
	public void setMON_LOCKWAIT(String mON_LOCKWAIT) {
		MON_LOCKWAIT = mON_LOCKWAIT;
	}
	public String getNUM_IOCLEANERS() {
		return NUM_IOCLEANERS;
	}
	public void setNUM_IOCLEANERS(String nUM_IOCLEANERS) {
		NUM_IOCLEANERS = nUM_IOCLEANERS;
	}
	public String getNUM_LOG_SPAN() {
		return NUM_LOG_SPAN;
	}
	public void setNUM_LOG_SPAN(String nUM_LOG_SPAN) {
		NUM_LOG_SPAN = nUM_LOG_SPAN;
	}
	public String getMON_LOCKTIMEOUT() {
		return MON_LOCKTIMEOUT;
	}
	public void setMON_LOCKTIMEOUT(String mON_LOCKTIMEOUT) {
		MON_LOCKTIMEOUT = mON_LOCKTIMEOUT;
	}
	public String getTRACKMOD() {
		return TRACKMOD;
	}
	public void setTRACKMOD(String tRACKMOD) {
		TRACKMOD = tRACKMOD;
	}
	public String getNUM_IOSERVERS() {
		return NUM_IOSERVERS;
	}
	public void setNUM_IOSERVERS(String nUM_IOSERVERS) {
		NUM_IOSERVERS = nUM_IOSERVERS;
	}
	public String getLOGINDEXBUILD() {
		return LOGINDEXBUILD;
	}
	public void setLOGINDEXBUILD(String lOGINDEXBUILD) {
		LOGINDEXBUILD = lOGINDEXBUILD;
	}
	public String getMON_LCK_MSG_LVL() {
		return MON_LCK_MSG_LVL;
	}
	public void setMON_LCK_MSG_LVL(String mON_LCK_MSG_LVL) {
		MON_LCK_MSG_LVL = mON_LCK_MSG_LVL;
	}
	public String getMON_REQ_METRICS() {
		return MON_REQ_METRICS;
	}
	public void setMON_REQ_METRICS(String mON_REQ_METRICS) {
		MON_REQ_METRICS = mON_REQ_METRICS;
	}
	public String getLOGARCHMETH2() {
		return LOGARCHMETH2;
	}
	public void setLOGARCHMETH2(String lOGARCHMETH2) {
		LOGARCHMETH2 = lOGARCHMETH2;
	}
	public String getDLCHKTIME() {
		return DLCHKTIME;
	}
	public void setDLCHKTIME(String dLCHKTIME) {
		DLCHKTIME = dLCHKTIME;
	}
	public String getSOFTMAX() {
		return SOFTMAX;
	}
	public void setSOFTMAX(String sOFTMAX) {
		SOFTMAX = sOFTMAX;
	}
	public String getMIRRORLOGPATH() {
		return MIRRORLOGPATH;
	}
	public void setMIRRORLOGPATH(String mIRRORLOGPATH) {
		MIRRORLOGPATH = mIRRORLOGPATH;
	}
	public String getMultipage_File_alloc_enabled() {
		return Multipage_File_alloc_enabled;
	}
	public void setMultipage_File_alloc_enabled(String multipage_File_alloc_enabled) {
		Multipage_File_alloc_enabled = multipage_File_alloc_enabled;
	}
	public String getNUMARCHRETRY() {
		return NUMARCHRETRY;
	}
	public void setNUMARCHRETRY(String nUMARCHRETRY) {
		NUMARCHRETRY = nUMARCHRETRY;
	}
	public String getAUTO_STMT_STATS() {
		return AUTO_STMT_STATS;
	}
	public void setAUTO_STMT_STATS(String aUTO_STMT_STATS) {
		AUTO_STMT_STATS = aUTO_STMT_STATS;
	}
	public String getFAILARCHPATH() {
		return FAILARCHPATH;
	}
	public void setFAILARCHPATH(String fAILARCHPATH) {
		FAILARCHPATH = fAILARCHPATH;
	}
	public String getUSEREXIT() {
		return USEREXIT;
	}
	public void setUSEREXIT(String uSEREXIT) {
		USEREXIT = uSEREXIT;
	}
	public String getDatabase_page_size() {
		return Database_page_size;
	}
	public void setDatabase_page_size(String database_page_size) {
		Database_page_size = database_page_size;
	}
	public String getHADR_LOCAL_HOST() {
		return HADR_LOCAL_HOST;
	}
	public void setHADR_LOCAL_HOST(String hADR_LOCAL_HOST) {
		HADR_LOCAL_HOST = hADR_LOCAL_HOST;
	}
	public String getMON_ACT_METRICS() {
		return MON_ACT_METRICS;
	}
	public void setMON_ACT_METRICS(String mON_ACT_METRICS) {
		MON_ACT_METRICS = mON_ACT_METRICS;
	}
	public String getAVG_APPLS() {
		return AVG_APPLS;
	}
	public void setAVG_APPLS(String aVG_APPLS) {
		AVG_APPLS = aVG_APPLS;
	}
	public String getWLM_COLLECT_INT() {
		return WLM_COLLECT_INT;
	}
	public void setWLM_COLLECT_INT(String wLM_COLLECT_INT) {
		WLM_COLLECT_INT = wLM_COLLECT_INT;
	}
	public String getLOGBUFSZ() {
		return LOGBUFSZ;
	}
	public void setLOGBUFSZ(String lOGBUFSZ) {
		LOGBUFSZ = lOGBUFSZ;
	}
	public String getDFT_MTTB_TYPES() {
		return DFT_MTTB_TYPES;
	}
	public void setDFT_MTTB_TYPES(String dFT_MTTB_TYPES) {
		DFT_MTTB_TYPES = dFT_MTTB_TYPES;
	}
	public String getDatabase_is_consistent() {
		return Database_is_consistent;
	}
	public void setDatabase_is_consistent(String database_is_consistent) {
		Database_is_consistent = database_is_consistent;
	}
	public String getSTAT_HEAP_SZ() {
		return STAT_HEAP_SZ;
	}
	public void setSTAT_HEAP_SZ(String sTAT_HEAP_SZ) {
		STAT_HEAP_SZ = sTAT_HEAP_SZ;
	}
	public String getDatabase_code_set() {
		return Database_code_set;
	}
	public void setDatabase_code_set(String database_code_set) {
		Database_code_set = database_code_set;
	}
	public String getSORTHEAP() {
		return SORTHEAP;
	}
	public void setSORTHEAP(String sORTHEAP) {
		SORTHEAP = sORTHEAP;
	}
	public String getCUR_COMMIT() {
		return CUR_COMMIT;
	}
	public void setCUR_COMMIT(String cUR_COMMIT) {
		CUR_COMMIT = cUR_COMMIT;
	}
	public String getMON_LW_THRESH() {
		return MON_LW_THRESH;
	}
	public void setMON_LW_THRESH(String mON_LW_THRESH) {
		MON_LW_THRESH = mON_LW_THRESH;
	}
	public String getDATABASE_MEMORY() {
		return DATABASE_MEMORY;
	}
	public void setDATABASE_MEMORY(String dATABASE_MEMORY) {
		DATABASE_MEMORY = dATABASE_MEMORY;
	}
	public String getMON_UOW_DATA() {
		return MON_UOW_DATA;
	}
	public void setMON_UOW_DATA(String mON_UOW_DATA) {
		MON_UOW_DATA = mON_UOW_DATA;
	}
	public String getBLOCKNONLOGGED() {
		return BLOCKNONLOGGED;
	}
	public void setBLOCKNONLOGGED(String bLOCKNONLOGGED) {
		BLOCKNONLOGGED = bLOCKNONLOGGED;
	}
	public String getTSM_NODENAME() {
		return TSM_NODENAME;
	}
	public void setTSM_NODENAME(String tSM_NODENAME) {
		TSM_NODENAME = tSM_NODENAME;
	}
	public String getPCKCACHESZ() {
		return PCKCACHESZ;
	}
	public void setPCKCACHESZ(String pCKCACHESZ) {
		PCKCACHESZ = pCKCACHESZ;
	}
	public String getHADR_REMOTE_HOST() {
		return HADR_REMOTE_HOST;
	}
	public void setHADR_REMOTE_HOST(String hADR_REMOTE_HOST) {
		HADR_REMOTE_HOST = hADR_REMOTE_HOST;
	}
	public String getAPPL_MEMORY() {
		return APPL_MEMORY;
	}
	public void setAPPL_MEMORY(String aPPL_MEMORY) {
		APPL_MEMORY = aPPL_MEMORY;
	}
	public String getRestore_pending() {
		return Restore_pending;
	}
	public void setRestore_pending(String restore_pending) {
		Restore_pending = restore_pending;
	}
	public String getDFT_REFRESH_AGE() {
		return DFT_REFRESH_AGE;
	}
	public void setDFT_REFRESH_AGE(String dFT_REFRESH_AGE) {
		DFT_REFRESH_AGE = dFT_REFRESH_AGE;
	}
	public String getHADR_SYNCMODE() {
		return HADR_SYNCMODE;
	}
	public void setHADR_SYNCMODE(String hADR_SYNCMODE) {
		HADR_SYNCMODE = hADR_SYNCMODE;
	}
	public String getOVERFLOWLOGPATH() {
		return OVERFLOWLOGPATH;
	}
	public void setOVERFLOWLOGPATH(String oVERFLOWLOGPATH) {
		OVERFLOWLOGPATH = oVERFLOWLOGPATH;
	}
	public String getMON_DEADLOCK() {
		return MON_DEADLOCK;
	}
	public void setMON_DEADLOCK(String mON_DEADLOCK) {
		MON_DEADLOCK = mON_DEADLOCK;
	}
	public String getAUTO_RUNSTATS() {
		return AUTO_RUNSTATS;
	}
	public void setAUTO_RUNSTATS(String aUTO_RUNSTATS) {
		AUTO_RUNSTATS = aUTO_RUNSTATS;
	}
	public String getLOCKLIST() {
		return LOCKLIST;
	}
	public void setLOCKLIST(String lOCKLIST) {
		LOCKLIST = lOCKLIST;
	}
	public String getRollforward_pending() {
		return Rollforward_pending;
	}
	public void setRollforward_pending(String rollforward_pending) {
		Rollforward_pending = rollforward_pending;
	}
	public String getDatabase_collating_sequence() {
		return Database_collating_sequence;
	}
	public void setDatabase_collating_sequence(String database_collating_sequence) {
		Database_collating_sequence = database_collating_sequence;
	}
	public String getNEWLOGPATH() {
		return NEWLOGPATH;
	}
	public void setNEWLOGPATH(String nEWLOGPATH) {
		NEWLOGPATH = nEWLOGPATH;
	}
	public String getDYN_QUERY_MGMT() {
		return DYN_QUERY_MGMT;
	}
	public void setDYN_QUERY_MGMT(String dYN_QUERY_MGMT) {
		DYN_QUERY_MGMT = dYN_QUERY_MGMT;
	}
	public String getINDEXSORT() {
		return INDEXSORT;
	}
	public void setINDEXSORT(String iNDEXSORT) {
		INDEXSORT = iNDEXSORT;
	}
	public String getAPPLHEAPSZ() {
		return APPLHEAPSZ;
	}
	public void setAPPLHEAPSZ(String aPPLHEAPSZ) {
		APPLHEAPSZ = aPPLHEAPSZ;
	}
	public String getHADR_LOCAL_SVC() {
		return HADR_LOCAL_SVC;
	}
	public void setHADR_LOCAL_SVC(String hADR_LOCAL_SVC) {
		HADR_LOCAL_SVC = hADR_LOCAL_SVC;
	}
	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"parent\":\"" + parent + "\",\"compDate\":\"" + compDate
				+ "\",\"ENABLE_XMLCHAR\":\"" + ENABLE_XMLCHAR + "\",\"AUTORESTART\":\"" + AUTORESTART
				+ "\",\"LOCKTIMEOUT\":\"" + LOCKTIMEOUT + "\",\"AUTO_PROF_UPD\":\"" + AUTO_PROF_UPD
				+ "\",\"Log_retain_for_recovery_status\":\"" + Log_retain_for_recovery_status + "\",\"MINCOMMIT\":\""
				+ MINCOMMIT + "\",\"REC_HIS_RETENTN\":\"" + REC_HIS_RETENTN + "\",\"User_exit_for_logging_status\":\""
				+ User_exit_for_logging_status + "\",\"UTIL_HEAP_SZ\":\"" + UTIL_HEAP_SZ + "\",\"DFT_SQLMATHWARN\":\""
				+ DFT_SQLMATHWARN + "\",\"HADR_database_role\":\"" + HADR_database_role + "\",\"SELF_TUNING_MEM\":\""
				+ SELF_TUNING_MEM + "\",\"SEQDETECT\":\"" + SEQDETECT + "\",\"TSM_MGMTCLASS\":\"" + TSM_MGMTCLASS
				+ "\",\"MAX_LOG\":\"" + MAX_LOG + "\",\"AUTO_REVAL\":\"" + AUTO_REVAL + "\",\"HADR_TIMEOUT\":\""
				+ HADR_TIMEOUT + "\",\"AUTO_DEL_REC_OBJ\":\"" + AUTO_DEL_REC_OBJ + "\",\"NUM_QUANTILES\":\""
				+ NUM_QUANTILES + "\",\"HADR_REMOTE_SVC\":\"" + HADR_REMOTE_SVC + "\",\"MON_PKGLIST_SZ\":\""
				+ MON_PKGLIST_SZ + "\",\"NUM_DB_BACKUPS\":\"" + NUM_DB_BACKUPS + "\",\"VARCHAR2_COMPAT\":\""
				+ VARCHAR2_COMPAT + "\",\"DB_MEM_THRESH\":\"" + DB_MEM_THRESH + "\",\"AUTO_REORG\":\"" + AUTO_REORG
				+ "\",\"ARCHRETRYDELAY\":\"" + ARCHRETRYDELAY + "\",\"VENDOROPT\":\"" + VENDOROPT
				+ "\",\"DFT_EXTENT_SZ\":\"" + DFT_EXTENT_SZ + "\",\"NUM_FREQVALUES\":\"" + NUM_FREQVALUES
				+ "\",\"MAXAPPLS\":\"" + MAXAPPLS + "\",\"DECFLT_ROUNDING\":\"" + DECFLT_ROUNDING
				+ "\",\"MON_OBJ_METRICS\":\"" + MON_OBJ_METRICS + "\",\"AUTO_MAINT\":\"" + AUTO_MAINT
				+ "\",\"ALT_COLLATE\":\"" + ALT_COLLATE + "\",\"DBHEAP\":\"" + DBHEAP + "\",\"NUMBER_COMPAT\":\""
				+ NUMBER_COMPAT + "\",\"DISCOVER_DB\":\"" + DISCOVER_DB + "\",\"INDEXREC\":\"" + INDEXREC
				+ "\",\"MAXLOCKS\":\"" + MAXLOCKS + "\",\"Default_number_of_containers\":\""
				+ Default_number_of_containers + "\",\"BLK_LOG_DSK_FUL\":\"" + BLK_LOG_DSK_FUL
				+ "\",\"TSM_PASSWORD\":\"" + TSM_PASSWORD + "\",\"LOGRETAIN\":\"" + LOGRETAIN + "\",\"BUFFPAGE\":\""
				+ BUFFPAGE + "\",\"STMTHEAP\":\"" + STMTHEAP + "\",\"LOGPRIMARY\":\"" + LOGPRIMARY
				+ "\",\"Database_territory\":\"" + Database_territory + "\",\"Database_code_page\":\""
				+ Database_code_page + "\",\"DFT_QUERYOPT\":\"" + DFT_QUERYOPT
				+ "\",\"Database_country_region_code\":\"" + Database_country_region_code
				+ "\",\"First_active_log_file\":\"" + First_active_log_file + "\",\"LOGARCHMETH1\":\"" + LOGARCHMETH1
				+ "\",\"CHNGPGS_THRESH\":\"" + CHNGPGS_THRESH + "\",\"DFT_LOADREC_SES\":\"" + DFT_LOADREC_SES
				+ "\",\"TSM_OWNER\":\"" + TSM_OWNER + "\",\"AUTO_STATS_PROF\":\"" + AUTO_STATS_PROF
				+ "\",\"DEC_TO_CHAR_FMT\":\"" + DEC_TO_CHAR_FMT + "\",\"CATALOGCACHE_SZ\":\"" + CATALOGCACHE_SZ
				+ "\",\"Path_to_log_files\":\"" + Path_to_log_files + "\",\"DFT_DEGREE\":\"" + DFT_DEGREE
				+ "\",\"LOGFILSIZ\":\"" + LOGFILSIZ + "\",\"Backup_pending\":\"" + Backup_pending
				+ "\",\"SHEAPTHRES_SHR\":\"" + SHEAPTHRES_SHR + "\",\"Restrict_access\":\"" + Restrict_access
				+ "\",\"Statement_concentrator\":\"" + Statement_concentrator + "\",\"DATE_COMPAT\":\"" + DATE_COMPAT
				+ "\",\"HADR_PEER_WINDOW\":\"" + HADR_PEER_WINDOW + "\",\"MAXFILOP\":\"" + MAXFILOP
				+ "\",\"LOGSECOND\":\"" + LOGSECOND + "\",\"AUTO_TBL_MAINT\":\"" + AUTO_TBL_MAINT
				+ "\",\"HADR_REMOTE_INST\":\"" + HADR_REMOTE_INST + "\",\"AUTO_DB_BACKUP\":\"" + AUTO_DB_BACKUP
				+ "\",\"DFT_PREFETCH_SZ\":\"" + DFT_PREFETCH_SZ + "\",\"MON_LOCKWAIT\":\"" + MON_LOCKWAIT
				+ "\",\"NUM_IOCLEANERS\":\"" + NUM_IOCLEANERS + "\",\"NUM_LOG_SPAN\":\"" + NUM_LOG_SPAN
				+ "\",\"MON_LOCKTIMEOUT\":\"" + MON_LOCKTIMEOUT + "\",\"TRACKMOD\":\"" + TRACKMOD
				+ "\",\"NUM_IOSERVERS\":\"" + NUM_IOSERVERS + "\",\"LOGINDEXBUILD\":\"" + LOGINDEXBUILD
				+ "\",\"MON_LCK_MSG_LVL\":\"" + MON_LCK_MSG_LVL + "\",\"MON_REQ_METRICS\":\"" + MON_REQ_METRICS
				+ "\",\"LOGARCHMETH2\":\"" + LOGARCHMETH2 + "\",\"DLCHKTIME\":\"" + DLCHKTIME + "\",\"SOFTMAX\":\""
				+ SOFTMAX + "\",\"MIRRORLOGPATH\":\"" + MIRRORLOGPATH + "\",\"Multipage_File_alloc_enabled\":\""
				+ Multipage_File_alloc_enabled + "\",\"NUMARCHRETRY\":\"" + NUMARCHRETRY + "\",\"AUTO_STMT_STATS\":\""
				+ AUTO_STMT_STATS + "\",\"FAILARCHPATH\":\"" + FAILARCHPATH + "\",\"USEREXIT\":\"" + USEREXIT
				+ "\",\"Database_page_size\":\"" + Database_page_size + "\",\"HADR_LOCAL_HOST\":\"" + HADR_LOCAL_HOST
				+ "\",\"MON_ACT_METRICS\":\"" + MON_ACT_METRICS + "\",\"AVG_APPLS\":\"" + AVG_APPLS
				+ "\",\"WLM_COLLECT_INT\":\"" + WLM_COLLECT_INT + "\",\"LOGBUFSZ\":\"" + LOGBUFSZ
				+ "\",\"DFT_MTTB_TYPES\":\"" + DFT_MTTB_TYPES + "\",\"Database_is_consistent\":\""
				+ Database_is_consistent + "\",\"STAT_HEAP_SZ\":\"" + STAT_HEAP_SZ + "\",\"Database_code_set\":\""
				+ Database_code_set + "\",\"SORTHEAP\":\"" + SORTHEAP + "\",\"CUR_COMMIT\":\"" + CUR_COMMIT
				+ "\",\"MON_LW_THRESH\":\"" + MON_LW_THRESH + "\",\"DATABASE_MEMORY\":\"" + DATABASE_MEMORY
				+ "\",\"MON_UOW_DATA\":\"" + MON_UOW_DATA + "\",\"BLOCKNONLOGGED\":\"" + BLOCKNONLOGGED
				+ "\",\"TSM_NODENAME\":\"" + TSM_NODENAME + "\",\"PCKCACHESZ\":\"" + PCKCACHESZ
				+ "\",\"HADR_REMOTE_HOST\":\"" + HADR_REMOTE_HOST + "\",\"APPL_MEMORY\":\"" + APPL_MEMORY
				+ "\",\"Restore_pending\":\"" + Restore_pending + "\",\"DFT_REFRESH_AGE\":\"" + DFT_REFRESH_AGE
				+ "\",\"HADR_SYNCMODE\":\"" + HADR_SYNCMODE + "\",\"OVERFLOWLOGPATH\":\"" + OVERFLOWLOGPATH
				+ "\",\"MON_DEADLOCK\":\"" + MON_DEADLOCK + "\",\"AUTO_RUNSTATS\":\"" + AUTO_RUNSTATS
				+ "\",\"LOCKLIST\":\"" + LOCKLIST + "\",\"Rollforward_pending\":\"" + Rollforward_pending
				+ "\",\"Database_collating_sequence\":\"" + Database_collating_sequence + "\",\"NEWLOGPATH\":\""
				+ NEWLOGPATH + "\",\"DYN_QUERY_MGMT\":\"" + DYN_QUERY_MGMT + "\",\"INDEXSORT\":\"" + INDEXSORT
				+ "\",\"APPLHEAPSZ\":\"" + APPLHEAPSZ + "\",\"HADR_LOCAL_SVC\":\"" + HADR_LOCAL_SVC + "\"} ";
	}
	
	
	
	
	
}
