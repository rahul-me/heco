package com.gcn.heco.app.model;

import org.springframework.stereotype.Component;

// Class Vpp

@Component
public class Vpp
{
	private Integer manualInstrScheduleVppId;

	public Integer getManualInstrScheduleVppId() {
		return manualInstrScheduleVppId;
	}

	public void setManualInstrScheduleVppId(Integer manualInstrScheduleVppId) {
		this.manualInstrScheduleVppId = manualInstrScheduleVppId;
	}

	private String vppId;

	public String getVppId() { return this.vppId; }

	public void setVppId(String vppId) { this.vppId = vppId; }
}

