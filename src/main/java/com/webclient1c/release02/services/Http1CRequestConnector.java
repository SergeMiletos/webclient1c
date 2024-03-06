package com.webclient1c.release02.services;


import java.util.List;

import org.apache.tapestry5.StreamResponse;

import com.webclient1c.release02.appclasses.CustomerInvoiceProducts;

public interface Http1CRequestConnector {
	public String runJsonQuery(String connectionString, String jsonContent);
	public StreamResponse runJsonQuery(String connectionString, String jsonContent, String invoiceData);
}