package com.gcn.heco.schedule.service;

import org.quartz.Scheduler;

public interface IMainScheduleService {
	void schedule(Scheduler scheduler);
}
