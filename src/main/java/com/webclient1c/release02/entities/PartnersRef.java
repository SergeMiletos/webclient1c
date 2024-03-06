package com.webclient1c.release02.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "partners")
public class PartnersRef {
	@Id
	@Column
	private String uuid;
	
	@Column(name="partner_code")
	private String partnerCode;
	
	@Column(name="partner_name")
	private String partnerName;
	@Column(name="partner_full_name")
	private String partnerFullName;
	
	@Column(name="inn")
	private String inn;
	
	@Column(name="kpp")
	private String kpp;
	
	@Column(name="company_or_human")
	private String companyOrHuman;

	@Column(name="hash1c")
	private String hash1C;

	public PartnersRef() {
		// Default empty constructor
	}
	
	public PartnersRef(String uuid, String partnerCode, String partnerName, String partnerFullName, String inn,
			String kpp, String companyOrHuman, String hash1C) {
		this.uuid = uuid;
		this.partnerCode = partnerCode;
		this.partnerName = partnerName;
		this.partnerFullName = partnerFullName;
		this.inn = inn;
		this.kpp = kpp;
		this.companyOrHuman = companyOrHuman;
		this.hash1C = hash1C.replaceAll("\\s+","");		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(uuid, inn, kpp);
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
		PartnersRef other = (PartnersRef) obj;
		return Objects.equals(companyOrHuman, other.companyOrHuman) && Objects.equals(inn, other.inn)
				&& Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return "PartnersRef [" + (uuid != null ? "id=" + uuid + ", " : "")
				+ (partnerCode != null ? "partnerCode=" + partnerCode + ", " : "")
				+ (inn != null ? "inn=" + inn + ", " : "") + (kpp != null ? "kpp=" + kpp + ", " : "")
				+ (partnerName != null ? "partnerName=" + partnerName + ", " : "")
				+ (partnerFullName != null ? "partnerFullName=" + partnerFullName + ", " : "")
				+ (companyOrHuman != null ? "companyOrHuman=" + companyOrHuman : "") + "]";
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPartnerFullName() {
		return partnerFullName;
	}

	public void setPartnerFullName(String partnerFullName) {
		this.partnerFullName = partnerFullName;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public String getCompanyOrHuman() {
		return companyOrHuman;
	}

	public void setCompanyOrHuman(String companyOrHuman) {
		this.companyOrHuman = companyOrHuman;
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String gethash1C() {
		return hash1C;
	}

	public void sethash1C(String hash1C) {
		this.hash1C = hash1C.replaceAll("\\s+","");
	}
	
	public boolean isEmpty() {
		return uuid == null;
	}
}
