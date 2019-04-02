package com.CNVZ.MyBatis.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias(value="User")
public class User implements Serializable {
	private static final long serialVersionUID=1L;
	
	private Integer id;
	private String username;
	private String password;
	private Double account;
	
	public User() {
		super();
	}
	
	public User(String username, String password, Double account) {
		super();
		this.username = username;
		this.password = password;
		this.account = account;
	}
	
	public User(Integer id, String username, String password, Double account) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.account = account;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Double getAccount() {
		return this.account;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAccount(Double account) {
		this.account = account;
	}
	
	@Override
	public String toString() {
		return "User[id=" + id + ",username=" + username + 
				",password=" + password + ",account=" + account + "]";
	}
	
}
