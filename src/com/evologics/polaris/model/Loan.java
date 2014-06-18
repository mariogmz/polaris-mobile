package com.evologics.polaris.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Loan {
	
	private int loan_id;
	private String objectName;
	private String loanOwner;
	private Date dateStart;
	private Date dateEnd;
	private String note;
	private String category;
	
	public static enum Loan_State{On_Time,Expired,Refunded};
	
	private Loan_State currentState;
	
	/**
	 * @param objectName
	 * @param loanOwner
	 * @param dateStart
	 * @param dateEnd
	 * @param note
	 * @param category
	 * @param currentState
	 */
	public Loan(int loan_id, String objectName, String loanOwner, Date dateStart,
			Date dateEnd, String note, String category, Loan_State currentState) {
		this.objectName = objectName;
		this.loanOwner = loanOwner;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.note = note;
		this.category = category;
		this.currentState = currentState;
	}
	
	/*
	 * This special constructor parses a JSON object
	 * to map this Java object
	 */
	
	public Loan(JSONObject json){
		try {
			//Object id
			this.loan_id = json.getInt("user_id");
			//ObjectName
			this.objectName = json.optString("concepto");
			//LoanOwner
			this.loanOwner = json.optString("contacto");
			//Dates parsing
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			//Date Start
			this.dateStart = sdf.parse(json.optString("fecha_prestamo"));
			//Date End
			this.dateEnd = sdf.parse(json.optString("fecha_entrega"));
	
			//Detail
			this.note = json.optString("detalle");
			//Category
			this.category = "";
			
			boolean regresado = json.isNull("regresado") ? false : true;
			if(regresado){
				this.currentState = Loan_State.Refunded;
			}else{
				this.currentState = new Date().after(dateEnd)? Loan_State.Expired : Loan_State.On_Time;
			}
		}catch(JSONException ex){
			Log.d("ERROR","JSON:" + ex.getMessage());
			ex.printStackTrace();
		}catch(ParseException ex){
			Log.d("ERROR","PARSING:" + ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}

	public int getLoan_id() {
		return loan_id;
	}

	public void setLoan_id(int loan_id) {
		this.loan_id = loan_id;
	}

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
