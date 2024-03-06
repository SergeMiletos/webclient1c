package com.webclient1c.release02.appclasses;

public class CustomerInvoiceProducts {
	
	String productUuid;
	String productName;
	String stock;
	String quantity;
	
	public CustomerInvoiceProducts() {
		this.productUuid = "Undefined";
		this.productName = "Undefined";
		this.stock = "Undefined";
		this.quantity = "0.00";
	}

	public CustomerInvoiceProducts(String productUuid, String productName, String stock, String quantity) {
		this.productUuid = productUuid;
		this.productName = productName;
		this.stock = stock;
		this.quantity = quantity;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public String getProductName() {
		return productName;
	}
	
	public String getStock() {
		return stock;
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
