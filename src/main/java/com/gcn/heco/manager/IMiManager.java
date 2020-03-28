package com.gcn.heco.manager;

import java.util.List;

import com.gcn.heco.database.model.MiSchedule;

public interface IMiManager {	
	
	List<MiSchedule> getScheduleWindow(long startTime);
	
	List<MiSchedule> getFirstScheduleWindow(long startTime);
	
	void cancelActiveSchedules();
	
	void postSchedule(MiSchedule miSchedule);		
}
