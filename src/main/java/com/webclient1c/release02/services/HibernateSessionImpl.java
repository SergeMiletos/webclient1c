package com.webclient1c.release02.services;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionImpl implements HibernateSession {

	@Override
	public SessionFactory createSessionFactory() {
    	Configuration configuration = new Configuration();
    	configuration.setProperty("hibernate.batch_versioned_data", "true");
    	configuration.setProperty("hibernate.order_updates", "true");
    	return configuration.configure("hibernate.cfg.xml").buildSessionFactory();
	}
	
}