package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Account {
	
	private int accountId;
	
	@Min(value = 1, message = "The field `user Id` is more than 0")
	private int userId;
	
	@NotBlank(message = "The field `username` should not be blank.")
	private String username;
	
	@DecimalMin(value = "0.01", message = "The field `balance` should be greater than 0.")
	private BigDecimal balance;
	
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
