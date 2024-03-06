package com.webclient1c.release02.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "products_stock_levels")
public class ProductsStockLevels {

	@Id
	@Column(name = "id")
	private String rowId;
	
	@Column(name="product_code")
	private String productCode;
	
	@Column(name="product_name")
	private String productName;

	@Column(name="uom")
	private String unitsOfMesure;
	
	@Column(name="stock_quantity_values", columnDefinition = "JSONB")
	@ColumnTransformer(write = "?::jsonb")
	private String stockQuantityValues;

	public ProductsStockLevels() {
		// Default empty constructor
	}
	
	public ProductsStockLevels(String rowId, String productCode, String productName, String unitsOfMesure,
			String stockQuantityValues) {
		this.rowId = rowId;
		this.productCode = productCode;
		this.productName = productName;
		this.unitsOfMesure = unitsOfMesure;
		this.stockQuantityValues = stockQuantityValues;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnitsOfMesure() {
		return unitsOfMesure;
	}

	public void setUnitsOfMesure(String unitsOfMesure) {
		this.unitsOfMesure = unitsOfMesure;
	}

	public String getStockQuantityValues() {
		return stockQuantityValues;
	}

	public void setStockQuantityValues(String stockQuantityValues) {
		this.stockQuantityValues = stockQuantityValues;
	}

}
