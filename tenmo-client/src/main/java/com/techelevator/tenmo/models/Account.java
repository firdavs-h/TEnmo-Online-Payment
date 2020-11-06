package com.techelevator.tenmo.models;

import java.math.BigDecimal;
public class Account {
	
	private Integer accountId;
	private Integer userId;
	private String username;
	private BigDecimal balance;
	
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	

	
}
