package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;

@RestController
public class AccountController {
	private AccountDAO accountDao;
	
	public AccountController(AccountDAO accountDAO) {
		this.accountDao =accountDAO;
	
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping (path="/balance/{id}", method = RequestMethod.GET)
	public BigDecimal getBal(@PathVariable int id) {
		return accountDao.getBalance(id);
		
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
	public void pastTransfers(@PathVariable int userId) {
		System.out.println(accountDao.pastTransfers(userId));
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "transfers/{userId}?transfer_id=")
	public void pastTransfersById(@PathVariable int userId, @RequestParam int transfer_id) {
		System.out.println(accountDao.pastTransfers(userId));
		
		
		
	}
	
	//@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "accounts", method = RequestMethod.GET)
	public List<Account>getAllAccounts() {
		return accountDao.getAllAccounts();		
		
		
	}
	//@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "send", method = RequestMethod.POST)
	public Transfer send(@RequestBody Transfer transfer) {
		return accountDao.send(transfer);
		
		
	}
	
	
	
	
	
}
