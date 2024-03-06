package com.webclient1c.release02.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclient1c.release02.appclasses.StockValueJson;
import com.webclient1c.release02.entities.ProductsStockLevels;
import com.webclient1c.release02.entities.StockRef;


public class StockLevelsSyncServiceImpl implements StockLevelsSyncService {
	private static final Logger logger = LogManager.getLogger(StockLevelsSyncServiceImpl.class);
    public static final String EMPTY_CELL_VALUE = "---";
    private static final String SERVER1C_URL = "http://localhost:8073/testcm/hs/";
	
    @Inject
    Http1CRequestConnector http1CRequestConnectorImpl;	
	
	private Session session;
	
	private long formatLevel(String currStr) {
		String tmpVal = currStr.replaceAll("[^0-9.]", "");
		return currStr.contains(EMPTY_CELL_VALUE) ? Long.valueOf(0) : Long.valueOf(tmpVal);
    }
	
    private String requestStockLevelsData() throws IOException {
        return http1CRequestConnectorImpl.runJsonQuery(SERVER1C_URL+"productslist/ProductsWithStockLevel"
        		, "{\"RequestOperation\":\"GetFullProductsList\"}");

    }
	
    	
    private void transform1CProductsData(JSONArray dataSource) {
    	ArrayList<JSONArray> UIDSortedArray = new ArrayList<JSONArray>();

    	for (int i = 0; i < dataSource.length(); i++)
    		UIDSortedArray.add(dataSource.getJSONArray(i));
    	
    	Collections.sort(UIDSortedArray, new Comparator<JSONArray>() {
			@Override
			public int compare(JSONArray arg0, JSONArray arg1) {
				int compare = 0;
				try {
					compare = arg0.getString(0).compareTo(arg1.getString(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return compare;
			}
		});

/*    	UIDSortedArray.forEach(withCounter((i, element) -> { if (element.getString(0).contains("13033938-695e-11ed-bcd4-52540049e617"))   
    															System.out.println("ind "+i+", UID "+element.getString(0)); 
    														}));
*/    	
    	String previousUID = "";
    	String currentStockLevel = "[";
    	StockValueJson curArrayElem = new StockValueJson();
    	
    	for (int j = 0, jsize = UIDSortedArray.size(); j < jsize; j++) {

    		String currentUID = UIDSortedArray.get(j).getString(0);
    		JSONArray curRawData = UIDSortedArray.get(j);
    		
    		if ( !curRawData.getString(4).isEmpty() ) 
    			curArrayElem  = new StockValueJson(curRawData.getString(4),
    					formatLevel(curRawData.getString(6)));
    		else 
    			curArrayElem = new StockValueJson();
    			
    		
    		if (previousUID.isEmpty()) {
    			if ( curArrayElem.getStockCode() != null )
    				currentStockLevel = currentStockLevel.concat(curArrayElem.toString());

    		} else if (previousUID.equals(currentUID)) {
    			if (currentStockLevel.length() > 1) 
    				currentStockLevel = currentStockLevel.concat(",");
    			currentStockLevel = currentStockLevel.concat(curArrayElem.toString());
    			
    		} else if ( !previousUID.equals(currentUID) ) {
    			JSONArray prevRawData = UIDSortedArray.get(j - 1);
    			ProductsStockLevels product = new ProductsStockLevels(prevRawData.getString(0), prevRawData.getString(1), 
    					prevRawData.getString(2), prevRawData.getString(3), currentStockLevel.concat("]"));

    			session.saveOrUpdate(product);
    			currentStockLevel = "[";
    			if ( curArrayElem.getStockCode() != null ) 
    				currentStockLevel = currentStockLevel.concat(curArrayElem.toString());
			}
    		
   		
    		if ( j == jsize - 1 ) {
    			ProductsStockLevels product = new ProductsStockLevels(curRawData.getString(0), curRawData.getString(1), 
						curRawData.getString(2), curRawData.getString(3), currentStockLevel.concat("]"));

     			session.saveOrUpdate(product);

			}
    		
    		previousUID = currentUID;
    	}
    	
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public void syncStockLevels() {
		logger.info("StockLevelsSyncServiceImpl instance: StockLevelsSyncService RUN started.");
		HibernateSessionImpl hs = new HibernateSessionImpl();
		SessionFactory sf = hs.createSessionFactory();
		session = sf.openSession();		
		session.beginTransaction();
    		
    		try {
					String stockLevels = requestStockLevelsData();
					JSONArray arrDocsJson = new JSONArray(stockLevels);

			    	ObjectMapper objectMapper = new ObjectMapper();
			    	
			    	for (Object object : arrDocsJson.getJSONArray(0).toList()) {
			    		StockRef result = objectMapper.convertValue(object, StockRef.class);
			    		session.saveOrUpdate(result);
					}
			    	
			    	Query sq = session.createQuery("FROM StockRef AS stocks");
					sq.getResultList();
			    	
//					Transform data table from 1C. Stock levels need to be grouped by product UID and placed into the JSONArray field
//					of entity. Stocks count, having non-zero product level is varying from product to product. Columns count cannot
//					be changed in the database dynamically, so I will use JSONB field for grouping stock level values.
					transform1CProductsData(arrDocsJson.getJSONArray(1));
					session.getTransaction().commit();
					session.beginTransaction();

					//					Refresh view's code for the case warehouses count has changed  
					ProcedureCall nq = session.createStoredProcedureCall("regen_stl_view");	
					nq.getOutputs();
			    	logger.info("Stored procedure regen_stl_view call is finished.");
			} catch (IOException e) {
				session.getTransaction().rollback();
				e.printStackTrace();
			}
    				
		session.close();
		
	}
}