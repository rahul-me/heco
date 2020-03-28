package com.gcn.heco.schedule.controller;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcn.heco.job.MainSchedulerJob;
import com.gcn.heco.schedule.EventSchedule;
import com.gcn.heco.schedule.MainSchedule;

@Service
public class ScheduleController implements IScheduleController {

	private static Logger logger = LoggerFactory.getLogger(MainSchedulerJob.class);

	@Autowired
	private MainSchedule mainSchedule;

	@Autowired
	private EventSchedule eventSchedule;
		
	public EventSchedule getEventSchedule() {
		return eventSchedule;
	}

	@Override
	public void stopScheduling() {
		synchronized (mainSchedule) {
			synchronized (eventSchedule) {
				logger.info("heco: Stopping scheduling.");
				mainSchedule.stop();
				eventSchedule.stop();
				logger.info("heco: Scheduling stopped.");
			}
		}
	}

	@Override
	public void reInitialize() {
		synchronized (mainSchedule) {
			synchronized (eventSchedule) {
				Scheduler scheduler = mainSchedule.getScheduler();
				try {
					if (scheduler.isShutdown()) {
						logger.info("heco: Restarting scheduling.");
						MainSchedulerJob.setFirstTime(true);
						mainSchedule.start();
						eventSchedule.start();
						logger.info("heco: Scheduling restarted.");
						triggerMainSchedule();
					}

				} catch (SchedulerException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@PostConstruct
	public void triggerMainSchedule() {
		logger.info("heco: Scheduling main scheduler");
		mainSchedule.scheduleMe();
		logger.info("heco: Main schedule has been scheduled.");
	}
}
