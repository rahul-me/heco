package com.gcn.heco.schedule.helper;

import java.util.Collections;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gcn.heco.constant.Constant;

@Component
public class ScheduleHelper implements IScheduleHelper{

	private static Logger logger = LoggerFactory.getLogger(ScheduleHelper.class);
	
	@Override
	public Set<JobKey> getJobKeys(Scheduler scheduler) {
		Set<JobKey> jobKeys = Collections.emptySet();
		try {
			if(scheduler != null && !scheduler.isShutdown()) {
				jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Constant.MI_SCH_GROUP));
			}
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return jobKeys;
	}

}
