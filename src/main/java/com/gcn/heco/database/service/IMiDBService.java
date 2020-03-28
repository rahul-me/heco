package com.gcn.heco.database.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gcn.heco.database.model.MiSchedule;

@Service
public interface IMiDBService {

	public void saveOrUpdate(MiSchedule maualApiSchedule);
	
	public void updateMISchedules(long time, String timeZone);
	
	public void deleteMISchedules(long time, String timeZone);
	
	public void archiveData(int duration, int expiration);
	
	public void saveOrUpdate(List<MiSchedule> list, int batchSize);
	
	public List<MiSchedule> getWindowSchedules(long startTime, int window);
	
	public List<MiSchedule> getFirstWindowSchedules(long startTime, int window);
	
	public MiSchedule getById(Long id);
}
