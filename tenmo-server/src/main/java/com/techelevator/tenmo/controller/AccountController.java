package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Account;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
	private AccountDAO accountDao;
	
	public AccountController(AccountDAO accountDAO) {
		this.accountDao =accountDAO;
	
	}
	
	
	@RequestMapping (path="/balance/{id}", method = RequestMethod.GET)
	public BigDecimal getBal(@PathVariable int id) {
		return accountDao.getBalance(id);
		
	}
	

	@RequestMapping(path = "transfers/{userId}", method = RequestMethod.GET)
	public List<Transfer> pastTransfers(@PathVariable int userId) {
		return accountDao.pastTransfers(userId);
		
	}
	

	@RequestMapping(path = "transfer/{userId}",  method = RequestMethod.GET)
	public Transfer pastTransfersById(@PathVariable int userId, @RequestParam int transfer_id) {
		return accountDao.transferById(userId, transfer_id);
		
	}
	

	@RequestMapping(path = "accounts", method = RequestMethod.GET)
	public List<Account>getAllAccounts() {
		return accountDao.getAllAccounts();		
		
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "send", method = RequestMethod.POST)
	public Transfer send(@RequestBody Transfer transfer) {
		return accountDao.send(transfer);	
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "transfers/{userId}/pending", method = RequestMethod.GET)
	public Transfer[] pendingTransfers(@PathVariable int userId, @RequestParam Transfer transfer) {
		return accountDao.pendingTransfers(userId);
		
	}
	
	
	
}
