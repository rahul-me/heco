package com.gcn.heco.file.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcn.heco.app.model.DateTimeInfo;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.database.service.IMiDBService;
import com.gcn.heco.helper.HecoHelper;

@Service
public class MiFileControllerHandler {
	
	private static Logger logger = LoggerFactory.getLogger(MiFileControllerHandler.class);
	
	@Autowired
	private IMiDBService iMiDBService;
	
	@Autowired
	private HecoHelper helper;
	
	@Autowired
	private ConfigProperties configProperties;
	
	public void addSchedules(List<MiSchedule> manualApiSchedules) {
		logger.info("heco: Process for adding new schedule data of a post request has been started.");
		try {
			if (!manualApiSchedules.isEmpty()) {
				iMiDBService.saveOrUpdate(manualApiSchedules, configProperties.getBatchSize());
			} else {
				logger.info("heco: No data present in file");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("heco: Process for adding new schedule data of a post request has been completed.");
	}

	public void addSchedulesUsingList(List<MiSchedule> manualApiSchedules) {
		logger.info("heco: Process for adding new schedule data of a put request has been started.");
		if (!manualApiSchedules.isEmpty()) {
			iMiDBService.saveOrUpdate(manualApiSchedules, configProperties.getBatchSize());
		} else {
			logger.info("heco: No data present in file");
		}
		logger.info("heco: Process for adding new schedule data of a put request has been completed.");
	}		

	public MiSchedule generateManualApiSchedule(String[] firstRow, DateTimeInfo dateTimeInfo) {
		try {
			long startTime = helper.getTimeInLong(dateTimeInfo, firstRow[0]);
			float value = Float.parseFloat(firstRow[1]) / configProperties.getVppIDs().split(",").length;
			return getManualApiSchedule(value, startTime, dateTimeInfo.getTimeZone());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private MiSchedule getManualApiSchedule(float value, long startTime, String timeZone) {
		try {
			MiSchedule schedule = new MiSchedule();
			schedule.setValue(value);
			schedule.setStartTime(startTime);
			schedule.setTimeZone(timeZone);
			schedule.setStatus(Constant.MI_STATUS_INIT);
			return schedule;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	public boolean isBlankRow(String[] firstRow) {
		if (firstRow != null) {
			try {
				if (firstRow[0] != null && (firstRow[0].trim().length() > 0)) {
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return false;
	}

	public boolean isTimeString(String row) {
		return (row != null && (row.indexOf(':') != row.lastIndexOf(':')));
	}

	public void makeUpdate(DateTimeInfo dateTimeInfo) {
		try {
			long timeinmillies = helper.getTimeInLong(dateTimeInfo);
			iMiDBService.updateMISchedules(timeinmillies, dateTimeInfo.getTimeZone());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void deleteMISchedules(long time, String timeZone) {
		iMiDBService.deleteMISchedules(time, timeZone);
	}
	
	public void archiveData(int duration, int expiration) {
		iMiDBService.archiveData(duration, expiration);
	}
}
