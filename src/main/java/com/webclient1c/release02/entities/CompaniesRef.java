package com.webclient1c.release02.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "companies")
public class CompaniesRef {

	@Id
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="company_code")
	private String code;
	
	@Column(name="company_name")
	private String name;

	@Column(name="inn")
	private String inn;
	
	@Column(name="kpp")
	private String kpp;

	@Column(name="hash1c")
	private String hash1c;
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getHash1c() {
		return hash1c;
	}

	public void setHash1c(String hash1c) {
		this.hash1c = hash1c.replaceAll("\\s+","");
	}

	@Override
	public String toString() {
		return "CompaniesRef [" + (uuid != null ? "uuid=" + uuid + ", " : "")
				+ (code != null ? "code=" + code + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (inn != null ? "inn=" + inn + ", " : "") + (kpp != null ? "kpp=" + kpp + ", " : "")
				+ (hash1c != null ? "hash1c=" + hash1c : "") + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, inn, uuid);
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
		CompaniesRef other = (CompaniesRef) obj;
		return Objects.equals(inn, other.inn) && Objects.equals(uuid, other.uuid);
	}
	
	public CompaniesRef() {
	// Default empty constructor	
	}
	
	public CompaniesRef(String uuid, String code, String name, String inn, String kpp, String hash1c) {
		this.uuid = uuid;
		this.code = code;
		this.name = name;
		this.inn = inn;
		this.kpp = kpp;
		this.hash1c = hash1c.replaceAll("\\s+","");
	}
	
	public boolean isEmpty() {
		return uuid == null;
	}

}