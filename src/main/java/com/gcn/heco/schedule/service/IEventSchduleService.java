package com.gcn.heco.schedule.service;

import org.quartz.Scheduler;

import com.gcn.heco.database.model.MiSchedule;

public interface IEventSchduleService {
	void schedule(Scheduler scheduler, MiSchedule miSchedule);
}
