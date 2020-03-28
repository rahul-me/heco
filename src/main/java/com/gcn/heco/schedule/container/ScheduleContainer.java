package com.gcn.heco.schedule.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gcn.heco.database.model.MiSchedule;

@Component
public class ScheduleContainer {
	
	private volatile List<MiSchedule> activeSchedules = Collections.synchronizedList(new ArrayList<MiSchedule>());

	public synchronized List<MiSchedule> getActiveSchedules() {
		List<MiSchedule> aveSchedules = new ArrayList<>(activeSchedules);
		refreshList();
		return aveSchedules;
	}
	
	private void refreshList(){
		activeSchedules.clear();
	}
	
	public void addActiveSchedule(MiSchedule miSchedule){
		activeSchedules.add(miSchedule);
	}
	
}
