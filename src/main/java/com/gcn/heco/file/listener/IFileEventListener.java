package com.gcn.heco.file.listener;

import java.util.List;

import com.gcn.heco.app.model.DateTimeInfo;
import com.gcn.heco.database.model.MiSchedule;

public interface IFileEventListener {
	
	void beforePost(List<MiSchedule> miSchedules, DateTimeInfo dateTimeInfo);
	
	void afterPost();
	
	void beforePut(List<MiSchedule> miSchedules, DateTimeInfo dateTimeInfo);
	
	void afterPut();
	
	void beforeDelete(DateTimeInfo dateTimeInfo);
	
	void afterDelete();
}
