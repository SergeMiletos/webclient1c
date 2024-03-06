package com.webclient1c.release02.appclasses;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.BeanModelSourceBuilder;
import org.apache.tapestry5.beanmodel.PropertyModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.commons.internal.util.MessagesImpl;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.webclient1c.release02.services.HibernateSessionImpl;
import com.webclient1c.release02.entities.StockRef;
import com.webclient1c.release02.pages.DynaBeanGridExample;

public class QueryListItemClassBuilder {

    

    private Messages messages;
	    
    public static final String STOCK_LABEL = "_stock";
    public static final String PROD_CODE_LABEL = "PRODUCT_CODE";
    public static final String PROD_NAME_LABEL = "PRODUCT_NAME";
    public static final String GET_PREFIX = "Map.get('";
    public static final String GET_COMPL = "')";
    
	public QueryListItemClassBuilder () {
		this.messages = MessagesImpl.forClass(DynaBeanGridExample.class);
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> buildClass() {
  Session hSessionImpl;
		BeanModelSource beanModelSource = new BeanModelSourceBuilder().build();
		hSessionImpl = new HibernateSessionImpl().createSessionFactory().openSession();
		BeanModel<BasicDynaBean> modelInstance = beanModelSource.createEditModel(BasicDynaBean.class, messages);
		
		Query sq = hSessionImpl.createQuery("FROM StockRef AS stocks");
		List<StockRef> stockList = sq.getResultList();
    	
		DynaProperty[] props = new DynaProperty[4+stockList.size()];
    	props[0] = new DynaProperty("ID", String.class);
    	props[1] = new DynaProperty(PROD_CODE_LABEL, String.class);
    	props[2] = new DynaProperty(PROD_NAME_LABEL, String.class);
    	props[3] = new DynaProperty("UOM", String.class);
    	
    	for (int i = 4; i < stockList.size()+4; i++) {
    		props[i] = new DynaProperty(STOCK_LABEL+stockList.get(i-4).getStockCode(), String.class);
    	}

        modelInstance.addExpression("ID", GET_PREFIX+"ID"+GET_COMPL);     
		PropertyModel currField = modelInstance.get("ID");
		currField.label("UUID товара");

		modelInstance.addExpression(PROD_CODE_LABEL, GET_PREFIX+PROD_CODE_LABEL+GET_COMPL);
		currField = modelInstance.get(PROD_CODE_LABEL);
		currField.label("Код товара");

		modelInstance.addExpression(PROD_NAME_LABEL, GET_PREFIX+PROD_NAME_LABEL+GET_COMPL);        
		currField = modelInstance.get(PROD_NAME_LABEL);
		currField.label("Наименование товара");

		modelInstance.addExpression("UOM", GET_PREFIX+"UOM"+GET_COMPL);        
		currField = modelInstance.get("UOM");
		currField.label("Ед. изм.");
		
		// Set stocks labels here
		for (int i = 4; i < stockList.size()+4; i++) {
    		modelInstance.addExpression(STOCK_LABEL+stockList.get(i-4).getStockCode(), 
    				GET_PREFIX+STOCK_LABEL+stockList.get(i-4).getStockCode()+GET_COMPL);        
    		currField = modelInstance.get(STOCK_LABEL+stockList.get(i-4).getStockCode());
    		currField.label(stockList.get(i-4).getStockName());
    	}

		hSessionImpl.close();
    	HashMap<String, Object> resultMap = new HashMap<>();
    	resultMap.put("modelInstance", modelInstance);
    	resultMap.put("queryListItemClass", new BasicDynaClass("queryListElem", BasicDynaBean.class, props));
    	
		return resultMap;
	}
}