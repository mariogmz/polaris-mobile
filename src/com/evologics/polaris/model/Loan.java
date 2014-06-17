package com.evologics.polaris.model;

import java.util.Date;

public class Loan {
	
	private String objectName;
	private String loanOwner;
	private Date dateStart;
	private Date dateEnd;
	private String note;
	private String category;
	
	public static enum Loan_State{On_Time,Expired,Refunded};
	
	private Loan_State currentState;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getLoanOwner() {
		return loanOwner;
	}

	public void setLoanOwner(String loanOwner) {
		this.loanOwner = loanOwner;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Loan_State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Loan_State currentState) {
		this.currentState = currentState;
	}
	
	//Tools
	
	public boolean isExpired(){
		return this.currentState == Loan_State.Expired ? true : false;
	}
	
	public boolean isRefunded(){
		return this.currentState == Loan_State.Refunded? true : false;
	}
}
