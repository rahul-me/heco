package com.gcn.heco.constant;

import com.gcn.heco.database.model.MiSchedule;
import com.gcn.heco.manager.MiManager;
import com.gcn.heco.schedule.EventSchedule;

public class Constant {

	private Constant() {
	}

	public static final String MI_SCH_TABLE = MiSchedule.class.getName();

	public static final String MI_MANAGER = MiManager.class.getName();

	public static final String EVENT_SCH = EventSchedule.class.getName();

	public static final int FIRST_INDEX_IN_ARRAY = 0;

	public static final String MI_SCH = MiSchedule.class.getName();

	public static final String MI_SCH_GROUP = MiSchedule.class.getName();

	public static final int ONE_MIN_IN_MILLI = 60000;

	public static final String MI_STATUS_INIT = "INIT";

	public static final String MI_STATUS_INPOST = "INPOST";

	public static final String MI_STATUS_CANCELLED = "CANCELLED";

	public static final String MI_START_TIME = "startTime";

	public static final String STATUS = "status";
	
	public static final String BAD_REQ_REASON = "error";
	
	public static final String BAD_REQ_ROW = "errorRow";

	public static final String STATUS_RECEIVED = "RECEIVED";

	public static final String STATUS_NOT_RECEIVED = "NOT_RECEIVED";

	public static final String STATUS_INVALID = "RECEIVED WITH INVALID REQUEST";

	public static final long ONE_DAY_IN_MILLI = 24 * 60 * 60 * 1000L;

	public static final String DATA_INVALID_INC = "AGC file cannot contain sequential signals that go back in time";

	public static final String DATA_INVALID = "AGC file cannot contain signal gaps larger than 1 minute";
	
	public static final String MAIN_JOB_KEY = "mainjob";
	
	public static final String MAIN_JOB_TRIGGER = "mainjobtrigger";
}
