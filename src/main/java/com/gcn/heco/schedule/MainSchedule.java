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
import com.gcn.heco.manager.IMiManager;
import com.gcn.heco.schedule.service.IMainScheduleService;

@Component
public class MainSchedule implements IMainSchedule {
	
	private static Logger logger = LoggerFactory.getLogger(MainSchedule.class);
	
	@Autowired
	private IMainScheduleService iMainSchedulerService;
	
	@Autowired
	private IMiManager iMiManager;
	
	@Autowired
	private EventSchedule eventSchedule;

	private Scheduler scheduler;
	
	public Scheduler getScheduler() {
		return scheduler;
	}

	@Override
	public void scheduleMe() {
		iMainSchedulerService.schedule(scheduler);		
	}


	@PostConstruct
	@Override
	public void start() {		
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.getContext().put(Constant.MI_MANAGER, iMiManager);
			scheduler.getContext().put(Constant.EVENT_SCH, eventSchedule);
			scheduler.start();			
		} catch (SchedulerException e) { 
			logger.error(e.getMessage(), e);
		}
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
