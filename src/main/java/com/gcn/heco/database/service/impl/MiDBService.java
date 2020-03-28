package com.gcn.heco.database.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.dao.IMiDao;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.database.service.IMiDBService;

@Repository
public class MiDBService implements IMiDBService {
	
	static Logger logger = LoggerFactory.getLogger(MiDBService.class);
	
	@Autowired
	private IMiDao iMiDao;

	@Override
	public void saveOrUpdate(MiSchedule maualApiSchedule) {
		iMiDao.saveOrUpdate(maualApiSchedule);
	}

	@Override
	public void updateMISchedules(long time, String timeZone) {
		iMiDao.updateMISchedules(time, timeZone);
	}

	@Override
	public void deleteMISchedules(long time, String timeZone) {
		iMiDao.deleteMISchedules(time, timeZone);
	}	

	@Override
	public void saveOrUpdate(List<MiSchedule> list, int batchSize) {
		iMiDao.saveOrUpdate(list, batchSize);
	}

	@Override
	public List<MiSchedule> getWindowSchedules(long startTime, int window) {
		List<MiSchedule> miSchedules = iMiDao.getWindowSchedules(startTime, window);
		if (miSchedules != null && !miSchedules.isEmpty()) {
			iMiDao.updateStatus(Constant.MI_STATUS_INPOST, startTime, window);
		}
		return miSchedules;
	}

	@Override
	public List<MiSchedule> getFirstWindowSchedules(long startTime, int window) {
		List<MiSchedule> miSchedules = iMiDao.getFirstWindowSchedules(startTime, window);
		if (miSchedules != null && !miSchedules.isEmpty()) {
			iMiDao.updateInitStatus(Constant.MI_STATUS_INPOST, startTime, window);
		}
		return miSchedules;
	}

	@Override
	public void archiveData(int duration, int expiration) {
		iMiDao.archiveData(duration, expiration);
	}

	@Override
	public MiSchedule getById(Long id) {	
		return iMiDao.getById(id);
	}
}
