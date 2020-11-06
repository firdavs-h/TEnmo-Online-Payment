package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserNotFoundException;
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
	public List<Transfer> pastTransfers(@PathVariable int userId, @RequestParam(required = false) Integer status) {
		return accountDao.pastTransfers(userId, status);
		
	}
	
	

	@RequestMapping(path = "accounts", method = RequestMethod.GET)
	public List<Account>getAllAccounts() {
		return accountDao.getAllAccounts();		
		
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "create", method = RequestMethod.POST)
	public Transfer create(@Valid @RequestBody Transfer transfer) throws UserNotFoundException {
		return accountDao.createRequest(transfer);	
		
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@RequestMapping(path = "update", method = RequestMethod.PUT)
	public Transfer update(@Valid @RequestBody Transfer transfer) throws UserNotFoundException {
		return accountDao.update(transfer);	
		
	}

	
	
	
}
