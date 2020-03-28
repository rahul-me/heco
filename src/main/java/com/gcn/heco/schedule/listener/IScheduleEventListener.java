package com.gcn.heco.schedule.listener;

import com.gcn.heco.app.model.NocMiSchedule;
import com.gcn.heco.database.model.MiSchedule;

public interface IScheduleEventListener {
	
	void afterPost(MiSchedule miSchedule, NocMiSchedule nocMiSchedule);
	
	void afterCancel(MiSchedule miSchedule, NocMiSchedule nocMiSchedule);	
}
