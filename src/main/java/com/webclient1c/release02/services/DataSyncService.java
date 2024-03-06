package com.webclient1c.release02.services;

public interface DataSyncService {
	public void dataInitShedule();
	
	public void companyRefSync();
	public void partnersRefSync();
	public void partnersContractsRefSync();

}