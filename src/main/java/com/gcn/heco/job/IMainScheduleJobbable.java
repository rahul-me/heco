package com.gcn.heco.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public interface IMainScheduleJobbable extends Job {
	void doJob(JobExecutionContext context);
}
