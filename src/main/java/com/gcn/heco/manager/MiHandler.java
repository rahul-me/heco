package com.gcn.heco.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcn.heco.app.model.NocMiSchedule;
import com.gcn.heco.app.model.Vpp;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.helper.HecoHelper;

@Service
public class MiHandler {

	static Logger logger = LoggerFactory.getLogger(HecoHelper.class);

	@Autowired
	private HecoHelper helper;

	@Autowired
	private ConfigProperties configProperties;

	public NocMiSchedule generateMIRequestObject(MiSchedule manualApiSchedule) {
		NocMiSchedule schedule = null;
		try {
			float value = manualApiSchedule.getValue();
			value = value * -1;
			schedule = new NocMiSchedule();
			schedule.setManualInstrScheduleId(null);
			schedule.setPeriodStartTime(helper.getTimePartFromDateString(
					helper.getTimeInString(manualApiSchedule.getStartTime(), manualApiSchedule.getTimeZone())));
			schedule.setPeriodDuration(configProperties.getPeriodDuration());
			schedule.setGranularity(configProperties.getPeriodDuration());
			schedule.setDaysOfWeekMask(null);
			schedule.setDayOfMonth(0); // need to update
			schedule.setMonthOfYear(0);
			schedule.setFirstFireTime(manualApiSchedule.getStartTime());
			schedule.setLastFireTime(manualApiSchedule.getStartTime());
			schedule.setScheduleRepetitionType(0);
			schedule.setTimeZone(manualApiSchedule.getTimeZone());
			schedule.setObject(configProperties.getObject());
			schedule.setComponent(configProperties.getComponent());
			schedule.setOperator((value > 0) ? configProperties.getScheduleOperatorGE()
					: configProperties.getScheduleOperatorLE());
			schedule.setPriority(configProperties.getPriority());
			schedule.setProgram(configProperties.getProgram());
			schedule.setValue(value);
			schedule.setVpps(generateVppList());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return schedule;
	}

	private List<Vpp> generateVppList() {
		List<Vpp> vppList = new ArrayList<>();
		String[] vppIDs = helper.getVppIDs(configProperties.getVppIDs());
		for (String vppID : vppIDs) {
			Vpp vpp = new Vpp();
			vpp.setManualInstrScheduleVppId(null);
			vpp.setVppId(vppID);
			vppList.add(vpp);
		}
		return vppList;
	}
}
