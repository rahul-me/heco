package com.gcn.heco.schedule;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.manager.IMiManager;
import com.gcn.heco.schedule.service.IEventSchduleService;

@Component
public class EventSchedule implements IEventSchedule{
	
	private static Logger logger = LoggerFactory.getLogger(EventSchedule.class);
	
	@Autowired
	private IEventSchduleService iEventScheduleService;
	
	@Autowired
	private IMiManager iMiManager;
	
	private Scheduler scheduler;
	
	
	
	public Scheduler getScheduler() {
		return scheduler;
	}

	@PostConstruct
	@Override
	public void start() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.getContext().put(Constant.MI_MANAGER, iMiManager);
			scheduler.start();
		} catch (SchedulerException e) {		
			logger.error(e.getMessage(), e);
		}		
	}
		
	@Override
	public void scheduleMe(MiSchedule miSchedule) {		
		iEventScheduleService.schedule(scheduler, miSchedule);
	}

	@Override
	public void stop() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {		
			logger.error(e.getMessage(), e);
		}
	}
}
