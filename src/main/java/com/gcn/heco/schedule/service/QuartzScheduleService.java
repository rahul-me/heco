package com.gcn.heco.schedule.service;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.job.EventSchedulerJob;
import com.gcn.heco.job.MainSchedulerJob;

@Service
public class QuartzScheduleService implements IEventSchduleService, IMainScheduleService {

	private static Logger logger = LoggerFactory.getLogger(QuartzScheduleService.class);

	@Autowired
	private ConfigProperties configProperties;

	@Override
	public void schedule(Scheduler scheduler) {

		JobDetail job = JobBuilder.newJob(MainSchedulerJob.class).withIdentity(Constant.MAIN_JOB_KEY).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(Constant.MAIN_JOB_TRIGGER).withSchedule(SimpleScheduleBuilder
				.simpleSchedule().withIntervalInMinutes(configProperties.getFrequency()).repeatForever()).build();

		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void schedule(Scheduler scheduler, MiSchedule miSchedule) {
		try {

			JobDetail job = JobBuilder.newJob(EventSchedulerJob.class)
					.withIdentity(String.valueOf(miSchedule.getId()), Constant.MI_SCH_GROUP).build();
			JobDataMap jobDataMap = job.getJobDataMap();
			jobDataMap.put(Constant.MI_SCH, miSchedule);

			Trigger trigger = TriggerBuilder.newTrigger().startAt(new Date(miSchedule.getStartTime())).build();

			if (scheduler.isShutdown()) {
				logger.info("heco: Scheduler has been shutdown");
			}

			
			scheduler.scheduleJob(job, trigger);
			

		} catch (SchedulerException e) {
			throw new IllegalStateException("Exception in scheduling an event job.", e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new IllegalStateException();
		}

	}

}
