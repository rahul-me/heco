package com.gcn.heco.app.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class NocMiSchedule
{

	private Integer manualInstrScheduleId;
	
	private String periodStartTime;
	
	private int periodDuration;

	private int granularity;
	
	private String daysOfWeekMask;

	private int dayOfMonth;

	private int monthOfYear;
	
	private long firstFireTime;

	private long lastFireTime;

	private int scheduleRepetitionType;

	private String timeZone;
	
	private int object;
	
	private int component;
	
	private String operator;
	
	private int priority;
	
	private int program;

	private float value;
	
	private long created;
	
	private long modified;

	private List<Vpp> vpps;

	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getManualInstrScheduleId() {
		return manualInstrScheduleId;
	}

	public void setManualInstrScheduleId(Integer manualInstrScheduleId) {
		this.manualInstrScheduleId = manualInstrScheduleId;
	}

	public String getPeriodStartTime() {
		return periodStartTime;
	}

	public void setPeriodStartTime(String periodStartTime) {
		this.periodStartTime = periodStartTime;
	}

	public int getPeriodDuration() {
		return periodDuration;
	}

	public void setPeriodDuration(int periodDuration) {
		this.periodDuration = periodDuration;
	}

	public int getGranularity() {
		return granularity;
	}

	public void setGranularity(int granularity) {
		this.granularity = granularity;
	}

	public String getDaysOfWeekMask() {
		return daysOfWeekMask;
	}

	public void setDaysOfWeekMask(String daysOfWeekMask) {
		this.daysOfWeekMask = daysOfWeekMask;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(int monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public long getFirstFireTime() {
		return firstFireTime;
	}

	public void setFirstFireTime(long firstFireTime) {
		this.firstFireTime = firstFireTime;
	}

	public long getLastFireTime() {
		return lastFireTime;
	}

	public void setLastFireTime(long lastFireTime) {
		this.lastFireTime = lastFireTime;
	}

	public int getScheduleRepetitionType() {
		return scheduleRepetitionType;
	}

	public void setScheduleRepetitionType(int scheduleRepetitionType) {
		this.scheduleRepetitionType = scheduleRepetitionType;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public int getObject() {
		return object;
	}

	public void setObject(int object) {
		this.object = object;
	}

	public int getComponent() {
		return component;
	}

	public void setComponent(int component) {
		this.component = component;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getProgram() {
		return program;
	}

	public void setProgram(int program) {
		this.program = program;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public List<Vpp> getVpps() {
		return vpps;
	}

	public void setVpps(List<Vpp> vpps) {
		this.vpps = vpps;
	}
}