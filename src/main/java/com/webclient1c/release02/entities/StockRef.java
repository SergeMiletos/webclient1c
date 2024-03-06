package com.webclient1c.release02.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "stock_names")
public class StockRef {

	@Id
	@Column(name="stock_code")
	private String stockCode;
	
	@Column(name="stock_name")
	private String stockName;
	
	public StockRef() {
		// Default empty constructor
	}
	
	public String getStockCode() {
		return stockCode;
	}
	
	public void setStockCode(String value) {
		this.stockCode = value;
	}
	
	public String getStockName() {
		return stockName;
	}
	
	public void setStockName(String value) {
		this.stockName = value;
	}
	
	public boolean isEmpty() {
		return stockCode == null;
	}

}