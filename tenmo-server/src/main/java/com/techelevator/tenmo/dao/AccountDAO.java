package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal getBalance(int userId);
	Transfer createRequest(Transfer transfer);
	List<Transfer> pastTransfers(int userId, Integer status);
	public List<Account> getAllAccounts(); 
	Transfer update(Transfer transfer);

}
