package com.gcn.heco.database.dao;

import java.util.List;

import javax.transaction.Transactional;

import com.gcn.heco.database.model.MiSchedule;

@Transactional
public interface IMiDao {
		
	public void saveOrUpdate(MiSchedule maualApiSchedule);
	
	public void updateMISchedules(long time, String timeZone);
	
	public void deleteMISchedules(long time, String timeZone);	
	
	public void archiveData(int duration, int expiration);	
	
	public void saveOrUpdate(List<MiSchedule> list, int batchSize);	
	
	public List<MiSchedule> getWindowSchedules(long startTime, int window);
	
	public List<MiSchedule> getFirstWindowSchedules(long startTime, int window);
	
	public void updateStatus(String status, long startTime, int window);
	
	public void updateInitStatus(String status, long startTime, int window);
	
	public MiSchedule getById(Long id);
}
