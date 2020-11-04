package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal getBalance();
	void send(int receiverId, BigDecimal amount);
	Transfer[] pastTransfers(int userId);
	Transfer transferById(int userId);
	void request(int senderId, BigDecimal amount);
	Transfer[] pendingTransfers(int userId);

}
