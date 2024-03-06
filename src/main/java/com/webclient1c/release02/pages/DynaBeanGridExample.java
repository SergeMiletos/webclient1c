package com.webclient1c.release02.pages;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.PropertyOverrides;

import org.apache.tapestry5.Renderable;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.PropertyModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.internal.services.URLEncoderImpl;
import org.apache.tapestry5.internal.util.RenderableAsBlock;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.HttpError;
import org.apache.tapestry5.services.URLEncoder;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.webclient1c.release02.entities.CompaniesRef;
import com.webclient1c.release02.entities.PartnersContractsRef;
import com.webclient1c.release02.entities.PartnersRef;
import com.webclient1c.release02.entities.StockRef;
import com.webclient1c.release02.entities.Users1C;
import com.webclient1c.release02.services.DataSyncService;
import com.webclient1c.release02.services.HibernateSession;
import com.webclient1c.release02.services.HibernateSessionImpl;
import com.webclient1c.release02.services.Http1CRequestConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclient1c.release02.appclasses.CustomerInvoice;
import com.webclient1c.release02.appclasses.CustomerInvoiceProducts;
import com.webclient1c.release02.appclasses.QueryListItemClassBuilder;
import com.webclient1c.release02.services.PDFGenerator;
import com.webclient1c.release02.services.PDFStreamResponse;


public class DynaBeanGridExample {
	private static final String UNCHECKED = "unchecked";


	private static final Logger logger = LogManager.getLogger(DynaBeanGridExample.class);
    
    public static final String STOCK_LABEL = "_stock";
    public static final String CLASS_ATTR = "class";
    public static final String STYLE_ATTR = "style";
    public static final String EMPTY_CELL_VALUE = "---";
    public static final String FIELD_NAME = "productName";
    public static final String PROD_NAME_LABEL = "PRODUCT_NAME";

