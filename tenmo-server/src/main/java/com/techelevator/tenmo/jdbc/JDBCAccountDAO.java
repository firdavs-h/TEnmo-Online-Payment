package com.techelevator.tenmo.jdbc;

import java.math.BigDecimal;
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
		if (results.next()) {

		}
		return results.getBigDecimal("balance");
	}

	@Override
	public Transfer createRequest(Transfer t) {

		String sqlGetNextInt = "SELECT nextval ('seq_transfer_id') ";
		SqlRowSet nextId = jdbcTemplate.queryForRowSet(sqlGetNextInt);
		nextId.next();
		int id = nextId.getInt(1);

		if ((t.getAmount().compareTo(getBalanceByAccount(t.getAccountFrom())) <= 0 && t.getTransferStatus().equals(2))
				|| t.getTransferStatus().equals(1)) {

			String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) "
					+ "VALUES (?, ?, ?, ?, ?, ?) ";
			jdbcTemplate.update(sql, id, t.getTransferType(), t.getTransferStatus(), t.getAccountFrom(),
					t.getAccountTo(), t.getAmount());
			t.setTransferId(id);
			changeBalance(t);

		}

		return t;

	}

	@Override
	public List<Transfer> pastTransfers(int userId, Integer status) {

		List<Transfer> transfers = new ArrayList<>();
		SqlRowSet results;
		if (status == null) {
			String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers "
					+ "JOIN accounts a ON a.account_id = transfers.account_from OR a.account_id = transfers.account_to "
					+ "WHERE a.user_id = ? ";
			results = jdbcTemplate.queryForRowSet(sql, userId);
		} else {
			String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers "
					+ "JOIN accounts a ON a.account_id = transfers.account_from OR a.account_id = transfers.account_to "
					+ "WHERE a.user_id = ? AND transfer_status_id=?";
			results = jdbcTemplate.queryForRowSet(sql, userId, status);
		}
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfers.add(transfer);
		}
		return transfers;
	}

	@Override
	public Transfer transferById(int userId, int transfer_id) {

		Transfer transfer = null;
		String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers "
				+ "JOIN accounts a ON a.account_id = transfers.account_from OR a.account_id = transfers.account_to "
				+ "WHERE transfer_id=? AND a.user_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer_id, userId);

		if (results.next()) {
			transfer = mapRowToTransfer(results);
		} else {
			throw new UserNotFoundException();
		}
		return transfer;
	}

	@Override
	public List<Transfer> pendingTransfers(Transfer t, int userId) {

		List<Transfer> transfers = new ArrayList<>();
		String sql = "SELECT transfer_status_id = 1 WHERE account_to = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfers.add(transfer);
		}
		return transfers;
	}

	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<>();
		String sql = "SELECT accounts.account_id, accounts.user_id, users.username FROM accounts "
				+ "JOIN users ON users.user_id =accounts.user_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Account account = mapRowToAccount(results);
			accounts.add(account);

		}
		return accounts;
	}

	public Transfer update(Transfer t) {

		if (t.getAmount().compareTo(getBalanceByAccount(t.getAccountFrom())) <= 0 || t.getTransferStatus().equals(1)) {
			String sql = "UPDATE transfers SET transfer_status_id=? WHERE transfer_id=? ";
			jdbcTemplate.update(sql, t.getTransferStatus(), t.getTransferId());
			changeBalance(t);
		}
		if ( t.getTransferStatus().equals(3)) {
			String sql = "UPDATE transfers SET transfer_status_id=? WHERE transfer_id=? ";
			jdbcTemplate.update(sql, t.getTransferStatus(), t.getTransferId());
		
	}
		return t;
	}

	// helpers
	private Account mapRowToAccount(SqlRowSet row) {

		Account account = new Account();
		account.setAccountId(row.getInt("account_id"));
		account.setUserId(row.getInt("user_id"));
		account.setUsername(row.getString("username"));
		return account;
	}

	private Transfer mapRowToTransfer(SqlRowSet rs) {
		Transfer trans = new Transfer();
		trans.setTransferId(rs.getInt("transfer_id"));
		trans.setTransferType(rs.getInt("transfer_type_id"));
		trans.setTransferStatus(rs.getInt("transfer_status_id"));
		trans.setAccountFrom(rs.getInt("account_from"));
		trans.setAccountTo(rs.getInt("account_to"));
		trans.setAmount(rs.getBigDecimal("amount"));
		return trans;
	}

	public void changeBalance(Transfer t) {

		if (t.getTransferStatus() == 2) {
			String sqlSender = "UPDATE accounts SET balance = (balance - ?) WHERE account_id = ?";
			jdbcTemplate.update(sqlSender, t.getAmount(), t.getAccountFrom());
			String sqlReceiver = "UPDATE accounts SET balance = (balance + ?) WHERE account_id = ?";
			jdbcTemplate.update(sqlReceiver, t.getAmount(), t.getAccountTo());
		}

	}

	public BigDecimal getBalanceByAccount(int accountId) {

		String sql = "SELECT balance FROM accounts WHERE  account_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
		if (results.next()) {

		}
		return results.getBigDecimal("balance");
	}

}
