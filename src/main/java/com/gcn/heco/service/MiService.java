package com.gcn.heco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.httpservice.HttpServiceHandler;

@Service
public class MiService implements IMiService{
	
	@Autowired
	private HttpServiceHandler httpService;	
	
	@Override
	public void cancelSchdule(MiSchedule miSchedule) {		
		httpService.sendDeleteRequestToNOC(miSchedule);
	}

	@Override
	public void postSchedule(MiSchedule miSchedule) {		
		httpService.sendPostRequestToMI(miSchedule);		
	}
	
}
