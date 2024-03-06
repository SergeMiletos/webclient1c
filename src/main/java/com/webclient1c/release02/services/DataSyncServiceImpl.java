package com.webclient1c.release02.services;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONArray;

import com.webclient1c.release02.entities.CompaniesRef;
import com.webclient1c.release02.entities.PartnersContractsRef;
import com.webclient1c.release02.entities.PartnersRef;

@EagerLoad
public class DataSyncServiceImpl implements DataSyncService {
	private static final Logger logger = LogManager.getLogger(DataSyncServiceImpl.class);
    private static final String SERVER1C_URL = "http://localhost:8073/testcm/hs/";

    @Inject
    Http1CRequestConnector http1CRequestConnectorImpl;	
 
    private HibernateSession hs;
    
    private SessionFactory sf;
    private Session session; 
    
	public DataSyncServiceImpl() {
		logger.info(" ^^^ DataSyncServiceImpl constructor is executing");
		hs = new HibernateSessionImpl();
		sf = hs.createSessionFactory();
	}
	
	@Override
	public void dataInitShedule() {
		logger.info("Starting up dataInitShedule");
	}

    private String requestCompaniesData() throws IOException {
        return http1CRequestConnectorImpl.runJsonQuery(SERVER1C_URL+"saleordersoperations/GetCompaniesData"
        		, "{\"RequestOperation\":\"GetCompaniesList\"}");

    }

    private String requestPartnersData() throws IOException {
        return http1CRequestConnectorImpl.runJsonQuery(SERVER1C_URL+"saleordersoperations/GetPartnersData"
        		, "{\"RequestOperation\":\"GetPartialPartnersList\"}");

    }

    private String requestPartnersContractsData() throws IOException {
        return http1CRequestConnectorImpl.runJsonQuery(SERVER1C_URL+"saleordersoperations/GetPartnersData"
        		, "{\"RequestOperation\":\"GetPartialPartnersContractsList\"}");

    }
    
    
	@Override
	public void companyRefSync() {
		logger.info("DataSyncSyncServiceImpl instance: companyRefSync() RUN started.");
		session = sf.openSession();
		session.beginTransaction();
		
		CompaniesRef company = null;
		
		JSONArray arrCompaniesJson = new JSONArray();
		try {
			arrCompaniesJson = new JSONArray(requestCompaniesData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	JSONArray valsArr;
		// CompaniesRef(String uuid, String code, String name, String inn, String kpp, String hash1c)
		for (Object currElem : arrCompaniesJson) {
			valsArr = (JSONArray) currElem;
			company = new CompaniesRef(valsArr.getString(0), valsArr.getString(1),
										valsArr.getString(2), valsArr.getString(3), 
										valsArr.getString(4), valsArr.getString(5));
			
			session.saveOrUpdate(company);	
		}
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public void partnersRefSync() {
		logger.info("DataSyncSyncServiceImpl instance: companyRefSync() RUN started.");
		session = sf.openSession();
		session.beginTransaction();
		
		PartnersRef partner = null;
		
		JSONArray arrCompaniesJson = new JSONArray();
		try {
			arrCompaniesJson = new JSONArray(requestPartnersData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	JSONArray valsArr;
		// CompaniesRef(String uuid, String code, String name, String inn, String kpp, String hash1c)
		for (Object currElem : arrCompaniesJson) {
			valsArr = (JSONArray) currElem;
			partner = new PartnersRef(valsArr.getString(0), valsArr.getString(1),
										valsArr.getString(2), valsArr.getString(3), 
										valsArr.getString(4), valsArr.getString(5),
										valsArr.getString(6), valsArr.getString(7));
			
			session.saveOrUpdate(partner);	
		}
		session.getTransaction().commit();
		session.close();
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void partnersContractsRefSync() {
		logger.info("DataSyncSyncServiceImpl instance: partnersContractsRefSync() RUN started.");
		session = sf.openSession();
		session.beginTransaction();
		
		PartnersContractsRef partnerContract = null;
		
		JSONArray arrContractsJson = new JSONArray();
		try {
			arrContractsJson = new JSONArray(requestPartnersContractsData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	JSONArray valsArr;
		Query sqp = session.createQuery("FROM PartnersRef AS p WHERE uuid = :partneruuid");
		Query sqc = session.createQuery("FROM CompaniesRef AS p WHERE uuid = :companyuuid");

		for (Object currElem : arrContractsJson) {
			valsArr = (JSONArray) currElem;
			PartnersRef partner = null;
			sqp.setParameter("partneruuid", valsArr.getString(6)); 
	    	List<PartnersRef> partnerResults = sqp.getResultList();
	    	if ( partnerResults != null && !partnerResults.isEmpty() ) 
	    		partner = partnerResults.get(0);
	    	
	    	CompaniesRef company = null;
			sqc.setParameter("companyuuid", valsArr.getString(7)); 
	    	List<CompaniesRef> companyResults = sqc.getResultList();
	    	if ( companyResults != null && !companyResults.isEmpty() ) 
	    		company = companyResults.get(0);
	    	
			
			partnerContract = new PartnersContractsRef(valsArr.getString(0), partner,
										company, valsArr.getString(1), valsArr.getString(2),
										 valsArr.getString(3), valsArr.getString(4), valsArr.getString(5));
			
			session.saveOrUpdate(partnerContract);	
		}
		session.getTransaction().commit();
		session.close();		
	}
}

