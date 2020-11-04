package com.techelevator.tenmo.jdbc;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserNotFoundException;

public class JDBCAccountDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public BigDecimal getBalance(int userId) {
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		if(results.next()) {
			
		}
		return results.getBigDecimal("balance");
	}

	@Override
	public void send(int userId, int receiverId, BigDecimal amount) {
		String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
	                 "VALUES (DEFAULT, 2, 2, ?, ?, ?) " + 
				    "WHERE account_from = (SELECT account_id FROM accounts WHERE user_id = 1) AND " +
	                 "account_to = (SELECT account_id FROM accounts WHERE user_id = 3); ";
		SqlRowSet results  = jdbcTemplate.queryForRowSet(sql, userId, receiverId, amount);
		results.next();
		
	}

	@Override
	public List<Transfer> pastTransfers(int userId) {
		
		List<Transfer> transfers = new ArrayList<>();
		String sql = "SELECT * FROM transfers WHERE user_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		while(results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfers.add(transfer);
		}
		return transfers;
	}

	@Override
	public Transfer transferById(int userId) {
		
		Transfer transfer = null;
		String sql = "SELECT * FROM transfers WHERE user_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		
		if(results.next()) {
			transfer = mapRowToTransfer(results);
		}
		else {
			throw new UserNotFoundException();
		}
		return transfer;
	}

	@Override
	public void request(int senderId, BigDecimal amount) {
		
		
	}

	@Override
	public Transfer[] pendingTransfers(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private Transfer mapRowToTransfer(SqlRowSet rs) {
		Transfer trans = new Transfer();
		trans.setTransferId(rs.getInt("transferId"));
		trans.setTransferType(rs.getString("TransferType"));
		trans.setTransferStatus(rs.getString("TransferStatus"));
		trans.setAccountFrom(rs.getInt("accountFrom"));
		trans.setAccountTo(rs.getInt("accountTo"));
		trans.setAmount(rs.getBigDecimal("amount"));
		return trans;
	}
	
	

}
