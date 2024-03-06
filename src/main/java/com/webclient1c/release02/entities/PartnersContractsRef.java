package com.webclient1c.release02.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "partners_contracts")
public class PartnersContractsRef {
	@Id
	@Column
	private String uuid;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "partners_uuid")
	private PartnersRef partner;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "companies_uuid")
	private CompaniesRef company;
	
	@Column(name="contract_code")
	private String contractCode;
	
	@Column(name="contract_name")
	private String contractName;
	
	@Column(name="contract_date")
	private String contractDate;
	
	@Column(name="contract_number")
	private String contractNumber;
	
	@Column(name="contract_type")
	private String contractType;

	public PartnersContractsRef() {
		// Default empty constructor
	}
	
	public PartnersContractsRef(String uuid, PartnersRef partner, CompaniesRef company, String contractCode,
			String contractName, String contractDate, String contractNumber, String contractType) {
		this.uuid = uuid;
		this.partner = partner;
		this.company = company;
		this.contractCode = contractCode;
		this.contractName = contractName;
		this.contractDate = contractDate;
		this.contractNumber = contractNumber;
		this.contractType = contractType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contractName, uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PartnersContractsRef other = (PartnersContractsRef) obj;
		return Objects.equals(contractName, other.contractName) && Objects.equals(uuid, other.uuid);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public PartnersRef getPartner() {
		return partner;
	}

	public void setPartner(PartnersRef partner) {
		this.partner = partner;
	}

	public CompaniesRef getCompany() {
		return company;
	}

	public void setCompany(CompaniesRef company) {
		this.company = company;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	
	public boolean isEmpty() {
		return uuid == null;
	}

}
