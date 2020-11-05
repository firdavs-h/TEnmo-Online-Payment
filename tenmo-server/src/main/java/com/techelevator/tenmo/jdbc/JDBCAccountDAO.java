package com.techelevator.tenmo.jdbc;


import java.math.BigDecimal;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserNotFoundException;

@Component
public class JDBCAccountDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	Account currentAcc = new Account();
	
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
	public Transfer send(Transfer t) {
		
		Transfer newTransfer = new Transfer();
		
		String sqlGetNextInt = "SELECT nextval ('seq_transfer_id'); ";
		SqlRowSet nextId = jdbcTemplate.queryForRowSet(sqlGetNextInt);
		nextId.next();
		int id = nextId.getInt(1);
		String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
	                 "VALUES (?, ?, ?, ?, ?, ?) ";
		SqlRowSet transfers = jdbcTemplate.queryForRowSet(sql,id, t.getTransferType());
		newTransfer =mapRowToTransfer(transfers);
		If(newTransfer.getAmount()<)
		
		return newTransfer;
		
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
	
	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<>();
		String sql = "SELECT accounts.account_id, accounts.user_id, users.username FROM accounts "
				+ "JOIN users ON users.user_id =accounts.user_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			Account account = mapRowToAccount(results);
			accounts.add(account);
			
		}
		return accounts;
	}
		
		
	private Account mapRowToAccount(SqlRowSet row) {
		
		Account account = new Account();
			account.setAccountId(row.getInt("account_id"));
			account.setUserId(row.getInt("user_id"));
			account.setUsername(row.getString("username"));
		return account;
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
	
	private Account setAccount(Principal p) {
		 
		String currentUser = p.getName();
		
		String sql ="SELECT*FROM accounts JOIN users ON users.user_id =accounts.user_id WHERE users.username =?";
		SqlRowSet temp =jdbcTemplate.queryForRowSet(sql,currentUser);
		temp.next();
		currentAcc.setAccountId(temp.getInt("account_id"));
		currentAcc.setUserId(temp.getInt("user_id"));
		currentAcc.setBalance(temp.getBigDecimal("balance"));
		
		return currentAcc;
		
	}
	
	

}