    private static final String SERVER1C_URL = "http://localhost:8073/testcm/hs/";
    
    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }    

    @Inject 
    private Session session; 

    @Inject
    private Http1CRequestConnector http1CRequestConnectorImpl;
    @Inject 
    private DataSyncService dataSyncServiceImpl;
    
    @Inject
    private BeanModelSource beanModelSource;
    @Inject
    private ComponentResources resources;
    @Inject 
    private Messages messages;    
    @Inject
    private Request request;
    @Environmental
    private JavaScriptSupport javaScriptSupport;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    
    @SessionState
    @Property
    private Users1C loggedUser;
    @Property
	private boolean loggedUserExists;

	
	@Property
	private CustomerInvoice invoiceBean;

	// Grid row instances
    @Property
	private PartnersRef partnerRowInstance;
    @Property
	private PartnersContractsRef contractsRowInstance;
    @Property
	private CompaniesRef companyRowInstance;    
    @Property
	private BasicDynaBean gridRowInstance;

    // Filters fields
    @Persist
    @Property
    private String partnersFilter;
    @Persist
    @Property
	private String companiesFilter; 
    @Persist
    @Property
    private String prodFilter;
	    
    @Persist
    @Property
    private BasicDynaClass listDynaElemClass;

    @Persist
    @Property
    private List<? extends BasicDynaBean> productsList;    

    @Persist
    @Property
    private List<? extends BasicDynaBean> productsListFiltered;    
    
    @Persist
    @Property
    private BeanModel<BasicDynaBean> modelInstance;
    
    public enum CurrentOperation {
    	REVIEW, UPDATE;
    }    
    @Property 
    @Persist(PersistenceConstants.FLASH)
    private CurrentOperation curOperation;
    
    @InjectComponent
    private Zone modalZone;
    @InjectComponent
    private Zone invoiceLinesZone;
    @InjectComponent
    private Zone productsLinesZone;
    @InjectComponent
    private Zone filterZone;    
    @InjectComponent
    private Zone tablePartnersZone;    
    @InjectComponent
    private Zone tableCompaniesZone;    
    @InjectComponent
    private Zone tableContractsZone;    
    @InjectComponent
    private Form myFormPlz;
    @InjectComponent
    private Form filterpartnersform;
    @InjectComponent
    private Form filtercompaniesform;
    @InjectComponent
    private Zone partnerNameZone;
    @InjectComponent
    private Zone companyNameZone;
    
    @InjectComponent 
    private Grid gsonListGrid;

    // User selection on the grid
    @Property
    @Persist(PersistenceConstants.FLASH)
    private String selectedQuantity;
    @Property 
    @Persist(PersistenceConstants.FLASH)
    private String selectedProduct;
    @Property 
    @Persist(PersistenceConstants.FLASH)
    private String selectedProductName;
    @Property
    @Persist(PersistenceConstants.FLASH)
    private String selectedStockName;
 
    @Persist
    @Property
    private List<StockRef> stockList;
    
    @Persist
    @Property
    private String invoiceOrganization;
    
    @Persist
    @Property
    private String invoicePartner;
    
    @Persist
    @Property
    private String invoiceContract;
    
    @Property
    private CustomerInvoiceProducts invoiceRow;
    
    @Persist
    @Property
    private ArrayList<CustomerInvoiceProducts> invoiceRows;
    
    @Persist
    @Property
    private BeanModel<CustomerInvoice> ciModel;

   
    @SuppressWarnings("unused")
	private String calculateHash (String inputVal) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(inputVal.strip().getBytes());
		byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        
        for (byte b : digest) 
        	sb.append(String.format("%02x", b));

        return sb.toString().toUpperCase();

    }
    
	public boolean isCurOperation(CurrentOperation curOperation) {
		return curOperation == this.curOperation;
	}
	
    Object onActivate(EventContext eventContext) {

    	return eventContext.getCount() > 0 ?
                new HttpError(404, "Resource not found") :
                null;

    }
    
    
    @SuppressWarnings({ "unchecked", "deprecation" })
	@SetupRender
    private void setupRender() {
		try { 
			initData(); 
		} catch (IOException e) { e.printStackTrace(); }

		dataSyncServiceImpl.companyRefSync();
		dataSyncServiceImpl.partnersRefSync();
		dataSyncServiceImpl.partnersContractsRefSync();
		
		Query<StockRef>  sq = session.getSession().createQuery("FROM StockRef AS stocks");
		stockList = sq.getResultList();
		
		invoiceOrganization = invoiceOrganization == null ? "" : invoiceOrganization;
		invoicePartner = invoicePartner == null ? "" : invoicePartner; 
		invoiceContract = invoiceContract == null ? "" : invoiceContract;
		partnersFilter = "";
		companiesFilter = "";
		prodFilter = "";

		invoiceRows = new ArrayList<>();			
		invoiceBean = new CustomerInvoice(); 
		
		ciModel = beanModelSource.createEditModel(CustomerInvoice.class, messages);		
		PropertyModel companyField = ciModel.get("Company");
		companyField.label(messages.get("company-label"));
    }	

    //  Event handlers
    @Log
    void onActionFromOpenPartnerCatalogLink() {
    	curOperation = CurrentOperation.UPDATE;
    	if (request.isXHR()) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalDialogPartner", "activate"));
    	}
    }
    
    @Log
    void onActionFromOpenCompanyCatalogLink() {
    	curOperation = CurrentOperation.UPDATE;
    	if (request.isXHR()) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalDialogCompany", "activate"));
    	}
    }

    @Log
    void onActionFromOpenContractCatalogLink() {
    	curOperation = CurrentOperation.UPDATE;
    	if (request.isXHR()) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalDialogContracts", "activate"));
    	}
    }

    // modalDialogId - id's from tml template, mode - "activate" or "hide"
    private JavaScriptCallback makeActionScriptModalList(String modalDialogId, String mode) {
    	return new JavaScriptCallback() {
    		public void run(JavaScriptSupport javaScriptSupport) {
    			javaScriptSupport.require("modal-dialog")
    				.invoke(mode)
    				.with("#"+modalDialogId);
    		}
    	};    
    }
    
     @Log
    @OnEvent(value="onActionFromSelectPartnerCatalogLink")
    public boolean onActionFromSelectPartnerCatalogLink(String partnerName, String partnerCodeINN) {
     	StringBuilder strb = new StringBuilder(partnerName);
     	strb.append(", ").append(partnerCodeINN);
     	invoicePartner = strb.toString();
    	if ( request.isXHR() ) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalDialogPartner", "hide"));
    	}
    	return true;
    }
    
    @Log
    @OnEvent(value="onActionFromSelectCompanyCatalogLink")
    public boolean onActionFromSelectCompanyCatalogLink(String companyName, String companyINN) {
     	StringBuilder strb = new StringBuilder(companyName);
     	strb.append(", ").append(companyINN);
     	invoiceOrganization = strb.toString();
    	if ( request.isXHR() ) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalDialogCompany", "hide"));
    	}
    	return true;
    }

    @Log
    @OnEvent(value="onActionFromSelectContractsCatalogLink")
    public boolean onActionFromSelectContractsCatalogLink(String contractName, String uuid) {
     	StringBuilder strb = new StringBuilder(contractName);
     	strb.append(" (").append(uuid).append(")");
     	invoiceContract = strb.toString();
    	if ( request.isXHR() ) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalDialogContracts", "hide"));
    	}
    	return true;
    }

    @OnEvent(value = "gridheader")
    public boolean ongridheader(String id) {
    	System.out.println("Event on \"on Grid Header Click\" link, "+id);
    	logger.info("Event on \"on Grid Header Click\" link (logger says)");
    	LocalDateTime locDate = LocalDateTime.now();
    	DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:MM:SS", Locale.forLanguageTag("ru_RU"));
    	String currDateTime = locDate.format(myFormatter);
    	ajaxResponseRenderer
    		.addRender("headerZoneStock_all", "Ajax updated ("+id+") "+currDateTime);

    	return true;
    }   
    
    @OnEvent(value = "gridquantityselected")
    public boolean onGridQuantitySelection(String product, String productUuid, String quantity, String stockName) {
    	LocalDateTime locDate = LocalDateTime.now();
    	DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:MM:SS", Locale.forLanguageTag("ru_RU"));
    	String currDateTime = locDate.format(myFormatter);
    	
    	selectedProduct = productUuid;
    	selectedProductName = product;
    	selectedStockName = stockName;
    	selectedQuantity = quantity;
    	curOperation = CurrentOperation.UPDATE;
    	
    	if (request.isXHR()) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalFormQuantity", "activate"));
    		ajaxResponseRenderer.addRender(modalZone);
    		ajaxResponseRenderer.addRender("headerZoneStock_all", "Ajax updated (\""+selectedProductName+"\") "+currDateTime);

    	}
    	
    	return true;
    }    

    @SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	private Supplier<PartnersRef> getPartnerOnField = () -> {
    	if ( !invoicePartner.isBlank() ) {
    		int index = invoicePartner.lastIndexOf(",");
	    	String partnerName = invoicePartner.substring(0, index).toLowerCase().trim();
	    	String partnerInn = invoicePartner.substring(index+1).trim();
	    	Query pq = session.getSession().createQuery("FROM PartnersRef as pr WHERE lower(pr.partnerName) = :pname AND pr.inn = :pinn");
		    pq.setParameter("pname", partnerName);
		    pq.setParameter("pinn", partnerInn);
		    
		    List<PartnersRef> result = pq.list();
		    if ( !result.isEmpty() )
		    	return result.get(0);
    	}
    	return new PartnersRef();
    };
    
    @SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
    private Supplier<CompaniesRef> getCompanyOnField = () -> {
    	if ( invoiceOrganization != null && !invoiceOrganization.isBlank() ) {
	    	int index = invoiceOrganization.lastIndexOf(",");
	    	String companyName = invoiceOrganization.substring(0, index).toLowerCase().trim();
	    	String companyInn = invoiceOrganization.substring(index+1).trim();
	    	Query cq = session.getSession().createQuery("FROM CompaniesRef as cr WHERE lower(cr.name) = :cname AND cr.inn = :cinn");
		    cq.setParameter("cname", companyName);
		    cq.setParameter("cinn", companyInn);
		    
		    List<CompaniesRef> result = cq.list();
		    if ( !result.isEmpty() )
		    	return result.get(0);
		    
    	}
	    return new CompaniesRef();
    };
    
    @SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
    private Supplier<PartnersContractsRef> getContractOnField = () -> {
    	if ( invoiceContract != null && !invoiceContract.isBlank() ) {
	    	int index = invoiceContract.lastIndexOf("(");
	    	String contractUUID = invoiceContract.substring(index+1, invoiceContract.length()-1).trim().toLowerCase();
	    	Query pcq = session.getSession().createQuery("FROM PartnersContractsRef as pcr WHERE lower(pcr.uuid) = :uuid");
		    pcq.setParameter("uuid", contractUUID);
		    
		    List<PartnersContractsRef> result = pcq.list();
		    if ( !result.isEmpty() )
		    	return result.get(0);
    	}
    	return new PartnersContractsRef();
    };
    
    public StreamResponse sendCustomerInvoiceData() throws IOException {
    	JSONArray arrayData = new JSONArray("[]");
    	JSONObject invoiceData = new JSONObject("{}");
    	CompaniesRef company = getCompanyOnField.get();    	    	
    	PartnersRef partner = getPartnerOnField.get();   	
    	PartnersContractsRef contract = getContractOnField.get();
    	
    	invoiceData.put("company", company.getUuid());
       	invoiceData.put("partner", partner.getUuid());
       	invoiceData.put("contract", contract.getUuid());

       	arrayData.put(invoiceData);
       	
    	JSONArray invoiceRowsLocal = new JSONArray("[]");
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	for (Object invoiceRow : invoiceRows) {
    		invoiceRowsLocal.put(objectMapper.writeValueAsString(invoiceRow));
    	}
    	arrayData.put(invoiceRowsLocal);
    	return http1CRequestConnectorImpl.runJsonQuery(SERVER1C_URL+"saleordersoperations/createCustomerInvoice"
    			, "{\"RequestOperation\":\"CreateCustomerInvoice\", \"ArrayData\":"
    			, arrayData.toString());
    }

    @OnEvent(value=EventConstants.SUBMIT, component="pdfExampleFormRemote")
    public StreamResponse onSendInvoice() {

    	try {
			return sendCustomerInvoiceData();
		} catch (IOException e) {

			e.printStackTrace();
		}
    	return null;

    }    

    @OnEvent(value=EventConstants.SUBMIT, component="pdfExampleForm")
    public StreamResponse onSubmit() {
		// Create PDF
		InputStream is = PDFGenerator.generatePDF("This is the content of a Dynamically Generated PDF");
		// Return response
		return new PDFStreamResponse(is,"MyDynamicSample");
	}

    @OnEvent(value="cancel")
    public boolean onCancel() {
    	curOperation = CurrentOperation.REVIEW;
    	logger.info("Request is Ajax: "+request.isXHR()+" (onCancel event, logger says)");   	
    	
    	if (request.isXHR() ) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalFormQuantity", "hide"));
    	}
    	return true;
    }
    
    @OnEvent(value=EventConstants.SUBMIT, component="modalForm")
    boolean onMyFormQuantitySelectionSubmit() {
    	curOperation = CurrentOperation.REVIEW;

    	StockRef localStock = stockList.stream()
    			.filter(ob -> ob.getStockCode().contains(selectedStockName.replace(STOCK_LABEL, "")))
    			.findAny()
    			.get();
    	
    	// Let's add a row to the Invoices product table
    	invoiceRows.add(new CustomerInvoiceProducts(selectedProduct, selectedProductName,  localStock.getStockCode(), selectedQuantity));
    	
    	if (request.isXHR() ) {
    		ajaxResponseRenderer.addCallback(makeActionScriptModalList("modalFormQuantity", "hide"));
    		ajaxResponseRenderer.addRender(invoiceLinesZone);
    	}
    	return true;   	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public List<CompaniesRef> getCompaniesList() {
    	if ( !companiesFilter.isBlank() ) {
    	
	    	Query pq = session.getSession().createQuery("FROM CompaniesRef as cr WHERE lower(cr.name) like :part OR cr.inn like :part");
		    pq.setParameter("part", "%"+companiesFilter.trim()+"%");
	  
		    return pq.list();
    	}
    	return session.getSession().createCriteria(CompaniesRef.class).list();
    }    
    
    @SuppressWarnings({ "deprecation", "unused" })
	public List<PartnersContractsRef> getContractsList() {
    	
    	CompaniesRef company = getCompanyOnField.get();     	
    	PartnersRef partner = getPartnerOnField.get();
    	
    	CriteriaBuilder criteriaBuilder = session.getSession().getCriteriaBuilder();
    	CriteriaQuery<PartnersContractsRef> crq = criteriaBuilder.createQuery(PartnersContractsRef.class);
    	Root<PartnersContractsRef> root = crq.from(PartnersContractsRef.class);
    	List<Predicate> predicates = new ArrayList<>();
    	if ( !company.isEmpty() )
    		predicates.add(criteriaBuilder.equal(root.get("company"), company));
    	if ( !partner.isEmpty() )
    		predicates.add(criteriaBuilder.equal(root.get("partner"), partner));
    	
    	if ( predicates.isEmpty() ) {
    		crq.select(root);
    	} else {
    		crq.select(root).where(predicates.toArray(Predicate[]::new));
    	}

    	Query<PartnersContractsRef> query = session.getSession().createQuery(crq);

    	return query.getResultList();
    }    


    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    List<String> onProvideCompletionsFrominvoiceContract(String partial) {
        List<String> matches = new ArrayList<>();
        String partialLower = "%"+partial.toLowerCase().strip()+"%";
		
        PartnersRef partner = getPartnerOnField.get();
		CompaniesRef company = getCompanyOnField.get();
		
		Query pq = session.getSession().createQuery("FROM PartnersContractsRef as pcr WHERE pcr.partner = :partner AND pcr.company = :company AND pcr.contractName like :contractname");
		pq.setParameter("partner", partner);
		pq.setParameter("company", company);
		pq.setParameter("contractname", partialLower);
		pq.setMaxResults(7);
		List<PartnersContractsRef> result = pq.list();
        
	    for (PartnersContractsRef partnerContract : result){
	        String name = partnerContract.getContractCode() + ", " + partnerContract.getContractName();
	        matches.add(name);
	    }

	    return matches;
    }    

    // Table data handlers

    @OnEvent(value=EventConstants.SUBMIT, component="filterForm")
    public boolean onfilterFormSubmit() {

    	//String currDateTime = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDateTime.now());

    	if (prodFilter.length() > 0) {
    		System.out.println("Filter string: "+prodFilter);
			System.out.println("Source length "+productsList.size());

			productsListFiltered = productsList.stream()
					.filter(prodElem -> prodElem.get(PROD_NAME_LABEL)
					.toString()
					.contains(prodFilter))
					.toList();
    	}

    	System.out.println("Event on \"onfilterFormSubmit\" button"+ ", ajax: "+request.isXHR());
    	logger.info("Event on \"onfilterFormSubmit\" button (logger says)");

    	
    	if (request.isXHR() ) {
    		ajaxResponseRenderer.addRender(productsLinesZone);
    	}
    	return true;
    }    
  
    @OnEvent(value="clearFilter")
    public boolean onClearFilter() {
    	System.out.println("Event on \"Clear filter\" button"+ ", context: none");
    	logger.info("Event on \"Clear filter\" button (logger says)");
    	//String currDateTime = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDateTime.now());
    	prodFilter = "";
		productsListFiltered = productsList.stream()
				.toList();

		if (request.isXHR() ) {
    		ajaxResponseRenderer.addRender(productsLinesZone);
       	}
    	
    	return true;
  	
    }
  
    @OnEvent(value=EventConstants.SUBMIT, component="filterpartnersform")
    public void onfilterPartnersFormSubmit() {
    	System.out.println("Filter string: "+partnersFilter);
    	
    	if ( request.isXHR() ) {
    		ajaxResponseRenderer.addRender(tablePartnersZone);
    	}
    }    
  
    @OnEvent(value="clearPartnersFilter")
    public boolean onClearPartnersFilter() {
    	partnersFilter = "";

		if (request.isXHR() )
    		ajaxResponseRenderer.addRender(tablePartnersZone);
    	
    	return true;
  	
    }
  
    public PropertyOverrides getPartnersPropertyOverrides() {
    	return new PropertyOverrides() {
            
            public Messages getOverrideMessages() {
                return messages;
            }
             
			public Block getOverrideBlock(String name) {
				if ( name.startsWith("partnerFullName") && name.contains("Cell") )  {
					return new RenderableAsBlock( new Renderable() {
						public void render(MarkupWriter writer) {
							Element currVisualElem = writer.getElement();
							Element newElem2 = writer.element("a", "id", partnerRowInstance.hashCode());								URLEncoder encImpl = new URLEncoderImpl();
							 
							String urlString = "/dynabeangridexample:onActionFromSelectPartnerCatalogLink/"
									+encImpl.encode(partnerRowInstance.getPartnerName())+"/"
									+encImpl.encode(partnerRowInstance.getInn()); 
							try {
								String url = new URI(urlString).toASCIIString();
								newElem2.attribute("href", url);
							} catch (URISyntaxException e) {
								e.printStackTrace();
							} 
							currVisualElem.attribute(CLASS_ATTR, "p-1");
							currVisualElem.attribute(STYLE_ATTR, "text-align: center;");
							currVisualElem.attribute("zone", "tablePartnersZone");
							writer.write( partnerRowInstance.getPartnerFullName() );
							writer.end();
						}
					});
				}				
				return null;
			}
    	};
    }
    
    public PropertyOverrides getContractsPropertyOverrides() {
    	return new PropertyOverrides() {
            
            public Messages getOverrideMessages() {
                return messages;
            }
             
			public Block getOverrideBlock(String name) {
				if ( name.startsWith("contractName") && name.contains("Cell") )  {
					return new RenderableAsBlock( new Renderable() {
						public void render(MarkupWriter writer) {
							Element currVisualElem = writer.getElement();
							Element newElem2 = writer.element("a", "id", contractsRowInstance.hashCode());								URLEncoder encImpl = new URLEncoderImpl();
							 
							String urlString = "/dynabeangridexample:onActionFromSelectContractsCatalogLink/"
									+encImpl.encode(contractsRowInstance.getContractName())+"/"
									+encImpl.encode(contractsRowInstance.getUuid()); 
							try {
								String url = new URI(urlString).toASCIIString();
								newElem2.attribute("href", url);
							} catch (URISyntaxException e) {
								e.printStackTrace();
							} 
							currVisualElem.attribute(CLASS_ATTR, "p-1");
							currVisualElem.attribute(STYLE_ATTR, "text-align: center;");
							currVisualElem.attribute("zone", "tableContractsZone");
							writer.write( contractsRowInstance.getContractName() );
							writer.end();
						}
					});
				}				
				return null;
			}
    	};
    }
    
    public PropertyOverrides getPropertyOverrides() {
        
    	return new PropertyOverrides() {
            
            public Messages getOverrideMessages() {
                return null;
            }
             
			public Block getOverrideBlock(String name) {
				
            	if ( name.startsWith(STOCK_LABEL) && !name.contains("Cell")) {
					String currentStockCode = name.replace(STOCK_LABEL, "").replace("Header", "");
	            	StockRef currentStock = stockList.stream()
	            		  .filter(ob -> ob.getStockCode()
	            		  .equals(currentStockCode))
	            		  .findAny()
	            		  .orElse(null);
            	  
	            	if (currentStock != null && name.equalsIgnoreCase(STOCK_LABEL+currentStockCode + "Header")) {
							
						return new RenderableAsBlock( new Renderable() {
								
								public void render(MarkupWriter writer) {
										
									Element currVisualElem = writer.getElement();
									currVisualElem.attribute(CLASS_ATTR, "align-top p-1");
									currVisualElem.attribute(STYLE_ATTR, "font-weght: bold; text-align: center;");
									Element newElem2 = writer.element("a","id", STOCK_LABEL+currentStockCode);
									newElem2.attribute("data-update-zone", "headerZoneStock_all");
									newElem2.attribute("href", "/dynabeangridexample:gridheader/8099432");
									newElem2.attribute(CLASS_ATTR, "p-1 btn btn-default");
									newElem2.attribute(STYLE_ATTR, "font-weght: bold; text-align: center;");
									writer.write(currentStock.getStockName());
									writer.end();
									
								}
							});
						}
               } else if ( name.startsWith(STOCK_LABEL) && name.contains("Cell") ) {
            	  String currentStockCode = name.replace(STOCK_LABEL, "").replace("Cell", "");
             	  StockRef currentStock = stockList.stream()
             			.filter(ob -> ob.getStockCode()
             			.equals(currentStockCode))
             			.findAny()
             			.orElse(null);
             	  if (currentStock != null && name.equalsIgnoreCase(STOCK_LABEL+currentStockCode + "Cell")) {
             		  return new RenderableAsBlock( new Renderable() {
								public void render(MarkupWriter writer) {
									Element currVisualElem = writer.getElement();
									currVisualElem.attribute(CLASS_ATTR, "p-1 align-middle");
									currVisualElem.attribute(STYLE_ATTR, "text-align: center;");
									Element newElem2 = writer.element("a", "id", gridRowInstance.hashCode());

									if (gridRowInstance.get(STOCK_LABEL+currentStock.getStockCode()) != null) {
										newElem2.attribute("data-update-zone", "headerZoneStock_all");
										String prodName = (gridRowInstance.get(PROD_NAME_LABEL) == null) ? "##" : gridRowInstance.get(PROD_NAME_LABEL).toString();
										String prodUuid = (gridRowInstance.get(PROD_NAME_LABEL) == null) ? "##" : gridRowInstance.get("ID").toString();
										
										URLEncoder encImpl = new URLEncoderImpl();
										String prodNameURL = encImpl.encode(prodName);
										 
										String urlString = "/dynabeangridexample:gridquantityselected/"
												+prodNameURL+"/"
												+prodUuid+"/"
												+gridRowInstance.get(STOCK_LABEL+currentStock.getStockCode()).toString()+"/"
												+STOCK_LABEL+currentStock.getStockCode(); 
										try {
											String url = new URI(urlString).toASCIIString();
											newElem2.attribute("href", url);
										} catch (URISyntaxException e) {
											e.printStackTrace();
										} 
									}
									newElem2.attribute(CLASS_ATTR, "p-1 btn btn-default");
									newElem2.attribute(STYLE_ATTR, "font-weght: bold;");
									writer.write( (gridRowInstance.get(STOCK_LABEL+currentStock.getStockCode()) == null) 
											? EMPTY_CELL_VALUE : gridRowInstance.get(STOCK_LABEL+currentStock.getStockCode()).toString() );
									writer.end();
 
								}
							});
             	  		}
					} else if (name.startsWith(PROD_NAME_LABEL) && name.contains("Cell")) { 
						return new RenderableAsBlock( new Renderable() {
							public void render(MarkupWriter writer) {
								Element currVisualElem = writer.getElement();
								currVisualElem.attribute(CLASS_ATTR, "p-1 table-namecol");
								currVisualElem.attribute(STYLE_ATTR, "text-align: start;");
								writer.write( (gridRowInstance.get(PROD_NAME_LABEL) == null) ? "##" : gridRowInstance.get(PROD_NAME_LABEL).toString());

							}
						});
					}
               return null;
            }
    	};
    }
    
    public PropertyOverrides getCompaniesPropertyOverrides() {
    	return new PropertyOverrides() {
            
            public Messages getOverrideMessages() {
                return messages;
            }
             
			public Block getOverrideBlock(String name) {
				if ( name.startsWith("name") && name.contains("Cell") )  {
					return new RenderableAsBlock( new Renderable() {
						public void render(MarkupWriter writer) {
							Element currVisualElem = writer.getElement();
							Element newElem2 = writer.element("a", "id", companyRowInstance.hashCode());								URLEncoder encImpl = new URLEncoderImpl();
							 
							String urlString = "/dynabeangridexample:onActionFromSelectCompanyCatalogLink/"
									+encImpl.encode(companyRowInstance.getName())+"/"
									+encImpl.encode(companyRowInstance.getInn()); 
							try {
								String url = new URI(urlString).toASCIIString();
								newElem2.attribute("href", url);
							} catch (URISyntaxException e) {
								e.printStackTrace();
							} 
							currVisualElem.attribute(CLASS_ATTR, "p-1");
							currVisualElem.attribute(STYLE_ATTR, "text-align: center;");
							currVisualElem.attribute("zone", "tableCompaniesZone");
							writer.write( companyRowInstance.getName() );
							writer.end();
						}
					});
				}				
				return null;
			}
    	};
    }
      
    @SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public List<PartnersRef> getPartnersList() {
    	if ( !partnersFilter.isBlank() ) {
    	    Query pq = session.getSession().createQuery("FROM PartnersRef as pr WHERE lower(pr.partnerName) like :part OR pr.inn like :part");
    	    pq.setParameter("part", "%"+partnersFilter.trim()+"%");
    	  
    	    return pq.list();
    	}
		return session.getSession().createCriteria(PartnersRef.class).list();
    		
    }
    
    @SuppressWarnings({ UNCHECKED, "rawtypes", "deprecation", "serial" })
    private void initData() throws IOException {

		Map<String, Object> helperMap = new QueryListItemClassBuilder().buildClass();
		modelInstance = (BeanModel<BasicDynaBean>) helperMap.get("modelInstance");
		listDynaElemClass = (BasicDynaClass) helperMap.get("queryListItemClass");
		
		
    	NativeQuery q = session.createNativeQuery("select * from stock_levels_view mv");

    	q.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuples, String[] aliases) {
                BasicDynaBean listElemDTO = new BasicDynaBean(listDynaElemClass);
                listElemDTO.set("PRODUCT_CODE", tuples[0]);
                listElemDTO.set(PROD_NAME_LABEL, tuples[1]);
                listElemDTO.set("UOM", tuples[2]);
                listElemDTO.set("ID", tuples[3]);
                
            	for (int i = 4; i < aliases.length; i++) {
            		listElemDTO.set(aliases[i], (tuples[i] == null) ? EMPTY_CELL_VALUE : tuples[i]);
            	}
                
                return listElemDTO;
            }
  
            @Override
            public List transformList(List list) {
                return list;
            }
        });
    	
    	productsList = q.list();
		productsListFiltered = productsList.stream()
				.toList();
    			
    }
}