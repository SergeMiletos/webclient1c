package com.webclient1c.release02.appclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class CustomerInvoice {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(CustomerInvoice.class);
	public String company;
	public String partner;
	public String contract;
	
	public CustomerInvoice() {
		this.company = "Test GMbH";
		this.partner = "Some customer";
		this.contract = "Default contract";
	}
	
	String getCompany() {
		return this.company;
	}
	
	String getPartner() {
		return partner;
	}
	
	String getContract() {
		return contract;
	}
	
	void setCompany(String value) {
		company = value;
	}
	
	void setPartner(String value) {
		partner = value;
	}
	
	void setContract(String value) {
		contract = value;
	}
	
	
}