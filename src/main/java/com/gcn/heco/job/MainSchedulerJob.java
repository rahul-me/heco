package com.gcn.heco.job;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.manager.IMiManager;
import com.gcn.heco.schedule.EventSchedule;

@Component
public class MainSchedulerJob implements IMainScheduleJobbable {

	private static Logger logger = LoggerFactory.getLogger(MainSchedulerJob.class);

	private static volatile boolean firstTime = true;

	public static boolean isFirstTime() {
		return firstTime;
	}

	public static void setFirstTime(boolean firstTime) {
		MainSchedulerJob.firstTime = firstTime;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("heco: Main scheduler's job started");
		doJob(context);
	}

	@Override
	public synchronized void doJob(JobExecutionContext context) {
		final long startTime = System.currentTimeMillis() + 3000;
		IMiManager iMiManager = null;
		EventSchedule eventSchedule = null;
		Scheduler scheduler = null;

		try {
			scheduler = context.getScheduler();
			SchedulerContext schedulerContext = scheduler.getContext();
			iMiManager = (IMiManager) schedulerContext.get(Constant.MI_MANAGER);
			eventSchedule = (EventSchedule) schedulerContext.get(Constant.EVENT_SCH);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}

		if (iMiManager != null && eventSchedule != null) {
			try {
				if (!scheduler.isShutdown()) {
					processJob(scheduler, startTime, iMiManager, eventSchedule);
				} else {
					logger.info("heco: Caught scheduler shutdown while going to get window schedule from db.");
				}
			} catch (SchedulerException se) {
				logger.error(se.getMessage(), se);
			}
		} else {
			logger.info("Scheduler has not been configured properly");
		}

		if (firstTime) {
			firstTime = false;
		}

		logger.info("heco: Main scheduler's job finished.");
	}

	private void processJob(Scheduler scheduler, long startTime, IMiManager iMiManager, EventSchedule eventSchedule) {
		// get schedules from db
		List<MiSchedule> miSchedules = new LinkedList<>(
				firstTime ? iMiManager.getFirstScheduleWindow(startTime) : iMiManager.getScheduleWindow(startTime));
		logger.info("heco: Got " + miSchedules.size() + " schedule(s) to schedule.");

		try {
			if (!scheduler.isShutdown()) {
				scheduleEvents(eventSchedule, miSchedules);
			} else {
				logger.info("heco: Caught scheduler shutdown in the begining of scheduling event schedules");
			}
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void scheduleEvents(EventSchedule eventSchedule, List<MiSchedule> miSchedules) {
		// generate quartz schedules from it
		for (MiSchedule miSchedule : miSchedules) {
			try {
				eventSchedule.scheduleMe(miSchedule);
			} catch (IllegalStateException e) {
				logger.info("heco: Caught scheduler shutdown in the middle of scheduling an event scheduler.");
				break;
			}

		}
	}

}
