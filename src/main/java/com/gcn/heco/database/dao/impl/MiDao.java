package com.gcn.heco.database.dao.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.dao.IMiDao;
import com.gcn.heco.database.model.MiSchedule;

@Repository
public class MiDao implements IMiDao {

	static Logger logger = LoggerFactory.getLogger(MiDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public synchronized void saveOrUpdate(MiSchedule maualApiSchedule) {
		try {
			getSession().saveOrUpdate(maualApiSchedule);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void saveOrUpdate(List<MiSchedule> list, int batchSize) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				session.save(list.get(i));
				if ((i % batchSize) == 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			transaction.commit();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	@Override
	public void updateStatus(String status, long startTime, int window) {
		try {
			String hql = "update " + Constant.MI_SCH_TABLE
					+ " set status = :status where startTime >= :startTime and startTime <= :windowEndTime";
			Query query = getSession().createQuery(hql).setString(Constant.STATUS, status)
					.setLong(Constant.MI_START_TIME, startTime)
					.setLong("windowEndTime", (startTime + (window * Constant.ONE_MIN_IN_MILLI)));
			int updates = query.executeUpdate();
			logger.info("heco:: Found "+updates+" schedule(s) to set status as inpost");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void updateInitStatus(String status, long startTime, int window) {
		try {
			String hql = "update " + Constant.MI_SCH_TABLE
					+ " set status = :status where startTime >= :startTime and startTime <= :windowEndTime and status = :statusInit";
			Query query = getSession().createQuery(hql).setString(Constant.STATUS, status)
					.setLong(Constant.MI_START_TIME, startTime)
					.setLong("windowEndTime", (startTime + (window * Constant.ONE_MIN_IN_MILLI)))
					.setString("statusInit", Constant.MI_STATUS_INIT);
			int updates = query.executeUpdate();
			logger.info("heco:: Found "+updates+" schedule(s) to set status as inpost");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MiSchedule> getWindowSchedules(long startTime, int window) {		
		try {
			Criteria criteria = getSession().createCriteria(MiSchedule.class);
			criteria.add(Restrictions.ge(Constant.MI_START_TIME, startTime));
			criteria.add(Restrictions.le(Constant.MI_START_TIME, (startTime + (window * Constant.ONE_MIN_IN_MILLI))));
			criteria.add(Restrictions.eq(Constant.STATUS, Constant.MI_STATUS_INIT));
			return criteria.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MiSchedule> getFirstWindowSchedules(long startTime, int window) {		
		try {
			Criteria criteria = getSession().createCriteria(MiSchedule.class);
			criteria.add(Restrictions.ge(Constant.MI_START_TIME, startTime));
			criteria.add(Restrictions.le(Constant.MI_START_TIME, (startTime + (window * Constant.ONE_MIN_IN_MILLI))));
			criteria.add(Restrictions.or(Restrictions.eq(Constant.STATUS, Constant.MI_STATUS_INIT),
					Restrictions.eq(Constant.STATUS, Constant.MI_STATUS_INPOST)));
			return criteria.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	@Override
	public void deleteMISchedules(long time, String timeZone) {
		try {
			String hql = "delete "+Constant.MI_SCH_TABLE+" where "+Constant.MI_START_TIME+">= :time  and "+Constant.MI_START_TIME+" < :endTime and timeZone = :timeZone";			
			Query query = getSession().createQuery(hql).setLong("time", (time - (time % 1000))).setLong("endTime", (time + Constant.ONE_DAY_IN_MILLI)).setString("timeZone", timeZone);
			int updates = query.executeUpdate();
			logger.info("heco: "+updates+" have been found for delete request.");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void updateMISchedules(long time, String timeZone) {
		try {
			String hql = "delete " + Constant.MI_SCH_TABLE
					+ " where "+Constant.MI_START_TIME+">= :time  and "+Constant.MI_START_TIME+" < :endTime and timeZone = :timeZone";
			Query query = getSession().createQuery(hql).setLong("time", (time - (time % 1000))).setLong("endTime", (time + Constant.ONE_DAY_IN_MILLI)).setString("timeZone", timeZone);
			int updates = query.executeUpdate();
			logger.info("heco: "+updates+" old data found for delete for update request.");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void archiveData(int duration, int expiration) {
		try {
			Query query = getSession().createSQLQuery("CALL archive(:duration, :expiration)");
			query.setInteger("duration", duration).setInteger("expiration", expiration);
			int updates = query.executeUpdate();
			logger.info("heco: Archive performed on "+updates+" entries." );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public MiSchedule getById(Long id) {
		Session session = getSession();
		MiSchedule miSchedule = null;
		try {
			Serializable getById = id;
			miSchedule = session.get(MiSchedule.class, getById);
		} catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return miSchedule;
	}
}
