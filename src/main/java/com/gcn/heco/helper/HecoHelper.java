package com.gcn.heco.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcn.heco.app.model.DateTimeInfo;
import com.gcn.heco.app.model.NocMiSchedule;
import com.gcn.heco.config.ConfigProperties;
import com.gcn.heco.constant.Constant;
import com.gcn.heco.database.model.MiSchedule;

@Component
public class HecoHelper {

	static Logger logger = LoggerFactory.getLogger(HecoHelper.class);

	@Autowired
	private ConfigProperties configProperties;

	private ObjectMapper objectMapper = new ObjectMapper();

	private DateTimeInfo generateDateTimeInfo(String date, String timeZone) {
		DateTimeInfo dateTimeInfo = new DateTimeInfo();
		dateTimeInfo.setDate(date);
		dateTimeInfo.setTimeZone(timeZone);
		return dateTimeInfo;
	}

	public long getTimeInLong(DateTimeInfo dti, String timeString) {
		dti.addTimePart(timeString);
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone(dti.getTimeZone()));
		cal.set(getInt(dti.getYear()), getInt(dti.getMonth()) - 1, getInt(dti.getDay()), getInt(dti.getHours()),
				getInt(dti.getMinutes()), getInt(dti.getSeconds()));
		return cal.getTimeInMillis();
	}

	public long getTimeInLong(DateTimeInfo dti) {
		dti.addTimePart(configProperties.getStartTimeForTheDate());
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone(dti.getTimeZone()));
		cal.set(getInt(dti.getYear()), getInt(dti.getMonth()) - 1, getInt(dti.getDay()), getInt(dti.getHours()),
				getInt(dti.getMinutes()), getInt(dti.getSeconds()));
		return cal.getTimeInMillis();
	}

	public String getTimeInString(long milliseconds, String timeZone) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		SimpleDateFormat sdf = new SimpleDateFormat(configProperties.getDateFormat());
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdf.format(calendar.getTime());
	}

	public String getTimePartFromDateString(String dateString) {
		String[] dateStringPart = dateString.split(" ");
		return dateStringPart[1];
	}

	private int getInt(String val) {
		return Integer.parseInt(val);
	}

	public String[] getVppIDs(String vppIDs) {
		return vppIDs.split(",");
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public long getCurrentTimeInMilliseconds() {
		return Calendar.getInstance().getTimeInMillis();
	}

	public NocMiSchedule getMISObjectFromJSON(String jsonString) {
		try {
			return objectMapper.readValue(jsonString, NocMiSchedule.class);
		} catch (Exception e) {
			logger.error(" in parsing string for JsonObject: ", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Object getObjectFromJsonString(String jsonString, @SuppressWarnings("rawtypes") Class instance) {
		try {
			return objectMapper.readValue(jsonString, instance);
		} catch (Exception e) {
			logger.error(" in parsing string for JsonObject: " + e);
			return null;
		}
	}

	public String getJsonString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public String getJsonStringWithPrettyPrinter(Object obj) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public DateTimeInfo generateDTI(HttpServletRequest request) {
		return generateDateTimeInfo(request.getParameter(configProperties.getReqParamDate()),
				request.getParameter(configProperties.getReqParamTimeZone()));
	}

	public boolean isRequestValid(HttpServletRequest request, MultipartFile file) {
		boolean status = false;
		try {
			if (request != null && file != null) {
				if (isParamAvailable(configProperties.getReqParamDate(), request)) {
					if (isParamAvailable(configProperties.getReqParamTimeZone(), request)) {
						status = true;
					} else {
						status = false;
					}
				} else {
					status = false;
				}
			} else {
				status = false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	private boolean isParamAvailable(String paraName, HttpServletRequest request) {
		String para = null;
		para = request.getParameter(paraName);
		return (para != null && para.trim().length() > 0) ? true : false;
	}

	public Map<String, List<String[]>> isRequestValid(InputStream inputStream) {
		return getDataFromHTTPRequest(inputStream);
	}

	public Map<String, List<String[]>> getDataFromHTTPRequest(InputStream inputStream) {
		Map<String, List<String[]>> data = new HashMap<>();
		List<String[]> rowsFromFile = new ArrayList<>();
		List<String[]> timeZoneData = new ArrayList<>();
		try (Reader in = new InputStreamReader(inputStream); BufferedReader reader = new BufferedReader(in)) {
			writeData(reader, rowsFromFile, timeZoneData);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		data.put(configProperties.getReqParamFile(), rowsFromFile);
		data.put(configProperties.getReqParamTimeZone(), timeZoneData);
		return data;
	}

	private void writeData(BufferedReader reader, List<String[]> rowsFromFile, List<String[]> timeZoneData)
			throws IOException {
		String out = null;		
		while ((out = reader.readLine()) != null) {
			if (out.trim().length() > 0) {
				if (out.contains(",")) {					
					if (out.split(",").length > 0 && out.split(",")[0].trim().contains(":")) {
						rowsFromFile.add(out.split(","));
					}
				} else if (out.contains(configProperties.getReqParamTimeZone().trim())) {
					addTimeZone(reader, timeZoneData);
				}
			}
		}
	}
	
	private void addTimeZone(BufferedReader reader, List<String[]> timeZoneData) throws IOException {
		String timeZone = null;
		while (true) {
			timeZone = reader.readLine();
			if (timeZone == null || timeZone.trim().length() > 0)
				break;
		}
		timeZoneData.add(new String[] { timeZone });
	}

	public DateTimeInfo generateDTI(String dateString, String timeZone) {
		return generateDateTimeInfo(dateString, timeZone);
	}

	public boolean isReqForCurrentDay(DateTimeInfo dateTimeInfo) {
		long startTimeOfTheDay = getTimeInLong(dateTimeInfo);
		long currentTime = getCurrentTimeInMilliseconds();
		long endTimeOfTheDay = startTimeOfTheDay + Constant.ONE_DAY_IN_MILLI;
		boolean res = (currentTime >= (startTimeOfTheDay - (startTimeOfTheDay % 1000))
				&& currentTime <= (endTimeOfTheDay - (endTimeOfTheDay % 1000)));
		logger.info("isReqForCurrentDay: " + res);
		return res;
	}

	public boolean isMiScheduleAvailableForWindow(List<MiSchedule> miSchedules) {
		long windowStartTime = getCurrentTimeInMilliseconds();
		long windowEndTime = windowStartTime + (configProperties.getWindowTime() * Constant.ONE_MIN_IN_MILLI);
		long firstSchStartTime = miSchedules.get(0).getStartTime();
		long lastSchStartTime = miSchedules.get(miSchedules.size() - 1).getStartTime();
		boolean res = (firstSchStartTime >= windowStartTime && firstSchStartTime <= windowEndTime)
				|| (lastSchStartTime >= windowStartTime && lastSchStartTime <= windowEndTime)
				|| (firstSchStartTime <= windowStartTime && lastSchStartTime >= windowEndTime);
		logger.info("isMiScheduleAvailableForWindow: " + res);
		return res;
	}
}
