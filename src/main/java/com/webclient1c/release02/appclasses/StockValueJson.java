package com.webclient1c.release02.appclasses;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockValueJson implements Serializable {
  
    private static final long serialVersionUID = -2789819989018660125L;

	private String stockCode;
      
    private Long quantity;

    public StockValueJson() {
    	// Default empty constructor
    	this.stockCode = null;
//    	this.quantity = 0L;
    }
    
	public StockValueJson(String stockCode, Long quantity) {
		this.stockCode = stockCode;
		this.quantity = quantity;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
  
	@Override
    public String toString() {
        return "{\"quantity\":"+quantity+",\"stockCode\":\""+stockCode+"\"}";
	}
}