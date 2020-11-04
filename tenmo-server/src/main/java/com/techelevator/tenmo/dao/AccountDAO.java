package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal getBalance(int userId);
	void send(int userId, int receiverId, BigDecimal amount);
	List<Transfer> pastTransfers(int userId);
	Transfer transferById(int userId);
	void request(int senderId, BigDecimal amount);
	Transfer[] pendingTransfers(int userId);

}
