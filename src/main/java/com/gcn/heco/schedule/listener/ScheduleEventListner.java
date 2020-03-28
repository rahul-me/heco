package com.gcn.heco.schedule.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcn.heco.app.model.NocMiSchedule;
import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.database.service.IMiDBService;
import com.gcn.heco.schedule.container.ScheduleContainer;

@Component
public class ScheduleEventListner implements IScheduleEventListener {
	
	static Logger logger = LoggerFactory.getLogger(ScheduleEventListner.class);
	
	@Autowired
	private ScheduleContainer scheduleContainer;
	
	@Autowired
	private IMiDBService iMiDBService;
	
	@Override
	public void afterPost(MiSchedule miSchedule, NocMiSchedule nocMiSchedule) {
		if(nocMiSchedule != null){
			Integer miId = nocMiSchedule.getManualInstrScheduleId();
			logger.info("heco: Response from NOC for post request for schedule having id "+miSchedule.getId());
			if(miId != null){
				miSchedule.setScheduleId(miId);
				scheduleContainer.addActiveSchedule(miSchedule);
				logger.info("heco: Schedule having id "+miSchedule.getId()+" has been added to active schedule list");
			}
		} else {
			logger.warn("heco: Troble getting response from NOC for post request for schedule having id "+miSchedule.getId());
		}
		
	}

	@Override
	public void afterCancel(MiSchedule miSchedule, NocMiSchedule nocMiSchedule) {
		if(nocMiSchedule != null && miSchedule != null){
			logger.info("heco: Response from NOC for cancel request for schedule having id "+miSchedule.getId());
			miSchedule.setStatus(Constant.MI_STATUS_CANCELLED);
			MiSchedule miSchedule2 = iMiDBService.getById(miSchedule.getId());
			if(miSchedule2 != null) {				
				iMiDBService.saveOrUpdate(miSchedule);
			}
		}
	}

}
