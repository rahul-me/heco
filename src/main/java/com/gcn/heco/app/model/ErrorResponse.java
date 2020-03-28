package com.gcn.heco.app.model;

import org.springframework.stereotype.Component;

@Component
public class ErrorResponse {

	private int errorAtRow;

	private String errorMessage;
	
	private boolean isTimeString;
	
	public int getErrorAtRow() {
		return errorAtRow;
	}

	public void setErrorAtRow(int errorAtRow) {
		this.errorAtRow = errorAtRow;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isTimeString() {
		return isTimeString;
	}

	public void setIsTimeString(boolean isTimeString) {
		this.isTimeString = isTimeString;
	}

	@Override
	public String toString() {
		return getErrorMessage()+":"+getErrorAtRow();
	}		
}
