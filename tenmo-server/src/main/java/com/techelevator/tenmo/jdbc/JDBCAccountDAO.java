package com.techelevator.tenmo.jdbc;

import java.math.BigDecimal;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Transfer;

public class JDBCAccountDAO implements AccountDAO {

	@Override
	public BigDecimal getBalance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(int receiverId, BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transfer[] pastTransfers(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transfer transferById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(int senderId, BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transfer[] pendingTransfers(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
