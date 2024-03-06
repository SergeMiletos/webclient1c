package com.webclient1c.release02.appclasses;

import java.util.HashMap;

@SuppressWarnings("unused")
public class ProductsListWithStockLevels  {
        private String productCode;
        private String productName;
        private String unitsOfMesure;
        private String stockName;
        String stockLevel;
        private HashMap<String, Object> properties;
        
        public ProductsListWithStockLevels(Object... varargs) {
             this.productCode = (String) varargs[0]; // UUID
            this.productName = (String) varargs[1];
            this.unitsOfMesure = (String) varargs[2];
            this.stockName = (String) varargs[3];
            this.stockLevel = (String) varargs[4];
            this.properties = new HashMap<>();
            this.properties.put("Stock_1", "Test stock 1");
        }
        
        public String getProductCode () {
        	return this.productCode;
        	}
        
        public void setProductCode (String inpVal) {
        	this.productCode = inpVal;
        	}
        
        public String getProductUuid () {
        	return this.productCode;
        	}
        
        public void setProductUuid (String inpVal) {
        	this.productCode = inpVal;
        	}
        
        public String getProductName () {
        	return this.productName;
        	}
        
        public void setProductName (String inpVal) {
        	this.productName = inpVal;
        	}
        
        public String getUnitsOfMesure () {
        	return this.unitsOfMesure;
        	}
        
        public void setUnitsOfMesure (String inpVal) {
        	this.unitsOfMesure = inpVal;
        	}
        
        public String getStockName () {
        	return this.stockName;
        	}
        
        public void setStockName (String inpVal) {
        	this.stockName = inpVal;
        	}
        
        public String getStockLevel () {
        	return this.stockLevel;
        	}
        
        public void setStockLevel (String inpVal) {
        	this.stockLevel = inpVal;
        	}
        
        public String getProperty() {
        	return (String) this.properties.get("Stock_1");
        	}
    }
