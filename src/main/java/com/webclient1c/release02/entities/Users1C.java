package com.webclient1c.release02.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users1c")
public class Users1C {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="user_name")
	private String userName;
	
	@Column(name="password")
	private String password;
	
	@Column(name="last_login")
	private LocalDateTime lastLogin;

	@Column(name="na_cl")
	private String salt;
	
	@Column(name="base64val")
	private String base64val;

	public Users1C() {
		this.userName = "Unknown user";
	}

	public String getBase64val() {
		return base64val;
	}

	public void setBase64val(String base64val) {
		this.base64val = base64val;
	}

	public void setIid(Long value ) {
		this.id = value;
	}
	
	public Long getIid() {
		return this.id;
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}
	
	public void setLastLogin(LocalDateTime newLoginDateTime) {
		this.lastLogin = newLoginDateTime;
	}
	
	public LocalDateTime getLastLogin() {
		return this.lastLogin;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}