package com.gcn.heco.app.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DateTimeInfo {
	
	// test line
	
	static Logger logger = LoggerFactory.getLogger(DateTimeInfo.class);

	private String date;
	
	private String timeZone;
	
	private String year;
	private String month;
	private String day;
	
	private String hours;
	private String minutes;
	private String seconds;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
		seperateDatePart(date);
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
		makeDate();
	}

	public String getMonth() {
		return month;		
	}

	public void setMonth(String month) {
		this.month = month;
		makeDate();
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
		makeDate();
	}
	
	
	public String getHours() {
		return hours;
	}

	public String getMinutes() {
		return minutes;
	}

	public String getSeconds() {
		return seconds;
	}

	private void seperateDatePart(String date){
		try{
			String[] datePart = date.split("-");
			year = datePart[0];
			month = datePart[1];
			day = datePart[2];
		} catch(Exception e){
			logger.error(e.getMessage());
		}
		
	}
	
	public void addTimePart(String timeString){
		String[] timePart = timeString.split(":");
		hours = timePart[0];
		minutes = timePart[1];
		seconds = timePart[2];
	}
	
	private void makeDate(){
		date = year+"-"+month+"-"+day;
	}
	
	
	
	
}
