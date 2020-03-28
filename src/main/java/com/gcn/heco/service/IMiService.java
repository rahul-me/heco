package com.gcn.heco.service;

import com.gcn.heco.database.model.MiSchedule;

public interface IMiService {
	
	void cancelSchdule(MiSchedule miSchedule);
	
	void postSchedule(MiSchedule miSchedule);
	
}
