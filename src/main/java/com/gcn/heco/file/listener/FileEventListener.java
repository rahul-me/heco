package com.gcn.heco.file.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcn.heco.app.model.DateTimeInfo;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.database.service.IMiDBService;
import com.gcn.heco.helper.HecoHelper;
import com.gcn.heco.schedule.EventSchedule;
import com.gcn.heco.schedule.controller.IScheduleController;
import com.gcn.heco.schedule.helper.IScheduleHelper;

@Service
public class FileEventListener implements IFileEventListener {

	private static Logger logger = LoggerFactory.getLogger(FileEventListener.class);

	@Autowired
	private IScheduleController iScheduleController;

	@Autowired
	private IMiDBService dbService;

	@Autowired
	private HecoHelper helper;

	@Autowired
	private IScheduleHelper scheduleHandler;

	@Autowired
	private EventSchedule eventSchedule;

	@Autowired
	private ConfigProperties config;

	@Override
	public void beforePost(List<MiSchedule> miSchedules, DateTimeInfo dateTimeInfo) {
		if (helper.isReqForCurrentDay(dateTimeInfo) && helper.isMiScheduleAvailableForWindow(miSchedules)) {
			logger.info("heco: Schedule window has been affected by post request.");
			iScheduleController.stopScheduling();
		}
	}

	@Override
	public void afterPost() {
		iScheduleController.reInitialize();
	}

	@Override
	public void beforePut(List<MiSchedule> miSchedules, DateTimeInfo dateTimeInfo) {
		if (helper.isReqForCurrentDay(dateTimeInfo) && (helper.isMiScheduleAvailableForWindow(miSchedules)
				|| (!scheduleHandler.getJobKeys(eventSchedule.getScheduler()).isEmpty())
				|| !dbService.getFirstWindowSchedules(helper.getCurrentTimeInMilliseconds(), config.getWindowTime())
						.isEmpty())) {
			logger.info("heco: Schedule window has been affected by put request.");
			iScheduleController.stopScheduling();
		}
	}

	@Override
	public void afterPut() {
		iScheduleController.reInitialize();
	}

	@Override
	public void beforeDelete(DateTimeInfo dateTimeInfo) {
		if(helper.isReqForCurrentDay(dateTimeInfo)) {
			iScheduleController.stopScheduling();
		}
	}

	@Override
	public void afterDelete() {
		iScheduleController.reInitialize();
	}

}
