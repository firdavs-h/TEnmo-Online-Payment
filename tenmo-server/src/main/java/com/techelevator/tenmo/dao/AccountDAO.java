package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal getBalance(int userId);
	Transfer send(Transfer transfer);
	List<Transfer> pastTransfers(int userId);
	Transfer transferById(int userId,int transfer_id);
	void request(int senderId, BigDecimal amount);
	Transfer[] pendingTransfers(int userId);
	public List<Account> getAllAccounts(); 

}
