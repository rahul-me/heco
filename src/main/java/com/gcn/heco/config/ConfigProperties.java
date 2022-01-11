package com.gcn.heco.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
public class ConfigProperties {
	
	static Logger logger = LoggerFactory.getLogger(ConfigProperties.class);
	
	@Value("${vppIDs}")
	private String vppIDs;
	
	@Value("${nocScheduleEndpoint}")
	private String nocScheduleEndpoint;
	
	@Value("${priority}")
	private Integer priority;
	
	@Value("${program}")
	private Integer program;
	
	@Value("${scheduleRepetitionType}")
	private String scheduleRepetitionType;
	
	@Value("${periodDuration}")
	private int periodDuration;
	
	@Value("${conTypeAJ}")
	private String conTypeAJ;
	
	@Value("${conTypeStr}")
	private String conTypeStr;
	
	@Value("${reqDelete}")
	private String reqDelete;
		
	@Value("${reqPost}")
	private String reqPost;
	
	@Value("${component}")
	private int component;
	
	@Value("${scheduleOperatorGE}")
	private String scheduleOperatorGE;
	
	@Value("${scheduleOperatorLE}")
	private String scheduleOperatorLE;
	
	@Value("${object}")
	private int object;	
	
	@Value("${reqParamDate}")
	private String reqParamDate;
	
	@Value("${reqParamTimeZone}")
	private String reqParamTimeZone;
	
	@Value("${reqParamFile}")
	private String reqParamFile;
	
	@Value("${dateFormat}")
	private String dateFormat;
	
	@Value("${startTimeForTheDate}")
	private String startTimeForTheDate;
	
	@Value("${archExp}")
	private int archExp;
	
	@Value("${batchSize}")
	private int batchSize;
	
	@Value("${frequency}")
	private int frequency;
	
	@Value("${window}")
	private int windowTime;	
	
	@Value("${miAdminKey}")
	private String adminKey;	
	
	@Value("${signalGap}")
	private int signalGap;
			
	public int getSignalGap() {
		return signalGap;
	}

	public int getPeriodDuration() {
		return periodDuration;
	}

	public String getAdminKey() {
		return adminKey;
	}	

	public int getFrequency() {
		return frequency;
	}

	public int getWindowTime() {
		return windowTime;
	}

	public int getBatchSize() {
		return batchSize;
	}	

	public int getArchExp() {
		return archExp;
	}

	public String getStartTimeForTheDate() {
		return startTimeForTheDate;
	}

	public String getDateFormat(){
		return dateFormat;
	}
	
	public String getReqParamDate() {
		return reqParamDate;
	}

	public String getReqParamTimeZone() {
		return reqParamTimeZone;
	}

	public String getReqParamFile() {
		return reqParamFile;
	}	

	public String getScheduleOperatorGE() {
		return scheduleOperatorGE;
	}

	public String getScheduleOperatorLE() {
		return scheduleOperatorLE;
	}	

	public static Logger getLog() {
		return logger;
	}

	public String getVppIDs() {
		return vppIDs;
	}

	public String getNocScheduleEndpoint() {
		return nocScheduleEndpoint;
	}

	public Integer getPriority() {
		return priority;
	}

	public Integer getProgram() {
		return program;
	}

	public String getScheduleRepetitionType() {
		return scheduleRepetitionType;
	}

	public String getConTypeAJ() {
		return conTypeAJ;
	}

	public String getConTypeStr() {
		return conTypeStr;
	}

	public String getReqDelete() {
		return reqDelete;
	}

	public String getReqPost() {
		return reqPost;
	}

	public int getComponent() {
		return component;
	}

	public int getObject() {
		return object;
	}	
}
