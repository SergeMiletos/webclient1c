package com.webclient1c.release02.appclasses;

public class DocumentListSaleOrders {
    	private String CheckPerformed;
    	private String DocNumber;
    	private String DocDate;
    	private String Company, Partner, Contract, Stock, Author, SaleOrder;
    	private String DocTotalSum, DebtBalance;
    	
    	public DocumentListSaleOrders(String CheckPerformed, String DocNumber, String DocDate, String Company,
    			String Partner, String Contract, String Stock, String DocTotalSum, String Author, 
    			String DebtBalance, String SaleOrder) {
    		this.CheckPerformed = CheckPerformed;
    		this.DocNumber = DocNumber;
    		this.DocDate = DocDate;
    		this.Company = Company;
    		this.Partner = Partner;
    		this.Contract = Contract;
    		this.Stock = Stock;
    		this.DocTotalSum = DocTotalSum;
    		this.Author = Author;
    		this.DebtBalance = DebtBalance;
    		this.SaleOrder = SaleOrder;
    	}
    	
    	@SuppressWarnings("unused")
    	public String getCheckPerformed () {return this.CheckPerformed;}
    	@SuppressWarnings("unused")
    	public void setCheckPerformed (String inpVal) {this.CheckPerformed = inpVal;}
    	@SuppressWarnings("unused")
    	public String getDocNumber () {return this.DocNumber;}
    	@SuppressWarnings("unused")
    	public void setDocNumber (String inpVal) {this.DocNumber = inpVal;}
    	@SuppressWarnings("unused")
    	public String getDocDate () {return this.DocDate;}
    	@SuppressWarnings("unused")
    	public void setDocDate (String inpVal) {this.DocDate = inpVal;}
    	@SuppressWarnings("unused")
    	public String getCompany () {return this.Company;}
    	@SuppressWarnings("unused")
    	public void setCompany (String inpVal) {this.Company = inpVal;}
    	@SuppressWarnings("unused")
    	public String getPartner () {return this.Partner;}
    	@SuppressWarnings("unused")
    	public void setPartner (String inpVal) {this.Partner = inpVal;}
    	@SuppressWarnings("unused")
    	public String getContract () {return this.Contract;}    	
    	@SuppressWarnings("unused")
    	public void setContract (String inpVal) {this.Contract = inpVal;}
    	@SuppressWarnings("unused")
    	public String getStock () {return this.Stock;}
    	@SuppressWarnings("unused")
    	public void setStock (String inpVal) {this.Stock = inpVal;}
    	@SuppressWarnings("unused")
    	public String getDocTotalSum () {return this.DocTotalSum;}
    	@SuppressWarnings("unused")
    	public void setDocTotalSum (String inpVal) {this.DocTotalSum = inpVal;}
    	@SuppressWarnings("unused")
    	public String getAuthor () {return this.Author;}
    	@SuppressWarnings("unused")
    	public void setAuthor (String inpVal) {this.Author = inpVal;}
    	@SuppressWarnings("unused")
    	public String getDebtBalance () {return this.DebtBalance;}
    	@SuppressWarnings("unused")
    	public void setDebtBalance (String inpVal) {this.DebtBalance = inpVal;}
    	@SuppressWarnings("unused")
    	public String getSaleOrder () {return this.SaleOrder;}
    	@SuppressWarnings("unused")
    	public void setSaleOrder (String inpVal) {this.SaleOrder = inpVal;}
    	
    }
