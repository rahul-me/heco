package com.gcn.heco.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.manager.IMiManager;

@Component
public class EventSchedulerJob implements IEventSchduleJobbable {
	
	private static Logger logger = LoggerFactory.getLogger(EventSchedulerJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		doJob(context); 
	}

	@Override
	public void doJob(JobExecutionContext context) {		
		IMiManager iMiManager = null;
		
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();
			
			iMiManager = (IMiManager) schedulerContext.get(Constant.MI_MANAGER);					
		} catch (SchedulerException e) {		
			logger.error(e.getMessage(), e);
		}
						
		MiSchedule miSchedule = (MiSchedule) context.getJobDetail().getJobDataMap().get(Constant.MI_SCH);
		
		if(iMiManager != null){
			iMiManager.cancelActiveSchedules();
			if(miSchedule != null){
				iMiManager.postSchedule(miSchedule);
			}			
		} else {
			logger.warn("heco: Scheduler has not been configured properly");
		}
						
	}
	
	

}
