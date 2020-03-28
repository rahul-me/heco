package com.gcn.heco.schedule;

import com.gcn.heco.database.model.MiSchedule;

public interface IEventSchedule extends ISchedule {
	void scheduleMe(MiSchedule miSchedule);
}
