package com.gcn.heco.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gcn.heco.app.model.DateTimeInfo;
import com.gcn.heco.app.model.ErrorResponse;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.file.listener.IFileEventListener;
import com.gcn.heco.helper.HecoHelper;
import com.opencsv.CSVReader;

/**
 * @author GridScape_Solutions
 *
 */
@Service
public class MiFileControllerService {

	static Logger logger = LoggerFactory.getLogger(MiFileControllerService.class);

	@Autowired
	private IFileEventListener iFileEventListener;
	
	@Autowired
	private MiFileControllerHandler fileHandler;
	
	@Autowired
	private HecoHelper helper;
	
	@Autowired
	private ConfigProperties configProperties;
	
	@Autowired
	private ErrorResponse errorResponse;

	@Async
	public void handleSchedules(List<MiSchedule> manualApiSchedules, DateTimeInfo dateTimeInfo) {
		synchronized (this) {
			iFileEventListener.beforePost(manualApiSchedules, dateTimeInfo);
			fileHandler.addSchedules(manualApiSchedules);
			iFileEventListener.afterPost();
		}
	}

	@Async
	public void updateSchedules(Map<String, List<String[]>> updateData, String date,
			List<MiSchedule> manualApiSchedules) {
		synchronized (this) {
			DateTimeInfo dateTimeInfo = helper.generateDTI(date,
					updateData.get(configProperties.getReqParamTimeZone()).get(0)[0]);
			iFileEventListener.beforePut(manualApiSchedules, dateTimeInfo);			
			fileHandler.makeUpdate(dateTimeInfo);
			fileHandler.addSchedulesUsingList(manualApiSchedules);
			iFileEventListener.afterPut();
		}
	}

	@Async
	public void deleteSchedules(DateTimeInfo dateTimeInfo) {
		synchronized (this) {			
			iFileEventListener.beforeDelete(dateTimeInfo);
			long timeinmillies = helper.getTimeInLong(dateTimeInfo);
			logger.info("heco: Process for deleting schedule has been started.");
			fileHandler.deleteMISchedules(timeinmillies, dateTimeInfo.getTimeZone());
			logger.info("heco: Process for deleting schedule has been completed.");
			iFileEventListener.afterDelete();			
		}
	}

	

	public void archiveDataAsExpected() {
		int duration = configProperties.getPeriodDuration();
		int expiration = configProperties.getArchExp();
		fileHandler.archiveData(duration, expiration);
	}
	
	public List<MiSchedule> listSchedules(InputStream inputStream, DateTimeInfo dateTimeInfo) {
		List<MiSchedule> manualApiSchedules = new ArrayList<>();
		InputStreamReader inputReader = new InputStreamReader(inputStream);
		CSVReader reader = new CSVReader(inputReader);
		try {
			List<String[]> alList = reader.readAll();
			reader.close();
			errorResponse.setIsTimeString(fileHandler.isTimeString(alList.get(0)[0]));
			for (String[] firstRow : alList) {
				if (fileHandler.isBlankRow(firstRow) && fileHandler.isTimeString(firstRow[0])) {
					MiSchedule manualApiSchedule = fileHandler.generateManualApiSchedule(firstRow, dateTimeInfo);
					if (manualApiSchedule != null)
						manualApiSchedules.add(manualApiSchedule);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (inputReader != null) {
					inputReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return manualApiSchedules;
	}
	
	public int isSchedulesValid(List<MiSchedule> manualApiSchedules) {
		int flag = 0;
		try {
			int limit = configProperties.getSignalGap() * Constant.ONE_MIN_IN_MILLI;
			if (manualApiSchedules != null && !manualApiSchedules.isEmpty()) {
				for (int i = 0; i < manualApiSchedules.size() - 1; i++) {
					long diff = manualApiSchedules.get(i + 1).getStartTime() - manualApiSchedules.get(i).getStartTime();
					if(!isValidSignalGap(diff, limit)) {
						flag=i+getErrorRow();
						errorResponse.setErrorAtRow(flag);
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag;
	}
	
	private boolean isValidSignalGap(long gap, int limit) {
		if (gap <= 0){			
			errorResponse.setErrorMessage(Constant.DATA_INVALID_INC);
			return false;
		}else if(gap > limit){				
			errorResponse.setErrorMessage(Constant.DATA_INVALID);
			return false;
		}
		return true;
	}
	
	public List<MiSchedule> listSchedules(Map<String, List<String[]>> data, String date) {
		List<MiSchedule> manualApiSchedules = new ArrayList<>();
		try {
			DateTimeInfo dateTimeInfo = helper.generateDTI(date,
					data.get(configProperties.getReqParamTimeZone()).get(0)[0]);
			List<String[]> dataList = data.get(configProperties.getReqParamFile());
			errorResponse.setIsTimeString(fileHandler.isTimeString(dataList.get(0)[0]));
			for (String[] firstRow : dataList) {
				if (fileHandler.isBlankRow(firstRow) && fileHandler.isTimeString(firstRow[0])) {
					MiSchedule manualApiSchedule = fileHandler.generateManualApiSchedule(firstRow, dateTimeInfo);
					if (manualApiSchedule != null)
						manualApiSchedules.add(manualApiSchedule);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return manualApiSchedules;
	}

	public int getErrorRow(){
		int flag =0;
		if(!errorResponse.isTimeString()){
			flag = 3;
		}else{
			flag=2;
		}
		return flag;
	}
}