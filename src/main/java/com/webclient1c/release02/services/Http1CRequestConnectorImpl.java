package com.webclient1c.release02.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclient1c.release02.appclasses.CustomerInvoiceProducts;
import com.webclient1c.release02.entities.Users1C;

public class Http1CRequestConnectorImpl implements Http1CRequestConnector {
	@Inject
	Session session;
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String runJsonQuery(String connectionString, String jsonContent) {
//		HibernateSessionImpl hs = new HibernateSessionImpl();
//		SessionFactory sf = hs.createSessionFactory();
//		Session session = sf.openSession();
		String result = "";
    	URL db1CEntURL = null;
    	HttpURLConnection connection1c = null;
    	BufferedReader inBuff = null;
    	Users1C admUser = null;
    	
		Query sq = session.getSession().createQuery("FROM Users1C AS users WHERE userName = :adm");
		sq.setParameter("adm", "Администратор");
    	List<Users1C> results = sq.getResultList();
    	if ( results != null && !results.isEmpty() ) 
    		admUser = results.get(0);

    	CookieManager cm = new java.net.CookieManager();
    	CookieHandler.setDefault(cm);

        String someJSONContent = jsonContent;

		try {
			if ( admUser != null && !admUser.getBase64val().isEmpty() ) {
	    		db1CEntURL = new URL(connectionString);
	    	
		 		connection1c = (HttpURLConnection) db1CEntURL.openConnection();
		    	
		 		connection1c.setRequestMethod("POST");
		 		connection1c.setReadTimeout(60000);
		 		connection1c.setConnectTimeout(60000);
		    	connection1c.setRequestProperty("Authorization", admUser.getBase64val());
		    	connection1c.setRequestProperty("Content-Type", "application/json");
		    	connection1c.setDoOutput(true);
		    	
		    	DataOutputStream dout = new DataOutputStream(connection1c.getOutputStream());
		    	
		    	dout.writeBytes(someJSONContent);
		    	dout.flush();
		    	dout.close();
		    	
		    	int responseCode = connection1c.getResponseCode();
		    	if (responseCode != HttpURLConnection.HTTP_OK) {
		    		System.out.println("Got unexpected error code: " + responseCode);
		    	}
		    	
				inBuff = new BufferedReader(new InputStreamReader(connection1c.getInputStream()));
		    	
				
				StringBuilder bld = new StringBuilder();
		    	String line;
					while ((line = inBuff.readLine()) != null) {
						bld.append(line);
					}
				result = bld.toString();
					inBuff.close();
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	@Override
	public StreamResponse runJsonQuery(String connectionString, String jsonContent, String invoiceData) {
    	URL db1CEntURL = null;
		try {
			db1CEntURL = new URL(connectionString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	HttpURLConnection connection1c = null;
		try {
			connection1c = (HttpURLConnection) db1CEntURL.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Users1C admUser = null;
    	
		Query sq = session.getSession().createQuery("FROM Users1C AS users WHERE userName = :adm");
		sq.setParameter("adm", "Администратор");
    	List<Users1C> results = sq.getResultList();
    	if ( results != null && !results.isEmpty() ) 
    		admUser = results.get(0);

    	CookieManager cm = new java.net.CookieManager();
    	CookieHandler.setDefault(cm);
        
    	StringBuilder sb = new StringBuilder(jsonContent);
    	sb.append(invoiceData+"}");
        try {
			

			if ( admUser != null && !admUser.getBase64val().isEmpty() ) {
				connection1c.setRequestMethod("POST");
		 		connection1c.setReadTimeout(30000);
		 		connection1c.setConnectTimeout(30000);
		 		connection1c.setRequestProperty("Authorization", admUser.getBase64val());
		 		connection1c.setRequestProperty("Content-Type", "application/json");
		 		connection1c.setDoOutput(true);       
 				DataOutputStream dout = new DataOutputStream(connection1c.getOutputStream());

 				//String corrected8String = new String(someJSONContent.getBytes(), "UTF-8");
 				dout.write(sb.toString().getBytes(StandardCharsets.UTF_8));
		    	dout.flush();
		    	dout.close();
		    	
		    	int responseCode = connection1c.getResponseCode();
		    	if (responseCode != HttpURLConnection.HTTP_OK) {
		    		System.out.println("Got unexpected error code: " + responseCode);
		    	}
		    		    	
				InputStream inpStream = connection1c.getInputStream();

				byte[] tmpBuf = inpStream.readAllBytes();

				String strBuf = new String(tmpBuf);
				Integer markerPos = strBuf.indexOf("%%EOF");

				byte[] tmpBuf2 = Arrays.copyOfRange(tmpBuf, strBuf.indexOf("%PDF-1.7"), tmpBuf.length - 1);
				System.out.println("+++ Got data response from server ");
				System.out.println("+++++ Marker position: "+markerPos);
				return new PDFStreamResponse(new ByteArrayInputStream(tmpBuf2), "MyDynamicSample");
			}
	    } catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
	    }
		return null;
	}
}