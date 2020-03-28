package com.gcn.heco.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.database.service.IMiDBService;
import com.gcn.heco.schedule.container.ScheduleContainer;
import com.gcn.heco.service.IMiService;

@Service
public class MiManager implements IMiManager{
	
	static Logger logger = LoggerFactory.getLogger(MiManager.class);
	
	@Autowired
	private ScheduleContainer scheduleContainer;
	
	@Autowired
	private IMiService iMiService;
	
	@Autowired
	private ConfigProperties configProperties;
	
	@Autowired
	private IMiDBService iMiDBService;
	
	@Async
	@Override
	public void cancelActiveSchedules(){		
		List<MiSchedule> activeSchedules = new ArrayList<>(scheduleContainer.getActiveSchedules());
		logger.info("heco: Found "+activeSchedules.size()+" schedule(s) to be cancelled at NOC.");
		for(MiSchedule miSchedule : activeSchedules){
			iMiService.cancelSchdule(miSchedule);
		}
	}	
	
	@Override
	public List<MiSchedule> getScheduleWindow(long startTime) {
		return iMiDBService.getWindowSchedules(startTime, configProperties.getWindowTime());		
	}

	@Override
	public List<MiSchedule> getFirstScheduleWindow(long startTime) {
		return iMiDBService.getFirstWindowSchedules(startTime, configProperties.getWindowTime());
	}
		
	@Override
	public void postSchedule(MiSchedule miSchedule) {
		iMiService.postSchedule(miSchedule);
	}
	

	
	
}
