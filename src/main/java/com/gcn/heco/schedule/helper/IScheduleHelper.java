package com.gcn.heco.schedule.helper;

import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;

public interface IScheduleHelper {
	
	Set<JobKey> getJobKeys(Scheduler scheduler);
}
