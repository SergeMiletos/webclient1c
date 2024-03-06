package com.webclient1c.release02.services;

import org.hibernate.SessionFactory;

public interface HibernateSession {
	public SessionFactory createSessionFactory();
}